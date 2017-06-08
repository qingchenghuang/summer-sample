<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>进货单据修改</title>
</head>
<body>
<div>
<h1>修改进货单据信息</h1>
<form action="Order.appendOrderH" method="post">
<input type="hidden" name="tbtype" value="ab"/> 
单号:<!-- <input type="text" name="tbno" value="${data.items.TBNo_}"/><br/> -->
  <input type="text" name="tbno" value="${tbno}" readonly="readonly" />
时间:<input type="text" name="date" value="${date}"  /><br/>
名称:<!-- <input type="text" name="name" value="${data.items.UserName_}" /> -->
<input type="text" name="name"  value="${name}"   /> 
&nbsp;&nbsp;金额:${total}&nbsp;&nbsp;&nbsp;<button name="modifyH" value="append">保存</button>

<hr/>

${dataGrid.html}
${msg}
<hr/>
<!-- <button name="submit" value="append">添加商品</button> -->
<a href="Order.appendproductList?tbno=${tbno}&name=${name}&date=${date}">添加商品</a>
<a href="javascript:window.history.back(-1)">返回</a>
</form>
</div>
</body>
</html>