package com.freedom.gpt.config;

import com.freedom.gpt.properties.ProxyProperties;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * @author bk
 */
@Configuration
public class IocBeanConfiguration {

    @Bean("okHttpTemplate")
    @Autowired
    @DependsOn("okHttp3Factory")
    public RestTemplate restTemplate(@Qualifier("okHttp3Factory") OkHttp3ClientHttpRequestFactory okHttp3Factory) {
        RestTemplate restTemplate = new RestTemplate(okHttp3Factory);
        return restTemplate;
    }

    @Bean(name = "okHttpClint")
    @ConditionalOnClass(value = {ProxyProperties.class})
    public OkHttpClient okHttpClient(ProxyProperties properties) {
        Proxy proxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress(properties.getHost(),properties.getPort()));
        return new OkHttpClient().newBuilder()
                .proxy(proxy)
                .build();
    }

    @Bean(name = "okHttp3Factory")
    public OkHttp3ClientHttpRequestFactory requestFactory(@Qualifier("okHttpClint") OkHttpClient okHttpClient) {
        OkHttp3ClientHttpRequestFactory factory = new OkHttp3ClientHttpRequestFactory(okHttpClient);
        factory.setConnectTimeout(10000);
        return factory;
    }

}
