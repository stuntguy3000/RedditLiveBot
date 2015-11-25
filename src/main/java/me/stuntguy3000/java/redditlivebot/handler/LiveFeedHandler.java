package me.stuntguy3000.java.redditlivebot.handler;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.scheduler.LiveFeedUpdateTask;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;

import java.util.HashMap;
import java.util.Map;

// @author Luke Anderson | stuntguy3000
public class LiveFeedHandler {
    private HashMap<String, LiveFeedUpdateTask> currentFeedTasks = new HashMap<>();
    private TelegramBot bot;

    public LiveFeedHandler(TelegramBot bot) {
        this.bot = bot;
    }

    public LiveFeedUpdateTask getFeedTimer(Chat chat) {
        if (currentFeedTasks.containsKey(chat.getId())) {
            LiveFeedUpdateTask liveFeedUpdateTask = currentFeedTasks.get(chat.getId());

            if (liveFeedUpdateTask == null) {
                currentFeedTasks.remove(chat.getId());
            } else {
                return liveFeedUpdateTask;
            }
        }

        return null;
    }

    public void startFeed(Chat chat, String redditThread) {
        if (getFeedTimer(chat) == null) {
            chat.sendMessage(SendableTextMessage.builder().message("Starting Live Feed: " + redditThread).build(), bot);
            currentFeedTasks.put(chat.getId(), new LiveFeedUpdateTask(redditThread, chat, RedditLiveBot.getRedditBot()));
        } else {
            chat.sendMessage(SendableTextMessage.builder().message("A feed is already running in this channel!").build(), bot);
        }
    }

    public void stop(Chat chat) {
        if (getFeedTimer(chat) != null) {
            if (getFeedTimer(chat).cancel()) {
                currentFeedTasks.remove(chat.getId());
            } else {
                chat.sendMessage(SendableTextMessage.builder().message("Error Occurred! Contact @stuntguy3000").build(), bot);
            }
        }
    }

    public int getCount() {
        return currentFeedTasks.size();
    }

    public void stopAll() {
        for (Map.Entry<String, LiveFeedUpdateTask> entry : new HashMap<>(currentFeedTasks).entrySet()) {
            if (entry.getValue().cancel()) {
                currentFeedTasks.remove(entry.getKey());
            }
        }
    }
}
    