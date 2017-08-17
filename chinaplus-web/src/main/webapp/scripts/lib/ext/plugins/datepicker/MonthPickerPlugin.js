/**
 * Plugin to make date field pick months only.
 * 
 * @screen Common
 * @author wang_jinghui
 */
Ext.define('Ext.date.plugin.monthPicker', {
	extend : 'Ext.plugin.Abstract',
	alias : 'plugin.monthPicker',
	pluginId : 'monthPicker',

	format : TRF.cnst.GLOBAL_YEAR_MONTH_FORMAT,
	showClear : true,

	init : function(datefield) {
		var me = this;
		me.callParent(arguments);
		var monthFormat;
		if (TRF.core.language == chinaplus.consts.code.Language.CN) {
			monthFormat = TRF.cnst.GLOBAL_DATE_YEAR_MONTH_FORMAT;
		} else {
			monthFormat = TRF.cnst.GLOBAL_YEAR_MONTH_FORMAT;
		}
		datefield.format = monthFormat;

		var triggerClickCount = 0;

		Ext.apply(datefield, {
			onTriggerClick : function() {
				if (!datefield.isExpanded && datefield.monthPending) {
					return;
				}
				if (datefield.isExpanded) {
					datefield.monthPending = true;
				}
				var isPickerCreated = Ext.isDefined(datefield.picker);
				Ext.form.DateField.prototype.onTriggerClick.apply(this, arguments);

				var datepicker = datefield.picker;
				if (Ext.isEmpty(datepicker)) {
					return;
				}
				//if (!isPickerCreated) {
					Ext.apply(datepicker, {
						onCancelClick : function() {
							datefield.onTriggerClick();
						},
						onOkClick : function(monthPicker, valueArray) {
							datefield.setValue(new Date(valueArray[1], valueArray[0]));
							datefield.onTriggerClick();
						}
					});
				//}
				if (datepicker.isVisible()) {
					datepicker.showMonthPicker(false);
				}

				if (triggerClickCount == 0) {
					triggerClickCount++;
					if (me.showClear) {
						var btn = new Ext.Button({
							text : chinaplus.label.Common_Button_Clear,
							handler : function() {
								datefield.setValue(null);
								datefield.onTriggerClick();
							}
						});
						btn.render(datepicker.monthPicker.okBtn.container);
					}
				}
				if (datefield.monthPending) {
					TRF.util.taskRunner.start({
						run : function(runCount) {
							if (runCount > 1) {
								TRF.util.taskRunner.stop(this);
								delete datefield.monthPending;
							}
						},
						interval : 230
					});
				}
			}
		});
	}
});