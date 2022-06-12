package com.nowcoder.community.service;

import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    //下面几个是发邮件需要的
    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")             //注入的是一个变量，方便后面使用
    private String domain;          //域名——这里不是一个买的域名，就是我们本地的域名

    @Value("${server.servlet.context-path}")
    private String contextPath;          //项目名


    public User findUserById(int id){

        return userMapper.selectById(id);
    }

    public Map<String,Object> register(User user){         //这个map返回值主要是为了存放不同的输入状态我们应该返回什么信息给浏览器做显示
        Map<String,Object> map = new HashMap<>();
        //首先对传入的值做判断，如果是空就直接抛异常
        if(user == null){
            throw new IllegalArgumentException("参数不能为空");
        }
        //输入不为空，但是里面的字段可能为空，所以需要再做一下判断
        if(StringUtils.isBlank(user.getUsername())){         //isBlank这个方法是封装好的，比较方便的可以判断多种为空的情况
            map.put("usernameMsg","账号不能为空！");
        }
        if(StringUtils.isBlank(user.getPassword())){         //isBlank这个方法是封装好的，比较方便的可以判断多种为空的情况
            map.put("passwordMsg","密码不能为空！");
        }
        if(StringUtils.isBlank(user.getEmail())){         //isBlank这个方法是封装好的，比较方便的可以判断多种为空的情况
            map.put("emailMsg","邮箱不能为空！");
        }

        //还要验证是否已经注册过了
        User user1 = userMapper.selectByName(user.getUsername());
        if(user1 == null){
            map.put("usernameMsg","该账号已存在！");
            return map;
        }


        //验证邮箱
        User user2 = userMapper.selectByEmail(user.getEmail());
        if(user2 == null){
            map.put("emailMsg","该邮箱已被注册！");
            return map;
        }

        //注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.MD5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head%dt.png",new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);        //执行insert的时候会自动生成id——application.properties中做了配置：mybatis.configuration.useGeneratedKeys=true

        //发送激活邮件
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        //http://localhost:8080/community/activation/101/code——激活路径
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url",url);
        String content = templateEngine.process("/main/activation",context);
        mailClient.sendMail(user.getEmail(),"激活账号",content);
        return map;
    }
}
