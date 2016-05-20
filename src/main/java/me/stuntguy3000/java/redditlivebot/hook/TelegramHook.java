package me.stuntguy3000.java.redditlivebot.hook;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.object.ClassGetter;
import me.stuntguy3000.java.redditlivebot.object.Lang;
import me.stuntguy3000.java.redditlivebot.object.command.Command;
import me.stuntguy3000.java.redditlivebot.object.reddit.livethread.LiveThreadChildrenData;
import me.stuntguy3000.java.redditlivebot.scheduler.LiveThreadBroadcasterTask;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.inline.send.InlineQueryResponse;
import pro.zackpollard.telegrambot.api.chat.inline.send.content.InputTextMessageContent;
import pro.zackpollard.telegrambot.api.chat.inline.send.results.InlineQueryResultArticle;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
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
            // Handle subscription prompting
            InlineQueryResponse.InlineQueryResponseBuilder subscriptionButton;
            InlineQueryResultArticle latestUpdate;
            if (!RedditLiveBot.getInstance().getSubscriptionHandler().isSubscribed(
                    TelegramHook.getBot().getChat(event.getQuery().getSender().getId()))) {
                subscriptionButton = InlineQueryResponse.builder().switch_pm_text("Click here to subscribe to @RedditLiveBot.").switch_pm_parameter("subscribe");
            } else {
                subscriptionButton = InlineQueryResponse.builder().switch_pm_text("You are subscribed to @RedditLiveBot.");
            }

            // Handle posting of last threads
            Lang.sendDebug("Live thread == null %s", RedditLiveBot.getInstance().getRedditHandler().getCurrentLiveThread());
            if (RedditLiveBot.getInstance().getRedditHandler().getCurrentLiveThread() == null) {
                Lang.sendDebug("No live thread");
                // Nothing to post
                latestUpdate = InlineQueryResultArticle.builder()
                        .title("Latest update")
                        .description("Not following any live threads!").thumbUrl(new URL("https://camo.githubusercontent.com/b13830f5a9baecd3d83ef5cae4d5107d25cdbfbe/68747470733a2f2f662e636c6f75642e6769746875622e636f6d2f6173736574732f3732313033382f313732383830352f35336532613364382d363262352d313165332d383964312d3934376632373062646430332e706e67")).hideUrl(true).thumbHeight(512).thumbWidth(512).id("latestNews").inputMessageContent(
                                InputTextMessageContent.builder().messageText(
                                        "*RedditLive is not following any live threads.*").parseMode(ParseMode.MARKDOWN).build()
                        ).build();
            } else {
                // Variables
                LiveThreadBroadcasterTask liveThreadBroadcasterTask = RedditLiveBot.getInstance().getRedditHandler().getCurrentLiveThread();
                LiveThreadChildrenData lastPost = liveThreadBroadcasterTask.getLastActualPost();

                if (lastPost == null) {
                    latestUpdate = InlineQueryResultArticle.builder()
                            .title("No last post available").description("Try again soon").thumbUrl(new URL("https://camo.githubusercontent.com/b13830f5a9baecd3d83ef5cae4d5107d25cdbfbe/68747470733a2f2f662e636c6f75642e6769746875622e636f6d2f6173736574732f3732313033382f313732383830352f35336532613364382d363262352d313165332d383964312d3934376632373062646430332e706e67")).hideUrl(true).thumbHeight(512).thumbWidth(512).id("latestNews").inputMessageContent(
                                    InputTextMessageContent.builder().messageText("*No last post available.*").parseMode(ParseMode.MARKDOWN).build()
                            ).build();
                } else {
                    long delay = System.currentTimeMillis() - lastPost.getCreated();
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(delay);
                    long seconds = TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.MILLISECONDS.toSeconds(delay);

                    Lang.sendDebug(System.currentTimeMillis() + " : created: " + lastPost.getCreated());

                    String title = "Latest update - Posted " + String.format("%d min, %d sec ago", delay, 0);
                    String body;
                    boolean useMarkdown = true;

                    // Decide HTML VS Markup(down)
                    if (lastPost.getAuthor().contains("/") || lastPost.getAuthor().contains("_") || lastPost.getAuthor().contains("*")
                            || lastPost.getBody().contains("/") || lastPost.getBody().contains("_") || lastPost.getBody().contains("*")) {
                        // HTML
                        useMarkdown = false;
                        body = String.format(
                                Lang.LIVE_THREAD_REPOST_UPDATE_HTML, liveThreadBroadcasterTask.getThreadID(), lastPost.getAuthor(), lastPost.getBody());
                    } else {
                        body = String.format(
                                Lang.LIVE_THREAD_REPOST_UPDATE, liveThreadBroadcasterTask.getThreadID(), lastPost.getAuthor(), lastPost.getBody());
                    }

                    latestUpdate = InlineQueryResultArticle.builder()
                            .title(title).description(lastPost.getBody()).thumbUrl(new URL("https://camo.githubusercontent.com/b13830f5a9baecd3d83ef5cae4d5107d25cdbfbe/68747470733a2f2f662e636c6f75642e6769746875622e636f6d2f6173736574732f3732313033382f313732383830352f35336532613364382d363262352d313165332d383964312d3934376632373062646430332e706e67")).hideUrl(true).thumbHeight(512).thumbWidth(512).id("latestNews").inputMessageContent(
                                    InputTextMessageContent.builder().messageText(body).parseMode(
                                            useMarkdown ? ParseMode.MARKDOWN : ParseMode.HTML).build()
                            ).build();
                }
            }

            event.getQuery().answer(TelegramHook.getBot(),
                    subscriptionButton.results(latestUpdate).cache_time(0).build());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
    