<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>商品表格</title>
<style type="text/css">
.dbgrid{
margin:0 auto;
	
}
</style>
</head>
<body>
<div style="text-align: center;">
<h1>商品表格</h1> 
<form method="post">
	<input type="text" name="productname" value="${productname}" /> 
	<button>搜索</button>
</form> 
${dataGrid.html}<br/><a href="javascript:window.history.back(-1)">返回</a><hr />
${page.html}
${msg}
</div>
</body>
</html>