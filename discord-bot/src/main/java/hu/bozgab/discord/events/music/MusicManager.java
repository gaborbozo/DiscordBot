package hu.bozgab.discord.events.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.URI;
import java.net.URISyntaxException;

public class MusicManager {
    private static MusicManager musicManager = null;
    AudioManager discordAudioManager;
    VoiceChannel voiceChannel;
    AudioPlayerManager audioPlayerManager;
    AudioPlayer audioPlayer;
    TrackScheduler trackScheduler;

    public static MusicManager getMusicManager(){
        if(musicManager == null)
            musicManager = new MusicManager();
        return musicManager;
    }

    private MusicManager(){
        audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);

        audioPlayer = audioPlayerManager.createPlayer();
        trackScheduler = new TrackScheduler(audioPlayer);
        audioPlayer.addListener(trackScheduler);
    }

    public void play(String trackURL, MessageReceivedEvent event){
        if(!isURI(trackURL))
            trackURL = "ytsearch:" + trackURL + " audio";

        try {
            if(!discordAudioManager.isConnected())
                join(event);
        }
        catch (NullPointerException e){
            join(event);
        }
        audioPlayerManager.loadItem(trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                trackScheduler.queue(audioTrack);

                event.getChannel().sendMessage("Adding to queue **")
                        .append(audioTrack.getInfo().author).append(" : ")
                        .append(audioTrack.getInfo().title).append("**").queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                if(audioPlaylist.getTracks().isEmpty())
                   return;

                AudioTrack audioTrack = audioPlaylist.getTracks().get(0);
                trackScheduler.queue(audioTrack);

                event.getChannel().sendMessage("Adding to queue **")
                        .append(audioTrack.getInfo().author).append(" : ")
                        .append(audioTrack.getInfo().title).append("**").queue();
            }

            @Override
            public void noMatches() {
                event.getChannel().sendMessage("Music not found!").queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                event.getChannel().sendMessage(e.getMessage()).queue();
            }
        });
    }

    public boolean isURI(String data){
        try {
            new URI(data);
            return true;
        } catch (URISyntaxException e){
            return false;
        }
    }

    public void join(MessageReceivedEvent event){
        try {
            if(discordAudioManager.isConnected()) {
                event.getChannel().sendMessage("Im already connected to **")
                        .append(discordAudioManager.getConnectedChannel().getName())
                        .append("**").queue();
                return;
            }
        } catch (NullPointerException e){
            //discordAudioManager = event.getGuild().getAudioManager();
        }

        discordAudioManager = event.getGuild().getAudioManager();
        voiceChannel = (VoiceChannel)event.getMember().getVoiceState().getChannel();
        discordAudioManager.openAudioConnection(voiceChannel);
        discordAudioManager.setSendingHandler(new AudioPlayerSendHandler(audioPlayer));
    }

    public void leave() {
        if(discordAudioManager.isConnected()){
            discordAudioManager.closeAudioConnection();
        }
        pause();
    }

    public void pause(){
        audioPlayer.setPaused(true);
    }

    public void resume() {
        audioPlayer.setPaused(false);
    }

    public void next() {
        trackScheduler.nextTrack();
    }
}
