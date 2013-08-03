<%@ include file="../header.jsp" %>
<%@ page pageEncoding="UTF-8" %>	
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title">
				<span class="mainbar-title-text" ><c:out value="${projectName}"></c:out> > <c:out value="${issue.title}"></c:out> > Archivos adjuntos</span>
				<a class="backLink commonControl" href="../issue/view?code=<c:out value="${issue.code}"></c:out>"><div class="backButton" title="Volver"></div></a>				
			</div>			
			<c:if test="${canAddIssueFile}">
				<div class="mainbar-details">				
					<div id="form-style">
						<div id="attachfileform">
							<form:form action="../issuefile/add" method="POST" commandName="issueFileForm" enctype="multipart/form-data">	
								<div id="attachfileform-title">Adjuntar nuevo archivo</div>
	
									<form:input type="hidden" path="issue" />
									<form:input type="hidden" path="uploader" />
						
									<label>Seleccionar archivo
										<span class="form-hint"> </span>
										<span class="error-message">
											<c:if test="${empty maxSizeError}">
												<form:errors path="file" />
											</c:if>
											<c:if test="${not empty maxSizeError}">
												<c:out value="${maxSizeError}" />
											</c:if>
										</span>
									</label>
									<input class="file-input" type="file" name="file" />
			
								   	<input class="form-button attachfile" type="submit" value="Adjuntar">
	
							</form:form>
						</div>
					</div>
				</div>
			
				<div class="mainbar-title">
					Archivos adjuntos
					
					<div class="mainbar-controls">			
					</div>
					
				</div>
			</c:if>
			<div id="mainbar-projects">
				<c:if test="${issue.filesQuantity == 0}">
					<p class="warning">No hay archivos adjuntos</p>
				</c:if>
				<c:if test="${issue.filesQuantity != 0}">
					<c:forEach var="file" items="${issue.files}">
						<div class="mainbar-file-mini">
						<c:if test="${canDownloadIssueFile}">
							<div class="mainbar-element-data">
								<a href="../issuefile/get?id=<c:out value="${file.id}"></c:out>"><c:out value="${file.filename}"></c:out></a><span> | <c:out value="${file.sizeInKilobytes}"></c:out><span> Kb | subido por <c:out value="${file.uploader.name}"></c:out> el <c:out value="${file.uploadDate.dayOfMonth}"></c:out>/<c:out value="${file.uploadDate.monthOfYear}"></c:out>/<c:out value="${file.uploadDate.year}"></c:out></span>
							</div>
						</c:if>
						<c:if test="${!canDownloadIssueFile}">
							<div class="mainbar-element-data">
								<c:out value="${file.filename}"></c:out><span> | <c:out value="${file.sizeInKilobytes}"></c:out><span> Kb | subido por <c:out value="${file.uploader.name}"></c:out> el <c:out value="${file.uploadDate.dayOfMonth}"></c:out>/<c:out value="${file.uploadDate.monthOfYear}"></c:out>/<c:out value="${file.uploadDate.year}"></c:out></span>
							</div>
						</c:if>
						<c:if test="${canDeleteIssueFile}">
							<div class="mainbar-file-status">
								<a href="../issuefile/delete?id=<c:out value="${file.id}"></c:out>"><div class="closeButton" title="Eliminar archivo"></div></a>
							</div>
						</c:if>
						</div>
						<div class="project-separator"></div>
					</c:forEach>
				</c:if>
			</div>
		</div>
	</div>
</div>


<%@ include file="../footer.jsp" %>