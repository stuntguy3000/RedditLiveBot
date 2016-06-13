package me.stuntguy3000.java.redditlivebot.command;

import java.util.Map;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.object.Lang;
import me.stuntguy3000.java.redditlivebot.object.command.Command;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;


// @author Luke Anderson | stuntguy3000
public class HelpCommand extends Command {
    public HelpCommand() {
        super("View command help", false, "help", "start", "?");
    }

    public void processCommand(CommandMessageReceivedEvent event) {
        StringBuilder commandHelp = new StringBuilder();

        if (event.getArgs().length > 0) {
            if (event.getArgs()[0].equalsIgnoreCase("subscribe")) {
                new SubscribeCommand().processCommand(event);
                return;
            }
        }

        commandHelp.append("*RedditLiveBot Command Help:*\n");

        for (Map.Entry<String[], Command> command : RedditLiveBot.instance.getCommandHandler().getCommands().entrySet()) {
            commandHelp.append(String.format("%s:  _%s_\n", Lang.stringJoin(command.getKey(), "/", ", "), command.getValue().getDescription()));
        }

        Lang.send(event.getChat(), commandHelp.toString());
    }
}