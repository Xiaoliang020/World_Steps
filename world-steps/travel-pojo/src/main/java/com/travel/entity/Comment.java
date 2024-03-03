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
public class Comment {

    private Long id;

    private Long userId;

    /**
     * 评论对象类型，1代表帖子，2代表评论
     */
    private int entityType;

    private Long entityId;

    private Long targetId;

    private String username;

    private String avatar;

    private String content;

    private int status;

    private LocalDateTime createTime;
}
