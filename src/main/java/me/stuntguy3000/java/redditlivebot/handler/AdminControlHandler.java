package me.stuntguy3000.java.redditlivebot.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Data;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.object.reddit.redditthread.RedditThreadChildrenData;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.Message;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.keyboards.InlineKeyboardButton;
import pro.zackpollard.telegrambot.api.keyboards.InlineKeyboardMarkup;

/**
 * Provides bot admins a simple way to control the bot
 *
 * @author stuntguy3000
 */
@Data
public class AdminControlHandler {

    private Chat adminChat;
    private HashMap<String, Message> updateMessages = new HashMap<>();
    private HashMap<String, String> lastMessages = new HashMap<>();

    public AdminControlHandler() {
        adminChat = TelegramHook.getBot().getChat(-115432737);
    }

    public void threadUpdate(RedditThreadChildrenData redditThread, String threadID) {
        Message message = updateMessages.get(threadID);
        String lastMessage = lastMessages.get(threadID);

        if (message == null) {
            message = adminChat.sendMessage("Loading...");
        }

        String threadInformation = "*Reddit Live Thread*\n\n" +
                "*Thread ID:* " + threadID + "\n" +
                "*Thread URL:* https://reddit.com/live/" + threadID + "\n" +
                "*Thread Title:* " + redditThread.getTitle() + "\n" +
                "*Score:* " + redditThread.getScore() + "\n";

        List<InlineKeyboardButton> buttons = new ArrayList<>();

        buttons.add(InlineKeyboardButton.builder()
                .callbackData(threadID)
                .text("Click here to follow").build());

        if (lastMessage != null && lastMessage.equals(threadInformation)) {
            return;
        }

        message = TelegramHook.getBot().editMessageText(message, threadInformation,
                ParseMode.MARKDOWN, false, InlineKeyboardMarkup.builder().addRow(buttons).build());

        updateMessages.put(threadID, message);
        lastMessages.put(threadID, threadInformation);
    }
}
