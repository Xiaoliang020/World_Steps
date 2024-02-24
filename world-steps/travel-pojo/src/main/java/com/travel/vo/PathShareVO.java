package com.travel.vo;


import com.travel.entity.Coordinate;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "路径分享页面的数据格式")
public class PathShareVO {
    private String id;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String startAddress;

    private String endAddress;

    private String name;

    private List<Coordinate> path;
}
