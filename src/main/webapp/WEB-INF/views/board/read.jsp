<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="../common/top.jsp" />

<div class="container">
    <!-- 메시지 -->
    <jsp:include page="../common/message.jsp" />
    <!--// 메시지 -->

    <h1 class="text-center my-4">게시글 보기</h1>

    <!-- 게시글 보기 -->
    <table class="table table-bordered mb-3">
        <tr>
            <th class="text-center col-2">제목</th>
            <td class="col-10">${boardVo.title}</td>
        </tr>
        <tr>
            <th class="text-center col-2">글쓴이</th>
            <td class="col-10">${boardVo.username}</td>
        </tr>
        <tr>
            <th class="text-center col-2">등록일시</th>
            <td class="col-10">${boardVo.createdAt.substring(0, 16)}</td>
        </tr>
        <tr>
            <th class="text-center col-2">내용</th>
            <td class="col-10">${boardVo.content}</td>
        </tr>
        <c:if test="${not empty boardVo.fileName}">
            <tr>
                <th class="text-center col-2">첨부파일</th>
                <td class="col-10"><a href="/board/${boardVo.seq}/download">${boardVo.originalFileName}</a></td>
            </tr>
        </c:if>
    </table>
    <div>
        <a href="/board/" class="btn btn-primary">게시글 목록</a>
        <a href="/board/${boardVo.seq}/update" class="btn btn-warning">게시글 수정</a>
        <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#deleteModal">게시글 삭제</button>
    </div>
    <!--// 게시글 보기 -->

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
    <!--// 삭제 모달 -->

    <!-- 스크립트 -->
    <jsp:include page="../common/script.jsp" />
    <!--// 스크립트 -->

</div>

<jsp:include page="../common/bottom.jsp" />