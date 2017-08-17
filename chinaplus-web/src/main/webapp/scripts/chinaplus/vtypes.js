Ext.apply(Ext.form.VTypes, {

	email: function(val, field){
	    // closure these in so they are only created once.
	    var email = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
	    return email.test(val);
	},
	emailText : 'This field should be an e-mail address in the format "user@domain.com"',
	
	tel: function(val, field){
	    // closure these in so they are only created once.
	    return /^[\u0020\u0026-\u0040\u005B-\u0060\u007B-\u007D]*$/.test(val);
	},
	telText : 'This field should be a valid tel. Only can fill in numbers and symbol.',

	daterange : function(val, field) {
		var date = field.parseDate(val);
		if (!date) {
			return true;
		}

		if (field.startDateField
				&& (!field.dateRangeMax || (date.getTime() != field.dateRangeMax.getTime()))) {
			var start = Ext.getCmp(field.startDateField);
			start.setMaxValue(date);
			field.dateRangeMax = date;
			start.validate();

		} else if (field.endDateField
				&& (!field.dateRangeMin || (date.getTime() != field.dateRangeMin.getTime()))) {
			var end = Ext.getCmp(field.endDateField);
			end.setMinValue(date);
			field.dateRangeMin = date;
			end.validate();

		}
		/*
		 * Always return true since we're only using this vtype to set the
		 * min/max allowed values (these are tested for after the vtype test)
		 */
		return true;
	},
	timerange : function(val, field) {
		var date = field.parseDate(val);
		if (!date) {
			return true;
		}
		if (field.startDateField) {
			var start = Ext.getCmp(field.startDateField);
			if (start.getValue() && field.parseDate(start.getValue())) {
				field.minValue = field.parseDate(start.getValue());
				//start.maxValue = date;
				//start.validate();
			}

		} else if (field.toDateField) {
			var end = Ext.getCmp(field.toDateField);
			if (end.getValue() && field.parseDate(end.getValue())) {
				field.maxValue = field.parseDate(end.getValue());
				//end.minValue = date;
				//end.validate();
			}
		}
		return true;
	},
	timediff : function(val, field){
		var date = field.parseDate(val);
		if (!date) {
			return true;
		}
		if (field.startDateField) {
			var start = Ext.getCmp(field.startDateField);
			if (start.getValue() && field.parseDate(start.getValue())) {
				field.diffTime = field.parseDate(start.getValue());
				//start.maxValue = date;
				//start.validate();
			}

		} else if (field.toDateField) {
			var end = Ext.getCmp(field.toDateField);
			if (end.getValue() && field.parseDate(end.getValue())) {
				field.diffTime = field.parseDate(end.getValue());
				//end.minValue = date;
				//end.validate();
			}
		}
		return true;
	},
	
	password : function(val, field) {
		if (field.initialPassField) {
			var pwd = Ext.getCmp(field.initialPassField);
			return (val == pwd.getValue());
		}
		return true;
	},

	passwordText : 'Passwords do not match',

	/**
	 * number check
	 */
	number : function(value) {
		return /^\d{1,9}$/.test(value);
	},
	numberText : 'Must be a numeric data',

	/**
	 * ip check
	 */
	ip : function(value) {
		return /^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$/
				.test(value)
				|| (value.indexOf('COM') == 0 && value.length > 3)
				|| (value.indexOf('LPT') == 0 && value.length > 3)
	},
	ipText : 'Must be a ip or COM or LPT'
});