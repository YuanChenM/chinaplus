/**
 * Plugin to add clear button on Ext.datepicker.
 * 
 * @screen Common
 * @author wang_jinghui
 */
Ext.define('Ext.date.plugin.Clear', {
	extend : 'Ext.plugin.Abstract',
	alias : 'plugin.datePickerClear',
	pluginId : 'datePickerClear',

	init : function(datefield) {
		var me = this;
		me.callParent(arguments);

		var triggerClickCount = 0;
		Ext.apply(datefield, {
			onTriggerClick : function() {
				Ext.form.DateField.prototype.onTriggerClick.apply(this, arguments);
				if (triggerClickCount == 0) {
					triggerClickCount++;
					if (Ext.isEmpty(this.picker)) {
						return;
					}
					var btn = new Ext.Button({
						text : chinaplus.label.Common_Button_Clear,
						handler : function() {
							if(datefield.startDateField) {
								datefield.setValue("9999-12-31");
							} else if(datefield.endDateField) {
								datefield.setValue("1000-01-01");
							}
							datefield.setValue(null);
							datefield.collapse();
						}
					});
					btn.render(this.picker.todayBtn.container);
				}
			}
		});
	}
});