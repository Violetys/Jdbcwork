define(
	[ "eui/modules/uibase", "eui/modules/ecoolbar", "eui/modules/elist" ],
	function(uibase, ecoolbar, elist) {
		"use strict";
		var EComponent = uibase.EComponent, ECoolBar = ecoolbar.ECoolBar, EList = elist.EList;

		/**
		 * 该对象是主界面对象，包含顶部工具栏，列表数据等功能
		 * 
		 * @exports TestIndex
		 * @class
		 * @augments EComponent
		 * @param {Object} options  组件参数
		 * @param {Window} [options.wnd=window] window对象,
		 */
		function TestIndex(options) {
			EComponent.call(this, options);
			this._initTestIndex();
		}

		EUI.extendClass(TestIndex, EComponent, "TestIndex");

		/**
		 * 销毁方法，对象需要提供销毁方法，负责销毁对象持有的资源
		 * 可参考devwiki:pageId=174817288
		 */
		TestIndex.prototype.dispose = function() {
			this.coolbar.dispose();
			this.coolbar = null;

			this.list.dispose();
			this.list = null;

			EComponent.prototype.dispose.call(this);
		};

		/**
		 * 初始化对象的dom结构
		 */
		TestIndex.prototype._initTestIndex = function() {
			//初始化工具栏
			this._initCoolBar();

			//初始化列表
			this._initList();

			//加载数据
			this.loadData();
		};

		/**
		 * 初始化工具栏
		 */
		TestIndex.prototype._initCoolBar = function() {
			this.coolbar = new ECoolBar({
				wnd : this.wnd,
				parentElement : this.doc.getElementById("coolbar"),
				width : "100%",
				height : "100%",
				baseCss : "eui-coolbar-btngray"
			});
			var band = this.coolbar.addBand("band_1_name", true, true);
			var btn = band.addButton(null, I18N.getString(
					"abistudy.js.testindex.js.refresh", "刷新"), null);
			btn.setOnClick(this.loadData.bind(this));
		};

		/**
		 * 初始化列表
		 */
		TestIndex.prototype._initList = function() {
			this.list = new EList({
				wnd : this.wnd,
				parentElement : this.doc.getElementById("list"),
				width : "100%",
				height : "100%",
				columnResize : true,
				autoTotalWidth : false,
				columns : [ {
					checkbox : true
				}, {
					indexColumn : true,
					start : 1
				}, {
					caption : I18N.getString("abistudy.js.testindex.js.name", "名称"),
					width : "30%",
					id : "name"
				}, {
					caption : I18N.getString("abistudy.js.testindex.js.resid", "资源ID"),
					width : "30%",
					id : "resid",
					hint : true
				}, {
					caption : I18N.getString("abistudy.js.testindex.js.ds", "数据库链接池"),
					width : "40%",
					id : "jdbcpool",
					hint : true
				} ]
			});
		}

		/**
		 * 加载elist的数据
		 */
		TestIndex.prototype.loadData = function() {
			var self = this;
			EUI.post({
				url : EUI.getContextPath() + "abistudy/studyindex.do",
				data : {
					action : "getData"
				},
				callback : function(q) {
					var data = q.getResponseJSON();
					self.list.refreshData(data);
				},
				waitMessage : {
					message : I18N.getString("abistudy.js.testindex.js.refreshing",
							"正在刷新..."),
					finish : I18N.getString("abistudy.js.testindex.js.refreshed",
							"刷新成功！")
				}
			});
		}

		return {
			TestIndex : TestIndex
		}
	});
