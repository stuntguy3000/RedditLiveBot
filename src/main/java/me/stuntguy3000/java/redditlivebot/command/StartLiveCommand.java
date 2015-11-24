package me.stuntguy3000.java.redditlivebot.command;

import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

// @author Luke Anderson | stuntguy3000
public class StartLiveCommand extends TelegramCommand {
    public StartLiveCommand(CommandMessageReceivedEvent event) {
        super("startlive", "/startlive [ID] (Update Period) Start a Reddit Live feed\n", event);
        processCommand();
    }

    public void processCommand() {
        String[] args = getEvent().getArgs();
        if (args.length > 0 && args.length < 3) {
            respond(SendableTextMessage.builder().message("Starting Live Feed: " + args[0]).build());
            return;
        }

        respond(SendableTextMessage.builder().message("Correct Usage: /startlive [ID] (Update Period)").build());
    }
}
    