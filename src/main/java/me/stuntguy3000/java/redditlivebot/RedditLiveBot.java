package me.stuntguy3000.java.redditlivebot;


import lombok.Getter;
import me.stuntguy3000.java.redditlivebot.handler.CommandHandler;
import me.stuntguy3000.java.redditlivebot.handler.ConfigHandler;
import me.stuntguy3000.java.redditlivebot.handler.UpdateHandler;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.util.LogHandler;
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
    private CommandHandler commandHandler = new CommandHandler();
    @Getter
    private ConfigHandler configHandler;
    @Getter
    private RedditClient redditClient;
    private Thread updaterThread;

    public void connectReddit() {
        LogHandler.log("Connecting to Reddit...");
        UserAgent myUserAgent = UserAgent.of("telegram", "me.stuntguy3000.java.redditlivebot", "1", configHandler.getBotSettings().getRedditUsername());

        if (redditClient != null) {
            try {
                LogHandler.log("Deauthenticating...");
                redditClient.deauthenticate();
            } catch (Exception ex) {
                LogHandler.log("Deauthenticating Failed!");
            }
        }

        redditClient = new RedditClient(myUserAgent);

        Credentials credentials = Credentials.script(configHandler.getBotSettings().getRedditUsername(), configHandler.getBotSettings().getRedditPassword(), configHandler.getBotSettings().getRedditAppID(), configHandler.getBotSettings().getRedditAppSecret());
        try {
            OAuthData authData = redditClient.getOAuthHelper().easyAuth(credentials);
            redditClient.authenticate(authData);

            LogHandler.log("Connected to Reddit. Username: " + redditClient.me().getFullName());
        } catch (OAuthException e) {
            e.printStackTrace();
        }
    }

    private void connectTelegram() {
        LogHandler.log("Connecting to Telegram...");
        new TelegramHook(configHandler.getBotSettings().getTelegramKey(), this);
    }

    public static void main(String[] args) {
        new RedditLiveBot().main();
    }

    private void main() {
        instance = this;
        configHandler = new ConfigHandler();

        if (this.getConfigHandler().getBotSettings().getAutoUpdater()) {
            LogHandler.log("Starting auto updater...");
            Thread updater = new Thread(new UpdateHandler(this, "RedditLiveBot"));
            updater.start();
            updaterThread = updater;
        } else {
            LogHandler.log("** Auto Updater is set to false **");
        }

        try {
            BUILD = Integer.parseInt(FileUtils.readFileToString(new File("build")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        connectReddit();
        connectTelegram();

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
                    System.exit(0);
                    return;
                }
                case "admins": {
                    LogHandler.log("Admins: " + configHandler.getBotSettings().getTelegramAdmins());
                    continue;
                }
                case "broadcast": {
                    Chat chat = TelegramBot.getChat("@RedditLive");
                    chat.sendMessage(SendableTextMessage.builder().message("*(Broadcast)* " + in.substring(10)).parseMode(ParseMode.MARKDOWN).build(), TelegramHook.getBot());
                    continue;
                }
                default: {
                    LogHandler.log("Unknown command!");
                }
            }
        }
    }

    public void sendToAdmins(String message) {
        for (int admin : configHandler.getBotSettings().getTelegramAdmins()) {
            TelegramBot.getChat(admin).sendMessage(message, TelegramHook.getBot());
        }
    }

    public void stopUpdater() {
        updaterThread.interrupt();
    }
}
    