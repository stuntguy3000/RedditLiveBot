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
public class AdminStopBroadcastCommand extends TelegramAdminCommand {
    public AdminStopBroadcastCommand(RedditLiveBot instance) {
        super(instance, "StopBroadcast", "Stops broadcasting in @RedditLive.");
    }

    @Override
    public void processCommand(CommandMessageReceivedEvent event) {
        Chat chat = event.getChat();

        chat.sendMessage("Stopping broadcast...", TelegramHook.getBot());
        TelegramHook.getLiveFeedHandler().stop(TelegramBot.getChat("@RedditLive"));
        return;
    }
}
