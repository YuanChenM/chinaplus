<%--
@screen main
@author liu_yinchuan
--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="date" class="java.util.Date"  scope="request"></jsp:useBean>
<c:set var="jsParam" value="${date.time}"  scope="request"></c:set>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="user" value="${sessionScope['LOGIN_INFO']}" scope="session"> </c:set>
<c:set var="language" value="${user.language.name}"  scope="session"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	 <meta http-equiv="X-UA-Compatible" content="IE=edge" />
	 <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	 <link rel="stylesheet" type="text/css" href="resources/style/stylesheet.css" />
	 <!--  href="resources/style/stylesheet-companyCalendar.css"--> 
	 <link rel="stylesheet" type="text/css" href="resources/style/loadingStyle.css" />
	 
	 <title>CHINA PLUS</title>
	 <style type='text/css'> 
	    input::-ms-clear{display:none;}
	 </style>
</head>
<body onselectstart="return((event.srcElement.type=='text' || event.srcElement.type=='textarea' || event.srcElement.id=='messageArea-body') && !event.srcElement.unselectable)">
<div id="loading">
 <input type="hidden" id="loginUser.userName" value="${user.userName}"/>
 <input type="hidden" id="loginUser.loginId" value="${user.loginId}"/>
 <input type="hidden" id="loginUser.userId" value="${user.userId}"/>
 <input type="hidden" id="loginUser.officeId" value="${user.officeId}"/>
 <input type="hidden" id="loginUser.language" value="${user.language.name}"/>
 <input type="hidden" id="loginUser.redirect" value="${redirect}"/>
 <input type="hidden" id="loginUser.vvAllFlag" value="${user.vvAllFlag}"/>
 <input type="hidden" id="loginUser.aisinAllFlag" value="${user.aisinAllFlag}"/>
 <div class="loading-indicator">
 	<img src="resources/images/extanim32.gif" width="32" height="32" style="margin-right: 8px; float: left; vertical-align: top;" />
	CHINA PLUS <br>
	<span id="loading-msg">${loadStyles}</span>
 </div>
</div>
<div id="initScriptLib">
    <script type="text/javascript">
    	loadingMsg=document.getElementById('loading-msg');
    	loadingMsg.innerHTML = '${loadCoreAPI}';
    </script>
    <script type="text/javascript">loadingMsg.innerHTML = '${loadUIComp}';</script>
    <script type="text/javascript" src="scripts/lib/ext/ext-all-debug.js"></script>
	<script type="text/javascript" src="scripts/lib/ext/tools/include-ext.js"></script>
	<script type="text/javascript" src="scripts/lib/ext/tools/options-toolbar.js"></script>
    <script type="text/javascript" src="scripts/lib/ext/resources/ext-locale/build/ext-locale-${language}.js"></script>
	<script type="text/javascript">loadingMsg.innerHTML = '${loadResource}';</script>
	<script type="text/javascript" src="labelResource.js?p=${jsParam}"></script>
    <script type="text/javascript" src="msgResource.js?p=${jsParam}"></script>
    <script type="text/javascript">loadingMsg.innerHTML = '${loadSystemAPI}';</script>
	<script type="text/javascript" src="scripts/lib/trf.js?p=${jsParam}"></script>
	<script type="text/javascript" src="scripts/lib/trf-cnst.js?p=${jsParam}"></script>
	<script type="text/javascript" src="scripts/lib/trf-validation.js?p=${jsParam}"></script>
	<script type="text/javascript" src="scripts/lib/trf-comp.js?p=${jsParam}"></script>
	<script type="text/javascript">
        TRF.core.token = '${sessionScope.token}';
    </script>
	<script type="text/javascript" src="scripts/chinaplus/chinaplus.js?p=${jsParam}"></script>
	<script type="text/javascript" src="scripts/chinaplus/model.js"></script>
	<script type="text/javascript" src="scripts/chinaplus/chinaplus-com.js?p=${jsParam}"></script>
	<script type="text/javascript" src="scripts/chinaplus/chinaplus-cnst.js?p=${jsParam}"></script>
    <script type="text/javascript" src="scripts/chinaplus/vtypes.js"></script>
	<script type="text/javascript" src="scripts/mainmenu.js?p=${jsParam}"></script>
	<script type="text/javascript" src="scripts/lib/ext/plugins/headerfilter/Base.js"></script>
	<script type="text/javascript" src="scripts/lib/ext/plugins/headerfilter/Filters.js"></script>
	<script type="text/javascript" src="scripts/lib/ext/plugins/headerfilter/TriFilter.js"></script>
	<script type="text/javascript" src="scripts/lib/ext/plugins/headerfilter/SingleFilter.js"></script>
	<script type="text/javascript" src="scripts/lib/ext/plugins/headerfilter/Date.js"></script>
	<script type="text/javascript" src="scripts/lib/ext/plugins/headerfilter/HeaderFilterBar.js?p=${jsParam}"></script>
	<script type="text/javascript" src="scripts/lib/ext/plugins/headerfilter/List.js?p=${jsParam}"></script>
	<script type="text/javascript" src="scripts/lib/ext/plugins/headerfilter/RList.js?p=${jsParam}"></script>
	<script type="text/javascript" src="scripts/lib/ext/plugins/headerfilter/Number.js"></script>
	<script type="text/javascript" src="scripts/lib/ext/plugins/headerfilter/String.js"></script>
	<script type="text/javascript" src="scripts/lib/ext/plugins/headerfilter/TriString.js"></script>
	<script type="text/javascript" src="scripts/lib/ext/plugins/itemselector/MultiSelect.js"></script>
	<script type="text/javascript" src="scripts/lib/ext/plugins/itemselector/ItemSelector.js"></script>
	<script type="text/javascript" src="scripts/lib/ext/plugins/datepicker/DatePickerClearPlugin.js"></script>
	<script type="text/javascript" src="scripts/lib/ext/plugins/datepicker/MonthPickerPlugin.js"></script>
	<script type="text/javascript" src="scripts/lib/ext/plugins/combo/CheckCombo.js?p=${jsParam}"></script>
	<script type="text/javascript" src="scripts/lib/ext/plugins/calendar/CompanyCalendar.js"></script>
	<script type="text/javascript" src="scripts/lib/ext/plugins/combo/BlankOptionPlugin.js"></script>
	<script type="text/javascript" src="scripts/lib/ext/plugins/selection/CheckboxModel.js"></script>
	<script type="text/javascript" src="scripts/lib/ext/plugins/summary/Summary.js"></script>
	<script type="text/javascript" src="scripts/lib/ext/plugins/grid/EmptyStoreView.js"></script>
	<script type="text/javascript" src="scripts/lib/ext/component/HTextField.js"></script>
	<script type="text/javascript" src="scripts/lib/ext/component/HTextArea.js"></script>
	<script type="text/javascript" src="scripts/lib/ext/component/HNumberField.js"></script>
	<script type="text/javascript" src="scripts/lib/ext/component/HDateField.js"></script>
	<script type="text/javascript" src="scripts/lib/ext/component/HCombo.js"></script>
	<script type="text/javascript" src="scripts/lib/ext/component/HCheckCombo.js?p=${jsParam}"></script>
	
	<link rel="stylesheet" type="text/css" href="resources/style/newestStyle.css" />
</div>
<div id="header">
</div>

<div id="center"></div>

<div id="footer" style="text-align:right;line-height:0.6">
</div>
<div id="dummyWin" class="x-hidden"></div>
</body>
</html>