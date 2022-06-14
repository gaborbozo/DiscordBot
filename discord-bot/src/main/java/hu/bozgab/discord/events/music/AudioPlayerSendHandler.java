package hu.bozgab.discord.events.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import java.nio.Buffer;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.ByteBuffer;

public class AudioPlayerSendHandler implements AudioSendHandler {
    private final AudioPlayer audioPlayer;
    private final ByteBuffer buffer;
    private final MutableAudioFrame audioFrame;

    public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.buffer = ByteBuffer.allocate(2048);
        this.audioFrame = new MutableAudioFrame();
        this.audioFrame.setBuffer(buffer);
    }

    @Override
    public boolean canProvide() {
        return audioPlayer.provide(audioFrame);
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        ((Buffer) buffer).flip();
        return buffer;
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}