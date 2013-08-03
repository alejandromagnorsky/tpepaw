<%@ include file="header.jsp" %>
<%@ page pageEncoding="UTF-8" %>
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title">Invalidar usuario</div>
			<div class="mainbar-details">
				<div id="form-style" class="new-project-form">
					<form action="invalidateUser" method="POST">
						<div class="requiredFields">* Campos requeridos.</div>
						<div class="form-center-column">
							<label>Nombre *
								<span class="form-hint">Máx.: 25 caracteres.</span>
								<span class="error-message"><c:out value="${invalidateNameError}"/></span>
							</label>
							<input type="text" size="25" maxlength="25" name="usernameInvalidate" value="<c:out value='${usernameInvalidate}' />"/>			
							<div class="form-buttons">				
								<input class="form-button" type="submit" value="Invalidar usuario" />
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