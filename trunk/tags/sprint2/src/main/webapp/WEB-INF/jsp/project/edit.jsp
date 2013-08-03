<script src="<%=request.getContextPath()%>/js/utils.js" type="text/javascript"></script>
<%@ include file="../header.jsp" %>
<%@ page pageEncoding="UTF-8" %>
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title">Modificar proyecto: <c:out value="${originalProjectName}"/></div>
			<div class="mainbar-details">
				<div id="form-style">
					<form:form action="../project/edit" method="POST" commandName="projectForm">
						<div class="requiredFields">* Campos requeridos.</div>
						
						<form:input type="hidden" path="originalProject" />
							
						<div class="form-left-column">
							<label>Nombre *
								<span class="form-hint">Máx.: 25 caracteres.</span>
								<span class="error-message"><form:errors path="name" /></span>
							</label>
							<form:input type="text" size="25" maxlength="25" path="name" />
							
							<label>Descripción
								<span id="form-description-countdown" class="form-hint"></span>
							</label>
							<form:textarea id="form-description" type="text" path="description" ></form:textarea>
						</div>
						
						<div class="form-right-column">			
							<label>Código *
								<span class="form-hint">Cadena alfanumérica.</span>
								<span class="error-message"><form:errors path="code" /></span>
							</label>
							<form:input type="text" path="code" />							
	
							<label>Líder *
								<span class="form-hint">Usuario líder.</span>
								<span class="error-message"><form:errors path="leader" /></span>
							</label>							
							<form:select path="leader">
								<form:options items="${userList}" />
							</form:select>					
							
							<label>Público *
								<span class="form-hint">Visibilidad.</span>
							</label>
							<div class="visibility-radios">
								<div class="visibility-radio1"><form:radiobutton class="visibility-radio" path="isPublic" value="false" />No</div>
								<div class="visibility-radio2"><form:radiobutton class="visibility-radio" path="isPublic" value="true" />Sí</div>
							</div>
					    	<div class="spacer"></div>
							<div class="form-buttons">
								<input class="form-button" type="submit" value="Editar proyecto">							
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