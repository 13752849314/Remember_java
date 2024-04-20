package com.happygh0st.remember.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.happygh0st.remember.common.Modifiable;
import lombok.Data;

import java.util.Date;

@Data
@TableName("bills")
public class Bill {
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

    private String username;

    @TableField("consumeType")
    @Modifiable(type = Integer.class)
    private Integer consumeType;

    @TableField("consumeMoney")
    @Modifiable(type = Float.class)
    private Float consumeMoney;

    @TableField("consumeTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Modifiable(pattern = "yyyy-MM-dd HH:mm:ss", type = Date.class)
    private Date consumeTime;

    @Modifiable()
    private String remark;
}
