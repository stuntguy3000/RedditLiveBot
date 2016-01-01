package me.stuntguy3000.java.redditlivebot.handler;

import me.stuntguy3000.java.redditlivebot.model.TelegramCommand;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

import java.util.HashMap;

/**
 * Created by amir on 2015-11-25.
 */
public class CommandHandler {
    public HashMap<String, TelegramCommand> normalCommands;
    public HashMap<String, TelegramCommand> adminCommands;

    public CommandHandler() {
        normalCommands = new HashMap<>();
    }

    public void registerCommand(TelegramCommand cmd) {
        switch (cmd.getCommandType()) {

            case NORMAL:
                normalCommands.put(cmd.getName().toLowerCase(), cmd);
                break;
            case ADMIN:
                adminCommands.put(cmd.getName().toLowerCase(), cmd);
                break;
        }
    }

    public void executeCommand(String s, CommandMessageReceivedEvent event) {
        s = s.toLowerCase();
        TelegramCommand cmd = null;

        if (s.equals("admin")) {
            if (event.getArgs().length == 0) {
                // Send help message.
                return;
            }
            cmd = adminCommands.get(event.getArgs()[0].toLowerCase());
        } else {
            cmd = normalCommands.get(s);
        }

        if (cmd == null) return;
        cmd.processCommand(event);
    }

    public String getBotFatherString() {
        StringBuilder sb = new StringBuilder();
        for (TelegramCommand cmd : normalCommands.values()) {
            sb
                    .append(cmd.createBotFatherString())
                    .append("\n");
        }

        return sb.toString();
    }
}
