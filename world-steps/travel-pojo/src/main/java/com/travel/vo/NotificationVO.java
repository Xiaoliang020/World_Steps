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
@ApiModel(description = "通知的数据格式")
public class NotificationVO {

//    private Message message;
    private String type;

    private LocalDateTime createTime;

    private String username;

    private String avatar;

    private int entityType;

    private Long entityId;

    private Long userId;

    private Long postId;

    private int count;

    private int unread;
}
