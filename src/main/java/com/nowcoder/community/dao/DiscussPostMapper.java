package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.*;

@Mapper
public interface DiscussPostMapper {

    //在社区首页显示前十个帖子，所以需要返回一个表，所以是集合的类型
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);      //参数是考虑后面的功能，比如个人主页

    //如果需要动态SQL【有些情况下可能不用这个参数，但是有些情况下需要这个参数——动态SQL】，并且方法中只有一个参数的时候，就必须加上这个注解@Param，取别名
    int selectDiscussPostRows(@Param("userId") int userId);        //获取用户的帖子数量


}
