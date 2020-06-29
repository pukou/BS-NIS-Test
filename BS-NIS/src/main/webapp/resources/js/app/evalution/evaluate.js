/*
* description:重新设计护理评估单：
* 1.支持无限级项目
* 2.支持自伸缩
* create by: dragon xinghl@bsoft.com.cn
* create time:2017/11/20 11:19
* since:5.6 update1
*
* TODO :1、表格类型绑定editor
* TODO :2、表格类型的基础数据、下拉数据
* TODO :3、表格类型的数据新增
* TODO :4、表格类型的数据删除
*/

$(function(){
    easyloader.base = "resources/js/lib/easyui1.5/";
    easyloader.locale = "zh_CN";
    using([ 'tooltip', 'form', 'textbox', 'combobox', 'linkbutton', 'datebox', 'pagination', 'datetimespinner',
        'numberspinner', 'datagrid', 'validatebox', 'messager', 'datetimebox', 'tabs' ], function() {

        init();
    });
})
/****************************全局参数控制***********************************/
var go_evalutionStyle = null;
// 在checkbox非checked状态下，是否清除子项目值
var go_clearChildValue = true;
// 是否支持互斥操作
var go_isSupportHc = true;
// 是否支持动画隐藏
var go_isSupportCartoon = true;
// 动画速度
var go_cartoonSpeed = 600;
// 分类标签默认折叠
var go_typeLabelExpand = true;
// 是否支持图章
var go_isSupportPictrue = true;
// 是否是PB调用
var go_isPowerBuilderInvake = false;
// 是否控制非本人评估单不可修改
var go_isControllOwener = true;

/*****************************缓存数据*************************************/
// 表格式的项目组id：项目的祖父节点项目序号，保存时，通过此id来找datagrid,同时将此id保存在明细表的父项目id和名称
var go_groupids = [];
// 分类id与组id的关系 分类id:[2,3]
var go_groupCompares = [];
// 基本信息对照关系 [id,xmkz]
var go_baseInfoCompares = [];
// 当前病人的基本信息
var go_baseInfos = [];
// 数据关联功能思路：
// 记录当前评估单样式，数据关联项目ID,及扩展信息
// 服务端返回信息格式：{XMXH:1,XMNR:'222',KJLX:'3'}
// 服务端可根据业务类别分别获取数据，传数组
// 提供服务端参数格式：[{YWLB:2,YSXH,YSLX,LXMX[{XMLX:1},{XMLX:2}]},{YWLB:5,LXMX[{XMLX:1},{XMLX:2}]}]
//     说明：YWLB:项目扩展中 |前的数据  XMLX : |后的数据

var go_zyh = 0;
var go_jgid = 1;
var go_jlxh = 0;
var go_jgmc = "创业数字医院";
var go_txsj = null;
var go_brbq = null;
var go_ysxh = null;
var go_version = null;
var go_userId = null;
var go_userName = null;
var go_record = {}; // 修改状态下，赋值此项目

var go_rightClickProjects = [];  // 需要右击产生菜单的项目,项目类型 = {数据关联项目}
var go_rightClickInputId = 0;    // 记录右击的input的id

/*******************************全局记录状态控制*****************************/
// 1：可修改状态 2：不可修改状态
// 可修改状态：新增 ，护士已签名是本人操作
// 不可修改状态：护士长签名，护士已签名不是本人操作 ，签名控件不控制状态
var go_status = 1;
// 监控值是否变化
var go_changed = false;
// 监控自动填充数据时段，该时段内所有触发的onChange事件都不予以触发go_changed值变化
var go_isAutoSetValue = false;
// 操作的回调方法
var go_callBackFunction = "";

/**************************************************************************/
function init(){
    /*$(".fire-checkbox-display-event").bind("click",function(o){
        var id = $(o).attr("id");
        alert(id)
    });*/
    if (go_isSupportPictrue){
        signobj =$(".header").zSign({ img: 'resources/images/evalution/checked.png',
            width:120,
            height:120,
            offset:8});
    }

    // 动态加载云打印控件
    var oscript = document.createElement("script");
    oscript.src ="http://localhost:8001/CLodopfuncs.js?priority=1";
    var head = document.head || document.getElementsByTagName("head")[0] || document.documentElement;
    head.insertBefore( oscript,head.firstChild );
}

function fireEvent(o){
    var id = $(o).attr("id");
    fireRodioBoxDisplayEvent(o);
    fireCheckBoxDisplayEvent(o);
    valueChanged(0,0);
}

/**
 * 触发鼠标事件
 * @param e
 */
function fireMouseDownEvent(e){
    alert(e);
}

/**
 * 触发显示事件
 * @param o
 */
function fireCheckBoxDisplayEvent(o){
    var id = $(o).attr("id");
    var groupName = $(o).attr("name");
    // 循环处理所有的
    var docs = $("input[name='"+groupName+"']").each(function(){
        var thisId = $(this).attr("id");
        var thisZkbz = $(this).attr("xjzkbz");
        if (thisZkbz == 0){
            // 选中状态
            if ($(this).is(":checked")) {
                // span上级项目是当前的，就显示
                $("span[parentid="+thisId+"]").css("display","inline");
                $("div[parentid="+thisId+"]").css("display","block");
            }else{
                $("span[parentid="+thisId+"]").css("display","none");
                $("div[parentid="+thisId+"]").css("display","none");

                // 隐藏的同时，需要将此项目的子项目所有的输入值都设置为null;
                // 利用jQuery选择器，实现此功能：当前parentid下所有的输入类型控件都设置空值
                if (go_clearChildValue){
                    setChildrenContrlNull(thisId);
                }
            }
        }
    });
}

function setChildrenContrlNull(parentId){
    $("span[parentid="+parentId+"] input[kjlx='textbox']").each(function(){
        $(this).textbox("reset");
    })

    $("span[parentid="+parentId+"] input[kjlx='mul-textbox']").each(function(){
        $(this).textbox("reset");
    })

    $("span[parentid="+parentId+"] input[kjlx='numberbox']").each(function(){
        $(this).numberbox("reset");
    })

    $("span[parentid="+parentId+"] input[kjlx='datebox']").each(function(){
        $(this).datebox("reset");
    })

    $("span[parentid="+parentId+"] input[kjlx='datetimebox']").each(function(){
        $(this).datetimebox("reset");
    })

    $("span[parentid="+parentId+"] input[kjlx='radiobox']").each(function(){
        $(this).attr("checked",false);
    })

    $("span[parentid="+parentId+"] input[kjlx='checkbox']").each(function(){
        $(this).attr("checked",false);
    })

    $("span[parentid="+parentId+"] input[kjlx='combobox']").each(function(){
        $(this).combobox("reset");
    })
}

/**
 * 处理单选按钮
 * @param o
 */
function fireRodioBoxDisplayEvent(o){
    var id = $(o).attr("id");
    var dxbz = $(o).attr("dxbz");
    var groupName = $(o).attr("name");
    // 在缓存中查找id ,并找到parent的
    // 若是parent是单选且下级互斥标志为1的，隐藏其他项目，并将下级的所有的项目值都清空
    // 只有是单选的按钮，才处理互斥
    if(dxbz == 1){
        var docs = $("input[name='"+groupName+"']").each(function(){
            var thisId = $(this).attr("id");
            var hcbz = $(this).attr("hcbz");
            // 将其他同组项目checked设置成false
            if ($(o).is(":checked")) {
                if (thisId != id){
                    $(this).attr("checked",false);
                    if (go_isSupportHc && hcbz == 1){
                        if (go_isSupportCartoon){
                            // 会将display的属性改成inline-block ，导致布局乱了
                            //$("span[xmid='"+thisId+"']").hide(go_cartoonSpeed).css("display","inline");
                            $("span[xmid='"+thisId+"']").css("display","none")
                        }else{
                            //$("span[xmid='"+thisId+"']").hide();
                            $("span[xmid='"+thisId+"']").css("display","none")
                        }

                    }

                }
            }else{ // 将其他同组项目checked设置成true
                if (thisId != id){
                    $(this).attr("checked",true);
                    if (go_isSupportHc && hcbz == 1){
                        if (go_isSupportCartoon){
                            $("span[xmid='"+thisId+"']").css("display","inline")
                        }else{
                            $("span[xmid='"+thisId+"']").css("display","inline")
                        }

                    }
                }
            }
        });
    }
}

function newEvalutionTest(){
    go_isPowerBuilderInvake = false;
    showProgressMessager('提示', '正在生成评估单，请稍后...', '');
    api_newEvalution(1,"苏州市立医院","202","2017-12-14 14:00:00",113,"4517","沈*强",2,2);
}

function isChangedTest(){

}

/**
 * 对外接口：监控是否有值变化
 */
function api_isChanged(){
    // 直接调用pb
    var param = null;
    if (go_changed){
        param = {flag:'ischanged',changed:'true'};
    }else{
        param = {flag:'ischanged',changed:'false'};
    }
    param = JSON.stringify(param);
    invokePowerBuilder(param);
}

/**
 * 对外接口：打印
 */
function api_print(){
    print();
}

/**
 * 对外接口：一键设置默认值
 * @param str 默认值
 */
function api_setTextBoxDefaultValue(str){
    go_isPowerBuilderInvake = true;
    setTextBoxDefaultValue(str);
}

/**
 * 对外接口: 新增评估记录
 * @param jgid 机构id
 * @param jgmc 机构名称
 * @param brbq 病人病区
 * @param txsj 填写时间
 * @param zyh 住院号
 * @param userid 用户id
 * @param styleId 样式序号
 * @param version 样式版本号
 */
function api_newEvalution(jgid,jgmc,brbq,txsj,zyh,userid,username,styleId,version){
    go_isPowerBuilderInvake = true;
    if (go_changed){
        $.messager.confirm('确认','前一份评估单值发生改变\r\n是否需要保存？',function(r){
            if (r){
                go_callBackFunction = "newEvalutionNow('"+jgid+"','"+jgmc+"','"+brbq+"','"+txsj+"','"+zyh+"','"+userid+"','"+username+"','"+styleId+"','"+version+"')";
                saveEvalutionRecord(false);
            }else{
                newEvalutionNow(jgid,jgmc,brbq,txsj,zyh,userid,username,styleId,version);
            }
        });
    }else{
        newEvalutionNow(jgid,jgmc,brbq,txsj,zyh,userid,username,styleId,version);
    }

}

/**
 * 对外接口：预览模式
 * @param jgid  机构ID
 * @param styleId  样式序号
 * @param version  样式版本号
 */
function api_previewEvalutionStyle(jgid,styleId,version){
    newEvalutionForPreview(jgid,styleId,version);
}

/**
 * 对外接口：打开记录
 * @param jlxh 记录序号
 */
function api_openEvalutionRecord(jlxh,userId,userName){
    go_isPowerBuilderInvake = true;
    // 判断是否值改变
    if (go_changed){
        $.messager.confirm('确认','前一份评估单值发生改变\r\n是否需要保存？',function(r){
            if (r){
                go_callBackFunction = "openEvalutionRecord('"+jlxh+"','"+userId+"','"+userName+"')";
                saveEvalutionRecord(false);
            }else{
                openEvalutionRecord(jlxh,userId,userName);
            }
        });
    }else{
        openEvalutionRecord(jlxh,userId,userName);
    }
}

/**
 * 对外接口：保存评估单
 */
