<%@ include file="../header.jsp" %>
<%@ page pageEncoding="UTF-8" %>
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title">Agregar usuario a <c:out value="${project.name}"/></div>
			<div class="mainbar-details">
				<div id="form-style" class="new-project-form">
					<form:form action="../project/addUser" method="POST" commandName="projectUserForm">
						<div class="requiredFields">* Campos requeridos.</div>
						<div class="form-center-column">
							<label>Nombre:
								<span class="error-message"><form:errors path="project" /></span>
							</label>
							<form:input type="hidden" path="project"/>
							<form:input type="hidden" path="source" />
							<form:select path="target">
								<form:options items="${validUsers}" itemLabel="name" />
							</form:select>
							<div class="form-buttons">
								<input class="form-button" type="submit" value="Agregar usuario" />
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