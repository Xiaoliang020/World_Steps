package com.travel.controller.user;

import com.travel.dto.MarkerDTO;
import com.travel.entity.Marker;
import com.travel.result.Result;
import com.travel.service.MarkerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/marker")
@Slf4j
@Api(tags = "Marker related interfaces")
public class MarkerController {
    @Autowired
    private MarkerService markerService;

    @PostMapping()
    @ApiOperation("Marker save")
    public Result save(@RequestBody MarkerDTO markerDTO) {
        // save the marker info in database
        log.info("User save a new marker: {}", markerDTO);
        markerService.save(markerDTO);
        return Result.success();
    }

    @PutMapping
    @ApiOperation("Marker update")
    public Result update(@RequestBody MarkerDTO markerDTO) {
        log.info("User update a marker: {}", markerDTO);
        markerService.update(markerDTO);
        return Result.success();
    }
}
