package hu.bozgab.discord.events.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer audioPlayer;
    private final BlockingQueue<AudioTrack> audioTracks;

    public TrackScheduler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.audioTracks = new LinkedBlockingQueue<>();
    }

    public BlockingQueue<AudioTrack> getAudioTracks() {
        return audioTracks;
    }

    public void queue(AudioTrack track) {
        if (!audioPlayer.startTrack(track, true)) {
            audioTracks.offer(track);
        }
    }

    public void nextTrack() {
        audioPlayer.startTrack(audioTracks.poll(), false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
                nextTrack();
        }
    }



    @Override
    public void onPlayerPause(AudioPlayer player) {
        System.out.println("Paused");
    }
}