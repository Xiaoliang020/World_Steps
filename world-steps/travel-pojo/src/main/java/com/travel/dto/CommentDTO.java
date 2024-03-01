package com.travel.dto;

import lombok.Data;

@Data
public class CommentDTO {

    private Long postId;

    private Long commentId;

    private Long userId;

    private String username;

    private String avatar;

    private String content;

    private Long targetId;
}
