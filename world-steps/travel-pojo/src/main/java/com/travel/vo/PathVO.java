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
@ApiModel(description = "路径展示页面的数据格式")
public class PathVO {
    private String id;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private int duration;

    private String startAddress;

    private String endAddress;

    private String name;
}
