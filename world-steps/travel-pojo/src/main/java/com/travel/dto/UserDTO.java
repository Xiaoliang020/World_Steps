package com.travel.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "用户注册时传递的数据模型")
public class UserDTO implements Serializable {

    private Long id;

    private String username;

    private String password;

    private String phone;

    private String gender;

    private String email;

    private String avatar;
}
