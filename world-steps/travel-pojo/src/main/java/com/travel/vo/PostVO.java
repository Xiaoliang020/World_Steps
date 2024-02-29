package com.travel.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "帖子展示页面的数据格式")
public class PostVO {
    private Long id;

    private String title;

    private String content;

    private String username;

    private String avatar;

    private String pathId;

    private String screenshot;

    private int commentCount;

    private double score;

    private LocalDateTime createTime;
}
