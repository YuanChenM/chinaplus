/**
 * trf.js
 * 
 * @screen COMMON		
 * @author liu_yinchuan
 */
//-----------------------------------------------------------
// TRF - Common Library
//-----------------------------------------------------------
Ext.namespace('TRF','TRF.comp','TRF.core','TRF.ex','TRF.ex.data','TRF.ex.form','TRF.util','TRF.access','TRF.pickers');

TRF.core.activeTransaction = undefined;

TRF.core.appMgr = new Ext.util.MixedCollection();

TRF.core.handleReadyCmp = new Ext.util.MixedCollection();

TRF.core.jsonMgr = new Ext.util.MixedCollection();

TRF.core.loadingStatusMgr = new Ext.util.MixedCollection();

//TRF.appDataMgr = new Ext.util.MixedCollection();

/**
 * アプリケーションの準備状態の管理用
 */
TRF.core.handleReadyPopupCmp = new Ext.util.MixedCollection();

/**
 * Collection to save callback function in popup
 */
TRF.core.popupCallbackMgr = new Ext.util.MixedCollection();

TRF.util.taskRunner = new Ext.util.TaskRunner();

/**
 * Load application.
 * @param applicationId	the screen id
 * @param screenId the action code
 * @param clsSession the flag of keep session(true/false)
 * @param params the params of screen load
 */
TRF.util.loadApplication = function(applicationId, screenId, clsSession, params) {

	// アクティブなトランザクションが存在する場合は無視
	if (TRF.core.isActiveTransactionExists()) return false;
	
	var tranId = Ext.id();

	// 現在起動中のアプリケーションの終了前処理
	var needConfirm = false;
	TRF.core.appMgr.each(function(id) {
		var app = Ext.getCmp(id);
		if (app.beforeUnload != undefined) {
			if (app.beforeUnload() == false) {
				TRF.core.endTransaction(tranId);
				return;
			}
		}
		if (!needConfirm) {
			// 編集内容を破棄してよいかの確認
			if (app.isEdited != undefined) {
				if (typeof(app.isEdited) == 'boolean') {
					needConfirm = app.isEdited;
				} else if (typeof(app.isEdited) == 'function') {
					needConfirm = app.isEdited();
				}
			}
		}
		
		if (needConfirm) return false;
	});

	var clearSession = true;

	if (arguments.length >= 2) {
		if(typeof(clsSession) == 'boolean'){
			clearSession = clsSession;
		}
	}

	var f = function() {
		//終了処理があればコール
		TRF.core.appMgr.each(function(id) {
			var app = Ext.getCmp(id);
			if (app.finalize != undefined) {
				app.finalize();
			}
		});
		TRF.core.startTransaction(tranId);
		TRF.core.getApplicationContents(applicationId, screenId, tranId, true, clearSession, params);
	};

	// 変更した内容を破棄してよいかの確認メッセージ
	if (needConfirm == true) {
		TRF.util.showMessageBoxByMsgIdAndArgs('c0001', null, function(btn) {
			if (btn == 'yes') {
				f();
			}
		});
	} else {
		f();
	}
};

/**
 * Get application contents.
 * @param applicationId	the screen id
 * @param screenId the action code
 * @param tranId the tranId
 * @param removeAll the flag of remove all tabs
 * @param clsSession the flag of keep session(true/false)
 * @param params the params of screen load
 * @param tabIndex the tabIndex
 * @param needInitMessageArea the flag of init message area
 * @param clearOld the flag of clear old tab
 * 
 */
TRF.core.getApplicationContents = function(applicationId, screenId, tranId, removeAll, clearSession, params ,tabIndex,needInitMessageArea,clearOld) {
	// マスク処理
	var json=TRF.core.jsonMgr.getByKey(applicationId);

	if (applicationId != "TOP") {
		if(json){
			TRF.core.mask(TRF.core.maskMsg.cacheApplication);
		}else{
			TRF.core.mask(TRF.core.maskMsg.application);
		}
	}
	// アプリケーションデータ領域のクリア(セッション維持の場合はクリアしない)
	/*if (clearSession) {
		TRF.appDataMgr.clear();
	}*/
	
	var tabs = Ext.getCmp(TRF.cnst.APPLICATION_TABPANNEL_ID);
	tabs.disabled = true;

	if (removeAll) {
		// 直前のコンテンツを破棄
		var keys = [];
		tabs.items.eachKey(function(arguments,key){
			if (key.id!=chinaplus.screen.CPCIFS01) {
				keys[keys.length] = key;
			}
		});
		Ext.each(keys, function(key) {
			tabs.remove(key, true);
		});
		//アプリケーションマネージャをクリア
		TRF.core.appMgr.clear();
	}

    //Performance log start point
    if (false && json) {
    	TRF.core.endTransaction(tranId);
    	//var jsonData = eval("("+json+")");
    	var jsonData = json;
    	//lazy load
    	setTimeout(function() {
			TRF.core.initContents(jsonData, tranId, params, tabIndex, true);
		}, 1);
	} else {
		clearOld = true;
		// ストアの生成
		var contentStore = TRF.util.createStore(applicationId, "screenContent", TRF.util.createContentModle, "contentStore");
		var storeParam = {};
		storeParam[TRF.cnst.TOKEN] = TRF.core.token;
        storeParam[TRF.cnst.SCREENID] = screenId;
		
		// アプリケーションのロード
		contentStore.load({
			params: storeParam,
			timeout : TRF.cnst.getApplicationContentsTimeout,
			scope: this,
			callback:function(records, operation, success){
				TRF.core.endTransaction(tranId);
				var tabs = Ext.getCmp(TRF.cnst.APPLICATION_TABPANNEL_ID);
				var contents = contentStore.getProxy().getReader().rawData;
	            	
				if(!Ext.isEmpty(operation) && !Ext.isEmpty(operation.error)){
					if(operation.error.status==TRF.cnst.NETWORK_ERROR){
						TRF.util.showMessageBoxByMsgIdAndArgs("e0002");		
					}else if(operation.error.status==TRF.cnst.RESULT_CODE_SESSION_TIMEOUT){
						TRF.util.exitApp();	
					}
					contents=undefined;
	        	}else{
					var statusCode=operation._response.status;
					if(statusCode == TRF.cnst.RESULT_CODE_EXCEPTION_ERROR){
						contents=undefined;
						var errorMessage=['Content: '+applicationId+' load failed!'];
						TRF.util.outMessage(errorMessage);
					}
				} 
				
	            if(contents==undefined){
	            	
					tabs.doLayout();
					tabs.disabled = false;
					TRF.core.handleReadyCmp.clear();
					TRF.core.unmask();
				} else {
	                var addFunc= function(clearOld){
	                	TRF.core.jsonMgr.add(applicationId, contents);
						TRF.core.initContents(contents,tranId,params,tabIndex,clearOld);
	                };
	                addFunc(clearOld);
			}
		}
	});
	}
	// メッセージエリアの初期化
    if(needInitMessageArea !== false){
		TRF.util.initMessageArea();
    }
};

/**
 * get access conllection.
 * @param {} resourceId
 * @param {} func
 */
TRF.util.getAccessCollection = function(rootId, resourceId, func) {
	
    //get collection
    var AccessCollection = function(resourceId, func) {
        var result = {};
        
        // load screen
        TRF.util.ajaxSubmit(function(responseData){
        	// set result
            if(responseData.success && !Ext.isEmpty(responseData.result)) {
                // get 
                result = responseData.result;
            }
            
            // return values
            var retData = {
                success : responseData.success, 
                messageCodes : responseData.messageCodes
            };
            
            // if has back function
            if(Ext.isFunction(func)) {
                func(retData);
            }
        }, resourceId, 'main/mainResource', {rootId: rootId});
        
        // getAccessLevel
        this.getAccessLevel = function(officeId) {
        	var accessLevel = chinaplus.consts.code.AccessLevel.NONE;
        	if (officeId == null) {
        		accessLevel = result.accessLevel;
        	} else {
        		if(result.officeLst != null) {
        			var officeLst = result.officeLst;
        			for(var i in result.officeLst) {
        				var office = officeLst[i];
        				if (office.officeId + '' == officeId + '') {
							accessLevel = office.accessLevel;
							break;
						}
        			}
        		}
        	}
        	
        	return accessLevel;
        };
        
        // getMainResourceId
        this.getMainResourceId = function() {
            return result.mainResourceId;
        };
        
        // destroy
        this.destroy = function() {
            result = null;
        };
    
        // get auth list
        this.getAuthCollect = function(screenId) {
            if(!Ext.isEmpty(result.screenAuthInfo)) {
                var infoList = result.screenAuthInfo;
                if(infoList != null) {
                    for(var i in infoList) {
                        var info = infoList[i];
                        if(info.screenId != null 
                                && info.screenId == screenId) {
                            // get this object
                            return info;
                        }
                    }
                }
            }
            return {};
        };
    };
	
	// if exist
    if(TRF.access.AuthInfo != null) {
        TRF.access.AuthInfo.destroy();
        TRF.access.AuthInfo = null;
    }
    
    // get new 
	TRF.access.AuthInfo = new AccessCollection(resourceId, func);
};

/**
 * TRF.util.controlAccess 
 */
TRF.util.controlAccess = function(comp, params) {
	
	var isControl = comp.isControl;
	if (isControl == null || isControl == undefined) {
		isControl = true;
	}
	
    // no need check
    if (!isControl) {
    	return true;
	}
	
	var accessLevel = null;
    
    // check access level
    if (TRF.access.AuthInfo != null) {
    	// check office
    	var officeId = null
    	// if office exists
    	if (params != null && Ext.isObject(params)) {
			if (params[TRF.cnst.AUTH_OFFICE] != null) {
				officeId = params[TRF.cnst.AUTH_OFFICE];
			}
		}
    	// get access information
    	accessLevel = TRF.access.AuthInfo.getAccessLevel(officeId);
    	// get auth information
        var authInfo = TRF.access.AuthInfo.getAuthCollect(comp.id);
        if(!Ext.isEmpty(authInfo.authList)) {
            // loop key
            var authList = authInfo.authList;
            for (var i in authList) {
                var auth = authList[i];
                // if not screen
                if(auth.authCode != null) {
                	// retrun
                	if(auth.authCode == comp.id) {
                		// no access level
                		if (accessLevel < auth.accessLevel) {
                            accessLevel = null;
                            break;
                        }
                	} else {
                		// hide items
                        var item = Ext.getCmp(comp.id + auth.authCode);
                        if (item && accessLevel < auth.accessLevel) {
                        	var authMethd = item.authMethd != null ? item.authMethd : chinaplus.consts.code.AuthMethd.HIDE;
                        	// if hidden
                        	if(authMethd == chinaplus.consts.code.AuthMethd.HIDE) {
                                item.hide();
                        	} else {
                        		TRF.util.makeCmpReadOnly(item);
                        	}
                        }
                	}
                }
            }
        }
    }
    
    // no access level
    if(accessLevel == null) {
        // load end
        TRF.util.dataLoadEnd(comp.id);
        // show message 
        TRF.util.showMessageBox(TRF.cnst.MESSAGEBOX_TYPE_WARN, TRF.util.getMessage('e0009'), function() {
            // remove
            TRF.util.removeApplication(comp.id);
        }); 
        // false
        return false;
    }
    
    return true;
};

/**
 * Init screen by json contents.
 * @param contents the json of screen
 * @param tranId the tranId
 * @param params the params of screen load
 * @param clearOld the flag of clear old tab
 * 
 */
TRF.core.initContents = function(contents, tranId, params, tabIndex, clearOld) {

	var tabs = Ext.getCmp(TRF.cnst.APPLICATION_TABPANNEL_ID);

	if (clearOld) {
		if(tabs.contains(Ext.getCmp(contents.id))){
		      tabs.remove(contents.id, true);
		}
		TRF.core.appMgr.remove(contents.id);
	}

	TRF.core.appMgr.add(contents.id);
	tabIndex !== undefined ? tabs.insert(tabIndex, contents) : tabs.add(contents);
	tabs.doLayout();
	tabs.disabled = false;
	
	// active current tab
	var activateTab = tabs.items.keys[tabs.items.keys.length - 1];
	if (tabIndex !== undefined) {
		activateTab = tabIndex;
	}
	tabs.setActiveItem(activateTab);
	TRF.util.registHandleReadyCmp(contents.id);
	
	// コンポーネントの初期化処理
	var comp = Ext.getCmp(contents.id);

	TRF.core.loadingStatusMgr.add(contents.id,TRF.util.genCounter());
	
	if (comp.initialize != undefined) {
		TRF.util.dataLoadStart(contents.id);
		
		// access control
		if (TRF.util.controlAccess(comp, params)) {

			// initialize
			comp.initialize(params);

			// data end
			TRF.util.dataLoadEnd(contents.id);
		}
	}
	
	var funcxx = function(runCount) {
		if(!TRF.util.isDataLoading(contents.id)){
			TRF.util.taskRunner.stop(this);
			TRF.core.loadingStatusMgr.remove(contents.id);
			TRF.util.handleReady(contents.id);
			TRF.util.iteratesChildInput(comp, function(cmp) {
				if (comp.needConfirmChange) {
					/*TRF.util.addOnChangeListenerToInputAndGrid(cmp, function(){
						if (!(cmp.needConfirmChange === false)) {
							if(!comp.isEdited){
								comp.isEdited = true;
							}
						}
					});*/
					if(TRF.util.isDetailTabMode_Edit() || TRF.util.isDetailTabMode_New()) {
						comp.isEdited = true;
					}
				}
				TRF.util.runFunctionToInput(cmp, function(){
					if (!cmp.readOnly && !cmp.allowBlank) {
						cmp.setFieldStyle('background:rgb(255, 255, 153);');
					}
					
					/*cmp.on('change',function(o, v){
						if(o.xtype != 'combo' && o.xtype != 'combobox' && o.xtype != 'hcombo' && o.xtype != 'checkcombo' && o.xtype != 'datefield'){
							if(o.xtype == 'numberfield'){
								if (typeof(v) == "string") {
							    //o.value = v.replace(/[^\u0000-\u00ff]/g,'');
								}
							}else{
								//o.setValue(v.replace(/[^\u0000-\u00ff]/g,''));
							}
						  
						}
					}, cmp);*/
				});
			});
			
			TRF.core.endTransaction(tranId);
			
			if(Ext.isFunction(comp.afterInit)){
			    comp.afterInit(params);
			}
			
			//TRF.util.tabIndex(comp);
		}
	};
	TRF.util.taskRunner.start({
		run : funcxx,
		interval : 50
	});
};

