<%@ include file="../header.jsp" %>
<%@ page pageEncoding="UTF-8" %>
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title">Quitar colaborador de <c:out value="${issue.title}"/></div>
			<div class="mainbar-details">
				<div id="form-style" class="new-project-form">
					<form:form action="../issue/removeCollaborator" method="POST" commandName="collaboratorForm">
						<div class="requiredFields">* Campos requeridos.</div>
						<div class="form-center-column">
							<form:input type="hidden" path="issue"/>
							
							<label>Nombre *
							</label>													
							<form:select path="target">
								<form:options items="${collaborators}" itemLabel="name" />
							</form:select>
							
							<div class="form-buttons">
								<input class="form-button" type="submit" value="Quitar colaborador" />
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