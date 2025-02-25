package dev.osunolimits.plugins.events.actions;

import dev.osunolimits.plugins.ShiinaEventListener;
import dev.osunolimits.plugins.events.ShiinaEvent;
import lombok.Getter;

@Getter
public class OnUserNameChangeEvent extends ShiinaEvent{
    
    private int userId;
    private String oldName;
    private String newName;

    public OnUserNameChangeEvent(int userId, String oldName, String newName) {
        this.userId = userId;
        this.oldName = oldName;
        this.newName = newName;
    }
    
    @Override
    public void call(ShiinaEventListener listener) {
        listener.onUserNameChangeEvent(this);
    }
}
