package me.stuntguy3000.java.redditlivebot.hook;

import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.event.Listener;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

// @author Luke Anderson | stuntguy3000
public class TelegramHook implements Listener {
    private static TelegramBot bot;

    public TelegramHook(String authKey) {
        bot = TelegramBot.login(authKey);
        bot.getEventsManager().register(this);
        bot.startUpdates(false);
        System.out.println("Connected to Telegram.");
    }

    public void onCommandMessageReceivedEvent(CommandMessageReceivedEvent event) {
        String command = event.getMessage().toString();

        System.out.println(command);

        if (command.startsWith("/startlive")) {
            //new StartLiveCommand(event);
        }
    }

    public static TelegramBot getBot() {
        return bot;
    }
}
    