<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="path" value="${pageContext.request.contextPath }" />    
<%-- /webapp/WEB-INF/decorators/layout.jsp
  <sitemesh:write property="title" />
  <sitemesh:write property="head" />
  <sitemesh:write property="body" />
  
  ======================================
  2026-04-07 과제
     1. http://gudi.kr/ 의 로고를 layout.jsp에 추가하기
        /shop1/ajax/goodeelogo 요청
        id="goodeelogo" 인 태그에 출력하기
     2. 게시물등록, 게시물수정,답변등록,이메일화면 이미지업로드가 되도록 수정하기   
--%>    
<!DOCTYPE html>
<html lang="en">
<head>
  <title><sitemesh:write property="title" /></title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
<%--  
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
  <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
  --%>
  
<%--
  summernote 설정 
    summernote : WYSIWYG(What You See Is You Get) 에디터 (CKEditor)
                 웹에서 서식 설정할 수 있는 에디터
                 JQuery 기반, Bootstrap과 호환됨
    설정은
      JQuery > Bootstrap > summernote 관련 js순으로 설정해야함             
 --%>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.css" rel="stylesheet">

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.js"></script>  
  <style>
  .fakeimg {
    height: 200px;
    background: #aaa;
  }
  </style>
  <sitemesh:write property="head" />  
</head>
<body>

<div class="jumbotron text-center" style="margin-bottom:0">
  <h1>클라우드 활용 자바 스프링 개발 부트캠프</h1>
  <p>구디아카데미 GDJ97</p> 
</div>

<nav class="navbar navbar-expand-sm bg-dark navbar-dark">
  <a class="navbar-brand" href="${path}">Home</a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
    <span class="navbar-toggler-icon"></span>
  </button>
  <div class="collapse navbar-collapse d-flex justify-content-between" id="collapsibleNavbar">
    <ul class="navbar-nav">
      <li class="nav-item">
        <a class="nav-link" href="${path}/user/mypage?userid=${loginUser.userid}">회원관리</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="${path}/item/list">상품관리</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="${path}/board/list?boardid=1">공지사항</a>
      </li>    
      <li class="nav-item">
        <a class="nav-link" href="${path}/board/list?boardid=2">자유게시판</a>
      </li>    
      <li class="nav-item">
        <a class="nav-link" href="${path}/board/list?boardid=3">QNA</a>
      </li>    
    </ul>
        
    <ul class="navbar-nav">
  	  <c:if test="${empty sessionScope.loginUser}">
        <li class="nav-item">
	 		<a class="nav-link" href="${path}/user/login">로그인</a>
 		</li>
        <li class="nav-item">
	 		<a class="nav-link" href="${path}/user/join">회원가입</a>
 		</li>
	  </c:if>   
	  <c:if test="${!empty sessionScope.loginUser}">
        <li class="nav-item">
			<span class="nav-link">${sessionScope.loginUser.username}님이 로그인 하셨습니다.</span>
        </li>
        <li class="nav-item">
	 		<a href="${path}/user/logout" class="nav-link">로그아웃</a>
        </li>
	  </c:if>   
    </ul>
  </div>  
</nav>

<div class="container-fluid px-5" style="margin-top:30px">
  <div class="row">
    <div class="col-sm-4">
    <%-- 로고 --%>
    <div id="goodeelogo"></div>
    <hr>
    
      <ul class="nav nav-pills flex-column">
        <li class="nav-item">
			<c:if test="${empty sessionScope.loginUser}">
	 		<a class="nav-link" href="${path}/user/login">로그인</a>
	 		<a class="nav-link" href="${path}/user/join">회원가입</a>
			</c:if>   
			<c:if test="${!empty sessionScope.loginUser}">
			${sessionScope.loginUser.username}님이 로그인 하셨습니다.&nbsp;&nbsp;
	 		<a href="${path}/user/logout">로그아웃</a>
			</c:if>   
        </li>
      </ul>
      <hr>
      <h5>수출입 은행 환율정보</h5>
      <div style="width:100%">
      	<div id="exchange" style="width:70%; margin:6px;"></div>
      </div>

      <h3>Some Links</h3>
      <p>Lorem ipsum dolor sit ame.</p>
      
      <hr class="d-sm-none">
    </div>
    <div class="col-sm-8">
    <sitemesh:write property="body" />
    </div>
  </div>
</div>

