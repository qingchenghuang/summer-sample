<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>销售单据</title>
</head>
<body>
<div>
<% java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
java.util.Date date = new java.util.Date();
String currentdate= formatter.format(date);
%>
<h1>销售单据信息</h1>
<form action="BCOrder.appendOrderH" method="post">
单号:<input type="text" name="tbno" value="${tbno}" />
时间:<input type="text" name="date" value="${date}" /><br/>
名称:<input type="text" name="name"  value="${name}" /> 
&nbsp;&nbsp;金额:${total}
<button name="submit" value="append">保存</button>
<hr/>
${dataGrid.html}
${msg}
<hr/>
<a href="BCOrder.appendproductList?tbno=${tbno}&name=${name}&date=${date}">添加商品</a>&nbsp;&nbsp;
<a href="Index">返回</a>
</form>
</div>
</body>
</html>