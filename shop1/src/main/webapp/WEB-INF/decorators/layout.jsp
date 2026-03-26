<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="path" value="${pageContext.request.contextPath }" />    
<%-- /webapp/WEB-INF/decorators/layout.jsp
  <sitemesh:write property="title" />
  <sitemesh:write property="head" />
  <sitemesh:write property="body" />
--%>    
<!DOCTYPE html>
<html lang="en">
<head>
  <title><sitemesh:write property="title" /></title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
  <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
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
      <h2>About Me</h2>
      <h5>Photo of me:</h5>
      <div class="fakeimg">Fake Image</div>
      <p>Some text about me in culpa qui officia deserunt mollit anim..</p>
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
</div>

</body>
</html>
