package com.nowcoder.community;

import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.entity.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MessageTest {

    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void testSelectMessage(){
        List<Message> list= messageMapper.selectConversations(111,0,20);
        for(Message message : list){
            System.out.println(message);
        }
        int count = messageMapper.selectConversationCount(111);
        System.out.println(count);
        List<Message> list1 = messageMapper.selectLetters("111_112",0,20);
        for(Message message : list1){
            System.out.println(message);
        }
        int count1 = messageMapper.selectLetterCount("111_112");
        System.out.println(count1);

        int count2 = messageMapper.selectLetterUnreadCount(131,"111_131");
        System.out.println(count2);
    }

    @Test
    public void testAddMessage(){
        Message message = new Message();
        message.setFromId(112);
        message.setToId(111);
        message.setContent("1");
        message.setConversationId("111_112");
        message.setCreateTime(new Date());

        int i = messageMapper.insertMessage(message);
        System.out.println(i);
    }

}
