<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /chartstudy/src/main/webapp/db/barline2.jsp
     최근 7일간 등록된 게시물 건수를 막대,선그래프로 출력하기
--%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>막대/선 그래프로 최근 7일간 등록된 게시글 건수 출력하기</title>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.4/Chart.min.js"></script>
</head>
<body>
<sql:setDataSource var="conn" driver="org.mariadb.jdbc.Driver"
  url="jdbc:mariadb://localhost:3306/springdb"
  user="gduser" password="1234" />
<sql:query var="rs" dataSource="${conn}">
SELECT date_format(regdate,'%Y-%m-%d') date,COUNT(*) cnt FROM board
GROUP BY date_format(regdate,'%Y-%m-%d')
ORDER BY 1 desc  LIMIT 0,7
</sql:query>
<div style="width:75%"><canvas id="canvas"></canvas></div>
<script type="text/javascript">
 let randomColorFactor = function(){
	 return Math.round(Math.random()*255)
 }
 let randomColor = function(opacity) {
	 return "rgba("+ randomColorFactor() + ","
			 + randomColorFactor() + ","
			 + randomColorFactor() + ","
			 +(opacity || '.3') +")"
 }
 let chartData = {
	labels: [<c:forEach items="${rs.rows}" var="m">"${m.date}",</c:forEach>],
	datasets: [
		{type: 'line',
		 borderWidth: 2,
		 borderColor:[<c:forEach items="${rs.rows}" var="m">randomColor(1),</c:forEach>],
		 label: '건수',
		 fill: false,
		 data: [<c:forEach items="${rs.rows}" var="m">"${m.cnt}",</c:forEach>],
		},
		{type: 'bar',
		 label: '건수',
		 backgroundColor: [<c:forEach items="${rs.rows}" var="m">randomColor(1),</c:forEach>],
		 data: [<c:forEach items="${rs.rows}" var="m">"${m.cnt}",</c:forEach>],
		 borderWidth: 2
		}]
  };
window.onload = function() {
	let ctx = document.getElementById('canvas');
	new Chart(ctx, {
		type: 'bar',	data: chartData,
		options: {			
			responsive: true,
			title: {display: true,   text: '게시판 최근 일자별 등록 건수'},
			legend : {display : false },
			scales: {
				xAxes: [{ display : true,
					      scaleLabel:  {
							  display : true,
							  labelString : "게시물 등록일"},
						      stacked : true}],			
				yAxes: [{ display : true,
						  scaleLabel:  {
							  display : true,
							  labelString : "게시물 작성 건수"
						  },
						  stacked : true
					     }]
			 }	}	})   }   
</script></body></html>