package me.stuntguy3000.java.redditlivebot.scheduler;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.handler.RedditHandler;
import me.stuntguy3000.java.redditlivebot.object.Lang;
import me.stuntguy3000.java.redditlivebot.object.reddit.LiveThread;
import me.stuntguy3000.java.redditlivebot.object.reddit.Subreddit;
import me.stuntguy3000.java.redditlivebot.object.reddit.livethread.LiveThreadChildrenData;
import me.stuntguy3000.java.redditlivebot.object.reddit.subreddit.SubredditChildren;
import me.stuntguy3000.java.redditlivebot.object.reddit.subreddit.SubredditChildrenData;

// @author Luke Anderson | stuntguy3000
public class SubredditScannerTask extends TimerTask {
    public SubredditScannerTask() {
        new Timer().schedule(this, 0, 15 * 1000);
    }

    @Override
    public void run() {
        Lang.sendDebug("Checking for new live threads...");

        try {
            Subreddit subreddit = RedditHandler.getSubreddit("live");

            if (subreddit != null) {
                for (SubredditChildren threadChild : subreddit.getData().getChildren()) {
                    SubredditChildrenData threadData = threadChild.getData();

                    if (threadData != null && threadData.getMedia() != null) {
                        long secs = (new Date().getTime()) / 1000;
                        String threadID = threadData.getMedia().getEvent_id();

                        LiveThread liveThreadData = RedditHandler.getLiveThread(threadID);
                        if (liveThreadData != null) {
                            LiveThreadChildrenData lastPost = liveThreadData.getData().getChildren().get(0).getData();

                            // Less than 1 hour old
                            if ((secs - lastPost.getCreated_utc()) < 3600) {
                                RedditLiveBot.instance.getAdminControlHandler().postNewLiveThread(threadData, threadID);
                            }
                        }
                    }
                }
            }

            Lang.sendDebug("No new posted live threads.");
        } catch (Exception e) {
            Lang.sendDebug("Exception Caught: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
    