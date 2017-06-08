<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>商品资料修改</title>
</head>
<body>
<div><h1>修改商品信息</h1>
<form action="AEOrder.modifyproduct" method="post">
进货单号:<input type="text" name="tbno" value="${data.items.TBNo_}" readonly="readonly"/><br/>
商品名称:<input type="text" name="productname" value="${data.items.Name_}" readonly="readonly"/><br/>
当前数量:<input type="text" name="nownumber" value="${data.items.Inventory_}" readonly="readonly"/><br/>
实际数量:<input type="text" name="number" value="${data.items.Num_}"/><br/>
商品单价:<input type="text" name="price" value="${data.items.Price_}"/><br/>
<input type="hidden" name="date" value="${date}"/>
<button name="submit" value="modify">保存</button>
<a href="AEOrder.appendOrderB?tbno=${data.items.TBNo_}&name=${data.items.Name_}&date=${date}">返回</a><br/>
${msg}
</form>
</div>
</body>
</html>