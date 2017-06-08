<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>盘点单信息</title>
</head>
<body>
<div>
<h1>盘点单信息</h1>
单号:${tbno}&nbsp;
时间:${date}<br/>
名称:${name}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;金额:${total}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<!-- <a href="Order.deleteOrder?tbno=${tbno}">删除</a> --><a href="AEOrder?tbno=${tbno}">删除</a>
<br/>
<hr/>
${dataGrid.html}
${msg}
<a href="AEOrder">返回</a>
${msg}
</div>
</body>
</html>