package me.stuntguy3000.java.redditlivebot.hook;

import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.event.Listener;
import pro.zackpollard.telegrambot.api.event.chat.message.TextMessageReceivedEvent;

// @author Luke Anderson | stuntguy3000
public class TelegramHook implements Listener {
    private TelegramBot bot;

    public TelegramHook(String authKey) {
        this.bot = TelegramBot.login(authKey);
        this.bot.getEventsManager().register(this);
        System.out.println("Connected to Telegram.");
    }

    @Override
    public void onTextMessageReceived(TextMessageReceivedEvent event) {
        System.out.println("Message Recieved.");
        event.getChat().sendMessage("The bot is working, " + event.getMessage().getSender().getUsername() + ".", bot);
    }

    public TelegramBot getBot() {
        return bot;
    }
}
    