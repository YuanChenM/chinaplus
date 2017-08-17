/**
 * model.js
 * 
 * @screen common        
 * @author zhang_pingwu
 */
Ext.define('cpmpms01EntityList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'ttcPartNo',type:'string'},
        {name:'partsDesEnglish',type:'string'},
        {name:'partsDesChinese',type:'string'},
        {name:'oldTTCPartNo',type:'string'},
        {name:'impOfficeCode',type:'string'},
      
        {name:'businessPattern',type:'string'},
        {name:'ttcCustCd',type:'string'},
        {name:'ssmskbCustCd',type:'string'},    
        {name:'custPartNo',type:'string'},        
        {name:'exportCountry',type:'string'},
       
        {name:'ttcSuppCd',type:'string'},
        {name:'ssmsMainRoute',type:'string'},
        {name:'ssmsVendorRoute',type:'string'},
        {name:'ssmsSuppCd',type:'string'},
        {name:'suppPartNo',type:'string'},
        
        {name:'westCustCd',type:'string'},
        {name:'westPartNo',type:'string'},
        {name:'mailInvCustCd',type:'string'},
        {name:'ttcImpWHCd',type:'string'},
        {name:'partType',type:'string'},
       
        {name:'carModel',type:'string'},
        {name:'buildOutIndicator',type:'string'},
        {name:'partStatus',type:'string'},
        {name:'discontIndicator',type:'string'},
        {name:'createdDate',type:'string'},
       
        {name:'createdBy',type:'string'},
        {name:'lastModifiedDate',type:'string'},
        {name:'lastModifiedBy',type:'string'},
    ]
});

Ext.define('CPOList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'cpoId',type:'number'},
        {name:'customerPONo',type:'string'},
        {name:'partsNo',type:'string'},
        {name:'orderMonth',type:'date',convert:function(value){
            var year = value.substring(0,4) * 1;
            var month = value.substring(4) * 1 - 1;
            return new Date(year,month);
        }},
        {name:'targetMonth',type:'date',convert:function(value){
        	if (value !== null && value !== undefined && value !== '') {
            var year = value.substring(0,4) * 1;
            var month = value.substring(4) * 1 - 1;
            return new Date(year,month); }
        }},
        {name:'orderType',type:'number'},
        {name:'customerCode',type:'string'},
        {name:'customerOpeningStockDate',type:'string'},    
        {name:'customerPOReferenceNo',type:'string'},        
        {name:'impCountry',type:'string'},
        {name:'cpoStatus',type:'number'},
        {name:'cpoStatusName',type:'string'},
        {name:'createBy',type:'string'},
        {name:'orderDate',type:'string'},
        {name:'approvedDate',type:'string'},
        {name:'version',type:'int'}
    ]
});

Ext.define('CPUList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'cpuId',type:'string'},
        {name:'cpuNo',type:'string'},
        {name:'partsNo',type:'string'},
        {name:'customerCode',type:'string'},
        {name:'remark',type:'string'},
        {name:'firstTargetMonth',type:'date',convert:function(value){
            var year = value.substring(0,4) * 1;
            var month = value.substring(4) * 1 - 1;
            return new Date(year,month);
        }},
        {name:'lastTargetMonth',type:'date',convert:function(value){
            var year = value.substring(0,4) * 1;
            var month = value.substring(4) * 1 - 1;
            return new Date(year,month);
        }},
        {name:'createdBy',type:'string'},
        {name:'createdDate',type:'string'}
    ]
});

Ext.define('DDList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'ddId',type:'number'},
        {name:'ddNo',type:'string'},
        {name:'SuppCode',type:'string'},
        {name:'custCode',type:'string'},
        {name:'status',type:'number'},
        {name:'cfmdDate',type:'string'},
        {name:'cfmdBy',type:'string'},
        {name:'createdDate',type:'string'},
        {name:'createdBy',type:'string'},
        {name:'version',type:'number'},
        {name:'officeId',type:'number'}
    ]
});

Ext.define('DDPartsList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'partNo',type:'string'},
        {name:'invoiceNo',type:'string'},
        {name:'etd',type:'string'},
        {name:'eta',type:'string'},
        {name:'qty',type:'number'},
        {name:'uomCode',type:'string'},
        {name:'partsId',type:'number'},
        {name:'orderQty',type:'number'},
        {name:'inspectedQty',type:'number'},
        {name:'pendingInspectQty',type:'number'}
    ]
});

Ext.define('Dynamic',{
    extend:'Ext.data.Model',
    fields:[
        {name:'partsId',type:'number'},
        {name:'supplierId',type:'number'},
        {name:'businessPattern',type:'number'},
        {name:'partsNo',type:'string'},
        {name:'partsName',type:'string'},
        {name:'supplierCode',type:'string'},
        {name:'orderLOT',type:'number'},
        {name:'safetyStockDays',type:'number'},
        {name:'supplierOrderBalance',type:'number'},
        {name:'ttscStock',type:'number'},
        {name:'onShippingQty',type:'number'},
        {name:'customerStockQTY',type:'number'},
        {name:'ngRatio',type:'string'},
        {name:'adjustmentQTY',type:'number'},
        {name:'adjustmentReason',type:'string'},
        {name:'orderQTY',type:'number'},
        {name:'uomCode',type:'string'},
        {name:'lastMonthFC1Qty',type:'number'},
        {name:'fluctuationRatio',type:'string'},
        {name:'fluctuationReason',type:'string'},
        {name:'fc1AdjustmentQTY',type:'number'},
        {name:'fc1AdjustmentReason',type:'string'},
        {name:'fc1OrderQty',type:'number'},
        {name:'fc2AdjustmentQTY',type:'number'},
        {name:'fc2AdjustmentReason',type:'string'},
        {name:'fc2OrderQty',type:'number'},
        {name:'fc3AdjustmentQTY',type:'number'},
        {name:'fc3AdjustmentReason',type:'string'},
        {name:'fc3OrderQty',type:'number'},
        {name:'fc4AdjustmentQTY',type:'number'},
        {name:'fc4AdjustmentReason',type:'string'},
        {name:'fc4OrderQty',type:'number'},
        {name:'fc5AdjustmentQTY',type:'number'},
        {name:'fc5AdjustmentReason',type:'string'},
        {name:'fc5OrderQty',type:'number'},
        {name:'rownum',type:'number'}
    ]
});

Ext.define('SPOList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'spoId',type:'number'},
        {name:'partsNo',type:'string'},
        {name:'supplierPONo',type:'string'},
        {name:'customerPONo',type:'string'},
        {name:'orderMonth',type:'date',convert:function(value){
            var year = value.substring(0,4) * 1;
            var month = value.substring(4) * 1 - 1;
            return new Date(year,month);
        }},
        {name:'targetMonth',type:'date',convert:function(value){
        	if (value !== null && value !== undefined && value !== '') {
            var year = value.substring(0,4) * 1;
            var month = value.substring(4) * 1 - 1;
            return new Date(year,month); }
        }},
        {name:'orderType',type:'number'},
        {name:'reasonForAdditional',type:'string'},
        {name:'supplierCode',type:'string'},
        {name:'customerCode',type:'string'},
        {name:'customerPOReferenceNo',type:'string'},
        {name:'expCountry',type:'string'},
        {name:'impCountry',type:'string'},
        {name:'spoStatus',type:'number'},
        {name:'cpoStatusName',type:'string'},
        {name:'createdBy',type:'string'},
        {name:'createdDate',type:'string'},
        {name:'pendingInputInboundPlan',type:'string'},
        {name:'pendingInputDeliveryPlan',type:'string'}
    ]
});

Ext.define('SpoPartsList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'partsId',type:'number'},
        {name:'partsNo',type:'string'},
        {name:'partsName',type:'string'},
        {name:'currency',type:'string'},
        {name:'unitPrice',type:'number'},
        {name:'qty',type:'number'},
        {name:'uomCode',type:'string'},
        {name:'amount',type:'number'},
        {name:'inspectedQty',type:'number'},
        {name:'pendingInspectQty',type:'number'},
        {name:'allocatingQty',type:'number'},
        {name:'status',type:'number'},
        {name:'forecast1',type:'number'},
        {name:'forecast2',type:'number'},
        {name:'forecast3',type:'number'},
        {name:'forecast4',type:'number'},
        {name:'forecast5',type:'number'}
    ]
});

Ext.define('AllocateBalanceList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'abId',type:'number'},
        {name:'abNo',type:'string'},
        {name:'abStatus',type:'number'},
        {name:'createdDate',type:'string'},
        {name:'createdBy',type:'string'},
        {name:'confirmedDate',type:'string'},
        {name:'confirmedBy',type:'string'},
        {name:'version',type:'int'}
    ]
});

