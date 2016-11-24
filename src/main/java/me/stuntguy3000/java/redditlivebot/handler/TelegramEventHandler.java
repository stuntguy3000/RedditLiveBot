/*
 * MIT License
 *
 * Copyright (c) 2016 Luke Anderson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.stuntguy3000.java.redditlivebot.handler;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.object.AdminInlineCommandType;
import me.stuntguy3000.java.redditlivebot.object.Lang;
import me.stuntguy3000.java.redditlivebot.object.config.Subscriber;
import me.stuntguy3000.java.redditlivebot.object.pagination.PaginatedMessage;
import me.stuntguy3000.java.redditlivebot.object.reddit.livethread.LiveThreadChildrenData;
import me.stuntguy3000.java.redditlivebot.scheduler.LiveThreadBroadcasterTask;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.inline.send.InlineQueryResponse;
import pro.zackpollard.telegrambot.api.chat.inline.send.content.InputTextMessageContent;
import pro.zackpollard.telegrambot.api.chat.inline.send.results.InlineQueryResultArticle;
import pro.zackpollard.telegrambot.api.chat.message.Message;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.Listener;
import pro.zackpollard.telegrambot.api.event.chat.CallbackQueryReceivedEvent;
import pro.zackpollard.telegrambot.api.event.chat.ParticipantJoinGroupChatEvent;
import pro.zackpollard.telegrambot.api.event.chat.inline.InlineQueryReceivedEvent;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;
import pro.zackpollard.telegrambot.api.event.chat.message.TextMessageReceivedEvent;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author stuntguy3000
 */
public class TelegramEventHandler implements Listener {

    private RedditLiveBot instance;

    public TelegramEventHandler() {
        instance = RedditLiveBot.instance;
    }

    @Override
    public void onParticipantJoinGroupChat(ParticipantJoinGroupChatEvent event) {
        if (event.getParticipant().getId() == TelegramHook.getBot().getBotID()) {
            event.getChat().sendMessage(SendableTextMessage.builder()
                    .message("*Hello!*\n\nThis is a quick message to let you know that @RedditLiveBot is set to monitor all chat messages. We *do not* store or log any user messages, as this feature is for admin controls only.\n\nIf you have any issues, feel free to contact @stuntguy3000 or view the source from /source.")
                    .parseMode(ParseMode.MARKDOWN).disableWebPagePreview(true)
                    .build()
            );
        }
    }

    @Override
    public void onCommandMessageReceived(CommandMessageReceivedEvent event) {
        String command = event.getCommand();

        // Process the command
        instance.getCommandHandler().executeCommand(command, event);
    }

    @Override
    public void onTextMessageReceived(TextMessageReceivedEvent event) {
        String message = event.getContent().getContent();
        AdminControlHandler adminControlHandler = instance.getAdminControlHandler();

        if (event.getMessage().getRepliedTo() != null) {
            if (adminControlHandler.getReplyActions().containsKey(event.getMessage().getRepliedTo().getMessageId())) {
                AdminInlineCommandType inlineCommandType = adminControlHandler.getReplyActions().remove(event.getMessage().getRepliedTo().getMessageId());

                if (!instance.isAdmin(event.getMessage().getSender().getId())) {
                    Lang.send(event.getChat(), Lang.ERROR_NOT_ADMIN);
                    return;
                }

                switch (inlineCommandType) {
                    /**
                     * Start following a feed
                     */
                    case START_FOLLOW: {
                        String id = message;

                        RedditLiveBot.instance.getRedditHandler().followLiveThread(id, false);
                        break;
                    }
                    /**
                     * Broadcast a message
                     */
                    case BROADCAST: {
                        Message broadcastMessage = Lang.send(TelegramHook.getRedditLiveChat(), Lang.GENERAL_BROADCAST, event.getMessage().getSender().getUsername(),
                                message.replaceAll("~", "\n"));

                        RedditLiveBot.instance.getSubscriptionHandler().forwardMessage(broadcastMessage);
                    }
                }
            }
        }
    }

