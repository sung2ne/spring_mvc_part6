package com.example.spring.board;

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
}
