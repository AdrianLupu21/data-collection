package com.smartmug.rabbitmq.events;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class UpdateDiagnosticsTemplateEvent extends ApplicationEvent {
    private String deviceId;
    private String resourcePath;
    public UpdateDiagnosticsTemplateEvent(final Object source,final String deviceId, final String resourcePath) {
        super(source);
        this.deviceId = deviceId;
        this.resourcePath = resourcePath;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }
}
