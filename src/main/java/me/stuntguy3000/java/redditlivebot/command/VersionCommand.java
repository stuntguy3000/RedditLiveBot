package me.stuntguy3000.java.redditlivebot.command;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

// @author Luke Anderson | stuntguy3000
public class VersionCommand extends TelegramCommand {
    public VersionCommand(RedditLiveBot instance) {
        super(instance, "version", "/version View the bot's current version\n");
    }

    public void processCommand(CommandMessageReceivedEvent event) {
        Chat chat = event.getChat();

        respond(chat,"RedditLive by @stuntguy3000 version " + RedditLiveBot.VERSION);
    }
}
    