function api_saveEvalutionRecord(){
    go_isPowerBuilderInvake = true;
    saveEvalutionRecord(true);
}
function saveEvalutionRecordTest(){
    go_isPowerBuilderInvake = false;
    saveEvalutionRecord(true);
}
// 说明：
// 1.评估单新增和修改采用相同的接口
// 2.前端判断明细项目记录的状态【新增、修改、删除】
// 3.根据class过滤出所有需要保存的控件，表格样式项目通过go_groupids处理
// TODO : 表格样式项目保存暂时不处理
function saveEvalutionRecord(invokePb){
    if (go_evalutionStyle == null || go_evalutionStyle.length <=0){
        showAlertMessager("提示信息","无评估单，不可保存")
        return ;
    }
    // 判断状态
    if (go_status == 2){
        showAlertMessager("提示信息","查看状态禁止修改!")
        return ;
    }

    // 1.主记录处理
    var record = {};
    if (go_jlxh == null || go_jlxh == 0){
        record.JLXH = 0;
        record.ZYH = go_zyh;
        record.BRBQ = go_brbq;
        record.YSXH = go_ysxh;
        record.BBH = go_version;
        record.TXSJ = go_txsj;
        record.TXGH = go_userId;
        record.TXXM = go_userName;
        record.JLSJ = parserDateTime(new Date());
        record.DYCS = 0;
        record.PGNR = "";
        record.ZFBZ = 0;
        record.JGID = go_jgid;
        record.YSLX =go_evalutionStyle[0].YSLX;
    }else{
        record.JLXH = go_jlxh;
        /*record.ZYH = go_record.ZYH;
        record.BRBQ = go_record.BRBQ;
        record.YSXH = go_record.YSXH;
        record.BBH = go_record.BBH;
        record.TXSJ = go_record.TXSJ;
        record.TXGH = go_record.TXGH;
        record.TXXM = go_record.TXXM;
        record.JLSJ = go_record.JLSJ;
        record.DYCS = 0;
        record.PGNR = "";
        record.ZFBZ = 0;
        record.JGID = go_record;
        record.YSLX =go_record.YSLX;*/
    }

    // 2.明细记录处理
    // (1)整理本次页面需要保存的项目
    var details = [];
    record.details = details;

    $("input[kjlx='textbox']").each(function(){
        handlerTextBox(this);
    });

    $("input[kjlx='mul-textbox']").each(function(){
        handlerTextBox(this);
    });

    $("input[kjlx='numberbox']").each(function(){
        handlerNumberbox(this);
    });

    $("input[kjlx='datebox']").each(function(){
        handlerDatebox(this);
    });

    $("input[kjlx='datetimebox']").each(function(){
        handlerDateTimebox(this);
    });

    $("input[kjlx='radiobox']").each(function(){
        handlerCheckbox(this);
    });

    $("input[kjlx='checkbox']").each(function(){
        handlerCheckbox(this);
    });

    $("input[kjlx='combobox']").each(function(){
        handlercombobox(this);
    });

    //console.log(record);
    // (2)确定明细数据状态 add update delete ignore
    confirmDetailsStatus(record,go_record);

    // (3)提交数据
    $.messager.confirm('确认','您确认提交数据吗？',function(r){
        if (r){
            commit(record);
        }
    });
    //console.log(record);

    /*******************************内部方法****************************/
    // 提交数据
    function commit(record){
        data = JSON.stringify(record);
        // 后台保存
        $.ajax({
            url : 'mobile/evaluation/v56_update1/commit/record',
            type : 'post',
            data : data,
            contentType : "application/json",
            dataType : "json",
            beforeSend : function(req) {
                showProgressMessager('操作提示', '正在提交数据...', '');
            },
            success : function(response) {
                if (response.ReType == 0) {
                    // 成功，置位修改状态
                    go_record = response.Data;
                    go_jlxh = go_record.JLXH;
                    go_changed = false;
                    // 成功以后调用pb
                    if (go_isPowerBuilderInvake){
                        var param = {flag:'save',success:'true',recordid:go_jlxh};
                        param = JSON.stringify(param);
                        try {
                            if (invokePb == true){
                                invokePowerBuilder(param);
                            }
                        } catch(error) {
                            console.log(error);
                        } finally {
                            $.messager.progress('close');
                        }
                    }
                    showOkMessager("操作提示","保存成功!")
                } else {
                    if (go_isPowerBuilderInvake){
                        var param = {flag:'save',success:'false',recordid:-1};
                        param = JSON.stringify(param);
                        if (go_isPowerBuilderInvake){
                            try {
                                if (invokePb == true){
                                    invokePowerBuilder(param);
                                }
                            } catch(error) {
                                console.log(error);
                            } finally {
                                $.messager.progress('close');
                            }
                        }
                    }
                    showAlertMessager('操作提示', response.Msg);
                }
                // 调用回调方法
                invokeCallBackStrFuntion(go_callBackFunction);
                go_callBackFunction = "";
            },
            complete : function(XMLHttpRequest, textStatus) {// 成功或失败都会调用
                $.messager.progress('close');
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                showAlertMessager('操作提示', XMLHttpRequest.responseText);
                $.messager.progress('close');
            }
        });
    }
    // 确定明细记录状态
    function confirmDetailsStatus(record,dbRecord){
        var details = record.details?record.details:[];
        var dbDetails = dbRecord.details?dbRecord.details:[];

        if (record.JLXH == null || record.JLXH == 0){
            confirmAddDetailsStatus(details);
        }else{
            confirmEditDetailsStatus(details,dbDetails);
        }
    }

    // 新增状态下明细记录状态确定
    function confirmAddDetailsStatus(details){
        for(var i = 0 ; i < details.length ; i++){
            var _d = details[i];
            _d.Status = "add";
        }
    }

    // 修改状态下明细记录状态确定
    function confirmEditDetailsStatus(details,dbDetails){
        var dc = details.length;
        var dbc = dbDetails.length;
        var dels = [];
        // 1.拿本次数据比较数据库明细，存在就比较值，不相等就更新status=update，相等不处理status=ignore；不存在status=add
        for (var i = 0 ; i < dc;i++){
            var _d = details[i];
            var _exsit = false;
            for (var j = 0 ; j < dbc;j++){
                var _db = dbDetails[j];
                if (_d.XMXH == _db.XMXH){
                    if (_d.XMNR == _db.XMNR){
                        _d.MXXH = _db.MXXH;
                        _d.Status = "ignore";
                    }else{
                        _d.MXXH = _db.MXXH;
                        _d.Status = "update";
                    }
                    _exsit = true;
                    break;
                }
            }
            if (!_exsit){
                _d.Status = "add";
            }
        }

        // 2.拿数据数据比较本次明细，存在不处理，不存在status=delete
        for (var i = 0 ; i < dbc;i++){
            var _db = dbDetails[i];
            var _exsit = false;
            for (var j = 0 ; j < dc; j++){
                var _d = details[j];
                if (_d.XMXH == _db.XMXH){
                    _exsit = true;
                    break;
                }
            }
            if (!_exsit){
                _db.Status = "delete";
                dels.push(_db);
            }
        }

        // 3.将删除的添加到本次结果中
        for (var i = 0 ; i < dels.length;i++){
            details.push(dels[i]);
        }
    }

    function handlercombobox(o){
        var value = $(o).combobox("getValue");
        if (!stringIsNull(value)){
            createDetail(o,value);
        }
    }

    function handlerTextBox(o){
        var value = $(o).textbox("getValue");
        if (!stringIsNull(value)){
            createDetail(o,value);
        }
    }

    function handlerNumberbox(o){
        var value = $(o).numberbox("getValue");
        if (!stringIsNull(value)){
            createDetail(o,value);
        }
    }

    function handlerDatebox(o){
        var value = $(o).datebox("getValue");
        if (!stringIsNull(value)){
            createDetail(o,value);
        }
    }

    function handlerDateTimebox(o){
        var value = $(o).datetimebox("getValue");
        if (!stringIsNull(value)){
            createDetail(o,value);
        }
    }

    function handlerCheckbox(o){
        if ($(o).is(":checked")){
            createDetail(o,"√");
        }
    }

    function createDetail(o,value){
        var xmxh = $(o).attr("id");
        var sjxm = $(o).attr("sjxm");
        var sjxmmc = $(o).attr("sjxmmc");
        var dzlx = $(o).attr("dzlx");
        var dzbdjl = $(o).attr("dzbdjl");
        var kjlx = confirmProjectControllType(o);
        var detail = {
            MXXH:0,
            JLXH:record.JLXH,
            XMXH:xmxh,
            SJXM:sjxm,
            KJLX:kjlx,
            SJXMMC:sjxmmc,
            XMNR:value,
            BGFZZH:0,
            DZLX:dzlx,
            DZBDJL:dzbdjl
        };
        details.push(detail);
    }
}

function confirmProjectControllType(o){
    // 控件类型定义：1.text 2:numberbox 3:datebox 4:datetimebox 5:checkbox 6:combobox
    var types = $(o).attr("kjlx");
    var _t = confirmControllTypeByStr(types)
    return _t;
}

function confirmControllTypeByStr(str){
    if (str == "textbox"){
        return 1;
    }else if (str == "mul-textbox"){
        return 1;
    }else if (str == "numberbox"){
        return 2;
    }else if (str == "datebox"){
        return 3;
    }else if (str == "datetimebox"){
        return 4;
    }else if (str == "radiobox"){
        return 5;
    }else if (str == "checkbox"){
        return 5;
    }else if (str == "combobox"){
        return 6;
    }
}
function openEvalutionRecordTest(){
    var jlxh = $("#jlxh").val();
    openEvalutionRecord(jlxh,"5028","王小王");
}

function openEvalutionRecord(jlxh,userid,username){
    go_userId = userid;
    go_userName = username;
    go_jlxh = jlxh;
    // 1.根据jlxh获取模板及各项目数据
    var param = {
        JLXH:jlxh
    };
    showProgressMessager('提示', '正在获取评估单数据，请稍后...', '');
    $.ajax({
        type : "post",
        url : "mobile/evaluation/v56_update1/get/record",
        data:param,
        beforeSend : function(req) {
            req.setRequestHeader("Accept", "application/json");
        },
        success : function(response) {
            if (response.ReType == 0) {
                go_evalutionStyle = response.Data.style;
                createStyle(go_evalutionStyle);
                go_record = response.Data.record;
                go_jlxh = jlxh;
                displayData(go_record);
                authorityControll();
                switchBackGroundColor();
                $.messager.progress('close');
            } else {
                showAlertMessager("错误信息", response.Msg);
                $.messager.progress('close');
            }
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            showAlertMessager('提示', textStatus + ":" + errorThrown);
            $.messager.progress('close');
        }
    })
}

/**
 * 展示数据
 * @param record
 */
function displayData(record){
    go_isAutoSetValue = true;
    var details = record.details;
    // 签名信息
    var txsj = record.TXSJ;
    var txxm = record.TXXM;
    var qmxm = record.QMXM;
    var syxm = record.SYXM;
    var qmgh = record.QMGH;
    var sygh = record.SYGH;

    if (!checknull(txsj)){
        $("#write-time").text(parserDateTime(txsj));
    }else{
        $("#write-time").text("");
    }
    if (!checknull(txxm)){
        $("#write-name").text(txxm);
    }else{
        $("#write-name").text("");
    }
    if (!checknull(qmxm)){
        $("#nurse-sign").val(qmxm);
        $("#nurse-sign").attr("usercode",qmgh);
    }else{
        $("#nurse-sign").val("");
        $("#nurse-sign").attr("usercode","");
    }
    if (!checknull(syxm)){
        $("#pnurse-sign").val(syxm);
        $("#pnurse-sign").attr("usercode",sygh);
    }else{
        $("#pnurse-sign").val("");
        $("#pnurse-sign").attr("usercode","");
    }
    //$("#").val();

    for (var i = 0 ; i < details.length;i++){
        var _d = details[i];
        var kjlx = _d.KJLX;
        var id = _d.XMXH;
        var value = _d.XMNR;
        var docId = "#"+id;
        var dzlx = _d.DZLX;
        var dzbdjl = _d.DZBDJL;

        // 控件类型定义：1.text 2:numberbox 3:datebox 4:datetimebox 5:checkbox 6:combobox
        if (kjlx == 1){
            $(docId).textbox("setValue",value);
        }else if (kjlx == 2){
            $(docId).numberbox("setValue",value);
        }else if (kjlx == 3){
            $(docId).datebox("setValue",value);
        }else if (kjlx == 4){
            $(docId).datetimebox("setValue",value);
        }else if (kjlx == 5){
            $(docId).attr("checked",true);
            fireEvent($(docId));
        }else if (kjlx == 6){
            $(docId).combobox("setValue",value);
        }
        // 数据关联信息 写入
        $(docId).attr("dzlx",dzlx);
        $(docId).attr("dzbdjl",dzbdjl);
    }
    go_isAutoSetValue = false;
}

