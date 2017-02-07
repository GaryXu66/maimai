<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/base/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Reg page</title>
</head>
<body>
	<div class="widget-main">
		<h4 class="header green lighter bigger">
			用户注册
		</h4>
		<p>填写信息:</p>

		<form action="${ctx}/user/reg" method="post" enctype="multipart/form-data">
			<fieldset>
				<label class="block clearfix">
					<span class="block input-icon input-icon-right">
						<input type="email" class="form-control" placeholder="邮箱" name="email" />
						<i class="ace-icon fa fa-envelope"></i>
					</span>
				</label>
				<br />
				<label class="block clearfix">
					<span class="block input-icon input-icon-right">
						<input type="text" class="form-control" placeholder="用户名" name="userName" />
						<i class="ace-icon fa fa-user"></i>
					</span>
				</label>
				<br />
				<label class="block clearfix">
					<span class="block input-icon input-icon-right">
						<input type="password" class="form-control" placeholder="密码" name="passA" />
						<i class="ace-icon fa fa-lock"></i>
					</span>
				</label>
				<br />
				<label class="block clearfix">
					<span class="block input-icon input-icon-right">
						<input type="password" class="form-control" placeholder="确认密码" name="passB" />
						<i class="ace-icon fa fa-retweet"></i>
					</span>
				</label>
				<!-- <br />
				<img alt="图片预览" id="img" width="200px;" height="120px;"/>
				<br>
				<label class="block clearfix">
					<span class="block input-icon input-icon-right">
						<input type="file" name="pic" id="userPic" />
					</span>
				</label> -->
				<br />
				<label class="block">
					<input type="checkbox" class="ace" />
					<span class="lbl">
						接受 <a href="#">用户协议</a>
					</span>
				</label>
				<br />
				<div class="clearfix">
					<button type="reset" class="width-30 pull-left btn btn-sm">
						<span class="bigger-110">重置</span>
					</button>

					<button type="submit" class="width-65 pull-right btn btn-sm btn-success">
						<span class="bigger-110">注册</span>
					</button>
				</div>
			</fieldset>
		</form>
	</div>

	<script src="${ctx}/js/jquery.min.js"></script>
	<script src="${ctx}/js/ajaxfileupload.js"></script>
	<script type="text/javascript">
	function uploadPic(elementId){
		$.ajaxFileUpload
	    (
	        {
	            url: '${ctx}/uploadfile/pic', //用于文件上传的服务器端请求地址
	            type: 'POST',
	            secureuri: false, //一般设置为false
	            fileElementId: elementId, //文件上传空间的id属性  <input type="file" id="file" name="file" />
	            dataType: 'text', //返回值类型 一般设置为json
	            success: function (data)  //服务器成功响应处理函数
	            {
	            	var newstr = decodeURIComponent(data.replace(/\+/g, '%20')) ;/**进行解码，并解决因空格产生的'+'原因**/
					var json = JSON.parse(newstr) ;
	                if(json.error == 1){
	                  layer.msg(json.message);
	              	  return;
	                }
	                if(json.message=="-1"){
	                	layer.msg("图片格式不支持");
 	                	return;
	                }

                	$("#img").attr("src",json.url);
	            }
	        }
	    );
	}

	$(function() {
		/* $("#userPic").change(function(result) {
			uploadPic("userPic");
		}); */
	});
	</script>
	</div>
</body>
</html>