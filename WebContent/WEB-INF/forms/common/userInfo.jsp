<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>用户资料</title>
<style type="text/css">
.dbgrid{
margin:0 auto;
	
}
</style>
</head>
<body>
	<div style="text-align: center;">
	<h1>用户资料</h1>
	<form  method="post">
	   <input type="text" name="name" value="${name}" />
	   <button>搜索</button>
	</form>
	${dataGrid.html}<hr /><a href="UserInfo.append">添加用户</a><hr />
	${page.html}
	${msg}
	</div>
</body>
</html>