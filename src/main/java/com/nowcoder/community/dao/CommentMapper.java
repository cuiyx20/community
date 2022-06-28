package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;

@Mapper
public interface CommentMapper {

    //分页，所以需要两个方法，一个查一共有多少数据，一个查有多少页
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    int selectCountByEntity(int entityType, int entityId);

    //向数据库中添加一个帖子
    int insertComment(Comment comment);
}
