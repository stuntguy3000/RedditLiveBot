package me.stuntguy3000.java.redditlivebot.command;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.object.Emoji;
import me.stuntguy3000.java.redditlivebot.object.Lang;
import me.stuntguy3000.java.redditlivebot.object.command.Command;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;


// @author Luke Anderson | stuntguy3000
public class VersionCommand extends Command {
    public VersionCommand() {
        super(RedditLiveBot.getInstance(), "View the bot's current version", false, "version", "about", "info");
    }

    public void processCommand(CommandMessageReceivedEvent event) {
        Chat chat = event.getChat();

        Lang.send(chat, Emoji.GHOST.getText() + " *RedditLiveBot by* @stuntguy3000\n" +
                "*Current build:* " + RedditLiveBot.BUILD + "\n\n" +
                "Source [Available on GitHub](https://github.com/stuntguy3000/telegames)\n" +
                "Created using @zackpollard's [JavaTelegramBotAPI](https://github.com/zackpollard/JavaTelegramBot-API)");
    }
}