package me.stuntguy3000.java.redditlivebot.scheduler;

import lombok.Getter;
import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.util.LiveUpdateWrapper;
import me.stuntguy3000.java.redditlivebot.util.LogHandler;
import net.dean.jraw.RedditClient;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.LiveUpdate;
import net.dean.jraw.paginators.LiveThreadPaginator;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

// @author Luke Anderson | stuntguy3000
public class LiveFeedUpdateTask extends TimerTask {
    private Timer timer = new Timer();
    @Getter
    private String feedID;
    @Getter
    private LiveUpdate lastPost;
    private RedditClient redditClient;
    private Chat chat;

    public LiveFeedUpdateTask(String feedID, Chat chat, RedditClient redditClient) {
        this.feedID = feedID;
        this.redditClient = redditClient;
        this.chat = chat;
        load();
        timer.schedule(this, 2000, 2000);
    }

    private void load() {
        LiveThreadPaginator liveThreadPaginator = new LiveThreadPaginator(redditClient, feedID);

        if (liveThreadPaginator.hasNext()) {
            liveThreadPaginator.next();
            Listing<LiveUpdate> currentPage = liveThreadPaginator.getCurrentListing();

            if (currentPage.size() > 0) {
                lastPost = currentPage.get(0);
            }
        }
    }

    @Override
    public void run() {
        try {

            LiveThreadPaginator liveThreadPaginator = new LiveThreadPaginator(redditClient, feedID);

            if (liveThreadPaginator.hasNext()) {
                liveThreadPaginator.next();
                Listing<LiveUpdate> currentPage = liveThreadPaginator.getCurrentListing();

                if (lastPost == null) {
                    if (currentPage.size() > 0) {
                        lastPost = currentPage.get(0);
                        postUpdate(lastPost);
                    }
                } else {
                    long originalPostedAt = lastPost.getCreatedUtc().getTime();

                    HashMap<LiveUpdate, Long> unposted = new HashMap<>();

                    for (LiveUpdate liveUpdate : currentPage) {
                        long postedAt = liveUpdate.getCreatedUtc().getTime();
                        if (postedAt > originalPostedAt) {
                            unposted.put(liveUpdate, postedAt);
                        }
                    }

                    if (!unposted.isEmpty()) {
                        LiveUpdateWrapper[] updatesStorted = new LiveUpdateWrapper[unposted.size()];

                        int i = 0;
                        for (LiveUpdate newUpdate : unposted.keySet()) {
                            updatesStorted[i] = new LiveUpdateWrapper(newUpdate);
                            i++;
                        }

                        Arrays.sort(updatesStorted);

                        for (LiveUpdateWrapper liveUpdateWrapper : updatesStorted) {
                            postUpdate(liveUpdateWrapper.getLiveUpdate());
                        }

                        lastPost = updatesStorted[updatesStorted.length - 1].getLiveUpdate();
                    }
                }
            }
        } catch (Exception ex) {
            LogHandler.log("Exception caught!");
            ex.printStackTrace();
            RedditLiveBot.getInstance().sendToAdmins("Exception caught: " + ex.getLocalizedMessage());
        }
    }

    private void postUpdate(LiveUpdate liveUpdate) {
        chat.sendMessage(
                SendableTextMessage.builder()
                        .message(String.format("*(%s) New update by %s*\n%s",
                                        feedID,
                                        liveUpdate.getAuthor(),
                                        liveUpdate.getBody())
                        ).parseMode(ParseMode.MARKDOWN).build(), TelegramHook.getBot()
        );
    }
}
    