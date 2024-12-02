package com.example.spring.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BoardDao {

    @Autowired
    private SqlSession session;

    private static final String namespace = "boardMapper";

    // 게시글 등록
    public int create(BoardVo boardVo) {
        return session.insert(namespace + ".create", boardVo);
    }

    // 게시글 목록
    public List<BoardVo> list(int offset, int pageSize, String searchType, String searchKeyword) {
        Map<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("pageSize", pageSize);
        params.put("searchType", searchType);
        params.put("searchKeyword", searchKeyword);
        return session.selectList(namespace + ".list", params);
    }

    // 게시글 보기
    public BoardVo read(int seq) {
        return session.selectOne(namespace + ".read", seq);
    }

    // 게시글 수정
    public int update(BoardVo boardVo) {
        return session.update(namespace + ".update", boardVo);
    }

    // 게시글 삭제
    public int delete(int seq) {
        return session.delete(namespace + ".delete", seq);
    }

    // 전체 게시글 수
    public int getTotalCount(String searchType, String searchKeyword) {
        Map<String, Object> params = new HashMap<>();
        params.put("searchType", searchType);
        params.put("searchKeyword", searchKeyword);
        return session.selectOne(namespace + ".getTotalCount", params);
    }
}
