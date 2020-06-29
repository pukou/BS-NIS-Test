<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>


<%
  String path = request.getContextPath();
  String basePath = request.getScheme() + "://"
          + request.getServerName() + ":" + request.getServerPort()
          + path + "/";
%>
<html>
<head>
  <base href="<%=basePath%>">
  <script type="text/javascript"
          src="resources/js/lib/jquery/jquery-1.11.1.js"></script>
  <title>一体化护理v5.6(测试页)</title>
</head>
<body>
<button onclick="getdata()">调用医嘱拆分服务605899</button>
<button onclick="getdataAdvicePlan()">rpc调用医嘱拆分服务</button>
</body>
<script>
  $(function() {

  })

 /*  function getdata(){
   $.ajax({
      data:{
        ksdm:"1033",
        jgid:1,
        ksrq:"2018-01-20",
        jsrq:"2018-01-21"
      },
      url:"mobile/advice/split/from/one/dept",
      type : 'post',
      beforeSend : function(req) {
        req.setRequestHeader("Accept", "application/json");
      },
      success : function(response) {
        alert(response.Msg);
      }
    })
  }*/
  function getdata(){
    $.ajax({
      data:{
        zyh:"605897",
        jgid:1,
        ksrq:"2017-02-19",
        jsrq:"2017-02-20"
      },
      url:"mobile/advice/split/from/one/patient",
      type : 'post',
      beforeSend : function(req) {
        req.setRequestHeader("Accept", "application/json");
      },
      success : function(response) {
        alert(response.Msg);
      }
    })
  }

  function getdataAdvicePlan(){
      $.ajax({
          data:{
              zyh:"605899",
              today:"2017-01-21",
              gslx:"",
              jgid:"1"
          },
          url:"mobile/advice/get/GetPlanList",
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
