/**
 * MainMenu.js
 *
 * @screen MainMenu
 * @author COMMON
 */

/**
 * Logout system.
 */
var logout = function() {

	var func = function(btn) {
		if (btn == 'yes') {
			/**
			 * redirect to login screen
			 */
			var redirectUrl = function() {
				window.location.href = TRF.cnst.INDEX_PAGE;
			};

			TRF.util.actionSubmit(redirectUrl, "index", "userLogout", {});
		}
	};

	TRF.util.showMessageBoxByMsgIdAndArgs('c1001',
			chinaplus.label.CPCMSS01_Button_Logout, func);
};

/**
 * banner header.
 */
Ext.HERDER_ITEM = [
		{
			region : 'west',
			width : '100%',
			xtype : 'box',
			id : 'logo'
		},
		{
			region : 'east',
			width : 535,
			baseCls : 'banner-east',
			id : TRF.cnst.APPLICATION_BANNER_ID,
			layout : 'absolute',
			items : [
					{
						baseCls : 'x-plain',
						x : -280,
						y : 3,
						items : []
					},
					{
						baseCls : 'x-plain',
						x : 150,
						y : 3,
						items : []
					},
					{
						baseCls : 'x-plain',
						x : -450,
						y : 3,
						items : [ {
							xtype : 'label',
							text : chinaplus.label.CPCMSS01_Label_DataDatetime
									+ " : ",
							style:'font-weight:bold'
						} ]
					},
					{
						baseCls : 'x-plain',
						x : -400,
						y : 17,
						items : [ {
							xtype : 'label',
							id : 'label_date'
						} ]

					},
					{
						baseCls : '',
						x : -450,
						y : 47,
						items : [ {
							xtype : 'label',
							text : chinaplus.label.CPCMSS01_Label_Promptinformation,
							style:'font-weight:bold'
						} ]
					},
					{
						baseCls : 'x-plain',
						x : -7,
						y : 47,
						items : [
								{
									xtype : 'box',
									// margin : '0 5 0 0',
									autoEl : {
										tag : 'img',
										src : 'resources/images/icons/pic_top-user.png'
									}
								},
								{
									xtype : 'label',
									style : 'text-align:right; padding-right:5px',
									text : chinaplus.label.CPCMSS01_Label_User
											+ " : "
								},
								{
									xtype : 'label',
									id : 'label_url',
									value : Ext.getDom('loginUser.loginId').value,
									// margin : '0 0 0 5',
									loadApplication : function() {
										var backFunc = function(resData) {
											if (resData.success === true) {
												// show
												TRF.util
														.setDetailTabMode_View();
												// set main resourceId
												TRF.access.mainResourceId = chinaplus.screen.CPCUMS02;
												// load application
												TRF.util
														.loadApplication(
																chinaplus.screen.CPCUMS02,
																chinaplus.screen.CPCUMS02,
																true, 'owner');
											}
										}

										// new
										TRF.util.getAccessCollection(
												chinaplus.screen.CPCUMS01,
												chinaplus.screen.CPCUMS02,
												backFunc);
									},
									html : '<font style="color:#FFF;cursor:pointer;"><span tabIndex=-1 onclick="Ext.getCmp(\'label_url\').loadApplication()">'
											+ Ext.getDom('loginUser.loginId').value
											+ '</span></font>'
								} ]
					},
					{
						baseCls : 'x-plain',
						x : 143,
						y : 50,
						width : 50,
						style : 'text-align:right; padding-right:5px',
						items : [ {
							xtype : 'label',
							text : chinaplus.label.CPCMSS01_Label_Office
									+ " : "
						} ]
					},
					{
						// panell for current warehouse and warehouse select
						baseCls : '',
						x : 193,
						y : 48,
						width : 120,
						items : [ {
							xtype : 'combo',
							id : 'bannerOffice',
							width : 120,
							valueField : 'id',
							displayField : 'text',
							triggerAction : 'all',
							hasEmptyItem : false,
							hideMode : 'visibility',
							tabIndex : -1,
							multiSelect : false,
							queryMode : 'local',
							previousValue : null,
							store : TRF.util.createStore(TRF.cnst.MAINSCREEN,
									'main/officelist', 'Combo',
									'mainmenu_bannerOfficeStore'),
							onChange : function(o, v) {
								if (o && v && TRF.core.office != o) {
									var redirectUrl = function() {
										window.location.href = TRF.cnst.MAIN_PAGE;
									};
									TRF.util.actionSubmit(redirectUrl,
											TRF.cnst.MAINSCREEN,
											"main/changeoffice", {
												officeId : o
											});
								}
							}
						} ]
					},
					{
						baseCls : 'x-plain',
						x : 343,
						y : 50,
						width : 70,
						style : 'text-align:right; padding-right:5px',
						items : [ {
							xtype : 'label',
							text : chinaplus.label.CPCMSS01_Label_Language
									+ " : "
						} ]
					},
					{
						// panell for current warehouse and warehouse select
						baseCls : '',
						x : 413,
						y : 48,
						width : 120,
						items : [ {
							xtype : 'combo',
							id : 'bannerLanguage',
							width : 120,
							valueField : 'id',
							displayField : 'text',
							triggerAction : 'all',
							hasEmptyItem : false,
							hideMode : 'visibility',
							tabIndex : -1,
							multiSelect : false,
							queryMode : 'local',
							previousValue : null,
							store : TRF.util.createStore(TRF.cnst.MAINSCREEN,
									'main/language', 'Combo',
									'mainmenu_bannerLanguageStore'),
							onChange : function(o, v) {
								if (o && v && TRF.core.language != o) {
									var redirectUrl = function() {
										window.location.href = TRF.cnst.MAIN_PAGE;
									};
									TRF.util.actionSubmit(redirectUrl,
											TRF.cnst.MAINSCREEN,
											"main/changelanguage", {
												language : o
											});
								}
							}
						} ]
					},
					{
						baseCls : 'xplain',
						frame : false,
						border : false,
						x : 456,
						y : 15,
						items : [ {
							xtype : 'button',
							text : '<font style="color:#FFFFFF;font-weight:bold;">'
									+ chinaplus.label.CPCMSS01_Button_Logout
									+ '</font>',
							iconCls : 'btn-logout',
							style : 'background-color:#006599;border-color:#006599;background-image:none;',
							width : 77,
							height : 24,
							tabIndex : -1,
							handler : logout
						} ]
					} ]
		} ];

