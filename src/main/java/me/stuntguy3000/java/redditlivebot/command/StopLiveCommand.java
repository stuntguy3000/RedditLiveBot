package me.stuntguy3000.java.redditlivebot.command;

import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.scheduler.LiveFeedUpdateTask;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

// @author Luke Anderson | stuntguy3000
public class StopLiveCommand extends TelegramCommand {
    public StopLiveCommand(CommandMessageReceivedEvent event) {
        super("stoplive", "/stoplive [ID] Cancel the Reddit Live feed\n", event);
        processCommand();
    }

    @Override
    public void processCommand() {
        Chat chat = getEvent().getChat();
        LiveFeedUpdateTask liveFeedUpdateTask = TelegramHook.getLiveFeedHandler().getFeedTimer(chat);

        if (liveFeedUpdateTask == null) {
            respond("No Reddit Live feed is active in this channel.");
        } else {
            respond(SendableTextMessage.builder().message("Reddit Live feed "
                    + "[" + liveFeedUpdateTask.getFeedID() + "](https://www.reddit.com/live/" + liveFeedUpdateTask.getFeedID() + ") has been stopped.")
                    .parseMode(ParseMode.MARKDOWN).disableWebPagePreview(true).build());
            TelegramHook.getLiveFeedHandler().stop(chat);
        }
    }
}
    