/**
 * Run a function to an input component.
 * @param rootCmp the root component contains all possible inputs.
 * @param func the function witch includes TRF.util.iteratesChildInput or TRF.util.runFunctionToInput.
 * 
 */
TRF.util.iteratesChildInput = function(rootCmp, func) {
	
	func(rootCmp);
	
	var items = rootCmp.items;
	if (!Ext.isEmpty(items)) {
		if (Array.isArray(items)) {
			for (var i = 0; i < items.length; i++) {
				TRF.util.iteratesChildInput(items[i],func);
			}
		} else {
			TRF.util.iteratesChildInput(items,func);
		}
	}
};

/**
 * Run a function to an input component.
 * @param rootCmp the root component contains the inputs.
 * @param runFunc the run function.
 * 
 */
TRF.util.runFunctionToInput = function(rootCmp,runFunc) {
	if (rootCmp instanceof Ext.form.field.Text || rootCmp instanceof Ext.form.field.ComboBox || rootCmp instanceof Ext.ux.HCombo || rootCmp instanceof Ext.ux.form.ItemSelector) {
		runFunc(rootCmp);
		return;
	}
};

/**
 * Add listener to an input component when data changed.
 * @param rootCmp the root component contains the inputs.
 * @param listenerFunc the listener function.
 * 
 */
TRF.util.addOnChangeListenerToInputAndGrid = function(rootCmp,listenerFunc) {
	if (rootCmp instanceof Ext.form.field.Base || rootCmp instanceof Ext.ux.form.ItemSelector) {
		rootCmp.on('change',listenerFunc);
		return;
	}else if(rootCmp instanceof Ext.grid.Panel){
		var curStore = rootCmp.store;
		curStore.on('datachanged',listenerFunc);
		curStore.on('update',listenerFunc);
		return;
	}
};

 /**
 * Create model of get json contents.
 * 
 */
TRF.util.createContentModle = Ext.define('User',{
									extend:'Ext.data.Model',
									fields:[
									   {name:'jsonContent',type:'string'}
									]
								});
 /**
 * Get data by store.
 * @param requestUrl the request Url
 * @param modelName the model Name
 * @param params the request params
 * @param func the callback fucntion
 * @return data
 */
TRF.util.getDataByStore=function(applicationId,requestUrl, modelName,func, params){
	
	var dataStore=TRF.util.createStore(applicationId, requestUrl, modelName, "dataStore");
	
	var storeParams={};
	if(params!=undefined){
		storeParams=params;
	}
			
	var callbackFunc=function(){
			var contents = dataStore.getProxy().getReader().rawData;
			func(contents);  
	};
	TRF.util.loadStore(dataStore,storeParams,callbackFunc);
};

/**
 * load store.
 * @param store the store
 * @param params the request params
 * @param func the callback fucntion
 */
TRF.util.loadStore=function(store, params, func, extraParams){
	
	if(!Ext.isEmpty(params)){
		Ext.apply(store.proxy.extraParams, {'swapData': params});
	}
	
	if(!Ext.isEmpty(extraParams)){
        Ext.apply(store.proxy.extraParams, extraParams);
    }
	
	store.load({
		timeout : TRF.cnst.getApplicationContentsTimeout,
		scope : this,
		//params: param,
		callback:function(records, options, success, arguments){
			if(!Ext.isEmpty(options) && !Ext.isEmpty(options.error)){
				if(options.error.status==TRF.cnst.NETWORK_ERROR){
					TRF.util.showMessageBoxByMsgIdAndArgs("e0002");		
				}else if(options.error.status==TRF.cnst.RESULT_CODE_SESSION_TIMEOUT){
					TRF.util.exitApp();	
				}
				
				return;
        	}
			//var statusCode=options._response.status;
			//if(statusCode == TRF.cnst.RESULT_CODE_EXCEPTION_ERROR){
			var responseText = null;
			if(options._response.responseText!=""){
				responseText = Ext.util.JSON.decode(options._response.responseText);
				if(responseText.messages){
					responseText.messages.forEach(function(msg){
						
						//var decodeMessage= //Ext.util.JSON.decode(msg);
						if((typeof msg)=="object"){
							TRF.util.showMessageBoxByMsgIdAndArgs(msg.messageCode,msg.messageArgs);
							return;
						}
					});
				}
			}
			//}
			
			if(func!=undefined){
				func(records, options, success, arguments, responseText);	
			}
		}
	});
};

/**
 * create a store.
 * @param requestUrl the request Url
 * @param modelName the name of model
 * @param storeId the id of store
 * @return store the json store 
 */
TRF.util.createMemoryStore = function(modelName, storeId) {
	TRF.util.outDebugMessage('(trf.js)[createMemoryStore] start storeId:' + storeId);

	var store = Ext.create('Ext.data.Store',{
		storeId:storeId,
		autoLoad:false,
		model:modelName,
		loadMask:true,
		proxy:{
			type:'memory',
			reader:{
				type:'json'
			}
		}
	});

    var startTime=undefined;
    var endTime;
    store.on("beforeload", function(obj, options){
        startTime=new Date();
    });
    store.on("load", function(obj, records, options){
    	endTime=new Date();
        TRF.util.outPerformanceLog(requestUrl, requestUrl, endTime.getTime()-startTime.getTime());
    });
    
	return store;
};

/**
 * create a store.
 * @param requestUrl the request Url
 * @param modelName the name of model
 * @param storeId the id of store
 * @return store the json store 
 */
TRF.util.createStore = function(applicationId, requestUrl, modelName, storeId, listeners, params) {
	TRF.util.outDebugMessage('(trf.js)[createStore] start request Url:' + requestUrl + ',storeId:' + storeId);

    if(Ext.isEmpty(params)){
         params = {}
    }
    
    // set defulat parameter
    params[TRF.cnst.TOKEN] = TRF.core.token;
    params[TRF.cnst.SCREENID] = applicationId;
    
    // make url
    requestUrl = TRF.util.makeUrlForApplication(requestUrl, applicationId);
    
    // create store
	var store = Ext.create('Ext.data.Store', {
		 model:modelName,
		 loadMask:true,
		 autoLoad:false,
		 pageSize : TRF.cnst.PAGE_SIZE_COMMON,
	     proxy: {
	     	type: 'ajax',
            url:requestUrl,
            timeout : TRF.cnst.getApplicationContentsTimeout,
            actionMethods:{create: "POST", read: "POST", update: "POST", destroy: "POST"},
            extraParams : params,
            paramsAsJson:true,
            noCache: false,
            headers: { "Accept": 'application/json', "Content-Type": 'application/json;' },
	        reader: {
				type : 'json',
				rootProperty:'datas',
				totalProperty:'totalCount',
				'messageCode':'messageCode',
				'message':'message'
	       	}
	    },
	    storeId:storeId,
	    listeners:(Ext.isEmpty(listeners)?{}:listeners)
	});

    store.on("load", function(obj, records, successful, options){
    	if(!successful){
    		if(options._callback === undefined){
                if(!Ext.isEmpty(options) && !Ext.isEmpty(options.error)){
                    if(options.error.status == TRF.cnst.NETWORK_ERROR){//'0'
                        TRF.util.showMessageBoxByMsgIdAndArgs("e0002");     
                    }else if(options.error.status == TRF.cnst.RESULT_CODE_SESSION_TIMEOUT){//'401'
                        TRF.util.exitApp(); 
                    }
                    return;
                }
                obj.removeAll();
                var statusCode=options._response.status;
                //if(statusCode == TRF.cnst.RESULT_CODE_EXCEPTION_ERROR){//'201'
                if(options._response.responseText!=""){
                    var responseText = Ext.util.JSON.decode(options._response.responseText);
                    if(responseText.messages){
                        responseText.messages.forEach(function(msg){
                            //var decodeMessage=Ext.util.JSON.decode(msg);
                            if((typeof msg) == "object"){
                                TRF.util.showMessageBoxByMsgIdAndArgs(msg.messageCode,msg.messageArgs);
                                return;
                            }
                        });
                    }
                }
               // }
    		}   	      
    	}
    	
    });
        
    return store;
};

/**
 * Add application.
 * @param applicationId	the screen id
 * @param screenId the action code
 * @param clsSession the flag of keep session(true/false)
 * @param params the params of screen load
 */
TRF.util.addApplication = function(applicationId, screenId, clsSession, params, tabIndex) {

	// アクティブなトランザクションが存在する場合は無視
	if (TRF.core.isActiveTransactionExists()) return false;

	// デフォルトfalse
	var clearSession = false;
	if(arguments.length >= 2){
		if(typeof(clsSession) == 'boolean'){
			clearSession = clsSession;
		}
	}

	//var contentId = 'D' + applicationId.substr(1);
	var needConfirm = false;
    var clearOld = false;

    if(TRF.core.appMgr.indexOf(applicationId)>=0){
        clearOld=true;
        var app = Ext.getCmp(applicationId);
        if (app.isEdited != undefined) {
            if (typeof(app.isEdited) == 'boolean') {
                needConfirm = app.isEdited;
            } else if (typeof(app.isEdited) == 'function') {
                needConfirm = app.isEdited();
            }
        }
    }
    
    var f = function() {
		var tranId = Ext.id();
		TRF.core.startTransaction(tranId);
        TRF.core.getApplicationContents(applicationId, screenId, tranId, false, clearSession, params, tabIndex, false, clearOld);
	};
    
    if (needConfirm) {
    	TRF.util.showMessageBoxByMsgIdAndArgs('c0001', null, function(btn) {
			if (btn == 'yes') {
				f();
			}
		});
	} else {
        f();
    }
};

/**
 * The function of replace application.
 * 
 */
TRF.util.replaceAppCallBack = Ext.emptyFn;

/**
 * Replace application.
 * @param applicationId	the screen id
 * @param clsSession the flag of keep session(true/false)
 * @param contentId the contentId
 * @param isEdited the edit flag
 */
TRF.util.replaceApplication = function(applicationId, clsSession, params, contentId, isEdited) {

	var tabs = Ext.getCmp(TRF.cnst.APPLICATION_TABPANNEL_ID);
	var tabIndex=undefined;
	tabs.items.each(function(item,index){
		var key = item.id;
		if (key == contentId) {
			tabIndex = index;
			return false;
		}
	});
	
	var needConfirm = false;
    if (isEdited && TRF.core.appMgr.indexOf(contentId) >= 0) {
		clearOld = true;
		var app = Ext.getCmp(contentId);
		if (app.isEdited != undefined) {
			if (typeof(app.isEdited) == 'boolean') {
				needConfirm = app.isEdited;
			} else if (typeof(app.isEdited) == 'function') {
				needConfirm = app.isEdited();
			}
		}
	}

    var f = function() {
		// アプリケーションマネージャをクリア
		TRF.util.replaceAppCallBack = TRF.util.removeApplication.createCallback(contentId);
		TRF.util.addApplication(applicationId, applicationId, clsSession, params , tabIndex);
	};

    if (needConfirm) {
		TRF.util.showMessageBoxByMsgIdAndArgs('c0001', null, function(btn) {
			if (btn == 'yes') {
				f();
			}
		});
	} else {
        f();
    }
};

/**
 * Close Application.
 * @param applicationId	the	screen id
 */
TRF.util.removeApplication = function(applicationId) {
    TRF.core.appMgr.remove(applicationId);
	var tabs = Ext.getCmp(TRF.cnst.APPLICATION_TABPANNEL_ID);
	tabs.remove(Ext.getCmp(applicationId), true);
};

/**
 * Load an application as a popup.
 * Callback function is need.
 * @param applicationId the screen id
 * @param applicationId the action code
 * @param params the params
 * @param popSize the popup screen size
 * @param cbFunc the call back function
 */
