<%@ include file="header.jsp" %>
<%@ page pageEncoding="UTF-8" %>
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title">Detalle de Tarea <a class="backLink commonControl" href="project?id=<c:out value="${project.id}"></c:out>">(Volver)</a>
			
				<div class="mainbar-controls">
					<c:if test="${canEditIssue }"><a href="editIssue?p_id=<c:out value="${project.id}"></c:out>&i_id=<c:out value="${issue.id}"></c:out>">Modificar tarea</a></c:if>
					<c:if test="${canCloseIssue }">
							<form class="controller-form"  method="post" action="issue?id=<c:out value="${issue.id}"></c:out>&operation=close" >
							<input class="issue-controller" type="submit" value="Cerrar tarea"/>
							</form>
						</c:if>
				</div>
			</div>
	
			<c:if test="${issue.state eq 'Abierta'  }">
				<div class="state-open mainbar-details">
			</c:if>
			
			<c:if test="${issue.state eq 'Cerrada'  }">
				<div class="state-closed mainbar-details">
			</c:if>
			
			
			<c:if test="${issue.state eq 'Finalizada'  }">
				<div class="state-completed mainbar-details">
			</c:if>
			
			<c:if test="${issue.state eq 'En curso'  }">
				<div class="state-ongoing mainbar-details">
			</c:if>
			
				<span><span class="bold">Título: </span> <c:out value="${issue.title}"></c:out></span><br />
				<span><span class="bold">Proyecto: </span>
					<a href="project?id=<c:out value="${project.id}"></c:out>">
					 <c:out value="${project.name}"></c:out>
					 </a>
				</span><br />
				<span><span class="bold">Código: </span> <c:out value="${issue.code}"></c:out></span><br />
				<span class="bold"> Creado el <c:out value="${issue.creationDate.dayOfMonth }"></c:out>/<c:out value="${issue.creationDate.monthOfYear}"></c:out>/<c:out value="${issue.creationDate.year }"></c:out>
					a las <c:out value="${issue.creationDate.hourOfDay }"></c:out> hs
					por <c:out value="${issue.reportedUser.name }"></c:out>
				</span> <br />
				
				
				<c:if test="${issue.completionDate != null }">
					<span class="bold">Fecha de finalización: <c:out value="${issue.completionDate.dayOfMonth }"></c:out>/<c:out value="${issue.completionDate.monthOfYear}"></c:out>/<c:out value="${issue.completionDate.year }"></c:out>
						a las <c:out value="${issue.completionDate.hourOfDay }"></c:out> hs
					</span><br />
				</c:if>				
				<c:if test="${issue.estimatedTime <= 0 }">
					<span class="bold warning-mild">No hay tiempo estimado.</span><br />
				</c:if>
				<c:if test="${issue.estimatedTime > 0 }">
					<span class="bold">Cantidad de horas estimadas:<c:out value="${issue.estimatedTime}"></c:out> hs</span><br />
				</c:if>
				
				
				<c:if test="${issue.assignedUser != null }">
					<span><span class="bold">Asignado a: </span> <c:out value="${issue.assignedUser.name}"></c:out></span><br />
				</c:if>
				<c:if test="${issue.assignedUser == null }">
					<span class="bold warning-mild">Esta tarea no tiene un usuario asignado.</span><br />
				</c:if>
				
				
				<c:if test="${issue.description != null }">
					<span><span class="bold">Descripción: </span> <c:out value="${issue.description}"></c:out></span><br />
				</c:if>
				<c:if test="${issue.description == null }">
					<span class="bold warning-mild"> Sin descripción.</span><br />
				</c:if>
				
				
				<span class="bold">Estado: </span> <c:out value="${issue.state}"></c:out><br />
				<c:if test="${hasResolution }">
				<span class="bold">Resolución: </span> <c:out value="${issue.resolution}"></c:out><br />
				</c:if>
				
				<c:if test="${issue.priority != null }">
				<span class="bold">Prioridad: </span> <c:out value="${issue.priority}"></c:out>
				</c:if>
				

			</div>
			
			<div class="mainbar-title">
					<div class="mainbar-controls-right medium-size ">
						<c:if test="${canAssignIssue }">
							<form class="controller-form"  method="post" action="assignIssue?id=<c:out value="${issue.id}"></c:out>" >
							<input class="issue-controller medium-size" type="submit" value="Asignarse tarea"/>
							</form>
						</c:if>
						<c:if test="${canMarkIssueAsOpen }">
							<form class="controller-form" method="post" action="issue?id=<c:out value="${issue.id}"></c:out>">
								<input value="open" type="hidden" name="operation"/>
								<input value="<c:out value="${issue.id}"></c:out>" type="hidden" name="id"/>
								<input class="issue-controller medium-size" type="submit" value="Marcar como Abierta"/>
							</form>
						</c:if>
						<c:if test="${canMarkIssueAsOngoing }">
							<form class="controller-form"  method="post" action="issue" >
								<input value="<c:out value="${issue.id}"></c:out>" type="hidden" name="id"/>
								<input value="ongoing" type="hidden" name="operation"/>
								<input class="issue-controller medium-size" type="submit" value="Marcar como En Curso"/>
							</form>
						</c:if>
						
						<c:if test="${ canResolveIssue }">
							<form onchange="this.submit()" id="resolution-form" class="controller-form"  method="post" action="issue" >
							<input value="resolve" type="hidden" name="operation"/>
							<input value="<c:out value="${issue.id}"></c:out>" type="hidden" name="id"/>
							<select  name="resolution" class="issue-controller medium-size">
								<option disabled=true selected="selected" >Resolver tarea...</option>
								<c:forEach var="entry" items="${resolutionStates}">
								  <option value="<c:out value="${entry.key}"/>"> <c:out value="${entry.value}"/></option>
								</c:forEach>
							</select>
							</form>
						</c:if>
					</div>
			</div>
		</div>
		<%@ include file="sidebar.jsp" %>
	</div>
</div>


<%@ include file="footer.jsp" %>