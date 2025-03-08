package com.stockheap.weather.config;






import com.stockheap.weather.WeatherConstants;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;




@Configuration
@EnableCaching
public class SpringConfiguration {




    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${open.weather.expire.time.in.seconds}")
    private long cacheExpireTimeInSeconds;


    @Value("${open.weather.base.url}")
    private String  openWeatherBaseUrl;



    @Value("${web.client.connection.seconds.timeout}")
    private int  webClientConnectionSecondsTimeout;

    @Value("${web.client.read.seconds.timeout}")
    private int webClientReadSecondsTimeout;


    @Value("${web.client.write.seconds.timeout}")
    private int webClientWriteSecondsTimeout;




    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10)) // Set TTL for cache entries
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));

        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put(WeatherConstants.CacheNames.WEATHER_FORECAST_CACHE, config.entryTtl(Duration.ofSeconds(cacheExpireTimeInSeconds))); // 30 min TTL
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(config)
                .build();
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisHost);  // Ensure correct IP address is set here
        configuration.setPort(redisPort);
        return new LettuceConnectionFactory(configuration);
    }


    @Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, webClientConnectionSecondsTimeout)
                .responseTimeout(Duration.ofSeconds(5))
                .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(webClientReadSecondsTimeout))
                        .addHandlerLast(new WriteTimeoutHandler(webClientWriteSecondsTimeout)));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(openWeatherBaseUrl)
                .build();
    }



}