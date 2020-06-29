<!--
* description:重新设计护理评估单：
* 1.支持无限级项目
* 2.支持自伸缩
* create by: dragon xinghl@bsoft.com.cn
* create time:2017/11/20 11:19
* since:5.6 update1
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
<head>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>护理评估</title>
    <link rel="stylesheet" type="text/css" href="resources/js/lib/easyui1.5/themes/icon.css">
    <link rel="stylesheet" type="text/css"
          href="resources/js/lib/easyui1.4/themes/bootstrap/tooltip.css">
    <link rel="stylesheet" type="text/css"
          href="resources/css/jquery.zsign.css">
    <link rel="stylesheet" type="text/css" class="library" href="resources/css/evalution/evaluate.css"/>
    <script type="text/javascript" class="library" src="resources/js/lib/core/jquery-1.11.1.js"></script>
    <script type="text/javascript" src="resources/js/lib/easyui1.5/easyloader.js"></script>
    <script type="text/javascript" class="library" src="resources/js/app/evalution/evaluate.js"></script>
    <script type="text/javascript" src="resources/js/app/utils.js"></script>
    <script type="text/javascript" src="resources/js/app/jquery.zsign.js"></script>
    <script type="text/javascript" src="resources/js/lib/core/jquery-printarea.js"></script>
    <%--<script type="text/javascript" src="http://localhost:8001/CLodopfuncs.js? priority=1"></script>--%>
</head>
<body oncontextmenu=self.event.returnValue=false>
<%--<body>--%>
<%--<div id='Loading'>
    <image src='resources/images/main/loading/loadding-z.gif'/>
    <font color="#2bd4cd" size="4">页面正在加载中···</font>
</div>--%>
<%----%>
<%--<div id="cc" class="easyui-layout" data-options="fit:true" style='background-image: url("resources/images/evalution/bg_3.jpg")'>
    <div data-options="region:'center',title:'',border:false" style="background-color: #7f7f7f;padding: 10px;">--%>

       <%-- <button onclick="newEvalutionTest()">新增评估</button>
        <button onclick="extendComboboxParser1()">测试正则</button>
        <button onclick="saveEvalutionRecordTest()">保存评估</button>
        <label>记录序号：</label><input id="jlxh" style="width: 60px;"/>
        <button onclick="openEvalutionRecordTest()">打开评估</button>
        <button onclick="setTextBoxDefaultValueTest()">一键设置默认值</button>
        <button onclick="getRelations()">关联数据</button>
        <button onclick="print()">打印</button>
        <button>回调方法</button>--%>

        <div id="evaluate-main" style='background: url("resources/images/evalution/bg_2.jpg") top center no-repeat;background-size:cover;'>
            <div class="header">
                <div id="title"><span id="title_span"></span></div>
                <div id="title2"><span id="two_title_span"></span></div>
            </div>
            <div class="content" id="content">

            </div>
            <div class="footer" style="display: none">
                <div id="footer" style="margin-bottom: 10px;margin-top: 10px;font-size: 13px;font-family: 宋体;">
                    <hr/>
                    <span><label>评估护士：</label><label id="write-name"></label></span>
                    <span><label style="margin-left: 10px;">评估时间：</label><label id="write-time"></label></span>
                    <span><label style="margin-left: 80px;">护士签名：</label><input id="nurse-sign" style="width: 120px;" readonly="readonly" onclick="sign_test(this)"></input></span>
                    <span><label style="margin-left: 10px;">护士长签名：</label><input id="pnurse-sign" style="width: 120px;" readonly="readonly" onclick="sign_test(this)"></input></span>
                </div>
            </div>
        </div>
        <!--打印区域-->
        <div id="printSpace" style="display: none;letter-spacing:6px;line-height:30px;">
            <div class="print-header" style="">
                <div style='font-family: "Microsoft YaHei", "微软雅黑";font-size: 18px ;width: 98%;text-align: center;padding: 3px;font-weight: bold;letter-spacing:4px;'><span id="print-title1"></span></div>
                <div style='font-family: "Microsoft YaHei", "微软雅黑";font-size: 20px ;width: 98%;text-align: center;padding: 3px;font-weight: bold;letter-spacing:4px;'><span id="print-title2"></span></div>
            </div>
            <div class="print-content" style="font-size: 15px;line-height:21px;">

            </div>
            <div class="print-footer" style="font-size: 15px;">
                <hr/>
                <span><label>评估护士：</label><label id="write-print-name"></label></span>
                <span><label style="margin-left: 10px;">评估时间：</label><label id="write-print-time"></label></span>
                <span><label style="margin-left: 80px;">护士签名：</label><label id="nurse-print-sign" style="width: 120px;" readonly="readonly"></label></span>
                <span><label style="margin-left: 10px;">护士长签名：</label><label id="pnurse-print-sign" style="width: 120px;" readonly="readonly"></label></span>
            </div>
        </div>
  <%--  </div>
</div>--%>
<!--右击菜单-->
<div id="menu" class="easyui-menu" style="width:150px;" data-options="onClick:menuHandler">
    <div id="m-blyy">病历引用</div>
    <div id="m-ywgl">业务关联</div>
    <div id="m-bdqm">表单签名</div>
</div>
</body>

</html>