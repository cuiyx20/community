package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
//业务组件，，想要作为Bean被Spring容器管理，加的注解就应该是@Service
//@Scope("prototype")     //但是一般使用的都是单例的
//设定Bean的范围——单例还是多个实例，Spring管理的Bean默认是单例，也就是参数是singleton，如果想要设置成多个实例，只需要把参数改为prototype
public class AlphaService {

    @Autowired
    private AlphaDao alphaDao;      //因为业务过程中可能是需要使用dao中的bean来访问数据库，所以这种依赖关系使用依赖注入来实现
    //这里没有加@Qualifier 的注解，就是使用的默认的AlphaDaoMyBatisImpl这个Bean


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
}
