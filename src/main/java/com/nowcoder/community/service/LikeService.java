package com.nowcoder.community.service;

import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    //点赞
    public void like(int userId, int entityType, int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);

        //一次点赞是点赞，存入redis，两次点赞就是取消点赞，查看redis中有没有这个key的value，有就删除
        boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey,userId);

        if(isMember){
            //取消赞
            redisTemplate.opsForSet().remove(entityLikeKey,userId);
        }else{
            redisTemplate.opsForSet().add(entityLikeKey,userId);
        }
    }

    //点赞状态——某人
    public int findEntityLikeStatus(int userId, int entityType, int entityId){
        //返回整型——如果将来业务中包含对帖子踩踩的功能，可以用int表示多个状态
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey,userId) ? 1 : 0;

    }

    //查询某实体点赞的数量
    public Long findEntityLikeCount(int entityType, int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }
}
