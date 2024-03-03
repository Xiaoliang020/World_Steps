package com.travel.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@ApiModel(description = "用户关注的数据模型")
@AllArgsConstructor
@NoArgsConstructor
public class FollowDTO implements Serializable {

    private Long userId;

    // 1: User, 2：Post
    private int entityType;

    private Long entityId;
}
