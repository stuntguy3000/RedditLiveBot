package me.stuntguy3000.java.redditlivebot;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import lombok.Getter;
import me.stuntguy3000.java.redditlivebot.handler.CommandHandler;
import me.stuntguy3000.java.redditlivebot.handler.ConfigHandler;
import me.stuntguy3000.java.redditlivebot.handler.LogHandler;
import me.stuntguy3000.java.redditlivebot.handler.RedditHandler;
import me.stuntguy3000.java.redditlivebot.handler.SubscriptionHandler;
import me.stuntguy3000.java.redditlivebot.handler.UpdateHandler;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.object.Lang;

// @author Luke Anderson | stuntguy3000
public class RedditLiveBot {
    public static int BUILD = 0;
    public static boolean DEBUG = false;
    @Getter
    public static RedditLiveBot instance;
    @Getter
    private static TelegramHook telegramHook;
    @Getter
    private CommandHandler commandHandler;
    @Getter
    private ConfigHandler configHandler;
    @Getter
    private RedditHandler redditHandler;
    @Getter
    private SubscriptionHandler subscriptionHandler;
    @Getter
    private Thread updaterThread;

    public static void main(String[] args) {
        new RedditLiveBot().main();
    }

    private void connectTelegram() {
        LogHandler.log("Connecting to Telegram...");
        DEBUG = getConfigHandler().getBotSettings().getDebugMode();
        LogHandler.log("Debug Mode is set to " + DEBUG);
        telegramHook = new TelegramHook(configHandler.getBotSettings().getTelegramKey(), this);
    }

    private void main() {
        instance = this;
        configHandler = new ConfigHandler();

        /**
         * Initialize Build Number
         */
        File build = new File("build");

        if (!build.exists()) {
            try {
                build.createNewFile();
                PrintWriter writer = new PrintWriter(build, "UTF-8");
                writer.print(0);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            BUILD = Integer.parseInt(FileUtils.readFileToString(build));
        } catch (IOException e) {
            e.printStackTrace();
        }

        LogHandler.log("======================================");
        LogHandler.log(" RedditLive build " + BUILD + " by @stuntguy3000");
        LogHandler.log("======================================");

        commandHandler = new CommandHandler();

        connectTelegram();

        if (this.getConfigHandler().getBotSettings().getAutoUpdater()) {
            LogHandler.log("Starting auto updater...");
            Thread updater = new Thread(new UpdateHandler("RedditLiveBot", "RedditLiveBot"));
            updater.start();
            updaterThread = updater;
        } else {
            LogHandler.log("** Auto Updater is set to false **");
        }

        subscriptionHandler = new SubscriptionHandler();
        redditHandler = new RedditHandler();
    }

    public void shutdown() {
        if (getRedditHandler().getCurrentLiveThread() != null) {
            configHandler.getBotSettings().setLastPost(getRedditHandler().getCurrentLiveThread().getLastPost());
            configHandler.getBotSettings().setCurrentLiveFeed(getRedditHandler().getCurrentLiveThread().getThreadID());
            Lang.sendDebug("Live current thread " + configHandler.getBotSettings().getCurrentLiveFeed());
        } else {
            Lang.sendDebug("No current thread");
            configHandler.getBotSettings().setLastPost(-1);
        }

        configHandler.saveSubscriptions();
        configHandler.saveConfig();

        System.exit(0);
    }
}
    