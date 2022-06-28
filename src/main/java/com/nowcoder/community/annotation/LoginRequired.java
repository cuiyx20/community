package com.nowcoder.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)     //表示这个注解可以用在方法上
@Retention(RetentionPolicy.RUNTIME)      //声明这个注解有效的时长
public @interface LoginRequired {
    //声明这个自定义的注解是为了方便拦截器拦截到这个注解标识的方法
}
