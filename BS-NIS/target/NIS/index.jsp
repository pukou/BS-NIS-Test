<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>


<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    String host = request.getHeader("host");
%>
<html>
<head>
    <base href="<%=basePath%>">
    <script type="text/javascript"
            src="resources/js/lib/jquery/jquery-1.11.1.js"></script>
    <script type="text/javascript"
            src="resources/js/lib/qrcode/qrcode.min.js"></script>

    <title>一体化护理v5.6</title>
</head>
<!--<body style="background:url(resources/images/welcome/welcome.jpg)">-->
<body>

<center>
    <label>点击下载: </label>
    <a id="qrlink" href="#">BSiENR.apk</a>
    <br>
    <hr>
    <div id="qrcode" style="width:100px; height:100px; margin-top:15px;"></div>

    <br>
    <label id="qrinfo">信息</label>
    <br>
    <label id="bzxx">升级描述</label>
</center>

</body>
<script>
    $(function() {
        makeQRCode();
    })

    function makeQRCode() {
        $.ajax({
            url:"NIS-UPDATE/nis/description.json",
            type : 'get',
            cache: false,
            beforeSend : function(req) {
                req.setRequestHeader("Accept", "application/json");
            },
            success : function(res) {
                //获取APK的URL地址，并设置下载链接
                var apkUrl = "<%=basePath%>" + "NIS-UPDATE/nis/" + res.FileName;
                $('#qrlink').attr("href", apkUrl);
                $('#qrlink').html("<b>" + res.FileName + "</b>");

                //生成二维条码
                var qrcode = new QRCode(document.getElementById("qrcode"), {
                    width : 100,
                    height : 100
                });

                qrcode.makeCode(apkUrl);

                // 设置版本信息及更新说明
                $('#qrinfo').text("VerCode: " + res.VersionCode + "  VerName: " + res.VersionName);
                $('#bzxx').text(res.Description);

                //alert("" + $("#qrlink").attr("href"));
            }
    })
    }

    function getdata(){
        $.ajax({
            url:"mobile/user/get/agencys",
            type : 'get',
            beforeSend : function(req) {
                req.setRequestHeader("Accept", "application/json");
            },
            success : function(response) {
                alert(response.Msg);
            }
        })
    }
</script>

</html>