TRF.util.popupApplication = function(applicationId, screenId, params, popSize, cbFunc ,parentCmp){
	//ignore popup if trasaction active.
    if (TRF.core.isActiveTransactionExists()) return false;
	
    var tranId = Ext.id();
    TRF.core.startTransaction(tranId);

    TRF.core.mask(TRF.core.maskMsg.application);

    //Performance log start point
    var startTime=new Date();

	// ストアの生成
	var contentStore = TRF.util.createStore(applicationId, "screenContent", TRF.util.createContentModle, "contentStore") ;
	
	// set common
	var storeParam = {};
    storeParam[TRF.cnst.TOKEN] = TRF.core.token;
    storeParam[TRF.cnst.SCREENID] = screenId;

	// アプリケーションのロード
	contentStore.load({
		params: storeParam,
		callback:function(records, operation, success){
			TRF.core.endTransaction(tranId);
			var tabs = Ext.getCmp(TRF.cnst.APPLICATION_TABPANNEL_ID);
			var contents = contentStore.getProxy().getReader().rawData;
			
			if(!Ext.isEmpty(operation) && !Ext.isEmpty(operation.error)){
				if(operation.error.status==TRF.cnst.NETWORK_ERROR){
					TRF.util.showMessageBoxByMsgIdAndArgs("e0002");		
				}else if(operation.error.status==TRF.cnst.RESULT_CODE_SESSION_TIMEOUT){
						TRF.util.exitApp();
				} 
				contents=undefined;
        	}else{
				var statusCode=operation._response.status;
				if(statusCode == TRF.cnst.RESULT_CODE_EXCEPTION_ERROR){
					contents=undefined;
					var errorMessage=['Content: '+applicationId+' load failed!'];
					TRF.util.outMessage(errorMessage);
				}	        		
        	}
			
            if(contents==undefined){
				tabs.doLayout();
				tabs.disabled = false;
				TRF.core.handleReadyCmp.clear();
				TRF.core.unmask();
            }
			else {
				TRF.core.handleReadyCmp.clear();
				TRF.core.unmask();
                TRF.util.addPopupApplication(contents, params, popSize, cbFunc ,parentCmp);
			}
		}
	});

	//forbid parent screen's tab 
   	var activeTab = Ext.getCmp(TRF.cnst.APPLICATION_TABPANNEL_ID).getActiveTab();	
   	if(parentCmp){
		activeTab = parentCmp;
	}
   	activeTab.popupFlg = true;
   	
    var endTime=new Date();
    var logTime=endTime.getTime()-startTime.getTime();

	// メッセージエリアの初期化
    TRF.util.outPerformanceLog(applicationId, screenId, logTime);
};

/**
 * Inner function for popupApplication function.
 * @param contents loaded by application id
 * @param param
 * @param cbFunc
 */

TRF.util.addPopupApplication = function(contents, params, popSize, cbFunc ,parentCmp){
    //default pop size is TRF.cnst.POPUP_MIDIUM
    if(popSize==undefined || popSize.indexOf("*")==-1){
        popSize=TRF.cnst.POPUP_MIDIUM;
    }
    
    var tranId = Ext.id();
    
    var width=0, height=0;
    var sizeArray=popSize.split('*');
    width=parseInt(sizeArray[0], 10);
    height=parseInt(sizeArray[1], 10);
    var winId=contents.id+"_Popup";
    contents.header = false;
    win = new Ext.Window({
	    	//partial:true,
	        id: winId,
	        layout:'fit',
	        title: contents.title,
	        //autoDestroy:false,
	        resizable: false,
	        //collapsible : false,
	        //maximizable : true,
	        //plain : true,
	        //closable:true,
	        ghost: false,
	        //parentCmp :parentCmp,
	        modal: true,
	        border: false,
	        contrain :true,
            constrainHeader:true,
	        width: width,
	        height: height,
	        items:[contents],
	        listeners:{
	        	'beforeclose':function(win){
	        		var popupCallBackFunc = TRF.core.popupCallbackMgr.map[contents.id];
	        		if(popupCallBackFunc && Ext.isFunction(popupCallBackFunc)){
	        			popupCallBackFunc();
	        			TRF.core.popupCallbackMgr.remove(contents.id);
	        		}
	        		TRF.core.appMgr.remove(contents.id);
	        		// init parent screen's tab 
	    			// var activeTab = Ext.getCmp(TRF.cnst.APPLICATION_TABPANNEL_ID).getActiveTab();
	    			if(win.parentCmp){
	    				activeTab = win.parentCmp;
	    			}
	        	},
	        	'show':function(win){
            		//win.header.tools[0].el.dom.children[0].classList.remove('x-tool-img');
            		//win.header.tools[0].el.dom.children[0].classList.add('x-tool-big-img');
            		win.header.tools[0].el.dom.children[0].className = win.header.tools[0].el.dom.children[0].className.replace('x-tool-img','');
            		win.header.tools[0].el.dom.children[0].className = win.header.tools[0].el.dom.children[0].className.replace('x-tool-close','');
                    win.header.tools[0].el.dom.children[0].className += ' x-tool-big-img';
	        	}
	        }
   	 });
    //Show popup window.
    win.show();
	var content = TRF.core.handleReadyPopupCmp;
    TRF.util.registHandleReadyCmp(winId, false, win.el, content);
    if(cbFunc!=undefined){
        TRF.core.popupCallbackMgr.add(contents.id, cbFunc);
    }

    var comp=Ext.getCmp(contents.id);
    if(comp===undefined){
    	comp = Ext.ComponentMgr.create(contents);
    }
    //popup screen set
    comp.popup = true;
    Ext.getCmp(contents.id).isPopup = true;
    //func set
    
    comp.registHandleReadyCmp = function(id, msg) {
    	return TRF.util.registHandleReadyCmp(id, msg, win.el, content);
    };
    comp.handleReady = function(id){
    	return TRF.util.handleReady(id, win.el, content);
    };

    TRF.core.loadingStatusMgr.add(contents.id,TRF.util.genCounter());
    if(comp.initialize!=undefined){
    	TRF.util.dataLoadStart(contents.id);
        comp.initialize(params);
        TRF.util.dataLoadEnd(contents.id);
    }

    TRF.core.appMgr.add(contents.id);
    // Mingyu CAI modified
    var pos = win.getPosition(true);
    if (pos[1] < 0) {
    	win.setPosition(pos[0], pos[1] + 145);
    }
    
	var funcxx = function(runCount) {
		if(!TRF.util.isDataLoading(contents.id)){
			TRF.util.taskRunner.stop(this);
			TRF.core.loadingStatusMgr.remove(contents.id);
            //add
            TRF.util.handleReady(winId, win.el, content);
			TRF.util.iteratesChildInput(comp, function(cmp) {
				if (comp.needConfirmChange) {
					TRF.util.addOnChangeListenerToInputAndGrid(cmp, function(){
						if (!(cmp.needConfirmChange === false)) {
							if (!comp.isEdited) {
								comp.isEdited = true;
							}
						}
					});
				}
				TRF.util.runFunctionToInput(cmp, function(){
					if (!cmp.readOnly && !cmp.allowBlank) {
						cmp.setFieldStyle('background:rgb(255, 255, 153);');
					}
					
					cmp.on('change',function(o, v){
                        if(o.xtype != 'combo' && o.xtype != 'combobox' && o.xtype != 'hcombo' && o.xtype != 'checkcombo' && o.xtype != 'datefield'){
                            if(o.xtype == 'numberfield'){
                                if (typeof(v) == "string") {
                                    //o.value = v.replace(/[^\u0000-\u00ff]/g,'');
								}
                            }else{
                                //o.setValue(v.replace(/[^\u0000-\u00ff]/g,''));
                            }
                          
                        }
                    }, cmp);
				});
			});
			/*if (comp.needConfirmChange) {
				TRF.util.addOnChangeListenerToChildInputAndGrid(comp, function() {
					if(!comp.isEdited){
						comp.isEdited = true;
					}
				});
			}
			TRF.util.doFuncToChildInput(comp, function(cmp) {
				if (!cmp.readOnly && !cmp.allowBlank) {
					cmp.setFieldStyle('background:yellow;');
				}
			});*/
			
			TRF.core.endTransaction(tranId);
		}
	};
	TRF.util.taskRunner.start({
		run : funcxx,
		interval : 50
	});
};


/**
 * Close a popup window.
 * @param contents
 */
TRF.util.removePopupApplication = function(applicationId) {
	var win = Ext.getCmp(applicationId+"_Popup");
	if (win) {
		win.close();
        TRF.core.appMgr.remove(applicationId);
        TRF.core.popupCallbackMgr.remove(applicationId);
	}
};

/**
 * The common function of form submit.
 * 
 * @param form the submit form
 * @param func the call back function
 * @param screenId the screen id
 * @param requestUrl the request Url
 * @param applicationData the submit data
 * @param submitType the submit type
 * @param downloadRequestUrl the Request Url for download 
 * @return submit result
 */
TRF.util.simpleSubmit = function(form, func, screenId, requestUrl, applicationData, submitType, downloadRequestUrl, filtersParam) {

	TRF.util.outDebugMessage('(trf.js)[simpleSubmit] start');
	if (TRF.core.isActiveTransactionExists()) {
		return;
	}

	var url = requestUrl;
	var trnId = Ext.id();
	TRF.core.startTransaction(trnId);
	//マスク処理
	//if(this.popup){
	var cmpEl = undefined;
	//if(form.owner.isPopup){
	if(form.owner.isPopup || (Ext.getCmp(screenId) && Ext.getCmp(screenId).isPopup)){
		//this.registHandleReadyCmp(trnId);
	    cmpEl = form.owner.isPopup ? form.owner.up() : Ext.getCmp(screenId).up();
		TRF.util.registHandleReadyCmp(trnId, undefined, cmpEl);
	}else{
		TRF.util.registHandleReadyCmp(trnId);	
	}

   	TRF.util.outDebugMessage('(trf.js)[simpleSubmit]:URL = ' + url);
   	
	// post parameterの作成
	var params = {};
	if(submitType !== TRF.cnst.ACTION_CD_UPLOAD) {
    	var filters = (arguments.length >= 8 && arguments[7] == null) ? {} : filtersParam;
    	if (arguments.length >= 5 && arguments[4] != null) {
    		
    		//applicationDataの指定がある場合
    		params = {
    			'swapData': applicationData,
    			'filters' : filters,
    			'selectedDatas' : applicationData[TRF.cnst.SELECTEDDATAS]
    		};
    	} else {
    		params = {
    			'swapData': {},
    			'filters' : filters,
    			'selectedDatas':{}
    		};
    	}
	}
	// clientTime
    var clientTime = Ext.util.Format.date(new Date(), TRF.cnst.GLOBAL_DATETIME_WITH_MS_FORMAT);
    // set defulat parameter
    params[TRF.cnst.TOKEN] = TRF.core.token;
    params[TRF.cnst.SCREENID] = screenId;
    params[TRF.cnst.CLIENTTIME] = clientTime;
	
	// set defalut values
	url = TRF.util.makeUrlForApplication(url, screenId);
    
    // set back fuction
	form.applicationCallbackFn = func;
	form.trnId = trnId;

    //performance log start
    var startTime=new Date();
	form.submit({
		url: url,
		scope: this,
		params: params,
		clientValidation: false,
    	success: function(form, action) {
    		TRF.core.endTransaction(form.trnId);
    		//if(this.popup){
    		/*if(form.owner.isPopup || Ext.getCmp(screenId).isPopup){
    			//this.handleReady(trnId);
    		    var el = form.owner.isPopup ? form.owner.up() : Ext.getCmp(screenId).up();
    			TRF.util.handleReady(form.trnId, el);
    		}else{*/
    		TRF.util.handleReady(form.trnId, cmpEl);
    		/*}*/
    		
    		var response = {};
    		if(action.response.responseText && action.response.responseText != "") {
    			response = Ext.util.JSON.decode(action.response.responseText);
    		}
    
    		var applicationData = {
    			success:(null==response.messages?true:false),
    			totalCount:response.totalCount,
    			result:response.data,
    			messageCodes:response.messages
    		};
    
    		if(!applicationData.success){
    			var messageCodes = response.messages;
    			var boxMsgs = [];
    			var msgArray = [];
    			var multiFlg = false;
    			var type = "";
    			messageCodes.forEach(function(msgCodeObj) {
    				var messageCode = msgCodeObj.messageCode;
    				var messageArgs = msgCodeObj.messageArgs;
    				if (messageCode.indexOf("c") == 0) {
    					type = TRF.cnst.MESSAGEBOX_TYPE_QUESTION;
    					var messageText = TRF.util.getMessage(messageCode, messageArgs);
    					boxMsgs.push(messageText);
    				} else if (messageCode.indexOf("w") == 0) {
    					if (type == TRF.cnst.MESSAGEBOX_TYPE_QUESTION) {
    						var messageText = TRF.util.getMessage(messageCode, messageArgs);
    						msgArray.push(messageText);
        				} else {
        					type = TRF.cnst.MESSAGEBOX_TYPE_WARN;
        					if (messageCode.indexOf("_") == -1) {
                                var messageText = TRF.util.getMessage(messageCode, messageArgs);
                                boxMsgs.push(messageText);
                            } else {
                                var messageText = TRF.util.getMessage(messageCode, messageArgs);
                                msgArray.push(messageText);
                            };
        			    };
    				} else if (messageCode.indexOf("i") == 0) {
    					type = TRF.cnst.MESSAGEBOX_TYPE_INFO;
    					var messageText = TRF.util.getMessage(messageCode, messageArgs);
    					boxMsgs.push(messageText);
    					msgArray.push(messageText);
    				} else if (messageCode.indexOf("e") == 0) {
    					type = TRF.cnst.MESSAGEBOX_TYPE_ERROR;
    					var messageText = TRF.util.getMessage(messageCode, messageArgs);
    					boxMsgs.push(messageText);
    					msgArray.push(messageText);
    				} else {
                        type = TRF.cnst.MESSAGEBOX_TYPE_ERROR;
                        var messageText = TRF.util.getMessage(messageCode, messageArgs);
                        boxMsgs.push(messageText);
                        msgArray.push(messageText);
    				}; 				
    			});
    			
    			// if no detail message array
                if (msgArray.length == 0) {
                    // show message
                    Ext.apply(msgArray ,boxMsgs);
                }
                
                // if no pop-up message
                if (boxMsgs.length == 0) {
                    if(messageCodes[0] != null) {
                    	// split message
                    	var messageCode = messageCodes[0].messageCode;
                    	if(messageCode.indexOf("_") > 0) {
                    		messageCode = messageCode.substring(0, 5);
                    	}
                    	// reset
                        boxMsgs.push(TRF.util.getMessage(messageCode, null));
                    }
                }
    			
    			if (type != TRF.cnst.MESSAGEBOX_TYPE_QUESTION) {
    				TRF.util.showMessageBoxL(type, boxMsgs, msgArray);
    		    } else {
                    TRF.util.showMessageBoxL(type, boxMsgs, msgArray, function(btn) {
                    	applicationData.confirm = btn;
                        // callback
                        if(Ext.isFunction(func)){
                             func(applicationData);
                        }
                    });
                    return;
                }
    		}
    		
    		if (TRF.cnst.ACTION_CD_DOWNLOAD==submitType && applicationData.success) {
    			Ext.apply(response, params);
    		    TRF.util.doFormDownload.call(form , response, downloadRequestUrl, cmpEl);
    		} else {
    			// callback関数の実行
    			if(Ext.isFunction(form.applicationCallbackFn)){
    				form.applicationCallbackFn(applicationData);
    			}
    		}
    	},
		failure: function(form, action) {
			TRF.core.endTransaction(form.trnId);
			//if(this.popup){
            /*if(form.owner.isPopup || Ext.getCmp(screenId).isPopup){
                //this.handleReady(trnId);
                var el = form.owner.isPopup ? form.owner.up() : Ext.getCmp(screenId).up();
                TRF.util.handleReady(form.trnId, el);
			}else{
				TRF.util.handleReady(form.trnId);
			}*/
			TRF.util.handleReady(form.trnId, cmpEl);

			var statusCode=arguments[1].response.status;
			if(statusCode == TRF.cnst.RESULT_CODE_SESSION_TIMEOUT || action.response.responseText.indexOf("[login]")>0)
			{
				TRF.util.exitApp();
				TRF.core.handleReadyCmp.clear();
				TRF.core.unmask();
				return;
			}

			if (action.failureType == Ext.form.Action.CONNECT_FAILURE) {
				TRF.util.showMessageBoxByMsgIdAndArgs("e0002");
			}else{
				var response = Ext.util.JSON.decode(action.response.responseText);
				if(response.messages){
					response.messages.forEach(function(msg){	
						//var decodeMessage=Ext.util.JSON.decode(msg);
						if((typeof msg)=="object"){
							TRF.util.showMessageBoxByMsgIdAndArgs(msg.messageCode, msg.messageArgs);	
						}
					});
				}
			}
		}
	});

    var endTime=new Date();
    var logTime=endTime.getTime()-startTime.getTime();
    TRF.util.outPerformanceLog(screenId, requestUrl, logTime);
};

