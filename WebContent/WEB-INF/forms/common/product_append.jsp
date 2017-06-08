<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>商品资料添加</title>
</head>
<body>
<div>
<h1>添加商品信息</h1>
<form action="Product.append" method="post">
商品代码:<input type="text" name="code" />&nbsp;<span style="color:red;">*</span><br/>
商品名称:<input type="text" name="name" />&nbsp;<span style="color:red;">*</span><br/>
商品单位:<input type="text" name="unit" />&nbsp;<span style="color:red;">*</span><br/>
商品价格:<input type="text" name="price" />&nbsp;<span style="color:red;">*</span><br/>
商品数量:<input type="text" name="stock" />&nbsp;<span style="color:red;">*</span><br/>
重量千克:<input type="text" name="weight" />&nbsp;<span style="color:red;">*</span><br/>
商品描述:<input type="text" name="description" /><br/><!-- 
商品状态:<label><input type="radio" name="status" value="上架" checked="checked" />上架</label><input type="radio" name="status" value="下架"/>下架</label>
<br/> -->
<button name="submit" value="append">保存</button>
<a href="Product">返回</a>
${msg}
</form>
</div>
</body>
</html>