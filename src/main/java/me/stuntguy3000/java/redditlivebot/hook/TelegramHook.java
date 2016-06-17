package me.stuntguy3000.java.redditlivebot.hook;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.handler.TelegramEventHandler;
import me.stuntguy3000.java.redditlivebot.object.ClassGetter;
import me.stuntguy3000.java.redditlivebot.object.Lang;
import me.stuntguy3000.java.redditlivebot.object.command.Command;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.Chat;

// @author Luke Anderson | stuntguy3000
public class TelegramHook {
    @Getter
    private static TelegramBot bot;
    @Getter
    private static Chat redditLiveChat;
    @Getter
    private final RedditLiveBot instance;

    public TelegramHook(String authKey, RedditLiveBot instance) {
        this.instance = instance;

        bot = TelegramBot.login(authKey);
        bot.startUpdates(false);
        bot.getEventsManager().register(new TelegramEventHandler());

        Lang.sendAdmin("Bot has connected, running build #%d", RedditLiveBot.BUILD);
        redditLiveChat = TelegramHook.getBot().getChat("@RedditLive");
    }

    public static void initializeCommands() {
        List<Class<?>> allCommands = ClassGetter.getClassesForPackage("me.stuntguy3000.java.redditlivebot.command.");
        allCommands.stream().filter(Command.class::isAssignableFrom).forEach(clazz -> {
            try {
                Command command = (Command) clazz.newInstance();
                Lang.sendDebug("Registered command %s.", Arrays.toString(command.getNames()));
            } catch (InstantiationException | IllegalAccessException e) {
                Lang.sendAdmin("Class %s failed to instantiate: %s", clazz.getSimpleName(), e.getLocalizedMessage());
                e.printStackTrace();
            }
        });
    }
}
    