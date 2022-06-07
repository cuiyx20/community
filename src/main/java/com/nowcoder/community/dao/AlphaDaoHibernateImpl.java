package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

@Repository("alphaHibernate")
//注解里的字符串就是自定义的Bean的名字——默认是类名小写加上Bean，如果觉得太长，或者想有自己定义的名字，就可以在注解中这样实现

//第一只有在项目的包下，并且前面加了可以被扫描的注解之后，才能作为一个bean被扫描，并被Spring容器管理
//鉴于这是一个访问数据库的类，所以前面加的就应该是@Repository注解，这样这个Bean就可以被装配到容器中了
public class AlphaDaoHibernateImpl implements AlphaDao{
    //AlphaDao这个访问接口的实现类，假设使用的是Hibernate技术实现的
    @Override
    public String select() {
        return "Hibernate";
    }

}