/**
 * 设置组件值
 * @param id
 * @param value
 */
function setComponentValue(id,value){
    var docId = "#"+id;
    var kjlx = $(docId).attr("kjlx");
    // 控件类型定义：1.text 2:numberbox 3:datebox 4:datetimebox 5:checkbox 6:combobox
    if (kjlx == "textbox"){
        $(docId).textbox("setValue",value);
    }else if (kjlx == "numberbox"){
        $(docId).numberbox("setValue",value);
    }else if (kjlx == "datebox"){
        $(docId).datebox("setValue",value);
    }else if (kjlx == "datetimebox"){
        $(docId).datetimebox("setValue",value);
    }else if (kjlx == "checkbox"){
        $(docId).attr("checked",true);
        fireEvent($(docId));
    }else if (kjlx == "combobox"){
        $(docId).combobox("setValue",value);
    }
}

function getComponentValue(o){
    var kjlx = $(o).attr("kjlx");
    // 控件类型定义：1.text 2:numberbox 3:datebox 4:datetimebox 5:checkbox 6:combobox
    var value ="";
    if (kjlx == "textbox"){
        value = $(o).textbox("getValue");
    }else if (kjlx == "numberbox"){
        value = $(o).numberbox("getValue");
    }else if (kjlx == "datebox"){
        value = $(o).datebox("getValue");
    }else if (kjlx == "datetimebox"){
        value = $(o).datetimebox("getValue");
    }else if (kjlx == "checkbox"){
        value = $(o).attr("checked",true);
        fireEvent($(docId));
    }else if (kjlx == "combobox"){
        value = $(o).combobox("getValue");
    }else {
        value = $(o).text();
    }

    return value;
}

function newEvalutionNow(jgid,jgmc,brbq,txsj,zyh,userid,username,styleId,version){
    go_jgid = jgid;
    go_jgmc = jgmc;
    go_zyh = zyh;
    go_jlxh = 0;
    go_txsj = txsj?txsj:parserDateTime(new Date());
    go_brbq = brbq;
    go_ysxh = styleId;
    go_version = version;
    go_userId = userid;
    go_userName = username;
    go_changed = false;
    newEvalution(jgid,styleId,version);
}

/**
 * 新增评估预览
 */
function newEvalutionForPreview(jgid,styleId,version){
    go_record = {};
    go_record.details = [];
    var zyh = go_zyh?go_zyh:0;
    // 清空签名信息
    clearSignInfo();
    // 写入签名信息

    var param = {
        JGID:jgid,
        YSXH:styleId,
        BBH:version,
        ZYH:0
    };
    // 获取评估样式记录
    $.ajax({
        type : "post",
        url : "mobile/evaluation/v56_update1/get/style",
        data:param,
        beforeSend : function(req) {
            req.setRequestHeader("Accept", "application/json");
        },
        success : function(response) {
            if (response.ReType == 0) {
                go_evalutionStyle = response.Data;
                createStyle(go_evalutionStyle);
                // 切换背景图片
                switchBackGroundColor();
                $.messager.progress('close');
            } else {
                showAlertMessager("错误信息", response.Msg);
                $.messager.progress('close');
            }
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            showAlertMessager('提示', textStatus + ":" + errorThrown);
            $.messager.progress('close');
        }
    })
}
/**
 * 新增评估
 */
function newEvalution(jgid,styleId,version){
    go_record = {};
    go_record.details = [];
    var zyh = go_zyh?go_zyh:0;
    // 清空签名信息
    clearSignInfo();
    // 写入签名信息
    writeSignInfo();

    var param = {
        JGID:jgid,
        YSXH:styleId,
        BBH:version,
        ZYH:go_zyh
    };
    // 获取评估样式记录
    $.ajax({
        type : "post",
        url : "mobile/evaluation/v56_update1/get/style",
        data:param,
        beforeSend : function(req) {
            req.setRequestHeader("Accept", "application/json");
        },
        success : function(response) {
            if (response.ReType == 0) {
                go_evalutionStyle = response.Data;
                if (go_evalutionStyle != null && go_evalutionStyle.length > 0){
                    go_baseInfos = go_evalutionStyle[0].baseInfo;
                }
                createStyle(go_evalutionStyle);
                authorityControll();
                getRelations();
                // 切换背景图片
                switchBackGroundColor();
                $.messager.progress('close');
            } else {
                showAlertMessager("错误信息", response.Msg);
                $.messager.progress('close');
            }
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            showAlertMessager('提示', textStatus + ":" + errorThrown);
            $.messager.progress('close');
        }
    })
}

/**
 * 创建评估样式
 * @param styleData
 */
function createStyle(styleDatas){
    if (styleDatas == null || styleDatas.length <= 0){
        return ;
    }
    var styleData = styleDatas[0];
    var ysmc = styleData.YSMC ? styleData.YSMC : "评估单";
    var title = "创业数字医院"+ ysmc;
    go_rightClickProjects = [];
    displayTitle(go_jgmc);
    displayTitile2(ysmc);
    displayContent(styleData);
}

function displayTitle(title){
    $("#title_span").text(title);
}

function displayTitile2(title){
    $("#two_title_span").text(title);
}

function displayContent(styleData){
    //console.log(styleData);
    var projects = styleData.projects;
    if (projects == null || projects.length <=0)
        return;

    var rootCount = projects.length;
    var html = "";
    $("#content").html(html);
    for (var i = 0 ; i < rootCount; i++){
        var project = projects[i];
        // root级别项目，默认都是分类标签，固维护时需要控制
        createHtml(0,null,null,null,project);
    }

    $.parser.parse($('#content'));

    // 写入基本信息
    handleBaseInfo();

    // 绑定事件
    bindingEvent();
}

/**
 // 可提前将一些项目的属性信息，写入标签中，方便后续处理
  1.由父项目确定的属性
  (1)控件类型 XJKJLX
    1,单行输入2.多行输入 3,单项选择 4,多项选择 5.下拉列表 6.标签显示 7.表格 9.无
  (2)下级默认展开
  (3)下级互斥

  2.由自身确定的属性
  (1)数据类型 (2)项目扩展 (3)项目类型 (4)数据格式 (5)换行标志 (6)项目名称 (7)前置文本 (8)后置文本
 * @param parent
 * @param it
 */
