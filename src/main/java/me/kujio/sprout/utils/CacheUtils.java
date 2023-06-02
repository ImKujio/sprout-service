package me.kujio.sprout.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CacheUtils {

    private final RedisTemplate redisTemplate;

    public CacheUtils(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public <T> T getOrPut(String key, Getter<T> getter) {
        if (!hasKey(key)) {
            T obj = getter.get();
            put(key, obj);
            return obj;
        }
        return get(key);
    }

    public <T> T get(String key) {
        ValueOperations<String,T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    public <T> void put(final String key, final T val) {
        redisTemplate.opsForValue().set(key, val);
    }

    public void del(String key) {
        redisTemplate.delete(key);
    }

    public void delPrefix(String prefix) {
        Set<String> keys = redisTemplate.keys(prefix + ":*");
        if (keys == null) return;
        redisTemplate.delete(keys);
    }

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public static interface Getter<T> {
        T get();
    }

}
