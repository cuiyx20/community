package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;

@Service
//业务组件，，想要作为Bean被Spring容器管理，加的注解就应该是@Service
//@Scope("prototype")     //但是一般使用的都是单例的
//设定Bean的范围——单例还是多个实例，Spring管理的Bean默认是单例，也就是参数是singleton，如果想要设置成多个实例，只需要把参数改为prototype
public class AlphaService {

    @Autowired
    private AlphaDao alphaDao;      //因为业务过程中可能是需要使用dao中的bean来访问数据库，所以这种依赖关系使用依赖注入来实现
    //这里没有加@Qualifier 的注解，就是使用的默认的AlphaDaoMyBatisImpl这个Bean

    //注入下面两个为了学习Spring管理事务的功能
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    public AlphaService(){
        System.out.println("实例化AlphaService");
    }
    @PostConstruct
    //这个注解的意思就是在构造器之后调用——初始化方法通常就是在构造器之后调用，用来初始化某些数据
    public void init(){
        System.out.println("初始化AlphaService");
    }

    @PreDestroy
    //在对象销毁之前调用——因为如果对象已经销毁了，就不能调用方法了
    public void destroy() {
        System.out.println("销毁AlphaDestroy");
    }

    /*
    上面两个主要是希望容器来自动的进行初始化和销毁
     */

    public String find(){
        return alphaDao.select();        //这个业务使用了AlphaDao来查询数据库中的内容
    }


    //Spring实现管理事务的两种方法
    //1、使用注解声明声明式事务
    @Transactional
    public Object save1(){
        //添加用户
        User user = new User();
        user.setUsername("alpha");
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.MD5("123" + user.getSalt()));
        user.setEmail("alpha@qq.com");
        user.setHeaderUrl("http://www.nowcoder.com/head/99t.png");
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        //添加帖子
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle("hello");
        post.setContent("新人报道！");
        post.setCreateTime(new Date());
        discussPostMapper.insertDiscussPost(post);

        //制造一个错误——如果这个方法被当作一个事务，那么这个方法就会全部执行成功，但是如果存在错误，就会回滚，所有操作都不应该执行
        Integer.valueOf("abc");        //将字符串准换为整数，因为我们输入的不是数字字符串，所以这个方法理应报错

        return "ok";
    }

    //第二种方法：使用TransactionTemplate管理事务，需要实现方法
    @Autowired
    private TransactionTemplate transactionTemplate;

    public Object save2(){
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return transactionTemplate.execute(new TransactionCallback<Object>() {

            @Override
            public Object doInTransaction(TransactionStatus status) {
                //添加用户
                User user = new User();
                user.setUsername("beta");
                user.setSalt(CommunityUtil.generateUUID().substring(0,5));
                user.setPassword(CommunityUtil.MD5("123" + user.getSalt()));
                user.setEmail("beta@qq.com");
                user.setHeaderUrl("http://www.nowcoder.com/head/999t.png");
                user.setCreateTime(new Date());
                userMapper.insertUser(user);

                //添加帖子
                DiscussPost post = new DiscussPost();
                post.setUserId(user.getId());
                post.setTitle("你好");
                post.setContent("我是新人！");
                post.setCreateTime(new Date());
                discussPostMapper.insertDiscussPost(post);

                //制造一个错误——如果这个方法被当作一个事务，那么这个方法就会全部执行成功，但是如果存在错误，就会回滚，所有操作都不应该执行
                Integer.valueOf("abc");        //将字符串准换为整数，因为我们输入的不是数字字符串，所以这个方法理应报错

                return "ok";
            }
        });
    }

}
