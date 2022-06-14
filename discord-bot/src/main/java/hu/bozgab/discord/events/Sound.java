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

        String message,command,param;
        message = event.getMessage().getContentRaw();
        try{
            command = message.substring(0,message.indexOf(" "));
            param = message.substring(message.indexOf(" ") + 1);
        } catch (StringIndexOutOfBoundsException e){
            command = message;
            param = "";
        }

        MusicManager musicManager = MusicManager.getMusicManager();

        switch (command){
            case "!join": musicManager.join(event); break;
            case "!leave": musicManager.leave(); break;
            case "!play": musicManager.play(param,event); break;
            case "!pause": musicManager.pause(); break;
            case "!continue": musicManager.resume(); break;
            case "!next": musicManager.next(); break;
        }
    }
}
