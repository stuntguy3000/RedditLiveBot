package me.stuntguy3000.java.redditlivebot.command;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

// @author Luke Anderson | stuntguy3000
public class VersionCommand extends TelegramCommand {
    public VersionCommand(CommandMessageReceivedEvent event) {
        super("version", "/version View the bot's current version\n", event);
        processCommand();
    }

    public void processCommand() {
        respond("RedditLive by @stuntguy3000 version " + RedditLiveBot.getVersion());
    }
}
    