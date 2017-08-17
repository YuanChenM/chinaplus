/**
 * @screen common
 * @author wang_jinghui
 */
TRF.util.doMenuAuthentication = function() {
	var isLoaded = false;
	var accessResource = {};

	var loadAccessResources = function(cbFunc, cbArgs) {
		var url = 'accessResource';

		var aclResourcestore = Ext.create('Ext.data.Store', {
			model : TRF.util.createContentModle,
			proxy : {
				type : 'ajax',
				actionMethods : {
					create : "POST",
					read : "POST",
					update : "POST",
					destroy : "POST"
				},
				url : url,
				paramsAsJson : true,
				noCache : false,
				headers : {
					"Accept" : 'application/json',
					"Content-Type" : 'application/json;'
				},
				reader : {
					type : 'json'
				}
			}
		});
		var aclResourcestoreCallback=function(){

		    var resourceArray = aclResourcestore.getProxy().getReader().rawData.swapData;
				accessResource = {};
			if (!Ext.isEmpty(resourceArray)) {
				resourceArray.forEach(function(resource) {
					accessResource[resource.resourceCode] = resource;
				});
			}
				isLoaded = true;
				if (Ext.isFunction(cbFunc) && Array.isArray(cbArgs)) {
					cbFunc.apply(this, cbArgs);
				}
	};

		TRF.util.loadStore(aclResourcestore,'',aclResourcestoreCallback);
	};

	var doAuthFunc = function(contentId, forceLoad) {
		if (forceLoad === true || !isLoaded) {
			loadAccessResources(doAuthFunc, [ contentId ]);
		} else {
			for ( var btnId in accessResource) {
				var btn = Ext.getCmp(btnId);
				if (!Ext.isEmpty(btn)) {
					btn.hide();
				}
			}
		}
	};

	return doAuthFunc;
}();