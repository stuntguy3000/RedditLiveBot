package me.stuntguy3000.java.redditlivebot.command.admin;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.model.commandtypes.TelegramAdminCommand;
import me.stuntguy3000.java.redditlivebot.util.DateUtil;
import net.dean.jraw.RedditClient;
import pro.zackpollard.telegrambot.api.chat.Chat;
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

        debugMessages.append("*Debug Information: *");
        debugMessages.append("*Reddit isAuthenticated:* `").append(redditClient.isAuthenticated()).append("`");
        debugMessages.append("*Reddit OAuth Access Code:* `").append(redditClient.getOAuthData().getAccessToken()).append("`");
        debugMessages.append("*Reddit OAuth Refresh Code:* `").append(redditClient.getOAuthData().getRefreshToken()).append("`");
        debugMessages.append("*Reddit OAuth Expiration Time:* `")
                .append(DateUtil.printDifference(redditClient.getOAuthData().getExpirationDate(), new Date())).append("`");

        SendableTextMessage.SendableTextMessageBuilder sendableTextMessageBuilder = SendableTextMessage.builder();
        sendableTextMessageBuilder.message(debugMessages.toString());

        chat.sendMessage(sendableTextMessageBuilder.build(), TelegramHook.getBot());
    }
}
