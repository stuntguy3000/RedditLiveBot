package me.stuntguy3000.java.redditlivebot.object.config;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

// @author Luke Anderson | stuntguy3000
public class BotSettings {
    @Getter
    private Boolean autoUpdater;
    @Getter
    @Setter
    private long currentFeedPost;
    @Getter
    @Setter
    private String currentLiveFeed;
    @Getter
    @Setter
    private Boolean debugMode;
    @Getter
    private List<String> knownLiveFeeds;
    @Getter
    private List<Long> telegramAdmins;
    @Getter
    private String telegramKey;

    public BotSettings() {
        this.telegramKey = "";
        this.telegramAdmins = new ArrayList<>();
        this.knownLiveFeeds = new ArrayList<>();
        this.autoUpdater = true;
        this.debugMode = false;
    }
}
    