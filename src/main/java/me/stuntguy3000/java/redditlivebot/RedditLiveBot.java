package me.stuntguy3000.java.redditlivebot;


import lombok.Getter;
import me.stuntguy3000.java.redditlivebot.handler.CommandHandler;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.util.Config;
import me.stuntguy3000.java.redditlivebot.util.LogHandler;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;

import java.io.IOException;

// @author Luke Anderson | stuntguy3000
public class RedditLiveBot {

    @Getter
    public static final String VERSION = "1.1";
    @Getter
    public static RedditClient redditClient;
    @Getter
    public static Config config;
    @Getter
    private CommandHandler commandHandler = new CommandHandler();

    public static void main(String[] args) {
        new RedditLiveBot().main();
    }

    public void main() {
        try {
            config = new Config();
            config.loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }

        connectReddit();
        connectTelegram();

        while (true) {
            String in = System.console().readLine();
            switch (in.toLowerCase()) {
                case "count": {
                    LogHandler.log("Live feed count: " + TelegramHook.getLiveFeedHandler().getCount());
                    continue;
                }
                case "stoplive": {
                    TelegramHook.getLiveFeedHandler().stopAll();
                    continue;
                }
                case "botfather": {
                    LogHandler.logn(commandHandler.getBotFatherString());
                    continue;
                }
                case "stop": {
                    TelegramHook.getLiveFeedHandler().stopAll();
                    System.exit(0);
                    return;
                }
                case "admins": {
                    LogHandler.log("Admins: ", RedditLiveBot.getConfig().getBotSettings().getTelegramAdmins());
                    continue;
                }
                default: {
                    LogHandler.log("Unknown command! Commands: count, stoplive, stop, botfather1");
                }
            }
        }
    }

    private void connectTelegram() {
        LogHandler.log("Connecting to Telegram...");
        new TelegramHook(config.getBotSettings().getTelegramKey(), this);
    }

    private void connectReddit() {
        LogHandler.log("Connecting to Reddit...");
        UserAgent myUserAgent = UserAgent.of("telegram", "me.stuntguy3000.java.redditlivebot", "1", config.getBotSettings().getRedditUsername());
        redditClient = new RedditClient(myUserAgent);
        Credentials credentials = Credentials.script(
                config.getBotSettings().getRedditUsername(),
                config.getBotSettings().getRedditPassword(),
                config.getBotSettings().getRedditAppID(),
                config.getBotSettings().getRedditAppSecret());
        try {
            OAuthData authData = redditClient.getOAuthHelper().easyAuth(credentials);
            redditClient.authenticate(authData);

            LogHandler.log("Connected to Reddit. Username: " + redditClient.me().getFullName());
        } catch (OAuthException e) {
            e.printStackTrace();
        }
    }
}
    