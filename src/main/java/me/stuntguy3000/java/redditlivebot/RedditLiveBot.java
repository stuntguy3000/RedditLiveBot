package me.stuntguy3000.java.redditlivebot;


import lombok.Getter;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.util.Config;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;

import java.io.IOException;

// @author Luke Anderson | stuntguy3000
public class RedditLiveBot {

    @Getter
    public static RedditClient redditClient;
    @Getter
    public static String VERSION = "1.0";
    @Getter
    public Config config;

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
                    System.out.println("Live feed count: " + TelegramHook.getLiveFeedHandler().getCount());
                    continue;
                }
                case "stoplive": {
                    TelegramHook.getLiveFeedHandler().stopAll();
                    continue;
                }
                case "stop": {
                    TelegramHook.getLiveFeedHandler().stopAll();
                    System.exit(0);
                    return;
                }
                default: {
                    System.out.println("Unknown command! Commands: count, stoplive, stop");
                }
            }
        }
    }

    private void connectTelegram() {
        System.out.println("Connecting to Telegram...");
        new TelegramHook(config.getTelegramKey());
    }

    private void connectReddit() {
        System.out.println("Connecting to Reddit...");
        UserAgent myUserAgent = UserAgent.of("telegram", "me.stuntguy3000.java.redditlivebot", "1", config.getRedditUsername());
        redditClient = new RedditClient(myUserAgent);
        Credentials credentials = Credentials.script(
                config.getRedditUsername(),
                config.getRedditPassword(),
                config.getRedditAppID(),
                config.getRedditAppSecret());
        try {
            OAuthData authData = redditClient.getOAuthHelper().easyAuth(credentials);
            redditClient.authenticate(authData);

            System.out.println("Connected to Reddit. Username: " + redditClient.me().getFullName());
        } catch (OAuthException e) {
            e.printStackTrace();
            // TODO: Improve error messages, add admin PM system
        }
    }
}
    