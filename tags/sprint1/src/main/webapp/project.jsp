<%@ include file="header.jsp" %>
<%@ page pageEncoding="UTF-8" %>	
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title">Detalle de <c:out value="${project.name}"></c:out> <a class="backLink commonControl" href="projects">(Volver)</a>
		
			<div class="mainbar-controls">
				<c:if test="${canEditProject}"><a href="editProject?id=<c:out value='${project.id}'></c:out>">Modificar proyecto</a></c:if>
				<c:if test="${canViewProject}"><a href="projectStatus?id=<c:out value='${project.id}'></c:out>">Ver estado</a></c:if>
			</div>
			</div>
			<div class="mainbar-details">
				<c:if test="${project.isPublic == true}"><span class="bold">Proyecto público</span><br/></c:if>
				<span><span class="bold">Código: </span> <c:out value="${project.code}"></c:out></span><br />
				<span><span class="bold">Líder: </span> <c:out value="${project.leader.name}"></c:out></span><br />
				<span><span class="bold">Descripción: </span><c:out value="${project.description}"></c:out></span>
			</div>
			<div class="mainbar-title">
				Tareas activas 
				
				<c:if test="${canAddIssue}">
					<div class="mainbar-controls">
						<a href="addIssue?id=<c:out value="${project.id}"></c:out>&reported_user=<c:out value="${name}"></c:out>">Agregar tarea</a>
						<a class="commonControl" href="listAssignedIssues?id=<c:out value="${project.id}"></c:out>">Ver asignadas</a>
					</div>
				</c:if>
			</div>
			<div id="mainbar-projects">
				<c:if test="${issueListSize == 0}">
					<p class="warning">No hay tareas cargadas.</p>
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
						
						
						
								<a href="issue?id=<c:out value="${issue.id}"></c:out>"> <span class="mainbar-element-title"><c:out value="${issue.title }"></c:out></span> </a>
								<div class="mainbar-element-data"> <span>Código: </span> <c:out value="${issue.code}"></c:out></div>
								<div class="mainbar-element-data"> <span>Creado el</span> <c:out value="${issue.creationDate.dayOfMonth }"></c:out>/<c:out value="${issue.creationDate.monthOfYear}"></c:out>/<c:out value="${issue.creationDate.year }"></c:out>
									<span>a las </span><c:out value="${issue.creationDate.hourOfDay }"></c:out> hs
									<span>por</span> <c:out value="${issue.reportedUser.name }"></c:out>
								</div>
								
								<c:if test="${issue.estimatedTime <= 0 }">
									<div class="mainbar-element-data"><span class="bold warning-mild">No hay tiempo estimado.</span></div>
								</c:if>
								<c:if test="${issue.estimatedTime > 0 }">
									<div class="mainbar-element-data"><span>Cantidad de horas estimadas:</span> <c:out value="${issue.estimatedTime}"></c:out> hs</div>
								</c:if>
								
								<c:if test="${issue.assignedUser != null }">
									<div class="mainbar-element-data"> <span>Asignado a: </span> <c:out value="${issue.assignedUser.name }"></c:out></div>
								</c:if>
								<c:if test="${issue.assignedUser == null }">
									<div class="mainbar-element-data"> <span>No hay usuario asignado.</span></div>
								</c:if>
								
								<div class="mainbar-element-status">
									<span class="bold">Estado: </span> <c:out value="${issue.state}"></c:out><br />
									
									<c:if test="${issue.priority != null }">
										<span class="bold">Prioridad: </span><c:out value="${issue.priority}"></c:out> <br />
									</c:if>
										
									<c:if test="${issue.resolution != null }">
										<span class="bold">Resolución: </span><c:out value="${issue.resolution}"></c:out> <br />
									</c:if>
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