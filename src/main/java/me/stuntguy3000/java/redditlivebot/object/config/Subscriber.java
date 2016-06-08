package me.stuntguy3000.java.redditlivebot.object.config;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author stuntguy3000
 */
@Data
@AllArgsConstructor
public class Subscriber {
    private String userID;
    private String username;
}
