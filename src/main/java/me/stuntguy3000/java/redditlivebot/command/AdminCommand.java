package me.stuntguy3000.java.redditlivebot.command;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.util.BotSettings;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
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
        String[] args = event.getArgs();

        BotSettings botSettings = RedditLiveBot.getInstance().getConfigHandler().getBotSettings();

        if (botSettings.getTelegramAdmins().contains(sender.getId())) {
            if (args.length == 0) {
                chat.sendMessage(SendableTextMessage.builder().message("*@stuntguy3000&#95;alt*").parseMode(ParseMode.MARKDOWN).build(), TelegramHook.getBot());
                chat.sendMessage("Admin Commands: count, stoplive, stop, botfather", TelegramHook.getBot());
            } else {
                switch (args[0].toLowerCase()) {
                    case "count": {
                        chat.sendMessage("Live feed count: " + TelegramHook.getLiveFeedHandler().getCount(), TelegramHook.getBot());
                        return;
                    }
                    case "stoplive": {
                        chat.sendMessage("Shutting down live feeds.", TelegramHook.getBot());
                        TelegramHook.getLiveFeedHandler().stopAll();
                        return;
                    }
                    case "botfather": {
                        chat.sendMessage(RedditLiveBot.getInstance().getCommandHandler().getBotFatherString(), TelegramHook.getBot());
                        return;
                    }
                    case "stop": {
                        chat.sendMessage("Shutting bot down.", TelegramHook.getBot());
                        TelegramHook.getLiveFeedHandler().stopAll();
                        System.exit(0);
                        return;
                    }
                    case "admins": {
                        chat.sendMessage("Admins: " + botSettings.getTelegramAdmins(), TelegramHook.getBot());
                        return;
                    }
                    case "startbroadcast": {
                        chat.sendMessage("Starting broadcast...", TelegramHook.getBot());
                        TelegramHook.getLiveFeedHandler().startFeed(TelegramBot.getChat("@RedditLive"), args[1].toLowerCase(), true);
                        return;
                    }
                    case "stopbroadcast": {
                        chat.sendMessage("Stopping broadcast...", TelegramHook.getBot());
                        TelegramHook.getLiveFeedHandler().stop(TelegramBot.getChat("@RedditLive"));
                        return;
                    }
                    case "chats": {
                        chat.sendMessage("Active Chats: " + RedditLiveBot.getInstance().getConfigHandler().getLiveThreads().getActiveChats().toString(), TelegramHook.getBot());
                        return;
                    }
                    default: {
                        chat.sendMessage("Admin Commands: count, stoplive, stop, botfather, admins, startbroadcast, stopbroadcast, chats", TelegramHook.getBot());
                    }
                }
            }
        }
    }
}
    