package com.freedom.gpt.config;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${porxy_host:127.0.0.1}")
    private String proxyHostname;

    @Value("${proxy_port:7891}")
    private int proxyPort;


    @Bean("okHttpTemplate")
    @Autowired
    @DependsOn("okHttp3Factory")
    public RestTemplate restTemplate(@Qualifier("okHttp3Factory") OkHttp3ClientHttpRequestFactory okHttp3Factory) {
        RestTemplate restTemplate = new RestTemplate(okHttp3Factory);
        return restTemplate;
    }

    @Bean(name = "okHttpClint")
    public OkHttpClient okHttpClient() {
        Proxy proxy = new Proxy(Proxy.Type.SOCKS,new InetSocketAddress(proxyHostname,proxyPort));
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
