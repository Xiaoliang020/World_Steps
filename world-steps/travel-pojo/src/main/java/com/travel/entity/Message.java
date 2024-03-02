package com.travel.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    public static final int UNREAD_STATUS = 0;

    public static final int READ_STATUS = 1;

    private Long id;

    private Long fromId;

    private Long toId;

    private String conversationId;

    private String content;

    // 0 代表未读， 1代表已读，2代表删除
    private int status;

    private LocalDateTime createTime;

}