function createHtml(typeId,great_grandparent,grandparent,parent,it){
    // 曾祖父节点属性
    var pppId = null;
    var pppXjkjlx = null;
    // 祖父节点属性
    var ppId = null;
    var ppXjkjlx = null;
    // 父节点属性
    var pId = null;
    var pName = null;
    var pXjkjlx = null;
    var pXjmrzk = null;
    var pXjhc = null;
    // 本节点属性
    var dataType = null;
    var projectExt = null;
    var projectType = null;
    var hrFlag = null;
    var name = null;
    var beforeText = null;
    var afterText = null;
    var displayLen = null;
    var editAble = true;
    var children = it.children;
    var id = null;
    var dataFormattor = null;
    var childExpand = null;
    var dataUpBound = 100;
    var dataDownBound = 0;
    var precision = 0;
    var extend = null;
    var importantProject = 0 ;
    var modifyAble = 1;
    var leftMargin = 0;

    var typeId = typeId; // 用来记录当前分类项目id

    if (parent != null){
        pId = parent.XMXH;
        pXjkjlx = parent.XJKJLX;
        pXjhc = parent.XJHC;
        pXjmrzk = parent.XJZK;
        pName = parent.XMMC;
    }else{
        pId = 0 ; // pid 0
        pXjkjlx = 9 ; // 无
        pXjmrzk = 1 ; // 展开
        pXjhc =0; // 不互斥
        pName = ""; // 无
    }

    if (grandparent != null){
        ppId = grandparent.XMXH;
        ppXjkjlx = grandparent.XJKJLX;
    }else{
        ppId = 0;
        ppXjkjlx = 9;
    }

    if (great_grandparent != null){
        pppId = great_grandparent.XMXH;
        pppXjkjlx = great_grandparent.XJKJLX;
    }else{
        pppId = 0;
        pppXjkjlx = 9;
    }

    /*********************************************************************/
    dataType = it.SJLX?it.SJLX:2; // 数据类型默认为2 字符型
    dataFormattor = it.SJGS?it.SJGS:""; // 数据格式，默认为空
    projectExt = it.XMKZ?it.XMKZ:""; // 项目扩展未设置项目扩展的，默认为空
    projectType = it.XMLB?it.XMLB: 7; // 项目类别未设置项目类别的 ，默认为常规项目
    hrFlag = it.HHBZ?it.HHBZ:0;// 换行标志默认0 不换行
    name = it.XMMC?it.XMMC:""; // 项目名称默认""
    beforeText = it.QZWB?it.QZWB:""; // 前置文本默认为空
    afterText = it.HZWB?it.HZWB:""; // 后置文本默认为空
    displayLen = it.XSCD?it.XSCD:6; // 显示长度默认为120px
    displayLen = displayLen * 8;
    id = it.XMXH?it.XMXH:0;
    childExpand = it.XJZK; // 下级展开标志，默认展开
    dataUpBound = it.SJSX?it.SJSX:dataUpBound; // 数据上限，只针对number控件
    dataDownBound = it.SJXX?it.SJXX:dataDownBound; // 数据下限，只针对number控件
    precision = it.XMKZ?it.XMKZ:precision; // 项目扩展，精度
    extend =it.XMKZ?it.XMKZ:""; // 项目扩展
    importantProject = it.ZDXM?it.ZDXM:0; // 重点项目
    modifyAble = it.XGBZ; // 修改标志
    // 左边距 项目本身属性
    leftMargin = it.XMZH?it.XMZH:0; // 左边距（使用项目组号字段保存）
    leftMargin = leftMargin + "px";
    var groupName = "group-"+pId;
    var pDocId = null; // 用于记录html插入点
    var html = "";     // 整体插入数据
    pDocId = (pId == 0)?"#content":"span[parentid="+pId+"]";

    /*************************处理表格类型*******************************/
    // 针对表格类型的项目，只处理3级，超过3级的忽略；不足3级如何处理
    // 当当前项目父节点的结束处理标志为1时，不在处理
    // 只有当曾祖父节点下级控件类型为表格类型时，会打上结束处理标志
    if (parent != null){
        if (parent.endFlag == 1){
            return ;
        }
    }

    if (ppXjkjlx == 7){
        it.endFlag = 1;
    }

    var xjkjlx = it.XJKJLX;
    if (xjkjlx == 7){
        var tableClass = "t-table"+typeId;
        var trid = "tr_"+id;
        inputClassName += " easyui-datagrid type-datagrid";
        _inputType = "datagrid";
        if (go_typeLabelExpand){

        }else{
            tableClass  += " content-span-cell-none";
        }
        html += '<span class="content-span-cell-block '+tableClass+'">'+name+'' +
            '    <a href="javascript:void(0);" style="margin-left: 50px;" class="easyui-linkbutton" ' +
            '            data-options="iconCls:\'icon-add\' ,size:\'small\',plain:\'true\'"   \n' +
            '            onclick="groupAdd(this)"' +
            '           dgid="'+id+'">添 加</a>  ' +
            '     <a href="javascript:void(0);" class="easyui-linkbutton" ' +
            '           data-options="iconCls:\'icon-remove\' ,size:\'small\',plain:\'true\'"   ' +
            '           onclick="groupDelete(this)"' +
            '           dgid="'+id+'">删 除</a>  ' +
            '</span>' +
            '<table  id='+id+' class="easyui-datagrid '+tableClass+'" style="width:790px;height:120px"' +
            '                       data-options="singleSelect:true,border:false">' +
            '                    <thead><tr id='+trid+'> </tr>' +
            '                    </thead>' +
            '                </table>';

        $("#content").append(html);
        // 记录所有datagrid的id；
        go_groupids.push(id);

        var childCount = children.length;
        if (childCount != null || childCount >0){
            for (var i = 0 ; i < childCount;i++){
                var childo = children[i];
                createHtml(typeId,grandparent,parent,it,children[i]);
            }
        }
        return;
    }
    if (pXjkjlx == 7){
        var childCount = children.length;
        if (childCount != null || childCount >0){
            for (var i = 0 ; i < childCount;i++){
                var childo = children[i];
                createHtml(typeId,grandparent,parent,it,children[i]);
            }
        }
        return;
    }
    if (ppXjkjlx == 7){
        var fid = "f_"+id;
        var dataoptions = "field:'"+id + "'," +
            "width:"+displayLen +"," +
            "halign:'center'," +
            "align:'center'," +
            "";
        if (pXjkjlx == 1){ // 输入
            html += '<th data-options="'+dataoptions+'">'+name+'</th>';
        }else if (pXjkjlx == 5){ // 下拉
            html += '<th data-options="'+dataoptions+'">'+name+'</th>';
        }

        //console.log(html);
        var trid = "#tr_"+ppId;
        //console.log(trid);
        $(trid).append(html);

        /*var childCount = children.length;
        if (childCount != null || childCount >0){
            for (var i = 0 ; i < childCount;i++){
                var childo = children[i];
                createHtml(grandparent,parent,it,children[i]);
            }
        }*/
        return;
    }
    /************************************************************/
    // 1.确定控件类型
    if (projectType == 1 || projectType == 2){
        pXjkjlx = 6; // 标签
    } else if (projectType == 3){
        pXjkjlx = 1; // 文本
        editAble = false;
    } else if (projectType == 5) {
        pXjkjlx = 1; // 文本
        editAble = false;
    }

    // 2.开始组装html
    var className = "";
    var childClassName = "";
    var inputClassName="";
    var eventName = "fireEvent(this)";
    var mouseDown = "fireMouseDownEvent(e)";

    // 默认展开标志,当前项目的子项目的属性
    if (childExpand == 0){
        childClassName += " content-span-cell-none"
    }else{
        childClassName += ""
    }

    // 换行标志
    if (hrFlag == 1){
        if (pId != 0){
            html += '<br/>';
        }
        className += " content-span-cell-inline";
    }else{
        className += " content-span-cell-inline";
    }

    // 分类标签 特殊处理
    if (projectType == 1){
        typeId = id;
        // 分类标签 展开折叠功能
        html += '<hr/><span style="margin-right: 5px;"><img expand="true" flid="'+id+'" src="resources/images/evalution/expand1.png" onclick="expandControll(this)"></span>';

        className = "class-label";
    }else if (projectType == 2){
        //className = "";
    }

    // 重点项目 labelClass 用于重点项目 及关联项目的字体颜色控制
    var dataoptions = "";
    var labelClassName ="";
    if (importantProject == 1){
        labelClassName = "important-project";
    }

    /*************************************可修改权限控制*********************************/
    // 修改标志控制(只有基本信息项目、签名项目、数据关联项目、常规项目需控制修改标志)
    if (projectType == 3 || projectType == 4 || projectType == 6 || projectType == 7){
        if (modifyAble == 1){
            if (dataoptions == "")
                dataoptions += "readonly:false";
            else
                dataoptions += ",readonly:false";
        }else{
            if (dataoptions == "")
                dataoptions += "readonly:true";
            else
                dataoptions += ",readonly:true";
        }
    }

    // 数据关联项目
    if (projectType == 6){ // 数据关联项目
        labelClassName += " blue-style ";
        if (dataoptions == ""){
            dataoptions += "readonly:true";
        }else{
            dataoptions += ",readonly:true";
        }
    }
    // 签名项目
    if (projectType == 4){
        if (dataoptions == ""){
            dataoptions += "readonly:true";
        }else{
            dataoptions += ",readonly:true";
        }
    }

    // 所有[常规项目]控件绑定onChange事件，监控值是否变化
    if (projectType == 7){
        if (dataoptions == ""){
            dataoptions += "onChange:valueChanged";
        }else{
            dataoptions += ",onChange:valueChanged";
        }
    }
    /*************************************end*********************************/
    // 添加Html
    // 标签显示
    var _inputType = "";
    html += '<span class="'+className+'" xmid="'+id+'">';

    if (pXjkjlx == 6){ // label类型不进行长度控制、前置文本和后置文本
        inputClassName += " type-label";
        html += '<label style="margin-left: '+leftMargin+'" class="'+inputClassName+' '+labelClassName+'" id='+id+'>'+name +'</label><span id="import-'+id+'" style="font-size: 13px;font-weight: 100;color: red;"></span>';
    }else if (pXjkjlx == 1){ // 单行编辑框
        // 单行编辑框 根据数据类型分为3中 ; 1数字、2文本、3日期
        var glyw = 0 ;
        var glxm = 0 ;
        if (dataType == 2){
            inputClassName += " easyui-textbox type-textbox";
            // 单行编辑框（若是项目类型：6 数据关联项目,写入扩展标志）
            var extents = extendDataUnionParser(extend);
            glyw = extents.GLYW; // 业务类型：2：风险 5：生命体征
            glxm = extents.GLXM; // 业务类型 = 2  glxm = xmxh ; = 5 glxm = 评估类型
            _inputType = "textbox";
        }else if (dataType == 1){
            var extents = extendDataUnionParser(extend);
            glyw = extents.GLYW; // 业务类型：2：风险 5：生命体征
            glxm = extents.GLXM; // 业务类型 = 2  glxm = xmxh ; = 5 glxm = 评估类型
            inputClassName += " easyui-numberbox type-numberbox";
            if (dataoptions == ""){
                dataoptions += "min:"+dataDownBound + ",max:"+dataUpBound + ",prompt: '"+dataDownBound+"-'+'"+dataUpBound+"',precision:'"+precision+"',width:'"+displayLen+"'";
            }else{
                dataoptions += ",min:"+dataDownBound + ",max:"+dataUpBound+ ",prompt: '"+dataDownBound+"-'+'"+dataUpBound+"',precision:'"+precision+"',width:'"+displayLen+"'";
            }
            _inputType = "numberbox";
        }else if (dataType == 3){ // 通过数据格式确定是否datebox /datetimebox
            var _inputClass =validDateBoxType(dataFormattor);
            inputClassName +=  " easyui-" + _inputClass + " type-" + _inputClass;
            if (dataoptions == ""){
                dataoptions += "editable:false,width:'"+displayLen+"'";
            }else{
                dataoptions += ",editable:false,width:'"+displayLen+"'";
            }
            if (_inputClass == "datebox"){
                dataoptions += ",prompt:'yyyy-mm-dd'"
            }else{
                dataoptions += ",prompt:'yyyy-mm-dd hh:mi:ss'";
            }
            _inputType = _inputClass;
            // TODO 日期格式化
        }

        if (id == 410){
            var a = 1;
        }
        html += '<label style="padding-right: 6px;margin-left: '+leftMargin+'" class="'+labelClassName+'">'+beforeText+'</label>' +
            '<input class="'+inputClassName+' '+labelClassName+'" ' +
            'id='+id+' ' +
            'kjlx="'+_inputType+'" ' +
            'sjxm="'+pId+'" ' +
            'xmlb="'+projectType+ '"' +
            'glyw="'+glyw+ '"' +
            'glxm="'+glxm+ '"' +
            'sjxmmc="'+pName+'" ' +
            'hcbz="'+pXjhc+'" ' +
            'style="width: '+displayLen+'px;" ' +
            'data-options="'+dataoptions+'"/>' +
            '<label  style="padding-right: 6px;" class="content-span-cell-after-label '+labelClassName+'">'+afterText+'</label>';

        // 加入到缓存中，等待绑定事件
        if (_inputType == "textbox" || _inputType == 'numberbox'){
            if (projectType == 4 || projectType == 6 || projectType == 7){
                go_rightClickProjects.push(id);
            }
        }

    }else if (pXjkjlx == 2){ // 多行编辑框
        inputClassName += " easyui-textbox type-mul-textbox";
        if (dataoptions == ""){
            dataoptions += "multiline:true";
        }else{
            dataoptions += ",multiline:true";
        }
        _inputType = "mul-textbox";
        html += '<label style="padding-right: 4px;margin-left: '+leftMargin+'" class="'+labelClassName+'">'+beforeText+'</label>' +
            '<input class="'+inputClassName+' '+labelClassName+'" ' +
            'id='+id+' ' +
            'kjlx="'+_inputType+'" ' +
            'xmlb="'+projectType+ '"' +
            'sjxm="'+pId+'" ' +
            'sjxmmc="'+pName+'" ' +
            'hcbz="'+pXjhc+'' +
            '"style="width: '+displayLen+'px;height:50px" data-options="'+dataoptions+'"/>' +
            '<label style="padding-right: 6px;" class="content-span-cell-after-label '+labelClassName+'">'+afterText+'</label>';

        // 加入到缓存中，等待绑定事件
        if (projectType == 4 || projectType == 6 || projectType == 7){
            go_rightClickProjects.push(id);
        }

    }else if (pXjkjlx == 3){ // 单项选择 :增加样式的原因，解决谷歌浏览器下checkbox与文本不对齐的问题
        inputClassName += " type-checkbox";
        _inputType = "checkbox";
        html += '<label style="padding-right: 4px;margin-left: '+leftMargin+'" class="'+labelClassName+'">'+beforeText+'</label>' +
            '<label class="'+labelClassName+'"><input type="checkbox" ' +
            'name="'+groupName+'" ' +
            'class="'+inputClassName+' '+labelClassName+'" ' +
            'id='+id+' ' +
            'kjlx="'+_inputType+'" ' +
            'xmlb="'+projectType+ '"' +
            'sjxm="'+pId+'" ' +
            'sjxmmc="'+pName+'" ' +
            'hcbz="'+pXjhc+'" ' +
            'dxbz=1 ' +
            'xjzkbz="'+childExpand+'"' +
            'style="vertical-align:middle; margin-top:-2px; margin-bottom:1px;" ' +
            'onclick="'+eventName+'"/>'+name+'</label>' +
            '<label style="padding-right: 6px;" class="content-span-cell-after-label '+labelClassName+'">'+afterText+'</label>';
    }else if (pXjkjlx == 4){ // 多项选择 :增加样式的原因，解决谷歌浏览器下checkbox与文本不对齐的问题
        inputClassName += " type-checkbox";
        _inputType = "checkbox";
        html += '<label style="padding-right: 4px;margin-left: '+leftMargin+';" class="'+labelClassName+'">'+beforeText+'</label>' +
            '<label class="'+labelClassName+'"><input type="checkbox" ' +
            'name="'+groupName+'" ' +
            'class="'+inputClassName+' '+labelClassName+'" ' +
            'xjzkbz='+childExpand+' ' +
            'id='+id+' ' +
            'xmlb="'+projectType+ '"' +
            'sjxm="'+pId+'" ' +
            'sjxmmc="'+pName+'" ' +
            'kjlx="'+_inputType+'" ' +
            'hcbz="'+pXjhc+'"' +
            ' style="vertical-align:middle; margin-top:-2px; margin-bottom:1px;" onclick="'+eventName+'"/>'+name+'</label>' +
            '<label style="padding-right: 6px;" class="content-span-cell-after-label '+labelClassName+'">'+afterText+'</label>';
    }else if (pXjkjlx == 5){ // 下拉选择
        inputClassName += " easyui-combobox type-combobox";
        _inputType = "combobox";
        // 下拉选择数据来源：
        // 1:以固定格式保存在XMKZ中 @名称1，代码1；名称2，代码2
        // 2:通过配置SQL语句获取 SQL格式：$SQLMOBHIS:SELECT XMMC,XMDM FROM XXX
        var result = extendComboboxParser(extend);
        // 需转成string字符串，否则将data自动转成对象
        var stro = JSON.stringify(result.maps);
        if(result.local){
            if (dataoptions == ""){
                dataoptions += "valueField:'XMDM',textField:'XMMC',data:"+stro;
            }else{
                dataoptions += ",valueField:'XMDM',textField:'XMMC',data:"+stro;
            }
            html += '<label style="padding-right: 4px;margin-left: '+leftMargin+';" class="'+labelClassName+'">'+beforeText+'</label>' +
                '<input class="'+inputClassName+' '+labelClassName+'" ' +
                'id='+id+' ' +
                'kjlx="'+_inputType+'" ' +
                'sjxm="'+pId+'" ' +
                'sjxmmc="'+pName+'" ' +
                'hcbz="'+pXjhc+'" ' +
                'xmlb="'+projectType+ '"' +
                'style="width: '+displayLen +'px;" ' +
                'data-options='+dataoptions+' ' +
                '/>' +
                '<label style="padding-right: 6px;" class="content-span-cell-after-label '+labelClassName+'">'+afterText+'</label>';
        }else{ //通过url服务端获取数据
            if (result.maps.length == 1){
                if (dataoptions == ""){
                    dataoptions += "valueField:'XMDM',textField:'XMMC',url:'mobile/evaluation/v56_update1/get/combo/datas?DataSource="+result.maps[0].DataSource.toString()+"&Sql="+result.maps[0].Sql.toString()+"'";
                }else{
                    dataoptions += ",valueField:'XMDM',textField:'XMMC',url:'mobile/evaluation/v56_update1/get/combo/datas?DataSource="+result.maps[0].DataSource.toString()+"&Sql="+result.maps[0].Sql.toString()+"'";
                }
            }
            html += '<label style="padding-right: 4px;margin-left: '+leftMargin+'" class="'+labelClassName+'">'+beforeText+'</label>' +
                '<input class="'+inputClassName+'" ' +
                'id='+id+' ' +
                'kjlx="'+_inputType+'" ' +
                'xmlb="'+projectType+ '"' +
                'sjxm="'+pId+'" ' +
                'sjxmmc="'+pName+'" ' +
                'hcbz="'+pXjhc+'" ' +
                'style="width: '+displayLen +'px;" ' +
                'data-options="'+dataoptions+'" ' +
                '/>' +
                '<label style="padding-right: 6px;" class="content-span-cell-after-label '+labelClassName+'">'+afterText+'</label>';
        }
    }

    if (children != null && children.length >0){
        html += '<span parentid='+id+' class='+childClassName+'></span>';
    }
    html +='</span>'

    // 缓存基本信息项目类型
    if (projectType == 3){
        go_baseInfoCompares.push({
            id:id,
            kz:extend
        })
    }

    $(pDocId).append(html);

    // 分类标签，展开控制
    if (projectType == 1){
        //expandControll($("img[flid='"+id+"']"));
    }
    /************************************************************/
    wrapperHtml();
    var childCount = children.length;
    if (childCount != null || childCount >0){
        for (var i = 0 ; i < childCount;i++){
            var childo = children[i];
            createHtml(typeId,grandparent,parent,it,children[i]);
        }
    }

}

