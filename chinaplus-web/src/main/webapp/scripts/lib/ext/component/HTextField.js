Ext.define('Ext.ux.HTextField', {
    extend : 'Ext.form.field.Text',
    alias : 'widget.htextfield',
    tabIndex: -1,
    
    initComponent : function(){
    	var me = this;
    	
        me.callParent();
        
    },
    
    listeners: {
        afterrender : {
            fn : function(cmp){
                if (!cmp.readOnly && !cmp.allowBlank) {
                    cmp.setFieldStyle('background:rgb(255, 255, 153);');
                    cmp.inputEl.dom.tabIndex = -1;
                }
            }
        },
        change : {
            fn : function(cmp, value){
                //cmp.setValue(value.replace(/[^\u0000-\u00ff]/g,''));
                
                if(!cmp.readOnly && !cmp.isEdited){
                    cmp.isEdited = true;
                }
            }
        }
    }
});