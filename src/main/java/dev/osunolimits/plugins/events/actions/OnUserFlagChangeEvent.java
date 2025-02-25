package dev.osunolimits.plugins.events.actions;

import dev.osunolimits.plugins.ShiinaEventListener;
import dev.osunolimits.plugins.events.ShiinaEvent;
import lombok.Getter;

@Getter
public class OnUserFlagChangeEvent extends ShiinaEvent {

    private int userId;
    private String flag;

    public OnUserFlagChangeEvent(int userId, String flag) {
        this.userId = userId;
        this.flag = flag;
    }

    @Override
    public void call(ShiinaEventListener listener) {
        listener.onUserFlagChangeEvent(this);
    }
    
}
