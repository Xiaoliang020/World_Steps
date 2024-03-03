package com.travel.controller.user;

import com.travel.annotation.Log;
import com.travel.constant.EntityTypeConstant;
import com.travel.constant.JwtClaimsConstant;
import com.travel.context.BaseContext;
import com.travel.dto.UserDTO;
import com.travel.dto.UserLoginDTO;
import com.travel.entity.User;
import com.travel.properties.JwtProperties;
import com.travel.result.Result;
import com.travel.service.FollowService;
import com.travel.service.LikeService;
import com.travel.service.UserService;
import com.travel.utils.JwtUtil;
import com.travel.vo.UserLoginVO;
import com.travel.vo.UserProfileVO;
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
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/user")
    @ApiOperation("User registration")
    public Result register(@RequestBody UserDTO userDTO) {
        log.info("New user: {}", userDTO);
        userService.save(userDTO);
        return Result.success();
    }

    /**
     * User log in
     * @param userLoginDTO
     * @return
     */
    @Log
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

    /**
     * Update User info
     * @param userDTO
     * @return
     */
    @PutMapping
    @ApiOperation("Update User info")
    public Result update(@RequestBody UserDTO userDTO) {
        log.info("Edit user info: {}", userDTO);
        userService.update(userDTO);
        return Result.success();
    }

    /**
     * Get user name by id
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    @ApiOperation("Get user name by id")
    public Result<String> getUsername(@PathVariable Long userId) {
        log.info("Get username by id: {}", userId);
        String username = userService.getUsername(userId);
        return Result.success(username);
    }

    @GetMapping("/profile/{userId}")
    @ApiOperation("Get user profile info")
    public Result<UserProfileVO> profile(@PathVariable Long userId) {
        log.info("Get profile by id: {}", userId);
        UserProfileVO userProfileVO = userService.getProfile(userId);
        // Get like count
        int userLikeCount = likeService.findUserLikeCount(userId);
        userProfileVO.setLikeCount(userLikeCount);

        // Get following count
        long followingCount = followService.findFollowingCount(userId, EntityTypeConstant.ENTITY_TYPE_USER);
        userProfileVO.setFollowingCount(followingCount);

        // Get follower count
        long followerCount = followService.findFollowerCount(EntityTypeConstant.ENTITY_TYPE_USER, userId);
        userProfileVO.setFollowerCount(followerCount);

        // Get if followed the user
        boolean hasFollowed = followService.hasFollowed(BaseContext.getCurrentId(), EntityTypeConstant.ENTITY_TYPE_USER, userId);
        userProfileVO.setHasFollowed(hasFollowed);

        return Result.success(userProfileVO);
    }
}
