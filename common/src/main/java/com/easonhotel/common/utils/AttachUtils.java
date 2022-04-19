package com.easonhotel.common.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.io.Files;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.UUID;

public class AttachUtils {
    private static String getFileName() {
        return getFileName(null);
    }

    private static String getFileName(String extension) {
        if (StringUtils.isBlank(extension)) {
            extension = "jpg";
        }
        return UUID.randomUUID().toString().replaceAll("-", "") + "." + extension;
    }

    public static Attachment upload(MultipartFile file, String attachPath) throws IOException {
        String fileName = file.getOriginalFilename();
        String extension = Files.getFileExtension(fileName);
        String contentType = file.getContentType();
        String subStr = DateFormatUtils.format(new Date(), "yyyyMMdd");
        File subDir = new File(attachPath, subStr);
        subDir.mkdirs();
        String saveName = getFileName(extension);
        File originalFile = new File(subDir, saveName);
        file.transferTo(originalFile);
        Attachment attachment = new Attachment();
        attachment.realName = fileName;
        attachment.saveName = saveName;
        attachment.fileType = contentType;
        attachment.savePath = subStr;
        attachment.fileSize = file.getSize();
        return attachment;
    }

    public static Attachment upload(File file, String attachPath) throws IOException {
        String fileName = file.getName();
        String extension = Files.getFileExtension(fileName);
        String contentType = "image/jpeg";
        String subStr = DateFormatUtils.format(new Date(), "yyyyMMdd");
        File subDir  = new File(attachPath, subStr);
        subDir.mkdirs();
        String saveName = getFileName(extension);
        File originalFile = new File(subDir, saveName);
        FileInputStream input = new FileInputStream(file);
        FileOutputStream out = new FileOutputStream(originalFile);
        byte[] buffer = new byte[1024];
        int hasRead = 0;
        while ((hasRead = input.read(buffer)) > 0) {
            out.write(buffer, 0, hasRead);
        }
        input.close();
        out.close();
        Attachment attachment = new Attachment();
        attachment.realName = fileName;
        attachment.saveName = saveName;
        attachment.fileType = contentType;
        attachment.savePath = subStr;
        attachment.fileSize = file.length();
        return attachment;
    }

    public static class Attachment {
        public String realName;
        public String saveName;
        public String savePath;
        public String fileType;
        public Long fileSize;
    }
}
