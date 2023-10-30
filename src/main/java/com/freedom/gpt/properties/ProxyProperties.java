package com.freedom.gpt.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author bk
 */
@ConfigurationProperties(prefix = "proxy")
@Getter
@Setter
public class ProxyProperties {
    private String host;
    private int port;
}