Ext.define('AllocBalPartsList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'partsId',type:'number'},
        {name:'abPartsId',type:'number'},
        {name:'partsNo',type:'string'},
        {name:'partsName',type:'string'},
        {name:'customerCode',type:'string'},
        {name:'supplierId',type:'number'},
        {name:'supplierCode',type:'string'},
        {name:'spoId',type:'number'},
        {name:'spoNo',type:'string'},
        {name:'orderMonth',type:'string'},
        {name:'orderType',type:'number'},
        {name:'orderQty',type:'number'},
        {name:'inspectedQty',type:'number'},
        {name:'allocatedQty',type:'number'},
        {name:'allocatingQty',type:'number'},
        {name:'version',type:'int'}
    ]
});

Ext.define('AllocationResult',{
    extend:'Ext.data.Model',
    fields:[
        {name:'partsId',type:'number'},
        {name:'cpuId',type:'number'},
        {name:'cpuDetailId',type:'number'},
        {name:'allocateQty',type:'number'},
        {name:'usageQty',type:'number'},
        {name:'etd',type:'string'},
        {name:'eta',type:'string'},
        {name:'targetMonth',type:'date',convert:function(value){
        	if (value !== null && value !== undefined && value !== '') {
            var year = value.substring(0,4) * 1;
            var month = value.substring(4) * 1 - 1;
            return new Date(year,month); }
        }},
        {name:'orderNo',type:'string'},
        {name:'partsNo',type:'string'},
        {name:'partsName',type:'string'},
        {name:'customerCode',type:'string'},
        {name:'deliveredBalanceQty',type:'number'}
    ]
});

Ext.define('CCRequsetList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'crId',type:'number'},
        {name:'requestNo',type:'string'},
        {name:'requestType',type:'number'},
        {name:'customerPONo',type:'string'},
        {name:'orderType',type:'number'},
        {name:'customerCode',type:'string'},
        {name:'orderMonth',type:'date',convert:function(value){
            var year = value.substring(0,4) * 1;
            var month = value.substring(4) * 1 - 1;
            return new Date(year,month);
        }},   
        {name:'reasonforRequest',type:'string'},
        {name:'status',type:'number'},
        {name:'createBy',type:'string'},
        {name:'createdDate',type:'string'},
        {name:'approvedBy',type:'string'},
        {name:'approvedDate',type:'string'},
        {name:'version',type:'int'}
    ]
});

Ext.define('CustomerList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'officeId',type:'string'},
        {name:'rownum',type:'string'},
        {name:'officeCode',type:'string'},
        {name:'customerId',type:'string'},
        {name:'customerCode',type:'string'},
        {name:'customerName',type:'string'},
        {name:'regionCode',type:'string'},
        {name:'businessPattern',type:'string'},
        {name:'inActiveFlag',type:'string'},
        {name:'createBy',type:'string'},
        {name:'createDate',type:'string'},
        {name:'updateBy',type:'string'},
        {name:'updateDate',type:'string'},
        {name:'version',type:'int'},
        {name:'ttlCustomerCode',type:'string'}
    ]
});

Ext.define('CustomerInfo',{
    extend:'Ext.data.Model',
    fields:[
        {name:'customerId',type:'int'},
        {name:'officeId',type:'int'},
        {name:'customerCode',type:'string'},
        {name:'customerName',type:'string'},
        {name:'region',type:'string'},
        {name:'address1',type:'string'},
        {name:'address2',type:'string'},
        {name:'address3',type:'string'},
        {name:'address4',type:'string'},
        {name:'contact1',type:'string'},
        {name:'telephone1',type:'string'},
        {name:'fax1',type:'string'},
        {name:'email1',type:'string'},
        {name:'postalCode',type:'string'},
        {name:'contact2',type:'string'},
        {name:'teltphone2',type:'string'},
        {name:'fax2',type:'string'},
        {name:'email2',type:'string'},
        {name:'businessPattern',type:'int'},
        {name:'activeFlag',type:'int'},
        {name:'createdBy',type:'int'},
        {name:'createdUser',type:'string'},
        {name:'createdDate',type:'string'},
        {name:'updatedBy',type:'int'},
        {name:'updatedUser',type:'string'},
        {name:'updatedDate',type:'string'},
        {name:'version',type:'int'},
    ]
});

Ext.define('SupplierList',{
	extend:'Ext.data.Model',
	fields:[
	    {name:'supplierId',type:'string'},
	    {name:'supplierCode',type:'string'},
	    {name:'supplierName',type:'string'},
	    {name:'regionCode',type:'string'},
	    {name:'currency',type:'string'},
	    {name:'packingDesc',type:'string'},
	    {name:'paymentTerms',type:'string'},
	    {name:'priceBasis',type:'string'},
	    {name:'destination',type:'string'},
	    {name:'activeFlag',type:'string'},
	    {name:'createdDate',type:'string'},
	    {name:'createdBy',type:'string'},
	    {name:'updatedDate',type:'string'},
	    {name:'updatedBy',type:'string'}
	]
});


Ext.define('OfficeList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'officeId',type:'string'},
        {name:'officeCode',type:'string'},
        {name:'officeName',type:'string'},
        {name:'regionCode',type:'string'},
        {name:'regionName',type:'string'},
        {name:'calendarId',type:'string'},
        {name:'calendarCode',type:'string'},
        {name:'createBy',type:'string'},
        {name:'inActiveFlag',type:'string'},
        {name:'createDate',type:'string'}
    ]
});

Ext.define('WarehouseList',{
	extend:'Ext.data.Model',
    fields:[
        {name:'whsId',type:'string'},
        {name:'whsCode',type:'string'},
        {name:'whsName',type:'string'},
        {name:'regionCode',type:'string'},
        {name:'calendarId',type:'string'},
        {name:'calendarCode',type:'string'},
        {name:'createBy',type:'string'},
        {name:'createDate',type:'string'},
        {name:'activeFlag',type:'string'},
    ]
});
Ext.define('TtcCustomerList',{
	extend:'Ext.data.Model',
    fields:[
        {name:'whsId',type:'string'},
        {name:'whsCode',type:'string'},
        {name:'whsCustomerCode',type:'string'},
        {name:'whsCustomerId',type:'string'},
    ]
});
Ext.define('PriceHisList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'supplierCode',type:'string'},
        {name:'expCountry',type:'string'},
        {name:'ngRatio',type:'string'},
        {name:'activeFlag',type:'string'},
        {name:'buyingPrice',type:'string'},
        {name:'buyingCurrency',type:'string'},
        {name:'effectiveFromDate',type:'string'},
        {name:'lastModifiedDate',type:'string'},
        {name:'lastModifiedBy',type:'string'},
        {name:'partsId',type:'string'},
        {name:'supplierId',type:'string'}
	]
});
Ext.define('PartsList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'officeId',type:'string'},
        {name:'officeCode',type:'string'},
        {name:'partsId',type:'string'},
        {name:'partsNo',type:'string'},
        {name:'partsName',type:'string'},
        {name:'impCountry',type:'string'},
        {name:'customerCode',type:'string'},
        {name:'expCountry',type:'string'},
        {name:'supplierCode',type:'string'},
        {name:'ddFlag',type:'string'},
        {name:'whsCode',type:'string'},
        {name:'targetMonth',type:'string'},
        {name:'businessPattern',type:'string'},
        {name:'orderCalcFlag',type:'string'},
        {name:'checkBFlag',type:'string'},
        {name:'model',type:'string'},
        {name:'activeFlag',type:'string'},
        {name:'buildoutDate',type:'string'}
    ]
});

Ext.define('Role',{
    extend:'Ext.data.Model',
    fields:[
        {name:'roleId', type:'string'},
        {name:'roleName', type:'string'},
        {name:'roleNotes', type:'string'},
	    {name:'updatedDate',type:'string'},
	    {name:'updateUser',type:'string'},
	    {name:'version',type:'int'}
    ]
});

Ext.define('Combo',{
    extend:'Ext.data.Model',
    fields:[
        {name:'id',type:'string'},
        {name:'text',type:'string'}
    ]
});

