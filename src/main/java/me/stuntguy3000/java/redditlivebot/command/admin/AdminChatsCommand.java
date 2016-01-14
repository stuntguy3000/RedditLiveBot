package me.stuntguy3000.java.redditlivebot.command.admin;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.model.commandtypes.TelegramAdminCommand;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

/**
 * Created by amir on 2016-01-02.
 */
public class AdminChatsCommand extends TelegramAdminCommand {
    public AdminChatsCommand(RedditLiveBot instance) {
        super(instance, "Chats", "Lists the chats this bot is active in.");
    }

    @Override
    public void processCommand(CommandMessageReceivedEvent event) {
        Chat chat = event.getChat();

        chat.sendMessage("Active Chats: " + RedditLiveBot.getInstance().getConfigHandler().getLiveThreads().getActiveChats().toString(), TelegramHook.getBot());
    }
}
