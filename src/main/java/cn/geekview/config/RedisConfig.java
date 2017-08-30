package cn.geekview.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;


@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport  {
    /**
     * 必须注册一个类型为CacheManager的bean，因为框架不能作为约定使用合理的默认值。
     * 而<cache：annotation-driven>元素假定一个名为“cacheManager”的bean，@EnableCaching按类型搜索缓存管理器bean。
     * 因此，缓存管理器bean方法的命名并不重要。
     * 对于那些希望在@EnableCaching和要使用的精确缓存管理器bean之间建立更直接的关系的人，可以实现CachingConfigurer回调接口
     */
    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
        return redisCacheManager;
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return ((target, method, params) -> {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(target.getClass().getName());
            stringBuilder.append(method.getName());
            for (Object param : params) {
                stringBuilder.append(param);
            }
            return stringBuilder.toString();
        });
    }

    /**
     * 修改序列化后在Redis中的格式
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        /**
         * om.setDateFormat(DateFormat.getDateTimeInstance());//格式化时间
         * 直接使用这种方式格式化时间的话，在Windows和在Linux上格式化后的时间格式是不一样的，
         * 因为DateFormat.getDateTimeInstance()具有默认语言环境的默认格式化风格。
         * Windows：2017-8-30 12:00:00
         * Linux：Aug 30, 2017 12:00:00 PM
         * 这样会出现这种情况：
         *      在Windows上序列化之后，在Linux上反序列化时识别不了，导致反序列化失败
         *      提示如下：
         *      org.springframework.data.redis.serializer.SerializationException: Could not read JSON: Can not deserialize value of type java.util.Date from String "2017-8-30 12:00:00":
         *           not a valid representation (error: Failed to parse Date value '2017-8-30 12:00:00': Unparseable date: "2017-8-30 12:00:00")
         *  解决办法（指定格式化形式）：
         *  om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
         */
//        om.setDateFormat(DateFormat.getDateTimeInstance());//格式化时间
        om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