/**
 * The function of action submit.
 * @param func the call back function
 * @param applicationId the screen id
 * @param actionUrl the action code
 * @param applicationData the submit data
 * @param dataType the ajax request param type
 */
TRF.util.actionSubmit = function(func, applicationId, actionUrl, applicationData) {

	TRF.util.outDebugMessage('(trf.js)[actionSubmit] start');

	//timeout
	var form = TRF.core.createTempForm(TRF.cnst.actionSubmitTimeout);

	// 内部でsimpleSubmitをcall
	TRF.util.simpleSubmit.call(this,form, func, applicationId, actionUrl, applicationData);
};

/**
 * The function of upload submit.
 * @param form the upload form
 * @param func the call back function
 * @param applicationId the screen id
 * @param actionUrl the action code
 * @param applicationData the submit data
 */
TRF.util.uploadSubmit = function(form, func, applicationId, actionUrl, applicationData) {
	TRF.util.outDebugMessage('(trf.js)[uploadSubmit] start');
	form.fileUpload = true;
	
	// set jsonData
	if(form.owner) {
		var jsonField = null;
    	if(!form.owner.down('#Common_trf_jsonData')) {
        	var jsonField = Ext.create('Ext.form.field.Text', {
        		xtype: 'textfield',
                name: TRF.cnst.JSONDATA,
                id:'Common_trf_jsonData',
                hidden : true
        	});
        	form.owner.add(jsonField);
    	} else {
    		jsonField = form.owner.down('#Common_trf_jsonData');
    	}
    	
    	// set into datas
    	var param = {'swapData':applicationData};
    	
    	// set value
    	jsonField.setValue(Ext.encode(param));
	}
	
	//timeout
	form.timeout = TRF.cnst.uploadSubmitTimeout,

	// 内部でsimpleSubmitをcall
	TRF.util.simpleSubmit.call(this,form,func,applicationId, actionUrl,applicationData,TRF.cnst.ACTION_CD_UPLOAD);
};

/**
 * The function of upload submit.
 * @param func the call back function
 * @param applicationId the screen id
 * @param actionUrlForCheck the action code for download check
 * @param actionUrl the action code
 * @param applicationData the submit data
 * @param eventsObservable the eventsObservable
 */
TRF.util.downloadSubmit = function(func, applicationId, urlForCheck, applicationData , urlForDownload, isHasFilters, eventsObservable) {

	TRF.util.outDebugMessage('(trf.js)[downloadSubmit] start');

	// formオブジェクトの生成
	var downloadForm = TRF.core.createDownloadForm(eventsObservable,TRF.cnst.downloadSubmitTimeout);
	
	var filters = null;
	if (isHasFilters == null || isHasFilters == true) {
		// get filers
		// if no items
		var app = Ext.getCmp(applicationId);
		var toolbar = app.findPlugin("headerfiltertoolbar");
        filters = toolbar ? toolbar.getHandlerFilters() : {};
	}

	// 内部でsimpleSubmitをcall
	TRF.util.simpleSubmit(downloadForm, func, applicationId, urlForCheck, applicationData,TRF.cnst.ACTION_CD_DOWNLOAD, urlForDownload, filters);
};

/**
 * Create form for action submit.
 * @param eventsObservable	the eventsObservable
 * @param timeout the timeout
 * 
 */
TRF.core.createDownloadForm = function(eventsObservable,timeout) {
	var downloadFormCmp = Ext.getCmp('downloadForm');
	if (downloadFormCmp == undefined) {
		downloadFormCmp = new Ext.FormPanel({
			id:'downloadForm',
			renderTo:document.body,
			fileUpload:true,
			jsonSubmit:true,
			hidden:true,
			timeout:timeout,
			items:[{}]
		});
	}
	var bform = downloadFormCmp.getForm();
	eventsObservable !== undefined ? (bform.eventsObservable = eventsObservable) : (delete bform.eventsObservable);
	return bform;
};

/**
 * Get file for from server's temp folder.
 * @param response the response
 * @param msg the error message
 * @param downloadactionUrl the download action code
 */
TRF.util.doFormDownload = function(response, downloadactionUrl, el) {
        var id = Ext.id();
        //if(Ext.isIE){
       	// TRF.util.registHandleReadyCmp(id);
   	    TRF.util.registHandleReadyCmp(id, undefined, el);
        //}
        var form = document.createElement('form');       
        form.target = id;
        form.method = 'POST';
        form.action = downloadactionUrl;
        
        var frame = document.createElement('iframe');
        frame.id = id;
        frame.name = id;
        frame.className = 'x-hidden';
        /*frame.onreadystatechange = function() {
			if (frame.readyState == "interactive") {
				TRF.util.handleReady(id);
				frame.onreadystatechange = Ext.emptyFn;
				setTimeout(function() {
							Ext.removeNode(form);
							Ext.removeNode(frame);
						}, 1800000);
			}
		};*/

        if(Ext.isIE){
            frame.src = Ext.SSL_SECURE_URL;
        }
        document.body.appendChild(frame);

        if(Ext.isIE){
           document.frames[id].name = id;
        }
        
        var hd = document.createElement('input');
        var jsonData = Ext.encode(response);
        hd.type = 'hidden';
        hd.name = TRF.cnst.JSONDATA;
        hd.value = jsonData;
        form.appendChild(hd);
        
        // add token
        hd = document.createElement('input');
        hd.type = 'hidden';
        hd.name = TRF.cnst.TOKEN;
        hd.value = TRF.core.token;
        form.appendChild(hd);
        
        // add screen id
        var applicationId = response[TRF.cnst.SCREENID];
        hd = document.createElement('input');
        hd.type = 'hidden';
        hd.name = TRF.cnst.SCREENID;
        hd.value = applicationId;
        form.appendChild(hd);
                
        // add clint time
        var clientTime = response[TRF.cnst.CLIENTTIME];
        hd = document.createElement('input');
        hd.type = 'hidden';
        hd.name = TRF.cnst.CLIENTTIME;
        hd.value = clientTime;
        form.appendChild(hd);
        
        document.body.appendChild(form);
        //var resultMessageId = undefined;
        try {
            form.submit();
        	//resultMessageId = "i1002";
        }catch(err){
        	//resultMessageId = "e0009";
        	TRF.util.showMessageBoxByMsgIdAndArgs('e0009');
        }
        //TRF.util.showMessageBoxByMsgIdAndArgs(resultMessageId);
        if(this.eventsObservable!==undefined){
        	 this.eventsObservable.fireEvent('download', this);
        }
        
        /*if(Ext.isIE10p){
        	setTimeout(function() {
        		TRF.util.handleReady(id);
        		setTimeout(function() {
                    Ext.removeNode(form);
                    Ext.removeNode(frame);
        		}, 18000000);
        	}, 3000);
        }*/
        var checkFun = function() {
            // call
            var params = {};
            params[TRF.cnst.JSONDATA] = jsonData;
            params[TRF.cnst.TOKEN] = TRF.core.token;
            params[TRF.cnst.SCREENID] = applicationId;
            params[TRF.cnst.CLIENTTIME] = clientTime;
            // check
            Ext.Ajax.request({
                url : TRF.util.makeUrlForApplication('main/checkDownload', applicationId),
                timeout: TRF.cnst.actionSubmitTimeout,
                jsonData : params,
                method : 'POST',
                success : function(result, request) {
                    var response = {};
                    if (result.responseText && result.responseText != "") {
                        response = Ext.util.JSON.decode(result.responseText);
                    }
                    var success = (null==response.messages || Ext.isEmpty(response.messages)) ? true : false;
                    if (success) {
                        var value = response.data;
                        if (!Ext.isEmpty(value) && value == '0') {
                            setTimeout(checkFun, 500);
                        } else if (!Ext.isEmpty(value) && value == '9') {
                            TRF.util.handleReady(id, el);
                            Ext.removeNode(form);
                            Ext.removeNode(frame);
                            TRF.util.showMessageBoxByMsgIdAndArgs("w1005");
                        } else {
                            TRF.util.handleReady(id, el);
                            TRF.util.showMessageBoxByMsgIdAndArgs("i1003");
            setTimeout(function() {
                   Ext.removeNode(form);
                   Ext.removeNode(frame);
                            }, 18000000);
                        }
                    }
                },
                failure : function(response) {
                    TRF.util.showMessageBoxByMsgIdAndArgs("e0002");
                    TRF.util.handleReady(id, el);
                    Ext.removeNode(form);
                    Ext.removeNode(frame);
                }
            });
        }
        // start check
        setTimeout(checkFun, 1000);
    };

/**
 * Create form for action submit.
 * @param func				コールバック関数
 * @param applicationId		アプリケーションID
 * @param actionUrl		アクションコード
 * @param applicationData	追加パラメータ（アプリケーションデータにラップしてサブミット）
 */
TRF.core.createTempForm = function(timeout) {
	var tempFormCmp = Ext.getCmp('tempForm'); 
	if (tempFormCmp == undefined) {
		// formオブジェクトの生成
		tempFormCmp = new Ext.FormPanel({
			id:'tempForm',
			renderTo:document.body,
			hidden:true,
			timeout:timeout,
			jsonSubmit:true,
			items:[{}]
		});
	}
	return tempFormCmp.getForm();
};

/**
 * Show message box.
 * @param type the type of message
 * @param messageCode the message code
 * @param func the call back function
 * @param htmlEncode the html encode
 * 
 */
