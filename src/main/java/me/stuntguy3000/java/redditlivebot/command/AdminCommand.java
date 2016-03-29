package me.stuntguy3000.java.redditlivebot.command;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.object.Lang;
import me.stuntguy3000.java.redditlivebot.object.command.Command;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

// @author Luke Anderson | stuntguy3000
public class AdminCommand extends Command {

    public AdminCommand() {
        super(RedditLiveBot.getInstance(), "Command used by RedditLiveBot administrators.", true, "admin");
    }

    @Override
    public void processCommand(CommandMessageReceivedEvent event) {
        Lang.send(event.getChat(), "Fetching data for you, %s", event.getMessage().getSender().getUsername());
    }
}
    