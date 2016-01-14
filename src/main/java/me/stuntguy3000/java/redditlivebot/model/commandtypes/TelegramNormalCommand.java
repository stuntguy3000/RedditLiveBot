package me.stuntguy3000.java.redditlivebot.model.commandtypes;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.model.CommandType;
import me.stuntguy3000.java.redditlivebot.model.TelegramCommand;

/**
 * Created by amir on 2016-01-02.
 */
public abstract class TelegramNormalCommand extends TelegramCommand {
    public TelegramNormalCommand(RedditLiveBot instance, String name, String description) {
        super(instance, name, description, CommandType.NORMAL);
    }
}
