package me.stuntguy3000.java.redditlivebot.command;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.handler.RedditHandler;
import me.stuntguy3000.java.redditlivebot.object.command.Command;
import pro.zackpollard.telegrambot.api.chat.message.Message;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

/**
 * @author stuntguy3000
 */
public class AdminCommand extends Command {

    public AdminCommand() {
        super("Command used by RedditLiveBot administrators.", true, "admin");
    }

    @Override
    public void processCommand(CommandMessageReceivedEvent event) {
        String statusText = "*Scanning new threads...*";

        RedditHandler redditHandler = RedditLiveBot.instance.getRedditHandler();

        // Current bot status
        if (redditHandler.getCurrentLiveThread() != null) {
            statusText = "*Following live thread.* ID: " + redditHandler.getCurrentLiveThread().getThreadID();
        }

        Message message = event.getChat().sendMessage(
                SendableTextMessage.builder()
                        .message(
                                "*Welcome to the RedditLive Admin Control Panel*\n\n" +
                                        "To use the control panel, please click on one of the buttons below.\n\nCurrent Status: " + statusText)
                        .replyMarkup(RedditLiveBot.instance.getAdminControlHandler().getMarkup(event.getChat()))
                        .parseMode(ParseMode.MARKDOWN).build());

        RedditLiveBot.instance.getInlineKeyboardHandler().addMessage(message);
    }
}
