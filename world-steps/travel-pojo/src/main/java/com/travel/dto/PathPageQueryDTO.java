package com.travel.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PathPageQueryDTO implements Serializable {
    //用户ID
    private String userId;

    //页码
    private int page;

    //每页显示记录数
    private int pageSize;
}
