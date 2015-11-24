package me.stuntguy3000.java.redditlivebot.command;

import pro.zackpollard.telegrambot.api.TelegramBot;

// @author Luke Anderson | stuntguy3000
public class StartLiveCommand {
    private TelegramCommand telegramCommand;
    private TelegramBot telegramBot;

    public StartLiveCommand(TelegramCommand telegramCommand) {
        this.telegramCommand = telegramCommand;
        this.telegramBot = telegramCommand.getBot();
        processCommand();
    }

    public void processCommand() {

    }
}
    