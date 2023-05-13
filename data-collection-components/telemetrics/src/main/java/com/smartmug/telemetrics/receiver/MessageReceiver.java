package com.smartmug.telemetrics.receiver;

import com.smartmug.device.configuration.client.spi.DeviceConfigurationClient;
import com.smartmug.device.management.client.spi.DeviceManagementClient;
import com.smartmug.device.management.dto.DeviceResourceProperties;
import com.smartmug.rabbitmq.events.UpdateDiagnosticsTemplateEvent;
import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.time.Instant;
import java.time.temporal.TemporalField;

@Component
public class MessageReceiver {

    @Autowired
    private DeviceConfigurationClient deviceConfigurationClient;
    @Autowired
    private DeviceManagementClient deviceManagementClient;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Value("${smartmug.diagnostic.template}")
    private String diagnosticTemplatePath;

    @Value("${smartmug.telemetrics.template}")
    private String telemetricsTemplatePath;

    private static final Logger logger = LoggerFactory.getLogger(MessageReceiver.class);

    @RabbitListener(queues = "diagnostic-test")
    public void handleDiagnosticMessage(String message, Message wholeMessage) {
        try {
            final String deviceId = wholeMessage.getMessageProperties().getHeader("deviceId");
            final DeviceResourceProperties deviceResourceProperties = deviceManagementClient.getDeviceResourceProperties(deviceId,
                    "diagnostic");

            if(null != deviceResourceProperties.getResourcePath() && !deviceResourceProperties.getResourcePath().isEmpty()){
                diagnosticTemplatePath = deviceResourceProperties.getResourcePath();
            }

            String jsonSchema = deviceConfigurationClient.getResource(diagnosticTemplatePath);

            OutputUnit result = getOutputUnit(message, jsonSchema);

            if(!result.getValid()){
                logger.info("diagnostics validation failed at {}", Instant.now().getEpochSecond());
                final UpdateDiagnosticsTemplateEvent updateDiagnosticsTemplateEvent
                        = new UpdateDiagnosticsTemplateEvent(this, deviceId, diagnosticTemplatePath);

                applicationEventPublisher.publishEvent(updateDiagnosticsTemplateEvent);
            }else{
                logger.info("diagnostics message saved at {}", Instant.now().getEpochSecond());
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @RabbitListener(queues = "telemetries-test")
    public void handleSensorMessage(String message, Message wholeMessage) throws URISyntaxException {
        final String deviceId = wholeMessage.getMessageProperties().getHeader("deviceId");
        final DeviceResourceProperties deviceResourceProperties = deviceManagementClient.getDeviceResourceProperties(deviceId,
                "telemetrics");

        if(null != deviceResourceProperties.getResourcePath() && !deviceResourceProperties.getResourcePath().isEmpty()){
            telemetricsTemplatePath = deviceResourceProperties.getResourcePath();
        }
        String jsonSchema = deviceConfigurationClient.getResource(telemetricsTemplatePath);

        OutputUnit result = getOutputUnit(message, jsonSchema);

        if(!result.getValid()){
            logger.info("telemetrics validation failed at {}", Instant.now().getEpochSecond());
            final UpdateDiagnosticsTemplateEvent updateDiagnosticsTemplateEvent
                    = new UpdateDiagnosticsTemplateEvent(this, deviceId, telemetricsTemplatePath);

            applicationEventPublisher.publishEvent(updateDiagnosticsTemplateEvent);
        }else{
            logger.info("telemetrics message saved at {}", Instant.now().getEpochSecond());
            //save message
        }
    }

    private OutputUnit getOutputUnit(String message, String jsonSchema) {
        JsonObject data = new JsonObject(jsonSchema);
        JsonObject messageObject = new JsonObject(message);

        JsonSchema schema = JsonSchema.of(data);

        OutputUnit result = Validator.create(
                        schema,
                        new JsonSchemaOptions().setDraft(Draft.DRAFT201909)
                                .setBaseUri("https://maxcup.com"))
                .validate(messageObject);
        return result;
    }
}
