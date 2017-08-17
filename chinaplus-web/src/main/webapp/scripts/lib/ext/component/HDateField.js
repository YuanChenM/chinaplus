Ext.define('Ext.ux.HDateField', {
    extend : 'Ext.form.field.Date',
    alias : 'widget.hdatefield',
    tabIndex: -1,
    
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