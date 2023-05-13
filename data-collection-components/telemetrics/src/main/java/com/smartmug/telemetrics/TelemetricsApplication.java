package com.smartmug.telemetrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@SpringBootApplication
@ComponentScan(basePackages = {"com.smartmug.keycloak","com.smartmug"})
public class TelemetricsApplication {
        private static final Logger logger = LoggerFactory.getLogger(TelemetricsApplication.class);

        public static void main(String[] args) {
            SpringApplication.run(TelemetricsApplication.class, args);
        }

        @Bean
        protected ServletContextListener listener() {
            return new ServletContextListener() {

                public void contextInitialized(ServletContextEvent sce) {
                    logger.info("ServletContext initialized");
                }

                public void contextDestroyed(ServletContextEvent sce) {
                    logger.info("ServletContext destroyed");
                }

            };
        }
    }
