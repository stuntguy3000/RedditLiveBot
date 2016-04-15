package me.stuntguy3000.java.redditlivebot.handler;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.object.Lang;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.Chat;

import java.util.ArrayList;

// @author Luke Anderson | stuntguy3000
public class SubscriptionHandler {
    private RedditLiveBot plugin;

    public SubscriptionHandler() {
        this.plugin = RedditLiveBot.getInstance();
    }

    public ArrayList<String> getSubscriptions() {
        return plugin.getConfigHandler().getSubscriptions().getSubscriptions();
    }

    public boolean isSubscribed(Chat chat) {
        return plugin.getConfigHandler().getSubscriptions().getSubscriptions().contains(chat.getId());
    }

    public void subscribeChat(Chat chat) {
        plugin.getConfigHandler().getSubscriptions().getSubscriptions().add(chat.getId());
        plugin.getConfigHandler().saveSubscriptions();
    }

    public void unsubscribeChat(String chat) {
        Lang.sendDebug("Unsubscribing chat: %s", plugin.getConfigHandler().getSubscriptions().getSubscriptions().remove(chat));
        plugin.getConfigHandler().saveSubscriptions();
    }

    public void broadcast(String threadID, String author, String body) {
        for (String chatID : getSubscriptions()) {
            Chat chat = TelegramBot.getChat(chatID);

            if (chat != null) {
                Lang.send(chat,
                        Lang.LIVE_THREAD_UPDATE, threadID, author, body);
            } else {
                unsubscribeChat(chatID);
            }
        }
    }
}
    