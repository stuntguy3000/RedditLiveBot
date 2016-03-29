package me.stuntguy3000.java.redditlivebot.object.reddit.livethread;

import lombok.Data;

import java.util.UUID;

// @author Luke Anderson | stuntguy3000
@Data
public class LiveThreadChildrenData {
    private String author;
    private String body;
    private long created_utc;
    private UUID id;
    private boolean stricken;
}
    