// YMWNMS01
Ext.define('NGList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'partsNo', type:'string'},
        {name:'partsName', type:'string'},
        {name:'srcCountry', type:'string'},
        {name:'supplierCode', type:'string'},
        {name:'impCountry', type:'string'},
        {name:'customerCode', type:'string'},
        {name:'partsModel', type:'string'},
        {name:'receivedDate', type:'string'},
        {name:'inspectedDate', type:'string'},
        {name:'qty', type:'number'},
        {name:'reasonCode', type:'string'},
        {name:'reasonName', type:'string'},
        {name:'position', type:'string'},
    ]
});


Ext.define('UserInfoList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'userId', type:'string'},
        {name:'loginId', type:'string'},
        {name:'userName', type:'string'},
        {name:'mailAddr', type:'string'},
        {name:'statusCode', type:'string'},
	    {name:'version',type:'string'}
    ]
});

Ext.define('OfficeRole',{
    extend:'Ext.data.Model',
    fields:[
        {name:'userOfficeRoleId', type:'int'},
        {name:'roleId', type:'int'},
        {name:'roleName', type:'string'},
        {name:'officeId', type:'int'},
        {name:'officeName', type:'string'},
    ]
});

Ext.define('AllocateParts',{
	extend:'Ext.data.Model',
	fields:[
	    {name:'partsNo', type:'string'},
	    {name:'partsName', type:'string'},
	    {name:'customerCode', type:'string'},
	    {name:'supplierCode',type:'string'},
	    {name:'receivedBalanceQty',type:'number'}
	]
});

Ext.define('AllocateSpoList',{
	extend:'Ext.data.Model',
	fields:[
	    {name:'rowNum', type:'int'},
	    {name:'partsNo', type:'string'},
	    {name:'customerCode', type:'string'},
	    {name:'supplierCode',type:'string'},
	    {name:'spoNo',type:'string'},
	    {name:'orderMonth',type:'date', convert:function(value){
			var year = value.substring(0,4) * 1;
			var month = value.substring(4) * 1 - 1;
			return new Date(year,month);
		}},
	    {name:'orderType',type:'string'},
	    {name:'orderQty',type:'number'},
	    {name:'inspectedQty',type:'number'},
	    {name:'allocatedQty',type:'number'},
	    {name:'allocBalQty',type:'string'}
	]
});
















Ext.define('MidInvoiceSummaryList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'midInvoiceNo',type:'string'},
        {name:'refNo',type:'string'},
        {name:'etd',type:'string'},
        {name:'impOfficeCode',type:'string'},
        {name:'dischargePort',type:'string'},
        {name:'expOfficeCode',type:'string'},
        {name:'loadingPort',type:'string'},
        {name:'status',type:'string'},
        {name:'esignStatus',type:'string'},
        {name:'unitPriceRevisedFlag',type:'string'},
        {name:'modifiedDateTimezone',type:'number'},
        {name:'statusName',type:'string'},
        {name:'issuedDate',type:'string'},
        {name:'etdDisplay',type:'string'},
        {name:'issuedDateDisplay',type:'string'},
        {name:'modifiedDateTimezoneDisplay',type:'string'}
    ]
});

Ext.define('ImpOrder',{
    extend:'Ext.data.Model',
    fields:[
        {name:'impOrderNo',type:'string'},
        {name:'changeSeq',type:'number'},
        {name:'customerRefNo',type:'string'},
        {name:'impPoNo',type:'string'},
        {name:'formType',type:'string'},
        {name:'newppPjCode',type:'string'},
        {name:'newppPjName',type:'string'},
        {name:'orderType',type:'number'},
        {name:'impOfficeCode',type:'string'},
        {name:'customerCode',type:'string'},
        {name:'customerContractNo',type:'string'},
        {name:'midOfficeCode',type:'string'},
        {name:'expOfficeCode',type:'string'},
        {name:'ttcContractNo',type:'string'},
        {name:'orderMonth',type:'date',convert:function(value){
            var year = value.substring(0,4) * 1;
            var month = value.substring(4) * 1 - 1;
            return new Date(year,month);
        }},
        {name:'createdDate',type:'date',convert:function(value){return new Date(value)}},
        {name:'createdDateDisplay',type:'string'},
        {name:'zeroFlag',type:'number'},
        {name:'poStatus',type:'number'},
        {name:'shippingPlanStatus',type:'number'},
        {name:'partsPendingTotal',type:'string'},
        {name:'customerPendingTotal',type:'string'}
    ]
});

Ext.define('changeImpOrder',{
    extend:'Ext.data.Model',
    fields:[
        {name:'impOrderNo',type:'string'},
        {name:'customerRefNo',type:'string'},
        {name:'impPoNo',type:'string'},
        {name:'impPoChangeSeq',type:'number'},
        {name:'midPoNo',type:'string'},
        {name:'midPoChangeSeq',type:'number'},
        {name:'expPoNo',type:'string'},
        {name:'expPoChangeSeq',type:'number'},
        {name:'formType',type:'string'},
        {name:'newppPjCode',type:'string'},
        {name:'newppPjName',type:'string'},
        {name:'orderType',type:'number'},
        {name:'orderTypeName',type:'string'},
        {name:'impOfficeCode',type:'string'},
        {name:'customerCode',type:'string'},
        {name:'customerContractNo',type:'string'},
        {name:'midOfficeCode',type:'string'},
        {name:'expOfficeCode',type:'string'},
        {name:'ttcContractNo',type:'string'},
        {name:'orderMonth',type:'date',convert:function(value){
            var year = value.substring(0,4) * 1;
            var month = value.substring(4) * 1 - 1;
            return new Date(year,month);
        }},
        {name:'createdDate',type:'date',convert:function(value){return new Date(value)}},
        {name:'zeroFlag',type:'number'},
        {name:'poStatusName',type:'string'},
        {name:'mocsOrderStatus',type:'string'}
    ]
});


Ext.define('ChangeCancelRequestSimple',{
    extend:'Ext.data.Model',
    fields:[
        {name:'requestNo',type:'string'},
        {name:'requestStatus',type:'number'},
        {name:'requestCreatedDate',type:'date',convert:function(value){return new Date(value);}},
        {name:'requestCreatedDateDisplay',type:'string'}
    ]
});

Ext.define('DR',{
    extend:'Ext.data.Model',
    fields:[
        {name:'drId',type:'int'},
        {name:'expPoNo',type:'string'},
        {name:'drName',type:'string'},
        {name:'drDateDisplay',type:'string'},
        {name:'drDate',type:'date',convert:function(value){return new Date(value);}}
    ]
});

Ext.define('DynamicChangeRequestSummaryList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'requestNo',type:'string'},
        {name:'impOrderNo',type:'string'},
        {name:'impOrderChangeSeq',type:'number'},
        {name:'custOrdRefNo',type:'string'},
        {name:'impPoNo',type:'string'},
        {name:'impPoNoChangeSeq',type:'number'},
        {name:'midPoNo',type:'string'},
        {name:'midPoNoChangeSeq',type:'number'},
        {name:'expPoNo',type:'string'},
        {name:'expPoNoChangeSeq',type:'number'},
        {name:'formType',type:'string'},
        {name:'orderType',type:'number'},
        {name:'impOfficeCode',type:'string'},
        {name:'customer',type:'string'},
        {name:'midOfficeCode',type:'string'},
        {name:'expOfficeCode',type:'string'},
        {name:'supportApprovalRequired',type:'string'},
        {name:'orderMonth',type:'date',convert:function(value){
            var year = value.substring(0,4) * 1;
            var month = value.substring(4) * 1 - 1;
            return new Date(year,month);
        }},
        {name:'changeStatus',type:'string'},
        {name:'createdDate',type:'date',convert:function(value){
            return new Date(value);
        }},
        {name:'accessLevel',type:'number'},
        {name:'midCountryFlag',type:'number'},
        {name:'roleType',type:'number'},
        {name:'poStatus',type:'number'}
    ]
});

