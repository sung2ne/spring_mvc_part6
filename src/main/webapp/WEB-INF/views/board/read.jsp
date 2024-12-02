<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시글</title>
    <!-- Bootstrap v5.3.3 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container">
        <!-- 메시지 -->
        <jsp:include page="../common/message.jsp" />
        <!--// 메시지 -->
        <h1 class="text-center my-4">게시글 보기</h1>
        <div class="card mb-3">
            <div class="card-header">${boardVo.title}</div>
            <div class="card-body">
                <div class="text-end text-muted">
                    <div>글쓴이 : ${boardVo.username}</div>
                    <div>등록일시 : ${boardVo.createdAt.substring(0, 16)}</div>
                </div>
                <div>${boardVo.content}</div>
                <!-- 첨부 파일 다운로드 링크 추가 -->
                <c:if test="${not empty boardVo.fileName}">
                    <div class="mt-3">
                        <strong>첨부 파일:</strong>
                        <a href="/board/${boardVo.seq}/download" class="btn btn-sm btn-outline-primary">
                            ${boardVo.originalFileName}
                            <i class="bi bi-download"></i>
                        </a>
                    </div>
                </c:if>
            </div>
        </div>
        <div>
            <a href="/board/" class="btn btn-primary">게시글 목록</a>
            <a href="/board/${boardVo.seq}/update" class="btn btn-warning">게시글 수정</a>
            <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#deleteModal">게시글 삭제</button>
        </div>
    </div>

    <!-- 삭제 모달 -->
    <div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <form id="deleteForm" action="/board/${boardVo.seq}/delete" method="POST">
                    <div class="modal-header">
                        <h1 class="modal-title fs-5" id="deleteModalModalLabel">게시글 삭제</h1>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="mb-3">
                            <span class="text-danger">삭제된 데이터는 복구할 수 없습니다.</span> 비밀번호를 입력해주세요.
                        </div>
                        <div>
                            <input type="password" id="password" name="password" placeholder="비밀번호" class="form-control" required>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">삭제 취소</button>
                        <button type="submit" class="btn btn-danger">게시글 삭제</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!-- 삭제 모달 -->

    <!-- Bootstrap v5.3.3 -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
