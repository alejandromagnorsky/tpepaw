<%@ include file="header.jsp" %>
<%@ page pageEncoding="UTF-8" %>
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title">Crear proyecto</div>
			<div class="mainbar-details">
				<div id="form-style">
					<form action="addProject" method=POST>
						<div class="requiredFields">* Campos requeridos.</div>
						<div class="form-left-column">
							<label>Nombre *
								<span class="form-hint">Máx.: 25 caracteres.</span>
								<span class="error-message"><c:out value="${projectNameError}"/></span>
							</label>
							<input id="projectName" type="text" size="25" maxlength="25" name="projectName" value="<c:out value="${projectName}"/>"/>
												
							<label>Descripción
								<span id="form-description-countdown" class="form-hint"></span>
							</label>
							<textarea id="form-description" name="projectDescription" type="text"><c:out value="${projectDescription}"/></textarea>
						</div>
						
						<div class="form-right-column">
							<label>Código *
								<span class="form-hint">Cadena alfanumérica.</span>
								<span class="error-message"><c:out value="${projectCodeError}"/></span>
							</label>
							<input id="projectCode" type="text" name="projectCode" value="<c:out value="${projectCode}"/>"/>
							
							<label>Líder *
								<span class="form-hint">Usuario líder.</span>
								<span class="error-message"><c:out value="${projectLeaderError}"/></span>
							</label>
							<input id="projectLeader" type="text" name="projectLeader" value="<c:out value="${projectLeader}"/>"/>
							
							<label>Público
								<span class="form-hint">Visibilidad.</span>
							</label>
							<div class="visibility-radios">
								<div class="visibility-radio1"><input class="visibility-radio" type="radio" name="projectVisibility" value="notpublic" <c:if test="${projectVisibility == false}">checked</c:if>>No</input></div>
								<div class="visibility-radio2"><input class="visibility-radio" type="radio" name="projectVisibility" value="public" <c:if test="${projectVisibility == true}">checked</c:if>>Sí</input></div>
							</div>
						    <!-- </div> -->
						   	<div class="spacer"></div>
						   	<div class="form-buttons">
								<input class="form-button" type="submit" value="Crear proyecto">
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