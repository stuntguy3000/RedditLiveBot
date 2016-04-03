package me.stuntguy3000.java.redditlivebot.command;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.object.Lang;
import me.stuntguy3000.java.redditlivebot.object.command.Command;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

import java.util.Map;


// @author Luke Anderson | stuntguy3000
public class HelpCommand extends Command {
    public HelpCommand() {
        super(RedditLiveBot.getInstance(), "View command help", false, "help", "start", "?");
    }

    public void processCommand(CommandMessageReceivedEvent event) {
        StringBuilder commandHelp = new StringBuilder();

        commandHelp.append("*RedditLiveBot Command Help:*\n");

        for (Map.Entry<String[], Command> command : RedditLiveBot.getInstance().getCommandHandler().getCommands().entrySet()) {
            commandHelp.append(String.format("_%s_: `%s`\n", Lang.strJoin(command.getKey(), ", "), command.getValue().getDescription()));
        }

        Lang.send(event.getChat(), commandHelp.toString());
    }
}