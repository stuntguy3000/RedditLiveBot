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
    private LiveThreadChildrenData lastPost = null;
    @Getter
    private List<UUID> alreadyPosted = new ArrayList<>();
    @Getter
    private String threadID;

    public LiveThreadTask(String threadID, LiveThreadChildrenData lastPost) {
        this.lastPost = lastPost;
        this.plugin = RedditLiveBot.getInstance();
        this.threadID = threadID;

        new Timer().schedule(this, 0, 15 * 1000);
    }

    private void postUpdate(LiveThreadChildrenData data) {
        if (data != null && !alreadyPosted.contains(data.getId())) {
            lastPost = data;
            alreadyPosted.add(data.getId());

            Lang.send(TelegramHook.getRedditLiveChat(),
                    Lang.LIVE_THREAD_UPDATE, getThreadID(), data.getAuthor(), data.getBody());
            RedditLiveBot.getInstance().getSubscriptionHandler().broadcast(
                    getThreadID(), data.getAuthor(), data.getBody());
        }
    }

    @Override
    public void run() {
        try {
            LiveThread liveThread = RedditHandler.getLiveThread(threadID);

            LinkedList<LiveThreadChildrenData> updates = new LinkedList<>();

            for (LiveThreadChildren liveThreadChild : liveThread.getData().getChildren()) {
                LiveThreadChildrenData data = liveThreadChild.getData();

                if (lastPost != null && !data.getId().equals(lastPost.getId()) && !alreadyPosted.contains(data.getId())) {
                    Lang.sendDebug("New data");
                    updates.add(data);
                }
            }


            if (lastPost == null) {
                postUpdate(updates.get(0));
            } else {
                updates.forEach(this::postUpdate);

                if (updates.isEmpty()) {
                    long secs = (new Date().getTime()) / 1000;

                    // Older than 6 hours?
                    if ((secs - lastPost.getCreated_utc()) > 21600) {
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
    