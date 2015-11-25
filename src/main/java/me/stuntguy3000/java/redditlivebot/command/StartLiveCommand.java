package me.stuntguy3000.java.redditlivebot.command;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

// @author Luke Anderson | stuntguy3000
public class StartLiveCommand extends TelegramCommand {
    public StartLiveCommand(RedditLiveBot instance) {
        super(instance, "startlive", "/startlive [ID] Start a Reddit Live feed\n");
    }

    public void processCommand(CommandMessageReceivedEvent event) {
        Chat chat = event.getChat();

        String[] args = event.getArgs();
        if (args.length == 1) {
            TelegramHook.getLiveFeedHandler().startFeed(event.getChat(), args[0]);
            return;
        }

        respond(chat, SendableTextMessage.builder().message("Correct Usage: /startlive [ID]").build());
    }


}
    