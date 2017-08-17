Ext.define('Ext.ux.HCombo', {
    extend : 'Ext.form.field.ComboBox',
    alias : 'widget.hcombo',
    multiSelect : false,
    isLoaded : false,
    tabIndex: -1,
    tpl:Ext.create('Ext.XTemplate','<tpl for=".">','<div class="x-boundlist-item" style="white-space: nowrap;">{text}</div>','</tpl>'),
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
    
    expand : function() {
        var me = this;        
        me.callParent();       
        if(me.store && !me.isLoaded && me.store.proxy.config.type != 'memory'){
            TRF.util.loadStore(me.store, null, function(records, operation, success){
                me.isLoaded = true;
            });
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