/**
 * 将基本信息写入页面
 * 只在新增状态下
 */
function handleBaseInfo(){
    if (go_jlxh == null || parseInt(go_jlxh) == 0){
        if (go_baseInfoCompares.length <=0 || go_baseInfos == null || go_baseInfos.length <=0 ){
            return ;
        }

        for (var i = 0 ; i < go_baseInfoCompares.length ; i++){
            var comp = go_baseInfoCompares[i];
            var docid = comp.id;
            var xmkz = comp.kz;

            if (xmkz == null || xmkz == "") continue;
            for (var j = 0 ; j < go_baseInfos.length ; j++){
                var info = go_baseInfos[j];
                var id = info.key;
                var value = info.value;
                if (xmkz == id){
                    setComponentValue(docid,value);
                }
            }
        }
    }
}
/**
 * 组装HTML：根据当前项目类别，父项目下级控件类型、下级互斥、下级默认展开等信息，确定展示的html格式
 * 1.各属性影响优先级：
 *   项目类别 > 控件类型
 * 2.重要属性都绑定到input上，方便后续操作
 * 3.项目类型：1.分类标签  2.项目标签 3.基本信息项目 4.签名项目 5.组号项目 6.数据关联项目 7.常规项目
 */
function wrapperHtml(){

}

/**
 * 根据数据格式确定是1 easyui-datebox,2 easyui-datetimebox
 * TODO 用正则表达式
 * @param str
 */
function validDateBoxType(str){
    if (str == "" || str == null)
        return "datebox";

    var s = str.split(":");
    if (s.length > 1){
        return "datetimebox";
    }else{
        return "datebox";
    }
}

function groupAdd(o){

    alert($(o).attr("dgid"));
}

function groupDelete(o){
    alert($(o).attr("dgid"));
}


/**
 * 下拉解析器,反馈对象{}
 * // 1:以固定格式保存在XMKZ中 @名称1，代码1；名称2，代码2
 // 2:通过配置SQL语句获取 SQL格式：$SQLMOBHIS:SELECT XMMC,XMDM FROM XXX
 */
function extendComboboxParser1(){
    var s = "@张三,1;李四,2"
    var s2 = "$SQLMOBHIS:SELECT XMMC,XMDM FROM XXX";
    extendComboboxParser(s2);
}

/**
 * 数据关联项目扩展解析器
 * @param str
 */
function extendDataUnionParser(str){
    var o ={GLYW:0,GLXM:0};
    if (checknull(str)){
        return o;
    }
    var strs = str.split("|");
    if (strs == null ||strs.length != 2){
        return o;
    }
    o.GLYW = strs[0];
    o.GLXM = strs[1];
    return o;
}
/**
 * 校验下拉列表项目扩展
 * @param str
 * @returns {{local: boolean, maps: Array}}
 */
function extendComboboxParser(str){
    var retObj = {
        local:true,
        maps:[]
    };

    // 确定本地加载数据还是远程加载
    var reg = new RegExp("@","");
    retObj.local = reg.test(str);

    // 切分数据
    if(retObj.local){
        reg = new RegExp(/@|;/);
        var strs = str.split(reg);
        if(strs != null){
            for(var i = 0 ; i < strs.length;i++){
                if(i==0){
                    continue;
                }
                var s = strs[i];
                if(s != null){
                    reg = new RegExp(/,/);
                    var _s = s.split(reg);
                    if(_s != null && _s.length == 2){
                        retObj.maps.push({
                            XMMC:_s[0],
                            XMDM:_s[1]
                        })
                    }
                }
            }
        }
    }else{
        reg = new RegExp(/\$|\:/);
        var strs = str.split(reg);
        if(strs != null && strs.length == 3){
            retObj.maps.push({
                DataSource:strs[1],
                Sql:strs[2]
            });
        }

    }
    return retObj;
}

function expandControll(o){
    var expand = $(o).attr("expand");
    var flid = $(o).attr("flid");
    var tableClass = ".t-table"+flid;
    if(expand == "true"){
        $(o).attr("src","resources/images/evalution/coll1.png");
        $(o).attr("expand","false");
        $("span[parentid='"+flid+"']").hide();
        $(tableClass).hide();

        handleImportantProject(o,true);

    }else{
        $(o).attr("src","resources/images/evalution/expand1.png");
        $(o).attr("expand","true");
        $("span[parentid='"+flid+"']").show();
        $(tableClass).show();
        var a = "span.t-table"+flid;
        $(a).css("display","block");

        handleImportantProject(o,false);
    }
}

/**
 * 处理重点项目展示与隐藏
 * @param o
 */
function handleImportantProject(o,isHide){
    var id = $(o).attr("flid");
    var docId = "#import-"+id;
    var content = "";
    $("[parentid='"+id+"'] .important-project").each(function () {
        var value = getComponentValue(this);
        if (value == null ||value == ""){
            content += " ";
        }else{
            content += " " + value + " ";
        }

    });

    if (!isHide){
        $(docId).html("");
    }else{
        $(docId).html(content);
    }
    return ;
}

/*********************************菜单功能处理*******************************/
/**
 * 绑定input表单的事件
 */
function bindingEvent(){
    bindingRightClickEvent();
}

/**
 * 绑定右击事件
 */
function bindingRightClickEvent(){
    // 护士签名，护士长签名
    $("#nurse-sign").bind("mousedown",function(e){
        if (e.which == 3){
            $('#menu').menu('show', {
                left: e.pageX,
                top: e.pageY
            });

            // 根据项目类型确定绑定的菜单功能
            if (go_status == 1){
                $('#menu').menu('disableItem', $('#m-blyy'));
                $('#menu').menu('disableItem', $('#m-ywgl'));
                $('#menu').menu('enableItem', $('#m-bdqm'));
            }else{
                $('#menu').menu('disableItem', $('#m-blyy'));
                $('#menu').menu('disableItem', $('#m-ywgl'));
                $('#menu').menu('enableItem', $('#m-bdqm'));
            }

            // 记录当前右击的input 的id ，方便后续的处理
            go_rightClickInputId = "nurse-sign";
            return false;
        }else if (e.which == 1){
        }
    });
    // 护士长签名
    $("#pnurse-sign").bind("mousedown",function(e){
        if (e.which == 3){
            $('#menu').menu('show', {
                left: e.pageX,
                top: e.pageY
            });

            // 根据项目类型确定绑定的菜单功能
            if (go_status == 1){
                $('#menu').menu('disableItem', $('#m-blyy'));
                $('#menu').menu('disableItem', $('#m-ywgl'));
                $('#menu').menu('enableItem', $('#m-bdqm'));
            }else{
                $('#menu').menu('disableItem', $('#m-blyy'));
                $('#menu').menu('disableItem', $('#m-ywgl'));
                $('#menu').menu('enableItem', $('#m-bdqm'));
            }

            // 记录当前右击的input 的id ，方便后续的处理
            go_rightClickInputId = "pnurse-sign";
            return false;
        }else if (e.which == 1){
        }
    });

    for (var i = 0 ; i < go_rightClickProjects.length; i++){
        var id = "#" + go_rightClickProjects[i];
        /*$(id).textbox('textbox').bind("keypress",function(e){
            console.log(e);
        });*/

        $(id).textbox('textbox').bind("mousedown",function(e){
            if (e.which == 3){
                $('#menu').menu('show', {
                    left: e.pageX,
                    top: e.pageY
                });

                // 根据项目类型确定绑定的菜单功能
                var inputProtype = easyUiProtypeAttr($(this));
                if (inputProtype != null){
                    var xmlb = inputProtype.attr("xmlb");
                    if (go_status == 1){ // 可修改状态
                        if (xmlb == 4){
                            // 查找“打开”项并禁用它
                            $('#menu').menu('disableItem', $('#m-blyy'));
                            $('#menu').menu('disableItem', $('#m-ywgl'));
                            $('#menu').menu('enableItem', $('#m-bdqm'));
                        } else if (xmlb == 6){
                            $('#menu').menu('disableItem', $('#m-blyy'));
                            $('#menu').menu('enableItem', $('#m-ywgl'));
                            $('#menu').menu('disableItem', $('#m-bdqm'));
                        } else{
                            $('#menu').menu('enableItem', $('#m-blyy'));
                            $('#menu').menu('disableItem', $('#m-ywgl'));
                            $('#menu').menu('disableItem', $('#m-bdqm'));
                        }
                    }else{
                        $('#menu').menu('disableItem', $('#m-blyy'));
                        $('#menu').menu('disableItem', $('#m-ywgl'));
                        $('#menu').menu('disableItem', $('#m-bdqm'));
                    }

                    // 记录当前右击的input 的id ，方便后续的处理
                    go_rightClickInputId = inputProtype.attr("id");
                }

                return false;
            }else if (e.which == 1){
            }
        });
    }
}

/**
 * 菜单处理
 * @param item
 */
