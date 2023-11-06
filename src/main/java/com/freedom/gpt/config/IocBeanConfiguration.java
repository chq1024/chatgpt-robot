package com.freedom.gpt.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.freedom.gpt.properties.ProxyProperties;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.text.SimpleDateFormat;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author bk
 */
@Configuration
public class IocBeanConfiguration {

    @Bean("okHttpTemplate")
    @DependsOn("okHttp3Factory")
    @Autowired
    public RestTemplate restTemplate(@Qualifier("okHttp3Factory") OkHttp3ClientHttpRequestFactory okHttp3Factory) {
        return new RestTemplate(okHttp3Factory);
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
        factory.setConnectTimeout(20000);
        return factory;
    }

    @Bean
    public ObjectMapper objectMapper() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        objectMapper.setDateFormat(dateFormat);
        return objectMapper;
    }

    @Bean(name = "gptThreadTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int corePoolSize = Runtime.getRuntime().availableProcessors();;
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(corePoolSize * 2 + 1);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("gpt-thread-");
        // 当线程池达到最大时的处理策略，默认抛出RejectedExecutionHandler异常
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池，默认false
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 等待所有任务结束最长等待时间，默认0毫秒
        executor.setAwaitTerminationSeconds(10);
        executor.initialize();
        return executor;
    }

}
