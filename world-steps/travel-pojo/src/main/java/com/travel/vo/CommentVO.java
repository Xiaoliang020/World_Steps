package com.travel.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentVO {

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

    private List<CommentVO> replies;

    private Long likeCount;

    private int likeStatus;

    private String targetName;
}
