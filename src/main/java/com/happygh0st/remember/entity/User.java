package com.happygh0st.remember.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDateTime;

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

    private String phone;

    private String email;

    @TableField("birthday")
    private Date birthday;

    @TableField("openId")
    private String openId;

    private String roles;
}
