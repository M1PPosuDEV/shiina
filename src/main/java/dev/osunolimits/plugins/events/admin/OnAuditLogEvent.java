package dev.osunolimits.plugins.events.admin;

import dev.osunolimits.plugins.ShiinaEventListener;
import dev.osunolimits.plugins.events.ShiinaEvent;
import lombok.Getter;

@Getter
public class OnAuditLogEvent extends ShiinaEvent{
    private String type;
    private Integer userId;
    private Integer targetId;
    private String[] privs;
    private String reason;
    private Integer status;
    private Integer mode;

    public OnAuditLogEvent(String type, Integer userId, Integer targetId, String[] privs, String reason, Integer status, Integer mode) {
        this.type = type;
        this.userId = userId;
        this.targetId = targetId;
        this.privs = privs;
        this.reason = reason;
        this.status = status;
        this.mode = mode;
    }

    @Override
    public void call(ShiinaEventListener listener) {
        listener.onAuditLogEvent(this);
    }
}
