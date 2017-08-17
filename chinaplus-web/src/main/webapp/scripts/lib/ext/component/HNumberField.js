Ext.define('Ext.ux.HNumberField', {
    extend : 'Ext.form.field.Number',
    alias : 'widget.hnumberfield',
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