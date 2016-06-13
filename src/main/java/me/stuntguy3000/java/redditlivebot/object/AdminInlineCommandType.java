package me.stuntguy3000.java.redditlivebot.object;

import lombok.Getter;

/**
 * @author stuntguy3000
 */
public enum AdminInlineCommandType {
    START_FOLLOW("startFollow"),
    STOP_FOLLOW("stopFollow"),
    SHOW_SUBS("showSubs"),
    ENABLE_DEBUG("enableDebug"),
    DISABLE_DEBUG("disableDebug"),
    BROADCAST("broadcast"),
    RESTART("restart");

    @Getter
    String commandID;

    AdminInlineCommandType(String commandID) {
        this.commandID = commandID;
    }
}
