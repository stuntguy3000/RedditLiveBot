package me.stuntguy3000.java.redditlivebot.util;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

// @author Luke Anderson | stuntguy3000
public class BotSettings {
    @Getter
    @Setter
    private String redditUsername;
    @Getter
    @Setter
    private String redditPassword;
    @Getter
    @Setter
    private String redditAppID;
    @Getter
    @Setter
    private String redditAppSecret;
    @Getter
    @Setter
    private String telegramKey;
    @Getter
    @Setter
    private List<Integer> telegramAdmins;
}
    