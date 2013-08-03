<script src="<%=request.getContextPath()%>/js/utils.js" type="text/javascript"></script>

<%@ include file="../header.jsp" %>
<%@ page pageEncoding="UTF-8" %>
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title">
				<span class="mainbar-title-text" >Detalle de tarea</span>
				<a class="backLink commonControl" href="../issue/list?code=<c:out value="${project.code}"></c:out>"><div class="backButton" title="Volver"></div></a>
				
				<div class="mainbar-controls">
					<a href="../work/list?code=<c:out value="${issue.code}"></c:out>"><div class="stateButton" title="Ver trabajos"></div></a>
					<c:if test="${canEditIssue }"><a href="../issue/edit?code=<c:out value="${issue.code}"></c:out>"><div class="editButton" title="Modificar tarea"></div></a></c:if>
					<c:if test="${canCloseIssue }">
							<form class="controller-form"  method="post" action="../issue/mark?code=<c:out value="${issue.code}"></c:out>&operation=close" >
								<input title="Cerrar tarea" class="issue-controller closeButton" type="submit" value=" "/>
							</form>
					</c:if>
				</div>
			</div>
	
			<c:if test="${issue.state.caption eq 'Abierta'  }">
				<div class="state-open mainbar-details">
			</c:if>
			
			<c:if test="${issue.state.caption eq 'Cerrada'  }">
				<div class="state-closed mainbar-details">
			</c:if>
			
			
			<c:if test="${issue.state.caption eq 'Finalizada'  }">
				<div class="state-completed mainbar-details">
			</c:if>
			
			<c:if test="${issue.state.caption eq 'En curso'  }">
				<div class="state-ongoing mainbar-details">
			</c:if>
			
				<span><span class="bold">Título: </span> <c:out value="${issue.title}"></c:out></span><br />
				<span><span class="bold">Proyecto: </span>
					<a href="../issue/list?code=<c:out value="${project.code}"></c:out>">
					 <c:out value="${project.name}"></c:out>
					 </a>
				</span><br />
				<span><span class="bold">Código: </span> <c:out value="${issue.code}"></c:out></span><br />
				<span class="bold"> Creado el <c:out value="${issue.creationDate.dayOfMonth }"></c:out>/<c:out value="${issue.creationDate.monthOfYear}"></c:out>/<c:out value="${issue.creationDate.year }"></c:out>
					a las <c:out value="${issue.creationDate.hourOfDay }"></c:out> hs
					por <c:out value="${issue.reportedUser.name }"></c:out>
				</span> <br />
				
				<c:if test="${estimatedTime == null }">
					<span class="bold warning-mild">No hay tiempo estimado.</span><br />
				</c:if>
				<c:if test="${estimatedTime != null }">
					<span class="bold">Tiempo estimado: <c:out value="${estimatedTime}"></c:out></span><br />
				</c:if>
				
				
				<c:if test="${issue.assignedUser != null }">
					<span><span class="bold">Asignado a: </span> <c:out value="${issue.assignedUser.name}"></c:out></span><br />
				</c:if>
				<c:if test="${issue.assignedUser == null }">
					<span class="bold warning-mild">Esta tarea no tiene un usuario asignado.</span><br />
				</c:if>
				
				<c:if test="${progressPercentage >= 0 }">
					<span class="bold">Progreso: </span>
					<c:if test="${progressPercentage == 0 }">
						<span>Sin empezar</span>
					</c:if>
					<c:if test="${progressPercentage > 0 && progressPercentage < 100 }">
						<span>En proceso</span>
					</c:if>
					<c:if test="${progressPercentage == 100 }">
						<span>El tiempo estimado ha sido trabajado</span>
					</c:if>
					<c:if test="${progressPercentage > 100 }">
						<span>El tiempo trabajado ha excedido al estimado</span>
					</c:if>
					
					<div class="bar">
						<c:if test="${progressPercentage <= 100 }">
							<div class="barContent" style="width:<c:out value="${progressPercentage}" />%;">
						</c:if>
						<c:if test="${progressPercentage > 100 }">
							<div class="barContentExceeded" style="width:100%;">
						</c:if>
							<div class="barText"><c:out value="${progressPercentage}"></c:out>%</div>
						</div>			
					</div>					
				</c:if>
				
				
				<c:if test="${not empty issue.description}">
					<span><span class="bold">Descripción: </span> <c:out value="${issue.description}"></c:out></span><br />
				</c:if>
				<c:if test="${empty issue.description}">
					<span class="bold warning-mild"> Sin descripción.</span><br />
				</c:if>
				
				
				<span class="bold">Estado: </span> <c:out value="${issue.state.caption}"></c:out><br />
				<c:if test="${hasResolution }">
				<span class="bold">Resolución: </span> <c:out value="${issue.resolution.caption}"></c:out><br />
				</c:if>
				
				<c:if test="${issue.priority != null }">
				<span class="bold">Prioridad: </span> <c:out value="${issue.priority.caption}"></c:out>
				</c:if>
				

			</div>
			
			<div class="mainbar-title">
					<div class="mainbar-controls-right medium-size ">
						<c:if test="${canAssignIssue }">
							<form class="controller-form"  method="post" action="../issue/assign?code=<c:out value="${issue.code}"></c:out>" >
							<input class="issue-controller medium-size" type="submit" value="Asignarse tarea"/>
							</form>
						</c:if>
						<c:if test="${canMarkIssueAsOpen }">
							<form class="controller-form" method="post" action="../issue/mark?code=<c:out value="${issue.code}"></c:out>">
								<input value="open" type="hidden" name="operation"/>
								<input value="<c:out value="${issue.code}"></c:out>" type="hidden" name="code"/>
								<input class="issue-controller medium-size" type="submit" value="Marcar como Abierta"/>
							</form>
						</c:if>
						<c:if test="${canMarkIssueAsOngoing }">
							<form class="controller-form"  method="post" action="../issue/mark" >
								<input value="<c:out value="${issue.code}"></c:out>" type="hidden" name="code"/>
								<input value="ongoing" type="hidden" name="operation"/>
								<input class="issue-controller medium-size" type="submit" value="Marcar como En Curso"/>
							</form>
						</c:if>
						
						<c:if test="${ canResolveIssue }">
							<form id="resolution-form" class="controller-form"  method="post" action="../issue/mark" >
								<input value="resolve" type="hidden" name="operation"/>
								<input value="<c:out value="${issue.code}"></c:out>" type="hidden" name="code"/>
								<select id="resolution-select"  name="resolution" class="issue-controller medium-size">
									<option disabled=true selected="selected" >Resolver tarea...</option>
									<c:forEach var="entry" items="${resolutionStates}">
									  <option value="<c:out value="${entry}"/>"> <c:out value="${entry.caption}"/></option>
									</c:forEach>
								</select>
							</form>
						</c:if>
					</div>
			</div>		
			
			<div class="issue-comments">
				<div class="issue-comments-title">Comentarios</div>
				<c:if test="${commentListSize != 0}">
					<c:forEach var="comment" items="${commentList}">
						<div class="issue-comments-spacer"></div>
						<div class="issue-comment">
							<div class="issue-comment-description"><c:out value="${comment.description}"></c:out></div>
							<div class="issue-comment-info">
								<span class="issue-comment-user"><c:out value="${comment.user.name}"></c:out></span>
								<span class="issue-comment-date">
									<c:out value="${comment.date.dayOfMonth }"></c:out>-<c:out value="${comment.date.monthOfYear}"></c:out>-<c:out value="${comment.date.year }"></c:out>
								</span>
							</div>
						</div>
					</c:forEach>
				</c:if>
				<c:if test="${commentListSize == 0}">
					<p class="warning">No se han agregado comentarios.</p>
				</c:if>
			</div>
			
			<c:if test="${canAddComment }">
				<div class="issue-comments">
					<div class="issue-comments-title">Dejar comentario</div>
					<form:form class="issue-comment-form" action="" method="POST" commandName="commentForm" >
						
						<form:input type="hidden" path="issue" />
						
						<span id="form-description-countdown" class="form-hint"></span>
						<form:textarea id="form-description" type="text" path="description" ></form:textarea>
						<div id="issue-comment-form-bottom-bar">
							<div id="issue-comment-form-errors" class="error-message">
								<form:errors path="description" />
							</div>
							<div class="issue-comment-form-buttons">
								<input id="issue-form-button" class="form-button" type="submit" value="Enviar" />
							</div>
						</div>
					</form:form>
				</div>
			</c:if>
		</div>
	</div>
</div>

<%@ include file="../footer.jsp" %>