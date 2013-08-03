<%@ include file="../header.jsp" %>
<%@ page pageEncoding="UTF-8" %>	
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title">
				<span class="mainbar-title-text" >Detalle de <c:out value="${project.name}"></c:out></span>
				<a class="backLink commonControl" href="../project/view"><div class="backButton" title="Volver"></div></a>
				
				<div class="mainbar-controls">
					<c:if test="${canViewVersionList}">
						<a class="commonControl" href="../project/versionList"><div class="versionListButton" title="Ver lista de versiones"></div></a>
					</c:if>
					<c:if test="${canViewWorkReport}">
						<a class="commonControl" href="../project/workReport"><div class="reportButton" title="Ver reporte del proyecto"></div></a>
					</c:if>
					<c:if test="${canAddUserToProject}">
						<a class="commonControl" href="../project/addUser"><div class="addUserButtonColor" title="Agregar un usuario al proyecto"></div></a>
					</c:if>
					<c:if test="${canRemoveUserToProject}">
						<a class="commonControl" href="../project/removeUser"><div class="removeUserButtonColor" title="Quitar usuario del proyecto"></div></a>
					</c:if>
					<c:if test="${canEditProject}"><a href="../project/edit"><div class="editButton" title="Modificar proyecto"></div></a></c:if>
					<c:if test="${canViewStatus}"><a href="../project/status"><div class="stateButton" title="Ver estado del proyecto"></div></a></c:if>
				</div>
				
				
			</div>
			<div class="mainbar-details">
				<c:if test="${project.isPublic == true}"><span class="bold">Proyecto público</span><br/></c:if>
				<span><span class="bold">Código: </span> <c:out value="${project.code}"></c:out></span><br />
				<span><span class="bold">Líder: </span> <c:out value="${project.leader.name}"></c:out></span><br />
				<span class="bold">Participantes del proyecto: </span>
				<ul>
					<c:forEach var="user" items="${project.users}">
						<li><c:out value="${user.name}"></c:out></li>
					</c:forEach>
				</ul>
				<span><span class="bold">Descripción: </span><c:out value="${project.description}"></c:out></span>
				 <br /><br />
				
				<span class="bold">Últimos 5 cambios: </span> <br />
					<div class="bold log-element">
						<span class="recent-log-span">Tarea</span>
						<span class="recent-log-span">Usuario</span>
						<span class="recent-log-span">Fecha</span>
						<span class="recent-log-span">Tipo de cambio</span>
						<span class="recent-log-span">Valor anterior</span>
						<span class="recent-log-span">Valor nuevo</span>
					<br />
					</div>
				<div class="log-div">
					<c:forEach var="log" items="${changelog}">
						<div class="log-element">
							<span class="recent-log-span"><c:out value="${log.issue.title}"/></span>
							<span class="recent-log-span"><c:out value="${log.source.name}"/></span>
							<span class="recent-log-span"> <c:out value="${log.date.dayOfMonth }"></c:out>/<c:out value="${log.date.monthOfYear}"></c:out>/<c:out value="${log.date.year }"></c:out> </span>
							<span class="recent-log-span"> <c:out value="${log.type.caption}"/></span>
							<span class="old-log-value recent-log-span"> <c:out value="${log.previous}"/></span>
							<span class="new-log-value recent-log-span"> <c:out value="${log.actual}"/></span>
						</div>
					</c:forEach>
				</div>
				
			</div>
			<div class="mainbar-title">
				Tareas activas 
				
				<div class="mainbar-controls">
					
					
					<c:if test="${hasFilter}">
						<div class="filter-menu-button-wrapper">
							<form:form action="../filter/remove" method="POST"><input class="filter-menu-button" type="submit" value="Remover filtro" /></form:form>
						</div>
					</c:if>
					
					<c:if test="${canAddIssue}">
						<a href="../issue/add"><div class="addButton" title="Agregar tarea"></div></a>
						<a class="commonControl" href="../issue/assigned"><div class="assignedButton" title="Ver tareas asignadas"></div></a>
					</c:if>
					
					<c:if test="${canViewFilterManager}">
							<a class="commonControl" href="../filter/list"><div class="filterListButton" title="Gestor de filtros" ></div></a>
					</c:if>
					
					<a class="commonControl" href="../filter/add"><div class="filterButton" title="Nuevo filtro" ></div></a>
					
				
				</div>
				
			</div>
			<div id="mainbar-projects">
				<c:if test="${issuesQuant == 0}">
					<p class="warning">No hay tareas cargadas.</p>
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
						
						
						
								<a href="../issue/view?code=<c:out value="${entry.key.code}"></c:out>"> <span class="mainbar-element-title"><c:out value="${entry.key.title }"></c:out></span> </a>
								<div class="mainbar-element-data"> <span>Código: </span> <c:out value="${entry.key.code}"></c:out></div>
								<div class="mainbar-element-data"> <span>Creado el</span> <c:out value="${entry.key.creationDate.dayOfMonth }"></c:out>/<c:out value="${entry.key.creationDate.monthOfYear}"></c:out>/<c:out value="${entry.key.creationDate.year }"></c:out>
									<span>a las </span><c:out value="${entry.key.creationDate.hourOfDay }"></c:out> hs
									<span>por</span> <c:out value="${entry.key.reportedUser.name }"></c:out>
								</div>
								
								<c:if test="${entry.value == null }">
									<div class="mainbar-element-data"><span class="bold warning-mild">No hay tiempo estimado.</span></div>
								</c:if>
								<c:if test="${entry.value != null }">
									<div class="mainbar-element-data"><span>Tiempo estimado:</span> <c:out value="${entry.value}"></c:out></div>
								</c:if>
								
								<c:if test="${entry.key.assignedUser != null }">
									<div class="mainbar-element-data"> <span>Asignado a: </span> <c:out value="${entry.key.assignedUser.name }"></c:out></div>
								</c:if>
								<c:if test="${entry.key.assignedUser == null }">
									<div class="mainbar-element-data"> <span>No hay usuario asignado.</span></div>
								</c:if>
								
								<div class="mainbar-element-status">
									<span class="bold">Estado: </span> <c:out value="${entry.key.state.caption}"></c:out><br />
									
									<c:if test="${entry.key.priority != null }">
										<span class="bold">Prioridad: </span><c:out value="${entry.key.priority.caption}"></c:out> <br />
									</c:if>
									
									<c:if test="${entry.key.type != null }">
										<span class="bold">Tipo: </span><c:out value="${entry.key.type.caption}"></c:out> <br />
									</c:if>	
									
									<c:if test="${entry.key.resolution != null }">
										<span class="bold">Resolución: </span><c:out value="${entry.key.resolution.caption}"></c:out> <br />
									</c:if>
									<c:if test="${entry.key.resolution == null }">
										<span class="bold"> </span><br />
									</c:if>									
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