package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

@Controller
@RequestMapping(path = "/comment")
public class CommentController{

    @Autowired
    private CommentService commentService;
    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/add/{discussPostId}",method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment){
        //因为我们需要从页面中传来的是评论回复的内容，实体类型，实体Id，所以需要用一个Comment实体来传比较方便——当然对于一些没有从页面获得的信息我们需要补充，比如用户——使用HostHolder来获得

        comment.setUserId(hostHolder.getUser().getId());      //如果用户没登录应该报错——后面做处理
        comment.setStatus(0);
        comment.setCreateTime(new Date());

        commentService.addComment(comment);

        return "redirect:/discuss/detail/" + discussPostId;      //重定向到这个帖子
    }
}
