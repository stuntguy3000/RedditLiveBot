package me.stuntguy3000.java.redditlivebot.scheduler;

import lombok.Getter;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.util.LiveUpdateWrapper;
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
    private LiveUpdate lastPostedListing;
    private RedditClient redditClient;
    private Chat chat;
    private boolean firstRun = true;

    public LiveFeedUpdateTask(String feedID, Chat chat, RedditClient redditClient) {
        this.feedID = feedID;
        this.redditClient = redditClient;
        this.chat = chat;
        timer.schedule(this, 0, 2000);
    }

    @Override
    public void run() {
        LiveThreadPaginator liveThreadPaginator = new LiveThreadPaginator(redditClient, feedID);

        if (liveThreadPaginator.hasNext()) {
            liveThreadPaginator.next();
            Listing<LiveUpdate> currentPage = liveThreadPaginator.getCurrentListing();

            if (lastPostedListing == null) {
                if (currentPage.size() > 0) {
                    lastPostedListing = currentPage.get(0);

                    if (firstRun) {
                        chat.sendMessage(
                                SendableTextMessage.builder()
                                        .message(String.format("(%s) Last update by %s\n%s", feedID,
                                                        lastPostedListing.getAuthor()
                                                        , lastPostedListing.getBody())
                                        ).parseMode(ParseMode.MARKDOWN).build(), TelegramHook.getBot()
                        );
                    }
                }
            } else {
                long originalPostedAt = lastPostedListing.getCreatedUtc().getTime();

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
                        chat.sendMessage(
                                SendableTextMessage.builder()
                                        .message(String.format("(%s) New update by %s\n%s", feedID,
                                                        liveUpdateWrapper.getLiveUpdate().getAuthor()
                                                        , liveUpdateWrapper.getLiveUpdate().getBody())
                                        ).parseMode(ParseMode.MARKDOWN).build(), TelegramHook.getBot()
                        );
                    }

                    lastPostedListing = updatesStorted[updatesStorted.length - 1].getLiveUpdate();
                }
                firstRun = false;
            }
        }
    }
}
    