package com.travel.service.impl;

import com.travel.constant.MessageConstant;
import com.travel.dto.UserDTO;
import com.travel.entity.User;
import com.travel.exception.RegisterFailedException;
import com.travel.mapper.UserMapper;
import com.travel.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * User register a new account
     * @param userDTO
     */
    public void save(UserDTO userDTO) {
        //Check if the user already exists
        if (userMapper.findByEmail(userDTO.getEmail()) != null) {
            throw new RegisterFailedException(MessageConstant.DUPLICATE_EMAIL);
        }

        if (userMapper.findByUsername(userDTO.getUsername()) != null) {
            throw new RegisterFailedException(MessageConstant.DUPLICATE_USERNAME);
        }

        User user = new User();

        // Properties copy
        BeanUtils.copyProperties(userDTO, user);

        // Encrypt the password
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));

        // Set create time
        user.setCreateTime(LocalDateTime.now());

        userMapper.insert(user);
    }
}
