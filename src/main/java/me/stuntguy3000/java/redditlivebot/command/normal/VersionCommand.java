package me.stuntguy3000.java.redditlivebot.command.normal;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.model.commandtypes.TelegramNormalCommand;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

// @author Luke Anderson | stuntguy3000
public class VersionCommand extends TelegramNormalCommand {
    public VersionCommand(RedditLiveBot instance) {
        super(instance, "version", "/version View the bot's current version");
    }

    public void processCommand(CommandMessageReceivedEvent event) {
        Chat chat = event.getChat();

        respond(chat, "RedditLive by @stuntguy3000 build " + RedditLiveBot.BUILD + ".\n" +
                "A huge thank you to zackpollard, aaomidi and bo0tzz for helping to create this bot.");
    }
}
    