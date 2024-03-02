package com.travel.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "发送消息传递的数据模型")
public class MessageDTO implements Serializable {
    private Long fromId;

    private String toName;

    private String content;
}
