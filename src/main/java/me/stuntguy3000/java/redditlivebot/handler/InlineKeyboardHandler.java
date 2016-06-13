package me.stuntguy3000.java.redditlivebot.handler;

import java.util.HashMap;
import java.util.UUID;

import pro.zackpollard.telegrambot.api.chat.message.Message;

/**
 * @author stuntguy3000
 */
public class InlineKeyboardHandler {
    private HashMap<UUID, Message> inlineKeyboards = new HashMap<>();

    public Message getMessage(UUID uuid) {
        return inlineKeyboards.get(uuid);
    }

    public UUID addMessage(Message message) {
        UUID uuid = UUID.randomUUID();

        inlineKeyboards.put(uuid, message);

        return uuid;
    }
}
