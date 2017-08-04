package com.sh.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.*;

/**
 * Created by li_y on 2017/8/1.
 * redis 客户端服务
 */
public class RedisClient {
    private static Logger logger = LoggerFactory.getLogger(RedisClient.class);
    private static final String REDIS_HOST = "127.0.0.1";
    private static final int REDIS_PORT = 6379;

    private Jedis jedis = null;

    public RedisClient() {
        jedis = new Jedis(REDIS_HOST, REDIS_PORT);
    }

    /**
     * set 字符串
     */

    public void set(String key, String value) {
        logger.info("redis put key={},value={}", key, value);
        jedis.set(key, value);
    }

    /**
     * get 字符串
     */
    public String get(String key) {
        logger.info("get key={}", key);
        if (jedis.exists(key)) {
            return jedis.get(key);
        } else {
            logger.info("redis key={} not exist", key);
            return null;
        }
    }


    /**
     * set Object
     */
    public void setObject(String key, Object object) {
        logger.info("redis set Object, key={}", key);
        SerializationUtil serializationUtil = new SerializationUtil();
        jedis.set(key.getBytes(), serializationUtil.serialize(object));
    }

    /**
     * get Object
     */
    public Object getObject(String key) {
        if (jedis.exists(key.getBytes())) {
            SerializationUtil serializationUtil = new SerializationUtil();
            return serializationUtil.unSerialize(jedis.get(key.getBytes()));
        } else {
            logger.info("key not exist");
            return null;
        }
    }

    /**
     * set hash
     *
     */
    public void hset(String key, String field, String value){
        jedis.hset(key,field,value);
    }

    /**
     * get hash
     */
    public String hget(String key, String field){
        return jedis.hget(key,field);
    }


    class SerializationUtil {
        /**
         * 序列化
         */

        public byte[] serialize(Object object) {
            ObjectOutputStream objectOutputStream = null;
            ByteArrayOutputStream byteArrayOutputStream = null;
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(object);
                byte[] bytes = byteArrayOutputStream.toByteArray();
                return bytes;

            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Object serialize fail", e);
            } finally {
                if (byteArrayOutputStream != null) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (objectOutputStream != null) {
                    try {
                        objectOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        /**
         * 反序列化
         */
        public Object unSerialize(byte[] bytes) {
            ByteArrayInputStream byteArrayInputStream = null;
            ObjectInputStream objectInputStream = null;
            try {
                byteArrayInputStream = new ByteArrayInputStream(bytes);
                objectInputStream = new ObjectInputStream(byteArrayInputStream);
                return objectInputStream.readObject();
            } catch (Exception e) {
                logger.error("unSerialize Object fail", e);
            }

            return null;
        }
    }


}
