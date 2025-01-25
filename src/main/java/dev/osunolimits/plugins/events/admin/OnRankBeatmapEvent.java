package dev.osunolimits.plugins.events.admin;

import dev.osunolimits.plugins.ShiinaEventListener;
import dev.osunolimits.plugins.events.ShiinaEvent;
import lombok.Getter;

@Getter
public class OnRankBeatmapEvent extends ShiinaEvent {

    private int beatmapId;
    private int status;
    private boolean frozen;
    private int adminId;

    public OnRankBeatmapEvent(int beatmapId, int status, boolean frozen, int adminId) {
        this.beatmapId = beatmapId;
        this.status = status;
        this.frozen = frozen;
        this.adminId = adminId;
    }

    @Override
    public void call(ShiinaEventListener listener) {
        listener.onRankBeatmapEvent(this);
    }
    
}