TRF.util.showMessageBox = function(type, message, func, htmlEncode) {
	
	// OKボタン押下時のアクション(デフォルト)
	var afterFn = Ext.emptyFn;
	
	if (func != undefined) {
		afterFn = func;
	}
		
	var _message = htmlEncode === false ? message : Ext.util.Format.htmlEncode(message);
	var _formatMessage = '<span style="display:inline-block; width:220px; white-space:pre-warp; word-wrap:break-word; overfolw:hidden; margin:0;">' + _message + '</span>';
	var msgBox = null;
	switch(type) {
	
		// modify by Wenting QIU for UserLogin_LI-1-2-E1 on 2015-01-21 start
		case TRF.cnst.MESSAGEBOX_TYPE_INFO:
			msgBox = Ext.Msg.show({
				title: chinaplus.label.Common_Message_Information,
				msg: _formatMessage,
				buttons: Ext.Msg.OK,
				icon: Ext.Msg.INFO,
				fn: function() {
					if(afterFn != Ext.emptyFn) {
					   afterFn();
					}
				}
			});
			TRF.util.outMessage([_message],false);
			break;
		// modify by Wenting QIU for UserLogin_LI-1-2-E1 on 2015-01-21 end
			
		// modify by Wenting QIU for UserLogin_LI-2-2-E1 on 2015-01-21 start
		case TRF.cnst.MESSAGEBOX_TYPE_WARN: 
			msgBox = Ext.Msg.show({
				title: chinaplus.label.Common_Message_Warning,
				msg: _formatMessage,
				buttons: Ext.Msg.OK,
				icon: Ext.Msg.WARNING,
				fn: function() {
					if(afterFn != Ext.emptyFn) {
                       afterFn();
                    }
				}
			});
			TRF.util.outMessage([_message],false);
			break;
		// modify by Wenting QIU for UserLogin_LI-2-2-E1 on 2015-01-21 end
			
		case TRF.cnst.MESSAGEBOX_TYPE_ERROR: 
			msgBox = Ext.Msg.show({
				title: chinaplus.label.Common_Message_Error,
				msg: _formatMessage,
				buttons: Ext.Msg.OK,
				icon: Ext.Msg.ERROR,
				fn: function() {
					if(afterFn != Ext.emptyFn) {
                       afterFn();
                    }
				}
			});
			TRF.util.outMessage([_message],false);
			break;
		case TRF.cnst.MESSAGEBOX_TYPE_QUESTION: 
			msgBox = Ext.Msg.show({
				title: chinaplus.label.Common_Message_Confirm,
				msg: _formatMessage,
				buttons: Ext.MessageBox.YESNO,
				icon: Ext.MessageBox.QUESTION,
				fn : function(btn) {
							if (btn == 'yes') {
								if(afterFn != Ext.emptyFn) {
                                    afterFn(btn);
                                }
							} else {
								if(afterFn != Ext.emptyFn) {
                                    afterFn(btn);
                                }
								TRF.core.handleReadyCmp.clear();
								TRF.core.unmask();
							}
						}
			});
			break;
		default:
			break;
	}
	if(msgBox != null){
		msgBox.focus();
	}
};

/**
 * Show message box and messageList.
 * @param type the type of message
 * @param messages Pop up message
 * @param messageList Message list
 * @param func the call back function
 * @param htmlEncode the html encode
 * 
 */
TRF.util.showMessageBoxL = function(type, messages, messageList, func, htmlEncode) {
	// OKボタン押下時のアクション(デフォルト)
	var afterFn = Ext.emptyFn;
	
	if (func != undefined) {
		afterFn = func;
	}
	
	// sort
	TRF.util.messageSort(messages);
	TRF.util.messageSort(messageList);
	
	var _formatMessage = null;
	var _message = null;
	var msgBox = null;
	if(typeof messages=="string"){
		_message = htmlEncode === false ? messages : Ext.util.Format.htmlEncode(messages);
		_formatMessage = '<span style="display:inline-block; width:220px; white-space:pre-warp; word-wrap:break-word; overfolw:hidden; margin:0;">' + _message + '</span>';
	
	} else {
		var tmpMsg = null;
		for (var j = 0, length = messages.length; j < length; j++) {
			tmpMsg = htmlEncode === false ? messages[j] : Ext.util.Format.htmlEncode(messages[j]);
			if (_message != null) {
				_message += '<br>';
				_message += tmpMsg;
			} else {
				_message = tmpMsg;
			}
		}
		_formatMessage = '<span style="display:inline-block; width:220px; white-space:pre-warp; word-wrap:break-word; overfolw:hidden; margin:0;">' + _message + '</span>';
	}

	switch(type) {
	
		// modify by Wenting QIU for UserLogin_LI-1-2-E1 on 2015-01-21 start
		case TRF.cnst.MESSAGEBOX_TYPE_INFO:
			msgBox = Ext.Msg.show({
				title: chinaplus.label.Common_Message_Information,
				msg: _formatMessage,
				buttons: Ext.Msg.OK,
				icon: Ext.Msg.INFO,
				fn: function() {
					if(Ext.isFunction(afterFn)){
					   afterFn();
					}
				}
			});
			TRF.util.outMessage(messageList, false);
			break;
		// modify by Wenting QIU for UserLogin_LI-1-2-E1 on 2015-01-21 end
			
		// modify by Wenting QIU for UserLogin_LI-2-2-E1 on 2015-01-21 start
		case TRF.cnst.MESSAGEBOX_TYPE_WARN: 
			msgBox = Ext.Msg.show({
				title: chinaplus.label.Common_Message_Warning,
				msg: _formatMessage,
				buttons: Ext.Msg.OK,
				icon: Ext.Msg.WARNING,
				fn: function() {
					if(Ext.isFunction(afterFn)){
                       afterFn();
                    }
				}
			});
			TRF.util.outMessage(messageList, false);
			break;
		// modify by Wenting QIU for UserLogin_LI-2-2-E1 on 2015-01-21 end
			
		case TRF.cnst.MESSAGEBOX_TYPE_ERROR: 
			msgBox = Ext.Msg.show({
				title: chinaplus.label.Common_Message_Error,
				msg: _formatMessage,
				buttons: Ext.Msg.OK,
				icon: Ext.Msg.ERROR,
				fn: function() {
					if(Ext.isFunction(afterFn)){
                       afterFn();
                    }
				}
			});
			TRF.util.outMessage(messageList, false);
			break;
		case TRF.cnst.MESSAGEBOX_TYPE_QUESTION:
				msgBox = Ext.Msg.show({
				title: chinaplus.label.Common_Message_Confirm,
				msg: _formatMessage,
				buttons: Ext.MessageBox.YESNO,
				icon: Ext.MessageBox.QUESTION,
				fn : function(btn) {
							if (btn == 'yes') {
								if(Ext.isFunction(afterFn)){
                                   afterFn(btn);
                                }
							} else {
								if(Ext.isFunction(afterFn)){
                                   afterFn(btn);
                                }
								TRF.core.handleReadyCmp.clear();
								TRF.core.unmask();
							}
						}
				});
			TRF.util.outMessage(messageList, false);
			break;
		default:
			break;
	}
	if(msgBox != null){
		msgBox.focus();
	}
};

/**
 * Show message box by message id and args. Authomaticly determine message type.
 * @param msgId the id of message
 * @param msgArgs the arguments of message
 * @param func the call back function
 * @param htmlEncode the html encode
 * 
 */
TRF.util.showMessageBoxByMsgIdAndArgs = function(msgId, msgArgs, func, htmlEncode) {
	var type = "";
	if (Ext.isString(msgId)) {
		switch (msgId.substring(0, 1)) {
		case "e":
			type = TRF.cnst.MESSAGEBOX_TYPE_ERROR;
			break;
		case "w":
			type = TRF.cnst.MESSAGEBOX_TYPE_WARN;
			break;
		case "i":
		case "s":
			type = TRF.cnst.MESSAGEBOX_TYPE_INFO;
			break;
		case "c":
			type = TRF.cnst.MESSAGEBOX_TYPE_QUESTION;
			break;
		default:
			break;
		}
	}
	var curMsg = TRF.util.getMessage(msgId, msgArgs);
	TRF.util.showMessageBox(type, curMsg, func, htmlEncode);
};

/**
 *alert message.
 */
TRF.util.alert = function(message, func) {
	
	var afterFunc =func;
	if(afterFunc==undefined){
		afterFunc=function(){};
	}
	
	Ext.Msg.show({
		title: chinaplus.label.Common_Message_Information,
		msg: message,
		muitlLine:false,
		buttons: Ext.Msg.OK,
		icon: Ext.Msg.INFO,
		fn: func
	});
};

/**
 * Regist handle is completed.
 */
TRF.util.handleReady = function(id, el, content) {
	TRF.util.outDebugMessage('Remove:' + id);
	content ? content.remove(id) : TRF.core.handleReadyCmp.remove(id);
	var empty = content ? content.getCount() == 0 : TRF.core.handleReadyCmp.getCount() == 0;
	if (empty)
		TRF.core.unmask(el);
};

/**
 * Regist handle start.
 */
TRF.util.registHandleReadyCmp = function(id, msg, el, content) {
	var notEmpty = content ? content.getCount() != 0 : TRF.core.handleReadyCmp.getCount() != 0;
	if (notEmpty){
		el = el ? el : Ext.getCmp(TRF.cnst.VIEWPORT_ID).getEl();
		if(el._maskMsg){
			el._maskMsg.dom.firstChild.innerHTML = msg ? msg : TRF.core.maskMsg.process;
		}
	}else{
		TRF.core.mask(msg, false, el);	
	}
	content ? content.add(id) : TRF.core.handleReadyCmp.add(id);
	TRF.util.outDebugMessage(id);
};


/**
 * Check if exist active transaction.
 * 
 */
TRF.core.isActiveTransactionExists = function() {
	return (TRF.core.activeTransaction != undefined);
};

/**
 * The start of transaction.
 * @param id the id of transaction
 */
TRF.core.startTransaction = function(id) {
	TRF.core.activeTransaction = id;
};

/**
 * The end of transaction.
 * @param id the id of transaction
 */
TRF.core.endTransaction = function(id) {
	TRF.core.activeTransaction = undefined;
};

/**
* Init the message area
*
*/
TRF.util.initMessageArea = function(){
	var msgArea = Ext.getCmp('messageArea');
	msgArea.expand(); //Modify for Extjs5.1.2
	var initStr = {"initMessage":""};
	var initMsgTpl = '{initMessage}';
	var initMsg = new Ext.Template(initMsgTpl);

	initMsg.overwrite(msgArea.body,initStr);

	// メッセージエリアのスクロール
	msgArea.body.scrollTo('top', 0);

	// メッセージエリアを強制的に閉じる（animateなし）
	if (msgArea.collapsed) {
		msgArea.collapsed = false;
	}
	msgArea.collapse(false);
};

/**
 * Output message in message area.
 * @param strArray the content of message
 * @param htmlEncode the html code
 */
TRF.util.outMessage = function(strArray, htmlEncode) {
	var msgArea = Ext.getCmp('messageArea');
	if(msgArea==undefined){
		return;
	}
	var now;
	if (TRF.core.language == chinaplus.consts.code.Language.CN) {
		now = Ext.util.Format.date(new Date(), TRF.cnst.GLOBAL_DATE_YEAR_MONTH_DAY_FORMAT) + " " + Ext.util.Format.date(new Date(), TRF.cnst.SHORT_TIME_WITH_SECOND_FORMAT);
    } else {
    	now = Ext.util.Format.date(new Date(), TRF.cnst.GLOBAL_DATETIME_LONG_WITH_SECOND_FORMAT);
    }
	var errorStr = [];
	if(typeof strArray=="string"){
		errorStr[0] = errorStr[i] = ({"message":' [ ' + now + ' ] &nbsp;&nbsp;' + strArray});
	}else{
		if(strArray!=null&&strArray!=""){
		for (var j = 0, length = strArray.length; j < length; j++) {
			if(!strArray[j]){
				strArray.remove(strArray[j]);
			}else{
				strArray[j] = (htmlEncode === false ? strArray[j] : Ext.util.Format.htmlEncode(strArray[j]));
			}
		}
		for(var i=0;i<strArray.length;i++){
			errorStr[i] = ({"message":' [ ' + now + ' ] &nbsp;&nbsp;' + strArray[i]});
		}
	}
	}

	// メッセージのテンプレート
	var errorMsgTpl = '{message}<br/>';
	var errorMsg = new Ext.Template(errorMsgTpl);

	// メッセージ出力
	for (var i=0,length =errorStr.length; i<length; i++) {
		var e =errorStr[i];
		if (e) {
			errorMsg.append(msgArea.body, e);
		}
	}

	if (errorStr.length > 0) {
		// メッセージエリアを展開
		msgArea.expand();
	}

	// メッセージエリアのスクロール
	msgArea.body.scrollTo('top', msgArea.body.getHeight(true));
};

/**
* Output debug message(message area)
* @param str the contents of message
*
*/
TRF.util.outDebugMessage = function(str){
	
	// 0:nomal,1:debug
	if (TRF.cnst.DEBUG_MODE != '1') return;

	var msgArea = Ext.getCmp('messageArea');
	var errorStr = ({"message":str});
	var errorMsgTpl = new Date().toLocaleString() + ' [Debug] {message}<br/>';
	var errorMsg = new Ext.Template(errorMsgTpl);

	// Output message
	errorMsg.append(msgArea.body, errorStr);

	// メッセージエリアを展開
	msgArea.expand();

	// メッセージエリアのスクロール
	msgArea.body.scrollTo('top', msgArea.body.getHeight(true));

};