Ext.define('DynamicChangedPartsList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'partStatus',type:'number'},
        {name:'ttcPartsNoOld',type:'string'},
        {name:'ttcPartsNoNew',type:'string'},
        {name:'customerPartsNoOld',type:'string'},
        {name:'customerPartsNoNew',type:'string'},
        {name:'supplierPartsNoOld',type:'string'},
        {name:'supplierPartsNoNew',type:'string'},
        {name:'eciOld',type:'string'},
        {name:'eciNew',type:'string'},
        {name:'colorCodeOld',type:'string'},
        {name:'colorCodeNew',type:'string'},
        {name:'modelOld',type:'string'},
        {name:'modelNew',type:'string'},
        {name:'customerBackNoOld',type:'string'},
        {name:'customerBackNoNew',type:'string'},
        {name:'supplierBackNoOld',type:'string'},
        {name:'supplierBackNoNew',type:'string'},
        {name:'customerPartsNameOld',type:'string'},
        {name:'customerPartsNameNew',type:'string'},
        {name:'pairedTtcPartsNo',type:'string'},
        {name:'supplierCode',type:'string'},
        {name:'importHsCode',type:'string'},
        {name:'supplierCodeByImp',type:'string'},
        {name:'supplierCodeByExp',type:'string'},
        {name:'supplierChangeReason',type:'string'},
        {name:'actionByImp',type:'string'},
        {name:'uomOld',type:'string'},
        {name:'uomNew',type:'string'},
        {name:'srbqOld',type:'string'},
        {name:'srbqNew',type:'string'},
        {name:'spqOld',type:'string'},
        {name:'spqNew',type:'string'},
        {name:'orderLotOld',type:'string'},
        {name:'orderLotNew',type:'string'},
        {name:'firmOld',type:'string'},
        {name:'firmNew',type:'string'},
        {name:'forecastN1Old',type:'string'},
        {name:'forecastN1New',type:'string'},
        {name:'forecastN2Old',type:'string'},
        {name:'forecastN2New',type:'string'},
        {name:'forecastN3Old',type:'string'},
        {name:'forecastN3New',type:'string'},
        {name:'buildOutFlag',type:'string'},
        {name:'orderlotFlag',type:'string'},
        {name:'lastOrderMonthByTtcImp',type:'string'},
        {name:'shippingPlanStatus',type:'string'}
    ]
});

Ext.define('Project',{
    extend:'Ext.data.Model',
    fields:[
        {name:'projectId',type:'string'},
        {name:'projectType',type:'string'},
        {name:'projectCode',type:'string'},
        {name:'projectName',type:'string'},
        {name:'officeImpCountry',type:'string'},
        {name:'officeExpCountry',type:'string'},
        {name:'startDate',type:'date',convert:Ext.util.dateConverterForIE8},
        {name:'endDate',type:'date',convert:Ext.util.dateConverterForIE8},
        {name:'productionMonth',type:'date',convert:Ext.util.dateConverterForIE8},
        {name:'currentMonth',type:'string'},
        {name:'status',type:'number'}
    ]
});

Ext.define('OrderSummaryList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'midSoNo',type:'string'},
        {name:'midPoNo',type:'string'},
        {name:'expSoNo',type:'string'},
        {name:'impPoNo',type:'string'},
        {name:'impOrderNo',type:'string'},
        {name:'custOrdRefNo',type:'string'},
        {name:'formType',type:'string'},
        {name:'newppPjCode',type:'string'},
        {name:'orderType',type:'number'},
        {name:'orderTypeName',type:'string'},
        {name:'ipoStatus',type:'number'},
        {name:'impOfficeCode',type:'string'},
        {name:'customerCode',type:'string'},

        {name:'customerContractNo',type:'string'},
        {name:'midOfficeCode',type:'string'},
        {name:'expOfficeCode',type:'string'},
        {name:'ttcContractNo',type:'string'},
        {name:'orderDate',type:'date',convert:Ext.util.dateConverterForIE8},
        {name:'ipoApprovedDate',type:'date',convert:Ext.util.dateConverterForIE8},
        {name:'ipoMpoApprovedDate',type:'date',convert:Ext.util.dateConverterForIE8},
        {name:'esoConfirmDate',type:'date',convert:Ext.util.dateConverterForIE8},
        {name:'zeroFlag',type:'number'},
        
        {name:'shippingPendingCnt',type:'number'},
        {name:'actionPendingCnt',type:'number'},
        {name:'noOfShippingCnt',type:'string'},
        {name:'noOfActionCnt',type:'string'},
        {name:'msoStatusName',type:'string'},
        {name:'mpoStatusName',type:'string'},
        {name:'impOrderNoOrignal',type:'string'},
        {name:'shippingPlanStatusName',type:'string'}
    ]
});

Ext.define('Event',{
    extend:'Ext.data.Model',
    fields:[
        {name:'id',type:'number'},
        {name:'eventName',type:'string'},
        {name:'responsbileParty',type:'string'},
        {name:'required',type:'number'},
        {name:'startMonth',type:'string'},
        {name:'endMonth',type:'string'},
        {name:'startDate',type:'date',convert:Ext.util.dateConverterForIE8},
        {name:'endDate',type:'date',convert:Ext.util.dateConverterForIE8}
    ]
});

Ext.define('UserInfo',{
    extend:'Ext.data.Model',
    fields:[
        {name:'id',type:'number'},
        {name:'loginId',type:'string'},
        {name:'userName',type:'string'},
        {name:'text',type:'string'}
    ]
});

Ext.define('UploadFile',{
    extend:'Ext.data.Model',
    fields:[
        {name:'uploadLogId',type:'number'},
        {name:'fileName',type:'string'},
        {name:'uploadBy',type:'string'},
        {name:'uploadDate',type:'date',convert:function(value){return new Date(value);}}
    ]
});



Ext.define('RoleManagement',{
    extend:'Ext.data.Model',
    fields:[
        {name:'userId', type:'number'},
        {name:'officeId', type:'number'},
        {name:'officeCode', type:'string'},
        {name:'roleType', type:'number'},
        {name:'accessLevel', type:'number'},
        {name:'companyId', type:'string'}
    ]
});

Ext.define('UserList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'loginId',type:'string'},
        {name:'userName',type:'string'},
        {name:'companyId',type:'string'},
        {name:'contact',type:'string'},
        {name:'backupPic1',type:'string'},
        {name:'backupPic2',type:'string'},
        {name:'status',type:'int'},
        {name:'createdDate',convert:function(value){return new Date(value);}},
        {name:'createdBy',type:'string'},
        {name:'updatedDate',convert:function(value){return new Date(value);}},
        {name:'updatedBy',type:'string'}
    ]
});

Ext.define('Imp',{
    extend:'Ext.data.Model',
    fields:[
        {name:'imp',type:'string'}
    ]
});

Ext.define('M01TCLAS01',{
    extend:'Ext.data.Model',
    fields:[
        {name:'mocsId',type:'int'},
        {name:'ttcPartsNo',type:'string'},
        {name:'customerPartsNo',type:'string'},
        {name:'supplierPartsNo',type:'string'},
        {name:'customerPartsName',type:'string'},
        {name:'eci',type:'string'},
        {name:'colorCode',type:'string'},
        {name:'model',type:'string'},
        {name:'impOrderNo',type:'string'},
        {name:'impOrderNoSeq',type:'string'},
        {name:'impPoNo',type:'string'},
        {name:'impPoNoSeq',type:'string'},
        {name:'orderCreatedDate',type:'date',convert:Ext.util.dateConverterForIE8},
        {name:'orderCreatedDateDisplay',type:'string'},
        {name:'cgBusinessType',type:'string'},//
        {name:'formType',type:'string'},
        {name:'impOfficeCode',type:'string'},//
        {name:'customerCode',type:'string'},
        //{name:'newppPjCodeName',type:'string'},
        {name:'newppPjCode',type:'string'},{name:'newppPjName',type:'string'},
        {name:'orderTypeDesc',type:'string'},
        {name:'midOfficeCode',type:'string'},
        {name:'expPoNo',type:'string'},
        {name:'expPoNoSeq',type:'string'},
        {name:'expOfficeCode',type:'string'},//
        {name:'supplierCode',type:'string'},
        {name:'orderMonth',type:'string'},
        {name:'uom',type:'string'},
        {name:'firm',type:'string'},//Order Qty
        {name:'PendingExpShippingQty',type:'string'},//firm - qty
        {name:'qty',type:'string'},//Invoice Qty
        {name:'InvoiceQty>OrderQty',type:'string'},//
        {name:'cgExpshippedStatus',type:'string'},//
        {name:'cgOrderStatus',type:'string'},//
        {name:'remarks',type:'string'},
        {name:'decimalDigits',type:'number'}
    ]
});
Ext.define('M01TCOAS01',{
    extend:'Ext.data.Model',
    fields:[
        {name:'codeValue',type:'int'},
        {name:'codeName',type:'string'}
    ]
});

