package com.travel.vo;

import com.travel.entity.Comment;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentVO {
    public static final int ENTITY_TYPE_POST = 1;

    public static final int ENTITY_TYPE_COMMENT = 2;

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

    private List<Comment> replies;
}
