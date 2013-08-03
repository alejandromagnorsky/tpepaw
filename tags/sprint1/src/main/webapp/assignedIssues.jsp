<%@ include file="header.jsp" %>
<%@ page pageEncoding="UTF-8" %>	
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title">
				Tareas asignadas en <c:out value="${project.name}"></c:out> 
				<a class="backLink commonControl" href="project?id=<c:out value="${project.id}"></c:out> ">(Ver todas)</a>
			</div>
			<div id="mainbar-projects">
				<c:if test="${issueListSize == 0}">
					<p class="warning">No tiene tareas asignadas.</p>
				</c:if>
				<c:if test="${issueListSize != 0}">
					<c:forEach var="issue" items="${issueList}">
					
					
						
						<c:if test="${issue.state eq 'Abierta'  }">
							<div class="state-open mainbar-element-mini">
						</c:if>
						
						<c:if test="${issue.state eq 'Cerrada'  }">
							<div class="state-closed mainbar-element-mini">
						</c:if>
						
						
						<c:if test="${issue.state eq 'Finalizada'  }">
							<div class="state-completed mainbar-element-mini">
						</c:if>
						
						<c:if test="${issue.state eq 'En curso'  }">
							<div class="state-ongoing mainbar-element-mini">
						</c:if>
								<a href="issue?id=<c:out value="${issue.id }"></c:out>"  > <span class="mainbar-element-title"><c:out value="${issue.title }"></c:out></span> </a>
								<div class="mainbar-element-data"> <span>Código: </span> <c:out value="${issue.code}"></c:out></div>
								<div class="mainbar-element-data"> <span>Creado el</span> <c:out value="${issue.creationDate.dayOfMonth }"></c:out>/<c:out value="${issue.creationDate.monthOfYear}"></c:out>/<c:out value="${issue.creationDate.year }"></c:out>
									<span>a las </span><c:out value="${issue.creationDate.hourOfDay }"></c:out> hs
									<span>por</span> <c:out value="${issue.reportedUser.name }"></c:out>
								</div>
								
								<c:if test="${issue.assignedUser != null }">
									<div class="mainbar-element-data"> <span>Asignado a: </span> <c:out value="${issue.assignedUser.name }"></c:out></div>
								</c:if>
								<c:if test="${issue.assignedUser == null }">
									<div class="mainbar-element-data"> <span>No hay usuario asignado.</span></div>
								</c:if>
								
								<c:if test="${issue.description != null }">
										<div class="mainbar-element-desc"> <c:out value="${issue.description }"></c:out></div>
								</c:if>
								<c:if test="${issue.description == null }">
									<div class="mainbar-element-data"> <span> Sin descripción. </span></div>
								</c:if>
									
								<div class="mainbar-element-status">
									<span class="bold">Estado: </span> <c:out value="${issue.state}"></c:out><br />
									<span class="bold">Prioridad: </span>  <c:out value="${issue.priority}"></c:out>
								</div>
								
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