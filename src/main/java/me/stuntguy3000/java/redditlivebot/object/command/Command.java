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

package me.stuntguy3000.java.redditlivebot.object.command;

import lombok.Getter;
import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.object.Lang;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

import java.util.Arrays;

public abstract class Command {
    @Getter
    private final boolean adminOnly;
    @Getter
    private final String description;
    @Getter
    private final String[] names;

    public Command(String description, boolean adminOnly, String... names) {
        this.names = names;
        this.description = description;
        this.adminOnly = adminOnly;

        RedditLiveBot.instance.getCommandHandler().registerCommand(this);
    }


    public String createBotFatherString() {
        return String.format("%s - %s", Arrays.toString(names), description);
    }

    public void preProcessCommand(CommandMessageReceivedEvent event) {
        if (adminOnly) {
            if (!RedditLiveBot.instance.isAdmin(event.getMessage().getSender().getId())) {
                Lang.send(event.getChat(), Lang.ERROR_NOT_ADMIN);
                return;
            }
        }

        processCommand(event);
    }

    public abstract void processCommand(CommandMessageReceivedEvent event);
}
