package com.travel.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "用户登录时传递的数据模型")
public class UserLoginDTO implements Serializable {

    private String username;

    private String password;
}
