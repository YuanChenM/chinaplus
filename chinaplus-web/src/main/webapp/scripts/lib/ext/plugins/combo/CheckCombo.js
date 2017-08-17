Ext.define('Ext.ux.CheckCombo', {
	extend : 'Ext.form.field.ComboBox',
	alias : 'widget.checkcombo',
	multiSelect : true,
	allSelector : undefined,
	addAllSelector : false,
	allText : chinaplus.label.Common_Button_All,
	editable: false,
	createPicker : function() {
		var me = this, picker, menuCls = Ext.baseCSSPrefix + 'menu', opts = Ext
				.apply({
					pickerField : me,
					selectionModel: me.pickerSelectionModel,
					floating : true,
					hidden : true,
					ownerCt : me.ownerCt,
					cls : me.el ? (me.el.up('.' + menuCls) ? menuCls : '') : '',
					store: me.getPickerStore(),
					displayField : me.displayField,
					focusOnToFront : false,
					pageSize : me.pageSize,
					tpl : [
							'<tpl for=".">',
							'<li role="option" class="x-boundlist-item" style="white-space: nowrap;"><span class="x-combo-checker">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{'
									+ me.displayField.replace(new RegExp(' ','gm'),'&#160;') + '}</span></li>', '</tpl>']
				}, me.listConfig, me.defaultListConfig);

		picker = me.picker = Ext.create('Ext.view.BoundList', opts);
		if (me.pageSize) {
			picker.pagingToolbar.on('beforechange', me.onPageChange, me);
		}
		
		if (!picker.initialConfig.maxHeight) {
			picker.on({
				'beforeshow': me.onBeforePickerShow,
				'scope': me
            });
			}
		picker.getSelectionModel().on({
			'beforeselect': me.onBeforeSelect,
			'beforedeselect': me.onBeforeDeselect,
			scope: me
				});
		picker.getNavigationModel().navigateOnSpace = false;
		me.on('change',me.adjustCls, me);

		return picker;
	},
	getValue : function() {
		if (this.value)
			return this.value.join(',');
		else
			return undefined;
	},
	getSubmitValue : function() {
		return this.getValue();
	},
	expand : function() {
		var me = this, bodyEl, ariaDom, picker, doc;
		if (me.rendered && !me.isExpanded && !me.isDestroyed) {
			bodyEl = me.bodyEl;
			picker = me.getPicker();
			doc = Ext.getDoc();
			picker.setMaxHeight(picker.initialConfig.maxHeight);
			if (me.matchFieldWidth) {
				picker.width = me.bodyEl.getWidth();
			}

			picker.show();
			// Add for ChinaPlus UAT issue001(drop down list cannot work after scrolling left and right) start
			picker.el.setX(0);
			picker.el.setY(0);
			// Add for ChinaPlus UAT issue001 end
			me.isExpanded = true;
			me.alignPicker();
			bodyEl.addCls(me.openCls);

			if (me.addAllSelector === true && Ext.isEmpty(me.allSelector)) {
				me.allSelector = picker.listEl
						.insertHtml(
								'afterBegin',
								'<li class="x-boundlist-item" role="option" style="white-space: nowrap;"><span class="x-combo-checker">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
										+ me.allText.replace(new RegExp(' ','gm'),'&#160;') + '</span></li>', true);
				me.allSelector.on('click', function(e) {
							if (me.allSelector.hasCls('x-boundlist-selected')) {
								me.allSelector
										.removeCls('x-boundlist-selected');

								me.setValue(undefined, true);
								me.fireEvent('select', me, []);
							} else {
								var records = [];
								me.store.each(function(record) {
											records.push(record);	
										});
								me.allSelector.addCls('x-boundlist-selected');

								me.setValue(records, true);
								me.fireEvent('select', me, records);
							}
						});
				me.allSelector.on('mouseover', function(e) {
							if (!me.allSelector.hasCls('x-boundlist-item-over'))
								me.allSelector.addCls('x-boundlist-item-over');
						});
				me.allSelector.on('mouseleave', function(e) {
							if (me.allSelector.hasCls('x-boundlist-item-over'))
								me.allSelector
										.removeCls('x-boundlist-item-over');
						});
				
				// set vale -- add by liu for when set default value, all is not unselected
				me.adjustCls(me, me.getValue());
			}

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
	},
	
	adjustCls : function(self, newValue, oldValue) {
	    
	    if (!self.allSelector) {
	        return;
	    }
	    
		if(!Ext.isEmpty(newValue)){
			var values = newValue.split(',');
			if (self.addAllSelector == true && self.allSelector != false) {
                if (values.length === self.store.getTotalCount())
                    self.allSelector.addCls('x-boundlist-selected');
                else
                    self.allSelector.removeCls('x-boundlist-selected');
            }
		}else{
		    if (self.addAllSelector == true && self.allSelector != false) {
		    self.allSelector.removeCls('x-boundlist-selected');
		}
		}
	},
	doQuery: function(queryString, forceAll, rawQuery) {
        var me = this,
            store = me.getStore(),
            
            refreshFilters = store.filters && !store.filters.length && !!queryString,
            
            queryPlan = me.beforeQuery({
                query: queryString || '',
                rawQuery: rawQuery,
                forceAll: forceAll,
                combo: me,
                cancel: false
            });
        
        if (queryPlan !== false && !queryPlan.cancel) {
            
            if (me.queryCaching && !refreshFilters && queryPlan.query === me.lastQuery) {
                
                if (Ext.isEmpty(me.allSelector)) {
                	me.getPicker().refresh();
                }
                me.expand();
            } else 
            {
                me.lastQuery = queryPlan.query;
                if (me.queryMode === 'local') {
                    me.doLocalQuery(queryPlan);
                } else {
                    me.doRemoteQuery(queryPlan);
                }
            }
        }
        return true;
    },
	//must be used before reloading store in all selector mode.
	destroyList : function(){
	   var me = this;
	   if(!Ext.isEmpty(me.allSelector)){
    	   me.picker.listEl.el.dom.innerHTML = '';
    	   me.allSelector = undefined;
	   }
	}
});
