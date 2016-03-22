package me.stuntguy3000.java.redditlivebot.command.normal;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.model.commandtypes.TelegramNormalCommand;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

// @author Luke Anderson | stuntguy3000
public class VersionCommand extends TelegramNormalCommand {
    public VersionCommand(RedditLiveBot instance) {
        super(instance, "version", "/version View the bot's current version");
    }

    public void processCommand(CommandMessageReceivedEvent event) {
        Chat chat = event.getChat();

        respond(chat, SendableTextMessage.builder().message("*RedditLive by* @stuntguy3000\n" +
                "`Build " + RedditLiveBot.BUILD + "`\n\n" +
                "Source [Available on GitHub](https://github.com/stuntguy3000/RedditLiveBot)\n" +
                "Created using @zackpollard's [JavaTelegramBotAPI](https://github.com/zackpollard/JavaTelegramBot-API)")
                .parseMode(ParseMode.MARKDOWN).disableWebPagePreview(true).build());
    }
}
    