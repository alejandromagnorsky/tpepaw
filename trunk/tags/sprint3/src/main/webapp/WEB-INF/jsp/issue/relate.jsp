<%@ include file="../header.jsp" %>
<%@ page pageEncoding="UTF-8" %>	
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title">
				<span class="mainbar-title-text" ><c:out value="${projectName}"></c:out> > <c:out value="${issue.title}"></c:out> > Crear relaciones</span>
				<a class="backLink commonControl" href="../issue/view?code=<c:out value="${issue.code}"></c:out>"><div class="backButton" title="Volver"></div></a>				
			</div>			
			
			<div class="mainbar-details">
				<div id="form-style">
					<div id="newrelationform">
						<form:form action="../issue/relate" method="POST" commandName="issueRelationForm">	
							<div id="newrelationform-title">Crear relaciones para <c:out value="${issue.title}" /></div>

								<form:input type="hidden" path="issue" />
								
								<div class="form-left-column">
									<label><c:out value="${issue.title}" />
										<span class="form-hint">depende de...</span>
									</label>
									<div class="checkboxContainer">
										<form:checkboxes cssClass="checkboxItem" items="${issues}" path="dependsOn" itemValue="code" itemLabel="title"/>
									</div>
									
									<div class="spacer"></div>
									
									<label><c:out value="${issue.title}" />
										<span class="form-hint">es necesaria para...</span>
									</label>
									<div class="checkboxContainer">
										<form:checkboxes cssClass="checkboxItem" items="${issues}" path="necessaryFor" itemValue="code" itemLabel="title"/>
									</div>
								</div>
								
								<div class="form-right-column">
									<label><c:out value="${issue.title}" />
										<span class="form-hint">se relaciona con...</span>
									</label>
									<div class="checkboxContainer">
										<form:checkboxes cssClass="checkboxItem" items="${issues}" path="relatedTo" itemValue="code" itemLabel="title"/>
									</div>
								
									<div class="spacer"></div>
									
									<label><c:out value="${issue.title}" />
										<span class="form-hint">está duplicada con...</span>
									</label>
									<div class="checkboxContainer">
										<form:checkboxes cssClass="checkboxItem" items="${issues}" path="duplicatedWith" itemValue="code" itemLabel="title"/>
									</div>
							   		
									<div class="spacer"></div>
									
							   		<input class="form-button" type="submit" value="Enviar">
							   		
								</div>							
										

						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>


<%@ include file="../footer.jsp" %>