package me.stuntguy3000.java.redditlivebot;

import lombok.Data;
import me.stuntguy3000.java.redditlivebot.handler.AdminControlHandler;
import me.stuntguy3000.java.redditlivebot.handler.CommandHandler;
import me.stuntguy3000.java.redditlivebot.handler.ConfigHandler;
import me.stuntguy3000.java.redditlivebot.handler.JenkinsUpdateHandler;
import me.stuntguy3000.java.redditlivebot.handler.LogHandler;
import me.stuntguy3000.java.redditlivebot.handler.PaginationHandler;
import me.stuntguy3000.java.redditlivebot.handler.RedditHandler;
import me.stuntguy3000.java.redditlivebot.handler.SubscriptionHandler;
import me.stuntguy3000.java.redditlivebot.handler.ThreadExecutionHandler;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.object.Lang;

// @author Luke Anderson | stuntguy3000
@Data
public class RedditLiveBot {
    public static boolean DEBUG = false;
    public static RedditLiveBot instance;
    private static TelegramHook telegramHook;
    private AdminControlHandler adminControlHandler;
    private CommandHandler commandHandler;
    private ConfigHandler configHandler;
    private JenkinsUpdateHandler jenkinsUpdateHandler;
    private PaginationHandler paginationHandler;
    private RedditHandler redditHandler;
    private SubscriptionHandler subscriptionHandler;
    private ThreadExecutionHandler threadExecutionHandler;

    public static void main(String[] args) {
        new RedditLiveBot().main();
    }

    private void connectTelegram() {
        LogHandler.log("Connecting to Telegram...");
        telegramHook = new TelegramHook(configHandler.getBotSettings().getTelegramKey(), this);
    }

    private void main() {
        instance = this;
        configHandler = new ConfigHandler();

        DEBUG = getConfigHandler().getBotSettings().getDebugMode();
        LogHandler.log("Debug Mode is set to " + DEBUG);

        if (this.getConfigHandler().getBotSettings().getAutoUpdater()) {
            LogHandler.log("Starting auto updater...");
            jenkinsUpdateHandler = new JenkinsUpdateHandler(
                    "RedditLiveBot", "http://ci.zackpollard.pro/job/",
                    "RedditLiveBot.jar", 10000
            );

            try {
                jenkinsUpdateHandler.startUpdater();
            } catch (JenkinsUpdateHandler.JenkinsUpdateException e) {
                e.printStackTrace();
            }
        } else {
            LogHandler.log("** Auto Updater is set to false **");
        }

        connectTelegram();

        commandHandler = new CommandHandler();
        adminControlHandler = new AdminControlHandler();
        subscriptionHandler = new SubscriptionHandler();
        redditHandler = new RedditHandler();
        paginationHandler = new PaginationHandler();
        threadExecutionHandler = new ThreadExecutionHandler();

        TelegramHook.initializeCommands();
    }

    public void shutdown() {
        if (getRedditHandler().getCurrentLiveThread() != null) {
            configHandler.getBotSettings().setLastPost(getRedditHandler().getCurrentLiveThread().getLastPost());
            configHandler.getBotSettings().setCurrentLiveFeed(getRedditHandler().getCurrentLiveThread().getThreadID());
            Lang.sendDebug("Live current thread - Feed ID: " + configHandler.getBotSettings().getCurrentLiveFeed());
        } else {
            Lang.sendDebug("No current thread");
            configHandler.getBotSettings().setLastPost(-1);
        }

        configHandler.saveConfigs();

        System.exit(0);
    }
}
    