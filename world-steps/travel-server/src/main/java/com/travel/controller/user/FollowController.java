package com.travel.controller.user;

import com.travel.constant.EntityTypeConstant;
import com.travel.dto.FollowDTO;
import com.travel.dto.FollowPageQueryDTO;
import com.travel.entity.Event;
import com.travel.event.EventProducer;
import com.travel.result.PageResult;
import com.travel.result.Result;
import com.travel.service.FollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/follow")
@Api(tags = "Follow related interfaces")
@Slf4j
public class FollowController {

    @Autowired
    private FollowService followService;

    @Autowired
    private EventProducer eventProducer;

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

        // Send notification
        Event event = Event.builder()
                .topic(EntityTypeConstant.TOPIC_FOLLOW)
                .userId(followDTO.getUserId())
                .entityType(followDTO.getEntityType())
                .entityId(followDTO.getEntityId())
                .entityUserId(followDTO.getEntityId())
                .build();

        eventProducer.fireEvent(event);

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

    /**
     * Page query followings
     * @param followPageQueryDTO
     * @return
     */
    @GetMapping("/page/followings")
    @ApiOperation("Page query followings")
    public Result<PageResult> pageFollowings(FollowPageQueryDTO followPageQueryDTO) {
        log.info("Page query followings: {}", followPageQueryDTO);
        PageResult pageResult = followService.pageFollowings(followPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * Page query followers
     * @param followPageQueryDTO
     * @return
     */
    @GetMapping("/page/followers")
    @ApiOperation("Page query followers")
    public Result<PageResult> pageFollowers(FollowPageQueryDTO followPageQueryDTO) {
        log.info("Page query followers: {}", followPageQueryDTO);
        PageResult pageResult = followService.pageFollowers(followPageQueryDTO);
        return Result.success(pageResult);
    }
}
