package hu.bozgab.discord;

import hu.bozgab.discord.events.Clear;
import hu.bozgab.discord.events.Sound;
import hu.bozgab.discord.events.Ping;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class Bot {
    public static void main(String[] args) {
        try {
            JDA jda = JDABuilder.createDefault
            (args[0])
                    .build();

            jda.addEventListener(new Ping());
            jda.addEventListener(new Clear());
            jda.addEventListener(new Sound());
        } catch (LoginException e){
            return; }
    }
}