function menuHandler(item){
    //console.log(item);
    if (item.id == "m-ywgl"){
        var glyw = $("#"+go_rightClickInputId).attr("glyw");
        var glxm = $("#"+go_rightClickInputId).attr("glxm");
        var dzbdjl = $("#"+go_rightClickInputId).attr("dzbdjl");
        var mode = 1 ;
        if (go_status == 1){
            if (checknull(dzbdjl) || dzbdjl == 0){
                mode = 1;
            }else{
                mode = 2;
            }
        }else{
            mode = 3;
        }

        var param = {
            flag:'union',
            type:glyw,
            item:glxm,
            mode:mode,
            recordid:dzbdjl
        }
        param = JSON.stringify(param);
        invokePowerBuilder(param);
    }else if (item.id == "m-blyy"){
        var param = {
            flag:"quote",
            type:"1"
        };
        param = JSON.stringify(param);
        invokePowerBuilder(param);
    }else if (item.id == "m-bdqm"){
        // 确定是【签名】【取消签名】
        var userName = $("#"+go_rightClickInputId).val();
        var mode = "sign";
        if (checknull(userName)){
            mode = "sign";
        }else{
            mode = "unsign";
        }
        var param = {
            flag:'sign',
            signdocid:"hsqm",
            mode:mode
        };

        // 签名状态，先判断护士是否签名
        if (mode == "sign"){
            var docId = $("#"+go_rightClickInputId).attr("id");
            if (docId == "pnurse-sign"){
                var hsqmxm = $("#nurse-sign").val();
                if (checknull(hsqmxm)){
                    $.messager.confirm('确认','护士还未签名,您确定审阅吗?\n审阅之后此表单将不能修改!',function(r){
                        if (r){
                            param = JSON.stringify(param);
                            invokePowerBuilder(param);
                        }
                    });
                }else{
                    param = JSON.stringify(param);
                    invokePowerBuilder(param);
                }
            }else{
                param = JSON.stringify(param);
                invokePowerBuilder(param);
            }
        }else if(mode == "unsign"){
            $.messager.confirm('确认','您确定要取消签名吗？',function(r){
                if (r){
                    param = JSON.stringify(param);
                    invokePowerBuilder(param);
                }
            });
        }

    }

    /**
     * 病历引用处理
     */
    function medicalRecordQuoteHandler(){

    }
    /**
     * 业务关联处理
     */
    function businessReflactHandler(){

    }
    /**
     * 签名处理
     */
    function signHandler(){

    }
}

/**
 * 获取当前组件所对应的原生input元素
 * @param JqueryObj
 * @constructor
 */
function easyUiProtypeAttr(JqueryObj)
{
    var parent = JqueryObj.parent();
    var input = parent.siblings("input")[0];
    // ceshi
    /*var targetInput = $(span).find("input:first");
    if (targetInput) {
        $(targetInput).attr(PropertyName, PropertyValue);
    }*/
    return $(input);
}

/*********************************菜单功能处理*******************************/

//function right
/**
 * 签名操作 {flag:'sign',signDocId:23}
 * @param o
 */
function sign_test(o){
    go_isPowerBuilderInvake = false;
    sign(o);
}

function api_sign(o){
    go_isPowerBuilderInvake = true;
    sign(o);
}

function sign(o){
    var id = $(o).attr("id");
    go_rightClickInputId = id;
    var param ={
        flag:'sign',
        signdocid:id,
        mode:"sign"
    };
    var params = JSON.stringify(param);

    if (go_isPowerBuilderInvake){
        CFWCefWebFunction(params);
        //comfirmPicture();
    }
}

function comfirmPicture(){
    $("#z-sign-ok").click();
}


/**
 * api-一键设置默认值
 * @param str
 */
function setTextBoxDefaultValueTest(){
    go_isPowerBuilderInvake = false;
    var s = "/";
    api_setTextBoxDefaultValue(s);
}

function setTextBoxDefaultValue(str){
    // 判断状态
    if (go_status == 2){
        showAlertMessager("提示信息","查看状态禁止修改!")
        return ;
    }
    $("input[kjlx='textbox']").each(function(){
        setTextBoxValue(this);
    });

    $("input[kjlx='mul-textbox']").each(function(){
        setTextBoxValue(this);
    });

    function setTextBoxValue(o){
        var value =  $(o).textbox("getValue");
        if (checknull(value)){
            $(o).textbox("setValue",str);
        }
    }
}

function checknull(value){
    if (value == null || value == ""){
        return true;
    }else{
        return false;
    }
}

/*************************************打印***********************************/

function print(){
    /*var obj = document.getElementById('evaluate-main');
    var prnhtml = obj.innerHTML;
    $('#trackedit_dialog').printArea(prnhtml);*/
    //window.print();

    var lodop = getCLodop();
    //lodop.SET_PRINT_PAGESIZE();
    //lodop.PRINT();
    //var html = $("#evaluate-main").innerHTML;
    // 设置预览窗口大小
    //lodop.SET_PREVIEW_WINDOW(0,0,0,860,540,"");
    // 设置边距
    //lodop.ADD_PRINT_RECT("1%","1%","97%","97%",0,1);
    // 设置右边距和下边距
    lodop.PRINT_INIT("打印评估单");
    if (printContentPrepare()){
        lodop.ADD_PRINT_HTM("3mm",26,"RightMargin:3mm","BottomMargin:3mm",$("#printSpace").html());
        //lodop.ADD_PRINT_HTM(0,0,"80%","80%",$("#evaluate-main").html());

        lodop.SET_PRINTER_INDEX("Adobe PDF");
        lodop.PREVIEW();
       // lodop.PRINT();
    }

}

/**
 * 打印内容准备
 */
function printContentPrepare(){
    var html = "";
    if (go_evalutionStyle == null) return false;
    if (go_jlxh == null) return false;
    $(".print-content").html("");
    createPrintStyle(go_evalutionStyle);
    return true;
}

/**
 * 创建评估样式
 * @param styleData
 */
function createPrintStyle(styleDatas){
    if (styleDatas == null || styleDatas.length <= 0){
        return ;
    }
    var styleData = styleDatas[0];
    var ysmc = styleData.YSMC ? styleData.YSMC : "评估单";
    var title = "创业数字医院"+ ysmc;
    displayPrintTitle(go_jgmc);
    displayPrintTitile2(ysmc);
    displayPrintSign();
    displayPrintContent(styleData);
}

function displayPrintSign(){
    var write_name = $("#write-name").text();
    var write_time = $("#write-time").text();
    var nurse_sign_name = $("#nurse-sign").val();
    var pnurse_sign_name = $("#pnurse-sign").val();

    $("#write-print-name").text(write_name);
    $("#write-print-time").text(write_time);
    $("#nurse-print-sign").text(nurse_sign_name);
    $("#pnurse-print-sign").text(pnurse_sign_name);
}

function displayPrintTitle(title){
    $("#print-title1").text(title);
}

function displayPrintTitile2(title){
    $("#print-title2").text(title);
}

function displayPrintContent(styleData){
    //console.log(styleData);
    var projects = styleData.projects;
    if (projects == null || projects.length <=0)
        return;

    var rootCount = projects.length;
    var html = "";
    $(".print-content").html(html);
    for (var i = 0 ; i < rootCount; i++){
        var project = projects[i];
        // root级别项目，默认都是分类标签，固维护时需要控制
        createPrintHtml(0,null,null,null,project);
    }

    // 互斥和显示处理
    $("#print-content input[kjlx='checkbox'][xmlb='7']").each(function(){
        //handleDisplay(this);
    });

    $.parser.parse($('.print-content'));

    function handleDisplay(o){
        fireEvent(o);
    }
    // 写入基本信息
    //handleBaseInfo();
}

/**
 // 可提前将一些项目的属性信息，写入标签中，方便后续处理
 1.由父项目确定的属性
 (1)控件类型 XJKJLX
 1,单行输入2.多行输入 3,单项选择 4,多项选择 5.下拉列表 6.标签显示 7.表格 9.无
 (2)下级默认展开
 (3)下级互斥

 2.由自身确定的属性
 (1)数据类型 (2)项目扩展 (3)项目类型 (4)数据格式 (5)换行标志 (6)项目名称 (7)前置文本 (8)后置文本
 * @param parent
 * @param it
 */