Ext.define('M01IMDAS06',{
    extend:'Ext.data.Model',
    fields:[
        {name:'invoicePartsId',type:'string'},
        {name:'ttcPartsNo',type:'string'},
        {name:'customerPartsNo',type:'string'},
        {name:'supplierPartsNo',type:'string'},
        {name:'colorCode',type:'string'},
        {name:'supplierPartsName',type:'string'},
        {name:'customerPartsName',type:'string'},
        {name:'customerOrderNo',type:'string'},
        {name:'expSoNo',type:'string'},
        {name:'qty',type:'number'},
        {name:'uom',type:'string'},
        {name:'midInvoiceCurrency',type:'string'},
        {name:'midInvoicePriceBak',type:'number'},
        {name:'midInvoicePrice',type:'number'},
        {name:'impPurchasePrice',type:'number'},
        {name:'midInvoiceAmount',type:'number'},
        {name:'impHSCode',type:'string'},
        {name:'totalQty',type:'number'},    
        {name:'totalAmount',type:'number'},
        {name:'formType',type:'string'},
        {name:'midInvoiceRefNo',type:'string'},
        {name:'status',type:'string'},
        {name:'currency',type:'string'}
    ]
});

Ext.define('MidInvoicePartsNonList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'ttcPartsNoNon',type:'string'},
        {name:'customerPartsNoNon',type:'string'},
        {name:'supplierPartsNoNon',type:'string'},
        {name:'colorCodeNon',type:'string'},
        {name:'supplierPartsNameNon',type:'string'},
        {name:'customerPartsNameNon',type:'string'},
        {name:'customerOrderNoNon',type:'string'},
        {name:'expSoNoNon',type:'string'},
        {name:'qtyNon',type:'number'},
        {name:'uomNon',type:'string'},
        {name:'midInvoiceCurrencyNon',type:'string'},
        {name:'midInvoicePriceNonBak',type:'number'},
        {name:'midInvoicePriceNon',type:'number'},
        {name:'midInvoiceAmountNon',type:'number'},
        {name:'impHSCodeNon',type:'string'},
        {name:'totalQtyNon',type:'number'},
        {name:'impPurchasePriceNon',type:'number'},
        {name:'totalAmountNon',type:'number'},
        {name:'currency',type:'string'}
    ]
});



Ext.define('EsoList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'expSoNo',type:'string'},
        {name:'expPoNo',type:'string'},
        {name:'orderMonth',type:'date',convert:function(value){
            var year = value.substring(0,4) * 1;
            var month = value.substring(4) * 1 - 1;
            return new Date(year,month);
        }},
        {name:'formType',type:'string'},
        {name:'ttcContractNo',type:'string'},
        {name:'orderType',type:'number'},
        {name:'orderTypeName',type:'string'},
        {name:'impOfficeCode',type:'string'},
        {name:'midOfficeCode',type:'string'},
        {name:'zeroFlag',type:'string'},
        {name:'epoStatus',type:'int'},
        {name:'epoStatusName',type:'string'},
        {name:'expPurchaseCurrency',type:'string'},
        {name:'changeSeq',type:'int'}
    ]
});
Ext.define('InvoiceSummaryList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'expInvoiceNo',type:'string'},
        {name:'midInvoiceNo',type:'string'},
        {name:'expInvoiceRefNo',type:'string'},
        {name:'impOfficeCode',type:'string'},
        {name:'midOfficeCode',type:'string'},
        {name:'expOfficeCode',type:'string'},
        {name:'midCountryFlag',type:'string'},
        {name:'pfiNo',type:'string'},
        {name:'ttcContractNo',type:'string'},
        {name:'transportMode',type:'string'},
        {name:'loadingPort',type:'string'},
        {name:'loadingPortName',type:'string'},
        {name:'dischargePort',type:'string'},
        {name:'dischargePortName',type:'string'},
        {name:'vesselName',type:'string'},
        {name:'etd',type:'string'},
        {name:'etdDisplay',type:'string'},
        {name:'etaDisplay',type:'string'},
        {name:'blDateDisplay',type:'string'},
        {name:'issuedDateDisplay',type:'string'},
        {name:'modifiedDateTimezoneDisplay',type:'string'},
        {name:'eta',type:'date',convert:function(value){return new Date(value)}},
        {name:'paymentTerms',type:'string'},
        {name:'shipperId',type:'string'},
        {name:'shipperName',type:'string'},
        {name:'shipperAddr1',type:'string'},
        {name:'shipperAddr2',type:'string'},
        {name:'shipperAddr3',type:'string'},
        {name:'shipperAddr4',type:'string'},
        {name:'shipperTel',type:'string'},
        {name:'shipperFax',type:'string'},
        {name:'consigneeId',type:'string'},
        {name:'consigneeName',type:'string'},
        {name:'consigneeAddr1',type:'string'},
        {name:'consigneeAddr2',type:'string'},
        {name:'consigneeAddr3',type:'string'},
        {name:'consigneeAddr4',type:'string'},
        {name:'consigneeTel',type:'string'},
        {name:'consigneeFax',type:'string'},
        {name:'accounteeId',type:'string'},
        {name:'accounteeName',type:'string'},
        {name:'accounteeAddr1',type:'string'},
        {name:'accounteeAddr2',type:'string'},
        {name:'accounteeAddr3',type:'string'},
        {name:'accounteeAddr4',type:'string'},
        {name:'accounteeTel',type:'string'},
        {name:'accounteeFax',type:'string'},
        {name:'deliveryId',type:'string'},
        {name:'deliveryName',type:'string'},
        {name:'deliveryAddr1',type:'string'},
        {name:'deliveryAddr2',type:'string'},
        {name:'deliveryAddr3',type:'string'},
        {name:'deliveryAddr4',type:'string'},
        {name:'deliveryTel',type:'string'},
        {name:'deliveryFax',type:'string'},
        {name:'blNo',type:'string'},
        {name:'blDate',type:'date',convert:function(value){return new Date(value)}},
        {name:'remark1',type:'string'},
        {name:'remark2',type:'string'},
        {name:'remark3',type:'string'},
        {name:'remark4',type:'string'},
        {name:'remark5',type:'string'},
        {name:'remark6',type:'string'},
        {name:'remark7',type:'string'},
        {name:'remark8',type:'string'},
        {name:'remark9',type:'string'},
        {name:'remark10',type:'string'},
        {name:'issuedDate',type:'date',convert:function(value){return new Date(value)}},
        {name:'status',type:'string'},
        {name:'statusName',type:'string'},
        {name:'currency',type:'string'},
        {name:'incoterms1',type:'string'},
        {name:'incoterms1Place',type:'string'},
        {name:'incoterms1Amount',type:'string'},
        {name:'freight',type:'string'},
        {name:'freightAmount',type:'string'},
        {name:'insAremium',type:'string'},
        {name:'insPremiunAmount',type:'string'},
        {name:'interest',type:'string'},
        {name:'interestAmount',type:'string'},
        {name:'commission',type:'string'},
        {name:'commissionAmount',type:'string'},
        {name:'item1',type:'string'},
        {name:'item1Amount',type:'string'},
        {name:'item2',type:'string'},
        {name:'item2Amount',type:'string'},
        {name:'item3',type:'string'},
        {name:'item3Amount',type:'string'},
        {name:'item4',type:'string'},
        {name:'item4Amount',type:'string'},
        {name:'item5',type:'string'},
        {name:'item5Amount',type:'string'},
        {name:'incoterms2',type:'string'},
        {name:'incoterms2Place',type:'string'},
        {name:'incoterms2Amount',type:'string'},
        {name:'termsRemark1',type:'string'},
        {name:'termsRemark2',type:'string'},
        {name:'incoterms1Non',type:'string'},
        {name:'incoterms1PlaceNon',type:'string'},
        {name:'incoterms1AmountNon',type:'string'},
        {name:'freightNon',type:'string'},
        {name:'freightAmountNon',type:'string'},
        {name:'insPremiumNon',type:'string'},
        {name:'insPremiunAmountNon',type:'string'},
        {name:'interestNon',type:'string'},
        {name:'interestAmountNon',type:'string'},
        {name:'commissionNon',type:'string'},
        {name:'commissionAmountNon',type:'string'},
        {name:'item1Non',type:'string'},
        {name:'item1AmountNon',type:'string'},
        {name:'item2Non',type:'string'},
        {name:'item2AmountNon',type:'string'},
        {name:'item3Non',type:'string'},
        {name:'item3AmountNon',type:'string'},
        {name:'item4Non',type:'string'},
        {name:'item4AmountNon',type:'string'},
        {name:'item5Non',type:'string'},
        {name:'item5AmountNon',type:'string'},
        {name:'incoterms2Non',type:'string'},
        {name:'incoterms2PlaceNon',type:'string'},
        {name:'incoterms2AmountNon',type:'string'},
        {name:'esignId',type:'string'},
        {name:'esignStatus',type:'string'},
        {name:'imp_show_date',type:'string'},
        {name:'modifiedDateTimezone',type:'date',convert:function(value){return new Date(value)}},
        {name:'approvedUser',type:'string'},
        {name:'approvedDate',type:'date',convert:function(value){return new Date(value)}},
        {name:'cancelled_user',type:'string'},
        {name:'cancelled_date',type:'date',convert:function(value){return new Date(value)}},
        {name:'createdDate',type:'date',convert:function(value){return new Date(value)}},
        {name:'createdUser',type:'string'},
        {name:'modifiedDate',type:'date',convert:function(value){return new Date(value)}},
        {name:'modifiedUser',type:'string'},
        {name:'commercialParts',type:'string'},
        {name:'nonCommercialParts',type:'string'},
        {name:'unitPriceRevisedFlag',type:'string'},
        {name:'accessLevel',type:'string'},
        {name:'decimalDigits',type:'string'}
    ]
});

