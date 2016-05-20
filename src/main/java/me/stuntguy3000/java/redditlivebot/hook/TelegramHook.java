package me.stuntguy3000.java.redditlivebot.hook;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.object.ClassGetter;
import me.stuntguy3000.java.redditlivebot.object.Lang;
import me.stuntguy3000.java.redditlivebot.object.command.Command;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.inline.send.content.InputTextMessageContent;
import pro.zackpollard.telegrambot.api.chat.inline.send.results.InlineQueryResultArticle;
import pro.zackpollard.telegrambot.api.event.Listener;
import pro.zackpollard.telegrambot.api.event.chat.inline.InlineQueryReceivedEvent;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

// @author Luke Anderson | stuntguy3000
public class TelegramHook implements Listener {
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
        bot.getEventsManager().register(this);

        Lang.sendAdmin("Bot has connected, running build #%d", RedditLiveBot.BUILD);

        this.initializeCommands();

        redditLiveChat = TelegramHook.getBot().getChat("@RedditLive");
        //redditLiveChat = TelegramBot.getChat(-14978569);
    }

    private void initializeCommands() {
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

    @Override
    public void onCommandMessageReceived(CommandMessageReceivedEvent event) {
        String command = event.getCommand();

        instance.getCommandHandler().executeCommand(command, event);
    }

    @Override
    public void onInlineQueryReceived(InlineQueryReceivedEvent event) {
        try {
            InlineQueryResultArticle article = InlineQueryResultArticle.builder()
                    .title("RedditLive Test")
                    .description("Latest update: Paul says hello, WWIII ensues.").url(
                            new URL("https://camo.githubusercontent.com/b13830f5a9baecd3d83ef5cae4d5107d25cdbfbe/68747470733a2f2f662e636c6f75642e6769746875622e636f6d2f6173736574732f3732313033382f313732383830352f35336532613364382d363262352d313165332d383964312d3934376632373062646430332e706e67")
                    ).hideUrl(true).thumbHeight(512).thumbWidth(512).id("latestNews").inputMessageContent(
                            InputTextMessageContent.builder().messageText("HELLO FROM THE OTHER SIDE").build()
                    ).build();

            event.getQuery().answer(TelegramHook.getBot(), article);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
    