package me.stuntguy3000.java.redditlivebot.object.reddit.redditthread;

import lombok.Data;

// @author Luke Anderson | stuntguy3000
@Data
public class RedditThreadChildrenData {
    private String author;
    private long created_utc;
    private RedditThreadChildrenDataMedia media;
    private int score;
    private String title;
}
    