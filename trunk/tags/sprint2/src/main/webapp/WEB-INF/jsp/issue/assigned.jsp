<%@ include file="../header.jsp" %>
<%@ page pageEncoding="UTF-8" %>	
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title">
			
				<span class="mainbar-title-text" >Tareas asignadas en <c:out value="${project.name}"></c:out> </span>
				<a class="backLink commonControl" href="../issue/list?code=<c:out value="${project.code}"></c:out>"><div class="backButton" title="Ver todas las tareas"></div></a>
			</div>
			<div id="mainbar-projects">
				<c:if test="${issuesQuant == 0}">
					<p class="warning">No tiene tareas asignadas.</p>
				</c:if>
				<c:if test="${issuesQuant != 0}">
					<c:forEach var="entry" items="${issues}">
					
						<c:if test="${entry.key.state.caption eq 'Abierta'  }">
							<div class="state-open mainbar-element-mini">
						</c:if>
						
						<c:if test="${entry.key.state.caption eq 'Cerrada'  }">
							<div class="state-closed mainbar-element-mini">
						</c:if>
						
						
						<c:if test="${entry.key.state.caption eq 'Finalizada'  }">
							<div class="state-completed mainbar-element-mini">
						</c:if>
						
						<c:if test="${entry.key.state.caption eq 'En curso'  }">
							<div class="state-ongoing mainbar-element-mini">
						</c:if>
								<a href="../issue/view?code=<c:out value="${entry.key.code}"></c:out>"  > <span class="mainbar-element-title"><c:out value="${entry.key.title }"></c:out></span> </a>
								<div class="mainbar-element-data"> <span>Código: </span> <c:out value="${entry.key.code}"></c:out></div>
								<div class="mainbar-element-data"> <span>Creado el</span> <c:out value="${entry.key.creationDate.dayOfMonth }"></c:out>/<c:out value="${entry.key.creationDate.monthOfYear}"></c:out>/<c:out value="${entry.key.creationDate.year }"></c:out>
									<span>a las </span><c:out value="${entry.key.creationDate.hourOfDay }"></c:out> hs
									<span>por</span> <c:out value="${entry.key.reportedUser.name }"></c:out>
								</div>
								
								<c:if test="${entry.key.assignedUser != null }">
									<div class="mainbar-element-data"> <span>Asignado a: </span> <c:out value="${entry.key.assignedUser.name }"></c:out></div>
								</c:if>
								<c:if test="${entry.key.assignedUser == null }">
									<div class="mainbar-element-data"> <span>No hay usuario asignado.</span></div>
								</c:if>
								<c:if test="${entry.value == null }">
									<span class="mainbar-element-data">No hay tiempo estimado.</span><br />
								</c:if>
								<c:if test="${entry.value != null }">
									<span class="mainbar-element-data"><span>Tiempo estimado: </span><c:out value="${entry.value}"></c:out></span><br />
								</c:if>
									
								<div class="mainbar-element-status">
									<span class="bold">Estado: </span> <c:out value="${entry.key.state.caption}"></c:out><br />
									<span class="bold">Prioridad: </span>  <c:out value="${entry.key.priority.caption}"></c:out>
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