Ext.define('Information',{
    extend:'Ext.data.Model',
    fields:[
        {name:'informationId', type:'string'},
        {name:'displayStartDate', type:'date', convert:function(value){return new Date(value);}},
        {name:'displayEndDate', type:'date', convert:function(value){return new Date(value);}},
        {name:'startDateDisplay', type:'string'},
        {name:'endDateDisplay', type:'string'},
        {name:'information', type:'string'},
        {name:'title',type:'string'}
    ]
});

Ext.define('RoleScreen',{
    extend:'Ext.data.Model',
    fields:[
        {name:'roleId', type:'string'},
        {name:'roleName', type:'string'},
        {name:'roleNotes', type:'string'},
        {name:'screenId', type:'string'},
        {name:'screenName', type:'string'},
        {name:'nonSelectable', type:'int'},
        {name:'lowSelectable', type:'int'},
        {name:'midSelectable', type:'int'},
        {name:'highSelectable', type:'int'},
        {name:'mainSelectable', type:'int'},
        {name:'accessLevel', type:'int'}
    ]
});
Ext.define('M01TCDAS02',{
    extend:'Ext.data.Model',
    fields:[
        {name:'roleId', type:'int'},
        {name:'roleUserId', type:'int'},
        {name:'roleName', type:'string'},
        {name:'roleType', type:'int'},
        {name:'roleTypeCode', type:'string'},
        {name:'officeCode', type:'string'},
        {name:'officeName', type:'string'},
        {name:'accessLevel',type:'int'}
    ]
});
Ext.define('M01TCLAS02',{
    extend:'Ext.data.Model',
    fields:[
        {name:'loginId', type:'string'},
        {name:'userName', type:'string'},
        {name:'mailAddr', type:'string'},
        {name:'companyCode', type:'string'},
        {name:'statusCode', type:'string'},
        {name:'roleName', type:'string'},
        {name:'roleType',type:'string'},
        {name:'officeCode',type:'string'}
    ]
});

Ext.define('InvoicePartsList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'invoicePartsId',type:'string'},
        {name:'ttcPartsNo',type:'string'},
        {name:'customerPartsNo',type:'string'},
        {name:'supplierPartsNo',type:'string'},
        {name:'colorCode',type:'string'},
        {name:'supplierPartsName',type:'string'},
        {name:'customerPartsName',type:'string'},
        {name:'customerOrderNo',type:'string'},
        {name:'customerOrderRefNo',type:'string'},
        {name:'expSoNo',type:'string'},
        {name:'qty',type:'string'},
        {name:'spq',type:'string'},
        {name:'uom',type:'string'},
        {name:'expInvoiceCurrency',type:'string'},
        {name:'expInvoicePrice',type:'string'},
        {name:'midPurchasePrice',type:'string'},
        {name:'midPurchaseCurrency',type:'string'},
        {name:'impPurchasePrice',type:'string'},
        {name:'impPurchaseCurrency',type:'string'},
        {name:'expInvoiceAmount',type:'string'},
        {name:'expHsCode',type:'string'},
        {name:'partsNetWeight',type:'string'},        
        {name:'totalGrossWeight',type:'string'},
        {name:'totalQty',type:'string'},
        {name:'totalAmount',type:'string'},
        {name:'expInvoiceRefNo',type:'string'},
        {name:'status',type:'string'},
        {name:'currency',type:'string'},
        {name:'formType',type:'string'}
    ]
});

Ext.define('ShippingCalendarList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'shipingRoute',type:'string'},
        {name:'effectiveFrom',type:'string'},
        {name:'effectiveTo',type:'string'},
        {name:'fromRegion',type:'string'},
        {name:'fromPort',type:'string'},
        {name:'expCustomslt',type:'string'},
        {name:'toRegion',type:'string'},
        {name:'toPort',type:'string'},
        {name:'impCustomslt',type:'string'},
        {name:'etd',type:'string'},
        {name:'eta',type:'string'},
        {name:'interval',type:'string'},
        {name:'shippingFrequency',type:'string'},
        {name:'transportMode',type:'string'},
        {name:'activeFlag',type:'string'}
    ]
});

Ext.define('UserToCustomerList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'loginId',type:'string'},
		{name:'userId',type:'string'},
		{name:'customerId',type:'string'},
        {name:'userCode',type:'string'},
        {name:'userName',type:'string'},
        {name:'customerCode',type:'string'},
        {name:'customerName',type:'string'},
        {name:'useFlag',type:'string'},
        {name:'activeFlag',type:'string'},
        {name:'officeCode',type:'string'},
        {name:'allCustomerFlag',type:'int'},
        {name:'businessPattern',type:'int'}
    ]
});

Ext.define('CrDetailList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'changeFlag',type:'int'},
        {name:'partsId',type:'int'},
        {name:'crDetailId',type:'int'},
        {name:'partsNo',type:'string'},
        {name:'partsName',type:'string'},
        {name:'orderLot',type:'number'},
        {name:'spoNo',type:'string'},
        {name:'deliveredQty',type:'number'},
        {name:'orderQty',type:'number'},

        {name:'forecastQty1',type:'number'},
        {name:'forecastQty2',type:'number'},
        {name:'forecastQty3',type:'number'},
        {name:'forecastQty4',type:'number'},
        {name:'forecastQty5',type:'number'},
        {name:'crQty1',type:'number'},
        {name:'crQty2',type:'number'},
        {name:'crQty3',type:'number'},
        {name:'crQty4',type:'number'},
        {name:'crQty5',type:'number'}
    ]
});

// -------------------------------------China Plus-------------------------------------------------------------
Ext.define('InvoiceList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'invoiceSummaryId',type:'int'},
        {name:'businessPattern',type:'int'},
        {name:'invoiceNo',type:'string'},
        {name:'irregularStatus',type:'int'},
        {name:'groupIrregularStatus',type:'int'},
        {name:'hasIrregularLink',type:'boolean'},
        {name:'uploadStatus',type:'int'},
        {name:'hasUploadLink',type:'boolean'},
        {name:'expCountry',type:'string'},
        {name:'impCountry',type:'string'},
        {name:'ttcSupplierCode',type:'string'},
        {name:'transportMode',type:'int'},
        {name:'etd',type:'string'},
        {name:'eta',type:'string'},
        {name:'invoiceQty',type:'number'},
        {name:'inboundQty',type:'number'},
        {name:'pendingQty',type:'number'},
        {name:'uploadId',type:'string'},
        {name:'userId',type:'string'},
        {name:'uploadReceiveTime',type:'string'},
        {name:'invoiceType',type:'int'},
        {name:'invoiceStatus',type:'int'},
        {name:'postRIFlag',type:'int'},
        {name:'postRIStatus',type:'int'},
        {name:'version',type:'int'}
    ]
});

Ext.define('InvoiceOrderList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'orderId',type:'int'},
        {name:'impOrderNo',type:'string'},
        {name:'cusOrderNo',type:'string'},
        {name:'soDate',type:'string'},
        {name:'expCountry',type:'string'},
        {name:'impCountry',type:'string'},
        {name:'ttcCustomerCode',type:'string'},
        {name:'nonInvoicedQty',type:'number'},
        {name:'orderStatus',type:'int'}
    ]
});

Ext.define('SupplementaryList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'invoiceNo',type:'string'},
        {name:'partsId',type:'int'},
        {name:'ttcPartsNo',type:'string'},
        {name:'orderMonth',type:'string'},
        {name:'disOrderMonth',type:'string'},
        {name:'qty',type:'number'},
        {name:'supplierId',type:'int'},
        {name:'expPartsId',type:'int'},
        {name:'ttcSuppCode',type:'string'},
        {name:'expRegion',type:'string'},
        {name:'inputQty',type:'string'},
        {name:'uomCode',type:'string'},
        {name:'showFlag',type:'int'}
    ]
});

