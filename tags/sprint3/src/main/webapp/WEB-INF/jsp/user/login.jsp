<%@ include file="../header.jsp" %>
<%@ page pageEncoding="UTF-8" %>
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title">Iniciar sesión</div>
			<div class="mainbar-details">
				<div id="form-style" class="new-project-form">
					<form:form action="../user/login" method="POST" commandName="loginForm">
						<div class="requiredFields">* Campos requeridos.</div>
							<div class="form-center-column">
								<label>Nombre *</label>
								<form:input type="text" size="25" path="username" />
								
								<label>Contraseña *</label>
								<form:input type="password" size="25" path="password" />
								<span class="error-message-center bold"><form:errors path="*" /></span>
														
								<div class="form-buttons">
									<input class="form-button" type="submit" value="Iniciar sesión" />
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