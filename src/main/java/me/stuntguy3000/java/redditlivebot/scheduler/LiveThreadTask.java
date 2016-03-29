package me.stuntguy3000.java.redditlivebot.scheduler;

import lombok.Getter;
import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.handler.RedditHandler;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.object.Lang;
import me.stuntguy3000.java.redditlivebot.object.reddit.LiveThread;
import me.stuntguy3000.java.redditlivebot.object.reddit.livethread.LiveThreadChildren;
import me.stuntguy3000.java.redditlivebot.object.reddit.livethread.LiveThreadChildrenData;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

// @author Luke Anderson | stuntguy3000
public class LiveThreadTask extends TimerTask {
    @Getter
    private RedditLiveBot plugin;
    @Getter
    private long lastPost = -1;
    @Getter
    private String threadID;

    public LiveThreadTask(String threadID) {
        this.plugin = RedditLiveBot.getInstance();
        this.threadID = threadID;

        new Timer().schedule(this, 0, 2 * 1000);
    }

    private void postUpdate(LiveThreadChildrenData data) {
        lastPost = data.getCreated_utc();
        Lang.send(TelegramHook.getRedditLiveChat(),
                Lang.LIVE_THREAD_UPDATE, getThreadID(), data.getAuthor(), data.getBody());
    }

    @Override
    public void run() {
        try {
            LiveThread liveThread = RedditHandler.getLiveThread(threadID);

            LinkedList<LiveThreadChildrenData> updates = new LinkedList<>();

            for (LiveThreadChildren liveThreadChild : liveThread.getData().getChildren()) {
                LiveThreadChildrenData data = liveThreadChild.getData();

                if (data.getCreated_utc() > lastPost) {
                    updates.add(data);
                }
            }


            if (lastPost == -1) {
                postUpdate(updates.get(0));
            } else {
                updates.forEach(this::postUpdate);
            }
        } catch (Exception e) {
            Lang.sendDebug("Exception Caught: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
    