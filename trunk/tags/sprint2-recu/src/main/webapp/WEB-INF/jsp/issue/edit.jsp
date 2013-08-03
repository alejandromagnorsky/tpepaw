<script src="<%=request.getContextPath()%>/js/utils.js" type="text/javascript"></script>
<%@ include file="../header.jsp" %>
<%@ page pageEncoding="UTF-8" %>
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title"><c:out value="${projectName}"/> &gt; <c:out value="${originalIssueName}"/>: Editar tarea</div>
			<div class="mainbar-details">
				<div id="form-style">
					<form:form action="" method="POST" commandName="issueForm">
						<div class="requiredFields">* Campos requeridos.</div>
						
						<form:input type="hidden" path="originalIssue" />
						<form:input type="hidden" path="project" />
												
						<div class="form-left-column">				
							<label>Pertenece a
								<span class="form-hint">Proyecto.</span>
							</label>
							<input disabled="disabled" type="text" size="25" value="<c:out value="${projectName}"/>" />
									
							<label>Título *
								<span class="form-hint">Máx.: 25 caracteres.</span>
								<span class="error-message"><form:errors path="title"/></span>
							</label>
							<form:input type="text" size="25" maxlength="25" path="title" />
							
							<label>Descripción
								<span id="form-description-countdown" class="form-hint"></span>
							</label>
							<form:textarea id="form-description" path="description" type="text"></form:textarea>
						</div>
						
						<div class="form-right-column">
							<label>Código</label>
							<input disabled="disabled" type="text" size="25" value="<c:out value="${issueCode}" />" />
							
							<label>Creada por</label>
							<input disabled="disabled" type="text" size="25" value="<c:out value="${reportedUserName}" />" />
																					
							<label>Asignada a
								<span class="form-hint">Usuario encargado.</span>
								<span class="error-message"><form:errors path="assignedUser" /></span>
							</label>
							<form:select path="assignedUser">
								<form:option value=""></form:option>
								<form:options items="${userList}" />
							</form:select>
												
							<label>Tiempo estimado
								<span class="form-hint">Formato: Xd Yh Zm.</span>
								<span class="error-message"><form:errors path="estimatedTime" /></span>
							</label>
							<form:input type="text" path="estimatedTime" />
							
							<label>Prioridad
								<span class="form-hint"></span>
							</label>
							<form:select path="priority">
								<form:options items="${priorityList}" itemLabel="caption" />
							</form:select>
							
						    <!-- </div> -->
						   	<div class="spacer"></div>
						   	<div class="form-buttons">
								<input class="form-button" type="submit" value="Editar tarea">
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