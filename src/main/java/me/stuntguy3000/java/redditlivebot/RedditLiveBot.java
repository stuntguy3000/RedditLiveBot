/*
 * MIT License
 *
 * Copyright (c) 2016 Luke Anderson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.stuntguy3000.java.redditlivebot;

import lombok.Data;
import me.stuntguy3000.java.redditlivebot.handler.*;
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

    /**
     * Checks to see if a user is an administrator.
     * @param user
     * @return
     */
    public boolean isAdmin(long user) {
        return getConfigHandler().getBotSettings().getTelegramAdmins().contains(user);
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
    