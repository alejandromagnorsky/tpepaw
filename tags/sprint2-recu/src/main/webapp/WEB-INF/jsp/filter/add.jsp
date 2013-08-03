<script src="<%=request.getContextPath()%>/js/utils.js" type="text/javascript"></script>
<%@ include file="../header.jsp" %>
<%@ page pageEncoding="UTF-8" %>
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title"><c:out value="${projectName}"/> > Crear filtro de tareas</div>
			<div class="mainbar-details">
				<div id="form-style">
					<form:form action="../filter/add" method="POST" commandName="filterForm">
						<div class="requiredFields">* Campos requeridos.</div>
						
						<form:input type="hidden" path="user" />
						<form:input type="hidden" path="project" />
						
						<div class="form-left-column">							
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
							
					    	<div class="button-spacer"></div>
							
							<c:if test="${canAddFilter}">
								<c:forEach var="entry" items="${actionList}">
									<label><c:out value="${entry.caption}" />
										<span class="form-hint"><c:out value="${entry.description}" /></span>
									</label>
									<div class="action-radio">
										<form:radiobutton path="action" value="${entry}" />
									</div>
								</c:forEach>
							</c:if>
							
							<div class="spacer"></div>
						   	<div class="form-buttons">
						   		<input class="form-button" type="submit" value="Enviar">
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
