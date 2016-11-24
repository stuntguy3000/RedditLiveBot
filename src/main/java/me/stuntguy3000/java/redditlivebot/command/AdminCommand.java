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
                        .replyMarkup(RedditLiveBot.instance.getAdminControlHandler().getDashboardKeyboardMarkup(event.getChat()))
                        .parseMode(ParseMode.MARKDOWN).build());
    }
}
