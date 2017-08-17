Ext.define('Ext.date.plugin.EmptyStoreView', {
    extend : 'Ext.plugin.Abstract',
    alias : 'plugin.emptystoreview',
    pluginId : 'emptystoreview',
    
    init : function(grid) {
        /*grid.on('afterlayout', function(g){
            if(g.store.data.length == 0){
            	var eleArray = Ext.Element.query('div.x-grid-item-container', true, g.el.dom);
            	eleArray[eleArray.length - 1].innerHTML = '<div>&#160;</div>';
                //g.view.el.dom.children[1].innerHTML = '<div>&#160;</div>'
            }
            var columns = g.columns,column;
            var i = 0, tempClassName;
            for(i=0;i<columns.length;i++){
                column = g.columns[i];
                tempClassName = column.el.dom.className;
                column.el.dom.className = tempClassName.replace('x-column-header-align-right','x-column-header-align-left');
            }
        }, grid);
        
        var store = grid.getStore();
        store.on('load', function(s){
            if(grid.store.data.length == 0){
            	var eleArray = Ext.Element.query('div.x-grid-item-container', true, grid.el.dom);
                eleArray[eleArray.length - 1].innerHTML = '<div>&#160;</div>';
                //grid.view.el.dom.children[1].innerHTML = '<div>&#160;</div>'
            }
            var columns = grid.columns,column;
            var i = 0, tempClassName;
            for(i=0;i<columns.length;i++){
                column = grid.columns[i];
                tempClassName = column.el.dom.className;
                column.el.dom.className = tempClassName.replace('x-column-header-align-right','x-column-header-align-left');
            }
        }, grid);*/
        
    }
});