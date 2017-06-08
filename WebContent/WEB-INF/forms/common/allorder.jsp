<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>进出明细查询</title>
<style type="text/css">
.dbgrid{
margin:0 auto;
	
}
</style>
</head>
<body>
<div style="text-align: center;">
<h1>进出明细查询</h1>
<form action="Inventory.AllOrder" method="post">
    商品名称:<input type="text" name="productname" value="${productname}" /> <br/><br/>
	起始时间:<input type="text" name="start" value="${start }" />&nbsp;&nbsp;结束时间:<input type="text" name="end" value="${end }" />
	<button name="submit" value="search">搜索</button><br/>
	
	
</form>

${dataGrid.html}<br/><a href="Index">返回主菜单</a><hr />
${page.html}
${msg}
</div>
</body>
</html>