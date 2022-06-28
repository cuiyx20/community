package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;

@Mapper
public interface MessageMapper {

    //分页展示，所以需要查某个用户的会话的列表——针对每个会话显示最新的私信
    List<Message> selectConversations(int userId,int offset,int limit);

    //还需要查询某个用户的所有会话的数量，用于分页展示
    int selectConversationCount(int userId);

    //查询某个会话所包含的私信详情列表
    List<Message> selectLetters(String conversationId, int offset, int limit);

    //还要查未读的会话个数，显示在页面上
    int selectLetterCount(String conversationId);

    //查询未读的私信的数量——因为有两个应用场景，一是查询某个用户所有的未读的会话的数量，一个是查询某个会话的所有未读的私信的数量，所以conversationId是一个可能传进来，可能不传的参数
    int selectLetterUnreadCount(int userId, String conversationId);

    //新增一个消息
    int insertMessage(Message message);

    //修改消息的状态
    int updateStatus(List<Integer> ids, int status);
}