Ext.define('IrregularList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'vesselName',type:'string'},
        {name:'disEtd',type:'string'},
        {name:'eta',type:'string'},
        {name:'planInbound',type:'string'},
        {name:'shippingRoute',type:'string'}
    ]
});

// CPKKPS01 Kanban Issued Plan Screen
Ext.define('KanbanList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'kanbanId',type:'string'},
        {name:'kanbanPlanNo',type:'string'},
        {name:'kanbanPlanNoDisplay',type:'string'},
        {name:'orderMonth',type:'string'},
        {name:'ttcCustomerCode',type:'string'},
        {name:'ttcSupplierCode',type:'string'},
        {name:'transportMode',type:'string'},
        {name:'orderQty',type:'number'},
        {name:'onShippingQty',type:'number'},
        {name:'inboundQty',type:'number'},
        {name:'orderBalance',type:'number'},
        {name:'status',type:'int'},
        {name:'uploadId',type:'string'},
        {name:'lastUploadTime',type:'string'},
        {name:'version',type:'int'}
    ]
});

// CPKKPS02 Kanban Plan Detailed Information Screen
Ext.define('KanbanInfo',{
    extend:'Ext.data.Model',
    fields:[
        {name:'kanbanId',type:'string'},
        {name:'kanbanPlanNo',type:'string'},
        {name:'orderMonth',type:'string'},
        {name:'customerCode',type:'string'},
        {name:'supplierCode',type:'string'}
    ]
});

Ext.define('PartInfoList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'partNo',type:'string'},
        {name:'partOrderQty',type:'number'},
        {name:'partOnShippingQty',type:'number'},
        {name:'partInboundQty',type:'number'},
        {name:'partOrderBalance',type:'number'},
        {name:'partStatus',type:'int'}
    ]
});

Ext.define('UploadInfoList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'kanbanIdUpload',type:'string'},
        {name:'kanbanPlanNo',type:'string'},
        {name:'kanbanPlanNoDisplay',type:'string'},
        {name:'transportMode',type:'string'},
        {name:'totalOrderQty',type:'number'},
        {name:'totalOnShippingQty',type:'number'},
        {name:'totalInboundQty',type:'number'},
        {name:'totalOrderBalance',type:'number'},
        {name:'status',type:'int'},
        {name:'uploadFileType',type:'int'},
        {name:'uploadId',type:'string'},
        {name:'uploadTime',type:'string'}
    ]
});

Ext.define('OrderForecast',{
    extend:'Ext.data.Model',
    fields:[
        {name:'orderForecastId',type:'string'},
        {name:'orderForecastNo',type:'string'},
        {name:'impRegion',type:'string'},
        {name:'customerCode',type:'string'},
        {name:'orderMonth',type:'string'},
        {name:'firstFcMonth',type:'string'},
        {name:'lastFcMonth',type:'string'},
        {name:'remark',type:'string'},
        {name:'uploadedBy',type:'string'},
        {name:'uploadedDate',type:'string'}
    ]
});
Ext.define('MainRoute',{
    extend:'Ext.data.Model',
    fields:[
        {name:'westCustCode',type:'string'},
        {name:'westPartsNo',type:'string'},
        {name:'ttcPartsNo',type:'string'},
        {name:'customerCode',type:'string'},
        {name:'supplierCode',type:'string'},
        {name:'ssmsMainRoute',type:'string'},
        {name:'ssmsVendorRoute',type:'string'},
        {name:'ssmsSupplierCode',type:'string'},
        {name:'expPartsId',type:'string'},
        {name:'showFlag',type:'int'},
    ]
});
Ext.define('OrderDownload',{
    extend:'Ext.data.Model',
    fields:[
        {name:'cfcNo',type:'string'},
        {name:'fcDateLan',type:'string'},
        {name:'firstFcMonthLan',type:'string'},
        {name:'lastFcMonthLan',type:'string'},
        {name:'remark',type:'string'},
        {name:'uploadedByName',type:'string'},
        {name:'uploadedDateLan',type:'string'}
    ]
});

Ext.define('OrderHistory',{
    extend:'Ext.data.Model',
    fields:[
        {name:'orderStatusId',type:'int'},
        {name:'orderId',type:'int'},
        {name:'ttcPartsNo',type:'string'},
        {name:'impPONo',type:'string'},
        {name:'expPONo',type:'string'},
        {name:'customerOrderNo',type:'string'},
        {name:'expSODate',type:'string'},
        {name:'expRegion',type:'string'},
        {name:'impRegion',type:'string'},
        {name:'ttcSuppCode',type:'string'},
        {name:'ttcCusCode',type:'string'},
        {name:'transportMode',type:'int'},
        {name:'orderQty',type:'number'},
        {name:'expBalanceOrder',type:'number'},
        {name:'expWHStock',type:'number'},
        {name:'expOutboundQty',type:'number'},
        {name:'onShippingQty',type:'number'},
        {name:'inboundQty',type:'number'},
        {name:'cancelledQty',type:'number'},
        {name:'impOrderBalance',type:'number'},
        {name:'customerBalance',type:'number'},
        {name:'stockTransferOut',type:'number'},
        {name:'transferToDetails',type:'string'},
        {name:'transferFrom',type:'string'},
        {name:'disOrderStatus',type:'int'}
    ]
});


