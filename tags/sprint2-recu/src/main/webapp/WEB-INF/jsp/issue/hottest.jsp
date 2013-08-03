<%@ include file="../header.jsp" %>
<%@ page pageEncoding="UTF-8" %>	
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title">
				<span class="mainbar-title-text" ><c:out value="${projectName}"></c:out> <c:out value="${issue.title}"></c:out> > Tareas más visitadas</span>
				<a class="backLink commonControl" href="../issue/list?code=<c:out value="${project.code}"></c:out>"><div class="backButton" title="Volver"></div></a>				
			</div>			
			<div id="mainbar-projects">
				<c:if test="${accessPerIssueSize == 0}">
					<p class="warning">No se visitaron las tareas de este proyecto.</p>
				</c:if>
				<c:if test="${accessPerIssueSize != 0}">
					<c:forEach var="access" items="${accessPerIssue}">
						<div class="mainbar-issue-mini">
							<div class="mainbar-element-data">
								<span>Código: <a href="../issue/view?code=<c:out value="${access.issue.code}"></c:out>"><c:out value="${access.issue.code}"></c:out></a></span> | <span>Título: <c:out value="${access.issue.title}"></c:out></span> | <span>Cantidad de accesos: <c:out value="${access.accessQuant}"></c:out></span> 
							</div>
						</div>
						<div class="project-separator"></div>
					</c:forEach>
				</c:if>
			</div>
		</div>
	</div>
</div>


<%@ include file="../footer.jsp" %>