/**
 * window define.
 */
var win = Ext
		.create(
				'Ext.panel.Panel',
				{
					region : 'west',
					id : 'west-panel',
					header : true,
					split : true,
					width : 190,
					minSize : 190,
					padding : '5 5 5 5',
					maxWidth : 250,
					title : '<font style="margin-left:-20px;position: absolute;margin-top: -6px;">'
							+ chinaplus.label.Common_Label_Title + '</font>',
					collapsible : true,
					margins : '0 0 0 5',
					layout : 'accordion',
					iconCls : 'icon-accordion',
					autoScroll : false,
					style : 'border-bottom-width:5px;',
					layoutConfig : {
						animate : true
					}
				});

/**
 * Show the business screen.
 * 
 * @param text
 *            screen id
 * @param url
 *            screen url
 * @param renderTo
 *            render to the element
 */
// function addTab(text, url, accessLevel, resourceId) {
function addTab(rootId, leafId, resourceId) {

	// get back function
	var backFunc = function(resData) {
		if (resData.success === true) {
			// show
			TRF.util.setDetailTabMode_View();
			// set main resourceId
			TRF.access.mainResourceId = resourceId;
			// set main resourceId
			TRF.access.rootId = rootId;
			// show
			TRF.util.loadApplication(resourceId, resourceId, true);
		}
	}

	// new
	TRF.util.getAccessCollection(rootId, resourceId, backFunc);
}

/**
 * Show the menu tree.
 * 
 * @param menuData
 */
function addTree(menuData) {
	Ext.Array.each(menuData, function(treeData) {
		var tree = Ext.create('Ext.tree.Panel', {
			title : treeData.rootText,
			autoScroll : true,
			rootVisible : false,
			viewConfig : {
				loadingText : 'load...'
			},
			store : Ext.create('Ext.data.TreeStore', treeData.treeContent),
			style : 'top:5px,left:5px;',
			listeners : {
			/*
			 * afterrender : function(obj) { obj.header.hide(); }
			 */
			}
		});
		tree.on('itemclick', function(view, node, item, index, e) {
			if (node.get('leaf')) {
				addTab(node.get('root'), node.get('id'), node.get('screenId'));
			}
		});
		tree.expandAll();
		win.add(tree);
		win.doLayout();
	});
};

