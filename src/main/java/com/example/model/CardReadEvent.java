package com.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class CardReadEvent {
    private String userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    private AccessDirection direction;

    public CardReadEvent() {
    }

    public CardReadEvent(String userId, LocalDateTime timestamp, AccessDirection direction) {
        this.userId = userId;
        this.timestamp = timestamp;
        this.direction = direction;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public AccessDirection getDirection() {
        return direction;
    }

    public void setDirection(AccessDirection direction) {
        this.direction = direction;
    }
}
