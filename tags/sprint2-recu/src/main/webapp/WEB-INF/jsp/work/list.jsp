<%@ include file="../header.jsp" %>
<%@ page pageEncoding="UTF-8" %>
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title">
				<span class="mainbar-title-text" >Trabajos en la tarea: <c:out value="${issue.title}" />	</span>		
				<a class="backLink commonControl" href="../issue/view?code=<c:out value="${issue.code}"></c:out>"><div class="backButton" title="Volver"></div></a>
				
				<div class="mainbar-controls">
				<c:if test="${canAddWork}"><a href="../work/add?code=<c:out value="${issue.code}"></c:out>"><div class="addButton" title="Agregar trabajo"></div></a></c:if>
			</div>
			</div>
			
			<div id="mainbar-projects">
			
				<c:if test="${worksQuant == 0}">
					<p class="warning">No hay trabajos registrados.</p>
				</c:if>
				<c:if test="${worksQuant != 0}">
					<c:forEach var="entry" items="${works}">
						<div class="mainbar-work-mini">
								<div class="mainbar-element-data"> <span>Usuario: </span> <c:out value="${entry.key.user.name }"></c:out></div>
								<div class="mainbar-element-data"> <span>Fecha: </span>  <c:out value="${entry.key.date.dayOfMonth }"></c:out>/<c:out value="${entry.key.date.monthOfYear}"></c:out>/<c:out value="${entry.key.date.year }"></c:out>
								<span>a las </span><c:out value="${entry.key.date.hourOfDay }"></c:out>:<c:out value="${entry.key.date.minuteOfHour }"></c:out> hs</div>
								<div class="mainbar-element-data"><span>Tiempo dedicado: </span><c:out value="${entry.value}" /></div>
								<div class="mainbar-element-desc"><span>Descripción: </span> <c:out value="${entry.key.description }"></c:out></div>
								<c:if test="${canEditWork}">
									<div class="mainbar-work-status">
										<a href="../work/edit?id=<c:out value="${entry.key.id}"></c:out>"><div class="editButton" title="Modificar trabajo"></div></a>
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