Ext.define('Ext.ux.selection.CheckBoxModel', {
    extend : 'Ext.selection.CheckboxModel',
    alias : 'widget.checkcombomodel',
    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
        return '<div class="' + Ext.baseCSSPrefix + 'grid-row-checker" role="presentation" tabIndex=-1 onFocus="TRF.util.focusCheckDiv(this);" onBlur="TRF.util.blurCheckDiv(this);">&#160;</div>';
    },
    getHeaderConfig: function() {
        var me = this,
            showCheck = me.showHeaderCheckbox !== false; 

        return {
            isCheckerHd: showCheck,
            //text : '&#160;',
            //text : '<div style="text-align:center" class="x-grid-header-checker" tabindex=0 >&#160;</div>',
            text : '<div class="' + Ext.baseCSSPrefix + 'grid-header-checker" role="presentation" tabIndex=-1 onFocus="TRF.util.focusCheckDiv(this);" onBlur="TRF.util.blurCheckDiv(this);" onkeypress="TRF.util.pressCheckDiv(this, event);">&#160;</div>',
            clickTargetName: 'el',
            width: me.headerWidth,
            sortable: false,
            draggable: false,
            resizable: false,
            hideable: false,
            menuDisabled: true,
            dataIndex: '',
            tdCls: me.tdCls,
            cls: showCheck ? Ext.baseCSSPrefix + 'column-header-checkbox ' : '',
            defaultRenderer: me.renderer.bind(me),
            editRenderer: me.editRenderer || me.renderEmpty,
            locked: me.hasLockedHeader()
        };
    },
    updateHeaderState: function() {
        // check to see if all records are selected
        var me = this,
            store = me.store,
            storeCount = store.getCount(),
            views = me.views,
            hdSelectStatus = false,
            selectedCount = 0,
            selected, len, i, grayCount = 0;
            
        if (!store.isBufferedStore && storeCount > 0) {
            selected = me.selected;
            hdSelectStatus = true;
            /*for (i = 0, len = selected.getCount(); i < len; ++i) {
                if (store.indexOfId(selected.getAt(i).id) === -1) {
                    break;
                }
                ++selectedCount;
            }*/
            for (i = 0, len = store.getCount(); i < len; ++i) {
                if (selected.indexOfKey(store.getAt(i).id) === -1 && store.getAt(i).forceUncheck !== true ) {
                    break;
                }
                selected.indexOfKey(store.getAt(i).id);
                ++selectedCount;
                if(store.getAt(i).forceUncheck === true){
                    ++grayCount;
                }
            }
            hdSelectStatus = (storeCount === selectedCount && storeCount !== grayCount);
        }
            
        if (views && views.length) {
            me.toggleUiHeader(hdSelectStatus);
        }
    }
});

TRF.util.focusCheckDiv = function(o){
    o.style.border = 'thin dotted gray';
}

TRF.util.blurCheckDiv = function(o){
    o.style.border = '';
}

TRF.util.pressCheckDiv = function(o, e){
	var keynum;
	var className;
    if(window.event){
        keynum = e.keyCode;
    }else if(e.which){
        keynum = e.which;
    }
    if(String.fromCharCode(keynum) == ' '){
    	o.parentElement.parentElement.parentElement.click();
    }
}