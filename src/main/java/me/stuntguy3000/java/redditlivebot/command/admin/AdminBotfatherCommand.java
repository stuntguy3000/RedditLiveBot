package me.stuntguy3000.java.redditlivebot.command.admin;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.model.commandtypes.TelegramAdminCommand;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

/**
 * Created by amir on 2016-01-02.
 */
public class AdminBotfatherCommand extends TelegramAdminCommand {
    public AdminBotfatherCommand(RedditLiveBot instance) {
        super(instance, "Botfather", "Creates the BotFather string.");
    }

    @Override
    public void processCommand(CommandMessageReceivedEvent event) {
        Chat chat = event.getChat();

        chat.sendMessage(RedditLiveBot.getInstance().getCommandHandler().getBotFatherString(), TelegramHook.getBot());
    }
}
