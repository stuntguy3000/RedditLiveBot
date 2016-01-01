package me.stuntguy3000.java.redditlivebot.command.admin;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.model.commandtypes.TelegramAdminCommand;
import me.stuntguy3000.java.redditlivebot.util.BotSettings;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

/**
 * Created by amir on 2016-01-02.
 */
public class AdminAdminsCommand extends TelegramAdminCommand {
    public AdminAdminsCommand(RedditLiveBot instance) {
        super(instance, "Admins", "Lists the admins of this bot.");
    }

    @Override
    public void processCommand(CommandMessageReceivedEvent event) {
        Chat chat = event.getChat();
        BotSettings botSettings = RedditLiveBot.getInstance().getConfigHandler().getBotSettings();

        chat.sendMessage("Admins: " + botSettings.getTelegramAdmins(), TelegramHook.getBot());

    }
}
