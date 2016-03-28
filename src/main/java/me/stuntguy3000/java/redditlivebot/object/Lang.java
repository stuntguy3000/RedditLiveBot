package me.stuntguy3000.java.redditlivebot.object;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.handler.LogHandler;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableMessage;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.user.User;

// @author Luke Anderson | stuntguy3000
public class Lang {
    public static final String ERROR_NOT_ADMIN = Emoji.RED_CROSS.getText() + " *You are not an admin!*";
    public static final String LIVE_THREAD_UPDATE = Emoji.PERSON_SPEAKING.getText() + " ```%s``` *New update by %s*\n\n%s";

    private static SendableMessage build(String message, Object... format) {
        SendableTextMessage.SendableTextMessageBuilder sendableTextMessageBuilder = SendableTextMessage.builder();
        sendableTextMessageBuilder.message(String.format(message, format));
        sendableTextMessageBuilder.parseMode(ParseMode.MARKDOWN);

        return sendableTextMessageBuilder.build();
    }

    public static void send(Long chatID, String message, Object... format) {
        TelegramHook.getBot().sendMessage(TelegramBot.getChat(chatID), build(message, format));
    }

    public static void send(Chat chat, String message, Object... format) {
        TelegramHook.getBot().sendMessage(chat, build(message, format));
    }

    public static void send(User user, String message, Object... format) {
        send(user.getId(), message, format);
    }

    public static void sendAdmin(String message, Object... format) {
        for (long adminID : RedditLiveBot.getInstance().getConfigHandler().getBotSettings().getTelegramAdmins()) {
            send(adminID, "[ADMIN] " + message, format);
        }

        LogHandler.log("[ADMIN] " + message, format);
    }

    public static void sendDebug(String message, Object... format) {
        if (RedditLiveBot.DEV_MODE) {
            sendAdmin("[DEBUG] " + message, format);
        }
    }
}
    