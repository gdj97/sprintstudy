<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /WEB-INF/view/board/list.jsp
    1. 첨부파일이 있는 경우 목록에 표시하기
    2. 오늘 등록한 게시물의 날짜는  HH:mm:ss 로
       이전 등록한 게시물의 날짜는  yyyy-MM-dd HH:mm:ss 형식으로 출력하기 
    3. 중복된 검색이 가능하게 구현하기   
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html><html><head>
<meta charset="UTF-8">
<title>${boardName}</title>
</head><body>
<h2 class="text-center">${boardName}</h2>
<form action="list" method="post" name="searchform">
<table class="table">
  <tr><td>
      <input type="hidden" name="pageNum" value="1">
      <input type="hidden" name="boardid" value="${param.boardid}">
      <select name="searchtype"  class="form-control">
      <option value="">선택하세요</option>
      <option value="title">제목</option>
      <option value="writer">작성자</option>
      <option value="content">내용</option>
      <option value="title,writer">제목+작성자</option>
      <option value="title,content">제목+내용</option>
      <option value="writer,content">작성자+내용</option>
      </select>
      <script type="text/javascript">
            searchform.searchtype.value="${param.searchtype}";
      </script></td>
      <td>
         <input type="text" name="searchcontent" value="${param.searchcontent }" class="form-control"></td>
       <td><button class="btn btn-primary">검색</button>
           <button type="button"  class="btn btn-success"
         onclick="location.href='list?boardid=${boardid}'">전체게시물보기</button>  
      </td></tr>
</table>
</form>
<table class="table">
<colgroup>
  <col style="width:10%">
  <col style="width:40%">
  <col style="width:20%">
  <col style="width:20%">
  <col style="width:10%">
  <colgroup>
</colgroup>
    <c:if test="${listcount > 0}"> <!-- 등록된 게시물 건수 -->
     <tr><td colspan="5" class="text-right">글갯수:${listcount}</td></tr>
     <tr><th>번호</th><th>제목</th><th>글쓴이</th><th>날짜</th><th>조회수</th></tr>
  <c:forEach var="board" items="${boardlist}">
      <tr><td>${boardno}</td><c:set var="boardno" value="${boardno - 1}" />
      <td class="text-left">
      <c:if test="${board.grplevel > 0}">
          <c:forEach begin="2" end="${board.grplevel}">&emsp;&emsp;</c:forEach>└</c:if><%-- ㅂ한자 --%>
      <a href="detail?num=${board.num}">${board.title}</a>
      </td>
      <td>${board.writer}</td>
      <td>${board.regdate }</td>
      <td>${board.readcnt}</td></tr>
  </c:forEach>
  
     <tr><td colspan="5" class="text-center">
     <c:if test="${pageNum > 1}">
     <a href="javascript:listpage(${pageNum - 1})" class="btn btn-primary">이전</a></c:if>
     <c:if test="${pageNum <= 1}"><span  class="btn btn-secondary">이전</span></c:if>
     <c:forEach var="a" begin="${startpage }" end="${endpage}">
         <c:if test="${a == pageNum}"><span class="btn btn-success">${a}</span></c:if>
         <c:if test="${a != pageNum}">
           <a href="javascript:listpage(${a})" class="btn btn-secondary">${a}</a></c:if>
     </c:forEach>
     <c:if test="${pageNum < maxpage}">
       <a href="javascript:listpage(${pageNum + 1})" class="btn btn-primary">다음</a></c:if>
     <c:if test="${pageNum >= maxpage}"><span class="btn btn-secondary">다음</span></c:if></td></tr>
   </c:if>
   
   <c:if test="${listcount == 0}">
      <tr><td colspan="5">등록된 게시물이 없습니다.</td></tr>
   </c:if>
   <tr><td colspan="5" class="text-center">
   <a href="write?boardid=${boardid}" class="btn btn-danger">글쓰기</a></td></tr>
</table>
<script type="text/javascript">
	function listpage(page) {
		document.searchform.pageNum.value=page;
		document.searchform.submit(); //submit 버튼 클릭 효과.
	}
</script>
</body></html>