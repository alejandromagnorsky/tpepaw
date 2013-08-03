<script src="<%=request.getContextPath()%>/js/utils.js" type="text/javascript"></script>

<%@ include file="../header.jsp" %>
<%@ page pageEncoding="UTF-8" %>
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title"><c:out value="${issue.title}"/> &gt; Agregar trabajo</div>
			<div class="mainbar-details">
				<div id="form-style">
					<form:form action="../work/add" method="POST" commandName="workForm">
						<div class="requiredFields">* Campos requeridos.</div>
						<div class="form-center-column">
							<label>Tiempo dedicado *
								<span class="form-hint">Formato: Xd Yh Zm.</span>
								<span class="error-message"><form:errors path="dedicatedTime" /></span>
							</label>
							<form:input type="text" size="25" maxlength="25" path="dedicatedTime" />
							<form:input type="hidden" path="issue" />
							
							<label>Descripción *
								<span id="form-description-countdown" class="form-hint"></span>
								<span class="error-message"><form:errors path="description" /></span>
							</label>
							<form:textarea id="form-description" type="text" path="description" ></form:textarea>
						
									
							<div class="spacer"></div>
							<div class="form-buttons">
								<input class="form-button" type="submit" value="Agregar trabajo">
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