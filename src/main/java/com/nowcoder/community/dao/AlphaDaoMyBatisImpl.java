package com.nowcoder.community.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
//这个注解保证这个Bean被优先的装配
public class AlphaDaoMyBatisImpl implements AlphaDao{
    //假设有一天有了一个新的方法，想用MyBatis实现这个访问数据库的方法

    @Override
    public String select() {
        return "MyBatis";
    }
}
