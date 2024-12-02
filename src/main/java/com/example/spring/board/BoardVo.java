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
    private MultipartFile uploadFile;
    private String fileName;
    private String originalFileName;
}
