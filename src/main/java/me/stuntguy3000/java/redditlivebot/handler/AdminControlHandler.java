package me.stuntguy3000.java.redditlivebot.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Data;
import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.object.AdminInlineCommandType;
import me.stuntguy3000.java.redditlivebot.object.reddit.redditthread.RedditThreadChildrenData;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.Message;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
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
    private HashMap<Long, AdminInlineCommandType> replyActions = new HashMap<>();

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
                .callbackData("f$" + threadID + "$" + redditThread.getTitle())
                .text("Click here to follow").build());

        if (lastMessage != null && lastMessage.equals(threadInformation)) {
            return;
        }

        message = TelegramHook.getBot().editMessageText(message, threadInformation,
                ParseMode.MARKDOWN, false, InlineKeyboardMarkup.builder().addRow(buttons).build());

        updateMessages.put(threadID, message);
        lastMessages.put(threadID, threadInformation);
    }

    public void updateMessage(Chat chat, SendableTextMessage updateMessage) {
        Message message = updateMessages.get(chat.getId());

        TelegramHook.getBot().editMessageText(message,
                updateMessage.getMessage(),
                updateMessage.getParseMode(),
                false, getMarkup(chat)
        );
    }

    public void addReplyMessage(Message message, AdminInlineCommandType type) {
        replyActions.put(message.getMessageId(), type);
    }

    public InlineKeyboardMarkup getMarkup(Chat chat) {
        RedditHandler redditHandler = RedditLiveBot.instance.getRedditHandler();
        List<InlineKeyboardButton> buttons = new ArrayList<>();

        // Current bot status
        if (redditHandler.getCurrentLiveThread() != null) {
            buttons.add(InlineKeyboardButton.builder()
                    .text("Unfollow current thread").callbackData(
                            AdminInlineCommandType.STOP_FOLLOW.getCommandID() + "#" + chat.getId())
                    .build());
        } else {
            buttons.add(InlineKeyboardButton.builder()
                    .text("Follow a thread").callbackData(
                            AdminInlineCommandType.START_FOLLOW.getCommandID() + "#" + chat.getId())
                    .build());
        }

        // Subscription data
        buttons.add(InlineKeyboardButton.builder()
                .text("View Subscriptions").callbackData(
                        AdminInlineCommandType.SHOW_SUBS.getCommandID() + "#" + chat.getId())
                .build());

        // Toggle Debug
        buttons.add(InlineKeyboardButton.builder()
                .text("Enable Debug").callbackData(
                        AdminInlineCommandType.ENABLE_DEBUG.getCommandID() + "#" + chat.getId())
                .build());

        buttons.add(InlineKeyboardButton.builder()
                .text("Disable Debug").callbackData(
                        AdminInlineCommandType.DISABLE_DEBUG.getCommandID() + "#" + chat.getId())
                .build());

        // Broadcast Message
        buttons.add(InlineKeyboardButton.builder()
                .text("Broadcast a message").callbackData(
                        AdminInlineCommandType.BROADCAST.getCommandID() + "#" + chat.getId())
                .build());

        // Restart bot
        buttons.add(InlineKeyboardButton.builder()
                .text("Restart the bot").callbackData(
                        AdminInlineCommandType.RESTART.getCommandID() + "#" + chat.getId())
                .build());

        // Build the final message
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder markup = InlineKeyboardMarkup.builder();

        List<InlineKeyboardButton> rows = new ArrayList<>();
        for (InlineKeyboardButton keyboardButton : buttons) {
            rows.add(keyboardButton);

            if (rows.size() == 2) {
                markup.addRow(rows);
                rows.clear();
            }
        }

        return markup.build();
    }
}
