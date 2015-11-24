package me.stuntguy3000.java.redditlivebot.command;

import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.event.chat.message.TextMessageReceivedEvent;

public class TelegramCommand {
    private String fullMessage;
    private String[] args;
    private TextMessageReceivedEvent event;
    private TelegramBot bot;

    public TelegramCommand(String fullMessage, String[] args, TextMessageReceivedEvent event, TelegramBot bot) {
        this.fullMessage = fullMessage;
        this.args = args;
        this.event = event;
        this.bot = bot;
    }

    public TelegramBot getBot() {
        return bot;
    }

    public String getFullMessage() {
        return fullMessage;
    }

    public void setFullMessage(String fullMessage) {
        this.fullMessage = fullMessage;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public TextMessageReceivedEvent getEvent() {
        return event;
    }

    public void setEvent(TextMessageReceivedEvent event) {
        this.event = event;
    }
}
