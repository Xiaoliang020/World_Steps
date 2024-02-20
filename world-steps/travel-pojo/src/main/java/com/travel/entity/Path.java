package com.travel.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonLineString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "paths")
@Data
public class Path {
    @Id
    private String id;

    private List<Coordinate> path;

    private GeoJsonLineString lineString;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private int duration;

    private String startAddress;

    private String endAddress;

    private String userId;

    private String name;

    private String screenshot;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    private Long createUser;
}
