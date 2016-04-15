package me.stuntguy3000.java.redditlivebot.scheduler;

import lombok.Getter;
import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.handler.RedditHandler;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.object.Lang;
import me.stuntguy3000.java.redditlivebot.object.reddit.LiveThread;
import me.stuntguy3000.java.redditlivebot.object.reddit.livethread.LiveThreadChildren;
import me.stuntguy3000.java.redditlivebot.object.reddit.livethread.LiveThreadChildrenData;

import java.util.*;

// @author Luke Anderson | stuntguy3000
public class LiveThreadTask extends TimerTask {
    @Getter
    private RedditLiveBot plugin;
    @Getter
    private long lastPost = -1;
    @Getter
    private List<UUID> alreadyPosted = new ArrayList<>();
    @Getter
    private String threadID;

    public LiveThreadTask(String threadID, Long lastPost) {
        this.lastPost = lastPost;
        this.plugin = RedditLiveBot.getInstance();
        this.threadID = threadID;

        new Timer().schedule(this, 0, 15 * 1000);
    }

    private void postUpdate(LiveThreadChildrenData data) {
        if (data != null && !alreadyPosted.contains(data.getId())) {
            if (data.getCreated_utc() > lastPost) {
                lastPost = data.getCreated_utc();
                alreadyPosted.add(data.getId());

                Lang.send(TelegramHook.getRedditLiveChat(),
                        Lang.LIVE_THREAD_UPDATE, getThreadID(), data.getAuthor(), data.getBody());
                RedditLiveBot.getInstance().getSubscriptionHandler().broadcast(
                        getThreadID(), data.getAuthor(), data.getBody());
            } else {
                Lang.sendDebug("Time check failed! Post: %s Last: %s PostID: %s", data.getCreated_utc(), lastPost, data.getId());
            }
        }
    }

    @Override
    public void run() {
        try {
            LiveThread liveThread = RedditHandler.getLiveThread(threadID);

            LinkedList<LiveThreadChildrenData> updates = new LinkedList<>();

            for (LiveThreadChildren liveThreadChild : liveThread.getData().getChildren()) {
                LiveThreadChildrenData data = liveThreadChild.getData();

                if (!alreadyPosted.contains(data.getId())) {
                    updates.add(data);
                }
            }


            if (lastPost == -1) {
                postUpdate(updates.get(0));
            } else {
                updates.forEach(this::postUpdate);

                if (updates.isEmpty()) {
                    long secs = (new Date().getTime()) / 1000;

                    // Older than 6 hours?
                    if ((secs - lastPost) > 21600) {
                        plugin.getRedditHandler().stopLiveThread();
                    }
                }
            }
        } catch (Exception e) {
            Lang.sendDebug("Exception Caught: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
    