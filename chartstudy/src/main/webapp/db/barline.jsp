<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /webapp/db/barline.jsp --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<!DOCTYPE html>
<html> 
<head>
<meta charset="UTF-8">
<title>막대/선 그래프로 게시글 작성자의 건수 출력하기</title>
<script type="text/javascript" 
src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.4/Chart.min.js"></script>
</head>
<body>
<sql:setDataSource var="conn" driver="org.mariadb.jdbc.Driver"
  url="jdbc:mariadb://localhost:3306/springdb"
  user="gduser" password="1234" />
<%-- board table에서 글작성자별 등록 건수 조회. 2건이상, 건수의 내림차순 정렬 조회 --%>
<sql:query var="rs" dataSource="${conn}">
SELECT writer,COUNT(*) cnt FROM board
GROUP BY writer
HAVING COUNT(*) > 1
ORDER BY 2 desc
</sql:query>
<div style="width:75%"><canvas id="canvas"></canvas></div>
<script type="text/javascript">
 let randomColorFactor = function(){  //0 ~ 255 사이의 임의의 수 리턴
	 return Math.round(Math.random()*255)
 }
 let randomColor = function(opacity) {  //rgba(red,green,blue,투명도)
	 return "rgba("+ randomColorFactor() + ","
			 + randomColorFactor() + ","
			 + randomColorFactor() + ","
			 +(opacity || '.3') +")"
 }
 let chartData = {
	labels:[<c:forEach items="${rs.rows}" var="m">"${m.writer}",</c:forEach>],  //작성자이름을 x축에 표시
	datasets: [
		{type: 'line',
		 borderWidth: 2,
		 borderColor:[<c:forEach items="${rs.rows}" var="m">randomColor(1),</c:forEach>], //라인색상
		 label: '건수',
		 fill: false,
		 data: [<c:forEach items="${rs.rows}" var="m">"${m.cnt}",</c:forEach>],
		},
		{type: 'bar',
		 label: '건수',
		 backgroundColor: [<c:forEach items="${rs.rows}" var="m">randomColor(),</c:forEach>],
		 data: [<c:forEach items="${rs.rows}" var="m">"${m.cnt}",</c:forEach>],
		 borderWidth: 5
		}]
   };
window.onload = function() {
	let ctx = document.getElementById('canvas').getContext('2d');
	new Chart(ctx, {
		type: 'bar',	data: chartData,
		options: {			
			responsive: true,
			title: {display: true,   text: '게시판 등록 건수'},
			legend : {display : false },
			scales: {
				xAxes: [{ display : true,   //x축옵션
					      scaleLabel:  {
							  display : true,  labelString : "게시물 작성자"
						  }
				      }],			
				yAxes: [{ display : true,  //y축옵션
						  scaleLabel:  {
							  display : true,  labelString : "게시물 작성 건수"
						  },
						  stacked : true
					     }]
			 }	}	})   }   
</script></body></html>