<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시글</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container">
        <!-- 메시지 -->
        <jsp:include page="../common/message.jsp" />
        <!--// 메시지 -->
        <h1 class="text-center my-4">게시글 수정</h1>
        <div class="card">
            <div class="card-body">
                <form id="boardForm" action="/board/${boardVo.seq}/update/" method="POST">
                    <div class="mb-3">
                        <label for="title" class="form-label">제목</label>
                        <input type="text" class="form-control" id="title" name="title" placeholder="제목을 입력하세요" required value="${boardVo.title}">
                    </div>
                    <div class="mb-3">
                        <label for="content" class="form-label">내용</label>
                        <textarea class="form-control" id="content" name="content" rows="5" placeholder="내용을 입력하세요" required>${boardVo.content}</textarea>
                    </div>
                    <div class="mb-3">
                        <label for="username" class="form-label">작성자</label>
                        <input type="text" class="form-control" id="username" name="username" placeholder="작성자를 입력하세요" required value="${boardVo.username}">
                    </div>
                    <div class="mb-3">
                        <label for="passwd" class="form-label">비밀번호</label>
                        <input type="password" class="form-control" id="passwd" name="passwd" placeholder="비밀번호를 입력하세요" required>
                    </div>
                    <button type="submit" class="btn btn-primary">등록하기</button>
                    <a href="/board/${boardVo.seq}/" class="btn btn-secondary">취소</a>
                </form>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>