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
        String[] args = event.getArgs();

        if (args.length == 0) {
            Lang.send(event.getChat(), RedditLiveBot.getInstance().getCommandHandler().getBotFatherString());
        } else if (args[0].equalsIgnoreCase("follow")) {
            if (args.length == 3) {
                RedditLiveBot.getInstance().getRedditHandler().startLiveThread(args[1], args[2]);
            } else {
                Lang.send(event.getChat(), Lang.ERROR_NOT_ENOUGH_ARGUMENTS);
            }
        }
    }
}
    