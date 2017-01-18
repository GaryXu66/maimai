<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/base/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	 <div id="container" name="content" type="text/plain">
       	 这里写你的初始化内容
    </div>
    
    <!-- 配置文件 -->
	
    <script type="text/javascript" src="${ctx}/js/umeditor/umeditor.config.js"></script>
    <!-- 编辑器源码文件 -->
	<script src="${ctx}/js/umeditor/umeditor.min.js"></script>
	<script src="${ctx}/js/umeditor/umeditor.js"></script>
    <!-- 实例化编辑器 -->
    <script type="text/javascript">
        var ue = UM.getEditor('container');
        ue.setHeight(300)
        ue.setWidth(300)
    </script>
</body>
</html>