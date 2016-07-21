package me.stuntguy3000.java.redditlivebot.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Data;
import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.object.AdminInlineCommandType;
import me.stuntguy3000.java.redditlivebot.object.reddit.subreddit.SubredditChildrenData;
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
    private HashMap<String, String> lastMessages = new HashMap<>();
    private HashMap<Long, AdminInlineCommandType> replyActions = new HashMap<>();
    private HashMap<String, Message> updateMessages = new HashMap<>();

    public AdminControlHandler() {
        adminChat = TelegramHook.getBot().getChat(-115432737);
    }

    /**
     * Posts a new live thread to the admin chat
     *
     * @param redditThread SubredditChildrenData the data to be posted
     * @param threadID     String the id of the thread
     */
    public void postNewLiveThread(SubredditChildrenData redditThread, String threadID) {
        Message message = updateMessages.get(threadID);
        String lastMessage = lastMessages.get(threadID);

        String threadInformation = "*Reddit Live Thread*\n\n" +
                "*Thread ID:* " + threadID + "\n" +
                "*Thread URL:* https://reddit.com/live/" + threadID + "\n" +
                "*Thread Title:* " + redditThread.getTitle() + "\n" +
                "*Score:* " + redditThread.getScore() + "\n";

        List<InlineKeyboardButton> buttons = new ArrayList<>();

        buttons.add(InlineKeyboardButton.builder()
                .callbackData("f," + threadID)
                .text("Follow (Normal)").build());

        buttons.add(InlineKeyboardButton.builder()
                .callbackData("fS," + threadID)
                .text("Follow (Silent)").build());

        if (lastMessage != null && lastMessage.equals(threadInformation)) {
            return;
        }

        if (message == null) {
            message = adminChat.sendMessage("Loading new live thread...");
        }

        message = TelegramHook.getBot().editMessageText(message, threadInformation,
                ParseMode.MARKDOWN, false, InlineKeyboardMarkup.builder().addRow(buttons).build());

        updateMessages.put(threadID, message);
        lastMessages.put(threadID, threadInformation);
    }

    /**
     * Adds a reply message to the actions map
     * <p>A reply message is one where the user is prompted to provide context or information
     * with one example being the broadcast command where the user specifies text to be broadcasted
     * </p>
     *
     * @param message Message the original message
     * @param type AdminInlineCommandType the type of command
     */
    public void addReplyMessage(Message message, AdminInlineCommandType type) {
        replyActions.put(message.getMessageId(), type);
    }

    /**
     * Generate the keyboard markup for a Chat
     * <p>The admin dashboard is a simple GUI to control basic functions of the bot</p>
     *
     * @param chat Chat the chat where the message will be sent
     *
     * @return InlineKeyboardMarkup the generated markup
     */
    public InlineKeyboardMarkup getDashboardKeyboardMarkup(Chat chat) {
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
        int count = RedditLiveBot.instance.getSubscriptionHandler().getSubscriptions().size();
        buttons.add(InlineKeyboardButton.builder()
                .text("View Subscriptions (" + count + ")").callbackData(
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
