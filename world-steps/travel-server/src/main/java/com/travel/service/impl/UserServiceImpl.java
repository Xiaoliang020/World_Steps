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
import com.travel.utils.RedisKeyUtil;
import com.travel.vo.UserProfileVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

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
        clearCache(user.getId());
        userMapper.update(user);
    }

    /**
     * Get username by id
     * @param userId
     * @return
     */
    public String getUsername(Long userId) {
        User user = findUserById(userId);
//        String username = userMapper.getUsernameById(userId);
        String username = user.getUsername();
        return username;
    }

    /**
     * Get user profile
     * @param userId
     * @return
     */
    public UserProfileVO getProfile(Long userId) {
        User user = findUserById(userId);
        if (user == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        UserProfileVO userProfileVO = new UserProfileVO();

        BeanUtils.copyProperties(user, userProfileVO);

        return userProfileVO;
    }

    /**
     * Find user by id
     * @param userId
     * @return
     */
    public User findUserById(Long userId) {
        User user = getCache(userId);
        if (user == null) {
            user = initCache(userId);
        }
        return user;
    }

    /**
     * Get user info from cache
     * @param userId
     */
    private User getCache(Long userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(redisKey);
    }

    /**
     * Initialize user data in redis
     * @param userId
     * @return
     */
    private User initCache(Long userId) {
        User user = userMapper.getById(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(redisKey, user, 3600, TimeUnit.SECONDS);
        return user;
    }

    /**
     * Clear redis data when data changed
     * @param userId
     */
    private void clearCache(Long userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }
}
