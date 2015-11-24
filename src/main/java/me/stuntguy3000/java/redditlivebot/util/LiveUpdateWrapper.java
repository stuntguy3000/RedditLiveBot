package me.stuntguy3000.java.redditlivebot.util;

import net.dean.jraw.models.LiveUpdate;

// @author Luke Anderson | stuntguy3000
public class LiveUpdateWrapper implements Comparable {
    private LiveUpdate liveUpdate;
    private long timestamp;

    public LiveUpdateWrapper(LiveUpdate liveUpdate, long timestamp) {
        this.liveUpdate = liveUpdate;
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(Object otherLiveUpdate) {
        if (!(otherLiveUpdate instanceof LiveUpdateWrapper))
            throw new ClassCastException("A Live Update Wrapper expected.");
        long otherTime = ((LiveUpdateWrapper) otherLiveUpdate).getTimestamp();

        if (otherTime == getTimestamp()) {
            return 0;
        }

        return (otherTime > getTimestamp() ? -1 : 1);
    }

    public LiveUpdate getLiveUpdate() {
        return liveUpdate;
    }

    public void setLiveUpdate(LiveUpdate liveUpdate) {
        this.liveUpdate = liveUpdate;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
    