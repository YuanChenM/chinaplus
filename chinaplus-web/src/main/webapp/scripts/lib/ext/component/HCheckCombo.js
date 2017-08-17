Ext.define('Ext.ux.HCheckCombo', {
    extend : 'Ext.form.field.ComboBox',
    alias : 'widget.hcheckcombo',
    multiSelect : true,
    allSelector : undefined,
    addAllSelector : false,
    allText : chinaplus.label.Common_Button_All,
    editable: false,
    isLoaded: false,
    tabIndex: -1,
    initComponent : function(){
        var me = this;
        me.callParent();
        me.store.on('load', function(){
            me.isLoaded = true;
        });
    },
    setParams:function(params){
        var me = this;
        me.store.params = params;
        me.isLoaded = false;
    },
    onTriggerClick:function(){
    	var me = this;
    	if(me.isExpanded){
    	   me.collapse();
    	}else{
    	   me.expand();
    	}
        
    },
    createPicker : function() {
        var me = this, picker, menuCls = Ext.baseCSSPrefix + 'menu', opts = Ext
                .apply({
                    pickerField : me,
                    selectionModel: me.pickerSelectionModel,
                    floating : true,
                    hidden : true,
                    ownerCt : me.ownerCt,
                    cls : me.el ? (me.el.up('.' + menuCls) ? menuCls : '') : '',
                    store: me.getPickerStore(),
                    displayField : me.displayField,
                    focusOnToFront : false,
                    pageSize : me.pageSize,
                    tpl : [
                            '<tpl for=".">',
                            '<li role="option" class="x-boundlist-item" style="white-space: nowrap;"><span class="x-combo-checker">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{'
                                    + me.displayField.replace(new RegExp(' ','gm'),'&#160;') + '}</span></li>', '</tpl>']
                }, me.listConfig, me.defaultListConfig);

        picker = me.picker = Ext.create('Ext.view.BoundList', opts);
//        if (me.pageSize) {
//            picker.pagingToolbar.on('beforechange', me.onPageChange, me);
//        }
//
//        me.mon(picker, {
//                    itemclick : me.onItemClick,
//                    refresh : me.onListRefresh,
//                    scope : me
//                });
//
//        me.mon(picker.getSelectionModel(), {
//                    'beforeselect' : me.onBeforeSelect,
//                    'beforedeselect' : me.onBeforeDeselect,
//                    'selectionchange' : me.onListSelectionChange,
//                    scope : me
//                });
//        me.on('change',me.adjustCls, me);
        if (!picker.initialConfig.maxHeight) {
            picker.on({
                'beforeshow': me.onBeforePickerShow,
                'scope': me
            });
        }
        picker.getSelectionModel().on({
                    'beforeselect' : me.onBeforeSelect,
                    'beforedeselect' : me.onBeforeDeselect,
                    scope : me
                });
		picker.getNavigationModel().navigateOnSpace = false;
        me.on('change',me.adjustCls, me);
        return picker;
    },
    getValue : function() {
        if (this.value)
            return this.value.join(',');
        else
            return undefined;
    },
    getSubmitValue : function() {
        return this.getValue();
    },
    expand : function() {
        var me = this;
        me.callParent();
        if(me.store && !me.isLoaded && me.store.proxy.config.type != 'memory'){
        	me.destroyList();
    	    TRF.util.loadStore(me.store, null, function(records, operation, success){
                me.isLoaded = true;
                me.insertAllSelector();
            });
        }else{
            me.insertAllSelector();
        }
    },
    insertAllSelector : function(){
        var me = this, bodyEl, picker, doc;

        if (me.rendered && !me.isDestroyed) {
            bodyEl = me.bodyEl;
            picker = me.getPicker();
            doc = Ext.getDoc();
            picker.setMaxHeight(picker.initialConfig.maxHeight);
            if (me.matchFieldWidth) {
                picker.width = me.bodyEl.getWidth();
            }

            picker.show();
            me.isExpanded = true;
            me.alignPicker();
            bodyEl.addCls(me.openCls);
            
            if (me.addAllSelector === true && Ext.isEmpty(me.allSelector)) {
                me.allSelector = picker.listEl
                        .insertHtml(
                                'afterBegin',
                                '<li class="x-boundlist-item" role="option" style="white-space: nowrap;"><span class="x-combo-checker">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
                                        + me.allText.replace(new RegExp(' ','gm'),'&#160;') + '</span></li>', true);
                me.allSelector.on('click', function(e) {
                    if (me.allSelector.hasCls('x-boundlist-selected')) {
                        me.allSelector
                                .removeCls('x-boundlist-selected');

                        me.setValue(undefined, true);
                        me.fireEvent('select', me, []);
                    } else {
                        var records = [];
                        me.store.each(function(record) {
                                    records.push(record);   
                                });
                        me.allSelector.addCls('x-boundlist-selected');

                        me.setValue(records, true);
                        me.fireEvent('select', me, records);
                    }
                });
                me.allSelector.on('mouseover', function(e) {
                    if (!me.allSelector.hasCls('x-boundlist-item-over'))
                        me.allSelector.addCls('x-boundlist-item-over');
                });
                me.allSelector.on('mouseleave', function(e) {
                    if (me.allSelector.hasCls('x-boundlist-item-over'))
                        me.allSelector
                                .removeCls('x-boundlist-item-over');
                });
            }
        }
    },
    adjustCls : function(self, newValue, oldValue) {
        if(!Ext.isEmpty(newValue)){
            var values = newValue.split(',');
            if (self.addAllSelector == true && self.allSelector != false) {
                if (values.length === self.store.getTotalCount())
                    self.allSelector.addCls('x-boundlist-selected');
                else
                    self.allSelector.removeCls('x-boundlist-selected');
            }
        }else{
            self.allSelector.removeCls('x-boundlist-selected');
        }
    },
    //must be used before reloading store in all selector mode.
    destroyList : function(){
       var me = this;
       if(!Ext.isEmpty(me.allSelector)){
           me.picker.listEl.el.dom.innerHTML = '';
           me.allSelector = undefined;
       }
    },
    
    listeners: {
        afterrender : {
            fn : function(cmp){
                if (!cmp.readOnly && !cmp.allowBlank) {
                    cmp.setFieldStyle('background:rgb(255, 255, 153);');
                    cmp.inputEl.dom.tabIndex = 0;
                }
            }
        },
        change : {
            fn : function(cmp, value){
                if(!cmp.readOnly && !cmp.isEdited){
                    cmp.isEdited = true;
                }
            }
        }
    }
});
