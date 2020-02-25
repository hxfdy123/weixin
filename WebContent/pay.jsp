<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="jquery.min.js"></script>
<title>支付页面测试</title>
</head>
<body>
商品信息xxxx<br/>
<img src="loadPayImage"/>

<script type="text/javascript">
//模拟定时监控支付状态，成功既返回主页
     function orderStatus(){
    	 
    	 $.post("PayState",{},function(data){//这里没有获取参数{}
    		 if(data==1){
    			 window.location.href = "JSP.jsp";
    		 }
    	 })
    	 
     }

     setInterval("orderStatus()",3000);
     
</script>

</body>
</html>