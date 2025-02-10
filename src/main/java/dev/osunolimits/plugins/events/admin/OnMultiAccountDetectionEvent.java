package dev.osunolimits.plugins.events.admin;

import dev.osunolimits.plugins.ShiinaEventListener;
import dev.osunolimits.plugins.events.ShiinaEvent;
import lombok.Getter;

@Getter
public class OnMultiAccountDetectionEvent extends ShiinaEvent {

    private int user1;
    private int user2;
    private int level;

    public OnMultiAccountDetectionEvent(int user1, int user2, int level) {
        this.user1 = user1;
        this.user2 = user2;
        this.level = level;
    }

    @Override
    public void call(ShiinaEventListener listener) {
        listener.onMultiAccountDetectionEvent(this);
    }
    
}
