package com.happygh0st.remember.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.happygh0st.remember.common.Results;
import com.happygh0st.remember.common.Roles;
import com.happygh0st.remember.entity.FileInfo;
import com.happygh0st.remember.entity.User;
import com.happygh0st.remember.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/file")
public class fileController {

    @Value("${remember.filePath}")
    private String filePath;

    private final UserUtils userUtils;

    public fileController(UserUtils userUtils) {
        this.userUtils = userUtils;
    }

    @PostMapping("/upload")
    @Roles()
    public Results upload(MultipartFile file) {
        synchronized (fileController.class) {
            String flag = System.currentTimeMillis() + "";
            String filename = file.getOriginalFilename();
            if (filename != null) {
                filename = filename.replace("-", "");
            } else {
                return Results.StatusErr().setMessage("文件名有误或未上传文件");
            }
            try {
                if (!FileUtil.isDirectory(filePath)) {
                    FileUtil.mkdir(filePath);
                }
                User user = userUtils.getUser();
                String path = filePath + user.getUsername() + "\\";
                if (!FileUtil.isDirectory(path)) {
                    FileUtil.mkdir(path);
                }
                FileUtil.writeBytes(file.getBytes(), path + flag + "-" + filename);
                log.info("用户{}-{}上传成功", user.getUsername(), filename);
                Thread.sleep(1L);
                return Results.StatusOk().setMessage("上传成功").addData("flag", flag);
            } catch (Exception e) {
                return Results.StatusErr().setMessage(e.getMessage());
            }
        }
    }

    @GetMapping("/download/{flag}")
    @Roles()
    public void download(@PathVariable("flag") String flag, HttpServletResponse response) {
        User user = userUtils.getUser();
        String path = filePath + user.getUsername() + "\\";
        if (!FileUtil.isDirectory(path)) {
            FileUtil.mkdir(path);
        }
        OutputStream os;
        List<String> fileNames = FileUtil.listFileNames(path);
        String avatar = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");
        try {
            if (StrUtil.isNotEmpty(avatar)) {
                avatar = avatar.split("-")[1];
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(avatar, StandardCharsets.UTF_8));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(path + avatar);
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
                log.info("用户{}-文件{}下载成功", user.getUsername(), avatar);
            }
        } catch (Exception e) {
            log.info("用户{}-文件{}下载失败", user.getUsername(), avatar);
        }
    }

    @GetMapping("/list")
    @Roles()
    public Results getFilesList() {
        User user = userUtils.getUser();
        String path = filePath + user.getUsername() + "\\";
        if (!FileUtil.isDirectory(path)) {
            FileUtil.mkdir(path);
        }
        List<String> fileNames = FileUtil.listFileNames(path);
        List<FileInfo> files = new ArrayList<>();
        for (String i : fileNames) {
            String[] ss = i.split("-");
            if (ss.length != 2) {
                continue;
            }
            Long times = Long.valueOf(ss[0]);
            String name = ss[1];
            String filePath = path + i;
            files.add(new FileInfo(filePath, times, name, user.getUsername()));
        }
        log.info("用户：{} 获取了文件列表", user.getUsername());
        return Results.StatusOk().setMessage("获取成功").addData("files", files);
    }
}
