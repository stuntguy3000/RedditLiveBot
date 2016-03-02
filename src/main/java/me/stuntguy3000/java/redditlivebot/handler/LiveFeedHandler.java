package me.stuntguy3000.java.redditlivebot.handler;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.scheduler.LiveFeedUpdateTask;
import me.stuntguy3000.java.redditlivebot.util.LogHandler;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;

import java.util.HashMap;
import java.util.Map;

// @author Luke Anderson | stuntguy3000
public class LiveFeedHandler {
    private TelegramBot bot;
    private HashMap<String, LiveFeedUpdateTask> currentFeedTasks = new HashMap<>();

    public LiveFeedHandler(TelegramBot bot) {
        this.bot = bot;
    }

    public int getCount() {
        return currentFeedTasks.size();
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

    public void load() {
        for (Map.Entry<String, String> chat : RedditLiveBot.getInstance().getConfigHandler().getLiveThreads().getActiveChats().entrySet()) {
            Chat chatInstance = TelegramBot.getChat(chat.getKey());

            if (chatInstance != null) {
                startFeed(chatInstance, chat.getValue(), true);
            }
        }
    }

    public void startFeed(Chat chat, String redditThread, boolean loadExisting) {
        if (!loadExisting) {
            if (getFeedTimer(chat) == null) {
                chat.sendMessage(SendableTextMessage.builder().message("Starting Live Feed: " + redditThread + " in channel " + chat.getId()).build(), bot);
            } else {
                chat.sendMessage(SendableTextMessage.builder().message("A feed is already running in this channel!").build(), bot);
            }
        } else {
            LogHandler.log("Loading existing chat: " + chat.getId());
        }

        RedditLiveBot.getInstance().getConfigHandler().getLiveThreads().addFeed(chat, redditThread);
        RedditLiveBot.getInstance().getConfigHandler().saveConfig("threads.json");

        currentFeedTasks.put(chat.getId(), new LiveFeedUpdateTask(redditThread, chat, RedditLiveBot.instance.getRedditClient()));
    }

    public void stop(Chat chat) {
        if (getFeedTimer(chat) != null) {
            if (getFeedTimer(chat).cancel()) {
                currentFeedTasks.remove(chat.getId());
            } else {
                chat.sendMessage(SendableTextMessage.builder().message("Error Occurred! Contact @stuntguy3000").build(), bot);
            }
        }

        RedditLiveBot.getInstance().getConfigHandler().getLiveThreads().removeFeed(chat);
        RedditLiveBot.getInstance().getConfigHandler().saveConfig("threads.json");
    }

    public void stopAll() {
        new HashMap<>(currentFeedTasks).entrySet().stream().filter(entry -> entry.getValue().cancel()).forEach(entry -> {
            currentFeedTasks.remove(entry.getKey());
        });
    }
}
    