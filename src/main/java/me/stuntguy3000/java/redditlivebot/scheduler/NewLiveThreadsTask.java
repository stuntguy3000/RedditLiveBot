package me.stuntguy3000.java.redditlivebot.scheduler;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.handler.RedditHandler;
import me.stuntguy3000.java.redditlivebot.object.Lang;
import me.stuntguy3000.java.redditlivebot.object.reddit.RedditThread;
import me.stuntguy3000.java.redditlivebot.object.reddit.redditthread.RedditThreadChildren;
import me.stuntguy3000.java.redditlivebot.object.reddit.redditthread.RedditThreadChildrenData;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

// @author Luke Anderson | stuntguy3000
public class NewLiveThreadsTask extends TimerTask {
    public NewLiveThreadsTask() {
        new Timer().schedule(this, 0, 600 * 1000);
    }

    @Override
    public void run() {
        Lang.sendDebug("Checking for new live threads...");

        try {
            RedditThread redditThread = RedditHandler.getThread("live");

            if (redditThread != null) {
                for (RedditThreadChildren threadChild : redditThread.getData().getChildren()) {
                    RedditThreadChildrenData threadData = threadChild.getData();

                    long secs = (new Date().getTime()) / 1000;
                    String threadID = threadData.getMedia().getEvent_id();

                    // Score more than 20, up to 3 hours old.
                    if (threadData.getScore() >= 20 &&
                            (secs - threadData.getCreated_utc() < 10800) &&
                            !RedditLiveBot.getInstance().getConfigHandler().getBotSettings().getKnownLiveFeeds().contains(threadID.toLowerCase())) {
                        Lang.sendDebug("Following thread %s.", threadID);
                        RedditLiveBot.getInstance().getRedditHandler().startLiveThread(threadData);
                        return;
                    }
                }
            }

            Lang.sendDebug("Nothing new!");
        } catch (Exception e) {
            Lang.sendDebug("Exception Caught: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
    