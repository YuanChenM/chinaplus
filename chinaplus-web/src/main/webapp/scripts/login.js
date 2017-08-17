/**
 * login.js
 * 
 * @screen login
 * @author Common
 */

/**
 * screen init.
 */
Ext.onReady(function() {
	Ext.QuickTips.init();
	var winWidth = 320;
	var winHeight = 90;
	var scrWidth = document.body.clientWidth;
	var scrHeight = document.body.clientHeight;
	var imgUpHeight = (scrHeight - 223 - 150) > 290 ? 290 : (scrHeight - 223 - 150);
	var marginLeft = (scrWidth - 960) / 2;	
	var keyUpHandler = function(self, e, eOpts ){
		if(13 === e.keyCode){
			if(self.id === chinaplus.screen.LOGIN+'_LoginId'){
				Ext.getCmp(chinaplus.screen.LOGIN+'_Pwd').focus();
			}else if(self.id === chinaplus.screen.LOGIN+'_Pwd'){
				Ext.getCmp(chinaplus.screen.LOGIN+'_LoginBtn').focus();
				Ext.getCmp(chinaplus.screen.LOGIN + '_LoginBtn').handler();
			}
		}
	};
	var changedHandler = function(){
		if(Ext.getCmp(chinaplus.screen.LOGIN+"_LoginId").value!="" && Ext.getCmp(chinaplus.screen.LOGIN+"_Pwd").value!=""){
			Ext.getCmp(chinaplus.screen.LOGIN + '_LoginBtn').removeCls('loginButtionGray');
		} else {
			Ext.getCmp(chinaplus.screen.LOGIN + '_LoginBtn').addCls('loginButtionGray');
		}
	};

	Ext.create('Ext.container.Viewport', {
		layout: 'center',
		width: scrWidth,
		heigth: scrHeight,
		margin: '-50 0 0 0',
		listeners : {
			afterrender : function(){
				Ext.getCmp(chinaplus.screen.LOGIN+'_LoginId').setValue('');
				Ext.getCmp(chinaplus.screen.LOGIN+'_Pwd').setValue('');
			},
			resize : function(){
				scrWidth = document.body.clientWidth;
				scrHeight = document.body.clientHeight;
				marginLeft = (scrWidth - 960) / 2;
				//reset screen width
				Ext.getCmp('loginImgUpWidth').setWidth(scrWidth);
				Ext.getCmp('loginImgMidWidth').setWidth(scrWidth);
				Ext.getCmp('loginImgDownWidth').setWidth(scrWidth);
				imgUpHeight = (scrHeight - 223 - 150) > 290 ? 290 : (scrHeight - 223 - 150);
				Ext.getCmp('loginImgUp').setHeight(imgUpHeight);
				//reset screen margin
				var marginNew = '0 ' + marginLeft;
				Ext.getCmp('loginImgUp').setMargin(marginNew);
				Ext.getCmp('loginImgMid').setMargin(marginNew);
			}
		},
		items : [{
			xtype: 'container',
			height: 661,
			layout: 'center',
			items: [{
				xtype:'container',
				layout:'vbox',
				height: 661,
				items:[{
					xtype: 'container',
					id: 'loginImgUpWidth',
					width: scrWidth,
					items: [{
						xtype: 'container',
						id: 'loginImgUp',
						margin: '0 ' + marginLeft,
						width: 960,
						height: imgUpHeight
					}]
				},{
					xtype: 'container',
					id: 'loginImgMidWidth',
					width: scrWidth,
					items: [{
						xtype: 'container',
						id: 'loginImgMid',
						margin: '0 ' + marginLeft,
						width: 960,
						layout: 'hbox',
						items: [{
							xtype: 'container',
							width: 300,
							height: 223
						},{
							xtype: 'container',
							width: 500,
							height: 250,
							margin : '-10 0 0 -30',
							//style: 'background-image: url(resources/images/pic_btn_login.png) !important; background-repeat: no-repeat;',
							items: [{
								xtype : 'form',
								id : chinaplus.screen.LOGIN + '_LoginForm',
								hidden : true
							},{
								xtype: 'container',
								width : winWidth,
								margin: 15,
								height: 210,
								border: false,
								defaults : {
									xtype: 'textfield',
									width : 300,
									height : 50,
									enableKeyEvents : true
								},
								items: [{
										margin: '5 0 15 0',
										fieldStyle : 'line-height:50px;font-size:24px;width:404px;border:none;border-color:#163965;border:1px solid #163965;background:#e8f2f9; filter:alpha(opacity=60); -moz-opacity:0.5; -khtml-opacity: 0.5; opacity: 0.5;background-image:url(resources/images/icons/pic_user.png);height:68px;padding-left:75px;',
										emptyText :chinaplus.label.Common_Label_LoginId,
										value : '',
										name : 'loginId',
										id : chinaplus.screen.LOGIN+'_LoginId',
										listeners : {
											keyup : keyUpHandler,
											change: changedHandler
										}
									}, {
										inputType : 'password',
										fieldStyle : 'line-height:50px;font-size:24px;width:404px;border:none;border-color:#163965;border:1px solid #163965;background:#e8f2f9; filter:alpha(opacity=60); -moz-opacity:0.5; -khtml-opacity: 0.5; opacity: 0.5;background-image:url(resources/images/icons/pic_password.png);height:68px;padding-left:75px;',
										emptyText :chinaplus.label.Common_Label_Password,
										value : '',
										name : 'pwd',
										id : chinaplus.screen.LOGIN+'_Pwd',
										listeners : {
											keyup : keyUpHandler,
											change: changedHandler
										}
								},{
									xtype : 'button',
									id : chinaplus.screen.LOGIN + '_LoginBtn',
									text : '<font style="color:#FFFFFF;font:normal 30px tahoma, arial, verdana, sans-serif;">' + chinaplus.label.Common_Button_Login + '</font>',
									width : 410,
									height : 60,
									frame : false,
									margin : '10 0 0 -5',
									style : 'vertical-align:middle;background-image: url(resources/images/icons/pic_btn_login.png) !important; background-repeat: no-repeat;background-color:transparent;border-color:transparent;padding-bottom:5px;',
									cls : 'loginButtionGray',
									handler : function() {

										// set flag
										if (TRF.cnst.DOUBLE_CLICK == true) {
											return;
										}
										
										// required check
										if (Ext.getCmp(chinaplus.screen.LOGIN+'_LoginId').value == ""||Ext.getCmp(chinaplus.screen.LOGIN+'_LoginId').value.length>20) {
											
											TRF.util.showMessageBox(TRF.cnst.MESSAGEBOX_TYPE_WARN,TRF.util.getMessage('w1017'));
											return;
										}
										if(Ext.getCmp(chinaplus.screen.LOGIN+'_Pwd').value == ""){
											TRF.util.showMessageBox(TRF.cnst.MESSAGEBOX_TYPE_WARN,TRF.util.getMessage('w1019'));
											return;
											
										}
										/**
										 * request successed handle method
										 */
										var successFunc = function (form, action) {
											var result = Ext.util.JSON.decode(action.response.responseText);
											/**
											 * redirect to main screen
											 */
											var redirectUrl = function (url) {
												var fullstyle = " status=no,toolbar=no,resizable=yes,top=0,left=0,fullscreen=0;";
												var winName = 'winName' + new Date().valueOf();
												var win = open(url, winName, fullstyle);
												if (Ext.isIE) {
													win.moveTo(0, 0);
													win.resizeTo(screen.availWidth,screen.availHeight); 
												}
												window.opener = null;
												window.open('', '_self');
												window.close();
												TRF.cnst.DOUBLE_CLICK = false;
											};
											
											var messages = result.messages;
											if(Ext.isArray(messages)){
												var message = TRF.util.getMyMessage(messages[0]);
												
												TRF.util.showMessageBoxL(TRF.cnst.MESSAGEBOX_TYPE_WARN, message, message, function() {});
												TRF.cnst.DOUBLE_CLICK = false;
												return;
											}
											
											var data = result.data;
											if(!Ext.isEmpty(data)){
												if (data*1 <= 0) {
													var message = TRF.util.getMessage('w1021');
													TRF.util.showMessageBox(TRF.cnst.MESSAGEBOX_TYPE_INFO, message, function() {
													   	   redirectUrl('resetpw');
													   }
													)
												} else {
													var message = TRF.util.getMessage('i1005', [data]);
													TRF.util.showMessageBox(TRF.cnst.MESSAGEBOX_TYPE_INFO, message, function() {
													   	   redirectUrl('main');
													   }
													)
												}
											} else {
												redirectUrl('main');
											}
										};
										
										//request failured handle method
										var failureFunc = function (form, action, message) {
											TRF.cnst.DOUBLE_CLICK = false;
											var statusCode = action.response.status;
											if (statusCode == TRF.cnst.NETWORK_ERROR) {
												//TRF.util.showMessageBoxByMsgIdAndArgs("e0002");
												TRF.util.showMessageBox(TRF.cnst.MESSAGEBOX_TYPE_WARN,TRF.util.getMessage('e0002'));
												return;
											}
											if (action.response.responseText != "") {
												var responseText = Ext.util.JSON.decode(action.response.responseText);
												if (responseText.message && Ext.isArray(responseText.message)) {
													var decodeMessage = Ext.util.JSON.decode(responseText.message[0]);
													if (Ext.isObject(messages)) {
														TRF.util.showMessageBox(TRF.cnst.MESSAGEBOX_TYPE_WARN,TRF.util.getMyMessage(decodeMessage));
													}
												}
											}
										};
										
										// set flag
										TRF.cnst.DOUBLE_CLICK = true;
										
										/**
										 * do login check
										 */
										Ext.getCmp(chinaplus.screen.LOGIN + '_LoginForm').submit({
											url : TRF.util.getRequestPath("login/checklogin"),
											jsonSubmit : true,
											scope : this,
											timeout: TRF.cnst.actionSubmitTimeout,
											params : {
												loginId : Ext.getCmp(chinaplus.screen.LOGIN + "_LoginId").value,
												pwd : Ext.getCmp(chinaplus.screen.LOGIN + "_Pwd").value
											},
											clientValidation : false,
											success : successFunc,
											failure : failureFunc
										});
									}
								}]
							}]
						}]
					}]
				},{
					xtype: 'container',
					width: 1366,
					id: 'loginImgDownWidth',
					items: [{
							xtype: 'container',
							html: '<div style="position: -ms-page;right: 10px;color:#233759;font-size:10px;text-align:right;bottom: 10px;">'+ chinaplus.label.Copyright +'</div>'
						}]
				}]
			}]
		}]
	});
});