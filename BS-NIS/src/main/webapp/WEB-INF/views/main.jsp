<!--
/**
* description:移动护理后台管理系统：
* create by: dragon xinghl@bsoft.com.cn
* create time:2017/11/20 11:19
* since:5.6 update1
*/
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<base href="<%=basePath%>">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta content="always" name="referrer">

	<link rel='icon' href='resources/images/main/cloud48.ico' type='image/x-ico'/>
	<title>移动护理后台管理系统</title>
	<link href="resources/js/lib/easyui1.5/themes/insdep/easyui.css" rel="stylesheet" type="text/css">
	<link href="resources/js/lib/easyui1.5/themes/insdep/easyui_animation.css" rel="stylesheet" type="text/css">
	<link href="resources/js/lib/easyui1.5/themes/insdep/easyui_plus.css" rel="stylesheet" type="text/css">
	<link href="resources/js/lib/easyui1.5/themes/insdep/insdep_theme_default.css" rel="stylesheet" type="text/css">
	<link href="resources/js/lib/easyui1.5/themes/insdep/icon.css" rel="stylesheet" type="text/css">
	<link href="resources/js/lib/easyui1.5/plugins/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">
	<link href="resources/css/main.css" rel="stylesheet" type="text/css">

	<script type="text/javascript" src="resources/js/lib/core/jquery-1.11.1.min.js"></script>
	<script type="text/javascript" src="resources/js/lib/easyui1.5/jquery.easyui-1.5.1.min.js"></script>
	<script type="text/javascript" src="resources/js/lib/easyui1.5/themes/insdep/jquery.insdep-extend.min.js"></script>
	<script>
        function closes() {
            $("#Loading").fadeOut("normal", function () {

                $(this).remove();

            });
        }
        var pc;
        $.parser.onComplete = function () {
            if (pc) {
                clearTimeout(pc);
            }
            pc = setTimeout(closes, 1000);
        }
	</script>
</head>
<body>
<div id='Loading' style="position:absolute;z-index:1000;top:0px;left:0px;width:100%;height:100%;background:#DDDDDB url('');text-align:center;padding-top: 20%;"><h3><image src='resources/images/main/loading/loadding-flower.gif'/><font color="#15428B">  努力加载中···</font></h3></div>
<div id="master-layout">
	<div data-options="region:'north',border:false,bodyCls:'theme-header-layout'">
		<div class="theme-navigate">
			<div class="left">
				<table>
				<tr class="img">
					<td><img class="login_logo" src="resources/images/main/LoginLogo2.gif"></td>
					<td><span class="login_name">移动护理后台管理系统</span></td>
				</tr>
				</table>
			</div>
			<div class="right"></div>
		</div>
	</div>

	<!--开始左侧菜单-->
	<div data-options="region:'west',border:false,bodyCls:'theme-left-layout'" style="width:200px;">
		<!--正常菜单-->
		<div class="theme-left-normal">
			<!--theme-left-switch 如果不需要缩进按钮，删除该对象即可-->
			<%--<div class="left-control-switch theme-left-switch"><i class="fa fa-chevron-left fa-lg"></i></div>--%>
			<!--start class="easyui-layout"-->
			<div class="easyui-layout" data-options="border:false,fit:true">
				<!--start region:'north' e0e9f0-->
				<div data-options="region:'north',border:false" style="height:88px;background: #cfdae6;">
					<!--start theme-left-user-panel-->
					<!--登录用户信息-->
					<div class="theme-left-user-panel">
						<dl>
							<dt>
								<img src="resources/images/portal/portrait86x86.png" width="43" height="43">
							</dt>
							<dd>
								<b class="badge-prompt" id="login-username2">${userName}</b>
								<span id="login-usercode2" style="display:none;">${userCode}</span>
								<span id="login-ygdm2">${ygdm}</span>
							</dd>
						</dl>
					</div>
					<!--end theme-left-user-panel-->
				</div>
				<!--end region:'north'-->

				<!--start region:'center' -->
				<div data-options="region:'center',border:false" id="main-manue">
					<!--菜单栏（开发使用的菜单，不再采用护理管理系统式从服务端获取）-->
					<div class="easyui-accordion" data-options="border:false,fit:true">
						<div title="护理文书调试">
							<ul class="easyui-datalist mune-list" data-options="border:false,fit:true">
								<li url="<%=basePath%>evalution/main/page">护理评估</li>
							</ul>
						</div>
						<div title="定时服务管理">
							<ul class="easyui-datalist mune-list" data-options="border:false,fit:true">
							</ul>
						</div>
					</div>
					<!--end easyui-accordion-->
				</div>
				<!--end region:'center'-->
			</div>
			<!--end class="easyui-layout"-->
		</div>
		<!--最小化菜单-->
		<div class="theme-left-minimal">
			<ul class="easyui-datalist" data-options="border:false,fit:true">
				<li><a class="left-control-switch"><i class="fa fa-chevron-right fa-2x"></i><p>打开</p></a></li>
			</ul>
		</div>
	</div>
	<!--结束左侧菜单-->

	<!--主工作区-->
	<div data-options="region:'center',border:false"  id="control" style="padding:2px;">
		<div id="user-info-more" class="easyui-tabs theme-tab-blue-line theme-tab-body-unborder" data-options="tools:'#tab-tools',fit:true">
		</div>
		<div id="tab-tools">
			<a href="javascript:void(0)" class="easyui-linkbutton"
			   data-options="plain:true,iconCls:'icon-remove'"
			   onclick="removeTab()"></a>
		</div>
	</div>
</div>
<script type="text/javascript" src="resources/js/app/main/main.js"></script>
</body>
</html>