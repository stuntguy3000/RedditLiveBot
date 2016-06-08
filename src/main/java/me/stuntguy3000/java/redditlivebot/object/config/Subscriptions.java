package me.stuntguy3000.java.redditlivebot.object.config;

import java.util.ArrayList;

import lombok.Data;

// @author Luke Anderson | stuntguy3000
@Data
public class Subscriptions {
    private ArrayList<Subscriber> subscriptions = new ArrayList<>();
}