package com.travel.controller.user;

import com.travel.constant.EntityTypeConstant;
import com.travel.dto.LikeDTO;
import com.travel.entity.Event;
import com.travel.event.EventProducer;
import com.travel.result.Result;
import com.travel.service.LikeService;
import com.travel.vo.LikeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/like")
@Api(tags = "Like related interfaces")
@Slf4j
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private EventProducer eventProducer;

    @PostMapping
    @ApiOperation("Like")
    public Result<LikeVO> like(@RequestBody LikeDTO likeDTO) {
        log.info("用户点赞: {}", likeDTO);

        // Like
        likeService.like(likeDTO);

        // Get count
        long likeCount = likeService.findEntityLikeCount(likeDTO);

        // Get status
        int likeStatus = likeService.findLikeStatus(likeDTO);

        if (likeStatus == 1) {
            Map<String, Object> data = new HashMap<>();
            data.put("postId", likeDTO.getPostId());
            // Send notification
            Event event = Event.builder()
                    .topic(EntityTypeConstant.TOPIC_LIKE)
                    .userId(likeDTO.getUserId())
                    .entityType(likeDTO.getEntityType())
                    .entityId(likeDTO.getEntityId())
                    .entityUserId(likeDTO.getEntityUserId())
                    .data(data)
                    .build();

            eventProducer.fireEvent(event);
        }

        LikeVO likeVO = new LikeVO(likeCount, likeStatus);

        return Result.success(likeVO);
    }

}