/**
 * Get label by key.
 * @params key the label key
 */
TRF.util.getLabel = function(key){

	var label = chinaplus.label[key];
	
	return label;
};

/**
 * put message list to message area.
 * @params messages [{messageCode:xxx,messageCode:['',''...]},...]
 */
TRF.util.outputMessages = function(messages){

	var outMessages=[];
	Ext.each(messages, function(message) {
		var messageContent=TRF.util.getMyMessage(message);
		if(messageContent!=""){
			outMessages.push(messageContent);	
		}
	});
	TRF.util.outMessage(outMessages);
};

/**
 * Get message content by message.
 * @params message the message  {messageCode:xxx,messageCode:['',''...]}
 * @return xxx
 */
TRF.util.getMyMessage = function(message){

	return TRF.util.getMessage(message.messageCode,message.messageArgs);
};

/**
 * Get message by message code.
 * @params messageCode the message code
 * @params messageArguments the message Arguments
 */
TRF.util.getMessage = function(messageCode, messageArguments){
	var message = chinaplus.message[messageCode];
	if (message == undefined) {
		message = messageCode;
	} else {
		message += ('(' + messageCode + ')');
	}
	
	if (messageArguments) {
		return TRF.util.makeMessage(message, messageArguments);
	} else {
		return message;
	}
};

/**
 * Set the arguments of message.
 * @param message the message content
 * @param messageArguments the arguments of message
 * @return message the message
 */
TRF.util.makeMessage = function(message, messageArguments){

	if(arguments[1] == null){
		return message;
	}
	
	if (messageArguments instanceof Array) {
		message = message.replace(/{(\d+)}/g, 
			function($0, $1) {
				var msg=messageArguments[parseInt($1)];
				try {
					msg=eval(messageArguments[parseInt($1)]);
					if (typeof msg != 'string' || msg == '') {
						msg = messageArguments[parseInt($1)];
					} 
				}catch(e) {
					//
				}
				
				// international
                if(chinaplus.label[msg]) {
                    msg = chinaplus.label[msg];
                }
				
				return msg;
			});
	} else {
		var msg = messageArguments;
		try {
			msg = eval(messageArguments);
			if (typeof msg != 'string') {
				msg = messageArguments;
			}
		}catch(e) {
			// do nothing
		}
		
		// international
		if(chinaplus.label[msg]) {
			msg = chinaplus.label[msg];
		}
		
		// convert
		message = message.replace(/\{0\}/g, msg);
	}

	return message;
};

/**
* Output Performance log(message area)
* 
* @param applicationId	the screen id
* @param actionUrl	the actionUrl
* @param str the contents of message
*
*/
TRF.util.outPerformanceLog = function(applicationId, actionCode, times, start, end){
	// TRF.cnst.PERFORMANCE_LOG_MODE = 0:nomal, 1:debug ,2:debug/db save
	var errorStr = ({
		"applicationId" : applicationId,
		"actionCode" : actionCode,
		"times" : times,
		"start" : start,
		"end" : end
	});

	switch (TRF.cnst.PERFORMANCE_LOG_MODE) {
		case '0' :
			break;
		case '1' : 
			var msgArea = Ext.getCmp('messageArea');
			var errorMsgTpl = new Date().toLocaleString();
			if(start){
			     errorMsgTpl = errorMsgTpl + ' [Performance] {applicationId} / {actionCode} , Time: {times} ms, From {start} To {end} <br/>';
			}else{
			     errorMsgTpl = errorMsgTpl + ' [Performance] {applicationId} / {actionCode} , Time: {times} ms <br/>';
			}
			var errorMsg = new Ext.Template(errorMsgTpl);
			// メッセージ出力
			errorMsg.append(msgArea.body, errorStr);
			// メッセージエリアを展開
			msgArea.expand();
			// メッセージエリアのスクロール
			msgArea.body.scrollTo('top', msgArea.body.getHeight(true));
			
			// db save
			//var message = new Ext.Template('[Page] {applicationId} - {actionId} times:{times}ms').apply(errorStr);
			//TRF.util.callAjax("writePerformanceLog", { 'message' : message });
			break;
		default :
			break;
	}
};

/**
* The type of maskmsg.
*
*/
TRF.core.maskMsg = {
	cacheApplication: chinaplus.label.Common_Label_LoadCacheApp ? chinaplus.label.Common_Label_LoadCacheApp : 'Load Cache Application...',
	application : chinaplus.label.Common_Label_LoadApplication? chinaplus.label.Common_Label_LoadApplication : 'Load Application...',
	process : chinaplus.label.Common_Label_Processing? chinaplus.label.Common_Label_Processing : 'Processing...',
	wait : chinaplus.label.Common_Label_Waiting? chinaplus.label.Common_Label_Waiting : 'Waiting...',
	load : chinaplus.label.Common_Label_Loading? chinaplus.label.Common_Label_Loading : 'Loading...'
};

/**
* Add mask.
*
* @param msgType the type of mask.
* @param msgCls the css od mask.
* @param el the mask area.
*/
TRF.core.mask = function(msgType, msgCls ,el) {
	if (el)
		el.mask(msgType ? msgType : TRF.core.maskMsg.process, msgCls ? msgCls : 'x-mask-loading');
	else {
		var viewport = Ext.getCmp(TRF.cnst.VIEWPORT_ID);
		viewport.getEl().mask(msgType ? msgType : TRF.core.maskMsg.process, msgCls ? msgCls : 'x-mask-loading');
	}

};

/**
* Remove mask.
*
* @param el the mask area.
*/
TRF.core.unmask = function(el) {
	if (el)
		el.unmask();
	else {
		var viewport = Ext.getCmp(TRF.cnst.VIEWPORT_ID);
		viewport.getEl().unmask();
	}
};

/**
* Show Authentication error.
*
*/
TRF.util.exitApp = function() {
	Ext.Msg.show({
				title : "error",
				msg : chinaplus.message.e0003+"(e0003)",
				buttons : Ext.Msg.OK,
				icon : Ext.Msg.ERROR,
				fn : function(btn) {
					if(btn=='ok') {
						window.location.href = TRF.cnst.INDEX_PAGE;
					}
				}
			});
};

var nothing = function() {
	/**
	 * private flag, to determine whether the detail tab to be open should be
	 * editable or not.
	 */
	var privateDetaiTabMode = "view";

	/**
	 * public function. To get the flag which determines whether the detail tab
	 * should be editable or not.
	 */
	TRF.util.isDetailTabMode_New = function() {
		return privateDetaiTabMode === "new";
	};
	
	/**
	 * public function. To get the flag which determines whether the detail tab
	 * should be editable or not.
	 */
	TRF.util.isDetailTabMode_Edit = function() {
		return privateDetaiTabMode === "edit";
	};
	
	/**
	 * public function. To get the flag which determines whether the detail tab
	 * should be editable or not.
	 */
	TRF.util.isDetailTabMode_View = function() {
		return privateDetaiTabMode === "view";
	};

	/**
	 * public function. To get the flag which determines whether the detail tab
	 * should be editable or not.
	 */
	TRF.util.setDetailTabMode_New = function() {
		privateDetaiTabMode = "new";
	};
	
	/**
	 * public function. To get the flag which determines whether the detail tab
	 * should be editable or not.
	 */
	TRF.util.setDetailTabMode_Edit = function() {
		privateDetaiTabMode = "edit";
	};
	
	/**
	 * public function. To get the flag which determines whether the detail tab
	 * should be editable or not.
	 */
	TRF.util.setDetailTabMode_View = function() {
		privateDetaiTabMode = "view";
	};
}();

/**
* Make specified Ext component looks like read only.
*
* @param cmp the Ext component.
*/
TRF.util.makeCmpReadOnly = function(cmp, errorFlag) {
	//cmp.inputEl.dom.unselectable = true;
	cmp.setReadOnly(true);
	cmp.on('focus',function(c){
		//Ext.getCmp('tabpanelId').focus();
		if(Ext.isIE){
			if (Ext.ieVersion < 11) {
                if(Ext.isEmpty(document.selection.createRange().text)){
                    Ext.getCmp('tabpanelId').focus();
                }
            }else{
                if(Ext.isEmpty(window.getSelection.createRange().text)){
                    Ext.getCmp('tabpanelId').focus();
                }
            }
        }else{
            var dom = c.inputEl.dom;
            if(dom.selectionEnd - dom.selectionStart == 0){
                Ext.getCmp('tabpanelId').focus();
            }
        }
	});
	if (cmp.getXType() != 'checkboxfield' && cmp.getXType() != 'checkbox') {
//		cmp.setFieldStyle('background:none;');
		if(!Ext.isEmpty(errorFlag) && errorFlag === true){
		  cmp.setFieldStyle('background:#f50000;');
		}else{
		  cmp.setFieldStyle('background:#cccccc;');
		}
		  cmp.triggerEl.setVisible(false);
		if (Ext.isIE8) {
			cmp.inputWrap.el.dom.style.borderBottom = 0;
			cmp.inputWrap.el.dom.style.borderTop = 0;
			cmp.inputWrap.el.dom.style.borderRight = 0;
			cmp.inputWrap.el.dom.style.borderLeft = 0;
		} else {
			cmp.inputWrap.setBorder(0);
		}
	}
	
    // check allow blank
    if (cmp.allowBlank != null && cmp.allowBlank == false) {
        cmp.allowBlank = true;
    }
};

/**
 * Ajax submit.
 * @param {} func callback function
 * @param {} applicationId applicationId
 * @param {} url url
 * @param {} applicationData json Data
 * @param {} rawParams raw params
 */
TRF.util.ajaxSubmit = function(func, applicationId, url, applicationData, rawParams) {
	// check duble click
	if (TRF.core.isActiveTransactionExists()) {
        return;
    }
	
	var trnId = Ext.id();
    TRF.core.startTransaction(trnId);
    //if(form.owner.isPopup){
    var cmpEl = undefined;
    if(Ext.getCmp(applicationId) && Ext.getCmp(applicationId).isPopup){
        //this.registHandleReadyCmp(trnId);
        cmpEl = Ext.getCmp(applicationId).up();
        TRF.util.registHandleReadyCmp(trnId, undefined, cmpEl);
    }else{
    TRF.util.registHandleReadyCmp(trnId);
    }
	
    // clientTime
    var clientTime = Ext.util.Format.date(new Date(), TRF.cnst.GLOBAL_DATETIME_WITH_MS_FORMAT);
    
    // post parameterの作成
    var params;
    if (arguments.length >= 4 && arguments[3] != null) {
    	params = {
    		'swapData': applicationData,
            'filters' : applicationData[TRF.cnst.FILTER_NAME],
            'selectedDatas' : applicationData[TRF.cnst.SELECTEDDATAS]
        };
    } else {
        params = {
        	'swapData': {},
        	'selectedDatas' : {},
        	'filters' : {}
        };
    }
    
    // clientTime
    var clientTime = Ext.util.Format.date(new Date(), TRF.cnst.GLOBAL_DATETIME_WITH_MS_FORMAT);
    // set defulat parameter
    params[TRF.cnst.TOKEN] = TRF.core.token;
    params[TRF.cnst.SCREENID] = applicationId;
    params[TRF.cnst.CLIENTTIME] = clientTime;
    
    // make new url
    url = TRF.util.makeUrlForApplication(url, applicationId);
    
    if (arguments.length == 5) {
        //rawParamsの指定がある場合、paramsに追加
        Ext.apply(params, rawParams);
    }
    
    Ext.Ajax.request({
        url : url,
        timeout: TRF.cnst.actionSubmitTimeout, // add timeout setting
        jsonData : params,
        method : 'POST',
        success : function(result, request) {
        	TRF.core.endTransaction(trnId);
        	TRF.util.handleReady(trnId, cmpEl);
        	
        	var response = {};
            if(result.responseText && result.responseText != "") {
                response = Ext.util.JSON.decode(result.responseText);
            }
    
            var responseData = {
                success:(null==response.messages || Ext.isEmpty(response.messages))?true:false,
                totalCount:response.totalCount,
                result:response.data,
                messageCodes:response.messages
            };
    
            if(!responseData.success){
                var messageCodes = response.messages;
                var boxMsgs = [];
                var msgArray = [];
                var multiFlg = false;
                var type = "";
                
                // make message
                messageCodes.forEach(function(msgCodeObj) {
                    var messageCode = msgCodeObj.messageCode;
                    var messageArgs = msgCodeObj.messageArgs;
                    if (messageCode.indexOf("c") == 0) {
                        type = TRF.cnst.MESSAGEBOX_TYPE_QUESTION;
                        var messageText = TRF.util.getMessage(messageCode, messageArgs);
                        boxMsgs.push(messageText);
                    } else if (messageCode.indexOf("w") == 0) {
                        if (type == TRF.cnst.MESSAGEBOX_TYPE_QUESTION) {
                            var messageText = TRF.util.getMessage(messageCode, messageArgs);
                            msgArray.push(messageText);
                        } else {
                            type = TRF.cnst.MESSAGEBOX_TYPE_WARN;
                            if (messageCode.indexOf("_") == -1) {
                                var messageText = TRF.util.getMessage(messageCode, messageArgs);
                                boxMsgs.push(messageText);
                            } else {
                                var messageText = TRF.util.getMessage(messageCode, messageArgs);
                                msgArray.push(messageText);
                            };
                        };
                    } else if (messageCode.indexOf("i") == 0) {
                        type = TRF.cnst.MESSAGEBOX_TYPE_INFO;
                        var messageText = TRF.util.getMessage(messageCode, messageArgs);
                        boxMsgs.push(messageText);
                        msgArray.push(messageText);
                    } else if (messageCode.indexOf("e") == 0) {
                        type = TRF.cnst.MESSAGEBOX_TYPE_ERROR;
                        var messageText = TRF.util.getMessage(messageCode, messageArgs);
                        boxMsgs.push(messageText);
                        msgArray.push(messageText);
                    } else {
                        type = TRF.cnst.MESSAGEBOX_TYPE_ERROR;
                        var messageText = TRF.util.getMessage(messageCode, messageArgs);
                        boxMsgs.push(messageText);
                        msgArray.push(messageText);
                    };              
                });
                
                // if no message array
                if(msgArray.length == 0) {
                	
                	// show message
                	Ext.apply(msgArray ,boxMsgs);
                }
                
                // if no pop-up message
                if (boxMsgs.length == 0) {
                    if(messageCodes[0] != null) {
                        // split message
                        var messageCode = messageCodes[0].messageCode;
                        if(messageCode.indexOf("_") > 0) {
                            messageCode = messageCode.substring(0, 5);
                        }
                        // reset
                        boxMsgs.push(TRF.util.getMessage(messageCode, null));
                    }
                }
                
                if (type != TRF.cnst.MESSAGEBOX_TYPE_QUESTION) {
                    TRF.util.showMessageBoxL(type, boxMsgs, msgArray);
                } else {
                	TRF.util.showMessageBoxL(type, boxMsgs, msgArray, function(btn) {
                		responseData.confirm = btn;
                		// callback
                        if(Ext.isFunction(func)){
                             func(responseData);
                        }
                	});
                	return;
                }
            }
            
            // callback
            if(Ext.isFunction(func)){
                func(responseData);
            }
        },
        failure : function(response) {
        	TRF.core.endTransaction(trnId);
        	TRF.util.handleReady(trnId, cmpEl);
            var statusCode=response.status;
            if(statusCode == TRF.cnst.NETWORK_ERROR){
                TRF.util.showMessageBoxByMsgIdAndArgs("e0002"); 
                return;
            }else if(statusCode == TRF.cnst.RESULT_CODE_SESSION_TIMEOUT)
            {
                TRF.util.exitApp();
                TRF.core.handleReadyCmp.clear();
                return;
            }
            if(response.responseText!=""){
                var responseText = Ext.util.JSON.decode(response.responseText);
                if(responseText.messages){
                    responseText.messages.forEach(function(msg){
                        
                        //var decodeMessage=Ext.util.JSON.decode(msg);
                        if((typeof msg)=="object"){
                            TRF.util.showMessageBoxByMsgIdAndArgs(msg.messageCode, msg.messageArgs); 
                        }
                    });
                }
            }
        }
    });
};

