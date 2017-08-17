<%--
@screen login
@author liu_yinchuan
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<title>CHINA PLUS</title>
<meta http-equiv="X-UA-Compatible" content="IE=8" />
</head>
<body>

</body>
</html>
<script language="javascript" type="text/javascript">
/**
function getRequestPath(url){
    var pathName = document.location.href;
    var index = pathName.substr(1).lastIndexOf("/");
    var result = pathName.substr(0, index + 1);
    result=result+"/"+url;

    return result;
};
window.location.href=getRequestPath("login");
*/
window.location.href="${ctx}/login";
</script>
