/**
 * 公用JS，依赖jQuery、EasyUi
 */
/**
 * 通过‘,’连接两个字符串
 * @param str1 str2
 * @returns {*}
 */
function connectString1AndString2(str1,str2){
	var str;
	//str1 对象可能为null
	if(str1 == null){
		str = str2;
	}else{
		str = str1 + ',' + str2;
	}
	return str;
}

/**
 * 获取字符串字节数
 * @param str
 * @returns {*}
 */
function getStringByteLength(str){
	// 对象为null ,或空字符串
	if(str == null || str == ""){
		return 0;
	}
	// 非字符串对象
	if(typeof str != "string"){
		return 0;
	}

	var chars = str.match(/[^\x00-\xff]/ig);
	var bytes =  str.length + (chars == null ? 0 : chars.length);

	return bytes;
}

/**
 * 'yyyy-MM-dd'
 *
 * @param {*} value
 * @return {String}
 */
function formatterYMD(value) {
	if (value == null) {
		return "";
	}
	var dateStr = value.split(" ")[0];
	var date = new Date(dateStr);
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	var d = date.getDate();
	return y + '-' + (m < 10 ? ('0' + m) : m) + '-' + (d < 10 ? ('0' + d) : d);
}

/**
 * 解析日期为 yyyy-MM-dd
 * 
 * @param date
 * @returns {*}
 */
function parserYMD(date) {

	if (!date) {
		return null;
	}
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var day = date.getDate();

	var result = year + "-";

	if (month < 10) {
		month = "0" + month;
	}
	if (day < 10) {
		day = "0" + day;
	}
	result += month + "-" + day;
	return result;
}

/**
 * 日期类型或字符串类型日期变量格式化
 * 
 * @param date
 * @returns {String}
 */
function parserDateTime(date) {
	var returnValue;
	if (typeof date == 'string') {
		if (date) {
			returnValue = date.split('.')[0];
		}
	} else if (typeof date == "object") {
		if (date) {
			var year = date.getFullYear();
			var month = date.getMonth() + 1;
			var day = date.getDate();
			var hour = date.getHours();
			var minute = date.getMinutes();
			var second = date.getSeconds();

			if (month < 10) {
				month = "0" + month;
			}
			if (day < 10) {
				day = "0" + day;
			}

			if (hour < 10) {
				hour = "0" + hour;
			}
			if (minute < 10) {
				minute = "0" + minute;
			}

			if (second < 10) {
				second = '0' + second;
			} else if (second >= 100) {
				second = second.substr(1, 2);
			}

			returnValue = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
		}
	} else {
        var reg = /\D/;      //  \D代表非数字
        if(!reg.test(date) ){   // 返回true,代表在字符串中找到了非数字。
            var _date = new Date(date);
            returnValue = formatterFullDate(_date);
        }else{
            returnValue = "";
		}

	}
	return returnValue;
}

/**
 * 'yyyy'
 *
 * @param {String} value
 * @return {*}
 */
function formateYear(value) {

	var dateStr = value.split(" ")[0];
	var date = new Date(dateStr);
	return date.getFullYear();
}

/**
 * 'yyyy-MM-dd HH:mm:ss'
 *
 * @param {*} value
 * @return {*}
 */
function formateDateTime(value) {

	if (value) {
		value = value.split('.')[0];
	}
	return value;

}

/**
 * 弹出提示对话框
 *
 * @param {String} title
 * @param {String} msg
 */
function showAlertMessager(title, msg) {

	$.messager.alert(title, msg);
}

function showOkMessager(title, msg) {
	showMessager(title, msg, 2000)
}

function showMessager(title, msg, timeout) {

	$.messager.show({
		title : title,
		msg : msg,
		showType : 'show',
		timeout : timeout
	});
}

function showProgressMessager(tilte, msg, text) {

	$.messager.progress({
		title : tilte,
		msg : msg,
		text : text
	});
}

/**
 * used by datatimespinner
 *
 * @param {Date} date
 * @return {*}
 */
function formatterY(date) {
	if (!date) {
		return '';
	}
	return date.getFullYear();
}

/**
 * used by datatimespinner
 * 
 * @param {int} s
 * @return {Date}
 */
function parserY(s) {

	if (!s) {
		return null;
	}

	var y = parseInt(s);
	var date = new Date();
	if (!isNaN(y) && s.length == 4) {
		date.setFullYear(y);
	}
	return date;
}

/**
 * used by dataspinner
 * 
 * @param date
 * @returns {string}
 */
function formatterYM(date) {

	if (!date) {
		return '';
	}
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	return y + '-' + (m < 10 ? ('0' + m) : m);
}

