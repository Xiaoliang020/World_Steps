package com.travel.controller.user;

import com.travel.constant.JwtClaimsConstant;
import com.travel.dto.UserDTO;
import com.travel.dto.UserLoginDTO;
import com.travel.entity.User;
import com.travel.properties.JwtProperties;
import com.travel.result.Result;
import com.travel.service.UserService;
import com.travel.utils.JwtUtil;
import com.travel.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "User related interfaces")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/user")
    @ApiOperation("User registration")
    public Result register(@RequestBody UserDTO userDTO) {
        log.info("New user: {}", userDTO);
        userService.save(userDTO);
        return Result.success();
    }

    @PostMapping("/login")
    @ApiOperation("User login")
    public Result login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("User login: {}", userLoginDTO);
        User user = userService.login(userLoginDTO);

        // After successfully login, get jwt token
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .avatar(user.getAvatar())
                .token(token)
                .build();

        return Result.success(userLoginVO);
    }

}
