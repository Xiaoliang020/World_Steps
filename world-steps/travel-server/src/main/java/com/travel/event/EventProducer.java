package com.travel.event;

import com.alibaba.fastjson.JSONObject;
import com.travel.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    /**
     * Handle event
     * @param event
     */
    public void fireEvent(Event event) {
        // Publish event to topic
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
    }

}
