package com.nowcoder.community.dao;
import org.apache.ibatis.annotations.Mapper;
import com.nowcoder.community.entity.User;

@Mapper
public interface UserMapper {

    User selectById(int id);       //根据用户id查询用户

    User selectByName(String username);     //根据用户名称查询用户

    User selectByEmail(String email);       //根据用户邮箱查询用户

    int insertUser(User user);     //插入用户

    int updateStatus(int id,int status);      //更新状态——需要用户id，更新哪个用户，以及需要修改为的状态status；作为输入

    int updateHeader(int id,String headerUrl);     //更新用户的头像，需要用户id，以及新头像的url

    int updatePassword(int id,String password);      //更新用户的密码，需要用户id，以及新的密码
}
