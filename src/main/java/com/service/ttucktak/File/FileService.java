package com.service.ttucktak.File;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.io.IOException;

@Service
@Slf4j
public class FileService {

    private String uploadDir = "/home/ubuntu/";

    public void fileUpload(MultipartFile multipartFile) {
        // File.seperator 는 OS종속적이다.
        // Spring에서 제공하는 cleanPath()를 통해서 ../ 내부 점들에 대해서 사용을 억제한다
        Path copyOfLocation = Paths.get(uploadDir + File.separator + StringUtils.cleanPath(multipartFile.getOriginalFilename()));
        try {
            // inputStream을 가져와서
            // copyOfLocation (저장위치)로 파일을 쓴다.
            // copy의 옵션은 기존에 존재하면 REPLACE(대체한다), 오버라이딩 한다
            Files.copy(multipartFile.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * URL에 있는 이미지 다운로드
     */
    public MultipartFile downloadFile(String imageUrl, String fileName, String orifinFileName, String contentType) throws IOException {
        return new ImageMultipartFile(imageUrl, fileName, orifinFileName, contentType);
    }


}