function createPrintHtml(typeId,great_grandparent,grandparent,parent,it){
    // 曾祖父节点属性
    var pppId = null;
    var pppXjkjlx = null;
    // 祖父节点属性
    var ppId = null;
    var ppXjkjlx = null;
    // 父节点属性
    var pId = null;
    var pName = null;
    var pXjkjlx = null;
    var pXjmrzk = null;
    var pXjhc = null;
    // 本节点属性
    var dataType = null;
    var projectExt = null;
    var projectType = null;
    var hrFlag = null;
    var name = null;
    var beforeText = null;
    var afterText = null;
    var displayLen = null;
    var editAble = true;
    var children = it.children;
    var id = null;
    var dataFormattor = null;
    var childExpand = null;
    var dataUpBound = 100;
    var dataDownBound = 0;
    var precision = 0;
    var extend = null;
    var importantProject = 0 ;
    var leftMargin = 0;

    var typeId = typeId; // 用来记录当前分类项目id

    if (parent != null){
        pId = parent.XMXH;
        pXjkjlx = parent.XJKJLX;
        pXjhc = parent.XJHC;
        pXjmrzk = parent.XJZK;
        pName = parent.XMMC;
    }else{
        pId = 0 ; // pid 0
        pXjkjlx = 9 ; // 无
        pXjmrzk = 1 ; // 展开
        pXjhc =0; // 不互斥
        pName = ""; // 无
    }

    if (grandparent != null){
        ppId = grandparent.XMXH;
        ppXjkjlx = grandparent.XJKJLX;
    }else{
        ppId = 0;
        ppXjkjlx = 9;
    }

    if (great_grandparent != null){
        pppId = great_grandparent.XMXH;
        pppXjkjlx = great_grandparent.XJKJLX;
    }else{
        pppId = 0;
        pppXjkjlx = 9;
    }

    /*********************************************************************/
    dataType = it.SJLX?it.SJLX:2; // 数据类型默认为2 字符型
    dataFormattor = it.SJGS?it.SJGS:""; // 数据格式，默认为空
    projectExt = it.XMKZ?it.XMKZ:""; // 项目扩展未设置项目扩展的，默认为空
    projectType = it.XMLB?it.XMLB: 7; // 项目类别未设置项目类别的 ，默认为常规项目
    hrFlag = it.HHBZ?it.HHBZ:0;// 换行标志默认0 不换行
    name = it.XMMC?it.XMMC:""; // 项目名称默认""
    beforeText = it.QZWB?it.QZWB:""; // 前置文本默认为空
    afterText = it.HZWB?it.HZWB:""; // 后置文本默认为空
    displayLen = it.XSCD?it.XSCD:6; // 显示长度默认为120px
    displayLen = displayLen * 8;
    id = it.XMXH?it.XMXH:0;
    childExpand = it.XJZK; // 下级展开标志，默认展开
    dataUpBound = it.SJSX?it.SJSX:dataUpBound; // 数据上限，只针对number控件
    dataDownBound = it.SJXX?it.SJXX:dataDownBound; // 数据下限，只针对number控件
    precision = it.XMKZ?it.XMKZ:precision; // 项目扩展，精度
    extend =it.XMKZ?it.XMKZ:""; // 项目扩展
    importantProject = it.ZDXM?it.ZDXM:0; // 重点项目
    // 左边距 项目本身属性
    leftMargin = it.XMZH?it.XMZH:0; // 左边距（使用项目组号字段保存）
    leftMargin = leftMargin +"px";
    var groupName = "group-"+pId;
    var pDocId = null; // 用于记录html插入点
    var html = "";     // 整体插入数据
    pDocId = (pId == 0)?".print-content":"span[parentprintid="+pId+"]";

    /*************************处理表格类型*******************************/
    // 针对表格类型的项目，只处理3级，超过3级的忽略；不足3级如何处理
    // 当当前项目父节点的结束处理标志为1时，不在处理
    // 只有当曾祖父节点下级控件类型为表格类型时，会打上结束处理标志
    if (parent != null){
        if (parent.endFlag == 1){
            return ;
        }
    }

    var xjkjlx = it.XJKJLX;
    if (xjkjlx == 7){
        it.endFlag = 1;
        return ;
    }

    if (xjkjlx == 7 || pXjkjlx == 7 || ppXjkjlx == 7){
        return ;
    }
    /************************************************************/
    // 1.确定控件类型
    if (projectType == 1 || projectType == 2){
        pXjkjlx = 6; // 标签
    } else if (projectType == 3){
        pXjkjlx = 1; // 文本
        editAble = false;
    } else if (projectType == 5) {
        pXjkjlx = 1; // 文本
        editAble = false;
    }

    // 2.开始组装html
    var className = "";
    var childClassName = "";
    var inputClassName="";
    var eventName = "fireEvent(this)";

    // 默认展开标志,当前项目的子项目的属性
    if (childExpand == 0){
        childClassName += " content-span-cell-none"
    }else{
        childClassName += ""
    }

    // 分类标签 特殊处理
    if (projectType == 1){
        typeId = id;
        // 分类标签 展开折叠功能
        html += '<hr/>';

        className = "class-label";
    }else if (projectType == 2){
        //className = "";
    }

    // 换行标志
    if (hrFlag == 1){
        if (pId != 0){
            html += '<br/>';
        }
        className += " content-span-cell-inline";
    }else{
        className += " content-span-cell-inline";
    }

    // 分类标签 特殊处理
    if (projectType == 1){
        typeId = id;
        className = "class-label";
    }else if (projectType == 2){
        //className = "";
    }

    // 重点项目
    var labelClassName ="";
    if (importantProject == 1){
        labelClassName = "important-project";
    }
    // 添加Html
    // 标签显示
    var _inputType = "";
    html += '<span class="'+className+'" xmid="'+id+'">';
    var dataoptions = "";
    if (pXjkjlx == 6){ // label类型不进行长度控制、前置文本和后置文本
        inputClassName += " type-label";
        var projectTypeStyle = "";
        if (projectType == 1){
            projectTypeStyle = "font-size: 16px;\n" +
            "    font-family: 宋体;\n" +
            "    font-weight: bold;" +
                "  margin-left:" + leftMargin + ";"
        }
        html += '<label style="'+projectTypeStyle+'" class="'+inputClassName+' '+labelClassName+'" printid='+id+'>'+name +'</label><span id="import-'+id+'" style="font-size: 11px;font-weight: bold;color: black;"></span>';
    }else if (pXjkjlx == 1){ // 单行编辑框
        // 单行编辑框 根据数据类型分为3中 ; 1数字、2文本、3日期
        if (dataType == 2){
            inputClassName += " easyui-textbox type-textbox";
            _inputType = "textbox";
        }else if (dataType == 1){
            inputClassName += " easyui-numberbox type-numberbox";
            _inputType = "numberbox";
        }else if (dataType == 3){ // 通过数据格式确定是否datebox /datetimebox
            var _inputClass =validDateBoxType(dataFormattor);
            inputClassName +=  " easyui-" + _inputClass + " type-" + _inputClass;
            _inputType = _inputClass;
        }
        // 获取数据
        var value = "";
        if (_inputType =="textbox"){
            value = $("#"+id).textbox("getValue");
        }else if(_inputType == "numberbox"){
            value = $("#"+id).numberbox("getValue");

        }else if (_inputType == "datebox"){
            value = formatterYMD($("#"+id).datebox("getValue"));

        }else if (_inputType == "datetimebox"){
            value = formatterFullDate(new Date($("#"+id).datetimebox("getValue")));
        }
        html += '<label style="padding-right: 6px;margin-left: '+leftMargin+'" class="'+labelClassName+'">'+beforeText+'</label>' +
            '<label  class="'+labelClassName+'" ' +
            'printid='+id+' ' +
            'kjlxp="'+_inputType+'" ' +
            'sjxmp="'+pId+'" ' +
            'sjxmmcp="'+pName+'" ' +
            'hcbzp="'+pXjhc+'"' +
            //'style="width: '+displayLen+'px;" ' +
            'style="height: 17px; border-bottom: 1px black solid;display: inline-block; width: '+displayLen+'px;" ' +
            '>'+value+'</label>' +
            '<label  style="padding-right: 6px;" class="content-span-cell-after-label '+labelClassName+'">'+afterText+'</label>';
    }else if (pXjkjlx == 2){ // 多行编辑框
        inputClassName += " easyui-textbox type-mul-textbox";
        _inputType = "mul-textbox";
        var value = "";
        value = $("#"+id).textbox("getValue");
        html += '<label style="padding-right: 4px;margin-left: '+leftMargin+'" class="'+labelClassName+'">'+beforeText+'</label>' +
            '<input class="'+labelClassName+'" ' +
            'printid='+id+' ' +
            'kjlxp="'+_inputType+'" ' +
            'sjxm=p"'+pId+'" ' +
            'sjxmmcp="'+pName+'" ' +
            'hcbzp="'+pXjhc+'' +
            '"style="width: '+displayLen+'px;height:50px"" value="'+value+'"/>' +
            '<label style="padding-right: 6px;" class="content-span-cell-after-label '+labelClassName+'">'+afterText+'</label>';
    }else if (pXjkjlx == 3){ // 单项选择 :增加样式的原因，解决谷歌浏览器下checkbox与文本不对齐的问题
        inputClassName += " type-checkbox";
        _inputType = "checkbox";
        var checked = $("#"+id).attr("checked");
        var _h ="";
        if (checked){
            _h = "checked="+checked
        }
        html += '<label style="padding-right: 4px;margin-left: '+leftMargin+'" class="'+labelClassName+'">'+beforeText+'</label>' +
            '<label class="'+labelClassName+'"><input type="checkbox" ' +
            'name="'+groupName+'" ' +
            'class="'+inputClassName+' '+labelClassName+'" ' +
            'printid='+id+' ' +
            'kjlxp="'+_inputType+'" ' +
            'sjxmp="'+pId+'" ' +
            'sjxmmcp="'+pName+'" ' +
            'hcbzp="'+pXjhc+'" ' +
            'dxbzp=1 ' +
            'xjzkbzp="'+childExpand+'" ' +
            ' ' + _h + ' '+
            ' style="vertical-align:middle; margin-top:-2px; margin-bottom:1px;" ' +
            'onclick="'+eventName+'"/>'+name+'</label>' +
            '<label style="padding-right: 6px;" class="content-span-cell-after-label '+labelClassName+'">'+afterText+'</label>';
    }else if (pXjkjlx == 4){ // 多项选择 :增加样式的原因，解决谷歌浏览器下checkbox与文本不对齐的问题
        inputClassName += " type-checkbox";
        _inputType = "checkbox";
        var checked = $("#"+id).attr("checked");
        var _h ="";
        if (checked){
            _h = "checked="+checked
        }
        html += '<label style="padding-right: 4px;margin-left: '+leftMargin+'" class="'+labelClassName+'">'+beforeText+'</label>' +
            '<label class="'+labelClassName+'"><input type="checkbox" ' +
            'name="'+groupName+'" ' +
            'class="'+inputClassName+' '+labelClassName+'" ' +
            'xjzkbzp='+childExpand+' ' +
            'printid='+id+' ' +
            'sjxmp="'+pId+'" ' +
            'sjxmmcp="'+pName+'" ' +
            'kjlxp="'+_inputType+'" ' +
            'hcbzp="'+pXjhc+'"' +
            ' ' + _h + ' '+
            ' style="vertical-align:middle; margin-top:-2px; margin-bottom:1px;" onclick="'+eventName+'"/>'+name+'</label>' +
            '<label style="padding-right: 6px;" class="content-span-cell-after-label '+labelClassName+'">'+afterText+'</label>';
    }else if (pXjkjlx == 5){ // 下拉选择
        inputClassName += " easyui-combobox type-combobox";
        _inputType = "combobox";
        var value = "";
        value = $("#"+id).combobox("getText");
        html += '<label style="padding-right: 4px;margin-left: '+leftMargin+'" class="'+labelClassName+'">'+beforeText+'</label>' +
            '<label class="'+labelClassName+'" ' +
            'printid='+id+' ' +
            'kjlxp="'+_inputType+'" ' +
            'sjxmp="'+pId+'" ' +
            'sjxmmcp="'+pName+'" ' +
            'hcbzp="'+pXjhc+'" ' +
            'style="height: 17px; border-bottom: 1px black solid;display: inline-block; width: '+displayLen +'px;" ' +
            '>'+value+'</label>' +
            '<label style="padding-right: 6px;" class="content-span-cell-after-label '+labelClassName+'">'+afterText+'</label>';

    }

    if (children != null && children.length >0){
        html += '<span parentprintid='+id+' class='+childClassName+'></span>';
    }
    html +='</span>'

    $(pDocId).append(html);

    /************************************************************/
    wrapperHtml();

    var childCount = children.length;
    if (childCount != null || childCount >0){
        for (var i = 0 ; i < childCount;i++){
            var childo = children[i];
            createPrintHtml(typeId,grandparent,parent,it,children[i]);
        }
    }
}

/**
 * 项目数据关联
 * 调用关联数据窗口:{flag:'union',type:2/5,item:2222,mode:1/2/4,recordid:1111}
 * 备注:mode(1新增,2修改,4查看),type(2风险评估,5生命体征),item(项目扩展的后半部分值),recordid(type为2时IENR_FXPGJL.PGXH,TYPE为5时BQ_SMTZ.CJH)
 */
function projectDataUnion(){

}
/**********************************调用Powerbuilder方法*****************************************/
/**
 * 反调用PB接口
 */
function invokePowerBuilder(paramStr){
    // 病历引用
    var params = {
        flag:"quote",
        code:"200",
        text:"尼玛啊啊啊啊 啊啊"
    };

    params = {
        flag:'sign',
        code:'200',
        msg:'签名成功',
        usercode:'4068',
        username:'邢海龙',
        signdocid:'1'
    };

    // TODO 修改成调用PB的方法
    //pbCallBack(params);
    CFWCefWebFunction(paramStr);
}

/************************************************End*******************************************/


/**********************************Powerbuilder回调页面统一接口*****************************************/
/**
 * pb回调函数
 * @param o 参数，JSON格式，内容不固定
 */
function pbCallBack(o){
    if (checknull(o))
        return ;
    if (checknull(o.code))
        return ;
    if (checknull(o.flag))
        return ;

    var code = o.code;
    var flag = o.flag;
    if(code == "200" || code == 200){
        // 签名返回值
        if(flag == "sign"){
            //showAlertMessager("",flag);
            signCallBack(o);
        // 病历引用
        }else if (flag == "quote"){
            quoteCallBack(o);
        // 数据引用
        }else if (flag == "union"){
            unionCallBack(o);
        }else if (flag == "changeconfirm"){
            changeConfirm(o);
        }
    }
}

/**
 * 状态确认
 * @param o
 */
function changeConfirm(o){
    var changed = o.changed;
    if (changed == "false"){
        go_changed = false;
    }else if (changed == "true"){
        go_changed = true;
    }
}
/**
 * 数据关联回调处理：将结果写入页面
 * 返回格式：{flag:'union',code:200,type:2/5,recordid:1}
 */
function unionCallBack(o){
    var type = o.type;
    var recordid = o.recordid;
    $("#"+go_rightClickInputId).attr("dzbdjl",recordid);
    getRelationDatas([type],recordid)
}

/**
 * 病历引用回调处理:将结果写入页面
 * @param o
 */
function quoteCallBack(o){
    if(checknull(o))
        return ;
    var text = o.text;
    if(go_rightClickInputId > 0 && !checknull(text)){
        $("#"+go_rightClickInputId).textbox("setValue",text);
    }
}

/**
 * PB签名回调处理：将签名信息写入页面，等待保存
 * @param o
 */
