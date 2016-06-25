package me.stuntguy3000.java.redditlivebot.object.reddit.subreddit;

import java.util.ArrayList;

import lombok.Data;

// @author Luke Anderson | stuntguy3000
@Data
public class SubredditData {
    private ArrayList<SubredditChildren> children;
    private String modhash;
}
    