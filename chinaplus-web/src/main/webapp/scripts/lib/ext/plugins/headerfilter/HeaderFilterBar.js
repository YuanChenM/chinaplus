Ext.define('Ext.grid.headerfilters.toolbar', {
	extend: 'Ext.plugin.Abstract',
    alias: 'plugin.headerfiltertoolbar',
    
    pluginId: 'headerfiltertoolbar',
    
    pagingToolbar: true,
    
    showMore: false,
    
    init: function (grid) {
    	//console.log('enter headerfiltertoolbar');
    	var me = this,
            store, columns, filter, filtertool, pagingtool, apply, clear, field;
            
        //console.dir(grid);
            store = grid.store;
        
        //<debug>
        //Ext.Assert.falsey(me.grid);
        //</debug>
            
        var buttoms = [{
            xtype:'button',
            iconCls: 'btn-refresh',
            text:chinaplus.label.Common_Button_Refresh,
            handler:function(){
                me.applyHandlerFilters();
            }
        },{
            xtype:'button',
            iconCls: 'btn-remove',
            text:chinaplus.label.Common_Button_ClearFilter,
            handler:function(){
                me.clearHandlerFilters();
            }
        }];
        
        if(me.showMore){
            buttoms.push({
                xtype:'button',
                iconCls: 'btn-details',
                text:chinaplus.label.Common_Button_Show + ' ' + TRF.cnst.PAGE_SIZE_ALL,
                handler:function(obj){
                    if(TRF.cnst.PAGE_SIZE_COMMON == store.pageSize) {
                        store.pageSize = TRF.cnst.PAGE_SIZE_ALL;
                        obj.setText(chinaplus.label.Common_Button_Show + ' ' + TRF.cnst.PAGE_SIZE_COMMON);
                    } else {
                        store.pageSize = TRF.cnst.PAGE_SIZE_COMMON;
                        obj.setText(chinaplus.label.Common_Button_Show + ' ' + TRF.cnst.PAGE_SIZE_ALL);
                    }
                    me.applyHandlerFilters();
                }
            });
        }
        
        grid.addDocked({
        	xtype:'toolbar',
        	dock:'top',
        	items:buttoms
        });
        
        if(me.pagingToolbar){
            var pagingtool = Ext.create('Ext.toolbar.Paging',{
            	xtype: 'pagingtoolbar',
    	        pageSize: TRF.cnst.PAGE_SIZE_COMMON,
    	        store: store,
    	        dock: 'bottom',
    	        displayInfo: true,
    	        listeners : {
    	           afterrender : function(paging){
    	               paging.items.getAt(4).inputEl.dom.tabIndex = -1;
    	           }
    	        }
            });
        
            pagingtool.on('beforechange',function(t, pageData, eOpts){
            	var pArray = t.up().plugins;
            	for(var i =0;i<pArray.length;i++){
            		if(pArray[i].ptype === 'headerfiltertoolbar'){
            			pArray[i].loadHandlerFilters();
            			break;
            		}
            	}
            });
            grid.addDocked(pagingtool);
            pagingtool.items.getAt(10).hide();
            //pagingtool.items.getAt(4).el.dom = -1;
        }
        me.grid = grid;
        
        //inactive auto filter
        columns = this.grid.headerCt.getGridColumns();
        
        for (i = 0, len = columns.length; i < len; i++) {
            column = columns[i];
            filter = column.filter;
        }
        
        me.loadHandlerFilters();
    },
    
    destroy: function () {
        //Ext.destroyMembers(this, 'menuItem', 'sep');
        this.callParent();
    },
    
    applyHandlerFilters: function(){
    	var me = this;
    	me.loadHandlerFilters();
    	this.grid.selModel.selected.clear();
    	this.grid.store.loadPage(1);
    },
    
    clearHandlerFilters: function(){
    	var columns = this.grid.headerCt.getGridColumns();
        for (i = 0, len = columns.length; i < len; i++) {
            column = columns[i];
            filter = column.filter;
            if(filter){
	            if((filter.type == 'number' || filter.type == 'tristring')){
	            	field = filter.fields;
	            	if (field){
	            		//field.lt.rawValue = undefined;
	            		field.lt.setValue(filter.filter.lt.defalutValue);
	            		filter.filter.lt.setValue(filter.filter.lt.defalutValue);
	            	}
	            	if (field){
	            		//field.gt.rawValue = undefined;
	            		field.gt.setValue(filter.filter.gt.defalutValue);
	            		filter.filter.gt.setValue(filter.filter.gt.defalutValue);
	            	}
	            }if(filter.type == 'date'){
	            	field = filter.fields;
	            	if (field){
	            		//field.lt.rawValue = undefined;
	            		field.lt.setValue(filter.filter.lt.defalutValue);
	            		filter.filter.lt.setValue(filter.filter.lt.defalutValue);
	            	}
	            	//if (field && field.gt.getValue()){
	            	if (field){
	            		//field.gt.rawValue = undefined;
	            		field.gt.setValue(filter.filter.gt.defalutValue);
	            		filter.filter.gt.setValue(filter.filter.gt.defalutValue);
	            	}
	            	filter.setActive(false);
	            	//console.dir(filter);
	            }else{
	            	if ((filter.type == 'list' || filter.type === 'rlist')){
	            		filter.filter.setValue(filter.defalutValue);
	            		filter.inputItem.setValue(filter.defalutValue);
	            		filter.setActive(false);
	            	}
	            	
	            	if (filter.type == 'string'){
	            		filter.filter.setValue(filter.defalutValue);
	            		filter.inputItem.setValue(filter.defalutValue);
	            		filter.setActive(false);
	            	}
	            }
            }
        };

        this.grid.store.proxy.extraParams.filters = {};

        //if (filterCollection) {
            //filterCollection.endUpdate();
        //}

        //if (autoFilter !== undefined) {
            //store.setAutoFilter(oldAutoFilter);
        //}
    },
    
    loadHandlerFilters: function(){
    	var columns = this.grid.headerCt.getGridColumns();
        var params = {},ltValue,gtValue,ltDate,gtDate;
        for (i = 0, len = columns.length; i < len; i++) {
        	var func = undefined;
            column = columns[i];
            filter = column.filter;
            func = column.filterValueGen;
            if(!Ext.isFunction(func)){
            	func = this.prepareFunc;
            }
            if(filter){
            	if(filter.type === 'number'){
	            	field = filter.fields;
	            	
	            	if (field && field.lt.getValue()){
	            		var toValue = func(field.lt.getValue() * 1);
	            		if(isNaN(toValue)){
	            			field.lt.setValue('');
	            		} else {
	            			params[column.dataIndex+'To'] = toValue;
		            		field.lt.setValue(toValue);
	            		}
	            	}
	            	if (field && field.gt.getValue()){
	            		var fromValue = func(field.gt.getValue() * 1);
	            		if(isNaN(fromValue)){
	            			field.gt.setValue('');
	            		} else {
	            			params[column.dataIndex+'From'] = fromValue;
		            		field.gt.setValue(fromValue);
	            		}
	            	}
	            }else if(filter.type === 'tristring'){
	            	field = filter.fields;
	            	if (field && field.lt.getValue()){
	            		params[column.dataIndex+'To'] = func(field.lt.getValue());
	            	}
	            	if (field && field.gt.getValue()){
	            		params[column.dataIndex+'From'] = func(field.gt.getValue());
	            	}
	            }else if(filter.type === 'date'){
	            	field = filter.fields;
	            	ltValue = field.lt.getValue();
	            	gtValue = field.gt.getValue();
	            	if (field && ltValue){
	            		ltDate = new Date(ltValue);
	            		params[column.dataIndex+'To'] = func(Ext.Date.format(ltDate,'Y-m-d'));
	            	}
	            	if (field && gtValue){
	            		gtDate = new Date(gtValue);
	            		params[column.dataIndex+'From'] = func(Ext.Date.format(gtDate,'Y-m-d'));
	            	}
	            }else{
	            	if ((filter.type === 'list' || filter.type === 'rlist') && filter.filter.getValue()) {
						var valueArray = func(filter.filter.getValue());
						if (valueArray.length > 0) {
							params[filter.filter._property] = valueArray;
						}
					}
	            	
	            	if (filter.type === 'string' && filter.filter.getValue()){
	            		params[filter.filter._property] = func(filter.filter.getValue());
	            	}
	            }
            }
        };
        
        Ext.apply(this.grid.store.proxy.extraParams,{'filters':params});
    },
    setDefaultValues : function(dataIdex, value, ltgt) {
    	var columns = this.grid.headerCt.getGridColumns();
        var params = {},ltValue,gtValue,ltDate,gtDate;
        var func = undefined;
        for (i = 0, len = columns.length; i < len; i++) {
        	column = columns[i];
            filter = column.filter;
            if(filter && (column.dataIndex == dataIdex)){
            	if(filter.type === 'number' 
            		|| filter.type === 'tristring'
            		||	filter.type === 'date'){
            		
	            	field = filter.fields;
	            	if (field && ltgt == 'lt'){
	            		field.lt.setValue(value);
	            		filter.filter.lt.setValue(value);
	            		filter.filter.lt.defalutValue = value;
	            	}
	            	if (field && ltgt == 'gt'){
	            		field.gt.setValue(value);
	            		filter.filter.gt.setValue(value);
	            		filter.filter.gt.defalutValue = value;
	            	}
	            	if(filter.type === 'date'){
	            		filter.setActive(false);
	            	}
	            }else{
	            	filter.filter.setValue(value);
	            	filter.inputItem.setValue(value);
	            	filter.setActive(false);
	            	filter.defalutValue = value;
	            }
            }
        }
    },
	getHandlerFilters : function(){
		var columns = this.grid.headerCt.getGridColumns();
        var params = {},ltValue,gtValue,ltDate,gtDate;
        var func = undefined;
        for (i = 0, len = columns.length; i < len; i++) {
            column = columns[i];
            filter = column.filter;
            func = column.filterValueGen;
            if(!Ext.isFunction(func)){
            	func = this.prepareFunc;
            }
            if(filter){
            	if(filter.type === 'number'){
	            	field = filter.fields;
	            	
	            	if (field && field.lt.getValue()){
	            		var toValue = func(field.lt.getValue() * 1);
	            		if(isNaN(toValue)){
	            			field.lt.setValue('');
	            		} else {
	            			params[column.dataIndex+'To'] = toValue;
		            		field.lt.setValue(toValue);
	            		}
	            	}
	            	if (field && field.gt.getValue()){
	            		var fromValue = func(field.gt.getValue() * 1);
	            		if(isNaN(fromValue)){
	            			field.gt.setValue('');
	            		} else {
	            			params[column.dataIndex+'From'] = fromValue;
		            		field.gt.setValue(fromValue);
	            		}
	            	}
	            }else if(filter.type === 'tristring'){
	            	field = filter.fields;
	            	if (field && field.lt.getValue()){
	            		params[column.dataIndex+'To'] = func(field.lt.getValue());
	            	}
	            	if (field && field.gt.getValue()){
	            		params[column.dataIndex+'From'] = func(field.gt.getValue());
	            	}
	            }else if(filter.type === 'date'){
	            	field = filter.fields;
	            	ltValue = field.lt.getValue();
	            	gtValue = field.gt.getValue();
	            	if (field && ltValue){
	            		ltDate = new Date(ltValue);
	            		params[column.dataIndex+'To'] = func(Ext.Date.format(ltDate,'Y-m-d'));
	            	}
	            	if (field && gtValue){
	            		gtDate = new Date(gtValue);
	            		params[column.dataIndex+'From'] = func(Ext.Date.format(gtDate,'Y-m-d'));
	            	}
	            }else{
	            	if ((filter.type === 'list' || filter.type === 'rlist') && filter.filter.getValue()) {
						var valueArray = func(filter.filter.getValue());
						if (valueArray.length > 0) {
							params[filter.filter._property] = valueArray;
						}
					}
	            	
	            	if (filter.type === 'string' && filter.filter.getValue()){
	            		params[filter.filter._property] = func(filter.filter.getValue());
	            	}
	            }
            }
        };
		return params;
	},
    
    prepareFunc : function(value){
    	if(Ext.isString(value)){
    		return Ext.String.trim(value);
    	}
    	return value;
    }
});