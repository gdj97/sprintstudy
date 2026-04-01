<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- /WEB-INF/view/search.jsp --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<!DOCTYPE html><html><head><meta charset="UTF-8">
<title>${title}찾기</title>
</head>
<body><table>
 <c:if test="${title=='아이디'}">
  <tr><th>${title} : </th><td>${result}</td></tr>
  <tr><td colspan="2">
        <input type="button" value="아이디전송" onclick="sendclose()"></td></tr>
  </c:if>        
  <c:if test="${title!='아이디'}">
  <tr><td colspan="2">
      비밀번호 초기화를 이메일로 전송했습니다.</td></tr>
  </c:if>      
</table>
<script type="text/javascript">
   function sendclose() {
	   // opener : login 화면
	   opener.document.loginform.userid.value='${result}';
	   self.close(); //현재페이지 닫기
   }
</script>
</body></html>