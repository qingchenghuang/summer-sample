<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>用户资料修改</title>
</head>
<body>
<div>
<h1>修改用户信息</h1>
<form action="UserInfo.updateuser" method="post">
<input type="hidden" name="uid" value="${data.items.UID_}"/>
用户代码:<input type="text" name="code" value="${data.items.Code_}" readonly="readonly" /><br/>
用户名称:<input type="text" name="name" value="${data.items.Name_ }" /><br/>
用户年龄:<input type="text" name="age" value="${data.items.Age_}" /><br/>
用户性别:<input type="text" name="sex" value="${data.items.Sex_ }"/><br/>
<button name="submit" value="modify">保存</button>
<a href="UserInfo">返回</a>
${msg}
</form>
</div>
</body>
</html>