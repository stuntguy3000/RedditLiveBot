package me.stuntguy3000.java.redditlivebot.handler;

import lombok.Getter;
import me.stuntguy3000.java.redditlivebot.object.command.Command;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    @Getter
    public HashMap<String[], Command> commands = new HashMap<>();

    public void executeCommand(String s, CommandMessageReceivedEvent event) {
        Command cmd = null;

        for (Map.Entry<String[], Command> command : commands.entrySet()) {
            for (String name : command.getKey()) {
                if (s.equalsIgnoreCase(name)) {
                    cmd = command.getValue();
                }
            }
        }

        if (cmd != null) {
            cmd.preProcessCommand(event);
        }
    }

    public String getBotFatherString() {
        StringBuilder sb = new StringBuilder();
        for (Command cmd : commands.values()) {
            sb.append(cmd.createBotFatherString()).append("\n");
        }

        return sb.toString();
    }

    public void registerCommand(Command cmd) {
        commands.put(cmd.getNames(), cmd);
    }
}
