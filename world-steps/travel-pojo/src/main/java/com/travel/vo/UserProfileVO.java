package com.travel.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户主页返回的数据格式")
public class UserProfileVO {

    @ApiModelProperty("主键值")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("收到点赞数")
    private Integer likeCount;

    @ApiModelProperty("粉丝数")
    private long followerCount;

    @ApiModelProperty("关注数")
    private long followingCount;

    @ApiModelProperty("是否已经关注")
    private boolean hasFollowed;
}
