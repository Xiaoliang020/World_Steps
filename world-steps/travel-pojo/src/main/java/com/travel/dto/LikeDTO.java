package com.travel.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@ApiModel(description = "用户点赞的数据模型")
@AllArgsConstructor
@NoArgsConstructor
public class LikeDTO implements Serializable {

    private Long userId;

    private int entityType;

    private Long entityId;
}