function signCallBack(o){
    //showAlertMessager("",JSON.stringify(o));
    var code = o.code;
    var flag = o.flag;
    var msg = o.msg;
    var usercode = o.usercode;
    var username = o.username;
    //var signdocid = o.signdocid;

    //showAlertMessager("","username:"+username + "\r\nusercode:"+usercode + "\nsigndocid:" +signdocid)
    if (checknull(usercode))
        return ;

    if (checknull(username))
        return ;

    // 主表单 护士签名 护士长签名
    var id = $("#"+go_rightClickInputId).attr("id");
    var userName = $("#"+go_rightClickInputId).val();
    var data = {};
    if (checknull(userName)){
        if (id == "nurse-sign" || id == "pnurse-sign"){
            $("#"+go_rightClickInputId).val(username);
            $("#"+go_rightClickInputId).attr("usercode",usercode);

            // 调用签名服务
            var signWho = "";
            if (id == "nurse-sign"){
                signWho = "1"; // 护士签名
            }else{
                signWho = "2"; // 护士长签名
            }
            data = {
                mode:"sign",
                jlxh:go_jlxh,
                signWho:signWho,
                signUserCode:usercode,
                signUserName:username
            };
        }
    }else{
        if (id == "nurse-sign" || id == "pnurse-sign"){
            $("#"+go_rightClickInputId).val("");
            $("#"+go_rightClickInputId).attr("usercode","");
            // 调用取消签名服务
            var signWho = "";
            if (id == "nurse-sign"){
                signWho = "1"; // 护士签名
            }else{
                signWho = "2"; // 护士长签名
            }
            data = {
                mode:"unsign",
                jlxh:go_jlxh,
                signWho:signWho,
                signUserCode:usercode,
                signUserName:username
            };
        }
    }

    // 调用签名服务
    if (checknull(go_jlxh)){
        showAlertMessager("提示信息","请先保存后进行签名");
        return ;
    }

    $.ajax({
        type : "post",
        url : "mobile/evaluation/v56_update1/sign",
        data:data,
        beforeSend : function(req) {
            req.setRequestHeader("Accept", "application/json");
        },
        success : function(response) {
            if (response.ReType == 0) {
                // 权限控制
                if (!checknull(go_record)){
                    if (data.signWho == 2){ // 护士长
                        if (data.mode == "sign"){  // 签名
                            go_record.SYZT = 1;
                        }else if (data.mode == "unsign"){ // 取消签名
                            go_record.SYZT = 0;
                        }
                    }

                    if (data.mode == "sign"){
                        showOkMessager("提示信息","签名成功!")
                    }else if (data.mode == "unsign"){
                        showOkMessager("提示信息","取消签名成功!")
                    }
                    authorityControll();
                    // 通知PB
                    var param = {
                        flag:'common',
                        mode:'updatesign'
                    };
                    var param = JSON.stringify(param);
                    invokePowerBuilder(param);
                }

                $.messager.progress('close');
            } else {
                showAlertMessager("错误信息", response.Msg);
                $.messager.progress('close');
            }
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            showAlertMessager('提示', textStatus + ":" + XMLHttpRequest.responseText);
            $.messager.progress('close');
        }
    })

}
/************************************************End*******************************************/

/**
 * 权限控制
 */
function authorityControll(){
    // 图章先全部清除
    handlerStatusIcon(0);

    // 1.确定状态及图章的显示
    if (!checknull(go_record)){
        if (go_jlxh == 0){
            go_status = 1;
        }else{
            var SYZT = go_record.SYZT ;
            if (SYZT == 1){
                go_status = 2;
                handlerStatusIcon(3);
            }else{
                // 未审阅的状态，判断当前是否已经签名，签名的护士与当前的护士是否为同一个
                if (go_isControllOwener){
                    var qmhs = $("#nurse-sign").attr("usercode");
                    if (!checknull(qmhs)){
                        if (qmhs != go_userId){
                            go_status = 2;
                            handlerStatusIcon(2);
                        }else{
                            go_status = 1;
                        }
                    }else{
                        go_status = 1;
                    }
                }else{
                    go_status = 1;
                }
            }
        }

    }else{
        go_status = 1;
    }

    // 2.处理控件的状态
    if (go_status != 2){ // 1:可修改状态 2:不可修改状态
        handlerControllsDisabled(false);
        handlerStatusIcon(1);
        return ;
    }

    // 1.所有右击菜单不可用
    // 2.所有控件禁用状态
    // 3.签名控件
    handlerControllsDisabled(true);

    // status [1:编辑 2：非本人操作 3：完成]
    function handlerStatusIcon(status){
        if (status == 0){
            $(".zsign .panel").remove();
            $(".ok img").remove();
        }
        if (status == 3){
            if (go_isSupportPictrue){
                signobj =$(".header").zSign({ img: 'resources/images/evalution/checked.png',
                    width:120,
                    height:120,
                    offset:8});
                document.getElementById("z-sign-ok").click();
            }
        }

        if (status == 2){
            if (go_isSupportPictrue){
                signobj =$(".header").zSign({ img: 'resources/images/evalution/disable.png',
                    width:120,
                    height:120,
                    offset:8});
                document.getElementById("z-sign-ok").click();
            }
        }

        if (status == 1){
            if (go_isSupportPictrue){
                signobj =$(".header").zSign({ img: 'resources/images/evalution/editable.png',
                    width:120,
                    height:120,
                    offset:8});
                document.getElementById("z-sign-ok").click();
            }
        }
    }
    function handlerControllsDisabled(status){
        //handlerSignControll(status);
        // 只有项目类别为7：常规项目，才受控制，其他类别有自己的控制属性
        $("input[kjlx='textbox'][xmlb='7']").each(function(){
            handlerTextBoxDisable(this,status);
        });

        $("input[kjlx='mul-textbox'][xmlb='7']").each(function(){
            handlerTextBoxDisable(this,status);
        });

        $("input[kjlx='numberbox'][xmlb='7']").each(function(){
            handlerNumberboxDisable(this,status);
        });

        $("input[kjlx='datebox'][xmlb='7']").each(function(){
            handlerDateBoxDisable(this,status);
        });

        $("input[kjlx='datebox'][xmlb='3']").each(function(){
            handlerDateBoxDisable(this,status);
        });

        $("input[kjlx='datetimebox'][xmlb='7']").each(function(){
            handlerDateTimeBoxDisable(this,status);
        });

        $("input[kjlx='datetimebox'][xmlb='3']").each(function(){
            handlerDateTimeBoxDisable(this,status);
        });

        $("input[kjlx='radiobox'][xmlb='7']").each(function(){
            handlerCheckBoxDisable(this,status);
        });

        $("input[kjlx='checkbox'][xmlb='7']").each(function(){
            handlerCheckBoxDisable(this,status);
        });

        $("input[kjlx='combobox'][xmlb='7']").each(function(){
            handlerComboboxDisable(this,status);
        });

    }

    function handlerSignControll(status){
        $("#nurse-sign").attr("disabled",status);
        $("#pnurse-sign").attr("disabled",status);

    }

    // 4.datagrid toolbar 禁用
    /*for (var i = 0 ; i < go_groupids.length; i++){
        var id = "#"+go_groupids[i];
        $(id).datagrid({
            toolbar: []
        });
    }*/

    function handlerTextBoxDisable(o,status){
        $(o).textbox("readonly",status);
    }

    function handlerNumberboxDisable(o,status){
        $(o).numberbox("readonly",status);
    }

    function handlerDateBoxDisable(o,status){
        $(o).datebox("readonly",status);
    }

    function handlerDateTimeBoxDisable(o,status){
        $(o).datetimebox("readonly",status);
    }

    function handlerCheckBoxDisable(o,status){
        $(o).attr("disabled",status);
    }

    function handlerComboboxDisable(o,status){
        $(o).combobox("readonly",status);
    }
}

/**
 * 新建和打开评估单记录时，清空签名信息
 */
function clearSignInfo(){
    $("#write-name").text("");
    $("#write-time").text("");
    $("#nurse-sign").val("");
    $("#pnurse-sign").val("");
}

function writeSignInfo(){
    $("#write-name").text(go_userName);
    $("#write-time").text(formatterFullDate(new Date(go_txsj)));
}

function getRelations(){
    var types = [2,3,5];
    getRelationDatas(types,0);
}
/**
 * 获取关联项目数据
 */
function getRelationDatas(types,cjzh){
    // 关联项目GLYW/GLXM/KJLX
    //var types = [2,3,5];
    // 1.组装获取数据入参
    var relations = [];
    for (var i = 0 ; i < types.length;i++){
        var _type = types[i];
        var relation = wrapperRelationParamByBussines(_type,cjzh);
        if (!checknull(relation)){
            if (relation.YWLBMX.length > 0){
                relations.push(relation);
            }
        }
    }
    console.log(relations);
    var data = JSON.stringify(relations);
    // 2.获取数据
    $.ajax({
        url : 'mobile/evaluation/v56_update1/get/relationdata',
        type : 'post',
        data : data,
        contentType : "application/json",
        dataType : "json",
        beforeSend : function(req) {
            //showProgressMessager('操作提示', '正在提交数据...', '');
        },
        success : function(response) {
            if (response.ReType == 0) {
                console.log(response);
                displayRelationData(response.Data);
            } else {
                showAlertMessager('关联数据获取失败!', response.Msg);
            }
        },
        complete : function(XMLHttpRequest, textStatus) {// 成功或失败都会调用
            $.messager.progress('close');
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            showAlertMessager('操作提示', XMLHttpRequest.responseText);
        }
    });

    // 3.写入数据
}

/**
 * 组装关联数据获取入参
 * @param bussines
 */
function wrapperRelationParamByBussines(bussines,cjzh){
    var _txsj;
    var _zyh;
    var _jgid;
    if (go_jlxh == 0){
        _txsj  = go_txsj;
        _zyh  = go_zyh;
        _jgid  = go_jgid;
    }else{
        if (!checknull(go_record)){
            _txsj = go_record.TXSJ;
            _zyh = go_record.ZYH;
            _jgid = go_record.JGID;
        }
    }
    if (checknull(go_evalutionStyle) || go_evalutionStyle.length <= 0 || checknull(_txsj) || checknull(_zyh) || checknull(_jgid))
        return null;

    var _ysxh = go_evalutionStyle[0].YSXH;
    var _yslx = go_evalutionStyle[0].YSLX;
    var relation = {
        YWLB:bussines,
        YSXH:_ysxh,
        YSLX:_yslx,
        TXSJ:parserDateTime(_txsj),
        ZYH:_zyh,
        JGID:_jgid,
        CJZH:cjzh,
        YWLBMX:[]
    };

    $("input[glyw='"+bussines+"']").each(function(){
        createDetail(this,relation);
    });

    return relation;

    function createDetail(o,relation){
        var glxmh = $(o).attr("glxm");
        var pgxmh = $(o).attr("id");
        var xmkjlx = $(o).attr("kjlx");
        var dzbdjl = $(o).attr("dzbdjl");
        var detail = {
            GLXMH:glxmh,
            PGXMH:pgxmh,
            XMKJLX:confirmControllTypeByStr(xmkjlx),
            DZBDJL:dzbdjl
        };
        relation.YWLBMX.push(detail);
    }
}

/**
 * 写入数据关联类项目值
 * @param datas
 */
function displayRelationData(datas){
    if (checknull(datas))
        return;
    if (datas.length <= 0)
        return;
    for (var i = 0 ; i < datas.length;i++){
        var data = datas[i];
        var xmxh = data.XMXH;
        var xmnr = data.XMNR;
        var kjlx = data.KJLX;
        var dzbdjl = data.DZBDJL;
        var dzlx = data.DZLX;
        var docId = "#" + xmxh;

        if (kjlx == 1 || kjlx == "1"){
            $(docId).textbox("setValue",xmnr);
        }else if (kjlx == 2|| kjlx == "2"){
            $(docId).numberbox("setValue",xmnr);
        }else if (kjlx == 3 || kjlx == "3"){
            $(docId).datebox("setValue",xmnr);
        }else if (kjlx == 4 || kjlx == "4"){
            $(docId).datetimebox("setValue",xmnr);
        }

        // 写入相关属性
        //var jq = easyUiProtypeAttr($(docId));
        $(docId).attr("DZLX",dzlx);
        $(docId).attr("DZBDJL",dzbdjl);
    }
}

/**
 * 切换背景图片，并设置背景颜色值
 */
function switchBackGroundColor(){
    $("#evaluate-main").css("background","");
    $(".footer").css("display","block");
}

function valueChanged(newValue,oldValue){
    // 自动写入值的阶段，不算值改变
    if (!go_isAutoSetValue){
        go_changed = true;
    }
}

function invokeCallBackStrFuntion(fun){
    eval(fun);
}
