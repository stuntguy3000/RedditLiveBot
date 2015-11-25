package me.stuntguy3000.java.redditlivebot.handler;

import me.stuntguy3000.java.redditlivebot.command.TelegramCommand;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

import java.util.HashMap;

/**
 * Created by amir on 2015-11-25.
 */
public class CommandHandler {
    public HashMap<String, TelegramCommand> commands;

    public CommandHandler() {
        commands = new HashMap<>();
    }

    public void registerCommand(TelegramCommand cmd) {
        commands.put(cmd.getName().toLowerCase(), cmd);
    }

    public void executeCommand(String s, CommandMessageReceivedEvent event) {
        TelegramCommand cmd = commands.get(s.toLowerCase());

        if (cmd == null) return;

        cmd.processCommand(event);
    }

    public String getBotFatherString() {
        StringBuilder sb = new StringBuilder();
        for (TelegramCommand cmd : commands.values()) {
            sb
                    .append(cmd.createBotFatherString())
                    .append("\n");
        }

        return sb.toString();
    }
}
