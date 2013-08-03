<%@ include file="header.jsp" %>
<%@ page pageEncoding="UTF-8" %>
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title">Registrar usuario</div>
			<div class="mainbar-details">
				<div id="form-style" class="new-project-form">
					<form action="register" method="POST">
						<div class="requiredFields">* Campos requeridos.</div>
						<div class="form-center-column">
							<label>Nombre *
								<span class="form-hint">Máx.: 25 caracteres.</span>
								<span class="error-message"><c:out value="${registerNameError}"/></span>
							</label>
							<input type="text" size="25" maxlength="25" name="usernameRegister" value="<c:out value='${usernameRegister}' />"/>
							
							<label>Contraseña *
								<span class="form-hint">Máx.: 25 caracteres.</span>
								<span class="error-message"><c:out value="${registerPasswordError}"/></span>
							</label>
							<input type="password" size="25" maxlength="25" name="passwordRegister" value="<c:out value='${passwordRegister}' />"/>
							
							<label>Nombre completo *
								<span class="form-hint">Máx.: 25 caracteres.</span>
								<span class="error-message"><c:out value="${registerFullnameError}"/></span>
							</label>
							<input type="text" size="25" maxlength="25" name="fullnameRegister" value="<c:out value='${fullnameRegister}' />"/>
							<div class="form-buttons">
								<input class="form-button" type="submit" value="Registrar usuario" />
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