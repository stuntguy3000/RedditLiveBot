package me.stuntguy3000.java.redditlivebot.command.admin;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.model.commandtypes.TelegramAdminCommand;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

/**
 * Created by amir on 2016-01-02.
 */
public class AdminAuthCommand extends TelegramAdminCommand {
    public AdminAuthCommand(RedditLiveBot instance) {
        super(instance, "Auth", "Reauthenticate Reddit");
    }

    @Override
    public void processCommand(CommandMessageReceivedEvent event) {
        RedditLiveBot.getInstance().connectReddit();
    }
}
