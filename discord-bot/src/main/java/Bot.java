import events.Ping;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class Bot {
    public static void main(String[] args) {
        try {
            JDA jda = JDABuilder.createDefault
            ("token")
                    .build();

            jda.addEventListener(new Ping());
        } catch (LoginException e){
            return; }
    }
}
