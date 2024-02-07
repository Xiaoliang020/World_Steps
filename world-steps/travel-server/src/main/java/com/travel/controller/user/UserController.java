package com.travel.controller.user;

import com.travel.dto.UserDTO;
import com.travel.result.Result;
import com.travel.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/user")
@Slf4j
@Api(tags = "User related interfaces")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping()
    @ApiOperation("User registration")
    public Result register(@RequestBody UserDTO userDTO) {
        log.info("New user: {}", userDTO);
        userService.save(userDTO);
        return Result.success();
    }
}
