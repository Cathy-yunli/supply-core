package com.lingshou.supply.common.redis;

import com.lingshou.supply.common.json.JsonHelper;
import com.lingshou.supply.contract.exception.ServiceException;
import com.owitho.redis.utils.RedisResourceRepository;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.JedisCommands;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author young
 * @date 2018/1/30
 */

public class RedisService {

    private String cacheKey;

    public RedisService setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
        return this;
    }

    private JedisCommands getJedis() {
        if (StringUtils.isEmpty(cacheKey)) {
            throw new ServiceException("no cacheKey in property!");
        }
        return RedisResourceRepository.getRedisResource(cacheKey);
    }

    private void closeJedis(JedisCommands jedis) {
        RedisResourceRepository.returnRedisResouce(jedis);
    }

    /**
     * 根据key获取缓存
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return 对应class的缓存
     */
    public <T> T get(String key, Class<? extends T> clazz) {
        String value = this.get(key);
        return this.jsonToObj(value, clazz);

    }

    /**
     * 根据key获取缓存
     *
     * @param key
     * @return
     */
    public String get(String key) {
        JedisCommands jedis = this.getJedis();
        try {
            return jedis.get(key);
        } finally {
            if (null != jedis) {
                closeJedis(jedis);
            }
        }
    }

    /**
     * 设置缓存
     *
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean set(String key, T value) {
        return this.set(key, toJsonString(value));
    }

    /**
     * 设置缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key, String value) {
        JedisCommands jedis = this.getJedis();
        try {
            if (value == null) {
                throw new ServiceException("value sent to redis cannot be null");
            }
            return jedis.set(key, value).equals("OK") ? true : false;
        } finally {
            if (null != jedis) {
                closeJedis(jedis);
            }
        }
    }

    /**
     * 设置缓存如果key不存在
     *
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean setIfNotExist(String key, T value) {
        return this.setIfNotExist(key, toJsonString(value));
    }

    /**
     * 设置缓存如果key不存在
     *
     * @param key
     * @param value
     * @return
     */
    public boolean setIfNotExist(String key, String value) {
        JedisCommands jedis = this.getJedis();
        try {
            if (value == null) {
                throw new ServiceException("value sent to redis cannot be null");
            }
            return jedis.setnx(key, value) == 1L ? true : false;
        } finally {
            if (null != jedis) {
                closeJedis(jedis);
            }
        }
    }

    /**
     * 设置缓存加并设置失效时间
     *
     * @param key
     * @param seconds
     * @param value
     * @param <T>
     * @return 成功 OK
     */
    public <T> boolean setWithExpire(String key, int seconds, T value) {
        return this.setWithExpire(key, seconds, toJsonString(value));
    }

    /**
     * 设置缓存加并设置失效时间
     *
     * @param key
     * @param seconds
     * @param value
     * @return
     */
    public boolean setWithExpire(String key, int seconds, String value) {
        JedisCommands jedis = this.getJedis();
        try {
            if (value == null) {
                throw new ServiceException("value sent to redis cannot be null");
            }
            return jedis.setex(key, seconds, value).equals("OK") ? true : false;
        } finally {
            if (null != jedis) {
                closeJedis(jedis);
            }
        }
    }

    /**
     * 设置缓存加并设置失效时间
     *
     * @param key
     * @param seconds
     * @return 成功 1
     */
    public Boolean setExpire(String key, int seconds) {
        JedisCommands jedis = this.getJedis();
        try {
            return jedis.expire(key, seconds) == 1L ? true : false;
        } finally {
            if (null != jedis) {
                closeJedis(jedis);
            }
        }
    }

    /**
     * 缓存是否存在
     *
     * @param key
     * @return
     */
    public Boolean exists(String key) {
        JedisCommands jedis = this.getJedis();
        try {
            return jedis.exists(key);
        } finally {
            if (null != jedis) {
                closeJedis(jedis);
            }
        }
    }

    /**
     * 返回一个key还能活多久，单位为秒
     *
     * @param key
     * @return 如果该key本来并没有设置过期时间，则返回-1，如果该key不存在，则返回-2
     */
    public long ttl(String key) {
        JedisCommands jedis = this.getJedis();
        try {
            return jedis.ttl(key);
        } finally {
            if (null != jedis) {
                closeJedis(jedis);
            }
        }
    }

    /**
     * 给指定key值 加1,返回增加后的值
     *
     * @param key
     * @return
     */
    public long incr(String key) {
        JedisCommands jedis = this.getJedis();
        try {
            return jedis.incr(key);
        } finally {
            if (null != jedis) {
                closeJedis(jedis);
            }
        }
    }

    /**
     * 给指定key值 减1
     *
     * @param key
     * @return
     */
    public long decr(String key) {
        JedisCommands jedis = this.getJedis();
        try {
            return jedis.decr(key);
        } finally {
            if (null != jedis) {
                closeJedis(jedis);
            }
        }
    }

    /**
     * 设置新缓存返回旧的缓存值
     * 默认返回和新缓存同类型的class
     *
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> T getSet(String key, T value) {
        return this.getSet(key, value, (Class<T>) value.getClass());
    }

    /**
     * 设置新缓存返回旧的缓存值
     * 手动设置返回类型
     *
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> T getSet(String key, T value, Class<? extends T> clazz) {
        if (value == null) {
            throw new ServiceException("value sent to redis cannot be null");
        }
        String oldValue = this.getSet(key, toJsonString(value));
        return jsonToObj(oldValue, clazz);
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public String getSet(String key, String value) {
        JedisCommands jedis = this.getJedis();
        try {
            if (value == null) {
                throw new ServiceException("value sent to redis cannot be null");
            }
            return jedis.getSet(key, value);
        } finally {
            if (null != jedis) {
                closeJedis(jedis);
            }
        }
    }

    public Boolean tryLock(String lockKey, long waitTimeOut, long lockTimeOut) {
        JedisCommands jedis = this.getJedis();
        //get lock wait time
        long end = System.currentTimeMillis() + waitTimeOut;
        int count = 1;
        try {
            do {
                //get lock expired time
                String expiresStr = String.valueOf(System.currentTimeMillis() + lockTimeOut);
                long setnx = jedis.setnx(lockKey, expiresStr);
                if (setnx == 1) {
                    return Boolean.TRUE;
                }

                String lockExpire = get(lockKey);
                //lock is expired
                if (StringUtils.isNotEmpty(lockExpire) && Long.parseLong(lockExpire) < System.currentTimeMillis()) {
                    //get old value
                    String oldExpire = getSet(lockKey, expiresStr);
                    if (StringUtils.isNotEmpty(oldExpire) && oldExpire.equals(lockExpire)) {
                        return Boolean.TRUE;
                    }
                }

                //no wait
                if (Objects.equals(0, waitTimeOut)) {
                    return Boolean.FALSE;
                }

                //release cpu 避免活锁
                long sleepTime;
                if (waitTimeOut < 100) {
                    sleepTime = waitTimeOut / 5;
                } else {
                    sleepTime = 10 << count++;
                }
                TimeUnit.MILLISECONDS.sleep(sleepTime);
            }
            while (System.currentTimeMillis() < end);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("Error occured when tryLock By Redis");
        } finally {
            if (null != jedis) {
                closeJedis(jedis);
            }
        }
        return Boolean.FALSE;
    }

    public void unLock(String redisLockKey) {

        JedisCommands jedis = this.getJedis();
        try {
            jedis.del(redisLockKey);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("Error occured when unLock By Redis");
        } finally {
            if (null != jedis) {
                closeJedis(jedis);
            }
        }
    }

    private <T> String toJsonString(T value) {
        if (value == null) {
            throw new ServiceException("value sent to redis cannot be null");
        }
        return JsonHelper.transObjToJsonString(value);
    }

    private <T> T jsonToObj(String value, Class<? extends T> clazz) {
        if (value == null) {
            return null;
        }
        return JsonHelper.transJsonStringToObj(value, clazz);
    }

    public Long hset(String mapName, String key, String value) {
        JedisCommands jedis = this.getJedis();
        try {
            return jedis.hset(mapName, key, value);
        } finally {
            if (null != jedis) {
                closeJedis(jedis);
            }
        }
    }

    public String hget(String mapName, String key) {
        JedisCommands jedis = this.getJedis();
        try {
            return jedis.hget(mapName, key);
        } finally {
            if (null != jedis) {
                closeJedis(jedis);
            }
        }
    }

    public Long hsetnx(String mapName, String key, String value) {
        JedisCommands jedis = this.getJedis();
        try {
            return jedis.hsetnx(mapName, key, value);
        } finally {
            if (null != jedis) {
                closeJedis(jedis);
            }
        }
    }

    public String hmset(String mapName, Map<String, String> hash) {
        JedisCommands jedis = this.getJedis();
        try {
            return jedis.hmset(mapName, hash);
        } finally {
            if (null != jedis) {
                closeJedis(jedis);
            }
        }
    }


    public List<String> hmget(String mapName, String... keys) {
        JedisCommands jedis = this.getJedis();
        try {
            return jedis.hmget(mapName, keys);
        } finally {
            if (null != jedis) {
                closeJedis(jedis);
            }
        }
    }

    public Long hdel(String mapName, String... field) {
        JedisCommands jedis = this.getJedis();
        try {
            return jedis.hdel(mapName, field);
        } finally {
            if (null != jedis) {
                closeJedis(jedis);
            }
        }
    }

    public Map<String, String> hgetAll(String mapName) {
        JedisCommands jedis = this.getJedis();
        try {
            return jedis.hgetAll(mapName);
        } finally {
            if (null != jedis) {
                closeJedis(jedis);
            }
        }
    }

}
