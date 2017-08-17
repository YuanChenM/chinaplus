/**
 * resetpw.js
 * 
 * @screen login
 * @author Common
 */

/**
 * screen init.
 */
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.create('Ext.container.Viewport', {
		title: "Viewport",  
        layout: "border", 
	    height : 1000,
		items : [{
			id : chinaplus.screen.CPCRPS01,
			title : chinaplus.label.CPCRPS01_Label_PageTitle,
			header : false,
			xtype : 'form',
			autoScroll : true,
			height : 9999,
			width : 9999,
			border : false,
			closable : false,
			frame : true,
			bodyStyle : 'text-align:top;',
			defaultType : 'textfield',
			fieldDefaults: {
				style : 'text-align:center',
				labelAlign: 'right',
				labelPad : 10,
				labelWidth : 150,
				margin : 2,
				labelSeparator : ''
			},
			tbar : [
				{
					text : chinaplus.label.CPCRPS01_Button_Save,
					iconCls : 'btn-save',
					/*add role*/
					handler : function () {
		                // get form values
					    var oldPassword = Ext.getCmp(chinaplus.screen.CPCRPS01 + '_OldPw').getValue();
					    var newPassword = Ext.getCmp(chinaplus.screen.CPCRPS01 + '_NewPw').getValue();
					    var confPassword = Ext.getCmp(chinaplus.screen.CPCRPS01 + '_ConfirmPw').getValue();
					    // check oldPassword
					    if (Ext.isEmpty(oldPassword)) {
					    	TRF.util.showMessageBox(TRF.cnst.MESSAGEBOX_TYPE_WARN, TRF.util.getMessage('w1003_001', 'CPCRPS01_Label_OldPw'));
					        return;
					    }
					    // check newPassword
					    if (Ext.isEmpty(newPassword)) {
					    	TRF.util.showMessageBox(TRF.cnst.MESSAGEBOX_TYPE_WARN, TRF.util.getMessage('w1003_001', 'CPCRPS01_Label_NewPw'));
					        return;
					    }
					    // check confPassword
					    if (Ext.isEmpty(confPassword)) {
					    	TRF.util.showMessageBox(TRF.cnst.MESSAGEBOX_TYPE_WARN, TRF.util.getMessage('w1003_001', 'CPCRPS01_Label_ConfirmPw'));
					        return;
		                }
		                
		                // if confpassword not equals with newPassword
		                if(newPassword != confPassword) {
		                    TRF.util.showMessageBox(TRF.cnst.MESSAGEBOX_TYPE_WARN, TRF.util.getMessage('w1003_017'));
		                    return;
		                }
		                // New Password is same as Old Password
		                if(newPassword == oldPassword) {
		                    TRF.util.showMessageBox(TRF.cnst.MESSAGEBOX_TYPE_WARN, TRF.util.getMessage('w1003_018'));
		                    return;
		                }
		                
						//request successful handle method
						var successFunc = function (form, action) {
							// get result
							var result = Ext.util.JSON.decode(action.response.responseText);
	                        var messages = result.messages;
							if (Ext.isArray(messages)) {
								
								// show no pop message
								var message = messages[0];
								if(message.messageCode.indexOf('_') < 0) {
									if(messages.length > 1) {
										message = messages[1];
									}
								}
								// show message
								TRF.util.showMessageBox(TRF.cnst.MESSAGEBOX_TYPE_WARN, TRF.util.getMyMessage(message));
								return;
							}
							
							// get OK message
							var message = TRF.util.getMessage('i1002');
	                        TRF.util.showMessageBox(TRF.cnst.MESSAGEBOX_TYPE_INFO, message, function() {
	                        	// refer to main
		                        window.location.href="main"; 
	                        });
	                        
						};
						/**
						 * request failured handle method
						 */
						var failureFunc = function (form, action, message) {
							var statusCode = action.response.status;
							if (statusCode == TRF.cnst.NETWORK_ERROR) {
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
						
		                // set password
		                var param = {swapData : {oldPassword:oldPassword, newPassword:newPassword, confPassword:confPassword}};
		                
		                // reset password
						Ext.getCmp(chinaplus.screen.CPCRPS01).submit({
							url : TRF.util.getRequestPath(TRF.util.makeUrlForApplication("manage/CPCRPS01/resetPassword", chinaplus.screen.CPCRPS01)),
							jsonSubmit : true,
							scope : this,
							timeout: TRF.cnst.actionSubmitTimeout,
							params : param,
							clientValidation : false,
							success : successFunc,
							failure : failureFunc
						});
					}
				}
			],
			items : [
			     {
		            margin : '10 0 0 0',
		            inputType : 'password',
		            maxLength : 30,
		            msgTarget : 'side',
		            fieldLabel : chinaplus.label.CPCRPS01_Label_OldPw,
		            autoFitErrors: false,
		            id : chinaplus.screen.CPCRPS01 + '_OldPw',
		            name : 'oldPassword',
		            style : 'width:600;'
		        },
				{
					margin : '10 0 0 0',
		            inputType : 'password',
		            maxLength : 30,
		            msgTarget : 'side',
		            fieldLabel : chinaplus.label.CPCRPS01_Label_NewPw,
		            autoFitErrors: false,
		            id : chinaplus.screen.CPCRPS01 + '_NewPw',
		            name : 'newPassword',
		            style : 'width:600;'
				},
		        {
		            margin : '10 0 0 0',
		            inputType : 'password',
		            maxLength : 30,
		            msgTarget : 'side',
		            fieldLabel : chinaplus.label.CPCRPS01_Label_ConfirmPw,
		            autoFitErrors: false,
		            id : chinaplus.screen.CPCRPS01 + '_ConfirmPw',
		            name : 'confPassword',
		            style : 'width:600;'
		        }
			]}]
	});
});