<script src="<%=request.getContextPath()%>/js/utils.js" type="text/javascript"></script>
<%@ include file="../header.jsp" %>
<%@ page pageEncoding="UTF-8" %>
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title">Agregar versión</div>
			<div class="mainbar-details">
				<div id="form-style">
					<form:form action="../project/addVersion" method="POST" commandName="versionForm">
						<div class="requiredFields">* Campos requeridos.</div>
						<div class="form-center-column">
						
							<label>Nombre *
								<span class="form-hint">Máx.: 25 caracteres.</span>
								<span class="error-message"><form:errors path="name" /></span>
							</label>							
							<form:input type="text" size="25" maxlength="25" path="name" />

							<label>Fecha estimada *
								<span class="form-hint">Formato: día/mes/año</span>
								<span class="error-message"><form:errors path="releaseDate" /></span>
							</label>
							<form:input type="text" size="25" maxlength="25" path="releaseDate" />

							<label>Descripción
								<span id="form-description-countdown" class="form-hint"></span>
								<span class="error-message"><form:errors path="description" /></span>
							</label>
							<form:textarea id="form-description" type="text" path="description" ></form:textarea>
							
							<form:label path="state" hidden="true" value="Open"></form:label>
							
						   	<div class="form-buttons">
								<input class="form-button" type="submit" value="Agregar versión">
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