package me.stuntguy3000.java.redditlivebot.command;

import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

// @author Luke Anderson | stuntguy3000
public class StartLiveCommand extends TelegramCommand {
    public StartLiveCommand(CommandMessageReceivedEvent event) {
        super("startlive", "/startlive [ID] Start a Reddit Live feed\n", event);
        processCommand();
    }

    public void processCommand() {
        String[] args = getEvent().getArgs();
        if (args.length == 1) {
            TelegramHook.getLiveFeedHandler().startFeed(getEvent().getChat(), args[0]);
            return;
        }

        respond(SendableTextMessage.builder().message("Correct Usage: /startlive [ID]").build());
    }
}
    