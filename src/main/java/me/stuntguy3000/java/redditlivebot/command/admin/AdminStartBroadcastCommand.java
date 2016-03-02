package me.stuntguy3000.java.redditlivebot.command.admin;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.model.commandtypes.TelegramAdminCommand;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

/**
 * Created by amir on 2016-01-02.
 */
public class AdminStartBroadcastCommand extends TelegramAdminCommand {
    public AdminStartBroadcastCommand(RedditLiveBot instance) {
        super(instance, "StartBroadcast", "Starts broadcasting a live thread on @RedditLive.");
    }

    @Override
    public void processCommand(CommandMessageReceivedEvent event) {
        String[] args = event.getArgs();
        Chat chat = event.getChat();

        chat.sendMessage("Starting broadcast...", TelegramHook.getBot());
        TelegramHook.getLiveFeedHandler().startFeed(TelegramBot.getChat("@RedditLive"), args[1].toLowerCase(), true);
    }
}
