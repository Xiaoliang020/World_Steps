package com.travel.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "评论分页的数据模型")
public class CommentPageQueryDTO {

    private int entityType;

    private Long entityId;

    //页码
    private int page;

    //每页显示记录数
    private int pageSize;
}