/**
 * Get the menu data.
 */
win.on('beforerender', function() {

	var menuDataCallbackFunc = function(contents) {
		addTree(contents);
	};
	TRF.util.getDataByStore(TRF.cnst.MAINSCREEN, "main/menu", "User",
			menuDataCallbackFunc);
});
/**
 * clear the message area.
 * 
 */
var clearMessage = function() {
	var msgArea = Ext.getCmp('messageArea');
	var initStr = {
		"initMessage" : ""
	};
	var initMsgTpl = '{initMessage}';
	var initMsg = new Ext.Template(initMsgTpl);
	initMsg.overwrite(msgArea.body, initStr);
};

/**
 * Main screen init.
 */
Ext
		.onReady(function() {
			Ext.QuickTips.init();
			Ext.SOURTH_HEIGHT = 18;

			TRF.core.language = Ext.getDom('loginUser.language').value;
			TRF.core.office = Ext.getDom('loginUser.officeId').value;
			TRF.core.userId = Ext.getDom('loginUser.userId').value;
			TRF.core.userName = Ext.getDom('loginUser.userName').value;
			TRF.core.vvAllFlag = Ext.getDom('loginUser.vvAllFlag').value;
			TRF.core.aisinAllFlag = Ext.getDom('loginUser.aisinAllFlag').value;
			var redirect = Ext.getDom('loginUser.redirect').value;

			Ext.viewport = Ext
					.create(
							'Ext.container.Viewport',
							{
								id : 'viewportId',
								layout : 'border',
								items : [
										{
											region : 'north',
											height : 70,
											layout : 'border',
											baseCls : 'banner',
											border : false,
											items : Ext.HERDER_ITEM
										},
										{
											xtype : 'panel',
											region : 'south',
											height : Ext.SOURTH_HEIGHT,
											style : 'text-align:right;',
											html : chinaplus.label.Copyright
										},
										{
											region : 'center',
											layout : 'border',
											border : false,
											items : [
													{
														region : 'south',
														id : 'messageArea',
														collapsible : true,
														collapsed : true,
														autoScroll : true,
														header : true,
														split : true,
														animCollapse : false,
														floatable : false,
														height : 75,
														tools : [ {
															type : 'refresh',
															handler : clearMessage
														} ]
													},
													win,
													{
														xtype : 'panel',
														region : 'center',
														id : 'tabpanelId',
														border : true,
														enableTabScroll : true,
														activeTab : 0,
														margins : '0 5 0 0',
														items : [ Ext
																.create(
																		'Ext.tab.Panel',
																		{
																			id : 'infotab',
																			layout : 'fit',
																			header : false,
																			items : []
																		}) ],
														listeners : {
															resize : function(
																	self,
																	width,
																	height,
																	oldWidth,
																	oldHeight,
																	eOpts) {
																if (Ext
																		.isDefined(oldHeight)) {

																	TRF.core.appMgr.items
																			.forEach(function(
																					tabId) {
																				Ext
																						.getCmp(
																								tabId)
																						.setHeight(
																								height - 30);
																			});
																}
															}
														}
													} ]
										} ]
							});

			Ext.getDom('loading').innerHTML = '';

			TRF.util.loadStore(Ext.getCmp('bannerLanguage').store, null,
					function() {
						Ext.getCmp('bannerLanguage')
								.setValue(TRF.core.language);
					});

			TRF.util.loadStore(Ext.getCmp('bannerOffice').store, null,
					function() {
						Ext.getCmp('bannerOffice').setValue(TRF.core.office);
					});
			
	        var syncFunc = function(responseData) {
	            if (null != responseData && null != responseData.result) {
	                Ext.getCmp('label_date').setData(responseData.result);
	            }
	            
				TRF.util.loadApplication(chinaplus.screen.CPCIFS01, chinaplus.screen.CPCIFS01);
	        };
	        TRF.util.ajaxSubmit(syncFunc, chinaplus.screen.CPCIFS01, 'com/CPCIFS01/loadBannerSyncTime');
		});
