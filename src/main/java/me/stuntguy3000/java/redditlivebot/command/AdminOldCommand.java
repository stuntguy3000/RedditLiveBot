package me.stuntguy3000.java.redditlivebot.command;

import java.util.ArrayList;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.handler.RedditHandler;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import me.stuntguy3000.java.redditlivebot.object.Lang;
import me.stuntguy3000.java.redditlivebot.object.command.Command;
import me.stuntguy3000.java.redditlivebot.object.config.Subscriber;
import me.stuntguy3000.java.redditlivebot.scheduler.LiveThreadBroadcasterTask;
import me.stuntguy3000.java.redditlivebot.scheduler.RedditScannerTask;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

// @author Luke Anderson | stuntguy3000
public class AdminOldCommand extends Command {

    public AdminOldCommand() {
        super("Command used by RedditLiveBot administrators.", true, "adminOld");
    }

    @Override
    public void processCommand(CommandMessageReceivedEvent event) {
        String[] args = event.getArgs();

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
                    return;
                } else if (args[0].equalsIgnoreCase("unfollow")) {
                    RedditLiveBot.instance.getRedditHandler().stopLiveThread(false);
                    Lang.send(event.getChat(), Lang.COMMAND_ADMIN_UNFOLLOW);
                    return;
                } else if (args[0].equalsIgnoreCase("debug")) {
                    Lang.send(event.getChat(), Lang.COMMAND_ADMIN_DEBUG, RedditLiveBot.DEBUG);
                    return;
                } else if (args[0].equalsIgnoreCase("restart")) {
                    Lang.send(event.getChat(), Lang.GENERAL_RESTART, event.getMessage().getSender().getUsername());
                    RedditLiveBot.instance.shutdown();
                    return;
                } else if (args[0].equalsIgnoreCase("status")) {
                    RedditHandler redditHandler = RedditLiveBot.instance.getRedditHandler();
                    LiveThreadBroadcasterTask liveThreadBroadcasterTask = redditHandler.getCurrentLiveThread();
                    RedditScannerTask redditScannerTask = redditHandler.getRedditScannerTask();

                    String redditData = Lang.COMMAND_ADMIN_STATUS +
                            "*Current Live Thread: *" + (liveThreadBroadcasterTask == null ? "None!" : "http://reddit.com/live/" + liveThreadBroadcasterTask.getThreadID()) +
                            "\n*New Live Thread Scanner: *" + (redditScannerTask == null ? "Not Scanning" : "Scanning") +
                            "\n*Last post: *" + (liveThreadBroadcasterTask == null ? "C >> " + RedditLiveBot.instance.getConfigHandler().getBotSettings().getLastPost() : "I >> " + liveThreadBroadcasterTask.getLastPost());

                    Lang.send(event.getChat(), redditData);
                    return;
                } else if (args[0].equalsIgnoreCase("subscriptions")) {
                    ArrayList<Subscriber> subscriptions = RedditLiveBot.instance.getSubscriptionHandler().getSubscriptions();

                    StringBuilder stringBuilder = new StringBuilder();

                    for (Subscriber subscriber : subscriptions) {
                        stringBuilder.append(subscriber.getUserID()).append("(").append(subscriber.getUsername()).append(")");
                        stringBuilder.append("\n");
                    }

                    Lang.send(event.getChat(), Lang.COMMAND_ADMIN_SUBSCRIPTIONS, subscriptions.size(), stringBuilder.toString());
                    return;
                }
                break;
            }
            case 2: {
                if (args[0].equalsIgnoreCase("follow")) {
                    Lang.send(event.getChat(), Lang.ERROR_NOT_ENOUGH_ARGUMENTS);
                    return;
                } else if (args[0].equalsIgnoreCase("debug")) {
                    boolean newDebug = Boolean.valueOf(args[1]);

                    RedditLiveBot.DEBUG = newDebug;
                    RedditLiveBot.instance.getConfigHandler().getBotSettings().setDebugMode(newDebug);

                    Lang.send(event.getChat(), Lang.COMMAND_ADMIN_DEBUG_TOGGLE, newDebug);
                    Lang.sendAdmin(Lang.COMMAND_ADMIN_DEBUG_TOGGLE, RedditLiveBot.DEBUG);
                    return;
                }
                break;
            }
            default: {
                if (args.length >= 3 && args[0].equalsIgnoreCase("follow")) {
                    RedditLiveBot.instance.getRedditHandler().startLiveThread(args[1], args[2], false);
                    return;
                } else {
                    if (args.length > 1 && args[0].equalsIgnoreCase("broadcast")) {
                        StringBuilder broadcastMessage = new StringBuilder();

                        for (int i = 1; i < args.length; i++) {
                            broadcastMessage.append(args[i]).append(" ");
                        }

                        for (Subscriber subscriber : RedditLiveBot.instance.getSubscriptionHandler().getSubscriptions()) {
                            Chat chat = TelegramHook.getBot().getChat(subscriber.getUserID());

                            Lang.send(chat, Lang.GENERAL_BROADCAST, event.getMessage().getSender().getUsername(),
                                    broadcastMessage.toString().replaceAll("~", "\n"));
                        }

                        Lang.send(TelegramHook.getRedditLiveChat(), Lang.GENERAL_BROADCAST, event.getMessage().getSender().getUsername(),
                                broadcastMessage.toString().replaceAll("~", "\n"));
                        return;
                    }
                }
            }
        }

        Lang.send(event.getChat(), Lang.ERROR_COMMAND_INVALID);
    }
}
    