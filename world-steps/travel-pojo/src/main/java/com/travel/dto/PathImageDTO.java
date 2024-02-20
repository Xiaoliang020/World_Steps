package com.travel.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "路径截图上传时的数据模型")
public class PathImageDTO {

    private String id;

    private String screenshot;
}
