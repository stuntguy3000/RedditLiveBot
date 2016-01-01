package me.stuntguy3000.java.redditlivebot.command.normal;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.model.commandtypes.TelegramNormalCommand;
import me.stuntguy3000.java.redditlivebot.scheduler.LiveFeedUpdateTask;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

// @author Luke Anderson | stuntguy3000
public class StatusCommand extends TelegramNormalCommand {
    public StatusCommand(RedditLiveBot instance) {
        super(instance, "status", "/status Reddit Live Bot status for the current channel");
    }

    @Override
    public void processCommand(CommandMessageReceivedEvent event) {
        Chat chat = event.getChat();

        LiveFeedUpdateTask liveFeedUpdateTask = TelegramHook.getLiveFeedHandler().getFeedTimer(chat);

        if (liveFeedUpdateTask == null) {
            respond(chat, "No Reddit Live feed is active in this channel.");
        } else {
            respond(chat, SendableTextMessage.builder().message("Following Reddit Live feed "
                    + "[" + liveFeedUpdateTask.getFeedID() + "](https://www.reddit.com/live/" + liveFeedUpdateTask.getFeedID() + ").")
                    .parseMode(ParseMode.MARKDOWN).disableWebPagePreview(true).build());
        }
    }
}
    