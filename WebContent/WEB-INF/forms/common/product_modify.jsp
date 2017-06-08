<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>商品资料修改</title>
<!-- <script type="text/javascript" src="../js/jquery-2.1.4.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function(){
        	window.onload=function(){alert("ss")};
        	alert("ss");
        	alert('${data.items.Status_}');
        	 if("上架" =='${data.items.Status_}'){
        		 alert("ssss");
        		 $('#radio1').attr("checked","checked");
        	 }else if("下架" =='${data.items.Status_}'){
        		 alert("sss");
        		 $('#radio2').attr("checked","checked");
        	 }
        });
        
    </script>
     -->
</head>
<body>
<div>
<h1>修改商品信息</h1>
<form action="Product.updateproduct" method="post">
<input type="hidden" name="uid" value="${data.items.UID_}"/>
商品代码:<input type="text" name="code" value="${data.items.Code_}" readonly="readonly" /><br/>
商品名称:<input type="text" name="name" value="${data.items.Name_ }" /><br/>
商品单位:<input type="text" name="unit" value="${data.items.Unit_}" /><br/>
商品价格:<input type="text" name="price" value="${data.items.Price_}"/><br/>
商品数量:<input type="text" name="stock" value="${data.items.Stock_}"/><br/>
重量千克:<input type="text" name="weight" value="${data.items.Weight_}"/><br/>
商品描述:<input type="text" name="description" value="${data.items.Description_}"/><br/>
 <!-- 商品状态:<label><input type="radio" name="status" id="radio1" value="上架" checked="checked" />上架</label><input type="radio" id="radio2" name="status" value="下架"/>下架</label>
<br/> -->
<button name="submit" value="modify">保存</button>
<a href="Product">返回</a>
${msg}
</form>
</div>
</body>
</html>