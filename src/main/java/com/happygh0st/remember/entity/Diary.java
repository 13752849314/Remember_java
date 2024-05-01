package com.happygh0st.remember.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("diary")
public class Diary {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created_at;

    @TableField("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updated_at;

    @TableField("deleted_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deleted_at;

    @TableField("recordDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date recordDate;

    private String mood;

    private String weather;

    private String type;

    private String message;

}
