package com.travel.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PostPageQueryDTO implements Serializable {
    //用户ID
    private String userId;

    //查找用户名
    private String username;

    //查找地址
    private String location;

    //查找标题
    private String title;

    //页码
    private int page;

    //每页显示记录数
    private int pageSize;
}
