package com.example.commonservice.configuration.redis;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
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
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Cấu hình Redis cho ứng dụng Spring Boot
 * Bao gồm: Connection pooling, Serialization, Cache management
 */
@Configuration // Đánh dấu đây là class cấu hình Spring
@EnableCaching // Kích hoạt tính năng caching của Spring
public class RedisConfig {

    // ==================== PHẦN 1: CÁC THUỘC TÍNH CẤU HÌNH ====================

    /**
     * Địa chỉ host của Redis server
     * Đọc từ application.properties/yml
     */
    @Value("${spring.data.redis.host}")
    private String redisHost;

    /**
     * Cổng kết nối Redis (mặc định: 6379)
     */
    @Value("${spring.data.redis.port:6379}")
    private int redisPort;

    /**
     * Mật khẩu xác thực Redis (nếu có)
     * Mặc định: rỗng (không có mật khẩu)
     */
    @Value("${spring.redis.password:}")
    private String redisPassword;

    /**
     * Thời gian timeout cho các lệnh Redis (ms)
     * Mặc định: 2000ms = 2 giây
     */
    @Value("${spring.redis.timeout:2000}")
    private int timeout;

    /**
     * Số lượng connection tối đa trong pool
     * Mặc định: 8
     */
    @Value("${spring.redis.lettuce.pool.max-active:8}")
    private int maxActive;

    /**
     * Số lượng connection idle tối đa được giữ trong pool
     * Mặc định: 8
     */
    @Value("${spring.redis.lettuce.pool.max-idle:8}")
    private int maxIdle;

    /**
     * Số lượng connection idle tối thiểu được giữ trong pool
     * Mặc định: 0
     */
    @Value("${spring.redis.lettuce.pool.min-idle:0}")
    private int minIdle;

    /**
     * Thời gian chờ tối đa khi pool hết connection (ms)
     * -1 = chờ vô thời hạn
     */
    @Value("${spring.redis.lettuce.pool.max-wait:-1}")
    private long maxWait;

    // ==================== PHẦN 2: REDIS CONNECTION FACTORY ====================

    /**
     * Tạo bean LettuceConnectionFactory để quản lý kết nối Redis
     * Lettuce là Redis client không đồng bộ, thread-safe
     *
     * @return LettuceConnectionFactory đã được cấu hình
     */
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        // Tạo cấu hình Redis standalone (single server)
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(redisHost); // Set địa chỉ host
        redisConfig.setPort(redisPort);     // Set cổng kết nối

        // Nếu có password thì set password để xác thực
        if (redisPassword != null && !redisPassword.isEmpty()) {
            redisConfig.setPassword(redisPassword);
        }

        // Cấu hình connection pool sử dụng Apache Commons Pool2
        GenericObjectPoolConfig<?> poolConfig = getGenericObjectPoolConfig();

        // Cấu hình Lettuce client với pooling
        LettucePoolingClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(timeout)) // Timeout cho mỗi command
                .poolConfig(poolConfig)                      // Apply pool config
                .build();

        // Tạo và trả về LettuceConnectionFactory
        return new LettuceConnectionFactory(redisConfig, clientConfig);
    }

    private GenericObjectPoolConfig<?> getGenericObjectPoolConfig() {
        GenericObjectPoolConfig<?> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(maxActive);      // Số connection tối đa
        poolConfig.setMaxIdle(maxIdle);         // Số connection idle tối đa
        poolConfig.setMinIdle(minIdle);         // Số connection idle tối thiểu
        poolConfig.setMaxWaitMillis(maxWait);   // Thời gian chờ khi hết connection

        // Test connection trước khi borrow từ pool
        poolConfig.setTestOnBorrow(true);

        // Test connection trước khi return về pool
        poolConfig.setTestOnReturn(true);

        // Test connection trong lúc idle
        poolConfig.setTestWhileIdle(true);
        return poolConfig;
    }

    // ==================== PHẦN 3: REDIS TEMPLATE ====================

    /**
     * Tạo RedisTemplate để thao tác với Redis
     * RedisTemplate cung cấp high-level API để làm việc với Redis
     *
     * @return RedisTemplate<String, Object> đã được cấu hình serialization
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        // Tạo template instance
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        // Set connection factory
        template.setConnectionFactory(redisConnectionFactory());

        // --- Cấu hình ObjectMapper cho JSON serialization ---
        ObjectMapper objectMapper = new ObjectMapper();

        // Đăng ký module hỗ trợ Java 8 Date/Time API (LocalDate, LocalDateTime,...)
        objectMapper.registerModule(new JavaTimeModule());

        // Kích hoạt default typing để serialize/deserialize polymorphic types
        // Lưu type information vào JSON để deserialize đúng class
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,  // Validator cho subtype
                ObjectMapper.DefaultTyping.NON_FINAL,   // Chỉ apply cho non-final classes
                JsonTypeInfo.As.PROPERTY                // Lưu type info dạng property
        );

        // Tạo JSON serializer sử dụng Jackson
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        // Serializer cho String key
        StringRedisSerializer stringSerializer = new StringRedisSerializer();

        // Set serializer cho Redis key (dạng String)
        template.setKeySerializer(stringSerializer);

        // Set serializer cho Hash key (dạng String)
        template.setHashKeySerializer(stringSerializer);

        // Set serializer cho Redis value (dạng JSON)
        template.setValueSerializer(serializer);

        // Set serializer cho Hash value (dạng JSON)
        template.setHashValueSerializer(serializer);

        // Kích hoạt transaction support (wrap operations trong transaction)
        template.setEnableTransactionSupport(true);

        // Initialize template sau khi set các properties
        template.afterPropertiesSet();

        return template;
    }

    // ==================== PHẦN 4: CACHE MANAGER ====================

    /**
     * Tạo CacheManager cho Spring Cache Abstraction
     * Quản lý các cache instance và cấu hình caching behavior
     *
     * @param redisConnectionFactory Connection factory được inject từ Spring context
     * @return RedisCacheManager đã được cấu hình
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // --- Cấu hình ObjectMapper cho cache serialization ---
        ObjectMapper objectMapper = new ObjectMapper();

        // Đăng ký Java 8 Date/Time module
        objectMapper.registerModule(new JavaTimeModule());

        // Kích hoạt default typing (tương tự như RedisTemplate)
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        // Tạo JSON serializer
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        // --- Cấu hình Redis Cache ---
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                // Set TTL (Time To Live) = 1 giờ cho mỗi cache entry
                .entryTtl(Duration.ofHours(1))

                // Cấu hình serialization cho cache key (String)
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))

                // Cấu hình serialization cho cache value (JSON)
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))

                // Không cache giá trị null
                .disableCachingNullValues();

        // Tạo và cấu hình RedisCacheManager
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(config)      // Apply cấu hình mặc định
                .transactionAware()         // Tích hợp với Spring transaction
                .build();
    }



}
