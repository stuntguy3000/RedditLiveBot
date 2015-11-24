package me.stuntguy3000.java.redditlivebot.command;

import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableMessage;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

public abstract class TelegramCommand {
    private final String cmd;
    private final String description;

    private final CommandMessageReceivedEvent event;

    public TelegramCommand(String cmd, String description, CommandMessageReceivedEvent event) {
        this.cmd = cmd;
        this.description = description;
        this.event = event;
    }

    public abstract void processCommand();

    public CommandMessageReceivedEvent getEvent() {
        return event;
    }

    public String getCmd() {
        return cmd;
    }

    public String getDescription() {
        return description;
    }

    public void respond(SendableMessage message) {
        event.getChat().sendMessage(message, TelegramHook.getBot());
    }
}
