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

    //发布帖子——增加帖子的方法
    int insertDiscussPost(DiscussPost discussPost);        //返回增加的帖子所在的行数

    //根据id查询帖子
    DiscussPost selectDiscussPostById(int id);

    //更新帖子中的评论数量
    int updateCommentCount(int id,int count);
}
