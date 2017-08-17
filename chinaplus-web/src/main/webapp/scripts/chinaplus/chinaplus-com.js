/**
 * chinaplus-common.js
 * 
 * @screen COMMON		
 * @author zhang_pingwu
 */
/** ******** const ********* */
Ext.namespace('chinaplus.master', 'chinaplus.master.consts');

chinaplus.master.list = {};

chinaplus.master.consts.TnmCodeCategory_ActiveFlag = 'ACTIVE_FLAG';
chinaplus.master.consts.TnmCodeCategory_UserStatus = 'USER_STATUS';
chinaplus.master.consts.TnmCodeCategory_Language = 'LANGUAGE';
chinaplus.master.consts.TnmCodeCategory_TransportMode = 'TRANSPORT_MODE';
chinaplus.master.consts.TnmCodeCategory_InvoiceIrregularStatus = 'INVOICE_IRREGULAR_STATUS';
chinaplus.master.consts.TnmCodeCategory_InvoiceUploadStatus = 'INVOICE_UPLOAD_STATUS';
chinaplus.master.consts.TnmCodeCategory_InvoiceStatus = 'INVOICE_STATUS';
chinaplus.master.consts.TnmCodeCategory_PostRiFlag = 'POST_RI_FLAG';
chinaplus.master.consts.TnmCodeCategory_InvoiceRiStatus = 'INVOICE_RI_STATUS';
chinaplus.master.consts.TnmCodeCategory_InvoiceType = 'INVOICE_TYPE';
chinaplus.master.consts.TnmCodeCategory_InvoiceOrderStatus = 'INVOICE_ORDER_STATUS';
chinaplus.master.consts.TnmCodeCategory_OrderHistoryStatus = 'DIS_ORDER_STATUS';
chinaplus.master.consts.TnmCodeCategory_NonInvoicedQty = 'NON_INVOICED_QTY';
chinaplus.master.consts.TnmCodeCategory_CustomerForecastStatus = 'CUSTOMER_FORECAST_STATUS';
chinaplus.master.consts.TnmCodeCategory_BUSINESS_PATTERN='BUSINESS_PATTERN';
chinaplus.master.consts.TnmCodeCategory_BUSINESS_TYPE="BUSINESS_TYPE";
chinaplus.master.consts.TnmCodeCategory_DiscontinueIndicator = 'DISCONTINUE_INDICATOR';
chinaplus.master.consts.TnmCodeCategory_KanbanStatus = 'KANBAN_STATUS';
chinaplus.master.consts.TnmCodeCategory_KanbanPartStatus = 'KANBAN_PART_STATUS';
chinaplus.master.consts.TnmCodeCategory_ImpStockFlag = 'IMP_STOCK_FLAG';
chinaplus.master.consts.TnmCodeCategory_BUILD_OUT_INDICATOR='BUILD_OUT_INDICATOR';
chinaplus.master.consts.TnmCodeCategory_PARTS_STATUS='PARTS_STATUS';
chinaplus.master.consts.TnmCodeCategory_PARTS_TYPE='PARTS_TYPE';
chinaplus.master.consts.TnmCodeCategory_DELAY_ADJUST='ON_SHIPPING_DELAY_ADJUST_P';
chinaplus.master.consts.TnmCodeCategory_HAS_QTY_FLAG='HAS_QTY_FLAG';
chinaplus.master.consts.TnmCodeCategory_ALARM_FLAG='ALARM_FLAG';
chinaplus.master.consts.TnmCodeCategory_INACTIVE_FLAG='INACTIVE_FLAG';
chinaplus.master.consts.TnmCodeCategory_CUSTOMER_STOCK_FLAG='CUSTOMER_STOCK_FLAG';
chinaplus.master.consts.TnmCodeCategory_KbUploadFileType = 'KB_UPLOAD_FILE_TYPE';
chinaplus.master.consts.TnmCodeCategory_InvoiceMatchStatus='INVOICE_MATCH_STATUS';
chinaplus.master.consts.TnmCodeCategory_IF_BATCH_STATUS='IF_BATCH_STATUS';
chinaplus.master.consts.TnmCodeCategory_IF_BATCH_TYPE='IF_BATCH_TYPE';
chinaplus.master.consts.TnmCodeCategory_PRODUCT_MATERIAL='PRODUCT_MATERIAL';
chinaplus.master.consts.TnmCodeCategory_SECTION_OF_CAR='SECTION_OF_CAR';
chinaplus.master.consts.TnmCodeCategory_PRODUCTION_PROCESS='PRODUCTION_PROCESS';
chinaplus.master.consts.TnmCodeCategory_BUSINESS_WITH_TTC_FLAG='BUSINESS_WITH_TTC_FLAG';
chinaplus.master.consts.TnmCodeCategory_NDA_AGREEMENT_FLAG='NDA_AGREEMENT_FLAG';
chinaplus.master.consts.TnmCodeCategory_OVERALL_EVALUATION='OVERALL_EVALUATION';
chinaplus.master.consts.TnmCodeCategory_SUPPLIER_STATUS='SUPPLIER_STATUS';
chinaplus.master.consts.UserOffices = 'USER_OFFICES';
chinaplus.master.consts.UOMS = 'TNM_UOM';

