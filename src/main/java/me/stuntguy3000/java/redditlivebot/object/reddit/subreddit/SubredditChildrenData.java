package me.stuntguy3000.java.redditlivebot.object.reddit.subreddit;

import lombok.Data;

// @author Luke Anderson | stuntguy3000
@Data
public class SubredditChildrenData {
    private String author;
    private long created_utc;
    private SubredditChildrenDataMedia media;
    private int score;
    private String title;
}
    