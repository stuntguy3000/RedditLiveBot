package me.stuntguy3000.java.redditlivebot.command.admin;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.model.commandtypes.TelegramAdminCommand;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

/**
 * Created by amir on 2016-01-02.
 */
public class AdminCountCommand extends TelegramAdminCommand {
    public AdminCountCommand(RedditLiveBot instance) {
        super(instance, "Count", "Shows number of chats this bot is active in.");
    }

    @Override
    public void processCommand(CommandMessageReceivedEvent event) {
        Chat chat = event.getChat();
        chat.sendMessage(String.format("Live feed count: %d", TelegramHook.getLiveFeedHandler().getCount()), TelegramHook.getBot());
    }
}
