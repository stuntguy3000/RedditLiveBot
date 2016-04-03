package me.stuntguy3000.java.redditlivebot.object.config;

import lombok.Data;

import java.util.ArrayList;

// @author Luke Anderson | stuntguy3000
@Data
public class Subscriptions {
    private ArrayList<String> subscriptions = new ArrayList<>();
}