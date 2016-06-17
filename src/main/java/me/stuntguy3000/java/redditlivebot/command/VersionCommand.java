package me.stuntguy3000.java.redditlivebot.command;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.handler.JenkinsUpdateHandler;
import me.stuntguy3000.java.redditlivebot.object.Emoji;
import me.stuntguy3000.java.redditlivebot.object.Lang;
import me.stuntguy3000.java.redditlivebot.object.command.Command;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;


// @author Luke Anderson | stuntguy3000
public class VersionCommand extends Command {
    public VersionCommand() {
        super("View the bot's current version", false, "version", "about", "info", "source");
    }

    public void processCommand(CommandMessageReceivedEvent event) {
        Chat chat = event.getChat();

        JenkinsUpdateHandler.UpdateInformation updateInformation = RedditLiveBot.instance.getJenkinsUpdateHandler().getLastUpdate();

        String buildInfo = "No build information available...";
        if (!(updateInformation == null || updateInformation.getGitCommitAuthors() == null || updateInformation.getGitCommitAuthors().isEmpty())) {
            buildInfo = String.format("*Build:* %d\n\n" +
                            "*Last commit information:*\n" +
                            "*Description:* %s\n" +
                            "*Author:* %s\n" +
                            "*Commit ID:* %s\n",
                    updateInformation.getBuildNumber(),
                    updateInformation.getGitCommitMessages().get(0),
                    updateInformation.getGitCommitAuthors().get(0),
                    "[" + updateInformation.getGitCommitIds().get(0) + "]" +
                            "(https://github.com/stuntguy3000/RedditLive/commit/"
                            + updateInformation.getGitCommitIds().get(0) + ")");
        }

        Lang.send(chat, Emoji.JOKER_CARD.getText() + " *RedditLiveBot by* @stuntguy3000\n" +
                buildInfo +
                "\nSource [Available on GitHub](https://github.com/stuntguy3000/redditlive)\n" +
                "Created using @zackpollard's [Java Telegram API](https://github.com/zackpollard/JavaTelegramBot-API)");

    }
}