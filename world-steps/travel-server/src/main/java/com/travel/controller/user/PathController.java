package com.travel.controller.user;

import com.travel.dto.PathDTO;
import com.travel.dto.PathImageDTO;
import com.travel.dto.PathPageQueryDTO;
import com.travel.result.PageResult;
import com.travel.result.Result;
import com.travel.service.PathService;
import com.travel.vo.PathVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/path")
@Api(tags = "Path related interfaces")
@Slf4j
public class PathController {

    @Autowired
    private PathService pathService;

    /**
     * Save a new path
     * @param pathDTO
     * @return
     */
    @PostMapping
    @ApiOperation("Path save")
    public Result<String> save(@RequestBody PathDTO pathDTO) {
        log.info("User save a new path: {}", pathDTO);
        // save the path info in database
        String pathId = pathService.save(pathDTO);
        return Result.success(pathId);
    }

    /**
     * Upload path screenshot
     * @param pathImageDTO
     * @return
     */
    @PutMapping
    @ApiOperation("Path screenshot upload")
    public Result uploadImage(@RequestBody PathImageDTO pathImageDTO) {
        log.info("User upload the screenshot: {}", pathImageDTO);
        pathService.updateImage(pathImageDTO);
        return Result.success();
    }

    /**
     * Search path by id and query by page
     * @param pathPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("Path page query")
    public Result<PageResult> page(PathPageQueryDTO pathPageQueryDTO) {
        log.info("分页查询：{}", pathPageQueryDTO);
        PageResult pageResult= pathService.pageQuery(pathPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * Delete by path id
     * @param pathId
     * @return
     */
    @DeleteMapping
    @ApiOperation("Path deletion")
    public Result deleteById(String pathId) {
        log.info("删除路径：{}", pathId);
        pathService.deleteById(pathId);
        return Result.success();
    }
}
