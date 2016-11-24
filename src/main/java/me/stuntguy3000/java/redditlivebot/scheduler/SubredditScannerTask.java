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

package me.stuntguy3000.java.redditlivebot.scheduler;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.handler.RedditHandler;
import me.stuntguy3000.java.redditlivebot.object.Lang;
import me.stuntguy3000.java.redditlivebot.object.reddit.LiveThread;
import me.stuntguy3000.java.redditlivebot.object.reddit.Subreddit;
import me.stuntguy3000.java.redditlivebot.object.reddit.livethread.LiveThreadChildrenData;
import me.stuntguy3000.java.redditlivebot.object.reddit.subreddit.SubredditChildren;
import me.stuntguy3000.java.redditlivebot.object.reddit.subreddit.SubredditChildrenData;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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
    