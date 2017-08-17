/**
 * @screen common
 * @author wang_jinghui
 */
// register customized VTypes
Ext.apply(Ext.form.field.VTypes, {
	// vtype validation function
	time : function(val, field) {
		// custom Vtype for vtype:'time'
		return /^([1-9]|1[0-9]):([0-5][0-9])(\s[a|p]m)$/i.test(val);
	},
	// vtype Text property: The error text to display when the validation
	// function returns false
	timeText : 'Not a valid time.  Must be in the format "12:34 PM"',
	// vtype Mask property: The keystroke filter mask
	timeMask : /[\d\s:amp]/i
});

Ext.apply(Ext.form.field.VTypes, {	
	mosinput : function(val, field) {
		return /^[\u0020-\u007E]*$/.test(val);
	},
	mosinputText : 'The field has invalid value',
	mosinputMask : /[\u0020-\u007E\s\S]/i
});

/**
 * Not allow space at beginning or end. 
 * 20 space
 * 
 * Not allow following charactor as file name
 * 22 "
 * 2A *
 * 2F /
 * 3A :
 * 3C <
 * 3E >
 * 3F ?
 * 5C \
 * 7C |
*/
Ext.apply(Ext.form.field.VTypes, {
	enfilename : function(val, field) {
		return /^[\u0020-\u0021\u0023-\u0029\u002B-\u002E\u0030-\u0039\u003B\u003D\u0040-\u005B\u005D-\u007B\u007D-\u007E]*$/.test(val);
	},
	enfilenameText : 'The field has invalid value(\\ / : * ? < > " |)',
	enfilenameMask : /[\u0020-\u0021\u0023-\u0029\u002B-\u002E\u0030-\u0039\u003B\u003D\u0040-\u005B\u005D-\u007B\u007D-\u007E]/i
});

Ext.apply(Ext.form.field.VTypes, {
	excel : function(val, field) {
		if (val == null || val == '') {
			return false;
		}
		var strArray = val.split('.');
		if(Ext.isEmpty(strArray) || strArray.length < 2) {
			return false;
		}
		var fileSurfix = strArray[strArray.length - 1].toLowerCase();

		if (fileSurfix == 'xls' || fileSurfix == 'xlsx') {
			return true ;
		}
		return false ;
	},
	excelText : 'Not a excel file.',
	excelMask : /[.]/i
});

/**
 * Get a new validator.
 */
TRF.util.createValidator = function() {
	return new function() {
		/**
		 * @private field. An array of error informations when validation fail.
		 *          Error structure :{id : comId, errors : [msg1, msg2]}
		 */
		var errors = [];

		/**
		 * @private field. the validation result.
		 */
		var result = true;

		/**
		 * @private function. To clear the error informations from last
		 *          validation and reset result.
		 */
		var clear = function() {
			errors = [];
			result = true;
		};

		/**
		 * @private function. Validate a component.
		 * @param cmpInfo.
		 *            Info of the component to be validated, which can be either
		 *            id of that component or the component itself.
		 */
		var doValidate = function(cmpInfo) {
			var cmp = cmpInfo;
			if (Ext.isString(cmpInfo)) {
				cmp = Ext.getCmp(cmpInfo);
			}
			
		    var validateResult = cmp.validate();
            if (!validateResult) {
                errors.push({
                    id : cmpInfo.id,
                    label : cmpInfo.getFieldLabel(),
                    errors : cmp.getErrors()
                });
                result = false;
            }
		};

		/**
		 * @public function.Validate certain inputs specified by param ids.
		 * 
		 * @param ids
		 *            of the input components, which can be either an array of
		 *            ids or multiple id parameters of string type.
		 * @param showMsgBox
		 *            true to show message box
		 */
		this.validate = function(ids, showMsgBox) {
			clear();
			var idArray = [];
			if (Array.isArray(ids)) {
				idArray = ids;
			} else {
				for (var idx = 0; idx < arguments.length; idx++) {
					idArray.push(arguments[idx]);
				}
			}
			idArray.forEach(doValidate);
			if (!result && showMsgBox) {
				TRF.util.showMessageBox(TRF.cnst.MESSAGEBOX_TYPE_WARN, mos.message.w1021);
			}
			return result;
		};

		/**
		 * @private function. Validate the whole certain form specified by param
		 *          formInfo.
		 * 
		 * @param formInfo.
		 *            Info of the form to be validated, which can be either a
		 *            form panel component or id of that form panel component.
		 * @param showMsgBox
		 *            true to show message box
		 */
		var validateForm = function(formInfo, showMsgBox) {
			if (Ext.isString(formInfo)) {
				return validateForm(Ext.getCmp(formInfo).getForm());
			} else {
				clear();
				formInfo.getFields().each(function(item) {
					doValidate(item);
				});
				if (!result && showMsgBox) {
					TRF.util.showMessageBox(TRF.cnst.MESSAGEBOX_TYPE_WARN, mos.message.w1021);
				}
				return result;
			}
		};

		/**
		 * @public function validateForm.
		 */
		this.validateForm = validateForm;
		
		/**
         * @private function. Validate the whole certain items specified by param
         *          arrayInfo.
         * 
         * @param arrayInfo.
         *            Info of the item to be validated.
         * @param showMsgBox
         *            true to show message box
         */
        var validateArray = function(arrayInfo, showMsgBox) {
            clear();
            Ext.Array.each(arrayInfo, function(item) {
                doValidate(item);
            });
            if (!result && showMsgBox) {
                TRF.util.showMessageBox(TRF.cnst.MESSAGEBOX_TYPE_WARN, mos.message.w1021);
            }
            return result;
        };

        /**
         * @public function validateArray.
         */
        this.validateArray = validateArray;

		/**
		 * @public function. Get a copy of the error informations from last
		 *         validation.
		 */
		this.getErrors = function() {
			var resultErrors = [];
			errors.forEach(function(tmpError) {
				resultErrors.push({
					id : tmpError.id,
					label : tmpError.label,
					errors : tmpError.errors
				});
			});
			return resultErrors;
		};

		return this;
	}();
};

/**
 * Validate certain inputs specified by param ids.
 * 
 * @param ids
 *            of the input components, which can be either an array of ids or
 *            multiple id parameters of string type.
 * @param showMsgBox
 *            true to show message box
 */
TRF.util.validate = function(ids, showMsgBox) {
	return TRF.util.createValidator().validate(ids, showMsgBox);
}

/**
 * Validate the whole certain form specified by param formInfo.
 * 
 * @param formInfo.
 *            Info of the form to be validated, which can be either a form panel
 *            component or id of that form panel component.
 * @param showMsgBox
 *            true to show message box
 */
TRF.util.validateForm = function(formInfo, showMsgBox) {
	return TRF.util.createValidator().validateForm(formInfo, showMsgBox);
}

/**
 * Generates param object according to the inputs of certain form.
 * 
 * @param formInfo.
 *            Info of the form that can be either a form panel component or id
 *            of that form panel component according to which the param object
 *            to be generated from.
 */
TRF.util.genParams = function(formInfo) {
	if (Ext.isString(formInfo)) {
		return TRF.util.genParams(Ext.getCmp(formInfo).getForm());
	} else {
		var params = {};
		formInfo.getFields().each(function(item) {
			if ("extLabel" !== item.xtype) {
				params[item.id] = item.value;
			}
		});
		return params;
	}
}