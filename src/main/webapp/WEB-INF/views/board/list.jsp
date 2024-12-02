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
        <h1 class="text-center my-4">게시글 목록</h1>

        <!-- 검색, 등록 버튼 -->
        <div class="d-flex justify-content-between mb-3">
            <!-- 검색 폼 -->
            <form action="/board/" method="GET">
                <div class="input-group">
                    <select name="searchType" class="form-select">
                        <option value="all" ${searchType == 'all' ? 'selected' : ''}>전체</option>
                        <option value="title" ${searchType == 'title' ? 'selected' : ''}>제목</option>
                        <option value="content" ${searchType == 'content' ? 'selected' : ''}>내용</option>
                        <option value="username" ${searchType == 'username' ? 'selected' : ''}>작성자</option>
                    </select>
                    <input type="text" name="searchKeyword" class="form-control" value="${searchKeyword}" placeholder="검색어를 입력하세요">
                    <button type="submit" class="btn btn-primary">검색</button>
                    <c:if test="${searchType != null}">
                        <a href="/board/" class="btn btn-danger">취소</a>
                    </c:if>
                </div>
            </form>

            <!-- 게시글 등록 버튼 -->
            <a href="/board/create/" class="btn btn-primary">게시글 등록</a>
        </div>

        <!-- 게시글 목록 -->
        <table class="table table-striped table-hover table-bordered">
            <thead>
                <tr>
                    <th>번호</th>
                    <th>제목</th>
                    <th>작성자</th>
                    <th>생성일시</th>
                    <th>수정일시</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${boardVoList}" var="boardVo">
                    <tr>
                        <td>${boardVo.seq}</td>
                        <td><a href="/board/${boardVo.seq}/">${boardVo.title}</a></td>
                        <td>${boardVo.username}</td>
                        <td>${boardVo.createdAt}</td>
                        <td>${boardVo.updatedAt}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <!-- 페이지네이션 -->
        <nav aria-label="Page navigation">
            <ul class="pagination justify-content-center">
                <c:if test="${pagination.page > 1}">
                    <li class="page-item">
                        <a class="page-link" href="/board/?page=1">처음</a>
                    </li>
                    <li class="page-item">
                        <a class="page-link" href="/board/?page=${pagination.page - 1}">이전</a>
                    </li>
                </c:if>

                <c:forEach begin="${pagination.startPage}" end="${pagination.endPage}" var="pageNum">
                    <li class="page-item ${pagination.page == pageNum ? 'active' : ''}">
                        <a class="page-link" href="/board/?page=${pageNum}">${pageNum}</a>
                    </li>
                </c:forEach>

                <c:if test="${pagination.page < pagination.totalPages}">
                    <li class="page-item">
                        <a class="page-link" href="/board/?page=${pagination.page + 1}">다음</a>
                    </li>
                    <li class="page-item">
                        <a class="page-link" href="/board/?page=${pagination.totalPages}">마지막</a>
                    </li>
                </c:if>
            </ul>
        </nav>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
