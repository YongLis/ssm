package com.sh.client;

import junit.framework.TestCase;

/**
 * Created by li_y on 2017/8/1.
 */
public class RedisClientTest extends TestCase {
    public void testSet() throws Exception {
        RedisClient redisClient = new RedisClient();
        redisClient.set("name", "张三");
        System.out.println(redisClient.get("name"));
    }

    public void testGet() throws Exception {

    }

    public void testSetObject() throws Exception {
        RedisClient redisClient = new RedisClient();

        Student student = new Student();
        student.setName("tom");
        student.setAge(21);
        student.setSex("男");

        redisClient.setObject("student", student);

        Student student1 = (Student) redisClient.getObject("student");
        System.out.println(student1.getName());
    }

    public void testGetObject() throws Exception {

    }


}