package com.example.service;

import com.example.config.MqttConfig;
import com.example.model.AccessDecisionEvent;
import com.example.model.CardReadEvent;
import com.example.mqtt.MqttClientManager;

import java.util.HashSet;
import java.util.Set;

public class AuthorizationSubscriber {

    private final MqttClientManager mqttClientManager;
    private final Set<String> authorizedUsers;

    public AuthorizationSubscriber(MqttClientManager mqttClientManager) {
        this.mqttClientManager = mqttClientManager;
        this.authorizedUsers = new HashSet<>();
    }

    public void addAuthorizedUser(String userId) {
        authorizedUsers.add(userId);
    }

    public void removeAuthorizedUser(String userId) {
        authorizedUsers.remove(userId);
    }

    public Set<String> getAuthorizedUsers() {
        return authorizedUsers;
    }

    public void startListening() {
        mqttClientManager.subscribe(MqttConfig.TOPIC_CARD_READ, CardReadEvent.class, this::processCardRead);
    }

    private void processCardRead(CardReadEvent event) {
        boolean isGranted = authorizedUsers.contains(event.getUserId());

        AccessDecisionEvent decisionEvent = new AccessDecisionEvent(
                event.getUserId(),
                isGranted,
                event.getTimestamp(),
                event.getDirection());

        mqttClientManager.publish(MqttConfig.TOPIC_ACCESS_DECISION, decisionEvent);
    }
}
