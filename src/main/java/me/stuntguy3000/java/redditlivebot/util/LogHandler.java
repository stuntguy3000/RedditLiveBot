package me.stuntguy3000.java.redditlivebot.util;

/**
 * Created by amir on 2015-11-25.
 */
public class LogHandler {

    public static void log(String s, Object... format) {
        String msg = String.format(s, format);
        System.out.println(msg);
    }

    public static void logn(String s, Object... format) {
        log(s + "%n", format);
    }
}