function formatterFullDate(date){
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var day = date.getDate();
    var hour = date.getHours();
    var minute = date.getMinutes();
    var second = date.getSeconds();

    if (month < 10) {
        month = "0" + month;
    }
    if (day < 10) {
        day = "0" + day;
    }

    if (hour < 10) {
        hour = "0" + hour;
    }
    if (minute < 10) {
        minute = "0" + minute;
    }

    if (second < 10) {
        second = '0' + second;
    } else if (second >= 100) {
        second = second.substr(1, 2);
    }

    var returnValue = year + "-" + month + "-" + day + " " + hour + ":" + minute;
    return returnValue;
}

/**
 * used by dataspinner
 * 
 * @param s
 *            'yyyy-MM'
 * @returns {*}
 */
function parserYM(s) {

	if (!s) {
		return null;
	}
	var ss = s.split('-');
	var y = parseInt(ss[0], 10);
	var m = parseInt(ss[1], 10);
	if (!isNaN(y) && !isNaN(m)) {
		return new Date(y, m - 1, 1);
	} else {
		return new Date();
	}
}

/**
 * 将数组转为 以','为分融符的字符串
 * 
 * @param {array} array
 */
function array2Str(array) {

	if (!array) {
		return '';
	}
	// 判断是否是数组
	var isArray = Object.prototype.toString.call(array) === '[object Array]';
	if (!isArray) {
		return '';
	}

	var result = '';
	var length = array.length;
	for (var i = 0; i < length; i++) {
		result += array[i];
		if (i != length - 1) {
			result += ',';
		}
	}
	return result;
}

/**
 * EasyUI datagrid 动态导出Excel
 * 
 * @param {object} data
 * @param {String} url 要导出的url
 */
function exporterExcel(data, url) {

	// 传输至后台，生面Excle，并下载
	$.ajax({
		url : url,
		type : 'post',
		data : data,
		beforeSend : function(req) {
			req.setRequestHeader("Accept", "application/json");
			showProgressMessager('操作提示', '正在提交数据...', '');
		},
		success : function(response) {
			if (response.isSuccess) {
				downloadFile(response.data);
				showOkMessager('操作提示', '生成Excel成功');
			} else {
				showAlertMessager('操作提示', response.errorMessage);
			}
		},
		complete : function(XMLHttpRequest, textStatus) {// 成功或失败都会调用
			$.messager.progress('close');
		}
	});
}

/**
 * 下载文件 , 注意：调用此方法要包含 jquery.fileDownload.min.js js库
 * 
 * @param {String} url
 */
function downloadFile(url) {

	$.fileDownload("auth/download" + url + ".pt").done().fail(function(response, url) {
		showAlertMessager('下载失败', response);
	});
}

/**
 * 获取当月第一天，用于datebox
 */
function getFirstDate() {

	var date = new Date();
	date.setDate(1);
	return parserYMD(date);
}

/**
 * 获取当月最后一天，用于datebox
 */
function getLastDate() {

	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	if (month > 12) {
		month -= 12;
		year++;
	}
	var newdate = new Date(new Date(year, month, 1).getTime() - 1000 * 60 * 60 * 24);
	return parserYMD(newdate);
}

/**
 * 获取可编辑div中的内容，并将其中的换行符转换为<br>
 * 用于保存到数据库
 *
 * @param obj
 */
function switchString(obj) {
	var str = obj.html();
	str = str.replace('<div>', '<br>');
	str = str.replace('</div>', '');
	return str;
}

/**
 * 将Date类型按特定格式转换
 * @param format
 * @returns {*}
 */
Date.prototype.format = function(format) {
	var date = {
		"M+": this.getMonth() + 1,
		"d+": this.getDate(),
		"h+": this.getHours(),
		"m+": this.getMinutes(),
		"s+": this.getSeconds(),
		"q+": Math.floor((this.getMonth() + 3) / 3),
		"S+": this.getMilliseconds()
	};
	if (/(y+)/i.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
	}
	for (var k in date) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1
				? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
		}
	}
	return format;
};


function getAges(str)
{
	var   r   =   str.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/);
	if(r==null)return   false;
	var   d=   new   Date(r[1],   r[3]-1,   r[4]);
	if   (d.getFullYear()==r[1]&&(d.getMonth()+1)==r[3]&&d.getDate()==r[4])
	{
		var   Y   =   new   Date().getFullYear();
		return((Y-r[1])   +"周岁");
	}
	return("输入的日期格式错误！");
}

// 判断字符串是否为空
function stringIsNull(str){
	if (str == null){
		return true;
	}
    if (str.replace(/(^s*)|(s*$)/g, "").length ==0){
        return true;
    }
    return false;
}