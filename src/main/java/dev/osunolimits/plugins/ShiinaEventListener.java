package dev.osunolimits.plugins;

import dev.osunolimits.plugins.events.actions.OnRegisterEvent;
import dev.osunolimits.plugins.events.admin.OnAddDonorEvent;
import dev.osunolimits.plugins.events.admin.OnRankBeatmapEvent;
import dev.osunolimits.plugins.events.clans.OnUserDenyClanEvent;
import dev.osunolimits.plugins.events.clans.OnUserGetKickedClanEvent;
import dev.osunolimits.plugins.events.clans.OnUserJoinClanEvent;
import dev.osunolimits.plugins.events.clans.OnUserRequestJoinClanEvent;
import dev.osunolimits.plugins.events.clans.OnUserUnDenyClanEvent;

public class ShiinaEventListener {

    // ADMIN
    public void onAddDonorEvent(OnAddDonorEvent event) { }

    public void onRankBeatmapEvent(OnRankBeatmapEvent event) { }

    // ACTIONS
    public void onRegisterEvent(OnRegisterEvent event) { }

    // CLANS
    public void onUserJoinClanEvent(OnUserJoinClanEvent event) { }

    public void onUserGetKickedClanEvent(OnUserGetKickedClanEvent event) { }

    public void onUserDenyClanEvent(OnUserDenyClanEvent event) { }

    public void onUserUnDenyClanEvent(OnUserUnDenyClanEvent event) { }

    public void onUserRequestJoinClanEvent(OnUserRequestJoinClanEvent event) { }
}
