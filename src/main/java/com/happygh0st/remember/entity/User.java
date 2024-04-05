package com.happygh0st.remember.entity;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.happygh0st.remember.common.Modifiable;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@TableName("users")
public class User {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("created_at")
    private LocalDateTime created_at;

    @TableField("updated_at")
    private LocalDateTime updated_at;

    @TableField("deleted_at")
    private LocalDateTime deleted_at;

    private String username;

    private String password;

    @Modifiable()
    private String phone;

    @Modifiable()
    private String email;

    @TableField("birthday")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Modifiable(pattern = "yyyy-MM-dd HH:mm:ss", type = Date.class)
    private Date birthday;

    @TableField("openId")
    private String openId;

    private String roles;

    public User Desensitization() {
        String s = JSON.toJSONString(this);
        User user = JSON.parseObject(s, User.class);
        user.setId(0);
        user.setPassword("");
        user.setOpenId("");
        user.setPhone(user.getPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
        return user;
    }
}
