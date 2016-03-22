package me.stuntguy3000.java.redditlivebot.hook;

import lombok.Getter;
import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.command.admin.*;
import me.stuntguy3000.java.redditlivebot.command.normal.StartLiveCommand;
import me.stuntguy3000.java.redditlivebot.command.normal.StatusCommand;
import me.stuntguy3000.java.redditlivebot.command.normal.StopLiveCommand;
import me.stuntguy3000.java.redditlivebot.command.normal.VersionCommand;
import me.stuntguy3000.java.redditlivebot.handler.LiveFeedHandler;
import me.stuntguy3000.java.redditlivebot.util.LogHandler;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.event.Listener;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;
import pro.zackpollard.telegrambot.api.event.chat.message.TextMessageReceivedEvent;

// @author Luke Anderson | stuntguy3000
public class TelegramHook implements Listener {
    @Getter
    private static TelegramBot bot;
    @Getter
    private static LiveFeedHandler liveFeedHandler;
    private final RedditLiveBot instance;

    public TelegramHook(String authKey, RedditLiveBot instance) {
        this.instance = instance;
        this.initializeCommands();

        bot = TelegramBot.login(authKey);
        bot.startUpdates(false);
        bot.getEventsManager().register(this);
        LogHandler.log("Connected to Telegram.");
        liveFeedHandler = new LiveFeedHandler(bot);
        liveFeedHandler.load();

        instance.sendToAdmins("Bot has connected, running build #" + RedditLiveBot.BUILD);
    }

    private void initializeCommands() {
        /** Admin Commands **/
        new AdminAdminsCommand(instance);
        new AdminBotfatherCommand(instance);
        new AdminChatsCommand(instance);
        new AdminCountCommand(instance);
        new AdminRestartCommand(instance);
        new AdminStartBroadcastCommand(instance);
        new AdminStopBroadcastCommand(instance);
        new AdminStopLiveCommand(instance);
        new AdminDebugCommand(instance);

        /** Normal Commands **/
        new StartLiveCommand(instance);
        new StatusCommand(instance);
        new StopLiveCommand(instance);
        new VersionCommand(instance);
    }

    @Override
    public void onCommandMessageReceived(CommandMessageReceivedEvent event) {
        String command = event.getCommand();

        LogHandler.log("DEBUG: Processing command %s by %s", command, event.getMessage().getSender().getUsername());

        instance.getCommandHandler().executeCommand(command, event);
    }

    @Override
    public void onTextMessageReceived(TextMessageReceivedEvent event) {
        LogHandler.log("DEBUG: Processing message %s by %s", event.getContent().getContent(), event.getMessage().getSender().getUsername());
    }
}
    