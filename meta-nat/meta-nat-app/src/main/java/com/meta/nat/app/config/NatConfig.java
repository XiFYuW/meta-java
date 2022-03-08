package com.meta.nat.app.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "nat")
@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
@RefreshScope
public class NatConfig {

    private String local;
    private String log;
    private String format;
    private String pan;
    private String performStart;
    private String performStop;
}
