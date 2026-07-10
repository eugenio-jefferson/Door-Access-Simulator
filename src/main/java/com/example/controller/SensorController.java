package com.example.controller;

import com.example.config.MqttConfig;
import com.example.model.AccessDirection;
import com.example.model.CardReadEvent;
import com.example.mqtt.MqttClientManager;

import java.time.LocalDateTime;

public class SensorController {

    private final MqttClientManager mqttClientManager;

    public SensorController(MqttClientManager mqttClientManager) {
        this.mqttClientManager = mqttClientManager;
    }

    public void simulateCardRead(String userId, AccessDirection direction) {
        CardReadEvent event = new CardReadEvent(userId, LocalDateTime.now(), direction);
        System.out.println(
                " > [SensorController] User with ID '" + userId + "' swiped card to go " + direction + ".");
        mqttClientManager.publish(MqttConfig.TOPIC_CARD_READ, event);
    }

    public void simulateDoorSensor(boolean isOpen) {
        String state = isOpen ? "OPEN" : "CLOSED";
        System.out.println(" > [SensorController] Door sensor detected door is now " + state + ".");
        mqttClientManager.publish(MqttConfig.TOPIC_DOOR_SENSOR, state);
    }
}
