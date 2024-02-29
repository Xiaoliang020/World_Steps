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
public class Post {
    private Long id;

    private String title;

    private String content;

    private String username;

    private String userId;

    private String avatar;

    private String pathId;

    private String screenshot;

    private int commentCount;

    private double score;

    private LocalDateTime createTime;
}
