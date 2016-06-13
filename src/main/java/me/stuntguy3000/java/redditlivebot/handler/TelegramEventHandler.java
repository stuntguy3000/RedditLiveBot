package me.stuntguy3000.java.redditlivebot.handler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.object.Lang;
import me.stuntguy3000.java.redditlivebot.object.reddit.livethread.LiveThreadChildrenData;
import me.stuntguy3000.java.redditlivebot.scheduler.LiveThreadBroadcasterTask;
import pro.zackpollard.telegrambot.api.chat.inline.send.InlineQueryResponse;
import pro.zackpollard.telegrambot.api.chat.inline.send.content.InputTextMessageContent;
import pro.zackpollard.telegrambot.api.chat.inline.send.results.InlineQueryResultArticle;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.event.Listener;
import pro.zackpollard.telegrambot.api.event.chat.CallbackQueryReceivedEvent;
import pro.zackpollard.telegrambot.api.event.chat.inline.InlineQueryReceivedEvent;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

/**
 * @author stuntguy3000
 */
public class TelegramEventHandler implements Listener {

    @Override
    public void onCommandMessageReceived(CommandMessageReceivedEvent event) {
        String command = event.getCommand();

        // Process the command
        RedditLiveBot.instance.getCommandHandler().executeCommand(command, event);
    }

    @Override
    public void onCallbackQueryReceivedEvent(CallbackQueryReceivedEvent event) {
        String ID = event.getCallbackQuery().getData();
        RedditHandler redditHandler = RedditLiveBot.instance.getRedditHandler();

        long userID = event.getCallbackQuery().getFrom().getId();
        if (!RedditLiveBot.instance.getConfigHandler().getBotSettings().getTelegramAdmins().contains(userID)) {
            event.getCallbackQuery().answer("You are not authorized to do this.", true);
            return;
        }

        if (ID.startsWith("adminStartFeed@")) {
            String feedID = ID.replace("adminStartFeed@", "");
            if (redditHandler.getCurrentLiveThread() == null) {
                event.getCallbackQuery().answer("Staring live thread.", false);
            } else {
                redditHandler.startLiveThread(feedID, feedID);
                event.getCallbackQuery().answer("Unable to start live thread, one is already running!", true);
            }
            return;
        }

        event.getCallbackQuery().answer("Unknown action! Button ID: " + ID, true);
    }

    @Override
    public void onInlineQueryReceived(InlineQueryReceivedEvent event) {
        try {
            // Handle subscription prompting
            InlineQueryResponse.InlineQueryResponseBuilder subscriptionButton;
            InlineQueryResultArticle latestUpdate;
            if (!RedditLiveBot.instance.getSubscriptionHandler().isSubscribed(
                    TelegramHook.getBot().getChat(event.getQuery().getSender().getId()))) {
                subscriptionButton = InlineQueryResponse.builder().switch_pm_text("Click here to subscribe to @RedditLiveBot.").switch_pm_parameter("subscribe");
            } else {
                subscriptionButton = InlineQueryResponse.builder().switch_pm_text("You are subscribed to @RedditLiveBot.");
            }

            // Handle posting of last threads
            if (RedditLiveBot.instance.getRedditHandler().getCurrentLiveThread() == null) {
                // Nothing to post
                latestUpdate = InlineQueryResultArticle.builder()
                        .title("Latest update")
                        .description("Not following any live threads!").thumbUrl(new URL("https://camo.githubusercontent.com/b13830f5a9baecd3d83ef5cae4d5107d25cdbfbe/68747470733a2f2f662e636c6f75642e6769746875622e636f6d2f6173736574732f3732313033382f313732383830352f35336532613364382d363262352d313165332d383964312d3934376632373062646430332e706e67")).hideUrl(true).thumbHeight(512).thumbWidth(512).id("latestNews").inputMessageContent(
                                InputTextMessageContent.builder().messageText(
                                        "*RedditLive is not following any live threads.*").parseMode(ParseMode.MARKDOWN).build()
                        ).build();
            } else {
                // Variables
                LiveThreadBroadcasterTask liveThreadBroadcasterTask = RedditLiveBot.instance.getRedditHandler().getCurrentLiveThread();
                LiveThreadChildrenData lastPost = liveThreadBroadcasterTask.getLastActualPost();

                if (lastPost == null) {
                    latestUpdate = InlineQueryResultArticle.builder()
                            .title("No last post available").description("Try again soon").thumbUrl(new URL("https://camo.githubusercontent.com/b13830f5a9baecd3d83ef5cae4d5107d25cdbfbe/68747470733a2f2f662e636c6f75642e6769746875622e636f6d2f6173736574732f3732313033382f313732383830352f35336532613364382d363262352d313165332d383964312d3934376632373062646430332e706e67")).hideUrl(true).thumbHeight(512).thumbWidth(512).id("latestNews").inputMessageContent(
                                    InputTextMessageContent.builder().messageText("*No last post available.*").parseMode(ParseMode.MARKDOWN).build()
                            ).build();
                } else {
                    long delay = (System.currentTimeMillis() / 1000) - lastPost.getCreated_utc();
                    long minutes = TimeUnit.SECONDS.toMinutes(delay);
                    long seconds = delay - TimeUnit.MINUTES.toSeconds(minutes);

                    String title = "Latest update - Posted " + String.format("%d min, %d sec ago", minutes, seconds);
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
