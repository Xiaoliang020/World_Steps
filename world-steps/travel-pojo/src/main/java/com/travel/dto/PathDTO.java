package com.travel.dto;

import com.travel.entity.Coordinate;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(description = "用户上传路径的数据模型")
public class PathDTO {
    private List<Coordinate> path;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private int duration;

    private String startAddress;

    private String endAddress;

    private String userId;

    private String name;
}
