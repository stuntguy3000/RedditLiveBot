/*
 * MIT License
 *
 * Copyright (c) 2016 Luke Anderson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.stuntguy3000.java.redditlivebot.command;

import me.stuntguy3000.java.redditlivebot.object.Emoji;
import me.stuntguy3000.java.redditlivebot.object.command.Command;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;
import pro.zackpollard.telegrambot.api.keyboards.InlineKeyboardButton;
import pro.zackpollard.telegrambot.api.keyboards.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;


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