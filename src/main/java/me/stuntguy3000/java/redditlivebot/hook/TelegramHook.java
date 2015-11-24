package me.stuntguy3000.java.redditlivebot.hook;

import me.stuntguy3000.java.redditlivebot.command.StartLiveCommand;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.event.Listener;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

// @author Luke Anderson | stuntguy3000
public class TelegramHook implements Listener {
    private static TelegramBot bot;

    public TelegramHook(String authKey) {
        bot = TelegramBot.login(authKey);
        bot.startUpdates(false);
        bot.getEventsManager().register(this);
        System.out.println("Connected to Telegram.");
    }

    @Override
    public void onCommandMessageReceived(CommandMessageReceivedEvent event) {
        String command = event.getCommand();

        System.out.println(command);

        if (command.equalsIgnoreCase("startlive")) {
            new StartLiveCommand(event);
        }
    }

    public static TelegramBot getBot() {
        return bot;
    }
}
    