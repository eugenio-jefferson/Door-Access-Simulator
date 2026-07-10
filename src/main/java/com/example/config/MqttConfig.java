package com.example.config;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.UUID;

public class MqttConfig {
    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    public static final String BROKER_HOST = dotenv.get("MQTT_BROKER_HOST", "broker.hivemq.com");
    public static final int BROKER_PORT = Integer.parseInt(dotenv.get("MQTT_BROKER_PORT", "1883"));
    public static final String USERNAME = dotenv.get("MQTT_USERNAME", "");
    public static final String PASSWORD = dotenv.get("MQTT_PASSWORD", "");
    public static final boolean USE_TLS = Boolean.parseBoolean(dotenv.get("MQTT_USE_TLS", "false"));

    public static final String TOPIC_CARD_READ = dotenv.get("MQTT_TOPIC_CARD_READ", "access-control/sensor/card");
    public static final String TOPIC_DOOR_SENSOR = dotenv.get("MQTT_TOPIC_DOOR_SENSOR", "access-control/sensor/door");
    public static final String TOPIC_ACCESS_DECISION = dotenv.get("MQTT_TOPIC_ACCESS_DECISION",
            "access-control/access/decision");

    public static String generateClientId() {
        return "AccessControlSimulator-" + UUID.randomUUID().toString();
    }
}
