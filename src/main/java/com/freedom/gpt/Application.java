package com.freedom.gpt;

import com.freedom.gpt.properties.ProxyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author bk
 */
@SpringBootApplication(scanBasePackages={"com.freedom.gpt"})
@EnableConfigurationProperties({ProxyProperties.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
