package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTest {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;         //这里使用依赖注入，自动获取Bean给这个变量，之后就可以使用这个变量来进行访问方法

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Test
    public void testSelectUser(){
        User user = userMapper.selectById(101);
        System.out.println(user);

        user = userMapper.selectByName("liubei");
        System.out.println(user);

        user = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);
    }

    @Test
    public void testInsertUser(){
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setSalt("abc");
        user.setEmail("test.qq.com");
        user.setHeaderUrl("http://www.nowcoder.com/101.png");
        user.setCreateTime(new Date());

        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getId());
    }

    @Test
    public void updateUser(){
        int rows = userMapper.updateStatus(150,1);
        System.out.println(rows);

        rows = userMapper.updateHeader(150,"http://www.nowcoder.com/102.png");
        System.out.println(rows);

        rows = userMapper.updatePassword(150,"hello");
        System.out.println(rows);
    }




    //因为开发entity和mapper的时候，涉及到对表里的数据进行访问，需要写sql语句，而编译器不会给出错误提醒，所以最好写一个测试类来测试一下字段有没有写错
    @Test
    public void testSelectDiscussPosts(){
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(149,0,10);
        //System.out.println(list);      ——要遍历集合不能这么写
        for(DiscussPost post : list){
            System.out.println(post);
        }

        int countId = discussPostMapper.selectDiscussPostRows(149);          //这个userId表示是否增加这个用户条件
        System.out.println(countId);
    }
    @Test
    public void insertDiscussPost(){
        DiscussPost discussPost = new DiscussPost();
        discussPost.setId(281);
        discussPost.setUserId(151);
        discussPost.setTitle("乘风破浪的姐姐3三公分组");
        discussPost.setContent("佳人组：于文文、刘恋、赵梦、唐诗逸、张蔷");
        discussPost.setType(1);
        discussPost.setStatus(1);
        discussPost.setCommentCount(161);
        discussPost.setCreateTime(new Date(System.currentTimeMillis()));
        int id = discussPostMapper.insertDiscussPost(discussPost);
        System.out.println(id);
    }

    //测试登录mapper
    //测试插入数据的方法
    @Test
    public void testInsertLoginTicket(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));

        loginTicketMapper.insertLoginTicket(loginTicket);
    }

    //测试查询数据的方法——根据ticket查询
    @Test
    public void testSelectByTicket(){
        /*
        LoginTicket loginTicket = new LoginTicket();
        loginTicket = loginTicketMapper.selectByTicket("abc");
         */
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);

        loginTicketMapper.updateStatus("abc",1);
        loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);
    }


}
