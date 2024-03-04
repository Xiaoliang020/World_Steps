package com.travel.service;

import com.travel.dto.UserDTO;
import com.travel.dto.UserLoginDTO;
import com.travel.entity.User;
import com.travel.vo.UserProfileVO;

public interface UserService {

    /**
     * User register a new account
     * @param userDTO
     */
    void save(UserDTO userDTO);

    /**
     * User login
     * @param userLoginDTO
     * @return
     */
    User login(UserLoginDTO userLoginDTO);

    /**
     * Update user info
     * @param userDTO
     */
    void update(UserDTO userDTO);

    /**
     * Get username by id
     * @param userId
     * @return
     */
    String getUsername(Long userId);

    /**
     * Get user profile
     * @param userId
     * @return
     */
    UserProfileVO getProfile(Long userId);

    /**
     * Find user by id
     * @param userId
     * @return
     */
    User findUserById(Long userId);
}
