package me.stuntguy3000.java.redditlivebot.scheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.UUID;

import lombok.Data;
import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.handler.RedditHandler;
import me.stuntguy3000.java.redditlivebot.object.Lang;
import me.stuntguy3000.java.redditlivebot.object.reddit.LiveThread;
import me.stuntguy3000.java.redditlivebot.object.reddit.livethread.LiveThreadChildren;
import me.stuntguy3000.java.redditlivebot.object.reddit.livethread.LiveThreadChildrenData;

// @author Luke Anderson | stuntguy3000
@Data
public class LiveThreadBroadcasterTask extends TimerTask {
    private final RedditLiveBot plugin;
    private final String threadID;
    private long lastPost = -1;
    private LiveThreadChildrenData lastActualPost = null;
    private List<UUID> alreadyPosted = new ArrayList<>();

    public LiveThreadBroadcasterTask(String threadID, Long lastPost) {
        this.lastPost = lastPost;
        this.plugin = RedditLiveBot.instance;
        this.threadID = threadID;

        new Timer().schedule(this, 0, 3 * 1000);
    }

    private void postUpdate(LiveThreadChildrenData data) {
        if (data != null && !alreadyPosted.contains(data.getId())) {
            lastPost = data.getCreated_utc();
            lastActualPost = data;
            alreadyPosted.add(data.getId());

            RedditLiveBot.instance.getRedditHandler().postLiveThreadUpdate(data, threadID);
        }
    }

    @Override
    public void run() {
        try {
            LiveThread liveThread = RedditHandler.getLiveThread(threadID);

            if (liveThread != null) {
                LinkedList<LiveThreadChildrenData> updates = new LinkedList<>();

                for (LiveThreadChildren liveThreadChild : liveThread.getData().getChildren()) {
                    LiveThreadChildrenData data = liveThreadChild.getData();

                    if (!alreadyPosted.contains(data.getId()) && data.getCreated_utc() > lastPost) {
                        updates.add(data);
                    }
                }


                if (lastPost == -1) {
                    LiveThreadChildrenData lastUpdate = updates.get(0);

                    long secs = (new Date().getTime()) / 1000;

                    // Older than 6 hours?
                    if ((secs - lastUpdate.getCreated_utc()) > 21600) {
                        plugin.getRedditHandler().unfollowLiveThread(false);
                    }

                    postUpdate(lastUpdate);
                } else {
                    if (updates.isEmpty()) {
                        long secs = (new Date().getTime()) / 1000;

                        // Older than 6 hours?
                        if ((secs - lastPost) > 21600) {
                            plugin.getRedditHandler().unfollowLiveThread(false);
                        }
                    } else {
                        TreeMap<Long, LiveThreadChildrenData> sortedData = new TreeMap<>();

                        for (LiveThreadChildrenData data : updates) {
                            sortedData.put(data.getCreated_utc(), data);
                        }

                        sortedData.values().forEach(this::postUpdate);
                    }
                }
            }
        } catch (Exception e) {
            Lang.sendDebug("Exception Caught: %s", e.getMessage());
            e.printStackTrace();
        }
    }
}
    