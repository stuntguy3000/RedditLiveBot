package me.stuntguy3000.java.redditlivebot.util;

import lombok.Getter;
import lombok.Setter;
import net.dean.jraw.models.LiveUpdate;

// @author Luke Anderson | stuntguy3000
public class LiveUpdateWrapper implements Comparable {
    @Getter
    @Setter
    private LiveUpdate liveUpdate;

    public LiveUpdateWrapper(LiveUpdate liveUpdate) {
        this.liveUpdate = liveUpdate;
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

    public long getTimestamp() {
        return liveUpdate.getCreatedUtc().getTime();
    }
}
    