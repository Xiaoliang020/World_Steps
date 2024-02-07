package com.travel.mapper;

import com.travel.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    /**
     * Insert a new user
     * @param user
     */
    @Insert("insert into user (username, password, email, phone, gender, create_time)"
            + "values" +
            "(#{username}, #{password}, #{email}, #{phone}, #{gender}, #{createTime})")
    void insert(User user);

    /**
     * Find a user by username
     * @param username
     * @return
     */
    @Select("select * from user where username=#{username}")
    User findByUsername(String username);

    /**
     * Find a user by email
     * @param email
     * @return
     */
    @Select("select * from user where email=#{email}")
    User findByEmail(String email);
}
