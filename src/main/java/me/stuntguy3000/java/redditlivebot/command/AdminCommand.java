package me.stuntguy3000.java.redditlivebot.command;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.util.BotSettings;
import me.stuntguy3000.java.redditlivebot.util.LogHandler;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;
import pro.zackpollard.telegrambot.api.user.User;
// @author Luke Anderson | stuntguy3000
public class AdminCommand extends TelegramCommand {
    public AdminCommand(RedditLiveBot instance) {
        super(instance, "admin", "");
    }

    public void processCommand(CommandMessageReceivedEvent event) {
        Chat chat = event.getChat();
        User sender = event.getMessage().getSender();

        BotSettings botSettings = RedditLiveBot.getInstance().getConfig().getBotSettings();
        LogHandler.log("Admins: " + botSettings.getRedditAppID());
        if (botSettings.getTelegramAdmins().contains(sender.getId())) {
            chat.sendMessage("You're chill, " + sender.getUsername(), TelegramHook.getBot());
        } else {
            chat.sendMessage("You cannot use this command " + sender.getUsername(), TelegramHook.getBot());
        }
    }
}
    