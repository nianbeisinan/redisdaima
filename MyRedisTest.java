package com.xiexin.redistest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis 的测试。 测试 和 ssm项目结合
 *  ssm项目如何使用redis， 第一种方式： 使用 jedis ，类似于 jdbc
 *  // 第一步： 在applicationContext。xml 中 注释去掉
 *  // 第二部：  把  db.properties 中的把 redis 配置的注释去掉。
 *
 *  springmvc 中的 单元测试。
 *  为什么要用 juint 单元测试， 因为 在框架中，传统的main方法 已经无法
 *  处理，如 req 请求，等等， 无法满足 测试需求了
 *  单元测试的好处是， 在最小的 代码结构单元中 找出 bug ， 最快速的找出bug所在的地方，
 *   迅速解决， 1个dao方法1个测试  1个controller 1个测试 ， 1个service 1 个测试。
 */
//import org.junit.runner.RunWith;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class) // 使用 spring的junit测试
@ContextConfiguration({"classpath:applicationContext.xml"}) //模拟ssm框架运行后加载xml容器
public class MyRedisTest {
    @Autowired
    private JedisPool jedisPool;

    // 测试 string类型
    @Test
    public void test01() throws InterruptedException {
        // redisTemplate
        String pcode = jedisPool.getResource().set("pcode", "4758");
        System.out.println("pcode = " + pcode);
        // 查询 pcode 这个key在不在， ---  exists key
        Boolean b = jedisPool.getResource().exists("pcode");
        System.out.println("b = " + b);
        if (b){
            System.out.println(" key 存在 b= " + b);
            // 如果在， 把他设置成  120 倒计时， 且值 也改为 7788
            jedisPool.getResource().setex("pcode",120,"7788");
            // 并且 在 10s 后 输出  所剩下的倒计时。！
            Thread.sleep(1000);
            Long ttl = jedisPool.getResource().ttl("pcode");
            System.out.println("ttl = " + ttl);
            // 输出完毕后，将 该key 设置成 永久的key
           jedisPool.getResource().persist("pcode"); // 注意， 他的返回值不是-1
            Long ttl2 = jedisPool.getResource().ttl("pcode");
            System.out.println("ttl2 = " + ttl2);
        }else{
            System.out.println("b ="+ b + " , key不存在");
        }

    }
    // 测试 常用命令
    @Test
    public void test02(){
       // 查询所有的key
        Set<String> keys = jedisPool.getResource().keys("*");
        for (String key : keys) {
            //System.out.println("key = " + key);
            String value = jedisPool.getResource().get(key);
            System.out.println(" key " + key+" : " + "value "+value);
            // 自增
            Long incr = jedisPool.getResource().incr(key);
            System.out.println("incr = " + incr);
            String value1 = jedisPool.getResource().get(key);
            System.out.println(" key " + key+" : " + "value "+value1);
        }
    }
    // 测试 hash
    @Test
    public void test03(){
        // 增加
      jedisPool.getResource().hset("food", "name", "苹果");
      jedisPool.getResource().hset("food", "color", "红色");
        // 查
        String color = jedisPool.getResource().hget("food", "color");
        System.out.println("color = " + color);
        // 查k
        Set<String> food = jedisPool.getResource().hkeys("food");
        for (String key : food) {
            System.out.println("key = " + key);
        }
        // 查kv
        Map<String, String> food1 = jedisPool.getResource().hgetAll("food");
        for (String s : food1.keySet()) {
            System.out.println("s = " + s);
        }
    }

    // 测试 list
    @Test
    public void test04(){
         jedisPool.getResource().lpush("names", "唐僧","孙悟空");
        List<String> names = jedisPool.getResource().lrange("names", 0, -1);
        for (String name : names) {
            System.out.println("name = " + name);
        }
        String names1 = jedisPool.getResource().lpop("names");
        System.out.println("names1 = " + names1);

        List<String> names2 = jedisPool.getResource().lrange("names", 0, -1);
        for (String name : names2) {
            System.out.println("name = " + name);
        }



    }
    // 测试set
    @Test
    public void test05(){
         jedisPool.getResource().sadd("pnames", "张三", "李四");
        Set<String> pnames = jedisPool.getResource().smembers("pnames");
        for (String pname : pnames) {
            System.out.println("pname = " + pname);
        }
        Long pnames1 = jedisPool.getResource().scard("pnames");
        System.out.println("pnames1 = " + pnames1);

        //指定删除：srem key value
        jedisPool.getResource().srem("pnames","张三");
        // 随机删除！用途很广，  spop names
        jedisPool.getResource().spop("pnames");
    }
    // zset
    @Test
    public void test06(){
//        增加： zadd key 分数 值 ， pnames ，
        jedisPool.getResource().zadd("xnames",1.0,"1娃娃");
        jedisPool.getResource().zadd("xnames",2.0,"2娃娃");
        jedisPool.getResource().zadd("xnames",3.0,"3娃娃");
        jedisPool.getResource().zadd("xnames",4.0,"4娃娃");
//        遍历： zrange key 0 -1 withscores 加上withscores 带分数， 不带 直接全部显示值
        Set<String> xnames = jedisPool.getResource().zrange("xnames", 1, -1);
        for (String xname : xnames) {
            System.out.println("xname = " + xname);
        }
//        查条数： zcard key
        Long xnames1 = jedisPool.getResource().zcard("xnames");
        System.out.println("xnames1 = " + xnames1);
//        指定删除： 移除集合中的一个或者多个成员 zrem key value
        Long xnames2 = jedisPool.getResource().zrem("xnames", "3娃娃");
        System.out.println("xnames2 = " + xnames2);
    }



}
