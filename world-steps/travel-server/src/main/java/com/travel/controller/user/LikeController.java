package com.travel.controller.user;

import com.travel.dto.LikeDTO;
import com.travel.result.Result;
import com.travel.service.LikeService;
import com.travel.vo.LikeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/like")
@Api(tags = "Like related interfaces")
@Slf4j
public class LikeController {

    @Autowired
    private LikeService likeService;

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

        LikeVO likeVO = new LikeVO(likeCount, likeStatus);

        return Result.success(likeVO);
    }

}
