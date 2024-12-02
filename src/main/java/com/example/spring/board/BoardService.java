package com.example.spring.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.spring.common.Pagination;

@Service
public class BoardService {

    @Autowired
    BoardDao boardDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 비밀번호 검증
    private boolean verifyPassword(int seq, String password) {
        BoardVo board = boardDao.read(seq);
        return board != null && passwordEncoder.matches(password, board.getPasswd());
    }

    // 게시글 등록
    public boolean create(BoardVo boardVo) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(boardVo.getPasswd());
        boardVo.setPasswd(encodedPassword);

        int result = boardDao.create(boardVo);
        return result > 0;
    }

    // 게시글 목록
    public Map<String, Object> list(int page, String searchType, String searchKeyword) {
        int pageSize = 10; // 페이지당 게시글 수

        // 전체 게시글 수 조회
        int totalCount = boardDao.getTotalCount(searchType, searchKeyword);

        // 페이지네이션 정보 생성
        Pagination pagination = new Pagination(page, pageSize, totalCount);

        // 페이징된 게시글 목록 조회
        List<BoardVo> boardVoList = boardDao.list(pagination.getOffset(), pageSize, searchType, searchKeyword);

        // 결과 맵 생성
        Map<String, Object> result = new HashMap<>();
        result.put("boardVoList", boardVoList);
        result.put("pagination", pagination);
        result.put("searchType", searchType);
        result.put("searchKeyword", searchKeyword);

        return result;
    }

    // 게시글 보기
    public BoardVo read(int seq) {
        return boardDao.read(seq);
    }

    // 게시글 수정
    public boolean update(BoardVo boardVo) {
        // 비밀번호 검증
        if (!verifyPassword(boardVo.getSeq(), boardVo.getPasswd())) {
            return false;
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(boardVo.getPasswd());
        boardVo.setPasswd(encodedPassword);

        int result = boardDao.update(boardVo);
        return result > 0;
    }

    // 게시글 삭제
    public boolean delete(BoardVo boardVo) {
        // 비밀번호 검증
        if (!verifyPassword(boardVo.getSeq(), boardVo.getPasswd())) {
            return false;
        }

        int result = boardDao.delete(boardVo.getSeq());
        return result > 0;
    }
}
