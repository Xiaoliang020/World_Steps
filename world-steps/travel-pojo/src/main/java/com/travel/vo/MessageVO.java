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
@ApiModel(description = "消息列表的数据格式")
public class MessageVO {

    private Long id;

    private Long fromId;

    private Long toId;

    private String conversationId;

    private String content;

    // 0 代表未读， 1代表已读，2代表删除
    private int status;

    private LocalDateTime createTime;

    private int unread;

    private String avatar;

    private String username;
}
