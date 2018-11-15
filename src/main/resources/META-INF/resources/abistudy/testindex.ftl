<@module.page>
	<@module.head title="${title!''}">
	</@module.head>
	<@module.body>
		<div class="eui-layout-container">
			<div id="coolbar" class="eui-layout-row-1 eui-layout-row-first eui-padding-left-20 eui-padding-right-20"></div>
			<div id="list" class="eui-layout-row-3 eui-layout-row-offset-1 eui-layout-row-last eui-padding-left-20 eui-padding-right-20 eui-padding-bottom-20 eui-scroll-auto"></div>
		</div>
	</@module.body>
	<script>
		require(["abistudy/js/testindex"], function(testindex){
			var mainPage = new testindex.TestIndex({
				wnd : window
			});
			EUI.addDispose(mainPage, window);
		});
	</script>
</@module.page>