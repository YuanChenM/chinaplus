/**
 * Filter type for {@link Ext.grid.column.Number number columns}.
 */
Ext.define('Ext.grid.headerfilters.filter.Number', {
    extend: 'Ext.grid.headerfilters.filter.TriFilter',
    alias: ['grid.headerfilter.number', 'grid.headerfilter.numeric'],

    uses: ['Ext.form.field.Number'],

    type: 'number',

    config: {
        /**
         * @cfg {Object} [fields]
         * Configures field items individually. These properties override those defined
         * by `{@link #itemDefaults}`.
         *
         * Example usage:
         *
         *      fields: {
         *          // Override itemDefaults for one field:
         *          gt: {
         *              width: 200
         *          }
         *
         *          // "lt" and "eq" fields retain all itemDefaults
         *      },
         */
        fields: {
            gt: {},
            lt: {}
        }
    },
    
    itemNumber: 2,

    itemDefaults: {
    	xtype: 'htextfield',
        enableKeyEvents: true,
        hideEmptyLabel: true,
        labelSeparator: '',
        margin: 0,
        layout:'fit',
        validation: true,
        maskRe : /[\d\-]/,
        tabIndex:0,
    },

    menuDefaults: {
        // A menu with only form fields needs some body padding. Normally this padding
        // is managed by the items, but we have no normal menu items.
        bodyPadding: 3,
        showSeparator: false
    },

    /**
     * @private
     * See the Date type for a full implementation.
     */
    convertValue: Ext.identityFn,

    createMenu: function () {
        var me = this,
            listeners = {
                scope: me,
                keyup: {
                    fn: me.onInputKeyUp,
                    buffer: 200
                },
                focus: function(o){
                    var column = o.getRefOwner();
                    var grid = o.getRefOwner().getRefOwner().grid;
                    var record = grid.store.getAt(0);
                    var cell = undefined;
                    if(!Ext.isEmpty(record)){
                       cell = grid.view.getCell(record, column);
                       //grid.view.scrollBy(cell.getRegion().left-o.getRegion().left,0,false); 
                    }
                },
                el: {
                    click: function(e) {
                    	e.target.focus();
                    }
                },
                afterrender: function(o){
                    o.move('b',0);
                    if(o.id === o.getRefOwner().items.items[2].id){
                       var diss2 = o.getRefOwner().getRegion().bottom-o.getRegion().bottom;
                       if(diss2>0){
                           o.move('b',diss2);
                        }else{
                           o.move('t',0-diss2);
                        }
                    }else{
                        var diss1 = o.getRefOwner().getRegion().top-o.getRegion().bottom;
                        if(diss1>0){
                           o.move('b',diss1);
                        }else{
                           o.move('t',0-diss1);
                        }
                    }
                    
                }
            },
            itemDefaults = me.getItemDefaults(),
            fields = me.getFields(),
            field, i, len, key, item, cfg;

        me.callParent();

        me.fields = {};

        var tempI = 0;
        for (i = 0, len = me.menuItems.length; i < len; i++) {
            key = me.menuItems[i];
            if (key !== '-') {
               field = fields[key];

                cfg = {
                    labelClsExtra: Ext.baseCSSPrefix + 'grid-filters-icon ' + field.iconCls
                };

                if (itemDefaults) {
                    Ext.merge(cfg, itemDefaults);
                }

                Ext.merge(cfg, field);
                delete cfg.iconCls;
                me.fields[key] = item = me.column.add(cfg);
                
                item.setWidth(me.column.width);
        		item.updateLayout();
        		
        		me.column.on({
                    scope: me,
                    resize: function(item, w, h, ow, oh){
                        me.column.items.items[2].move('b',0);
                        var diss2 = item.getRegion().bottom-me.column.items.items[2].getRegion().bottom;
                        if(diss2>0){
                           me.column.items.items[2].move('b',diss2);
                        }else{
                           me.column.items.items[2].move('t',0-diss2);
                        }
                        
                        me.column.items.items[1].move('b',0);
                        var diss1 = me.column.items.items[2].getRegion().top-me.column.items.items[1].getRegion().bottom;
                        if(diss1>0){
                           me.column.items.items[1].move('b',diss1);
                        }else{
                           me.column.items.items[1].move('t',0-diss1);
                        }
                    }
                });
                
                item.filter = me.filter[key];
                item.filterKey = key;
                item.on(listeners);
                
                tempI++;
            }
        }
    },

    /**  
     * @private
     * Handler method called when there is a keyup event on an input
     * item of this menu.
     */
    onInputKeyUp: function (field, e) {
        var value;

        value = {};
        value[field.filterKey] = field.getValue();

        this.setValue(value);
    },


    /**  
     * @private
     * Handler method called when there is a spin event on a NumberField
     * item of this menu.
     */
    onInputSpin: function (field, direction) {
        var value = {};

        value[field.filterKey] = field.getValue();

        this.setValue(value);
    },

    stopFn: function(e) {
        e.stopPropagation();
    }
});
