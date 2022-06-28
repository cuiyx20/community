package com.nowcoder.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testStrings(){
        String redisKey = "test:count";

        redisTemplate.opsForValue().set(redisKey,1);        //添加

        System.out.println(redisTemplate.opsForValue().get(redisKey));     //获取

        System.out.println(redisTemplate.opsForValue().increment(redisKey));    //增加

        System.out.println(redisTemplate.opsForValue().decrement(redisKey));     //减少
    }

    @Test
    public void testHash(){
        String redisKey = "test:user";

        redisTemplate.opsForHash().put(redisKey,"id",1);
        redisTemplate.opsForHash().put(redisKey,"username","zhangsan");

        System.out.println(redisTemplate.opsForHash().get(redisKey,"id"));
        System.out.println(redisTemplate.opsForHash().get(redisKey,"username"));
    }

    @Test
    public void testList(){
        String redisKey = "test:ids";

        redisTemplate.opsForList().leftPush(redisKey,101);
        redisTemplate.opsForList().leftPush(redisKey,102);
        redisTemplate.opsForList().leftPush(redisKey,103);

        System.out.println(redisTemplate.opsForList().size(redisKey));     //看列表中有多少条数据
        System.out.println(redisTemplate.opsForList().index(redisKey,0));   //获取某个索引位置的数据
        System.out.println(redisTemplate.opsForList().range(redisKey,0,2));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
    }

    @Test
    public void testSet(){
        String redisKey = "test:teacher";

        redisTemplate.opsForSet().add(redisKey,"刘备","关羽","张飞","赵云","诸葛亮");

        System.out.println(redisTemplate.opsForSet().size(redisKey));
        System.out.println(redisTemplate.opsForSet().pop(redisKey));
        System.out.println(redisTemplate.opsForSet().members(redisKey));
    }

    @Test
    public void testSortedSets(){
        String redisKey = "test:student";

        redisTemplate.opsForZSet().add(redisKey,"唐僧",80);
        redisTemplate.opsForZSet().add(redisKey,"悟空",90);
        redisTemplate.opsForZSet().add(redisKey,"八戒",50);
        redisTemplate.opsForZSet().add(redisKey,"沙僧",70);
        redisTemplate.opsForZSet().add(redisKey,"白龙马",60);

        System.out.println(redisTemplate.opsForZSet().zCard(redisKey));      //统计一共有多少数据
        System.out.println(redisTemplate.opsForZSet().score(redisKey,"八戒"));     //查某个人的分数
        System.out.println(redisTemplate.opsForZSet().reverseRank(redisKey,"八戒"));     //由大到小倒序的排名
        System.out.println(redisTemplate.opsForZSet().range(redisKey,0,2));     //由小到大去前三名
        System.out.println(redisTemplate.opsForZSet().reverseRange(redisKey,0,2));
    }

    @Test
    public void testKeys(){
        //redisTemplate.delete("test:user");

        System.out.println(redisTemplate.hasKey("test:user"));    //判断是否有某个key

        redisTemplate.expire("test:teacher",20, TimeUnit.SECONDS);     //设置某个key的过期时间
    }

    //多次访问同一个key——可以将key绑定
    @Test
    public void testBoundOperations(){
        String redisKey = "test:count";

        BoundValueOperations operations = redisTemplate.boundValueOps(redisKey);

        operations.increment();
        operations.increment();
        operations.increment();
        operations.increment();
        operations.increment();

        System.out.println(operations.get());

    }

    //编程式事务
    @Test
    public void testTransactional(){

        Object obj = redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String redisKey = "test:tx";

                operations.multi();      //启用事务

                operations.opsForSet().add(redisKey,"张三");
                operations.opsForSet().add(redisKey,"王五");
                operations.opsForSet().add(redisKey,"李四");

                System.out.println(operations.opsForSet().members(redisKey));     //redis的事务是把所有的命令放到一个队列中，等之后统一执行，所以事务中间的查询并不会立刻返回结果！！

                return operations.exec();     //提交事务
            }
        });

        System.out.println(obj);

    }
}
