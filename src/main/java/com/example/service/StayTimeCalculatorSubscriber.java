package com.example.service;

import com.example.config.MqttConfig;
import com.example.model.AccessDecisionEvent;
import com.example.model.AccessDirection;
import com.example.mqtt.MqttClientManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class StayTimeCalculatorSubscriber {

    private final MqttClientManager mqttClientManager;
    private final Map<String, LocalDateTime> userEntryTimes;
    private final Map<String, Duration> totalStayTimes;

    public StayTimeCalculatorSubscriber(MqttClientManager mqttClientManager) {
        this.mqttClientManager = mqttClientManager;
        this.userEntryTimes = new HashMap<>();
        this.totalStayTimes = new HashMap<>();
    }

    public void startListening() {
        mqttClientManager.subscribe(MqttConfig.TOPIC_ACCESS_DECISION, AccessDecisionEvent.class, this::processDecision);
    }

    private void processDecision(AccessDecisionEvent event) {
        if (!event.isGranted()) {
            return;
        }

        String userId = event.getUserId();
        LocalDateTime eventTime = event.getTimestamp();

        if (event.getDirection() == AccessDirection.IN) {
            userEntryTimes.put(userId, eventTime);
        } else if (event.getDirection() == AccessDirection.OUT) {
            LocalDateTime entryTime = userEntryTimes.remove(userId);
            if (entryTime != null) {
                Duration stayDuration = Duration.between(entryTime, eventTime);
                totalStayTimes.put(userId, totalStayTimes.getOrDefault(userId, Duration.ZERO).plus(stayDuration));
            } else {
                System.out
                        .println("[StayTimeCalculator] Warning: User with ID: '" + userId
                                + "' exited without a recorded entry.");
            }
        }
    }

    public void printReport() {
        System.out.println("\n-=-=-=-=-= Stay Time Report =-=-=-=-=-");
        if (totalStayTimes.isEmpty()) {
            System.out.println("No completed visits yet.");
        } else {
            for (Map.Entry<String, Duration> entry : totalStayTimes.entrySet()) {
                System.out.println("User " + entry.getKey() + ": " + entry.getValue().getSeconds() + " seconds");
            }
        }

        if (!userEntryTimes.isEmpty()) {
            System.out.println("\nUsers currently inside:");
            for (String userId : userEntryTimes.keySet()) {
                System.out.println("- User " + userId);
            }
        }
    }
}
