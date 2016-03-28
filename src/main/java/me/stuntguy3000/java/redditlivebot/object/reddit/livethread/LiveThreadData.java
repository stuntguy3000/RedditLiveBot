package me.stuntguy3000.java.redditlivebot.object.reddit.livethread;

import lombok.Data;

import java.util.ArrayList;

// @author Luke Anderson | stuntguy3000
@Data
public class LiveThreadData {
    private ArrayList<LiveThreadChildren> children;
    private String modhash;
}
    