package dev.osunolimits.plugins.events.actions;

import dev.osunolimits.plugins.ShiinaEventListener;
import dev.osunolimits.plugins.events.ShiinaEvent;
import lombok.Getter;

@Getter
public class OnBeatmapFavoriteEvent extends ShiinaEvent{
    
    private int userId;
    private int beatmapSetId;
    private boolean isFavorite;

    public OnBeatmapFavoriteEvent(int userId, int beatmapSetId, boolean isFavorite) {
        this.userId = userId;
        this.beatmapSetId = beatmapSetId;
        this.isFavorite = isFavorite;
    }

    @Override
    public void call(ShiinaEventListener listener) {
        listener.onBeatmapFavoriteEvent(this);
    }
}
