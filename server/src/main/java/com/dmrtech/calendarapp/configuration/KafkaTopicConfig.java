package com.dmrtech.calendarapp.configuration;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${kafka.kafkaBroker}")
    private String kafkaBroker;

    @Value(value = "${kafka.calendarItemTopicName}")
    private String calendarItemTopicName;



    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBroker);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic calendarItems() {
        return new NewTopic(calendarItemTopicName, 1, (short) 1);
    }
}
