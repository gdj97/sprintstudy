<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /webapp/db/pie.jsp --%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>  
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>파이 그래프로 게시글 작성자의 건수 출력하기</title>
<script type="text/javascript" 
  src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.4/Chart.min.js"></script>
</head>
<body>
<sql:setDataSource var="conn" driver="org.mariadb.jdbc.Driver"
  url="jdbc:mariadb://localhost:3306/springdb"
  user="gduser" password="1234" />
<sql:query var="rs" dataSource="${conn}">
SELECT writer,COUNT(*) cnt FROM board
GROUP BY writer
HAVING COUNT(*) > 1
ORDER BY 2 desc
</sql:query>
<div style="width:75%"><canvas id="canvas"></canvas></div>
<script type="text/javascript">
   let randomColorFactor = function() {
	   return Math.round(Math.random() * 255); //0 ~ 255
   }
   let randomColor = function(opacity) {
	   return "rgba(" + randomColorFactor() + ","
			    + randomColorFactor() + ","
			    + randomColorFactor() + ","
			    + (opacity || '.3') + ")";
	   
   };
   let config = {
	   type : 'pie',
	   data: {
		   datasets : [
			   {
                 data:[<c:forEach items="${rs.rows}" var="m">"${m.cnt}",</c:forEach>],
                 backgroundColor:[<c:forEach items="${rs.rows}" var="m">randomColor(1),</c:forEach>]
            }],
  		    labels:[<c:forEach items="${rs.rows}" var="m">"${m.writer}",</c:forEach>]
	   },
	   options:{
		   responsive:true,
		   legend : {position : 'left'},
		   title: {
					display: true,
					text: '글쓴이 별 게시판 등록 건수',
					position : 'top'
		   },
		   animation: {  animateScale: false, animateRotate: false}
	   }
   }
   window.onload = function() {
	 let ctx = document.getElementById("canvas");
	 new Chart(ctx,config);
   }
</script></body></html>