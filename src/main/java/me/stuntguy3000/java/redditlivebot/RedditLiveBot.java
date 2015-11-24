package me.stuntguy3000.java.redditlivebot;


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

    private Config config;
    private TelegramHook telegramHook;

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

        }
    }

    private void connectTelegram() {
        System.out.println("Connecting to Telegram...");
        telegramHook = new TelegramHook(config.getTelegramKey());
    }

    private void connectReddit() {
        System.out.println("Connecting to Reddit...");
        UserAgent myUserAgent = UserAgent.of("desktop", "me.stuntguy3000.java.redditlivebot", "1", config.getRedditUsername());
        RedditClient redditClient = new RedditClient(myUserAgent);
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
        }
    }
}
    