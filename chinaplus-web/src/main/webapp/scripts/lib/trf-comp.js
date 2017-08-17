//-----------------------------------------------------------
Ext.override(Ext.form.field.ComboBox, {
    expand: function() {
        var me = this,
            bodyEl, ariaDom, picker, doc;
        if (me.rendered && !me.isExpanded && !me.isDestroyed) {
            bodyEl = me.bodyEl;
            picker = me.getPicker();
            doc = Ext.getDoc();
            picker.setMaxHeight(picker.initialConfig.maxHeight);
            if (me.matchFieldWidth) {
                picker.width = me.bodyEl.getWidth();
            }
            
            picker.show();
            picker.el.setX(0);
            picker.el.setY(0);
            me.isExpanded = true;
            me.alignPicker();
            bodyEl.addCls(me.openCls);
            
            me.touchListeners = doc.on({
                translate: false,
                touchstart: me.collapseIf,
                scope: me,
                delegated: false,
                destroyable: true
            });
            
            me.scrollListeners = Ext.on({
                scroll: me.onGlobalScroll,
                scope: me,
                destroyable: true
            });
            
            Ext.on('resize', me.alignPicker, me, {
                buffer: 1
            });
            me.fireEvent('expand', me);
            me.onExpand();
        }
    }
});