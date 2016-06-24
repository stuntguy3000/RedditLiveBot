package me.stuntguy3000.java.redditlivebot.scheduler;

import lombok.AllArgsConstructor;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.Message;

/**
 * @author stuntguy3000
 */
@AllArgsConstructor
public class SendMessageTask implements Runnable {

    private final Message message;
    private final String chatID;

    @Override
    public void run() {
        Chat chat = TelegramHook.getBot().getChat(chatID);
        message.forwardMessage(chat);
    }
}
