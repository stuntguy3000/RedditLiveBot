package me.stuntguy3000.java.redditlivebot.command.admin;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.model.commandtypes.TelegramAdminCommand;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

/**
 * Created by amir on 2016-01-22.
 */
public class AdminRestartCommand extends TelegramAdminCommand {
    public AdminRestartCommand(RedditLiveBot instance) {
        super(instance, "Restart", "Restarts the bot");
    }

    @Override
    public void processCommand(CommandMessageReceivedEvent event) {
        System.exit(0);
    }
}