Ext.define('StockStatusListModel',{
    extend:'Ext.data.Model',
    fields:[
        {name:'ttcPartsNo',type:'string'},
        {name:'partsNameEn',type:'string'},
        {name:'partsNameCn',type:'string'},
        {name:'expCounty',type:'string'},
        {name:'supplierCode',type:'string'},
        {name:'supplierPartsNo',type:'string'},
        {name:'impCountry',type:'string'},
        {name:'customerCode',type:'string'},
        {name:'oldPartsNo',type:'string'},
        {name:'custPartsNo',type:'string'},
        {name:'custBackNo',type:'string'},
        {name:'businessType',type:'string'},
        {name:'partsType',type:'string'},
        {name:'spq',type:'string'},
        {name:'carModel',type:'string'},
        {name:'delayAdjustmentPattern',type:'string'},
        {name:'expBalanceQty',type:'string'},
        {name:'expPlanDelayQty',type:'string'},
        {name:'expPlanDelayAddSea',type:'string'},
        {name:'expPlanDelayAddAir',type:'string'},
        {name:'expPlanDelayMonSea',type:'string'},
        {name:'expPlanDelayMonAir',type:'string'},
        {name:'expFuturePlanQty',type:'string'},
        {name:'expFuturePlanAddSea',type:'string'},
        {name:'expFuturePlanAddAir',type:'string'},
        {name:'expFuturePlanMonSea',type:'string'},
        {name:'expFuturePlanMonAir',type:'string'},
        {name:'expStockQty',type:'string'},
        {name:'expOnshippingQty',type:'string'},
        {name:'inRackQty',type:'string'},
        {name:'totalImportStockQty',type:'string'},
        {name:'eciOnholdQty',type:'string'},
        {name:'impWhsQty',type:'string'},
        {name:'preparedObQty',type:'string'},
        {name:'custStockQty',type:'string'},
        {name:'endingStockDate',type:'string'},
        {name:'totalSupplyChainQty',type:'string'},
        {name:'totalNotInRdQty',type:'string'},
        {name:'etdDelayQty',type:'string'},
        {name:'inboundDelayQty',type:'string'},
        {name:'ngOnholdQty',type:'string'},
        {name:'stockQty1a',type:'string'},
        {name:'stockDays1a',type:'string'},
        {name:'stockBoxes1a',type:'string'},
        {name:'minAlarm1a',type:'string'},
        {name:'maxAlarm1a',type:'string'},
        {name:'nextInboundDate',type:'string'},
        {name:'nextInboundQty',type:'string'},
        {name:'nextInvoice',type:'string'},
        {name:'stockDaysPlan1a',type:'string'},
        {name:'stockQty1b',type:'string'},
        {name:'stockDays1b',type:'string'},
        {name:'stockBoxes1b',type:'string'},
        {name:'minAlarm1b',type:'string'},
        {name:'maxAlarm1b',type:'string'},
        {name:'stockDaysPlan1b',type:'string'},
        {name:'endDateTm',type:'string'},
        {name:'shortageDateTm',type:'string'},
        {name:'minAlarmTm',type:'string'},
        {name:'maxAlarmTm',type:'string'},
        {name:'minStockDayTm',type:'string'},
        {name:'maxStockDayTm',type:'string'},
        {name:'minBoxesTm',type:'string'},
        {name:'maxBoxesTm',type:'string'},
        {name:'partsRemark',type:'string'},
        {name:'rundownRemark',type:'string'},
        {name:'endDateF1',type:'string'},
        {name:'shortageDateF1',type:'string'},
        {name:'minAlarmF1',type:'string'},
        {name:'maxAlarmF1',type:'string'},
        {name:'minStockDayF1',type:'string'},
        {name:'maxStockDayF1',type:'string'},
        {name:'minBoxesF1',type:'string'},
        {name:'maxBoxesF1',type:'string'},
        {name:'endDateF2',type:'string'},
        {name:'shortageDateF2',type:'string'},
        {name:'minAlarmF2',type:'string'},
        {name:'maxAlarmF2',type:'string'},
        {name:'minStockDayF2',type:'string'},
        {name:'maxStockDayF2',type:'string'},
        {name:'minBoxesF2',type:'string'},
        {name:'maxBoxesF2',type:'string'},
        {name:'endDateF3',type:'string'},
        {name:'shortageDateF3',type:'string'},
        {name:'minAlarmF3',type:'string'},
        {name:'maxAlarmF3',type:'string'},
        {name:'minStockDayF3',type:'string'},
        {name:'maxStockDayF3',type:'string'},
        {name:'minBoxesF3',type:'string'},
        {name:'maxBoxesF3',type:'string'},
        {name:'endDateF4',type:'string'},
        {name:'shortageDateF4',type:'string'},
        {name:'minAlarmF4',type:'string'},
        {name:'maxAlarmF4',type:'string'},
        {name:'minStockDayF4',type:'string'},
        {name:'maxStockDayF4',type:'string'},
        {name:'minBoxesF4',type:'string'},
        {name:'maxBoxesF4',type:'string'},
        {name:'endDateF5',type:'string'},
        {name:'shortageDateF5',type:'string'},
        {name:'minAlarmF5',type:'string'},
        {name:'maxAlarmF5',type:'string'},
        {name:'minStockDayF5',type:'string'},
        {name:'maxStockDayF5',type:'string'},
        {name:'minBoxesF5',type:'string'},
        {name:'maxBoxesF5',type:'string'},
        {name:'endDateF6',type:'string'},
        {name:'shortageDateF6',type:'string'},
        {name:'minAlarmF6',type:'string'},
        {name:'maxAlarmF6',type:'string'},
        {name:'minStockDayF6',type:'string'},
        {name:'maxStockDayF6',type:'string'},
        {name:'minBoxesF6',type:'string'},
        {name:'maxBoxesF6',type:'string'},
        {name:'endDate3',type:'string'},
        {name:'shortageDate3',type:'string'},
        {name:'stockDay3',type:'string'},
        {name:'safetyAlarm3',type:'string'},
        {name:'orderSafetyStock',type:'string'},
        {name:'shortageAddOn3',type:'string'},
        {name:'safetyAddOn3',type:'string'},
        {name:'overRatioDate4',type:'string'},
        {name:'fluctuationRatio4',type:'string'},
        {name:'gapValue4',type:'string'},
        {name:'safetyAddOn3',type:'string'},
        {name:'minStock',type:'string'},
        {name:'maxStock',type:'string'},
        {name:'minBox',type:'string'},
        {name:'maxBox',type:'string'},
        {name:'saCustStockFlag',type:'string'},
        {name:'lastObActualDate',type:'string'},
        {name:'buildOutFlag',type:'string'},
        {name:'buildOutMonth',type:'string'},
        {name:'lastOrderMonth',type:'string'},
        {name:'lastDeliveryMonth',type:'string'},
        {name:'inActiveFlag',type:'string'}
    ]
});

Ext.define('CPIIFS01EntityList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'officeCode',type:'string'},
        {name:'batchId',type:'string'},
        {name:'batchTypeName',type:'string'},
        {name:'dateTime',type:'string'},
        {name:'status',type:'string'}
    ]
});

Ext.define('CPIIFS02EntityList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'ifIpNo',type:'string'},
        {name:'officeCode',type:'string'},
        {name:'wrongInvocie',type:'string'},
        {name:'matchedInvocie',type:'string'},
        {name:'businessPattern',type:'string'},
        {name:'status',type:'string'},
        {name:'mismatchDate',type:'string'},
        {name:'matchDate',type:'string'}
    ]
});

Ext.define('CPIIFS03EntityList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'invoiceNo',type:'string'},
        {name:'etd',type:'string'},
        {name:'eta',type:'string'},
        {name:'transportMode',type:'string'},
        {name:'vesselName',type:'string'},
        {name:'blno',type:'string'},
        {name:'bldate',type:'string'},
        {name:'expRegion',type:'string'},
        {name:'impRegion',type:'string'},
        {name:'uploadedBy',type:'string'},
        {name:'uploadedDate',type:'string'}
    ]
});

Ext.define('TnmNonTtcCustomer',{
    extend:'Ext.data.Model',
    fields:[
        {name:'nonTtcCustId',type:'string'},
        {name:'kanbCustomerCode',type:'string'},
    ]
});

//VVPLS01
Ext.define('VVPLS01PageList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'exportOfficeCode',type:'int'},
        {name:'fullCompanyName',type:'string'},
        {name:'headquarterCode',type:'int'},
        {name:'officeBranchCode',type:'int'},
        {name:'productMateialCode',type:'int'},
        {name:'sectionofthecarCode',type:'int'},
        {name:'productionProcessCode',type:'int'},
        {name:'overallEvaluationCode',type:'int'},
        {name:'createdDate',type:'date',convert:function(value){return new Date(value);}},
        {name:'lastUpdatedDate',type:'date',convert:function(value){return new Date(value);}},
        {name:'createdDateForDisplay',type:'string'},
        {name:'lastUpdatedDateForDisplay',type:'string'},
        {name:'statusCode',type:'int'},
        {name:'exportOfficeName',type:'string'},
        {name:'headquarterName',type:'string'},
        {name:'officeBranchName',type:'string'},
        {name:'productMateialName',type:'string'},
        {name:'sectionofthecarName',type:'string'},
        {name:'productionProcessName',type:'string'},
        {name:'overallEvaluationName',type:'string'},
        {name:'statusName',type:'string'}
    ]
});

//VVPDS01
Ext.define('VVPDS01DetailList',{
    extend:'Ext.data.Model',
    fields:[
        {name:'exportOfficeId',type:'int'},
        {name:'exportOfficeName',type:'string'},
        {name:'createdDate',type:'date',convert:function(value){return new Date(value);}},
        {name:'createdBy',type:'string'},
        {name:'lastUpdatedDate',type:'date',convert:function(value){return new Date(value);}},
        {name:'statusId',type:'int'},
        {name:'statusName',type:'string'},
        {name:'fullCompanyName',type:'string'},
        {name:'localAddress',type:'string'},
        {name:'capital',type:'string'},
        {name:'businessActivity',type:'string'},
        {name:'shareholder',type:'string'},
        {name:'companyWebsite',type:'string'},
        {name:'noOfEmployee',type:'string'},
        {name:'mainCustomer',type:'string'},
        {name:'headquarterId',type:'int'},
        {name:'headquarterName',type:'string'},
        {name:'endUserOem',type:'string'},
        {name:'officeBranchId',type:'int'},
        {name:'officeBranchName',type:'string'},
        {name:'productMateialId',type:'int'},
        {name:'productMateialName',type:'string'},
        {name:'sectionofthecarId',type:'int'},
        {name:'sectionofthecarName',type:'string'},
        {name:'productionProcessId',type:'int'},
        {name:'productionprocessName',type:'string'},
        {name:'remarks1',type:'string'},
        {name:'businessWithLocalTtcId',type:'int'},
        {name:'businessWithLocalTtcName',type:'string'},
        {name:'NdaAgreementId',type:'int'},
        {name:'NdaAgreementName',type:'string'},
        {name:'suppilerTarget',type:'string'},
        {name:'appealingPoint',type:'string'},
        {name:'overallEvaluationId',type:'int'},
        {name:'overallEvaluationName',type:'string'},
        {name:'riskConcernIfAny',type:'string'},
        {name:'remarks2',type:'string'},
        {name:'supportingDocument1',type:'string'},
        {name:'supportingDocument2',type:'string'},
        {name:'supportingDocument3',type:'string'},
        {name:'supportingDocument4',type:'string'}
    ]
});