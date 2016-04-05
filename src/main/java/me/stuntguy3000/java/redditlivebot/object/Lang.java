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
    public static final String CHAT_SUBSCRIBED = Emoji.GREEN_BOX_TICK.getText() + " *This chat has subscribed to RedditLiveBot's updates.*";
    public static final String CHAT_UNSUBSCRIBED = Emoji.GREEN_BOX_TICK.getText() + " *You have unsubscribed from updates.*";
    public static final String COMMAND_ADMIN_DEBUG = "*Debug is set to* `%s`*.*";
    public static final String COMMAND_ADMIN_DEBUG_TOGGLE = "*Debug mode has been changed to* `%s`*.*";
    public static final String COMMAND_ADMIN_STATUS = "*RedditLiveBot Status:*\n\n%s";
    public static final String COMMAND_ADMIN_SUBSCRIPTIONS = "*RedditLive Subscriptions (%s):* \n`%s`";
    public static final String ERROR_CHAT_NOT_SUBSCRIBED = Emoji.RED_CROSS.getText() + " *This chat is not subscribed.*";
    public static final String ERROR_CHAT_SUBSCRIBED = Emoji.RED_CROSS.getText() + " *This chat is already subscribed.*";
    public static final String ERROR_NOT_ADMIN = Emoji.RED_CROSS.getText() + " *You are not a RedditLiveBot administrator!*";
    public static final String ERROR_NOT_ENOUGH_ARGUMENTS = Emoji.RED_CROSS.getText() + " *Invalid command usage!*";
    public static final String GENERAL_BROADCAST = Emoji.PERSON_SPEAKING.getText() + "*Announcement by* @%s\n\n%s";
    public static final String GENERAL_RESTART = "*Manual Restart engaged by* `%s`*.*";
    public static final String LIVE_THREAD_START = Emoji.BLUE_RIGHT_ARROW.getText() + " *Following a new feed!*\n\n" +
            "_Name: _ %s\n_URL: _ https://reddit.com/live/%s";
    public static final String LIVE_THREAD_STOP = Emoji.REPLAY.getText() + " *RedditLive has stopped tracking this live feed due to inactivity*";
    public static final String LIVE_THREAD_UPDATE = Emoji.PERSON_SPEAKING.getText() + " `%s` *New update by %s*\n\n%s";

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
            send(adminID, "*[ADMIN]* " + message, format);
        }

        LogHandler.log("[ADMIN] " + message.replace("`[DEBUG]`", "[DEBUG]"), format);
    }

    public static void sendDebug(String message, Object... format) {
        if (RedditLiveBot.DEBUG) {
            sendAdmin("`[DEBUG]` " + message, format);
        }
    }

    private static void sendRaw(long chatID, String message, Object... format) {
        TelegramHook.getBot().sendMessage(TelegramBot.getChat(chatID), SendableTextMessage.builder().message(String.format(message, format)).build());
    }

    public static String stringJoin(String[] aArr, String prefix, String sSep) {
        StringBuilder sbStr = new StringBuilder();
        for (int i = 0, il = aArr.length; i < il; i++) {
            if (i > 0) {
                sbStr.append(sSep);
            }
            sbStr.append(prefix).append(aArr[i]);
        }
        return sbStr.toString();
    }
}
    