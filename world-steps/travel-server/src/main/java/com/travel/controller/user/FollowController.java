package com.travel.controller.user;

import com.travel.dto.FollowDTO;
import com.travel.result.Result;
import com.travel.service.FollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/follow")
@Api(tags = "Follow related interfaces")
@Slf4j
public class FollowController {

    @Autowired
    private FollowService followService;

    /**
     * Follow an entity
     * @param followDTO
     * @return
     */
    @PostMapping
    @ApiOperation("Follow an entity")
    public Result follow(@RequestBody FollowDTO followDTO) {
        log.info("Follow: {}", followDTO);
        followService.follow(followDTO);
        return Result.success();
    }

    /**
     * Unfollow an entity
     * @param followDTO
     * @return
     */
    @PostMapping("/unfollow")
    @ApiOperation("Unfollow an entity")
    public Result unfollow(@RequestBody FollowDTO followDTO) {
        log.info("Unfollow: {}", followDTO);
        followService.unfollow(followDTO);
        return Result.success();
    }
}
