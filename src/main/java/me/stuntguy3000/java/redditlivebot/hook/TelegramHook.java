package me.stuntguy3000.java.redditlivebot.hook;

import lombok.Getter;
import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.command.*;
import me.stuntguy3000.java.redditlivebot.handler.LiveFeedHandler;
import me.stuntguy3000.java.redditlivebot.util.LogHandler;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.event.Listener;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

// @author Luke Anderson | stuntguy3000
public class TelegramHook implements Listener {
    @Getter
    private static TelegramBot bot;
    @Getter
    private static LiveFeedHandler liveFeedHandler;
    private final RedditLiveBot instance;

    public TelegramHook(String authKey, RedditLiveBot instance) {
        this.instance = instance;

        bot = TelegramBot.login(authKey);
        bot.startUpdates(false);
        bot.getEventsManager().register(this);
        LogHandler.log("Connected to Telegram.");
        liveFeedHandler = new LiveFeedHandler(bot);

        this.initializeCommands();
    }

    private void initializeCommands() {
        new StartLiveCommand(instance);
        new StatusCommand(instance);
        new StopLiveCommand(instance);
        new VersionCommand(instance);
        new AdminCommand(instance);
    }

    @Override
    public void onCommandMessageReceived(CommandMessageReceivedEvent event) {
        String command = event.getCommand();

        instance.getCommandHandler().executeCommand(command, event);
    }
}
    