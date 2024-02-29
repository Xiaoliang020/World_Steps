package com.travel.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "用户发布帖子的数据模型")
public class PostDTO {
    private String title;

    private String content;

    private String username;

    private String userId;

    private String avatar;

    private String pathId;

    private String screenshot;
}
