package dev.osunolimits.plugins.events.clans;

import dev.osunolimits.plugins.ShiinaEventListener;
import dev.osunolimits.plugins.events.ShiinaEvent;
import lombok.Getter;

@Getter
public class OnUserDenyClanEvent extends ShiinaEvent {

    private int clanId;
    private int userId;
    private int adminId;

    public OnUserDenyClanEvent(int clanId, int userId, int adminId) {
        this.clanId = clanId;
        this.userId = userId;
        this.adminId = adminId;
    }

    @Override
    public void call(ShiinaEventListener listener) {
        listener.onUserDenyClanEvent(this);
    }
    
    
}
