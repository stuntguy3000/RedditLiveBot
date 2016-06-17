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
import me.stuntguy3000.java.redditlivebot.object.reddit.RedditThread;
import me.stuntguy3000.java.redditlivebot.object.reddit.livethread.LiveThreadChildrenData;
import me.stuntguy3000.java.redditlivebot.object.reddit.redditthread.RedditThreadChildrenData;
import me.stuntguy3000.java.redditlivebot.scheduler.LiveThreadBroadcasterTask;
import me.stuntguy3000.java.redditlivebot.scheduler.RedditScannerTask;

// @author Luke Anderson | stuntguy3000
public class RedditHandler {
    private static final String USER_AGENT = "me.stuntguy3000.java.redditlivebot (by /u/stuntguy3000)";
    @Getter
    private LiveThreadBroadcasterTask currentLiveThread;
    @Getter
    private RedditScannerTask redditScannerTask;

    /**
     * Constructs a new RedditHandler
     */
    public RedditHandler() {
        String existingID = RedditLiveBot.instance.getConfigHandler().getBotSettings().getCurrentLiveFeed();

        if (existingID == null) {
            Lang.sendDebug("No existing thread.");
            redditScannerTask = new RedditScannerTask();
        } else {
            Lang.sendDebug("Existing thread.");
            startLiveThread(existingID, null, RedditLiveBot.instance.getConfigHandler().getBotSettings().getLastPost());
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

    public static RedditThread getThread(String id) throws Exception {
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

            return gson.fromJson(threadPostingsJSON, RedditThread.class);
        } else {
            throw new HTTPException(htmlConnection.getResponseCode());
        }
    }

    public void startLiveThread(String id, String title) {
        startLiveThread(id, title, -1);
    }

    public void startLiveThread(String id, String title, long lastPost) {
        if (currentLiveThread != null) {
            currentLiveThread.cancel();
            currentLiveThread = null;
        }

        if (redditScannerTask != null) {
            redditScannerTask.cancel();
            redditScannerTask = null;
        }

        currentLiveThread = new LiveThreadBroadcasterTask(id, lastPost);

        RedditLiveBot.instance.getConfigHandler().addFeed(id);
        RedditLiveBot.instance.getConfigHandler().setCurrentFeed(id);

        Lang.send(TelegramHook.getRedditLiveChat(), Lang.LIVE_THREAD_START,
                (title == null ? "Unknown Title" : title), id);
    }

    public void startLiveThread(RedditThreadChildrenData threadData) {
        startLiveThread(threadData.getMedia().getEvent_id(), threadData.getTitle(), -1);
    }

    public void stopLiveThread(boolean silent) {
        if (currentLiveThread != null) {
            currentLiveThread.cancel();
            currentLiveThread = null;
        }

        if (redditScannerTask != null) {
            redditScannerTask.cancel();
            redditScannerTask = null;
        }

        redditScannerTask = new RedditScannerTask();
        RedditLiveBot.instance.getConfigHandler().setCurrentFeed(null);

        if (!silent) {
            Lang.send(TelegramHook.getRedditLiveChat(), Lang.LIVE_THREAD_STOP);
        }
    }

    public void postUpdate(LiveThreadChildrenData data, String threadID) {
        String author = data.getAuthor();
        String body = data.getBody();

        if (author.contains("/") || author.contains("_") || author.contains("*")
                || body.contains("/") || body.contains("_") || body.contains("*")) {
            Lang.sendHtml(TelegramHook.getRedditLiveChat(),
                    Lang.LIVE_THREAD_UPDATE_HTML, threadID, author, body);

            RedditLiveBot.instance.getSubscriptionHandler().broadcastHtml(
                    threadID, author, body);
        } else {
            Lang.send(TelegramHook.getRedditLiveChat(),
                    Lang.LIVE_THREAD_UPDATE, threadID, author, body);

            RedditLiveBot.instance.getSubscriptionHandler().broadcast(
                    threadID, author, body);
        }
    }
}
    