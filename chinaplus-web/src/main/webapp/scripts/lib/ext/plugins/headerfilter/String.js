/**
 * Filter by a configurable Ext.form.field.Text
 *
 * Example Usage:
 *
 *     var grid = Ext.create('Ext.grid.Panel', {
 *         ...
 *         columns: [{
 *             text: 'Name',
 *             dataIndex: 'name',
 *
 *             filter: {
 *                 // required configs
 *                 type: 'string',
 *
 *                 // optional configs
 *                 value: 'foo',
 *                 active: true, // default is false
 *                 itemDefaults: {
 *                     // any Ext.form.field.Text configs accepted
 *                 }
 *             }
 *         }],
 *         ...
 *     });
 */
Ext.define('Ext.grid.headerfilters.filter.String', {
    extend: 'Ext.grid.headerfilters.filter.SingleFilter',
    alias: 'grid.headerfilter.string',

    type: 'string',

    operator: 'like',
    
    itemNumber:1,

    itemDefaults: {
        xtype: 'htextfield',
        enableKeyEvents: true,
        hideEmptyLabel: true,
        labelSeparator: '',
        margin: 0,
        columnWidth:100,
        layout:'fit',
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
     * Template method that is to initialize the filter and install required menu items.
     */
    createMenu: function () {
        var me = this,
            config,margin;

        me.callParent();
        config = Ext.apply({}, me.getItemDefaults());
        if (config.iconCls && !('labelClsExtra' in config)) {
            config.labelClsExtra = Ext.baseCSSPrefix + 'grid-filters-icon ' + config.iconCls;
        }
        delete config.iconCls;
        me.inputItem = me.column.add(config);
        
        me.inputItem.setWidth(me.column.width);
        me.inputItem.updateLayout();
		
        me.column.on({
            scope: me,
            resize: function(item, w, h, ow, oh){
            	me.inputItem.move('b',0);
            	var diss = item.getRegion().bottom-me.inputItem.getRegion().bottom;
            	if(diss>0){
            	   me.inputItem.move('b',diss);
            	}else{
            	   me.inputItem.move('t',0-diss);
            	}
            }
        });
		
        me.inputItem.on({
            scope: me,
            keyup: {
                fn: me.onInputKeyUp,
                buffer: 200
            },
            focus: function(o){
            	var column = o.getRefOwner();
            	//var grid = o.getRefOwner().getRefOwner().grid;
            	var temp,grid;
            	for(temp = o.getRefOwner(); (!temp || (temp.getXType() != 'gridpanel' && temp.getXType() != 'grid')); temp = temp.getRefOwner()){
                    grid = temp.grid;
                }
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
                me.inputItem.move('b',0);
                var diss = o.getRegion().bottom-me.inputItem.getRegion().bottom;
                if(diss>0){
                   me.inputItem.move('b',diss);
                }else{
                   me.inputItem.move('t',0-diss);
                }
            },
            change: function(o, v){
                //o.setValue(v.replace(/[^\u0000-\u00ff]/g,''));
            }
        });
    },

    /**
     * @private
     * Handler method called when there is a keyup event on this.inputItem
     */
    onInputKeyUp: function (field, e) {
    	var me = this;

        me.setValue(field.getValue());
    },

    /**
     * @private
     * Template method that is to set the value of the filter.
     * @param {Object} value The value to set the filter.
     */
    setValue: function (value) {
        var me = this;
        if (me.inputItem) {
            me.inputItem.setValue(value);
        }

        me.filter.setValue(value);
    },

    activateMenu: function () {
        this.inputItem.setValue(this.filter.getValue());
    }
});
