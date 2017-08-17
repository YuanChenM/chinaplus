<%--
@screen login
@author liu_yinchuan
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<jsp:useBean id="date" class="java.util.Date" scope="request"></jsp:useBean>
<c:set var="jsParam" value="${date.time}" scope="request"></c:set>
<c:set var="language" value="en" scope="request"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
     <meta http-equiv="X-UA-Compatible" content="IE=edge" />
     <link rel="stylesheet" type="text/css" href="resources/style/stylesheet.css" />
     <link rel="stylesheet" type="text/css" href="resources/style/loadingStyle.css" />
     <title>CHINA PLUS</title>
     <style>
	    input::-ms-clear{display:none;}
	    input[type="password"]::-ms-reveal{display:none;}
	</style>
</head>

<body>
    <div id="initScriptLib">
	    <script type="text/javascript" src="scripts/lib/ext/ext-all-debug.js"></script>
	    <script type="text/javascript" src="scripts/lib/ext/tools/include-ext.js"></script>
	    <script type="text/javascript" src="scripts/lib/ext/tools/options-toolbar.js"></script>
	    <script type="text/javascript" src="scripts/lib/ext/resources/ext-locale/build/ext-locale-${language}.js"></script>
	    <script type="text/javascript" src="labelResource.js?p=${jsParam}"></script>
	    <script type="text/javascript" src="msgResource.js?p=${jsParam}"></script>
	    <script type="text/javascript" src="scripts/lib/trf.js?p=${jsParam}"></script>
	    <script type="text/javascript" src="scripts/lib/trf-cnst.js?p=${jsParam}"></script>
	    <script type="text/javascript" src="scripts/lib/trf-validation.js?p=${jsParam}"></script>
	    <script type="text/javascript">
	         TRF.core.token = '${sessionScope.token}';
        </script>
	    <script type="text/javascript" src="scripts/chinaplus/chinaplus.js?p=${jsParam}"></script>
	    <script type="text/javascript" src="scripts/chinaplus/model.js"></script>
	    <script type="text/javascript" src="scripts/chinaplus/chinaplus-cnst.js?p=${jsParam}"></script>
	    <script type="text/javascript" src="scripts/chinaplus/vtypes.js"></script>
	    <script type="text/javascript" src="scripts/resetpw.js?p=${jsParam}"></script>
	<div id="reset-div"></div> 
</body>
</html>