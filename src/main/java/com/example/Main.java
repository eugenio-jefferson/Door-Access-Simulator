package com.example;

import com.example.controller.SensorController;
import com.example.mqtt.MqttClientManager;
import com.example.service.AuthorizationSubscriber;
import com.example.service.DoorActuatorSubscriber;
import com.example.service.StayTimeCalculatorSubscriber;
import com.example.ui.ConsoleMenu;

public class Main {
    public static void main(String[] args) {
        System.out.println("\nStarting Door Access Simulator...");

        MqttClientManager mqttClientManager = new MqttClientManager();
        mqttClientManager.connect();
        System.out.println("Connected to MQTT Broker.");

        SensorController sensorController = new SensorController(mqttClientManager);

        AuthorizationSubscriber authorizationSubscriber = new AuthorizationSubscriber(mqttClientManager);
        authorizationSubscriber.startListening();

        StayTimeCalculatorSubscriber stayTimeCalculatorSubscriber = new StayTimeCalculatorSubscriber(mqttClientManager);
        stayTimeCalculatorSubscriber.startListening();

        DoorActuatorSubscriber doorActuatorSubscriber = new DoorActuatorSubscriber(mqttClientManager);
        doorActuatorSubscriber.startListening();

        authorizationSubscriber.addAuthorizedUser("123");

        ConsoleMenu menu = new ConsoleMenu(sensorController, authorizationSubscriber, stayTimeCalculatorSubscriber);
        menu.start();

        mqttClientManager.disconnect();
        System.out.println("Disconnected from MQTT Broker.");
        System.exit(0);
    }
}
