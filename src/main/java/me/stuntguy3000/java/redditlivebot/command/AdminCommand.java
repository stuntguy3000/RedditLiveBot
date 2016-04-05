package me.stuntguy3000.java.redditlivebot.command;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.handler.RedditHandler;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.object.Lang;
import me.stuntguy3000.java.redditlivebot.object.command.Command;
import me.stuntguy3000.java.redditlivebot.scheduler.LiveThreadTask;
import me.stuntguy3000.java.redditlivebot.scheduler.NewLiveThreadsTask;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.Chat;
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

        } else if (args[0].equalsIgnoreCase("follow")) {
            if (args.length == 3) {
                RedditLiveBot.getInstance().getRedditHandler().startLiveThread(args[1], args[2]);
            } else {
                Lang.send(event.getChat(), Lang.ERROR_NOT_ENOUGH_ARGUMENTS);
            }
        }

        switch (args.length) {
            case 0: {
                Lang.send(event.getChat(), "*RedditLive Admin Help:*\n" +
                        "*/admin follow <id> <name>* - Manually follow a live thread\n" +
                        "*/admin unfollow* - Unfollows the current live thread\n" +
                        "*/admin restart* - Restarts the bot\n" +
                        "*/admin subscriptions* - View subscriptions data\n" +
                        "*/admin broadcast <message>* - Broadcast a message to all subscribers\n" +
                        "*/admin debug [on/off]* - View or toggle debug mode\n" +
                        "*/admin status* - Status of RedditLive's Subscription Service");
                return;
            }
            case 1: {
                if (args[0].equalsIgnoreCase("follow")) {
                    Lang.send(event.getChat(), Lang.ERROR_NOT_ENOUGH_ARGUMENTS);
                } else if (args[0].equalsIgnoreCase("unfollow")) {
                    Lang.send(event.getChat(), Lang.ERROR_NOT_ENOUGH_ARGUMENTS);
                } else if (args[0].equalsIgnoreCase("debug")) {
                    Lang.send(event.getChat(), Lang.COMMAND_ADMIN_DEBUG, RedditLiveBot.DEBUG);
                } else if (args[0].equalsIgnoreCase("restart")) {
                    Lang.sendAdmin(Lang.GENERAL_RESTART, event.getMessage().getSender().getUsername());
                    RedditLiveBot.getInstance().shutdown();
                } else if (args[0].equalsIgnoreCase("status")) {
                    RedditHandler redditHandler = RedditLiveBot.getInstance().getRedditHandler();
                    LiveThreadTask liveThreadTask = redditHandler.getCurrentLiveThread();
                    NewLiveThreadsTask newLiveThreadsTask = redditHandler.getNewLiveThreadsTask();

                    String redditData = Lang.COMMAND_ADMIN_STATUS + "*Current Live Thread: *" + (liveThreadTask == null ? "None!" : liveThreadTask.getThreadID()) +
                            "\n*New Live Thread Scanner: *" +
                            (newLiveThreadsTask == null ? "Not Scanning" : "Scanning");

                    Lang.send(event.getChat(), redditData);
                } else if (args[0].equalsIgnoreCase("subscriptions")) {
                    String[] subscriptions = (String[]) RedditLiveBot.getInstance().getSubscriptionHandler().getSubscriptions().toArray();
                    Lang.sendAdmin(Lang.COMMAND_ADMIN_SUBSCRIPTIONS,
                            subscriptions.length,
                            Lang.stringJoin(subscriptions,
                                    null, ", "));
                }
                return;
            }
            case 2: {
                if (args[0].equalsIgnoreCase("follow")) {
                    Lang.send(event.getChat(), Lang.ERROR_NOT_ENOUGH_ARGUMENTS);
                } else if (args[0].equalsIgnoreCase("debug")) {
                    boolean newDebug = Boolean.valueOf(args[1]);

                    RedditLiveBot.DEBUG = newDebug;
                    RedditLiveBot.getInstance().getConfigHandler().getBotSettings().setDebugMode(newDebug);

                    Lang.send(event.getChat(), Lang.COMMAND_ADMIN_DEBUG_TOGGLE, newDebug);
                    Lang.sendAdmin(Lang.COMMAND_ADMIN_DEBUG_TOGGLE, RedditLiveBot.DEBUG);
                }
                return;
            }
            default: {
                if (args.length >= 3 && args[0].equalsIgnoreCase("follow")) {
                    RedditLiveBot.getInstance().getRedditHandler().startLiveThread(args[1], args[2]);
                } else if (args.length > 1 && args[0].equalsIgnoreCase("broadcast")) {
                    StringBuilder broadcastMessage = new StringBuilder();

                    for (int i = 1; i < args.length; i++) {
                        broadcastMessage.append(args[i]).append(" ");
                    }

                    for (String chatID : RedditLiveBot.getInstance().getSubscriptionHandler().getSubscriptions()) {
                        Chat chat = TelegramBot.getChat(chatID);

                        Lang.send(chat, Lang.GENERAL_BROADCAST, event.getMessage().getSender().getUsername(),
                                broadcastMessage.toString().replaceAll("~", "\n"));
                    }

                    Lang.send(TelegramHook.getRedditLiveChat(), Lang.GENERAL_BROADCAST, event.getMessage().getSender().getUsername(),
                            broadcastMessage.toString().replaceAll("~", "\n"));
                }
            }
        }
    }
}
    