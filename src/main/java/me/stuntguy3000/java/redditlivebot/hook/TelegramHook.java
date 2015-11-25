package me.stuntguy3000.java.redditlivebot.hook;

import lombok.Getter;
import me.stuntguy3000.java.redditlivebot.command.StartLiveCommand;
import me.stuntguy3000.java.redditlivebot.command.StatusCommand;
import me.stuntguy3000.java.redditlivebot.command.StopLiveCommand;
import me.stuntguy3000.java.redditlivebot.command.VersionCommand;
import me.stuntguy3000.java.redditlivebot.handler.LiveFeedHandler;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.event.Listener;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

// @author Luke Anderson | stuntguy3000
public class TelegramHook implements Listener {
    @Getter
    private static TelegramBot bot;
    @Getter
    private static LiveFeedHandler liveFeedHandler;

    public TelegramHook(String authKey) {
        bot = TelegramBot.login(authKey);
        bot.startUpdates(false);
        bot.getEventsManager().register(this);
        System.out.println("Connected to Telegram.");
        liveFeedHandler = new LiveFeedHandler(bot);
    }

    @Override
    public void onCommandMessageReceived(CommandMessageReceivedEvent event) {
        String command = event.getCommand();

        if (command.equalsIgnoreCase("startlive")) {
            new StartLiveCommand(event);
        } else if (command.equalsIgnoreCase("status")) {
            new StatusCommand(event);
        } else if (command.equalsIgnoreCase("stoplive")) {
            new StopLiveCommand(event);
        } else if (command.equalsIgnoreCase("version")) {
            new VersionCommand(event);
        }
    }
}
    