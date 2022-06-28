package com.nowcoder.community;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
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
public class CommentTest {

    @Autowired
    private CommentService commentService;
    @Autowired
    private DiscussPostService discussPostService;

    @Test
    public void testSelectCommentByType(){
        List<Comment> list = commentService.findCommentByEntity(1,228,0,10);
        int count = commentService.findCommentCount(1,228);
        System.out.println(count);
        for(Comment comment : list){
            System.out.println(comment.toString());
        }
    }

    @Test
    public void testInsertComment(){
        Comment comment = new Comment();
        comment.setUserId(152);
        comment.setEntityType(1);
        comment.setEntityId(281);
        comment.setContent("喜欢于文文");
        comment.setCreateTime(new Date());

        int row = commentService.addComment(comment);
        System.out.println(row);
        int id = comment.getEntityId();
        DiscussPost post = discussPostService.findDiscussPostById(id);
        int count = post.getCommentCount();
        System.out.println(count);
        row = discussPostService.updateCommentCount(id,count + 1);
        post = discussPostService.findDiscussPostById(id);
        count = post.getCommentCount();
        System.out.println(count);
    }
}
