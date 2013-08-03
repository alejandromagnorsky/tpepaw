<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>trackr - Your favorite tracker</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/css/forms.css" rel="stylesheet" type="text/css">
<link rel="shortcut icon" href="<%=request.getContextPath()%>/resources/images/favicon.ico">
</head>
<body>

	<div id="bg"/>
<div id="main-container">



	<div id="header">
		<div id="topbar">
			<c:if test="${username == null}">
				<a class="commonControl" href="../user/login"><div class="loginButton" title="Ingresar"></div></a>
			</c:if>
			<c:if test="${username != null}">
				<span>Usuario: <c:out value="${fullname}" /></span>
				<form class="topbar-form" method="POST" action="../user/logout">
					<input class="topbar-controller closeButton" type="submit" title="Cerrar sesion" value=" "/>
				</form>
			</c:if>
			<a href="../user/help"><div class="helpButton" title="Ayuda"></div></a>
			<c:if test="${canInvalidateUser}">
				<a href="../user/invalidate"><div class="removeUserButton" title="Invalidar usuario"></div></a>
			</c:if>
			<c:if test="${canRegister}">
				<a  href="../user/register"><div class="addUserButton" title="Registrar nuevo usuario"></div></a>
			</c:if>	
		</div>
		<div id="header-bg"></div>
		<div id="main-header">
			<div id="logo">
				<a href="../project/view"><img src="<%=request.getContextPath()%>/resources/images/logo.png"></img></a>
			</div>
		</div>
	</div>