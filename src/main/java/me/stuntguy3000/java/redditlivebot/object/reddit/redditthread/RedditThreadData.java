package me.stuntguy3000.java.redditlivebot.object.reddit.redditthread;

import lombok.Data;

import java.util.ArrayList;

// @author Luke Anderson | stuntguy3000
@Data
public class RedditThreadData {
    private ArrayList<RedditThreadChildren> children;
    private String modhash;
}
    