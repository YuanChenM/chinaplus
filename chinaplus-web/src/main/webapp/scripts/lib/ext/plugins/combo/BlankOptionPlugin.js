/**
 * Plugin to add a blank option for combo.
 * 
 * @screen Common
 * @author duan_kai
 */
Ext.define('Ext.date.plugin.BlankOption', {
    extend : 'Ext.plugin.Abstract',
    alias : 'plugin.blankoption',
    pluginId : 'blankoption',
    
    init : function(field) {
    	var store = field.getStore();
    	store.on('load', function(s){
    	    if(s.data.length > 0){
               s.insert(0,{id:'&#160;',text:'&#160;'});
            }
    	}, store);
    	
    	field.on('change', function(o, v){
    	   if(v == '&#160;'){
    	       o.setValue(undefined);
    	   }
    	}, this);
    },
    
    removeBlankOption :  function(){
        var store = this.cmp.getStore();
        store.remove(store.getAt(0));
    },
    
    insertBlankOption : function(){
        var store = this.cmp.getStore();
        if(store.getAt(0).id != '&#160;'){
            store.insert(0,{id:'&#160;',text:'&#160;'});
        }
    },
    
    replaceById : function(id, record){
        var store = this.cmp.getStore();
        var i, temp;
        for(i=0;i<store.data.length;i++){
            temp = store.getAt(i).data;
            if(temp.id == id){
                store.remove(store.getAt(i));
                store.insert(i, record);
            }
        }
    },
    
    replaceByText : function(text, record){
        var store = this.cmp.getStore();
        var i, temp;
        for(i=0;i<store.data.length;i++){
            temp = store.getAt(i).data;
            if(temp.text == text){
                store.remove(store.getAt(i));
                store.insert(i, record);
            }
        }
    },
    
    isExist : function(id){
        var result = -1;
        var store = this.cmp.getStore();
        var i, temp;
        for(i=0;i<store.data.length;i++){
        	temp = store.getAt(i).data;
            if(temp.id == id){
                result = i;
                break;
            }
        }
        return result;
    },
    
    insertNewWithIdCheck : function(record){
        this.removeBlankOption();
        var i = this.isExist(record.id);
        if(i == -1){
            this.cmp.getStore().insert(0, record);
        }else{
            this.cmp.getStore().insert(i, record);
        }
        this.insertBlankOption();
    }
});