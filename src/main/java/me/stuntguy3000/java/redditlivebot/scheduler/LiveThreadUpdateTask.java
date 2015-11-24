package me.stuntguy3000.java.redditlivebot.scheduler;

import me.stuntguy3000.java.redditlivebot.util.LiveUpdateWrapper;
import net.dean.jraw.RedditClient;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.LiveUpdate;
import net.dean.jraw.paginators.LiveThreadPaginator;

import java.util.*;

// @author Luke Anderson | stuntguy3000
public class LiveThreadUpdateTask extends TimerTask {
    private Timer timer = new Timer();
    private String threadID;
    private LiveUpdate lastPostedListing;
    private RedditClient redditClient;

    public LiveThreadUpdateTask(String threadID, RedditClient redditClient) {
        this.threadID = threadID;
        this.redditClient = redditClient;
        timer.schedule(this, 0, 10000);
    }

    @Override
    public void run() {
        LiveThreadPaginator liveThreadPaginator = new LiveThreadPaginator(redditClient, threadID);

        if (liveThreadPaginator.hasNext()) {
            liveThreadPaginator.next();
            Listing<LiveUpdate> currentPage = liveThreadPaginator.getCurrentListing();

            if (lastPostedListing == null) {
                if (currentPage.size() > 0) {
                    lastPostedListing = currentPage.get(currentPage.size() - 1);
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
                    for (Map.Entry<LiveUpdate, Long> newUpdate : unposted.entrySet()) {
                        updatesStorted[i] = new LiveUpdateWrapper(newUpdate.getKey(), newUpdate.getValue());
                        i++;
                    }

                    Arrays.sort(updatesStorted);

                    for (LiveUpdateWrapper liveUpdateWrapper : updatesStorted) {
                        System.out.println("[NEW] " + liveUpdateWrapper.getLiveUpdate().getBody());
                    }

                    lastPostedListing = updatesStorted[updatesStorted.length - 1].getLiveUpdate();
                }
            }
        }
    }
}
    