<%@ include file="../header.jsp" %>
<%@ page pageEncoding="UTF-8" %>	
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title">
				<span class="mainbar-title-text" >Reporte de trabajo de <c:out value="${project.name}"></c:out></span>
				<a class="backLink commonControl" href="../issue/list?code=<c:out value='${project.code}'></c:out>"><div class="backButton" title="Volver"></div></a>
				
				<div class="mainbar-controls">
					<c:if test="${canEditProject}"><a href="../project/edit"><div class="editButton" title="Modificar proyecto"></div></a></c:if>
				</div>
			</div>
			<div class="mainbar-details">
				<div id="form-style" class="new-project-form">
					<form:form action="../project/workReport" method="POST" commandName="workReportForm">
						<span class="bold">Seleccione un rango de fechas: <br />
						<div class="requiredFields">* Campos requeridos.</div>
							<div class="form-center-column">
								<form:input type="hidden" path="source" />
								<form:input type="hidden" path="project"/>
								
								<label>Desde *
									<span class="form-hint">Formato: día/mes/año</span>
								</label>
								<form:input type="text" size="10" maxlength="10" path="from" /> <br />
								
								<label>Hasta *
									<span class="form-hint">Formato: día/mes/año</span>
								</label>
								<form:input type="text" size="10" maxlength="10" path="to" />
								
								<br/>
								<span class="error-message-center bold"><form:errors path="project" /></span>
								
								<div class="form-buttons">							
									<input class="form-button" type="submit" value="Ver reporte" />
								</div>
								<div class="spacer"></div>
							</div>
					</form:form>
				</div>
			</div>
			
			<c:if test="${userSet != null}">
				<div class="state-open mainbar-details">
			
				<div class="mainbar-title">
					<span class="mainbar-title-text" >Resultado:</span>
				
				</div>
				<table id="status-table">
					<thead>
						<tr><th scope="col">Usuario</th><th scope="col">Tiempo trabajado</th></tr>
					</thead>
					<tbody>						
						<c:forEach var="entry" items="${userSet}">
							<tr>
									<td><span><c:out value='${entry.key.name}'></c:out></span></td>
									<td><c:out value='${entry.value}'></c:out></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:if>
		</div>
	</div>
</div>


<%@ include file="../footer.jsp" %>
