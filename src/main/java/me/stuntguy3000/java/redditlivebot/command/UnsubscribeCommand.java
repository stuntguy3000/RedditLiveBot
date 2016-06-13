package me.stuntguy3000.java.redditlivebot.command;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.handler.SubscriptionHandler;
import me.stuntguy3000.java.redditlivebot.object.Lang;
import me.stuntguy3000.java.redditlivebot.object.command.Command;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

// @author Luke Anderson | stuntguy3000
public class UnsubscribeCommand extends Command {

    public UnsubscribeCommand() {
        super("Subscribe to RedditLive updates.", false, "unsubscribe");
    }

    @Override
    public void processCommand(CommandMessageReceivedEvent event) {
        SubscriptionHandler subscriptionHandler = RedditLiveBot.instance.getSubscriptionHandler();

        Chat chat = event.getMessage().getChat();

        if (!subscriptionHandler.isSubscribed(chat)) {
            Lang.send(chat, Lang.ERROR_CHAT_NOT_SUBSCRIBED);
        } else {
            Lang.send(chat, Lang.CHAT_UNSUBSCRIBED);
            subscriptionHandler.unsubscribeChat(chat.getId());
        }
    }
}
    