<%@ include file="header.jsp" %>
<%@ page pageEncoding="UTF-8" %>
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title"><span class="error-title">¡Rompiste trackr! Para retornar, click <span><a class="bold" href="<%=request.getContextPath()%>/bin/project/view">aquí</a></span></span></div>
			<div id="mainbar-details"><div class="error-message"><c:out value="${errormsg}"/></div></div>
		</div>
	
	</div>
</div>


<%@ include file="footer.jsp" %>