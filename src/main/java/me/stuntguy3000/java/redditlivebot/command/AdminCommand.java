package me.stuntguy3000.java.redditlivebot.command;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.util.Config;
import me.stuntguy3000.java.redditlivebot.util.LogHandler;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;
import pro.zackpollard.telegrambot.api.user.User;

// @author Luke Anderson | stuntguy3000
public class AdminCommand extends TelegramCommand {
    public AdminCommand(RedditLiveBot instance) {
        super(instance, "/admin", null);
    }

    public void processCommand(CommandMessageReceivedEvent event) {
        Chat chat = event.getChat();
        User sender = event.getMessage().getSender();

        LogHandler.log("Admin command fired");

        if (sender.getId() == Config.getInstance().getTelegramAdmin()) {
            chat.sendMessage("You cannot use this command " + sender.getUsername(), TelegramHook.getBot());
            LogHandler.log("User %s tried to use an admin command.", sender.getId());
        } else {
            chat.sendMessage("You're chill, " + sender.getUsername(), TelegramHook.getBot());
        }
    }
}
    