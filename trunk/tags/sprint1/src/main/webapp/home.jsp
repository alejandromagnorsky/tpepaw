<%@ include file="header.jsp" %>
<%@ page pageEncoding="UTF-8" %>
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title">
			<c:if test="${public == false}">Proyectos Activos</c:if>
			<c:if test="${public == true}">Proyectos Públicos</c:if>
			
			<c:if test="${canAddProject}">
				<div class="mainbar-controls">
					<a href="addProject">Crear proyecto</a>
				</div>
					</c:if>
			</div>
			
			<div id="mainbar-projects">
			
				<c:if test="${projectListSize == 0}">
					<p class="warning">No hay proyectos cargados.</p>
				</c:if>
				<c:if test="${projectListSize != 0}">
					<c:forEach var="project" items="${projectList}">
						<div class="mainbar-element-mini">
								<span class="mainbar-element-title">
									<a href="project?id=<c:out value="${project.id}"></c:out>">
									<c:out value="${project.name }"></c:out>
									</a>
								</span>
								<div class="mainbar-element-data"> <span>Código: </span> <c:out value="${project.code }"></c:out></div>
								<div class="mainbar-element-data"> <span>Líder: </span> <c:out value="${project.leader.name }"></c:out></div>
								<div class="mainbar-element-desc"> <c:out value="${project.description }"></c:out></div>
						</div>
						<div class="project-separator"></div>
					</c:forEach>
				</c:if>
			</div>
		</div>
	
		<%@ include file="sidebar.jsp" %>
		
	</div>
</div>


<%@ include file="footer.jsp" %>