package com.smartmug.rabbitmq.events;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class SendTemplateEvent extends ApplicationEvent {

    private String templateData;
    private String consumerGroup;

    public SendTemplateEvent(final Object source, final String templateData, final String consumerGroup) {
        super(source);
        this.templateData = templateData;
        this.consumerGroup = consumerGroup;
    }

    public SendTemplateEvent(final Object source, final Clock clock) {
        super(source, clock);
    }

    public String getTemplateData() {
        return templateData;
    }

    public void setTemplateData(final String templateData) {
        this.templateData = templateData;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(final String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

}
