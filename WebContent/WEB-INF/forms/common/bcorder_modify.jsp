<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>销售单据修改</title>
</head>
<body>
<div>
<h1>修改销售单据信息</h1>
<form action="Order.appendproductList" method="post">
<input type="hidden" name="tbtype" value="ab"/> 
单号:<!-- <input type="text" name="tbno" value="${data.items.TBNo_}"/><br/> -->
  <input type="text" name="tbno" value="${tbno}" readonly="readonly" />
时间:<input type="text" name="date" value="${date}" readonly="readonly" /><br/>
名称:<!-- <input type="text" name="name" value="${data.items.UserName_}" /> -->
<input type="text" name="name"  value="${name}"   readonly="readonly" /> 
&nbsp;&nbsp;金额:${total}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="BCOrder.appendproductList?tbno=${tbno}&name=${name}&date=${date}">添加商品</a>
<!-- <button name="submit" value="append">添加商品</button> -->
<hr/>

${dataGrid.html}
${msg}
<hr/>

<a href="javascript:window.history.back(-1)">返回</a>
</form>
</div>
</body>
</html>