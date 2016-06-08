package me.stuntguy3000.java.redditlivebot.object.config;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

// @author Luke Anderson | stuntguy3000
@Data
public class BotSettings {
    private Boolean autoUpdater;
    private long lastPost;
    private String currentLiveFeed;
    private Boolean debugMode;
    private List<String> knownLiveFeeds;
    private List<Long> telegramAdmins;
    private String telegramKey;

    public BotSettings() {
        this.telegramKey = "";
        this.telegramAdmins = new ArrayList<>();
        this.knownLiveFeeds = new ArrayList<>();
        this.autoUpdater = true;
        this.debugMode = false;
    }
}
    