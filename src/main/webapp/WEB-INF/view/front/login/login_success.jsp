<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/base/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录成功</title>
<link href="${ctx}/js/umeditor/themes/default/css/umeditor.css" type="text/css" rel="stylesheet">
</head>
<body>
<div class="main-container">
	<div class="main-content">
		<div class="row">
			<div class="col-sm-10 col-sm-offset-1">
				<div class="login-container">
					<div class="center">
						<h1>
							<i class="ace-icon fa fa-leaf green"></i>
							<span class="red">Welcome</span>
							<span class="white" id="id-text2">Enter in</span>
						</h1>
						<h4 class="blue" id="id-company-text">&copy; Company name</h4>
					</div>

					<div class="space-6"></div>

					<div class="position-relative">
					<table>
						<tr>
							<td>姓名:${user.loginName}</td>
							<td>姓名:${user.userName}</td>
							<td>姓名:${user.age}</td>
							<td>姓名:${user.phone}</td>
							<td>姓名:${user.email}</td>
							<%-- <td>姓名:${user.IdNum}</td> --%>
						</tr>
					</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>


</body>
</html>