    @Override
    public void onCallbackQueryReceivedEvent(CallbackQueryReceivedEvent event) {
        String ID = event.getCallbackQuery().getData();
        long userID = event.getCallbackQuery().getFrom().getId();

        RedditHandler redditHandler = instance.getRedditHandler();

        // Standard admin functionality with the prefix # representing a chat
        if (ID.contains("#")) {
            if (!instance.getConfigHandler().getBotSettings().getTelegramAdmins().contains(userID)) {
                event.getCallbackQuery().answer("You are not authorized to do this.", true);
                return;
            }

            String command = ID.split("#")[0];
            Chat chat = TelegramHook.getBot().getChat(ID.split("#")[1]);

            if (command.equals(AdminInlineCommandType.START_FOLLOW.getCommandID())) {
                /**
                 * Start following a live feed
                 */
                Message message = chat.sendMessage(
                        SendableTextMessage.builder().message(
                                "*Please reply to this message with the live feed you would like to follow.*\n\n" +
                                        "Syntax: `<ID> [title]`"
                        ).parseMode(ParseMode.MARKDOWN).build()
                );

                instance.getAdminControlHandler().addReplyMessage(
                        message, AdminInlineCommandType.START_FOLLOW
                );
            } else if (command.equals(AdminInlineCommandType.STOP_FOLLOW.getCommandID())) {
                /**
                 * Stop following
                 */
                redditHandler.unfollowLiveThread(true);

                chat.sendMessage(
                        SendableTextMessage.builder().message(
                                "*Silently stopped following any current live threads.*"
                        ).parseMode(ParseMode.MARKDOWN).build()
                );
            } else if (command.equals(AdminInlineCommandType.SHOW_SUBS.getCommandID())) {
                /**
                 * Show all subscribers
                 */
                ArrayList<Subscriber> subscriptions = RedditLiveBot.instance.getSubscriptionHandler().getSubscriptions();

                List<String> subList = new ArrayList<>();

                for (Subscriber subscriber : subscriptions) {
                    subList.add(subscriber.getUserID() + " - " + subscriber.getUsername());
                }

                PaginatedMessage paginatedMessage =
                        RedditLiveBot.instance.getPaginationHandler()
                                .createPaginatedMessage(subList, 15);

                paginatedMessage.setMessage(chat.sendMessage(
                        SendableTextMessage.builder()
                                .message(paginatedMessage.getPaginatedList().getCurrentPageContent())
                                .replyMarkup(paginatedMessage.getButtons())
                                .parseMode(ParseMode.NONE)
                                .disableWebPagePreview(true)
                                .build()));
            } else if (command.equals(AdminInlineCommandType.ENABLE_DEBUG.getCommandID())) {
                /**
                 * Enable debug mode
                 */
                RedditLiveBot.DEBUG = true;
                RedditLiveBot.instance.getConfigHandler().saveConfigs();

                chat.sendMessage(
                        SendableTextMessage.builder().message(
                                "*Enabled debug mode.*"
                        ).parseMode(ParseMode.MARKDOWN).build()
                );
            } else if (command.equals(AdminInlineCommandType.DISABLE_DEBUG.getCommandID())) {
                /**
                 * Disable debug mode
                 */
                RedditLiveBot.DEBUG = false;
                RedditLiveBot.instance.getConfigHandler().saveConfigs();

                chat.sendMessage(
                        SendableTextMessage.builder().message(
                                "*Disabled debug mode.*"
                        ).parseMode(ParseMode.MARKDOWN).build()
                );
            } else if (command.equals(AdminInlineCommandType.BROADCAST.getCommandID())) {
                /**
                 * Broadcast a message to all subscribers and the channel
                 */
                Message message = chat.sendMessage(
                        SendableTextMessage.builder().message(
                                "*Please reply to this message with the content you would like to Broadcast.*"
                        ).parseMode(ParseMode.MARKDOWN).build()
                );

                instance.getAdminControlHandler().addReplyMessage(
                        message, AdminInlineCommandType.BROADCAST
                );
            } else if (command.equals(AdminInlineCommandType.RESTART.getCommandID())) {
                /**
                 * Restart the bot
                 */
                chat.sendMessage(
                        SendableTextMessage.builder().message(
                                "*Restarting...*"
                        ).parseMode(ParseMode.MARKDOWN).build()
                );

                instance.shutdown();
            }

            event.getCallbackQuery().answer("", false);
            return;
        } else if (ID.startsWith("f,") || ID.startsWith("fS,")) {
            /**
             * Feed Following
             */
            if (!instance.getConfigHandler().getBotSettings().getTelegramAdmins().contains(userID)) {
                event.getCallbackQuery().answer("You are not authorized to do this.", true);
                return;
            }

            boolean silent = ID.startsWith("fS,");

            /**
             * Manually follow a feed
             */
            String threadID = ID.split(",")[1];
            Message sentMessage = instance.getAdminControlHandler().getUpdateMessages().get(threadID);

            Lang.sendDebug(sentMessage.getChat().getId() + " | CHATNAME");

            redditHandler.followLiveThread(threadID, silent);

            String threadInformation = "*Reddit Live Thread*\n\n" +
                    "*Thread ID:* " + threadID + "\n" +
                    "*Thread URL:* https://reddit.com/live/" + threadID + "\n" +
                    "*Now following this live feed.*";

            TelegramHook.getBot().editMessageText(
                    sentMessage,
                    threadInformation,
                    ParseMode.MARKDOWN, true, null
            );

            event.getCallbackQuery().answer("", false);
            return;
        } else if (ID.startsWith("usrSubscribe:")) {
            /**
             * User Subscriptions
             */
            String userToSubscribe = ID.split(":")[1];
            Chat chat = TelegramHook.getBot().getChat(userToSubscribe);

            RedditLiveBot.instance.getSubscriptionHandler().subscribeChat(chat);
            event.getCallbackQuery().answer("You have subscribed to @RedditLiveBot", false);
        } else {
            /**
             * Pagination via UUID
             */
            String action;
            UUID uuid;

            try {
                uuid = UUID.fromString(ID.split("\\|")[0]);
                action = ID.split("\\|")[1];

                PaginatedMessage paginatedMessage = RedditLiveBot.instance.getPaginationHandler().getMessage(uuid);

                if (paginatedMessage != null) {
                    String content;
                    switch (action) {
                        case "next": {
                            content = paginatedMessage.getPaginatedList().switchToNextPage();
                            break;
                        }
                        case "prev": {
                            content = paginatedMessage.getPaginatedList().switchToPreviousPage();
                            break;
                        }
                        case "ignore": {
                            event.getCallbackQuery().answer("Use Next or Previous to navigate.", true);
                            return;
                        }
                        default: {
                            event.getCallbackQuery().answer("Unable to continue! Contact @stuntguy3000", true);
                            return;
                        }
                    }

                    TelegramHook.getBot().editMessageText(
                            paginatedMessage.getMessage(), content, ParseMode.NONE, false, paginatedMessage.getButtons()
                    );
                    return;
                }
            } catch (Exception ignore) {

            }
        }

        event.getCallbackQuery().answer("Unknown action! Button ID: " + ID, true);
    }

