package com.travel.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@ApiModel(description = "用户上传标点的数据模型")
public class MarkerDTO {
    @Id
    private String id; // Object ID

    private double markerLat;
    private double markerLng;
    private String type;
    private String markerID;
    private String name;
    private String text;
    private String icon;
    private String pathID;
    // private String picture;
    private List<String> picture;
}
