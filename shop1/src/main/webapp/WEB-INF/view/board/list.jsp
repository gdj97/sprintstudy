<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /WEB-INF/view/board/list.jsp --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html><html><head>
<meta charset="UTF-8">
<title>${boardName}</title>
</head><body>
<h2 class="text-center">${boardName}</h2>
<table class="table">
    <c:if test="${listcount > 0}"> <!-- 등록된 게시물 건수 -->
     <tr><td colspan="5" class="text-right">글갯수:${listcount}</td></tr>
     <tr><th>번호</th><th>제목</th><th>글쓴이</th><th>날짜</th><th>조회수</th></tr>
  <c:forEach var="board" items="${boardlist}">
      <tr><td>${boardno}</td><c:set var="boardno" value="${boardno - 1}" />
      <td><c:if test="${board.grplevel > 0}">
          <c:forEach begin="2" end="${board.grplevel}">&emsp;&emsp;</c:forEach>└</c:if><%-- ㅂ한자 --%>
      <a href="detail?num=${board.num}">${board.title}</a>
      </td>
      <td>${board.writer}</td><td>${board.regdate }</td>
      <td>${board.readcnt}</td></tr>
  </c:forEach>
  
     <tr><td colspan="5" class="text-center">
     <c:if test="${pageNum > 1}"><a href="list?pageNum=${pageNum - 1}&boardid=${boardid}" class="btn btn-primary">이전</a></c:if>
     <c:if test="${pageNum <= 1}"><span  class="btn btn-secondary">이전</span></c:if>
     <c:forEach var="a" begin="${startpage }" end="${endpage}">
         <c:if test="${a == pageNum}"><span class="btn btn-success">${a}</span></c:if>
         <c:if test="${a != pageNum}"><a href="list?pageNum=${a}&boardid=${boardid}" class="btn btn-secondary">${a}</a></c:if>
     </c:forEach>
     <c:if test="${pageNum < maxpage}">
       <a href="list?pageNum=${pageNum + 1}&boardid=${boardid}" class="btn btn-primary">다음</a></c:if>
     <c:if test="${pageNum >= maxpage}"><span class="btn btn-secondary">다음</span></c:if></td></tr>
   </c:if>
   
   <c:if test="${listcount == 0}">
      <tr><td colspan="5">등록된 게시물이 없습니다.</td></tr>
   </c:if>
   <tr><td colspan="5" class="text-center">
   <a href="write?boardid=${boardid}" class="btn btn-danger">글쓰기</a></td></tr>
</table></body></html>