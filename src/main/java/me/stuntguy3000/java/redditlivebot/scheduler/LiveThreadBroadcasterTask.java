package me.stuntguy3000.java.redditlivebot.scheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.UUID;

import lombok.Getter;
import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.handler.RedditHandler;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.object.Lang;
import me.stuntguy3000.java.redditlivebot.object.reddit.LiveThread;
import me.stuntguy3000.java.redditlivebot.object.reddit.livethread.LiveThreadChildren;
import me.stuntguy3000.java.redditlivebot.object.reddit.livethread.LiveThreadChildrenData;

// @author Luke Anderson | stuntguy3000
public class LiveThreadBroadcasterTask extends TimerTask {
    @Getter
    private RedditLiveBot plugin;
    @Getter
    private long lastPost = -1;
    @Getter
    private List<UUID> alreadyPosted = new ArrayList<>();
    @Getter
    private String threadID;

    public LiveThreadBroadcasterTask(String threadID, Long lastPost) {
        this.lastPost = lastPost;
        this.plugin = RedditLiveBot.getInstance();
        this.threadID = threadID;

        new Timer().schedule(this, 0, 3 * 1000);
    }

    private void postUpdate(LiveThreadChildrenData data) {
        if (data != null && !alreadyPosted.contains(data.getId())) {
            lastPost = data.getCreated_utc();
            alreadyPosted.add(data.getId());

            String author = data.getAuthor();
            String body = data.getBody();

            if (author.contains("/") || author.contains("_") || author.contains("*")
                    || body.contains("/") || body.contains("_") || body.contains("*")) {
                Lang.sendHtml(TelegramHook.getRedditLiveChat(),
                        Lang.LIVE_THREAD_UPDATE_HTML, getThreadID(), author, body);

                RedditLiveBot.getInstance().getSubscriptionHandler().broadcastHtml(
                        getThreadID(), author, body);
            } else {
                Lang.send(TelegramHook.getRedditLiveChat(),
                        Lang.LIVE_THREAD_UPDATE, getThreadID(), author, body);

                RedditLiveBot.getInstance().getSubscriptionHandler().broadcast(
                        getThreadID(), author, body);
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

                if (!alreadyPosted.contains(data.getId()) && data.getCreated_utc() > lastPost) {
                    updates.add(data);
                }
            }


            if (lastPost == -1) {
                postUpdate(updates.get(0));
            } else {
                if (updates.isEmpty()) {
                    long secs = (new Date().getTime()) / 1000;

                    // Older than 6 hours?
                    if ((secs - lastPost) > 21600) {
                        plugin.getRedditHandler().stopLiveThread();
                    }
                } else {
                    TreeMap<Long, LiveThreadChildrenData> sortedData = new TreeMap<>();

                    for (LiveThreadChildrenData data : updates) {
                        sortedData.put(data.getCreated_utc(), data);
                    }

                    double actualLastPost = lastPost;

                    sortedData.descendingMap().entrySet().stream().filter(data -> data.getValue().getCreated_utc() > actualLastPost).forEachOrdered(data -> {
                        postUpdate(data.getValue());
                    });
                }
            }
        } catch (Exception e) {
            Lang.sendDebug("Exception Caught: %s", e.getMessage());
            e.printStackTrace();
        }
    }
}
    