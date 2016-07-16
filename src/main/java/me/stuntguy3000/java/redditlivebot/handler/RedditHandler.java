package me.stuntguy3000.java.redditlivebot.handler;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import javax.xml.ws.http.HTTPException;

import lombok.Getter;
import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.object.Lang;
import me.stuntguy3000.java.redditlivebot.object.reddit.LiveThread;
import me.stuntguy3000.java.redditlivebot.object.reddit.Subreddit;
import me.stuntguy3000.java.redditlivebot.object.reddit.livethread.LiveThreadChildrenData;
import me.stuntguy3000.java.redditlivebot.scheduler.LiveThreadBroadcasterTask;
import me.stuntguy3000.java.redditlivebot.scheduler.SubredditScannerTask;
import pro.zackpollard.telegrambot.api.chat.message.Message;

// @author Luke Anderson | stuntguy3000
public class RedditHandler {
    private static final String USER_AGENT = "me.stuntguy3000.java.redditlivebot (by /u/stuntguy3000)";
    @Getter
    private LiveThreadBroadcasterTask currentLiveThread;
    @Getter
    private SubredditScannerTask subredditScanner;

    /**
     * Constructs a new RedditHandler
     */
    public RedditHandler() {
        String existingID = RedditLiveBot.instance.getConfigHandler().getBotSettings().getCurrentLiveFeed();

        if (existingID == null) {
            Lang.sendDebug("No existing thread.");
            subredditScanner = new SubredditScannerTask();
        } else {
            Lang.sendDebug("Existing thread.");
            followLiveThread(existingID, null, RedditLiveBot.instance.getConfigHandler().getBotSettings().getLastPost(), true);
        }
    }

    /**
     * Get a LiveThread object from a reddit live ID
     *
     * @param id String the Reddit Live thread ID
     *
     * @return LiveThread the live thread instance.
     */
    public static LiveThread getLiveThread(String id) throws Exception {
        String url = "https://www.reddit.com/live/" + id + ".json?limit=10";

        URL urlObject = new URL(url);

        try {
            HttpURLConnection htmlConnection = (HttpURLConnection) urlObject.openConnection();
            htmlConnection.setRequestProperty("User-Agent", USER_AGENT);
            htmlConnection.setUseCaches(false);
            htmlConnection.setConnectTimeout(2000);
            htmlConnection.setReadTimeout(2000);
            htmlConnection.connect();

            if (htmlConnection.getResponseCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(htmlConnection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                Gson gson = new Gson();
                String threadPostingsJSON = response.toString();

                return gson.fromJson(threadPostingsJSON, LiveThread.class);
            } else {
                throw new HTTPException(htmlConnection.getResponseCode());
            }
        } catch (SocketTimeoutException ex) {
            System.out.println("[ERROR] SocketTimeoutException");
            return null;
        }
    }

    /**
     * Returns a Subreddit content based upon the ID
     *
     * @param id String the subreddit's name
     *
     * @return Subreddit the associated subreddit
     */
    public static Subreddit getSubreddit(String id) throws Exception {
        String url = "https://www.reddit.com/r/" + id + "/new.json?limit=1";

        URL urlObject = new URL(url);

        HttpURLConnection htmlConnection = (HttpURLConnection) urlObject.openConnection();
        htmlConnection.setRequestProperty("User-Agent", USER_AGENT);
        htmlConnection.setUseCaches(false);
        htmlConnection.setConnectTimeout(2000);
        htmlConnection.setReadTimeout(2000);
        htmlConnection.connect();

        if (htmlConnection.getResponseCode() == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(htmlConnection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Gson gson = new Gson();
            String threadPostingsJSON = response.toString();

            return gson.fromJson(threadPostingsJSON, Subreddit.class);
        } else {
            throw new HTTPException(htmlConnection.getResponseCode());
        }
    }

    /**
     * Follow a live thread with no last post
     *
     * @param id String the live thread's ID
     * @param title String the title of the live thread
     * @param silent Boolean true to follow the live thread silently (suppress any announcement messages)
     */
    public void followLiveThread(String id, String title, boolean silent) {
        followLiveThread(id, title, -1, silent);
    }

    /**
     * Follow a live thread
     *
     * @param id String the live thread's ID
     * @param title String the title of the live thread
     * @param lastPost Long the Unix time of the last post
     * @param silent Boolean true to follow the live thread silently (suppress any announcement messages)
     */
    public void followLiveThread(String id, String title, long lastPost, boolean silent) {
        if (currentLiveThread != null) {
            currentLiveThread.cancel();
            currentLiveThread = null;
        }

        if (subredditScanner != null) {
            subredditScanner.cancel();
            subredditScanner = null;
        }

        currentLiveThread = new LiveThreadBroadcasterTask(id, lastPost);

        RedditLiveBot.instance.getConfigHandler().addKnownFeed(id);
        RedditLiveBot.instance.getConfigHandler().setCurrentFeed(id);

        if (!silent) {
            Lang.send(TelegramHook.getRedditLiveChat(), Lang.LIVE_THREAD_START,
                    (title == null ? "Unknown Title" : title), id);
        }
    }

    /**
     * Stop following a live thread
     *
     * @param silent Boolean true to suppress any messages
     */
    public void unfollowLiveThread(boolean silent) {
        if (currentLiveThread != null) {
            currentLiveThread.cancel();
            currentLiveThread = null;
        }

        if (subredditScanner != null) {
            subredditScanner.cancel();
            subredditScanner = null;
        }

        subredditScanner = new SubredditScannerTask();
        RedditLiveBot.instance.getConfigHandler().setCurrentFeed(null);

        if (!silent) {
            Lang.send(TelegramHook.getRedditLiveChat(), Lang.LIVE_THREAD_STOP);
        }
    }

    /**
     * Post an update from a Live thread
     *
     * @param data LiveThreadChildrenData the update information
     * @param threadID String the live thread's ID
     */
    public void postLiveThreadUpdate(LiveThreadChildrenData data, String threadID) {
        String author = data.getAuthor();
        String body = data.getBody();
        RedditLiveBot.instance.getSubscriptionHandler().forwardMessage(Lang.send(TelegramHook.getRedditLiveChat(),
                Lang.LIVE_THREAD_UPDATE, threadID, author, body));
    }
}
    