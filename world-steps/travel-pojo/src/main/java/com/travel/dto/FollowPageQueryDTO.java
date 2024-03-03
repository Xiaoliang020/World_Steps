package com.travel.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@ApiModel(description = "用户查询关注分页的数据模型")
@AllArgsConstructor
@NoArgsConstructor
public class FollowPageQueryDTO implements Serializable {

    private Long userId;

    //页码
    private int page;

    //每页显示记录数
    private int pageSize;
}
