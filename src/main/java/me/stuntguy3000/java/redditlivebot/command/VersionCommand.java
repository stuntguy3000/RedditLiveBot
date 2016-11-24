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
import me.stuntguy3000.java.redditlivebot.handler.JenkinsUpdateHandler;
import me.stuntguy3000.java.redditlivebot.object.Emoji;
import me.stuntguy3000.java.redditlivebot.object.Lang;
import me.stuntguy3000.java.redditlivebot.object.command.Command;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;


// @author Luke Anderson | stuntguy3000
public class VersionCommand extends Command {
    public VersionCommand() {
        super("View the bot's current version", false, "version", "about", "info", "source");
    }

    public void processCommand(CommandMessageReceivedEvent event) {
        Chat chat = event.getChat();

        JenkinsUpdateHandler.UpdateInformation updateInformation = RedditLiveBot.instance.getJenkinsUpdateHandler().getLastUpdate();

        String buildInfo = "No build information available...";
        if (!(updateInformation == null || updateInformation.getGitCommitAuthors() == null || updateInformation.getGitCommitAuthors().isEmpty())) {
            buildInfo = String.format("*Build:* %d\n\n" +
                            "*Last commit information:*\n" +
                            "*Description:* %s\n" +
                            "*Author:* %s\n" +
                            "*Commit ID:* %s\n",
                    updateInformation.getBuildNumber(),
                    updateInformation.getGitCommitMessages().get(0),
                    updateInformation.getGitCommitAuthors().get(0),
                    "[" + updateInformation.getGitCommitIds().get(0) + "]" +
                            "(https://github.com/stuntguy3000/RedditLiveBot/commit/"
                            + updateInformation.getGitCommitIds().get(0) + ")");
        }

        Lang.send(chat, Emoji.JOKER_CARD.getText() + " *RedditLiveBot by* @stuntguy3000\n" +
                buildInfo +
                "\nSource [Available on GitHub](https://github.com/stuntguy3000/RedditLiveBot)\n" +
                "Created using @zackpollard's [Java Telegram API](https://github.com/zackpollard/JavaTelegramBot-API)");

    }
}