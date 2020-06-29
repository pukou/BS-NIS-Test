$(function() {
	init();
	//getAccordtions();
});

function init(){
	/*布局部分*/
	$('#master-layout').layout({
		fit:true/*布局框架全屏*/
	});

	/*右侧菜单控制部分*/
	var left_control_status=true;
	var left_control_panel=$("#master-layout").layout("panel",'west');

	$(".left-control-switch").on("click",function(){
		if(left_control_status){
			left_control_panel.panel('resize',{width:70});
			left_control_status=false;
			$(".theme-left-normal").hide();
			$(".theme-left-minimal").show();
		}else{
			left_control_panel.panel('resize',{width:200});
			left_control_status=true;
			$(".theme-left-normal").show();
			$(".theme-left-minimal").hide();
		}
		$("#master-layout").layout('resize', {width:'100%'})
	});

	/*右侧菜单控制结束*/
	$(".theme-navigate-user-modify").on("click",function(){
		$('.theme-navigate-user-panel').menu('hide');
		$.insdep.window({id:"personal-set-window",href:"user.html",title:"修改资料"});

	});


	$("#main-manue .datagrid-row").on("click",function(event){
		var menuName = $(event)[0].target.innerHTML;
		// 根据菜单名称查找当前菜单的节点，并获取到url属性
		var nodes = $(".mune-list li");
		for(var i = 0 ; i < nodes.length;i++){
			var _name = $(nodes[i]).html();
			if(_name == menuName){
				var url = $(nodes[i]).attr("url");
				addTab(url,menuName);
				break;
			}
		}
	})
}

/**
 * 增加或选择tab页
 *
 * @param url String
 * @param name String
 */
function addTab(url, name) {
	var pp = $('.easyui-tabs').tabs('exists', name);
	if (pp) {
		$('.easyui-tabs').tabs('select', name);
	} else {
		var temp = '<iframe width="100%" height="100%" frameborder="0" src="#{url}" style="width: 100%; height: 100%;"></iframe>';
		var content = $(temp.replace(/#{url}/g, url));
		$('.easyui-tabs').tabs('add', {
			title : name,
			content : content,
			closable : true
		});
	}
}
/**
 * 加载首级菜单
 */
function getAccordtions() {
	$.ajax({
		url : "auth/accords.pt",
		beforeSend : function(req) {
			req.setRequestHeader("Accept", "application/json");
		},
		success : function(data) {
			if (data.isSuccess) {
				var accordTmp = "<div title='#{title}' style='padding:10px;' id='#{id}'><ul id='#{treeid}'></ul></div>";
				var $accordion = $(".easyui-accordion");
				$(data.datalist).each(function() {
					var content = $(accordTmp.replace(/#{title}/g, this.title).replace(/#{id}/g, this.id).replace(/#{treeid}/g, this.id).replace(/#{treeUrl}/g, this.treeUrl));
					$accordion.accordion('add', {
						title : this.title,
						selected : false,
						content : content
					});
					var $ul = $("ul[id='" + this.id + "']");
					$ul.tree({
						url : this.treeUrl,
						animate : true,
						method : 'get',
						onClick : function(node) {
							if (node.attributes) {
								addTab(node.attributes.url, node.text);
							}
						}
					});
				});
			} else {
				alert("加载失败");
			}
		}
	});
}

function removeTab() {
	var $tabs = $('.easyui-tabs');
	var tab = $tabs.tabs('getSelected');
	if (tab) {
		var index = $tabs.tabs('getTabIndex', tab);
		$tabs.tabs('close', index);
	}
}