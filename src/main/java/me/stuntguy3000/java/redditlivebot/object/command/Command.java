package me.stuntguy3000.java.redditlivebot.object.command;

import java.util.Arrays;

import lombok.Getter;
import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.object.Lang;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

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
