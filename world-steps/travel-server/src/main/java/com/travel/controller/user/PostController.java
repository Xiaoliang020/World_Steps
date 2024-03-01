package com.travel.controller.user;

import com.travel.dto.CommentDTO;
import com.travel.dto.PostDTO;
import com.travel.dto.PostPageQueryDTO;
import com.travel.result.PageResult;
import com.travel.result.Result;
import com.travel.service.PostService;
import com.travel.vo.PostVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/post")
@Slf4j
@Api(tags = "Post related interfaces")
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * Save the post user created
     * @param postDTO
     * @return
     */
    @PostMapping
    @ApiOperation("Post save")
    public Result save(@RequestBody PostDTO postDTO) {
        // save the marker info in database
        log.info("User create a new post: {}", postDTO);
        postService.save(postDTO);
        return Result.success();
    }

    /**
     * Get post by page
     * @param postPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("Post page query")
    public Result<PageResult> page(PostPageQueryDTO postPageQueryDTO) {
        log.info("分页查询帖子：{}", postPageQueryDTO);
        PageResult pageResult= postService.pageQuery(postPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * Get post by id
     * @param postId
     * @return
     */
    @GetMapping("/{postId}")
    @ApiOperation("Get post by id")
    public Result<PostVO> getById(@PathVariable Long postId) {
        log.info("查询帖子：{}", postId);
        PostVO postVO = postService.getById(postId);
        return Result.success(postVO);
    }

    @PostMapping("/reply")
    @ApiOperation("Add a new reply comment")
    public Result addReplyToPost( @RequestBody CommentDTO commentDTO) {
        log.info("回复帖子：{}", commentDTO);
        postService.addReply(commentDTO);
        return Result.success();
    }
}