/**
 * Set detail information for detail screen by url.
 * @param {} func
 * @param {} application
 * @param {} url
 * @param {} applicationData
 * @param {} rawParams
 */
TRF.util.detailSubmit = function(func, application, url, applicationData, rawParams) {
	
	// back function
	var backfun = function(responseData) {
		
		// set values
		if(responseData.success) {
			
			// get result
			var result = responseData.result;
			if(!result || !Ext.isObject(result)) {
				return;
			}
			
			// values set function
			var valueSetfun = function(item) {
				// if item is not exit
				if(item == null || item == 'undefined') {
                    return ;
                }
				
                // set value
				if(item.xtype == "checkcombo" || item.xtype == "combo" || item.xtype == 'combobox') {
                    if(item.name != null && result[item.name] != null && result[item.name] != 'null') {
                        if(!item.readOnly) {
                            item.setValue(result['' + item.name]);
                        } else {
                        	if(item.displayName != null) {
                                item.setRawValue(result[item.displayName]);
                        	} else if(item.master != null) {
                        		item.setRawValue(chinaplus.master.getMasterName(item.master, result[item.name]));
                        	} else {
                        		item.setRawValue(result[item.name]);
                        	}
                        }
                    }
				} else if(item.xtype == "textfield" || item.xtype == "numberfield" || item.xtype == "datefield"
                     || item.xtype == "textareafield" || item.xtype == "textarea") {
                    if(item.name != null && result[item.name] != null && result[item.name] != 'null') {
                        if(item.displayName != null) {
                            item.setValue(result[item.displayName]);
                        } else if(item.master != null) {
                            item.setValue(chinaplus.master.getMasterName(item.master, result[item.name]));
                        } else {
                            item.setValue(result[item.name]);
                        }
                    }
                } else if(item.xtype == "checkboxfield") {
					if(item.name != null && result[item.name] != null && result[item.name] != 'null') {
						if(result[item.name] == true || result[item.name] == 'true' || ('' + result[item.name]) == '1') {
                            item.checked = true;
						} else {
							item.checked = false;
						}
                    }
                } else if(item.xtype == "form" || item.xtype == "container" || item.xtype == "fieldcontainer" 
                            || item.xtype == "fieldset" || item.xtype == "panel" || item.xtype == "tabpanel") {
                   // for each items
                   if (item.items != null && item.items.getCount() > 0) {   
                       Ext.each(item.items.items, valueSetfun, this);
                   } 
                }
			};
			
			// loop
			if(application.items != null && application.items.getCount() > 0) {
			   Ext.each(application.items.items, valueSetfun, this);
			}
		}
		
		// call back
		if(Ext.isFunction(func)){
            func(responseData);
        }
	};
	
	// call ajax
    TRF.util.ajaxSubmit.call(this, backfun, application.id, url, applicationData, rawParams);
};

/**
 * Get detail screen information.
 * @param {} application
 * @return {}
 */
TRF.util.getDetailInfo = function(comp) {
	
	var param = {};
	
    // values set function
    var valueSetfun = function(item) {
        // if item is not exit
        if(item == null || item == 'undefined') {
            return ;
        }
        
        // set value
        if(item.xtype == "checkcombo" || item.xtype == "combo" || item.xtype == 'combobox') {
            if(item.name != null) {
            	if(item.getValue() != null) {
            		param[item.name] = '' + item.getValue();
            	} else {
            		param[item.name] = '' + item.getRawValue();
            	}
            }
            
        } else if(item.xtype == "textfield" || item.xtype == "numberfield" || item.xtype == "datefield"
             || item.xtype == "textareafield" || item.xtype == "textarea") {
            if(item.name != null) {
                param[item.name] = '' + item.getRawValue();
            }
            
        } else if(item.xtype == "checkboxfield") {
            if(item.name != null) {
                if(item.checked == true) {
                    param[item.name] = '1';
                } else {
                    param[item.name] = '0';
                }
            }
        } else if(item.xtype == "container" || item.xtype == "fieldcontainer" || item.xtype == "fieldset") {
           // for each items
           if (item.items != null && item.items.getCount() > 0) {   
               Ext.each(item.items.items, valueSetfun, this);
           } 
        } else if(item.xtype == "form") {
        	Ext.apply(param, item.getForm().getFieldValues());
        } else if(item.xtype == "panel" || item.xtype == "tabpanel") {
            Ext.each(item.items.items, valueSetfun, item);
        }
    };
    
    // loop
    if(comp.xtype == 'form') {
    	return comp.getForm().getFieldValues();
    } else if(comp.items != null && comp.items.getCount() > 0) {
       Ext.each(comp.items.items, valueSetfun, this);
    }
	
    return param;
};

/**
 * message sort.
 * @param {} messages
 */
TRF.util.messageSort = function(messages) {
   if(Ext.isArray(messages)) {
       Ext.Array.sort(messages, function(a, b) {
       	    return a.substr(a.lastIndexOf('(')) > b.substr(b.lastIndexOf('('));
       });
   }
};

TRF.util.addBrToString = function(str, length, bufferLength) {
    var result = '';
    var strLength = str.length;
    /*for(var i = 0; i < strLength/length; i++){
        result += str.substring(length*i,length*(i+1));
        if(i < strLength/length-1){
            result += '<br>';
        }
    }*/
    if(str.replace(/[^x00-xff]/g,'xx').length<=length){
        return str;
    }
    
    var I=0;
    var schar;
    
    for(var i=0;schar=str.charAt(i);i++){
        result+=schar;
        I+=(schar.match(/[^x00-xff]/)!=null?2:1);
        if(I>=length){
            if(schar==' '){
                result+='<br>';
                I=0;
            }
        }
        if(bufferLength && I>=length+bufferLength){
            result+='<br>';
            I=0;
        }
    }
    
    return result;
};

TRF.util.addGridTip = function(grid) {
	var view = grid.getView();
	var tip = Ext.create('Ext.tip.ToolTip', {
	    // The overall target element.
	    target: view.el,
	    // Each grid row causes its own separate show and hide..x-grid-cell.x-grid-cell-first
	    delegate: '.x-grid-cell',
	    // Moving within the row should not hide the tip.
	    trackMouse: true,
	    // Render immediately so that tip.body can be referenced prior to the first show.
	    renderTo: Ext.getBody(),
	    tpl:'<div class="x-tooltip" style="text-align:left;">{0}</div>',
	    listeners: {
	        // Change content dynamically depending on which element triggered the show.
	        beforeshow: function updateTipBody(tip) {
	        	var tipStr = Ext.String.trim(tip.triggerElement.innerText);
	        	if(Ext.isEmpty(tipStr)){
	        		return false;
	        	}else{
	        		//tip.update(TRF.util.addBrToString(tipStr, 50, 10));
	        		tip.setWidth(tipStr.length*10>tip.minWidth?tipStr.length*10:tip.minWidth);
	        		tip.update(tipStr);
	        	}
	        }
	    }
	});
	var desListeners = grid.mon(grid,{
		destroyable : true,
		destroy : function() {
			tip.destroy();
			desListeners.destroy();
		}
	});
};

TRF.util.addFieldTipToPanel = function(panel) {
	var tip = Ext.create('Ext.tip.ToolTip', {
	    // The overall target element.
	    target: panel.el.dom,
	    // Each grid row causes its own separate show and hide..x-grid-cell.x-grid-cell-first
	    delegate: '.x-form-field[readonly]',
	    // Moving within the row should not hide the tip.
	    trackMouse: true,
	    // Render immediately so that tip.body can be referenced prior to the first show.
	    renderTo: Ext.getBody(),
	    tpl:'<div class="x-tooltip" style="text-align:left;">{0}</div>',
	    listeners: {
	        // Change content dynamically depending on which element triggered the show.
	        beforeshow: function updateTipBody(tip) {
        		var tipStr = Ext.String.trim(tip.triggerElement.value);
	        	if(Ext.isEmpty(tipStr)){
	        		return false;
	        	}else{
	        		//tip.update(TRF.util.addBrToString(tipStr, 50, 10));
	        		tip.setWidth(tipStr.length*10>tip.minWidth?tipStr.length*10:tip.minWidth);
	        		tip.update(tipStr);
	        	}
	        }
	    }
	});

	var desListeners = panel.mon(panel,{
		destroyable : true,
		destroy : function() {
			tip.destroy();
			desListeners.destroy();
		}
	});
	
	var tip2 = Ext.create('Ext.tip.ToolTip', {
        // The overall target element.
        target: panel.el.dom,
        // Each grid row causes its own separate show and hide..x-grid-cell.x-grid-cell-first
        delegate: '.x-field-input-ellipsis[readonly]',
        // Moving within the row should not hide the tip.
        trackMouse: true,
        // Render immediately so that tip.body can be referenced prior to the first show.
        renderTo: Ext.getBody(),
        tpl:'<div class="x-tooltip" style="text-align:left;">{0}</div>',
        listeners: {
            // Change content dynamically depending on which element triggered the show.
            beforeshow: function updateTipBody(tip) {
                var tipStr = Ext.String.trim(tip.triggerElement.value);
                if(Ext.isEmpty(tipStr)){
                    return false;
                }else{
                    //tip2.update(TRF.util.addBrToString(tipStr, 50, 10));
                	tip2.setWidth(tipStr.length*10>tip2.minWidth?tipStr.length*10:tip2.minWidth);
                	tip2.update(tipStr);
                }
            }
        }
    });

    var desListeners2 = panel.mon(panel,{
        destroyable : true,
        destroy : function() {
            tip2.destroy();
            desListeners2.destroy();
        }
    });
};

/**
 * right key forbid
 */
