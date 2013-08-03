<%@ include file="../header.jsp" %>
<%@ page pageEncoding="UTF-8" %>
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title">Registrar usuario</div>
			<div class="mainbar-details">
				<div id="form-style" class="new-project-form">
					<form:form action="../user/register" method="POST" commandName="registerForm">
						<div class="requiredFields">* Campos requeridos.</div>
						<div class="form-center-column">
							<label>Nombre *
								<span class="form-hint">Máx.: 25 caracteres.</span>
								<span class="error-message"><form:errors path="username" /></span>
							</label>
							<form:input type="text" size="25" maxlength="25" path="username" />
							
							<label>Contraseña *
								<span class="form-hint">Máx.: 25 caracteres.</span>
								<span class="error-message"><form:errors path="password" /></span>
							</label>
							<form:input type="password" size="25" maxlength="25" path="password" />
							
							<label>Confirmar contraseña *
								<span class="error-message"><form:errors path="confirmPassword" /></span>
							</label>
							<form:input type="password" size="25" maxlength="25" path="confirmPassword" />
							
							<label>Nombre completo *
								<span class="form-hint">Máx.: 25 caracteres.</span>
								<span class="error-message"><form:errors path="fullname" /></span>
							</label>
							<form:input type="text" size="25" maxlength="25" path="fullname" />
							
							<label>Correo electrónico *
								<span class="form-hint">Formato: a@b.com</span>
								<span class="error-message"><form:errors path="email" /></span>
							</label>
							<form:input type="text" size="25" maxlength="50" path="email" />
							
							<div class="form-buttons">
								<input class="form-button" type="submit" value="Registrar usuario" />
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