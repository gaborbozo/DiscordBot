package hu.bozgab.discord.events;

import hu.bozgab.discord.events.music.MusicManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Sound extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;

        String[] message = event.getMessage().getContentRaw().split(" ");

        if(message[0].equals("!join")){
            MusicManager.getMusicManager().join(event);
        } else if(message[0].equals("!leave")){
            MusicManager.getMusicManager().leave();
        } else if (message[0].equals("!play") && message.length > 1){
            MusicManager.getMusicManager().play(message[1], event);
        }
    }
}
