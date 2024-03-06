package com.travel.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class NoticePageQueryDTO implements Serializable {

    //用户ID
    private Long userId;

    //通知类别
    private String topic;

    //页码
    private int page;

    //每页显示记录数
    private int pageSize;
}
