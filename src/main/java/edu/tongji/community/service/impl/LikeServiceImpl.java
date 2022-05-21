package edu.tongji.community.service.impl;

import edu.tongji.community.service.LikeService;
import edu.tongji.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void like(int userId, int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        // 判断当前用户是否已经存在redis的集合中
        boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey,userId);
        if(isMember){
            // 如果存在，证明已经点过赞了，就取消点赞
            redisTemplate.opsForSet().remove(entityLikeKey, userId);
        }else{
            // 在redis中存不可重复集合Set(里面存的是点过赞的用户id），key是点赞 like:entity:entityType:entityId ,value是当前用户
            // value是当前用户的好处是可以获取更多的信息，存数字就很单一没法扩展业务
            redisTemplate.opsForSet().add(entityLikeKey, userId);
        }
    }

    // 查询某实体点赞的数量,即查点赞key集合中的userId数
    public long findEntityLikeCount(int entityType, int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    // 查询某人对某实体的点赞状态
    public int findEntityLikeStatus(int userId, int entityType, int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
    }
}
