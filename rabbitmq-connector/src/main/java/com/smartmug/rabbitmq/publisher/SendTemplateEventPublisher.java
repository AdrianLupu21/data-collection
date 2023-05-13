package com.smartmug.rabbitmq.publisher;

import com.smartmug.rabbitmq.events.SendTemplateEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
public class SendTemplateEventPublisher implements ApplicationListener<SendTemplateEvent> {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void onApplicationEvent(final SendTemplateEvent event) {
        rabbitTemplate.convertAndSend(event.getConsumerGroup(), event);
    }

}
