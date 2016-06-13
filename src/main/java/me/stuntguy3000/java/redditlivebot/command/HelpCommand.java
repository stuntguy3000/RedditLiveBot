package me.stuntguy3000.java.redditlivebot.command;

import java.util.ArrayList;
import java.util.List;

import me.stuntguy3000.java.redditlivebot.object.Emoji;
import me.stuntguy3000.java.redditlivebot.object.command.Command;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;
import pro.zackpollard.telegrambot.api.keyboards.InlineKeyboardButton;
import pro.zackpollard.telegrambot.api.keyboards.InlineKeyboardMarkup;


// @author Luke Anderson | stuntguy3000
public class HelpCommand extends Command {
    public HelpCommand() {
        super("View command help", false, "help", "start", "?");
    }

    public void processCommand(CommandMessageReceivedEvent event) {
        StringBuilder commandHelp = new StringBuilder();

        if (event.getArgs().length > 0) {
            if (event.getArgs()[0].equalsIgnoreCase("subscribe")) {
                new SubscribeCommand().processCommand(event);
                return;
            }
        }

        commandHelp.append("*Welcome to RedditLiveBot*\n" +
                "Created by @stuntguy3000, this bot allows you to stay up to date to trending RedditLive threads. " +
                "All the content is monitored constantly by our administration team, ensuring the content is of a high standard.\n\n" +
                "To begin, type /subscribe in a group chat, or join @RedditLive to stay up to date!\n\n" +
                "*Command help:*\n" +
                "/subscribe - Subscribe to updates from @RedditLive\n" +
                "/unsubscribe - Unsubscribe to updates from @RedditLive\n" +
                "/version - Show bot version information, and provide a link to the source code.");

        List<InlineKeyboardButton> buttons = new ArrayList<>();

        buttons.add(InlineKeyboardButton.builder()
                .text(Emoji.GREEN_BOX_TICK.getText() + " Subscribe")
                .callbackData("usrSubscribe:" + event.getChat().getId())
                .build());

        SendableTextMessage sendableTextMessage = SendableTextMessage.builder()
                .message(commandHelp.toString())
                .parseMode(ParseMode.MARKDOWN)
                .replyMarkup(InlineKeyboardMarkup.builder().addRow(buttons).build())
                .build();

        event.getChat().sendMessage(sendableTextMessage);
    }
}