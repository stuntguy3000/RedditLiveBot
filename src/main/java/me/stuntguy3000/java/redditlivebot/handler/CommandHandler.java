package me.stuntguy3000.java.redditlivebot.handler;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import me.stuntguy3000.java.redditlivebot.object.command.Command;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

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
