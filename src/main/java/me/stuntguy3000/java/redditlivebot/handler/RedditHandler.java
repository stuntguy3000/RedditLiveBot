package me.stuntguy3000.java.redditlivebot.handler;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import lombok.Getter;
import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.object.Lang;
import me.stuntguy3000.java.redditlivebot.object.reddit.LiveThread;
import me.stuntguy3000.java.redditlivebot.object.reddit.RedditThread;
import me.stuntguy3000.java.redditlivebot.object.reddit.redditthread.RedditThreadChildrenData;
import me.stuntguy3000.java.redditlivebot.scheduler.LiveThreadTask;
import me.stuntguy3000.java.redditlivebot.scheduler.NewLiveThreadsTask;

import javax.xml.ws.http.HTTPException;

// @author Luke Anderson | stuntguy3000
public class RedditHandler {
    private static final String USER_AGENT = "me.stuntguy3000.java.redditlivebot:v" + RedditLiveBot.BUILD + " (by /u/stuntguy3000)";
    @Getter
    private LiveThreadTask currentLiveThread;
    @Getter
    private NewLiveThreadsTask newLiveThreadsTask;

    public RedditHandler() {
        newLiveThreadsTask = new NewLiveThreadsTask();
    }

    public static LiveThread getLiveThread(String id) throws UnirestException, HTTPException {
        GetRequest getRequest = Unirest.get("https://www.reddit.com/live/" + id + ".json?limit=5");
        getRequest.header("User-agent", USER_AGENT);

        HttpResponse<JsonNode> liveThread = getRequest.asJson();

        if (liveThread.getStatus() == 200) {
            Gson gson = new Gson();
            String threadPostingsJSON = liveThread.getBody().toString();

            return gson.fromJson(threadPostingsJSON, LiveThread.class);
        } else {
            throw new HTTPException(liveThread.getStatus());
        }
    }

    public static RedditThread getThread(String id) throws UnirestException, HTTPException {
        GetRequest getRequest = Unirest.get("https://www.reddit.com/r/" + id + "/new.json?limit=1");
        getRequest.header("User-agent", USER_AGENT);

        HttpResponse<JsonNode> redditThread = getRequest.asJson();

        if (redditThread.getStatus() == 200) {
            Gson gson = new Gson();
            String threadPostingsJSON = redditThread.getBody().toString();

            return gson.fromJson(threadPostingsJSON, RedditThread.class);
        } else {
            throw new HTTPException(redditThread.getStatus());
        }
    }

    public void startLiveThread(RedditThreadChildrenData redditThreadChildrenData) {
        startLiveThread(redditThreadChildrenData.getMedia().getEvent_id(), redditThreadChildrenData.getTitle());
    }

    public void startLiveThread(String id, String title) {
        if (currentLiveThread != null) {
            currentLiveThread.cancel();
        }

        if (newLiveThreadsTask != null) {
            newLiveThreadsTask.cancel();
        }

        currentLiveThread = new LiveThreadTask(id);

        RedditLiveBot.getInstance().getConfigHandler().addFeed(id);
        RedditLiveBot.getInstance().getConfigHandler().setCurrentFeed(id);
        Lang.send(TelegramHook.getRedditLiveChat(), Lang.LIVE_THREAD_START, title, id);
    }

    public void stopLiveThread() {
        if (currentLiveThread != null) {
            currentLiveThread.cancel();
        }

        if (newLiveThreadsTask != null) {
            newLiveThreadsTask.cancel();
        }

        newLiveThreadsTask = new NewLiveThreadsTask();
        RedditLiveBot.getInstance().getConfigHandler().setCurrentFeed(null);
        Lang.send(TelegramHook.getRedditLiveChat(), Lang.LIVE_THREAD_STOP);
    }
}
    