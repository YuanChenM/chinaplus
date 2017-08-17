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
<meta http-equiv="X-UA-Compatible" content="edge" />

<title>CHINA PLUS</title>
<style>
    body{
        background-image: url(resources/images/bg.png);
        background-repeat: no-repeat;
        background-position: center;
        background-color: rgba(153, 225, 254, 1);
        background-size: cover
    }
    input::-ms-clear{display:none;}
    input[type="password"]::-ms-reveal{display:none;}
</style>
</head>

<body>
    <div id="initScriptLib">
        <script type="text/javascript" src="scripts/lib/ext/ext-all.js"></script>
        <script type="text/javascript" src="scripts/lib/ext/tools/include-ext.js"></script>
        <script type="text/javascript" src="scripts/lib/ext/resources/ext-locale/build/ext-locale-${language}.js"></script>
        <script type="text/javascript" src="labelResource.js?p=${jsParam}"></script>
        <script type="text/javascript" src="msgResource.js?p=${jsParam}"></script>
        <script type="text/javascript" src="scripts/lib/trf.js?p=${jsParam}"></script>
        <script type="text/javascript" src="scripts/lib/trf-cnst.js?p=${jsParam}"></script>
        <script type="text/javascript" src="scripts/chinaplus/chinaplus.js?p=${jsParam}"></script>
        <script type="text/javascript" src="scripts/chinaplus/chinaplus-cnst.js?p=${jsParam}"></script>
        <script type="text/javascript" src="scripts/login.js?p=${jsParam}"></script>
    </div>
    <div id="login-div"></div> 
</body>
</html>