    // TODO: Cleanup, modulate where possible
    @Override
    public void onInlineQueryReceived(InlineQueryReceivedEvent event) {
        try {
            // Handle subscription prompting
            InlineQueryResponse.InlineQueryResponseBuilder subscriptionButton;
            InlineQueryResultArticle latestUpdate;
            if (!instance.getSubscriptionHandler().isSubscribed(
                    TelegramHook.getBot().getChat(event.getQuery().getSender().getId()))) {
                subscriptionButton = InlineQueryResponse.builder().switch_pm_text("Click here to subscribe to @RedditLiveBot.").switch_pm_parameter("subscribe");
            } else {
                subscriptionButton = InlineQueryResponse.builder().switch_pm_text("You are subscribed to @RedditLiveBot.");
            }

            // Handle posting of last threads
            if (instance.getRedditHandler().getCurrentLiveThread() == null) {
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
                    String body = String.format(
                            Lang.LIVE_THREAD_REPOST_UPDATE, liveThreadBroadcasterTask.getThreadID(), lastPost.getAuthor(), lastPost.getBody());

                    latestUpdate = InlineQueryResultArticle.builder()
                            .title(title).description(lastPost.getBody()).thumbUrl(new URL("https://camo.githubusercontent.com/b13830f5a9baecd3d83ef5cae4d5107d25cdbfbe/68747470733a2f2f662e636c6f75642e6769746875622e636f6d2f6173736574732f3732313033382f313732383830352f35336532613364382d363262352d313165332d383964312d3934376632373062646430332e706e67")).hideUrl(true).thumbHeight(512).thumbWidth(512).id("latestNews").inputMessageContent(
                                    InputTextMessageContent.builder().messageText(body).parseMode(ParseMode.MARKDOWN).build()
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
