package com.travel.dto;

import lombok.Data;

@Data
public class MessageConversationPageQueryDTO {
    //用户ID
    private String conversationId;

    //页码
    private int page;

    //每页显示记录数
    private int pageSize;
}
