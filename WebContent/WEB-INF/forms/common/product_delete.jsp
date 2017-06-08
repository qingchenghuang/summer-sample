<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>商品资料删除</title>
</head>
<body>
<div>
<h1>是否删除商品信息</h1>
<form action="Product.delete" method="post">
<input type="hidden" name="uid" value="${data.items.UID_}"/>
商品代码:${data.items.Code_}<br/>
商品名称:${data.items.Name_ }<br/>
商品单位:${data.items.Unit_}<br/>      
商品价格:${data.items.Price_}<br/>
商品数量:${data.items.Stock_}<br/>
重量千克:${data.items.Weight_}<br/>
商品描述:${data.items.Description_}<br/>
<button name="submit" value="delete">确认</button>
<a href="Product">返回</a><br/>
${msg}
</form>
</div>
</body>
</html>