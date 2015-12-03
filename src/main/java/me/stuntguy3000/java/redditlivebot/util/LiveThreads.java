package me.stuntguy3000.java.redditlivebot.util;

import lombok.Getter;
import pro.zackpollard.telegrambot.api.chat.Chat;

import java.util.HashMap;

// @author Luke Anderson | stuntguy3000
public class LiveThreads {
    @Getter
    private HashMap<String, String> activeChats = new HashMap<>();

    public void addFeed(Chat chat, String redditThread) {
        activeChats.put(chat.getId(), redditThread);
    }

    public void removeFeed(Chat chat) {
        activeChats.remove(chat.getId());
    }
}
    