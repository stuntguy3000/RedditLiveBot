package me.stuntguy3000.java.redditlivebot.command;

import java.util.ArrayList;
import java.util.List;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.handler.RedditHandler;
import me.stuntguy3000.java.redditlivebot.object.command.Command;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.Message;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;
import pro.zackpollard.telegrambot.api.keyboards.InlineKeyboardButton;
import pro.zackpollard.telegrambot.api.keyboards.InlineKeyboardMarkup;

/**
 * @author stuntguy3000
 */
public class AdminCommand extends Command {

    public AdminCommand() {
        super("Command used by RedditLiveBot administrators.", true, "admin");
    }

    @Override
    public void processCommand(CommandMessageReceivedEvent event) {
        RedditHandler redditHandler = RedditLiveBot.instance.getRedditHandler();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        Chat chat = event.getChat();

        String statusText = "Scanning new threads...";

        if (redditHandler.getCurrentLiveThread() != null) {
            statusText = "Following live thread. ID: " + redditHandler.getCurrentLiveThread().getThreadID();

            buttons.add(InlineKeyboardButton.builder()
                    .text("Unfollow current thread").callbackData("adminCommand@unfollowCurrent#" + event.getChat().getId())
                    .build());
        } else {
            buttons.add(InlineKeyboardButton.builder()
                    .text("Follow a thread").callbackData("adminCommand@startFollow#" + event.getChat().getId())
                    .build());
        }

        Message message = chat.sendMessage(
                SendableTextMessage.builder()
                        .message(
                                "*Welcome to the RedditLive Admin Control Panel*\n\n" +
                                        "To use the control panel, please click on one of the buttons below.\n\nCurrent Status: " + statusText)
                        .replyMarkup(InlineKeyboardMarkup.builder().addRow(buttons).build())
                        .parseMode(ParseMode.MARKDOWN).build());


        RedditLiveBot.instance.getInlineKeyboardHandler().addMessage(message);
    }
}
