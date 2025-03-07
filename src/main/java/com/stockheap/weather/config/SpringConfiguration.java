package com.stockheap.weather.config;






import com.stockheap.weather.WeatherConstants;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;




@Configuration
public class SpringConfiguration {



//
//    @Value("rest.template.connection.pool.size:50")
//    private Integer restTempConnectionPool;
//
//    @Value("rest.template.max.default.routes:20")
//    private Integer restTempMaxDefaultRoutes;


//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate(clientHttpRequestFactory());
//    }

//    @Bean
//    public ClientHttpRequestFactory clientHttpRequestFactory() {
//        return new HttpComponentsClientHttpRequestFactory(httpClient());
//    }
//
//


    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${open.weather.expire.time.in.seconds}")
    private long cacheExpireTimeInSeconds;





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



//    @Bean
//    public WebClient webClient() {
//        // Create a connection pool with limits
//        ConnectionProvider connectionProvider = ConnectionProvider.builder("custom-pool")
//                .maxConnections(50) // Maximum connections in the pool
//                .pendingAcquireMaxCount(100) // Max pending requests if all connections are busy
//                .pendingAcquireTimeout(Duration.ofMillis(5000)) // Max wait time for a connection
//                .maxIdleTime(Duration.ofSeconds(30)) // Close idle connections after 30s
//                .maxLifeTime(Duration.ofMinutes(5)) // Maximum connection lifetime
//                .evictInBackground(Duration.ofSeconds(60)) // Background connection cleanup
//                .build();
//
//        // Configure HttpClient with timeouts and pooling
//        HttpClient httpClient = HttpClientBuilder.create(connectionProvider)
//                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) // Connection timeout
//                .responseTimeout(Duration.ofSeconds(10)) // Response timeout
//                .doOnConnected(conn ->
//                        conn.addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS)) // Read timeout
//                                .addHandlerLast(new WriteTimeoutHandler(10, TimeUnit.SECONDS)) // Write timeout
//                );
//
//
//        PoolingHttpClientConnectionManager poolingConnManager = new PoolingHttpClientConnectionManager();
//        poolingConnManager.setDefaultMaxPerRoute(restTempMaxDefaultRoutes);
//        poolingConnManager.setMaxTotal(restTempConnectionPool);
//        HttpClient httpClient2 =  HttpClientBuilder.create()
//                .setConnectionManager(poolingConnManager)
//                .build();
//
//
//        return WebClient.builder()
//                .baseUrl("https://jsonplaceholder.typicode.com")
//                .clientConnector(new ClientTransportConnector(httpClient2)) // Attach HttpClient
//                .build();
//    }
//
//
//
//

//    @Bean
//    public HttpClient httpClient() {
//
//        PoolingHttpClientConnectionManager poolingConnManager = new PoolingHttpClientConnectionManager();
//        poolingConnManager.setDefaultMaxPerRoute(restTempMaxDefaultRoutes);
//        poolingConnManager.setMaxTotal(restTempConnectionPool);
//        return HttpClientBuilder.create()
//                .setConnectionManager(poolingConnManager)
//                .build();
//    }
}