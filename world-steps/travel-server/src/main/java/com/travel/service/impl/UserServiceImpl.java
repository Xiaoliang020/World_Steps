package com.travel.service.impl;

import com.travel.constant.MessageConstant;
import com.travel.dto.UserDTO;
import com.travel.dto.UserLoginDTO;
import com.travel.entity.User;
import com.travel.exception.AccountNotFoundException;
import com.travel.exception.PasswordErrorException;
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
        if (userMapper.getByEmail(userDTO.getEmail()) != null) {
            throw new RegisterFailedException(MessageConstant.DUPLICATE_EMAIL);
        }

        if (userMapper.getByUsername(userDTO.getUsername()) != null) {
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

    /**
     * User login
     * @param userLoginDTO
     * @return
     */
    public User login(UserLoginDTO userLoginDTO) {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();

        User user = userMapper.getByUsername(username);

        if (user == null) {
            // Account does not exist
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // Password compare
        // Use MD5 to encrypt first
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(user.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        return user;
    }

    /**
     * Update user info
     * @param userDTO
     */
    public void update(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);

        userMapper.update(user);
    }
}
