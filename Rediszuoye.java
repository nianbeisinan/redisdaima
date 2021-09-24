package com.xiexin.redistest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Set;

import static redis.clients.jedis.BinaryClient.LIST_POSITION.BEFORE;

@RunWith(SpringJUnit4ClassRunner.class) // 使用 spring的junit测试
@ContextConfiguration({"classpath:applicationContext.xml"}) //模拟ssm框架运行后加载xml容器
public class Rediszuoye {
    @Autowired
    private JedisPool jedisPool;

//    大作业1： 编程题
//    用java代码写，把咱们班33个人的名字 形成 一个集合，
//    运行后随机点一个人的名字，就把这个人的名字移除。 再次
//    点名是 点 32个人的随机中的一个。


    @Test
    public void test01(){
//        randomkey 随机出key
        jedisPool.getResource().sadd("pnames", "白世纪" ,
                "陈红利" ,
                "陈世纪" ,
                "陈洋洋" ,
                "杜晓梦" ,
                "付春辉" ,
                "高芳芳" ,
                "郭旭" ,
                "胡艺果" ,
                "贾礼博" ,
                "李雪莹" ,
                "李祎豪" ,
                "林梦娇" ,
                "刘顺顺" ,
                "卢光辉" ,
                "吕亚伟" ,
                "宁静静" ,
                "牛志洋" ,
                "史倩影" ,
                "宋健行" ,
                "孙超阳" ,
                "孙乾力" ,
                "田君垚" ,
                "汪高洋" ,
                "王学斌" ,
                "杨天枫" ,
                "杨原辉" ,
                "袁仕奇" ,
                "张浩宇" ,
                "张晓宇" ,
                "张志鹏" ,
                "赵博苛" ,
                "邹开源");
        Set<String> pnames = jedisPool.getResource().smembers("pnames");
        for (String pname : pnames) {
            System.out.println("pname = " + pname);
        }
        Long pnames1 = jedisPool.getResource().scard("pnames");
        System.out.println("pnames1 = " + pnames1);
        // 随机删除！用途很广，  spop names
        String pnames2 = jedisPool.getResource().spop("pnames");
        System.out.println("pnames2 = " + pnames2);

    }
//    大作业2：编程题，
//    使用 java 代码编写，
//    有一个双端队列集合， 里面有 10 条数据，
//    查询出  第5个人是什么数据，
//    左边弹出1个 ， 右边弹出1个，打印还剩多少条数据，
//    然后，再 第3个数据前面，插入一个数据，
//    然后，进行查询全部数据进行查看。
@Test
public void test02(){
        jedisPool.getResource().lpush("tnames", "唐僧","孙悟空","沙和尚","猪八戒","白龙马","大娃","二娃","三娃","四娃","五娃");
//    List<String> tnames0 = jedisPool.getResource().lrange("tnames", 0, -1);
//    for (String name : tnames0) {
//        System.out.println("name = " + name);
//    }
//    System.out.println("-------------------------");
    String tnames = jedisPool.getResource().lindex("tnames", 4);
    System.out.println("tnames = " + tnames);
    //String type = jedisPool.getResource().type(tnames);

    String tnames1 = jedisPool.getResource().lpop("tnames");
    System.out.println("tnames1 = " + tnames1);
    String tnames2 = jedisPool.getResource().rpop("tnames");
    System.out.println("tnames2 = " + tnames2);
    //linsert hero  before "刘备"   "朝阳"
    System.out.println("--------------------------");
    jedisPool.getResource().linsert("tnames",BEFORE,"二娃", "妖刀");
    List<String> tnames3 = jedisPool.getResource().lrange("tnames", 0, -1);
    for (String name : tnames3) {
        System.out.println("name = " + name);
    }
}
}
