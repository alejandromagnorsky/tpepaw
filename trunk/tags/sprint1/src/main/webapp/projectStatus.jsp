<%@ include file="header.jsp" %>
<%@ page pageEncoding="UTF-8" %>	
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title">Estado de <c:out value="${project.name}"></c:out> <a class="backLink commonControl" href="project?id=<c:out value='${project.id}'></c:out>">(Volver)</a>
		
			<div class="mainbar-controls">
				<c:if test="${canEditProject}"><a href="editProject?id=<c:out value='${project.id}'/>">Modificar proyecto</a></c:if>
			</div>
			</div>
			<div class="mainbar-details">
				<c:if test="${project.isPublic == true}"><span class="bold">Proyecto público</span><br/></c:if>
				<span><span class="bold">Código: </span> <c:out value="${project.code}"></c:out></span><br />
				<span><span class="bold">Líder: </span> <c:out value="${project.leader.name}"></c:out></span><br />
				<span><span class="bold">Descripción: </span><c:out value="${project.description}"></c:out></span><br />
				<br />

				<table id="status-table">
					<thead>
						<tr><th scope="col">Tipo de tarea</th><th scope="col">Cantidad</th><th scope="col">Horas</th></tr>
					</thead>
					<tbody>
						<tr>
							<td><span>Abiertas</span></td>
							<td><c:out value="${stateQuant[0]}"></c:out></td>
							<td>
								<c:if test="${stateHours[0] > 0}">
									<span><c:out value="${stateHours[0]}"></c:out> hs</span>
								</c:if>
								<c:if test="${stateHours[0] <= 0}">
									<span>-</span>
								</c:if>
							</td>
						</tr>
						<tr>
							<td><span>En curso</span></td>
							<td><c:out value="${stateQuant[1]}"></c:out></td>
							<td>
								<c:if test="${stateHours[1] > 0}">
									<span><c:out value="${stateHours[1]}"></c:out> hs</span>
								</c:if>
								<c:if test="${stateHours[1] <= 0}">
									<span>-</span>
								</c:if>
							</td>
						</tr>
						<tr>
							<td><span>Finalizadas</span></td>
							<td><c:out value="${stateQuant[2]}"></c:out></td>
							<td>
								<c:if test="${stateHours[2] > 0}">
									<span><c:out value="${stateHours[2]}"></c:out> hs</span>
								</c:if>
								<c:if test="${stateHours[2] <= 0}">
									<span>-</span>
								</c:if>
							</td>
						</tr>
						<tr>
							<td><span>Cerradas</span></td>
							<td><c:out value="${stateQuant[3]}"></c:out></td>
							<td>
								<c:if test="${stateHours[3] > 0}">
									<span><c:out value="${stateHours[3]}"></c:out> hs</span>
								</c:if>
								<c:if test="${stateHours[3] <= 0}">
									<span>-</span>
								</c:if>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
				<%@ include file="sidebar.jsp" %>
	</div>
</div>


<%@ include file="footer.jsp" %>
