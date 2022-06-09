package hu.bozgab.discord.events;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Clear extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        try {
            String[] message = event.getMessage().getContentRaw().split(" ");
            if (message[0].equals("!clear") && message.length > 1) {
                int n = Integer.parseInt(message[1]);
                List<Message> messageList = event.getChannel().getHistory().retrievePast(n + 1).complete();
                event.getTextChannel().deleteMessages(messageList).queue();
            }
        } catch (NumberFormatException e) {
            event.getChannel().sendMessage("**Error!**\n").append(e.getMessage()).queue();
        } catch (IllegalArgumentException e){
            event.getChannel().sendMessage("**Error!**\n").append(e.getMessage()).queue();
        }
    }
}
