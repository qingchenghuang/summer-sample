<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>用户资料添加</title>
</head>
<body>
<div>
<h1>添加用户信息</h1>
<form action="UserInfo.append" method="post">
用户代码:<input type="text" name="code" /><br/>
用户名称:<input type="text" name="name" /><br/>
用户年龄:<input type="text" name="age" /><br/>
用户性别:<input type="text" name="sex" /><br/>
<button name="submit" value="append">保存</button>
<a href="UserInfo">返回</a>
${msg}
</form>
</div>
</body>
</html>