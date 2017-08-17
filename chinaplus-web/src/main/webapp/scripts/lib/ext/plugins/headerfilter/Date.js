/**
 * Filter by a configurable Ext.picker.DatePicker menu
 *
 * Example Usage:
 *
 *     var grid = Ext.create('Ext.grid.Panel', {
 *         ...
 *         columns: [{
 *             // required configs
 *             text: 'Date Added',
 *             dataIndex: 'dateAdded',
 *
 *             filter: {
 *                 type: 'date',
 *      
 *                 // optional configs
 *                 dateFormat: 'm/d/Y',  // default
 *                 beforeText: 'Before', // default
 *                 afterText: 'After',   // default
 *                 onText: 'On',         // default
 *                 pickerOpts: {
 *                     // any DatePicker configs
 *                 },
 *      
 *                 active: true // default is false
 *             }
 *         }],
 *         ...
 *     });
 */
Ext.define('Ext.grid.headerfilters.filter.Date', {
    extend: 'Ext.grid.headerfilters.filter.TriFilter',
    alias: 'grid.headerfilter.date',
    uses: ['Ext.picker.Date', 'Ext.menu.Menu'],

    type: 'date',

    config: {
        /**
         * @cfg {Object} [fields]
         * Configures field items individually. These properties override those defined
         * by `{@link #itemDefaults}`.
         *
         * Example usage:
         *      fields: {
         *          gt: { // override fieldCfg options
         *              width: 200
         *          }
         *      },
         */
        fields: {
            lt: {},
            gt: {}
        }
    },
    
    itemNumber: 2,

    itemDefaults: {
        xtype: 'hdatefield',
        anchor:'100%',
        width: 125,
        editable:false,
        format:'d M Y',
        enableKeyEvents:true,
        focusOnToFront:false,
        tabIndex:0,
    },

    /**
     * @cfg {String} dateFormat
     * The date format to return when using getValue.
     * Defaults to 'm/d/Y'.
     */
    dateFormat: 'd M Y',
    
    convertValue: Ext.identityFn,

    /**
     * @private
     * Template method that is to initialize the filter and install required menu items.
     */
    createMenu: function (config) {
        var me = this,
        	listeners = {
                scope: me,
                blur: {
                    fn: me.onInputKeyUp,
                    buffer: 200
                },
                spin: {
                    fn: me.onInputSpin,
                    buffer: 200
                },
                focus: function(o){
                    var column = o.getRefOwner();
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

        this.callParent(arguments);

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
                    var dateFormat;
                    if (TRF.core.language == chinaplus.consts.code.Language.CN) {
                        dateFormat = TRF.cnst.GLOBAL_DATE_YEAR_MONTH_DAY_FORMAT;
                    } else {
                        dateFormat = TRF.cnst.GLOBAL_DATETIME_SHORT_FORMAT;
                    }
                    itemDefaults.format = dateFormat;
                    Ext.merge(cfg, itemDefaults);
                }

                if(me.column.filterPlugins){
                	cfg.plugins = me.column.filterPlugins;
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
                
                /*item.getPicker().on({
                    scope: this,
                    el: {
                        mouseleave: function(e){
                        	this.hide();
                        }
                    }
                });*/
                
                TRF.util.pickers(TRF.access.mainResourceId, item.id);
                
                tempI++;
            }
        }
    },
    
    onInputKeyUp: function (field, e) {
        var value;

        value = {};
        value[field.filterKey] = field.getValue();

        this.setValue(value);
    },
    
    onInputSpin: function (field, direction) {
        var value = {};

        value[field.filterKey] = field.getValue();

        this.setValue(value);
    },

    stopFn: function(e) {
        e.stopPropagation();
    }
});
