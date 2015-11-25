package me.stuntguy3000.java.redditlivebot.command;

import lombok.Getter;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableMessage;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

public abstract class TelegramCommand {
    @Getter
    private final String cmd;
    @Getter
    private final String description;
    @Getter
    private final CommandMessageReceivedEvent event;

    public TelegramCommand(String cmd, String description, CommandMessageReceivedEvent event) {
        this.cmd = cmd;
        this.description = description;
        this.event = event;
    }

    public abstract void processCommand();

    public void respond(SendableMessage message) {
        event.getChat().sendMessage(message, TelegramHook.getBot());
    }

    public void respond(String message) {
        event.getChat().sendMessage(message, TelegramHook.getBot());
    }
}
