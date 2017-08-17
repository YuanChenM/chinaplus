Ext.define('Ext.ux.grid.feature.Summary', {
    extend : 'Ext.grid.feature.Summary',
    alias : 'feature.chinaplussummary',
    
    dock: 'bottom',
    
    // Synchronize column widths in the docked summary Component
    onColumnHeaderLayout: function() {
        var view = this.view,
            columns = view.headerCt.getVisibleGridColumns(),
            column,
            len = columns.length, i,
            summaryEl = this.summaryBar.el,
            el;

        for (i = 0; i < len; i++) {
            column = columns[i];
            el = summaryEl.down(view.getCellSelector(column));
            if (el) {
                if(el.dom.className.indexOf('x-grid-cell-special') == -1){
                   el.dom.className += ' x-grid-cell-special';
                }
                if(el.dom.className.indexOf('x-grid-back-editable') != -1){
                   el.dom.className = el.dom.className.replace('x-grid-back-editable','');
                }
                if(el.dom.style.borderColor != '#d0d0d0'){
                   el.dom.style.borderColor = '#d0d0d0';
                }
                el.setWidth(column.width || (column.lastBox ? column.lastBox.width : 100));
            }
        }
    }
    
});