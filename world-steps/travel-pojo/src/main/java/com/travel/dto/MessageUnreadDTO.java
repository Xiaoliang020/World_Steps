package com.travel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageUnreadDTO implements Serializable {
    //用户ID
    private Long userId;

    private String conversationId;
}
