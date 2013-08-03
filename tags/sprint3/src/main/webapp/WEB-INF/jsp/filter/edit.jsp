<script src="<%=request.getContextPath()%>/js/utils.js" type="text/javascript"></script>
<%@ include file="../header.jsp" %>
<%@ page pageEncoding="UTF-8" %>
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title"><c:out value="${projectName}"/> > <c:out value="${originalFilterName}"/>: Editar</div>
			<div class="mainbar-details">
				<div id="form-style">
					<form:form action="../filter/edit" method="POST" commandName="filterForm">
						<div class="requiredFields">* Campos requeridos.</div>
						
						<form:input type="hidden" path="user" />
						<form:input type="hidden" path="project" />
						<form:input type="hidden" path="originalFilter" />
						<form:input type="hidden" path="action" />
						
						<div class="form-left-column">
							<label>Nombre del filtro *
								<span class="form-hint">Máx.: 25 caracteres.</span>
								<span class="error-message"><form:errors path="name" /></span>
							</label>
							<form:input type="text" size="25" maxlength="25" path="name" />
							
							<label>Código
								<span class="form-hint">Máx.: 25 caracteres.</span>
							</label>
							<form:input type="text" size="25" maxlength="25" path="issueCode" />
							
							<label>Título
								<span class="form-hint">Máx.: 25 caracteres.</span>
							</label>
							<form:input type="text" size="25" maxlength="25" path="issueTitle" />
												
							<label>Descripción
								<span id="form-description-countdown" class="form-hint"></span>
							</label>
							<form:textarea id="form-description" type="text" path="issueDescription" ></form:textarea>
						</div>
						
						<div class="form-right-column">
							<label>Desde
								<span class="form-hint">Formato: día/mes/año</span>
								<span class="error-message"><form:errors path="dateFrom" /></span>
							</label>
							<form:input type="text" size="10" maxlength="10" path="dateFrom" />
							
							<label>Hasta
								<span class="form-hint">Formato: día/mes/año</span>
								<span class="error-message"><form:errors path="dateTo" /></span>
							</label>
							<form:input type="text" size="10" maxlength="10" path="dateTo" />
								
							<label>Creador
								<span class="form-hint">Usuario creador.</span>
							</label>							
							<form:select path="issueReportedUser">
								<form:option value=""></form:option>
								<form:options items="${userList}" />
							</form:select>
							
							<label>Asignado
								<span class="form-hint">Usuario asignado.</span>
							</label>							
							<form:select path="issueAssignedUser">
								<form:option value=""></form:option>
								<form:options items="${userList}" />
							</form:select>
							
							<label>Tipo
								<span class="form-hint">Tipo de tarea.</span>
							</label>							
							<form:select path="issueType">
							    <form:option value=""></form:option>
								<form:options items="${typeList}" itemLabel="caption" />
							</form:select>
								
							<label>Estado
								<span class="form-hint">Estado de la tarea.</span>
							</label>							
							<form:select path="issueState">
								<form:option value=""></form:option>
								<form:options items="${stateList}" itemLabel="caption" />
							</form:select>
							
							<label>Resolución
								<span class="form-hint">Estado de resolución.</span>
							</label>							
							<form:select path="issueResolution">
								<form:option value=""></form:option>
								<form:options items="${resolutionList}" itemLabel="caption" />
							</form:select>		
							
							<label>Versión afectada
								<span class="form-hint">Versión en la que una tarea no estará resuelta.</span>
							</label>							
							<form:select path="affectedVersion">
							    <form:option value=""></form:option>
								<form:options items="${versions}" itemValue="id" itemLabel="name" />
							</form:select>
							
							<div class="button-spacer"></div>
							
							<label>Versión resuelta
								<span class="form-hint">Versión en la que una tarea se estará resolviendo.</span>
							</label>							
							<form:select path="fixedVersion">
							    <form:option value=""></form:option>
								<form:options items="${versions}" itemValue="id" itemLabel="name" />
							</form:select>						
							
					    	<div class="button-spacer"></div>
							
							<div class="spacer"></div>
						   	<div class="form-buttons">
						   		<input class="form-button" type="submit" value="Editar filtro">
							</div>
							
						</div>
						
						<div class="spacer"></div>
					</form:form>
				</div>
			</div>
		</div>
		
	</div>	
</div>


<%@ include file="../footer.jsp" %>
