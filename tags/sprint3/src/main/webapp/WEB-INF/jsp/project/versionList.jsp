<%@ include file="../header.jsp" %>
<%@ page pageEncoding="UTF-8" %>	
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title">
				<span class="mainbar-title-text" >Versiones de <c:out value="${project.name}"></c:out></span>
				<a class="backLink commonControl" href="../issue/list?code=<c:out value='${project.code}'></c:out>"><div class="backButton" title="Volver"></div></a>
				
				<div class="mainbar-controls">
					<c:if test="${canAddVersion}">
						<a class="commonControl" href="../project/addVersion"><div class="addButton" title="Agregar versión"></div></a>
					</c:if>
					<c:if test="${canEditProject}">
						<a href="../project/edit"><div class="editButton" title="Modificar proyecto"></div></a>
					</c:if>
				</div>
			</div>
				<div class="state-open mainbar-details">
			
				<div class="mainbar-title">
					<span class="mainbar-title-text" >Versiones:</span>
				
				</div>
				<table id="version-table">
					<thead>
						<tr><th scope="col">Versión</th><th scope="col">Fecha estimada</th><th></th><th></th></tr>
					</thead>
					<tbody>						
						<c:forEach var="version" items="${versions}">
							<tr>
								<td><span><c:out value='${version.name}'></c:out></span></td>
								<td><span><c:out value="${version.releaseDate.dayOfMonth }"></c:out>/<c:out value="${version.releaseDate.monthOfYear}"></c:out>/<c:out value="${version.releaseDate.year }"></c:out></span></td>
								<td>	
									<c:if test="${canEditVersion}">
										<a class="commonControl" href="../project/editVersion?id=<c:out value="${version.id}"/>"><div class="editButton" title="Editar versión"></div></a>
									</c:if>
								</td>
								<td>
									<c:if test="${canDeleteVersion}">
										<a class="commonControl" href="../project/deleteVersion?id=<c:out value="${version.id}"/>"><div class="closeButton" title="Eliminar versión"></div></a>
									</c:if>
								</td>
							</tr>
							
						</c:forEach>
					</tbody>
				</table>
				
				<br/><br/>
				
				<div class="mainbar-title">
					<span class="mainbar-title-text" >Versiones no liberadas:</span>
				
				</div>
				<table id="versionLib-table">
					<thead>
						<tr><th scope="col">Versión</th><th scope="col">Fecha estimada</th><th></th><th></th><th>Restante para liberación</th></tr>
					</thead>
					<tbody>						
						<c:forEach var="version" items="${nonReleasedVersions}">
							<tr>
								<td><span><c:out value='${version.name}'></c:out></span></td>
								<td><span><c:out value="${version.releaseDate.dayOfMonth }"></c:out>/<c:out value="${version.releaseDate.monthOfYear}"></c:out>/<c:out value="${version.releaseDate.year }"></c:out></span></td>
								<td>	
									<c:if test="${canEditVersion}">
										<a class="commonControl" href="../project/editVersion?id=<c:out value="${version.id}"/>"><div class="editButton" title="Editar versión"></div></a>
									</c:if>
								</td>
								<td>
									<c:if test="${canDeleteVersion}">
										<a class="commonControl" href="../project/deleteVersion?id=<c:out value="${version.id}"/>"><div class="closeButton" title="Eliminar versión"></div></a>
									</c:if>
								</td>
								<td>
									<div class="bar">
										<c:if test="${version.progressPercentage <= 100 }">
											<div class="barContent" style="width:<c:out value="${version.progressPercentage}" />%;">
										</c:if>
										<c:if test="${version.progressPercentage > 100 }">
											<div class="barContentExceeded" style="width:100%;">
										</c:if>
											<div class="barText"><c:out value="${version.progressPercentage}"></c:out>%</div>
										</div>			
									</div>		
								</td>
							</tr>
							
						</c:forEach>
					</tbody>
				</table>
		</div>
	</div>
</div>


<%@ include file="../footer.jsp" %>
