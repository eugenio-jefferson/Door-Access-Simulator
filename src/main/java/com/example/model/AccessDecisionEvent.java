package com.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class AccessDecisionEvent {
    private String userId;
    private boolean isGranted;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    private AccessDirection direction;

    public AccessDecisionEvent(String userId, boolean isGranted, LocalDateTime timestamp, AccessDirection direction) {
        this.userId = userId;
        this.isGranted = isGranted;
        this.timestamp = timestamp;
        this.direction = direction;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isGranted() {
        return isGranted;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public AccessDirection getDirection() {
        return direction;
    }
}
