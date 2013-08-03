<%@ include file="header.jsp" %>
<%@ page pageEncoding="UTF-8" %>
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title"><c:out value="${projectName}"/> &gt; <c:out value="${originalIssueName}"/>: Editar tarea</div>
			<div class="mainbar-details">
				<div id="form-style">
					<form action="editIssue" method=POST>
						<div class="requiredFields">* Campos requeridos.</div>
						<div class="form-left-column">				
							<label>Pertenece a
								<span class="form-hint">Proyecto.</span>
							</label>
							<input disabled="disabled" type="text" size="25" value="<c:out value="${projectName}"/>"/>
							<input type="hidden" name="projectName" value="<c:out value="${projectName}"/>"/>
										
							<label>Nombre *
								<span class="form-hint">Máx.: 25 caracteres.</span>
								<span class="error-message"><c:out value="${issueNameError}"/></span>
							</label>
							<input id="issueName" type="text" size="25" maxlength="25" name="issueName" value="<c:out value="${issueName}"/>"/>
							
							<label>Descripción
								<span id="form-description-countdown" class="form-hint"></span>
							</label>
							<textarea id="form-description" name="issueDescription" type="text"><c:out value="${issueDescription}"/></textarea>
						</div>
						
						<div class="form-right-column">
							<label>Código</label>
							<input disabled="disabled" type="text" size="25" value="<c:out value="${issueCode}"/>"/>
							
							<label>Creada por</label>
							<input disabled="disabled" type="text" size="25" value="<c:out value="${issueReportedUser}"/>"/>
							
							<input id="issueId" type="hidden" name="issueId" value="<c:out value="${issueId}"/>"/>
							<input id="originalIssueName" type="hidden" name="originalIssueName" value="<c:out value="${originalIssueName}"/>"/>
							<input id="issueCode" type="hidden" name="issueCode" value="<c:out value="${issueCode}"/>"/>
							<input id="issueReportedUser" type="hidden" name="issueReportedUser" value="<c:out value="${issueReportedUser}"/>"/>
							<input id="projectId" type="hidden" name="projectId" value="<c:out value="${projectId}"/>"/>
							<input id="projectName" type="hidden" name="projectName" value="<c:out value="${projectName}"/>"/>
						
							<label>Asignada a
								<span class="form-hint">Usuario encargado.</span>
								<span class="error-message"><c:out value="${issueAssignedUserError}"/></span>
							</label>
							<input id="issueAssignedUser" type="text" name="issueAssignedUser" value="<c:out value="${issueAssignedUser}"/>"/>
							
							<label>Tiempo estimado
								<span class="form-hint">Duración en horas.</span>
								<span class="error-message"><c:out value="${issueEstimatedTimeError}"/></span>
							</label>
							<input id="issueEstimatedTime" type="text" name="issueEstimatedTime" value="<c:out value="${issueEstimatedTime}"/>"/>
							
							<label>Prioridad
								<span class="form-hint"></span>
							</label>
							<select name="issuePriority">
								<c:forEach var="priority" items="${priorityStates}">
								  <option value="<c:out value="${priority.name}"/>" <c:if test="${issuePriority eq priority.name}">selected="selected"</c:if>> <c:out value="${priority}"/></option>
								</c:forEach>
							</select>
							
						    <!-- </div> -->
						   	<div class="spacer"></div>
						   	<div class="form-buttons">
								<input class="form-button" type="submit" value="Editar tarea">
							</div>
						</div>
					
						<div class="spacer"></div>
					</form>
				</div>
			</div>
		</div>
		
		<%@ include file="sidebar.jsp" %>
	
	</div>	
</div>


<%@ include file="footer.jsp" %>