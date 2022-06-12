package com.nowcoder.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class CommunityUtil {
    //生成随机字符串
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    //MD5加密
    //MD5加密：只能加密，不能解密，并且每次加密都是一个值
    //在密码后面加一个随机字符串，然后再进行加密就可以了
    public static String MD5(String key){
        if(StringUtils.isBlank(key)){
            return null;
        }else{
            return DigestUtils.md5DigestAsHex(key.getBytes());          //加密成16进制的字符串——Spring封装的方法
        }
    }
}
