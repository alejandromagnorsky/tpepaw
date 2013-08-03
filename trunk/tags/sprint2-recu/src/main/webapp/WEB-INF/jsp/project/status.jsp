<%@ include file="../header.jsp" %>
<%@ page pageEncoding="UTF-8" %>	
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title">
				<span class="mainbar-title-text" >Estado de <c:out value="${project.name}"></c:out></span>
				<a class="backLink commonControl" href="../issue/list?code=<c:out value='${project.code}'></c:out>"><div class="backButton" title="Volver"></div></a>
				
				<div class="mainbar-controls">
					<c:if test="${canEditProject}"><a href="../project/edit"><div class="editButton" title="Modificar proyecto"></div></a></c:if>
				</div>
			</div>
			<div class="mainbar-details">
				<c:if test="${project.isPublic == true}"><span class="bold">Proyecto público</span><br/></c:if>
				<span><span class="bold">Código: </span> <c:out value="${project.code}"></c:out></span><br />
				<span><span class="bold">Líder: </span> <c:out value="${project.leader.name}"></c:out></span><br />
				<span><span class="bold">Descripción: </span><c:out value="${project.description}"></c:out></span><br />
				<br />

				<table id="status-table">
					<thead>
						<tr><th scope="col">Tipo de tarea</th><th scope="col">Cantidad</th><th scope="col">Tiempo estimado</th></tr>
					</thead>
					<tbody>
						<c:forEach var="row" items="${rows}">
							<c:out value="${row}" escapeXml="false" />
						</c:forEach>
					</tbody>
				</table>
				<br/>
			</div>
		</div>
	</div>
</div>


<%@ include file="../footer.jsp" %>
