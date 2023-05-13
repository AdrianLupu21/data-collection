package com.smartmug.rabbitmq.publisher;

import com.smartmug.rabbitmq.events.UpdateDiagnosticsTemplateEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
public class UpdateTemplateEventPublisher implements ApplicationListener<UpdateDiagnosticsTemplateEvent> {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void onApplicationEvent(final UpdateDiagnosticsTemplateEvent event) {
        rabbitTemplate.convertAndSend(event);
    }
}