TRF.util.mouseClickEvent = function(){
	 
//	if(window.event.srcElement.tagName != undefined && window.event.srcElement.tagName.match(/^a$/i)==null){
//		if (/*document.selection.type == 'Text' ||*/
//			(window.event.srcElement.tagName.match(/^input$/i)!=null && !window.event.srcElement.disabled)
//		|| (window.event.srcElement.tagName.match(/^textarea$/i)!=null && !window.event.srcElement.disabled)) {
//			window.event.returnValue = true;
//		}else{
//			window.event.returnValue = false;
//		}
//	}else{
		//window.event.returnValue = false;
		return false;
//	}
};
document.oncontextmenu = TRF.util.mouseClickEvent;

/**
 * Record the count of times a screen begin to load data.
 * @param applicationId  the screen id
 * 
 */
TRF.util.dataLoadStart = function(applicationId){
	TRF.core.loadingStatusMgr.get(applicationId).add();
};

/**
 * Record the count of times a screen done loading data.
 * @param applicationId  the screen id
 * 
 */
TRF.util.dataLoadEnd = function(applicationId){
	TRF.core.loadingStatusMgr.get(applicationId).sub();
};

/**
 * See if a screen still loading data.
 * @param applicationId  the screen id
 * 
 */
TRF.util.isDataLoading = function(applicationId){
	return TRF.core.loadingStatusMgr.get(applicationId).getCount() != 0;
};

/**
 * Generate a new counter.
 */
TRF.util.genCounter = function(){
	return (function(){
		var counter = 0;
		this.add = function(){
			counter++;
		};
		this.sub = function(){
			counter--;
		};
		this.getCount = function(){
			return counter * 1;
		};
		return this;
	})();
};

/**
 * Add keyup listener to focusable link.
 */
TRF.util.addEnterListenerToFocusableLink = function(){
	Ext.query('.link-focusable').forEach(function(focusableLinkEle){
		var focusableLinkDomEle = Ext.get(focusableLinkEle);
		if(!focusableLinkDomEle.hasListeners.keyup){
			focusableLinkDomEle.on('keyup',function(eventObj,ele){
				if(eventObj.keyCode === 13){
					ele.click();
				}
			});
		}
	});
};

/**
 * Change the backgroud color when td is focused by tab key.
 * @param innerDiv  the customized inner div element in td
 * 
 */
TRF.util.changeTdBackOnFocus = function(innerDiv){
	innerDiv.parentElement.style.backgroundColor = '#b8cfee';
	innerDiv.style.backgroundColor = '#b8cfee';
	innerDiv.style.outline = 'none';
};

/**
 * Change the backgroud color when td lost focus by tab key.
 * @param innerDiv innerDiv  the customized inner div element in td
 * 
 */
TRF.util.changeTdBackOnBlur = function(innerDiv){
	innerDiv.parentElement.style.backgroundColor = '#FFCC99';
	innerDiv.style.backgroundColor = '#FFCC99';
};


/**
 * 
 * @param id the column id.
 * @param {} params the params of combo options loader.
 */
TRF.util.reloadComboOptions = function(applicationId , id, params, needSwap){
	var cmp = Ext.getCmp(id);
	var store = undefined;
	var combo = undefined;
	if(cmp.xtype == 'gridcolumn'){
	    store = cmp.filter.optionsStore;
        combo = cmp.filter.inputItem;
        if(store){
            if(!Ext.isEmpty(params)){
                if(store.params){
                	if(needSwap){
                	   Ext.apply(store.params, {swapData:params});
                	}else{
                	   store.params = params;
                	}
            	}else{
            	    store.params = {};
            	    if(needSwap){
                       Ext.apply(store.params, {swapData:params});
                    }else{
                       store.params = params;
                    }
            	}
            }
        }
        if(combo){
           combo.store = store;
           combo.isLoaded = false;
        }
	}else{
	    store = cmp.store;
        combo = cmp;
        if(store){
            if(!Ext.isEmpty(params)){
            	var applyParam = {};
                if(store.params){
                    if(needSwap){
                        applyParam.swapData = params;
                        Ext.apply(store.params, applyParam);
                    }else{
                        Ext.apply(applyParam, params);
                        store.params = applyParam;
                    }
                }else{
                    store.params = {};
                    if(needSwap){
                        applyParam.swapData = params;
                        Ext.apply(store.params, {swapData:params});
                    }else{
                    	Ext.apply(applyParam, params);
                        store.params = params;
                    }
                }
            }
        }
        combo.isLoaded = false;
	}
	
};

TRF.util.pickers = function(screenId, cmpId){
    if(!TRF.pickers.screen || TRF.pickers.screen !== screenId){
    	if(TRF.pickers.holder){
    		var tempCmp;
	        Ext.Array.each(TRF.pickers.holder.getKeys(), function(key){
	        	tempCmp = Ext.getCmp(key);
	        	if(tempCmp){
	        	  tempCmp.collapse();
	        	}
            });
    	}
        TRF.pickers.screen = screenId;
        TRF.pickers.holder = new Ext.util.HashMap();
    }
    
    var cmp = Ext.getCmp(cmpId);
    if(cmp){
    	cmp.removeListener('expand', TRF.util.autoCollapse);
        cmp.on('expand', TRF.util.autoCollapse, cmp);
        TRF.pickers.holder.add(cmp.id,'');
    }
};

TRF.util.autoCollapse = function(o){
	var tempCmp;
    Ext.Array.each(TRF.pickers.holder.getKeys(), function(key){
        if(o.id !== key){
        	tempCmp = Ext.getCmp(key);
        	if(tempCmp){
        	   tempCmp.collapse();
        	}
        }
    });
};

/**
 * get the system root path.
 * 
 * @return system root path
 */
TRF.util.getRequestPath= function(url){
	var pathName = document.location.href;
	var index = pathName.substr(1).lastIndexOf("/");
	var result = pathName.substr(0, index + 1);
	result=result+"/"+url;
	return result;
};

TRF.util.tabIndexElements = [];
TRF.util.tabIndex = function(screenCmp){
	var eleArray = Ext.Element.query('*[tabindex]',true,screenCmp.el.dom);
	var tempEle = undefined;
    var i;
    
    //dufault to previous tabindex collection
    for(i = 0; i < TRF.util.tabIndexElements.length; i++){
        tempEle = TRF.util.tabIndexElements[i];
        tempEle.tabIndex = -1;
    }
    TRF.util.tabIndexElements = [];
    
    //touch and filter a new index collection
	if(screenCmp.isPopup){//window with json
	    var tools = screenCmp.getRefOwner().tools;
	    
	    //window tools
	    for(i = 0;i<tools.length;i++){
	    	tempEle = tools[i].el.dom;
	        TRF.util.tabIndexElements.push(tempEle);
	    }
	}else if(screenCmp.xtype == 'window'){//window without json
		var tools = screenCmp.tools;
		
		//window tools
        for(i = 0;i<tools.length;i++){
            tempEle = tools[i].el.dom;
            TRF.util.tabIndexElements.push(tempEle);
        }
	}else{//tab with json
	    var tabs = Ext.getCmp(TRF.cnst.APPLICATION_TABPANNEL_ID).tabBar.items;
        var bannerArray = Ext.Element.query('A',true,Ext.getCmp(TRF.cnst.APPLICATION_BANNER_ID).el.dom);
        var aElement = undefined, aElementArray;
        
        //banner button and hyperlink
        for(i = 0;i<bannerArray.length;i++){
            tempEle = bannerArray[i];
            TRF.util.tabIndexElements.push(tempEle);
        }
        
        //tabs
        for(i = 0;i<tabs.length;i++){
            tempEle = tabs.getAt(i).el.dom;
            TRF.util.tabIndexElements.push(tempEle);
        }
	}
	
	//screen tabindex elements
    for(i = 0;i<eleArray.length;i++){
        tempEle = eleArray[i];
        if(tempEle.tagName == 'INPUT'){
            if(tempEle.getAttribute('readonly') == null && Ext.isEmpty(tempEle.style.display)){
                TRF.util.tabIndexElements.push(tempEle);
            }
        }else if(tempEle.tagName == 'A'){
            if(Ext.isEmpty(tempEle.style.display)){
                TRF.util.tabIndexElements.push(tempEle);
            }
        }else if(tempEle.tagName == 'DIV'){
            if(tempEle.className.indexOf('x-grid-header-checker') > -1){
                TRF.util.tabIndexElements.push(tempEle);
            }else{
                tempEle.tabIndex = -1;
            }
        }else if(tempEle.tagName == 'TR'){
            aElement = Ext.Element.query('div[tabindex]',true,tempEle)[0];
            if(aElement){
                TRF.util.tabIndexElements.push(aElement);
            }
            aElement = Ext.Element.query('a',true,tempEle)[0];
            if(aElement){
                TRF.util.tabIndexElements.push(aElement);
            }
            tempEle.tabIndex = -1;
        }else{
            tempEle.tabIndex = -1;
        }
    }
	
	//set tabindex to a new index collection
	for(i = 0;i<TRF.util.tabIndexElements.length;i++){
        tempEle = TRF.util.tabIndexElements[i];
        tempEle.tabIndex = i+1;
    }
	
    console.log(TRF.util.tabIndexElements);
};

/**
 * get Decimal Format.
 * @param v data value
 * @param uomCode the UOM Code
 * @return formated data
 */
TRF.util.getDecimalFormat=function(v, uomCode) {
	
	var digits = chinaplus.master.getMasterName(chinaplus.master.consts.UOMS, uomCode);
    return TRF.util.getDecimalFormatByDigits(v, digits);
};

/**
 * get Decimal Format.
 * @param v data value
 * @param digits the digits
 * @return formated data
 */
TRF.util.getDecimalFormatByDigits=function(v, digits) {
	
    var format = "###,##0";
    if (digits > 0) {
    	format += ".";
        for (var i = 0; i < digits; i++) {
        	format += "0";
        }
    }
    return Ext.util.Format.number(v, format);
};

/**
 * get Decimal Format.
 * @param v data value
 * @param uomCode the UOM Code
 * @return formated data
 */
TRF.util.getDecimalFormatNoComma=function(v, uomCode) {
	
    var digits = chinaplus.master.getMasterName(chinaplus.master.consts.UOMS, uomCode);
    var format = "#####0";
    if (digits > 0) {
    	format += ".";
        for (var i = 0; i < digits; i++) {
        	format += "0";
        }
    }
    return Ext.util.Format.number(v, format);
};

TRF.util.makeUrlForApplication=function(url, applicationId) {
    
    var makeUrl = url;
    makeUrl = makeUrl + "?" + TRF.cnst.TOKEN + "=" + TRF.core.token;
    makeUrl = makeUrl + "&" + TRF.cnst.SCREENID + "=" + applicationId;
     // clientTime
    var clientTime = Ext.util.Format.date(new Date(), TRF.cnst.GLOBAL_DATETIME_WITH_MS_FORMAT);
    makeUrl = makeUrl + "&" + TRF.cnst.CLIENTTIME + "=" + clientTime;
    
    return makeUrl;
};

TRF.util.decimalAdd = function(bd1, bd2){ 
     var m = Math.pow(10, TRF.cnst.MAX_DIGIT) 
     return (bd1*m + bd2*m)/m 
};

TRF.util.decimalSub = function(bd1, bd2){ 
     var m = Math.pow(10, TRF.cnst.MAX_DIGIT) 
     return (bd1*m - bd2*m)/m 
};

TRF.util.setDefaultValuesForToolBar = function(app, dataIndex, value, ltgt){
     var toolbar = app.findPlugin("headerfiltertoolbar");
     if(toolbar != null) {
        toolbar.setDefaultValues(dataIndex, value, ltgt);
     }
};

TRF.util.getSelectedValuesForToolBar = function(app){
	var toolbar = app.findPlugin("headerfiltertoolbar");
    var filters = toolbar ? toolbar.getHandlerFilters() : {};
    return filters;
};

TRF.util.createStaticOptions = function(dataArray){
	var optionArray = [];
	dataArray.forEach(function(data){
		var option = {};
		option["id"] = data;
		option["text"] = data;
		optionArray.push(option);
    });
    return optionArray;
};

TRF.util.arrayContains = function(array, value) {
	if (array == null || array.length == 0) {
		return false;
	}
	for (var i = 0; i < array.length; i++) {
		if (array[i] == value) {
			return true;
		}
	}
	return false;
};

TRF.util.clearFilter = function(application) {

		// values set function
		var valueSetfun = function(item) {
			// if item is not exit
			if(item == null || item == 'undefined') {
	            return ;
	        };
	        // set value
			if(item.xtype == "checkcombo" || item.xtype == "combo" || item.xtype == 'combobox') {
	            item.setValue(undefined);
			} else if(item.xtype == "textfield" || item.xtype == "numberfield" || item.xtype == "datefield"
	             || item.xtype == "textareafield" || item.xtype == "textarea") {
				item.setValue(undefined);
	        } else if(item.xtype == "checkboxfield") {
	        	item.setValue(undefined); 
	        } else if(item.xtype == "form"  || item.xtype == "fieldcontainer" || item.xtype == "container" 
	                    || item.xtype == "fieldset" || item.xtype == "panel" || item.xtype == "tabpanel") {
	           // for each items
	           if (item.items != null && item.items.getCount() > 0) {   
	               Ext.each(item.items.items, valueSetfun, this);
	           } 
	        }
		};
		
		// loop
		if(application.items != null && application.items.getCount() > 0) {
			Ext.each(application.items.items, valueSetfun, this);
		}
	


};


