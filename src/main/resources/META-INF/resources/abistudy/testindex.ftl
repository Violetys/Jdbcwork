<@module.page>
<@module.head title="${title}">
</@module.head>
<@module.body>
<div class="eui-layout-container">
<div id="coolbar" class="eui-layout-row-1 eui-layout-row-first"></div>
<div id="list" class="eui-layout-row-3 eui-layout-row-offset-1 eui-layout-row-last eui-padding-left-15 eui-padding-right-15"></div>
</div>
</@module.body>
<script>
var datasobj = ${data};
</script>
<script>
require(["eui/modules/ecoolbar", "eui/modules/elist"], function(ecoolbar, elist){
	var ecoolbar_btn = new ecoolbar.ECoolBar({
		wnd : window,
		parentElement : document.getElementById("coolbar"),
		width : "100%",
		height : "100%",
		baseCss : "eui-coolbar-btngray"
	});
	var band = ecoolbar_btn.addBand("band_1_name", true, true);

	var btn = band.addButton(null, I18N.getString("abistudy.testindex.ftl.refresh", "刷新"), null);
	btn.setOnClick(function(){
		EUI.post({
			url: EUI.getContextPath() + "abistudy/index.do",
			data: {
				action: "refresh"
			},
			callback: function (q) {
				q.checkResult();
				var data = q.getResponseJSON();
				list.refreshData(data);
			},
			waitMessage: { 
				message: I18N.getString("abistudy.testindex.ftl.refreshing", "正在刷新..."), 
				finish: I18N.getString("abistudy.testindex.ftl.refreshed", "刷新成功！")
			}
		});
	});

	var list = new elist.EList({
		parentElement: document.getElementById("list"),
		width: "100%",
		height: "100%",
		columnResize: true,
		autoTotalWidth: false,
		columns: [{
				checkbox: true
			},{
				indexColumn: true,
				start: 1
			},{
				caption: I18N.getString("abistudy.testindex.ftl.name", "名称"),
				width: "30%",
				id: "name"
			},{
				caption: I18N.getString("abistudy.testindex.ftl.resid", "资源ID"),
				width: "30%",
				id: "resid",
				hint: true
			},{
				caption: I18N.getString("abistudy.testindex.ftl.ds", "数据库链接池"),
				width: "40%",
				id: "jdbcpool",
				hint: true
			}],
		datas: datasobj
	});

});
</script>
</@module.page>