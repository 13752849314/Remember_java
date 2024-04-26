package com.happygh0st.remember.entity;

import cn.hutool.core.io.FileUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class FileInfo {
    private String filename;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadTime;

    private String username;

    private String size;

    private Long timestamp;

    public FileInfo(String filePath, Long times, String name, String username) {
        this.username = username;
        this.timestamp = times;
        this.filename = name;

        this.size = FileUtil.file(filePath).length() / 1024 + 1 + "KB";
        this.uploadTime = new Date(times);

    }
}
