<%@ include file="../header.jsp" %>
<%@ page pageEncoding="UTF-8" %>
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title">
				<span class="mainbar-title-text" >Gestor de filtros personales para  <c:out value="${project.name}" />	</span>		
				<a class="backLink commonControl" href="../issue/list?code=<c:out value="${project.code}"></c:out>"><div class="backButton" title="Volver"></div></a>
				
				<div class="mainbar-controls">
				<c:if test="${canAddFilter}">
					<a href="../filter/add"><div class="addButton" title="Nuevo filtro"></div></a>
				</c:if>
			</div>
			</div>
			
			<div id="mainbar-projects">
			
				<c:if test="${filtersQuant == 0}">
					<p class="warning">No hay filtros registrados para este proyecto.</p>
				</c:if>
				<c:if test="${filtersQuant != 0}">
				
					<c:forEach var="entry" items="${filters}">
						<div class="mainbar-filter-mini">
							<div class="mainbar-element-data"><c:out value="${entry.name}"></c:out></div>
							<div class="mainbar-element-data"><span>Por código: </span><c:out value="${entry.issueCode}"></c:out></div>
							<div class="mainbar-element-data"><span>Por título: </span><c:out value="${entry.issueTitle}" /></div>
							<div class="mainbar-element-data"><span>Por descripción: </span> <c:out value="${entry.issueDescription}"></c:out></div>
							<div class="mainbar-element-data"><span>Por creador: </span> <c:out value="${entry.issueReportedUser.name}"></c:out></div>
							<div class="mainbar-element-data"><span>Por usuario asignado: </span> <c:out value="${entry.issueAssignedUser.name}"></c:out></div>
							<div class="mainbar-element-data"><span>Por tipo: </span> <c:out value="${entry.issueType.caption}"></c:out></div>
							<div class="mainbar-element-data"><span>Por estado: </span> <c:out value="${entry.issueState.caption}"></c:out></div>
							<div class="mainbar-element-data"><span>Por resolución: </span> <c:out value="${entry.issueResolution.caption}"></c:out></div>
							<div class="mainbar-element-data"> <span>Por fecha de creación desde: </span>  <c:out value="${entry.dateFrom.dayOfMonth }"></c:out>/<c:out value="${entry.dateFrom.monthOfYear}"></c:out>/<c:out value="${entry.dateFrom.year }"></c:out>
								<span>hasta: </span><c:out value="${entry.dateTo.dayOfMonth }"></c:out>/<c:out value="${entry.dateTo.monthOfYear}"></c:out>/<c:out value="${entry.dateTo.year }"></c:out></div>
							
							<div class="mainbar-filter-status">
								<a href="../filter/apply?id=<c:out value="${entry.id}"></c:out>"><div class="stateButton" title="Aplicar filtro"></div></a>
							</div>
							
							<c:if test="${canEditFilter}">
								<div class="mainbar-filter-status">
									<a href="../filter/edit?id=<c:out value="${entry.id}"></c:out>"><div class="editButton" title="Editar filtro"></div></a>
								</div>
							</c:if>
							<c:if test="${canDeleteFilter}">
								<div class="mainbar-filter-status">
									<a href="../filter/delete?id=<c:out value="${entry.id}"></c:out>"><div class="closeButton" title="Eliminar filtro"></div></a>
								</div>
							</c:if>
							
						</div>
						<div class="project-separator"></div>
					</c:forEach>
					
				</c:if>
			</div>
		</div>
		
	</div>
</div>

<%@ include file="../footer.jsp" %>