// add end
chinaplus.master.consts.NOTNULL_OPTION = ["*"];

Ext.onReady(function(){
	var callbackFunc=function(contents){
		Ext.each(contents, function(content) {
			chinaplus.master.list = content;
		});
	};
	var model = 'Combo';
	if(!Ext.ClassManager.get(model)){
	    Ext.define('Combo',{
            extend:'Ext.data.Model',
            fields:[
                {name:'id',type:'string'},
                {name:'text',type:'string'}
            ]
        });
	}
	TRF.util.getDataByStore(TRF.cnst.MAINSCREEN, "common/master/load", "Combo",callbackFunc, {});
});

/**
 * get the status
 * 
 * @param category
 *            return master as an array of object
 */
chinaplus.master.getMasterByCategory = function(category) {
	if (Ext.isEmpty(category)) {
		return;
	}
	return chinaplus.master.list[category];
};

/**
 * get the status
 * 
 * @param category
 *            return master as an array of object
 */
chinaplus.master.getMasterByCategorySort = function(category, sort, hasBlank) {
	if (Ext.isEmpty(category)) {
		return;
	}
	var masterCodeArray = chinaplus.master.list[category];
	var comboConfArray = [];
	masterCodeArray.forEach(function(masterCodeObj) {
		comboConfArray.push([ masterCodeObj.id, masterCodeObj.text ]);
	});
	
	if(!Ext.isEmpty(sort) && sort == true) {
		comboConfArray = comboConfArray.reverse();
	}
	
	if(!Ext.isEmpty(hasBlank) && hasBlank == true) {
		comboConfArray.unshift(chinaplus.master.consts.BLANK_OPTION);
	}
	
	return comboConfArray;
};

/**
 * get the status for combo
 * 
 * @param category
 *            return master as an array of object
 */
chinaplus.master.getMasterByCategoryForCombo = function(category) {
	if (Ext.isEmpty(category)) {
		return;
	}
	var masterCodeArray = chinaplus.master.list[category];
	var comboConfArray = [];
	masterCodeArray.forEach(function(masterCodeObj) {
		comboConfArray.push([ isNaN(masterCodeObj.id * 1) ? masterCodeObj.id : masterCodeObj.id * 1, masterCodeObj.text ]);
	});
	
	return comboConfArray;
};

/**
 * get the status for combo
 * 
 * @param category
 *            return master as an array of object
 */
chinaplus.master.getSpoStatusWithoutCancelledForCombo = function() {
	
	var masterCodeArray = chinaplus.master.list[chinaplus.master.consts.TnmCodeCategory_SpoStatus];
	var comboConfArray = [];
	masterCodeArray.forEach(function(masterCodeObj) {
		if(masterCodeObj.id != 90){
			comboConfArray.push([ isNaN(masterCodeObj.id * 1) ? masterCodeObj.id : masterCodeObj.id * 1, masterCodeObj.text ]);
		}
		
	});
	
	return comboConfArray;
};

/**
 * get the master Code.
 * 
 * @param category
 * @param master
 *            Name return master Code
 */
chinaplus.master.getMasterCode = function(category, masterName) {
	if (Ext.isEmpty(masterName)) {
		return;
	}
	var status = chinaplus.master.getMasterByCategory(category);
	var masterCode = undefined;
	Ext.each(status, function(item) {

		if (item.text == masterName) {
			masterCode = item.id;
			return false;
		}
	});
	return masterCode;
};

/**
 * get the master name.
 * 
 * @param category
 * @param masterCode
 *            return master name
 */
chinaplus.master.getMasterName = function(category, masterCode) {
	if (Ext.isEmpty(masterCode)) {
		return;
	}
	var master = chinaplus.master.getMasterByCategory(category);
	var masterName = undefined;
	
	Ext.each(master, function(item) {
		if (item.id.toString() == masterCode.toString()) {
			masterName = item.text;
			return false;
		}
	});
	return masterName;
};

Ext.on('resize', function(x, y){
    var win = Ext.WindowManager.getActive();
    if(win){
    	if(win.maximized){
    	   win.restore();
    	   win.maximize();
    	}
    	win.center();
    }
});
