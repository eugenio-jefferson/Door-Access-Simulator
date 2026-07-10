package com.example.service;

import com.example.config.MqttConfig;
import com.example.model.AccessDecisionEvent;
import com.example.mqtt.MqttClientManager;

public class DoorActuatorSubscriber {

    private final MqttClientManager mqttClientManager;

    public DoorActuatorSubscriber(MqttClientManager mqttClientManager) {
        this.mqttClientManager = mqttClientManager;
    }

    public void startListening() {
        mqttClientManager.subscribe(MqttConfig.TOPIC_ACCESS_DECISION, AccessDecisionEvent.class, this::processDecision);
    }

    private void processDecision(AccessDecisionEvent event) {
        if (event.isGranted()) {
            System.out.println(
                    " > [DoorActuator] Access GRANTED for User ID '" + event.getUserId() + "'. Opening door...");
            simulateDoorOpenClose();
        } else {
            System.out.println(
                    " > [DoorActuator] Access DENIED for User ID '" + event.getUserId() + "'. Door remains locked.");
        }
    }

    private void simulateDoorOpenClose() {
        try {
            System.out.println(" > [DoorActuator] Door is OPEN.");

            Thread.sleep(2000);

            System.out.println(" > [DoorActuator] Door is CLOSED.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
