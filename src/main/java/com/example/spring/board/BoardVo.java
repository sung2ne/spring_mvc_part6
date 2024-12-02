package com.example.spring.board;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class BoardVo {
    private int seq;
    private String title;
    private String content;
    private String username;
    private String passwd;
    private String createdAt;
    private String updatedAt;
    private MultipartFile uploadFile;   // 업로드 파일  
    private String fileName;            // 저장된 파일 이름
    private String originalFileName;    // 원본 파일 이름
    private boolean deleteFile;         // 파일 삭제 체크박스 상태
}
