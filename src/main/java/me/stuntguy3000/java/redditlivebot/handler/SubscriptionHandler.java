package me.stuntguy3000.java.redditlivebot.handler;

import java.util.ArrayList;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.object.Lang;
import me.stuntguy3000.java.redditlivebot.object.config.Subscriber;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.user.User;

// @author Luke Anderson | stuntguy3000
public class SubscriptionHandler {
    private RedditLiveBot plugin;

    public SubscriptionHandler() {
        this.plugin = RedditLiveBot.instance;
    }

    public ArrayList<Subscriber> getSubscriptions() {
        return plugin.getConfigHandler().getSubscriptions().getSubscriptions();
    }

    public boolean isSubscribed(Chat chat) {
        for (Subscriber subscriber : getSubscriptions()) {
            if (subscriber.getUserID().equals(chat.getId())) {
                return true;
            }
        }

        return false;
    }

    public void subscribeChat(Chat chat) {
        plugin.getConfigHandler().getSubscriptions().getSubscriptions().add(new Subscriber(chat.getId(), chat.getName()));
        plugin.getConfigHandler().saveSubscriptions();
    }

    public void subscribeChat(User user) {
        plugin.getConfigHandler().getSubscriptions().getSubscriptions().add(new Subscriber(String.valueOf(user.getId()), user.getUsername()));
        plugin.getConfigHandler().saveSubscriptions();
    }

    public void unsubscribeChat(Subscriber subscriberToRemove) {
        for (Subscriber subscriber : new ArrayList<>(getSubscriptions())) {
            if (subscriber.getUserID().equals(subscriberToRemove.getUserID())) {
                unsubscribeChat(subscriber.getUserID());
            }
        }

        plugin.getConfigHandler().saveSubscriptions();
    }

    public void broadcast(String threadID, String author, String body) {
        for (Subscriber subscriber : new ArrayList<>(getSubscriptions())) {
            Chat chat = TelegramHook.getBot().getChat(subscriber.getUserID());

            if (chat != null) {
                Lang.send(chat,
                        Lang.LIVE_THREAD_UPDATE, threadID, author, body);
            } else {
                unsubscribeChat(subscriber);
            }
        }
    }

    public void broadcastHtml(String threadID, String author, String body) {
        for (Subscriber subscriber : new ArrayList<>(getSubscriptions())) {
            Chat chat = TelegramHook.getBot().getChat(subscriber.getUserID());

            if (chat != null) {
                Lang.sendHtml(chat,
                        Lang.LIVE_THREAD_UPDATE_HTML, threadID, author, body);
            } else {
                unsubscribeChat(subscriber);
            }
        }
    }

    public void unsubscribeChat(String id) {
        for (Subscriber subscriber : new ArrayList<>(getSubscriptions())) {
            if (subscriber.getUserID().equals(id)) {
                unsubscribeChat(subscriber);
            }
        }

        plugin.getConfigHandler().saveSubscriptions();
    }
}
    