<div class="jumbotron text-center" style="margin-bottom:0">
  <p>Footer</p>
    <hr>
    <div>
    <span id="si">
     <select name="si" onchange="getText('si')">
	     <option value="">시도를 선택하세요</option>
     </select>
    </span>
    <span id="gu">
	<select name="gu" onchange="getText('gu')">
		<option value="">구군을 선택하세요</option>
	</select>
    </span>
    <span id="dong">
	   <select name="dong">
  		  <option value="">동리를 선택하세요</option>
	   </select>
    </span></div>   
</div>
<script>
$(function(){  //화면 준비되면
	goodeelogo();
	getSido(); //호이스팅기능: 선언보다 먼저 호출되는 것이 가능
//	exchangeString();
	exchangeJson();
})
function getSido() { 
	$.ajax({
		url : "${path}/ajax/select1",
		success : function(data) {	//서버에서 문자열로 데이터 전달. 	
//			   console.log(data) //[서울특별시,경기도,경상북도,....] 
			   let arr = data.substring(data.indexOf('[')+1, data.indexOf(']')).split(",");
			   $.each(arr,function(i,item){ //i:인덱스, item:내용(서울특별시,경기도,...)
				   $("select[name=si]").append(function(){
					   return "<option>"+item+"</option>"
				   })
			   })
		}
	})
}
function getText(name) {
	let city = $("select[name='si']").val();  // 시도 선택값
	let gu = $("select[name='gu']").val();    // 구군 선택값
	let disname;    //option 태그의 위치값. select 태그의 이름
	let toptext="구군을 선택하세요";
	let params = "";
	if (name == "si") { //시도를 선택한 경우
		params = "si=" + city.trim();  // 예:서울특별시, 경기도 ..
		disname = "gu";                // 변경할 select 태그의 name 속성값  
	} else if (name == "gu") { //구군를 선택한 경우
		params = "si=" + city.trim()+"&gu="+gu.trim();  //예 : si=서울특별시&gu=금천구,...
		disname = "dong";              // 변경할 select 태그의 name 속성값
		toptext="동리를 선택하세요";		
	} else { 
		return ;
	}
	$.ajax({
	  url : "${path}/ajax/select",   // 결과값 List<String> 형태로 서버에서 리턴
 	  type : "POST",    
	  data : params,  	
	  // 서버에서 전달될 List 객체를 배열로 변형 : pom.xml 에서 com.fasterxml.jackson.core 설정 필요
	  success : function(arr) { //arr : 서버에서 List<String> 형태로 리턴하면 클라이언트는 배열형식의 객체로 데이터 수신.
		  $("select[name="+disname+"] option").remove(); //기존의 option 태그들을 제거
		  $("select[name="+disname+"]").append(function(){
			  return "<option value=''>"+toptext+"</option>" //select 태그의 첫번째 option을 설정
		  })
		  $.each(arr,function(i,item) {
			  $("select[name="+disname+"]").append(function(){
				  return "<option>"+item+"</option>"
			  })
		  })
	  }
   })				
}
function exchangeString() {
	   $.ajax("${path}/ajax/exchangeString",{
		   success : function(data) {
			   console.log(data)
			   $("#exchange").html(data)
		   },
		   error : function(e) {
			   alert("환율 조회시 서버 오류 발생 :" + e.status)
		   }
	   })	
}
function exchangeJson() {
	   $.ajax("${path}/ajax/exchangeJson",{
		 //json : 자바스크립트객체로 받음. 서버에서 Map 객체로 전달, 클라이언트에서는 JSON객체받음
		 //     => com.fasterxml.jackson.core 설정이 필요
		   success : function(json) {  
//			   console.log(json)
			   let html = "<h4 class='text-right'>"+json.exdate+"</h4>"
			   html += "<table class='table table-sm table-bordered'>"
			   html += "<tr><th>통화</th><th>기준율</th>";
			   html += "<th class='text-nowrap'>받을실때</th><th class='text-nowrap'>보내실때</th></tr>";
			   //json.trlist : 서버 List<List<String>> 자료형 => 배열을 배열로 받음
			   $.each(json.trlist,function(i,tds){ //tds : 배열객체
				   html += "<tr><td>"+tds[0]+"<br>"+tds[1]+"</td><td>"+tds[4]+"</td>"
					     + "<td>"+tds[2]+"</td><td>"+tds[3]+"</td></tr>"
			   })
			   html += "</table>"
			   $("#exchange").html(html)
		   },
		   error : function(e) {
			   alert("환율 조회시 서버 오류 발생 :" + e.status)
		   }
	   })
}
</script>
</body></html>