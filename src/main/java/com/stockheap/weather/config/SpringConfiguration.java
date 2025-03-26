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
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import static io.lettuce.core.ReadFrom.SLAVE_PREFERRED;


@Configuration
@EnableCaching
public class SpringConfiguration implements WebMvcConfigurer {




    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${weather.expire.time.in.seconds}")
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
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.stockheap.weather.controller"))
                .paths(PathSelectors.any())
                .build();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Allow all requests without authentication
                )
                .csrf(csrf -> csrf.disable()); // Disable CSRF for testing purposes

        return http.build();
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Allow requests from localhost:3000 to all endpoints
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000,http://localhost:8080,http://127.0.0.1:3000,http://127.0.0.1:8080") // You can specify multiple origins if needed
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Allow specific HTTP methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow credentials (cookies, authorization headers, etc.)
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

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

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .readFrom(SLAVE_PREFERRED)
                .build();


        return new LettuceConnectionFactory(configuration, clientConfig);
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