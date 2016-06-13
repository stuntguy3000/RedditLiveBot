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
    private final RedditLiveBot instance;
    @Getter
    private final String[] names;

    public Command(RedditLiveBot instance, String description, boolean adminOnly, String... names) {
        this.instance = instance;
        this.names = names;
        this.description = description;
        this.adminOnly = adminOnly;

        instance.getCommandHandler().registerCommand(this);
    }


    public String createBotFatherString() {
        return String.format("%s - %s", Arrays.toString(names), description);
    }

    public void preProcessCommand(CommandMessageReceivedEvent event) {
        if (adminOnly) {
            if (!RedditLiveBot.instance.getConfigHandler().getBotSettings().getTelegramAdmins().contains(event.getMessage().getSender().getId())) {
                Lang.send(event.getChat(), Lang.ERROR_NOT_ADMIN);
                return;
            }
        }

        processCommand(event);
    }

    public abstract void processCommand(CommandMessageReceivedEvent event);
}
