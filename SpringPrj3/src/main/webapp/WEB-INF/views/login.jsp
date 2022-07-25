<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
   <title>Insert title here</title>
   <style> 
  input[name="submitt"]{background-color:skyblue; 
  border-color: white; color: white; padding-right: 77px; padding-left: 77px;
                padding-bottom: 5px; padding-top: 5px; text-align: middle;}
  input[name="email"]{padding: 15px;}
  input[name="passwd"]{padding: 15px;}
  div[class="middlee"]{position: absolute;
                       left: 50%; top: 20%;
                       transform: translateX(-50%);}
  h1[name="log"]{color:rgb(26, 188, 156); padding-right: 48px; padding-left: 48px;}
  a {text-decoration: none;}	
   </style>
</head>
<body> 
   <!-- 메뉴 -->
	<%@ include file="/WEB-INF/include/menu.jsp" %>
	<hr>
	<div class="middlee" style="text-align: center;">
   <h1 name="log">로그인</h1> 
   <form action="/login" method="post">
   <input type="text" placeholder="example@greens.trip" name="userId"><br><br>
   <input type="password" placeholder="****" name="userPw"><br><br>  
   <p style="color: red;">${requestScope.loginFailMsg}</p>
   
   <input type="submit" value="로그인" name="submitt" style="width:300px; height: 50px; margin-bottom: 20px;"> 
   
</form>
<a href="/submit">회원가입</a>  
<a href="http://localhost:9090/findpassword">비밀번호 찾기</a>
<div style="padding-bottom: 20px; margin: 30px 0px 20px; "> 
   <a href="/oauth2/authorization/google"><img src="/img/google.png" alt="" style="width:300px; height:50px"></a>
</div>
<div style="padding-bottom: 10px; margin: 10px auto;">
   <img src="/img/naver.png" alt="" style="width:300px;height:55px" hidden>
</div>
   </div>

</body></html>