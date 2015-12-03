package me.stuntguy3000.java.redditlivebot.util;

import lombok.Getter;
import pro.zackpollard.telegrambot.api.chat.Chat;

import java.util.ArrayList;
import java.util.HashMap;
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
    // HashMap<Chat, LiveThread>
    @Getter
    private HashMap<String, String> activeChats;

    public BotSettings() {
        this.redditUsername = "";
        this.redditPassword = "";
        this.redditAppID = "";
        this.redditAppSecret = "";
        this.telegramKey = "";
        this.telegramAdmins = new ArrayList<>();
        this.activeChats = new HashMap<>();
    }

    public void addFeed(Chat chat, String redditThread) {
        activeChats.put(chat.getId(), redditThread);
    }

    public void removeFeed(Chat chat) {
        activeChats.remove(chat.getId());
    }
}
    