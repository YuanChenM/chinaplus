/**
 * List filters are able to be preloaded/backed by an Ext.data.Store to load
 * their options the first time they are shown.
 *
 * List filters are also able to create their own list of values from  all unique values of
 * the specified {@link #dataIndex} field in the store at first time of filter invocation.
 *
 * Example Usage:
 *
 *     var filters = Ext.create('Ext.grid.Panel', {
 *         ...
 *         columns: [{
 *             text: 'Size',
 *             dataIndex: 'size',
 *
 *             filter: {
 *                 type: 'list',
 *                 // options will be used as data to implicitly creates an ArrayStore
 *                 options: ['extra small', 'small', 'medium', 'large', 'extra large']
 *             }
 *         }],
 *         ...
 *     });
 */
Ext.define('Ext.grid.headerfilters.filter.RList', {
    extend: 'Ext.grid.headerfilters.filter.SingleFilter',
    alias: 'grid.headerfilter.rlist',

    type: 'list',

    operator: 'in',
    
    itemNumber:1,

    itemDefaults: {
    	xtype:'combo',
    	layout:'fit',
    	queryMode:'local',
    	displayField:'text',
    	valueField:'id',
    	emptyText:''
    },

    /**
     * @cfg {Array} [options]
     * `data` to be used to implicitly create a data store
     * to back this list when the data source is **local**. If the
     * data for the list is remote, use the {@link #store}
     * config instead.
     *
     * If neither store nor {@link #options} is specified, then the choices list is automatically
     * populated from all unique values of the specified {@link #dataIndex} field in the store at first
     * time of filter invocation.
     *
     * Each item within the provided array may be in one of the
     * following formats:
     *
     *   - **Array** :
     *
     *         options: [
     *             [11, 'extra small'],
     *             [18, 'small'],
     *             [22, 'medium'],
     *             [35, 'large'],
     *             [44, 'extra large']
     *         ]
     *
     *   - **Object** :
     *
     *         labelField: 'name', // override default of 'text'
     *         options: [
     *             {id: 11, name:'extra small'},
     *             {id: 18, name:'small'},
     *             {id: 22, name:'medium'},
     *             {id: 35, name:'large'},
     *             {id: 44, name:'extra large'}
     *         ]
     * 
     *   - **String** :
     *
     *         options: ['extra small', 'small', 'medium', 'large', 'extra large']
     *
     */

    /**
     * @cfg {String} idField
     * Defaults to 'id'.
     */
    idField: 'id',

    /**
     * @cfg {String} labelField
     * Defaults to 'text'.
     */
    labelField: 'text',

    /**
     * @cfg {String} paramPrefix
     * Defaults to 'Loading...'.
     */
    loadingText: 'Loading...',

    /**
     * @cfg {Boolean} loadOnShow
     * Defaults to true.
     */
    loadOnShow: true,

    /**
     * @cfg {Boolean} single
     * Specify true to group all items in this list into a single-select
     * radio button group. Defaults to false.
     */
    single: false,

    plain: true,

    defaultValue: undefined,
    
    allText: 'All',

    /**
     * @cfg {Ext.data.Store} [store]
     * The {@link Ext.data.Store} this list should use as its data source
     * when the data source is **remote**. If the data for the list
     * is local, use the {@link #options} config instead.
     *
     * If neither store nor {@link #options} is specified, then the choices list is automatically
     * populated from all unique values of the specified {@link #dataIndex} field in the store at first
     * time of filter invocation.
     */

    destroy: function () {
        var me = this,
            store = me.optionsStore;
            
        if (store) {
            if (me.autoStore) {
                store.destroyStore();
            } else {
                store.un('unload', me.onLoad, me);
            }
        }

        me.callParent();
    },

    /**
     * @private
     * Creates the Menu for this filter.
     * @param {Object} config Filter configuration
     * @return {Ext.menu.Menu}
     */
    createMenu: function(config) {
        var me = this,
            gridStore = me.grid.store,
            optionsStore = me.optionsStore,
            options = me.options,
            menu, data;
            
        me.callParent(arguments);

        menu = me.menu;

        if (optionsStore) {
            data = optionsStore.getData();
            if (!data.length) {
                optionsStore.on('load', me.createMenuStore, me);
                if(me.params){
                	optionsStore.load({
                		params: me.params
                	});
                }else{
                optionsStore.load();
                }
            } else {
                me.createMenuItems(optionsStore);
            }

        }
        // If there are supplied options, then we know the optionsStore is local.
        else if (options) {
            me.createMenuStore(options);
        }
        // A ListMenu which is completely unconfigured acquires its store from the unique values of its field in the store.
        else if (gridStore.getData().length) {
            me.createMenuStore();
        }
        // If there are no records in the grid store, then we know it's async and we need to listen for its 'load' event.
        else {
            gridStore.on('load', me.createMenuStore, menu, {single: true});
        }
    },

    /** @private */
    createMenuItems: function (store) {
        var me = this,
            menu = me.menu,
            data = store.getData(),
            len = data.length,
            itemDefaults = me.getItemDefaults(),
            records, gid, itemValue, i, config, margin;
            
            config = Ext.apply({}, me.getItemDefaults());
	        if (config.iconCls && !('labelClsExtra' in config)) {
	            config.labelClsExtra = Ext.baseCSSPrefix + 'grid-filters-icon ' + config.iconCls;
	        }
	        delete config.iconCls;
	        config[id] = me.name+'Filter';
	        config['multiSelect'] = me.multiSelect;
	        
	        if(me.multiSelect){
	           config['xtype'] = 'checkcombo';
	           config['addAllSelector'] = true;
	           config['allText'] = me.allText;
	           config['editable'] = false;
	        }else{
	           config['forceSelection'] = true;
	        }
	        
	        if(me.defaultValue){
	        	config['value'] = me.defaultValue;
	        }
	        
			if (!Ext.isEmpty(me.listeners)) {
				config['listeners'] = me.listeners;
			}
	        
			if(!me.inputItem){
	        me.inputItem = me.column.add(config);
			}else{
				if(me.inputItem.picker){
				    me.inputItem.picker.listEl.el.dom.innerHTML = '';
				}
			    me.inputItem.allSelector = undefined;
			    me.inputItem.store.removeAll();
			}
	        
	        me.inputItem.setWidth(me.column.width);
        	me.inputItem.updateLayout();
        	
        	me.column.on({
                scope: me,
                resize: function(item, w, h, ow, oh){
                	if(!me.inputItem.el){
                	   return;
                	}
                    me.inputItem.move('b',0);
                    var diss = item.getRegion().bottom-me.inputItem.getRegion().bottom;
                    if(diss>0){
                       me.inputItem.move('b',diss);
                    }else{
                       me.inputItem.move('t',0-diss);
                    }
                    if(me.inputItem && me.inputItem.isExpanded){
                        me.inputItem.picker.setWidth(item.getWidth());
                    }
                }
            });
	        me.inputItem.setStore(store);
	        
	        if(me.defaultValue){
	        	me.filter.setValue(me.defaultValue);
	        }
	        me.inputItem.on({
	            scope: me,
	            change: {
	                fn: me.findRecord
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
	                me.inputItem.move('b',0);
                    var diss = o.getRegion().bottom-me.inputItem.getRegion().bottom;
                    if(diss>0){
                       me.inputItem.move('b',diss);
                    }else{
                       me.inputItem.move('t',0-diss);
                    }
	            }
	        });
	        /*me.inputItem.getPicker().on({
                scope: this,
                el: {
                    mouseleave: function(e){
                        this.hide();
                    }
                }
            });*/
	        TRF.util.pickers(TRF.access.resourceId, me.inputItem.id);
	        
	        Ext.apply(me.inputItem,{
	        	onListSelectionChange: function(list, selectedRecords) {
	        		Ext.form.field.ComboBox.prototype.onListSelectionChange.apply(this, arguments);
                	if('&#160;' === this.getValue()+''){
                		this.setValue(undefined);
                	}else if((this.getValue()+'').indexOf(',&#160;')>0 || (this.getRawValue()+'').indexOf(',&#160;')>0){
                        this.setValue((this.getValue()+'').replace(',&#160;',''));
                    }
	            },
	            onItemClick : function(){
	            	Ext.form.field.ComboBox.prototype.onItemClick.apply(this, arguments);
	            	if('&#160;' === this.getValue()+'' || '&#160;' === this.getRawValue()+''){
	            		this.setValue(undefined);
                	}else if((this.getValue()+'').indexOf(',&#160;')>0 || (this.getRawValue()+'').indexOf(',&#160;')>0){
                        this.setValue((this.getValue()+'').replace(',&#160;',''));
                    }
	            }
	        });
	        
	        if(me.inputItem.el){
	            me.inputItem.move('b',0);
                var diss = me.column.getRegion().bottom-me.inputItem.getRegion().bottom;
                if(diss>0){
                   me.inputItem.move('b',diss);
                }else{
                   me.inputItem.move('t',0-diss);
                }
	        }
	        
            me.loaded = true;
//        }
    },

    createMenuStore: function (options) {
        var me = this,
            storeOptions = [],
            i, len, value, store;

        options = options || me.grid.store.collect(me.column.dataIndex, false, true) || [];
        if(!me.multiSelect){
        	storeOptions.push(['&#160;', '&#160;']);
        }
        if('object' === Ext.typeOf(options)){
        	for (i = 0, len = options.getData().length; i < len; i++) {
        		value = options.getAt(i);
        		storeOptions.push([value.get(me.idField), value.get(me.labelField)]);
        	}
        }else{
            for (i = 0, len = options.length; i < len; i++) {
                value = options[i];
                switch (Ext.typeOf(value)) {
                    case 'array': 
                        storeOptions.push(value);
                        break;
                    case 'object':
                        storeOptions.push([value[me.idField], value[me.labelField]]);
                        break;
                    default:
                        if (value != null) {
                            storeOptions.push([value, value]);
                        }
                }
            }
        }
        
        store = me.column.store = new Ext.data.ArrayStore({
            fields: [me.idField, me.labelField],
            data: storeOptions
        });

        me.createMenuItems(store);

        me.loaded = true;
        me.autoStore = true;
    },
    findRecord: function (combo, newValue, oldValue, eOpts){
        var me = this;
        if(combo.findRecordByDisplay(newValue) || combo.findRecordByValue(newValue)){
            me.setValue(combo, newValue, oldValue, eOpts);
        }

        if(me.multiSelect){
            me.setValue(combo, newValue, oldValue, eOpts);
        }
    },
    /**
     * @private
     * Template method that is to set the value of the filter.
     */
    setValue: function (combo, newValue, oldValue, eOpts) {
        var me = this,
            items = me.column.items,
            value,
            i, len, checkItem;

        for (i = 0, len = items.length; i < len; i++) {
            checkItem = items.getAt(i);
            if (checkItem.value) {
            	value = checkItem.value;
            	break;
            }
        }

        if(value+'' === '&#160;'){
        	value = undefined;
        }else if((value+'').indexOf(',&#160;')>0){
            (value+'').replace(',&#160;','');
        }

        me.filter.setValue(value);
        combo.setValue(value);
    },

    /**
     * Lists will initially show a 'loading' item while the data is retrieved from the store.
     * In some cases the loaded data will result in a list that goes off the screen to the
     * right (as placement calculations were done with the loading item). This adapter will
     * allow show to be called with no arguments to show with the previous arguments and
     * thus recalculate the width and potentially hang the menu from the left.
     */
    show: function () {
        if (this.loadOnShow && !this.loaded && !this.optionsStore.loading) {
            this.optionsStore.load();
        }

        this.callParent();
    },

    activateMenu: function () {
        var me = this,
            items = me.menu.items,
            value = me.filter.getValue(),
            i, len, checkItem;

        for (i = 0, len = items.length; i < len; i++) {
            checkItem = items.getAt(i);

            if (value.indexOf(checkItem.value) > -1) {
                checkItem.setChecked(true, /*suppressEvents*/ true);
            }
        }
    }
});
