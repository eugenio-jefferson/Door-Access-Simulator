package com.example.mqtt;

import com.example.config.MqttConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class MqttClientManager {

    private final Mqtt5AsyncClient client;
    private final ObjectMapper objectMapper;

    public MqttClientManager() {
        var clientBuilder = Mqtt5Client.builder()
                .identifier(MqttConfig.generateClientId())
                .serverHost(MqttConfig.BROKER_HOST)
                .serverPort(MqttConfig.BROKER_PORT);

        if (MqttConfig.USE_TLS) {
            clientBuilder.sslWithDefaultConfig();
        }

        if (MqttConfig.USERNAME != null && !MqttConfig.USERNAME.isEmpty()) {
            clientBuilder.simpleAuth()
                    .username(MqttConfig.USERNAME)
                    .password(MqttConfig.PASSWORD.getBytes(StandardCharsets.UTF_8))
                    .applySimpleAuth();
        }

        this.client = clientBuilder.buildAsync();

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public void connect() {
        client.connect().join();
    }

    public void disconnect() {
        client.disconnect().join();
    }

    public void publish(String topic, Object payload) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(payload);
            client.publishWith()
                    .topic(topic)
                    .qos(MqttQos.AT_LEAST_ONCE)
                    .payload(jsonPayload.getBytes(StandardCharsets.UTF_8))
                    .send();
        } catch (Exception e) {
            System.err.println("Failed to serialize and publish message: " + e.getMessage());
        }
    }

    public <T> void subscribe(String topic, Class<T> payloadType, Consumer<T> messageHandler) {
        client.subscribeWith()
                .topicFilter(topic)
                .qos(MqttQos.AT_LEAST_ONCE)
                .callback(publish -> {
                    String jsonPayload = new String(publish.getPayloadAsBytes(), StandardCharsets.UTF_8);
                    try {
                        T payload = objectMapper.readValue(jsonPayload, payloadType);
                        messageHandler.accept(payload);
                    } catch (Exception e) {
                        System.err.println("Failed to deserialize message: " + e.getMessage());
                    }
                })
                .send().join();
    }
}
