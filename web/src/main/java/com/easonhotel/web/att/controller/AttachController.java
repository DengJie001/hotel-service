package com.easonhotel.web.att.controller;

import com.easonhotel.common.ResData;
import com.easonhotel.common.utils.AttachUtils;
import com.easonhotel.dao.att.entity.Attach;
import com.easonhotel.dao.att.service.IAttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/static")
public class AttachController {

    @Autowired
    private IAttachService attachService;

    private static final String ATTACH_PATH = "D:\\EasonHotelFile\\Images\\";

    @PostMapping("/upload")
    public ResData<Object> upload(@RequestParam("file") MultipartFile file) {
        AttachUtils.Attachment attachment = null;
        try {
            attachment = AttachUtils.upload(file, ATTACH_PATH);
        } catch (IOException e) {
            e.printStackTrace();
            return ResData.create(-1, "上传文件异常");
        }
        Attach attach = new Attach();
        attach.setFileSize(Integer.parseInt(attachment.fileSize.toString()));
        attach.setFileType(attachment.fileType);
        attach.setRealName(attachment.realName);
        attach.setSaveName(attachment.saveName);
        attach.setSavePath(attachment.savePath);
        attachService.save(attach);
        return ResData.create(attach.getId());
    }

    @GetMapping("/download/{id}")
    public Object download(@PathVariable String id) throws UnsupportedEncodingException {
        Attach attach = attachService.getById(id);
        if (attach == null) {
            ResData.create(-1, null, "无此图片");
        }
        String path = ATTACH_PATH + attach.getSavePath() + "\\" + attach.getSaveName();
        File file = new File(path);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Cache-Control", "public, max-age=2592000");
        String encodeName = URLEncoder.encode(attach.getRealName(), StandardCharsets.UTF_8.toString());
        if (attach.getFileType().startsWith("image")) {
            httpHeaders.add("Content-Disposition", "inline; filename*=UTF-8''" + encodeName);
        } else {
            httpHeaders.add("Content-Disposition", "attachment; filename*=UTF-8''" + encodeName);
        }
        return ResponseEntity.ok().headers(httpHeaders).contentLength(file.length()).contentType(MediaType.parseMediaType(attach.getFileType())).body(new FileSystemResource(file));
    }
}
