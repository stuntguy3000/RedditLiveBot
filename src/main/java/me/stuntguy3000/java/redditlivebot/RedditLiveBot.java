package me.stuntguy3000.java.redditlivebot;


import lombok.Getter;
import me.stuntguy3000.java.redditlivebot.handler.CommandHandler;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.util.Config;
import me.stuntguy3000.java.redditlivebot.util.LogHandler;
import me.stuntguy3000.java.redditlivebot.util.Updater;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;
import org.apache.commons.io.FileUtils;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;

import java.io.File;
import java.io.IOException;

// @author Luke Anderson | stuntguy3000
public class RedditLiveBot {

    public static Integer BUILD = 0;
    @Getter
    public static RedditLiveBot instance;
    @Getter
    private RedditClient redditClient;
    @Getter
    private Config config;
    @Getter
    private CommandHandler commandHandler = new CommandHandler();

    public static void main(String[] args) {
        new RedditLiveBot().main();
    }

    public void main() {
        instance = this;
        config = new Config();

        try {
            BUILD = Integer.parseInt(FileUtils.readFileToString(new File("build")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        connectReddit();
        connectTelegram();

        new Thread(new Updater(this)).start();

        while (true) {
            String in = System.console().readLine();
            switch ((in.contains(" ") ? in.toLowerCase().split(" ")[0] : in.toLowerCase())) {
                case "count": {
                    LogHandler.log("Live feed count: " + TelegramHook.getLiveFeedHandler().getCount());
                    continue;
                }
                case "stoplive": {
                    TelegramHook.getLiveFeedHandler().stopAll();
                    continue;
                }
                case "botfather": {
                    LogHandler.log(commandHandler.getBotFatherString());
                    continue;
                }
                case "stop": {
                    TelegramHook.getLiveFeedHandler().stopAll();
                    System.exit(0);
                    return;
                }
                case "admins": {
                    LogHandler.log("Admins: " + config.getBotSettings().getTelegramAdmins());
                    continue;
                }
                case "broadcast": {
                    Chat chat = TelegramBot.getChat("@RedditLive");
                    chat.sendMessage(
                            SendableTextMessage.builder()
                                    .message("*(Broadcast)* " + in.substring(10))
                                    .parseMode(ParseMode.MARKDOWN)
                                    .build()
                            , TelegramHook.getBot());
                    continue;
                }
                default: {
                    LogHandler.log("Unknown command! Commands: count, stoplive, stop, botfather, broadcast");
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

    public void sendToAdmins(String message) {
        for (int admin : config.getBotSettings().getTelegramAdmins()) {
            TelegramBot.getChat(admin).sendMessage(message, TelegramHook.getBot());
        }
    }
}
    