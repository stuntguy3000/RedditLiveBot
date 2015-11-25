package me.stuntguy3000.java.redditlivebot;


import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.util.Config;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;

import java.io.IOException;

// @author Luke Anderson | stuntguy3000
public class RedditLiveBot {

    private static RedditClient redditClient;
    private Config config;
    private TelegramHook telegramHook;

    public static void main(String[] args) {
        new RedditLiveBot().main();
    }

    public static RedditClient getRedditBot() {
        return redditClient;
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

        Chat mazenchat = TelegramBot.getChat(-17349250);
        while (true) {
            String in = System.console().readLine();
            if ("quit".equals(in)) {
                break;
            }
            SendableTextMessage message = SendableTextMessage.builder().message(in).build();
            TelegramHook.getBot().sendMessage(mazenchat, message);
        }
    }

    private void connectTelegram() {
        System.out.println("Connecting to Telegram...");
        telegramHook = new TelegramHook(config.getTelegramKey());
    }

    private void connectReddit() {
        System.out.println("Connecting to Reddit...");
        UserAgent myUserAgent = UserAgent.of("desktop", "me.stuntguy3000.java.redditlivebot", "1", config.getRedditUsername());
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
        }
    }
}
    