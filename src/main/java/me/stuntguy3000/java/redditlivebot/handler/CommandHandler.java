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

package me.stuntguy3000.java.redditlivebot.handler;

import lombok.Getter;
import me.stuntguy3000.java.redditlivebot.object.command.Command;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles the execution of Telegram commands
 *
 * @author stuntguy3000
 */
public class CommandHandler {
    @Getter
    public HashMap<String[], Command> commands = new HashMap<>();

    /**
     * Executes a command
     *
     * @param commandLabel String the command being sent
     * @param event        CommandMessageReceivedEvent
     */
    public void executeCommand(String commandLabel, CommandMessageReceivedEvent event) {
        Command cmd = null;

        for (Map.Entry<String[], Command> command : commands.entrySet()) {
            for (String name : command.getKey()) {
                if (commandLabel.equalsIgnoreCase(name)) {
                    cmd = command.getValue();
                }
            }
        }

        if (cmd != null) {
            cmd.preProcessCommand(event);
        }
    }

    /**
     * Registers a new command
     *
     * @param cmd Command the command being registered
     */
    public void registerCommand(Command cmd) {
        commands.put(cmd.getNames(), cmd);
    }
}
