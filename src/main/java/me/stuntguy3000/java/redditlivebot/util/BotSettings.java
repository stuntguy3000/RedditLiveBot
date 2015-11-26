package me.stuntguy3000.java.redditlivebot.util;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

// @author Luke Anderson | stuntguy3000
public class BotSettings {
    @Getter
    private String redditUsername;
    @Getter
    private String redditPassword;
    @Getter
    private String redditAppID;
    @Getter
    private String redditAppSecret;
    @Getter
    private String telegramKey;
    @Getter
    private List<Integer> telegramAdmins;

    public BotSettings() {
        this.redditUsername = "";
        this.redditPassword = "";
        this.redditAppID = "";
        this.redditAppSecret = "";
        this.telegramKey = "";
        this.telegramAdmins = new ArrayList<>();
    }
}
    