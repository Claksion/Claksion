package com.claksion.seat;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.util.ObjectUtils;

@Slf4j
@SpringBootTest
class RedisTemplateTest {

    @Autowired
    RedisTemplate redisTemplate;

    @DisplayName("Data Type이 String인 경우 테스트")
    @Test
    public void testString() {

        // Given
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String requestKey = "string_key";
        String requestValue = "value_01";

        // When
        valueOperations.set(requestKey, requestValue, 5, TimeUnit.MINUTES);

        // Then
        String value = valueOperations.get(requestKey);
        log.info("testString - value: {}", value);
        assertEquals(value, value);
    }

    @DisplayName("Data Type이 List인 경우 테스트")
    @Test
    public void testList() {

        // Given
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        String key = "list_key";
        List<String> strs = Arrays.asList("value_01", "value_02", "value_03");

        // When
        for (String str : strs) {
            listOperations.leftPush(key, str);
        }
        redisTemplate.expire(key, 5, TimeUnit.MINUTES);

        // Then
        for (String str : strs) {
            String value = listOperations.rightPop(key);
            log.info("testList - value: {}", value);
            assertTrue(value.contains(str));
        }
    }

    @DisplayName("Data Type이 Set인 경우 테스트")
    @Test
    void testSet() {

        // Given
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        String key = "set_key";
        String value01 = "value_01";
        String value02 = "value_02";
        String value03 = "value_03";

        // When
        setOperations.add(key, value01, value02, value03);
        redisTemplate.expire(key, 5, TimeUnit.MINUTES);

        // Then
        Set<String> members = setOperations.members(key);
        log.info("testSet - members: {}", members);
        assertFalse(ObjectUtils.isEmpty(members));
        assertAll(
                () -> assertTrue(members.contains(value01)),
                () -> assertTrue(members.contains(value02)),
                () -> assertTrue(members.contains(value03))
        );
    }

    @DisplayName("Data Type이 Hash인 경우 테스트")
    @Test
    void testHash() {

        // Given
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        String key = "hash_key";
        String hashKey = "field_01";
        String value = "value_01";

        // When
        hashOperations.put(key, hashKey, value);
        redisTemplate.expire(key, 5, TimeUnit.MINUTES);

        // Then
        Map<Object, Object> map = hashOperations.entries(key);
        log.info("testHash - map: {}", map);
        assertAll(
                () -> assertTrue(map.keySet().contains(hashKey)),
                () -> assertTrue(map.values().contains(value))
        );
    }

    @DisplayName("Data Type이 ZSet인 경우 테스트")
    @Test
    void testSortedSet() {

        // Given
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        String key = "z_set_key";
        String value01 = "value_01";
        String value02 = "value_02";
        String value03 = "value_03";

        // When
        zSetOperations.add(key, value01, 1.0);
        zSetOperations.add(key, value02, 2.0);
        zSetOperations.add(key, value03, 3.0);
        redisTemplate.expire(key, 5, TimeUnit.MINUTES);

        // Then
        Set<String> range = zSetOperations.range(key, 0, 3);
        log.info("testSortedSet - range: {}", range);
        assertFalse(ObjectUtils.isEmpty(range));
        assertAll(
                () -> assertTrue(range.contains(value01)),
                () -> assertTrue(range.contains(value02)),
                () -> assertTrue(range.contains(value03))
        );
    }
}