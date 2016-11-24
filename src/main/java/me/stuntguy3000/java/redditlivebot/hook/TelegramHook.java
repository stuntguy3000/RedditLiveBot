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

package me.stuntguy3000.java.redditlivebot.hook;

import lombok.Getter;
import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.handler.JenkinsUpdateHandler;
import me.stuntguy3000.java.redditlivebot.handler.TelegramEventHandler;
import me.stuntguy3000.java.redditlivebot.object.ClassGetter;
import me.stuntguy3000.java.redditlivebot.object.Lang;
import me.stuntguy3000.java.redditlivebot.object.command.Command;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.Chat;

import java.util.Arrays;
import java.util.List;

// @author Luke Anderson | stuntguy3000
public class TelegramHook {
    @Getter
    private static TelegramBot bot;
    @Getter
    private static Chat redditLiveChat;
    @Getter
    private final RedditLiveBot instance;

    public TelegramHook(String authKey, RedditLiveBot instance) {
        this.instance = instance;

        bot = TelegramBot.login(authKey);
        bot.startUpdates(false);
        bot.getEventsManager().register(new TelegramEventHandler());

        JenkinsUpdateHandler.UpdateInformation updateInformation = instance.getJenkinsUpdateHandler().getLastUpdate();

        if (updateInformation == null || updateInformation.getGitCommitAuthors() == null || updateInformation.getGitCommitAuthors().isEmpty()) {
            Lang.sendAdmin("*RedditLiveBot has connected.\n\n No build information available...*");
        } else {
            Lang.sendAdmin("*RedditLiveBot has connected (Build %d).*\n\n" +
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

        redditLiveChat = TelegramHook.getBot().getChat("@RedditLive");
    }

    public static void initializeCommands() {
        List<Class<?>> allCommands = ClassGetter.getClassesForPackage("me.stuntguy3000.java.redditlivebot.command.");
        allCommands.stream().filter(Command.class::isAssignableFrom).forEach(clazz -> {
            try {
                Command command = (Command) clazz.newInstance();
                Lang.sendDebug("Registered command %s.", Arrays.toString(command.getNames()));
            } catch (InstantiationException | IllegalAccessException e) {
                Lang.sendAdmin("Class %s failed to instantiate: %s", clazz.getSimpleName(), e.getLocalizedMessage());
                e.printStackTrace();
            }
        });
    }
}
    