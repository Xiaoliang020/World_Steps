package com.travel.service;

import com.travel.dto.UserDTO;

public interface UserService {

    /**
     * User register a new account
     * @param userDTO
     */
    void save(UserDTO userDTO);
}
