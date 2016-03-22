package me.stuntguy3000.java.redditlivebot.command.admin;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.model.commandtypes.TelegramAdminCommand;
import me.stuntguy3000.java.redditlivebot.util.DateUtil;
import net.dean.jraw.RedditClient;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

import java.util.Date;

/**
 * Created by amir on 2016-01-02.
 */
public class AdminDebugCommand extends TelegramAdminCommand {
    public AdminDebugCommand(RedditLiveBot instance) {
        super(instance, "Debug", "List debug information.");
    }

    @Override
    public void processCommand(CommandMessageReceivedEvent event) {
        Chat chat = event.getChat();

        StringBuilder debugMessages = new StringBuilder();

        RedditClient redditClient = RedditLiveBot.getInstance().getRedditClient();

        debugMessages.append("*Debug Information:*\n");
        debugMessages.append("*Live Threads:* `").append(RedditLiveBot.getInstance().getConfigHandler().getLiveThreads().getActiveChats()).append("`\n");
        debugMessages.append("*Reddit isAuthenticated:* `").append(redditClient.isAuthenticated()).append("`\n");
        debugMessages.append("*Reddit OAuth Access Code:* `").append(redditClient.getOAuthData().getAccessToken()).append("`\n");
        debugMessages.append("*Reddit OAuth Refresh Code:* `").append(redditClient.getOAuthData().getRefreshToken()).append("`\n");
        debugMessages.append("*Reddit OAuth Expiration Time:* `")
                .append(DateUtil.printDifference(new Date(), redditClient.getOAuthData().getExpirationDate())).append("`\n");

        SendableTextMessage.SendableTextMessageBuilder sendableTextMessageBuilder = SendableTextMessage.builder();
        sendableTextMessageBuilder.message(debugMessages.toString());
        sendableTextMessageBuilder.disableNotification(true);
        sendableTextMessageBuilder.parseMode(ParseMode.MARKDOWN);

        chat.sendMessage(sendableTextMessageBuilder.build(), TelegramHook.getBot());
    }
}
