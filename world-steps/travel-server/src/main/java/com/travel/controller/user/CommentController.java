package com.travel.controller.user;

import com.travel.constant.EntityTypeConstant;
import com.travel.context.BaseContext;
import com.travel.dto.CommentDTO;
import com.travel.dto.CommentPageQueryDTO;
import com.travel.entity.Event;
import com.travel.event.EventProducer;
import com.travel.result.PageResult;
import com.travel.result.Result;
import com.travel.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/comment")
@Api(tags = "Comment related interfaces")
@Slf4j
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private EventProducer eventProducer;

    /**
     * Page query comment
     * @param commentPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("Comment page query")
    public Result<PageResult> page(CommentPageQueryDTO commentPageQueryDTO) {
        log.info("分页查询评论：{}", commentPageQueryDTO);
        PageResult pageResult = commentService.pageQuery(commentPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * Add a reply to comment
     * @param commentDTO
     * @return
     */
    @PostMapping("/reply")
    @ApiOperation("Add a new reply comment")
    public Result addReplyToComment(@RequestBody CommentDTO commentDTO) {
        log.info("回复评论：{}", commentDTO);
        commentService.add(commentDTO);

        // Send notification
        Map<String, Object> data = new HashMap<>();
        data.put("postId", commentDTO.getPostId());
        Event event = Event.builder()
                .topic(EntityTypeConstant.TOPIC_COMMENT)
                .userId(BaseContext.getCurrentId())
                .entityType(EntityTypeConstant.ENTITY_TYPE_COMMENT)
                .entityId(commentDTO.getCommentId())
                .entityUserId(commentDTO.getTargetId())
                .data(data)
                .build();

        eventProducer.fireEvent(event);

        return Result.success();
    }
}
