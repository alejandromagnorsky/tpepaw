<%@ include file="header.jsp" %>
<%@ page pageEncoding="UTF-8" %>
	<div id="content">
		
		<div id="mainbar">
			<div class="mainbar-title">Ayuda</div>
			<div class="mainbar-details">
				<div id="helpContents">
					<span id="helpIndexTitle">Contenido</span>
					<ol>
						<li>
							Proyectos
							<ul>
								<li><a href="#addProjectHelp">Crear o editar proyecto</a></li>
								<li><a href="#projectStatusHelp">Ver estado de un proyecto</a></li>
							</ul>
						</li>
						<li>
							Tareas
							<ul>
								<li><a href="#addIssueHelp">Agregar o editar tarea</a></li>
								<li><a href="#editIssueHelp">Cambiar estado de una tarea</a></li>
							</ul>
						</li>
						<li>
							Usuarios
							<ul>
								<li><a href="#registerHelp">Registrar usuario</a></li>
								<li><a href="#invalidateUserHelp">Invalidar usuario</a></li>
							</ul>
						</li>
					</ol>
				</div>
				<br />
				<div class="helpTopicTitle">Proyectos</div>
				<div class="helpSection">				
					<div id="addProjectHelp" class="helpTitle">Crear o editar proyecto</div>
						<p>
							Sólo los administradores pueden crear o editar proyectos. <br />
							Para crear un proyecto diríjase a la página de inicio, seleccione en "Crear Proyecto"
							y complete los datos solicitados y presione "Aceptar". <br />
							Para editar un proyecto, selecciónelo en la página de inicio y elija la opción de "Modificar proyecto". 
							Luego complete los datos solicitados y presione "Aceptar".
						</p>
				</div>
				<div class="helpSection">
					<div id="projectStatusHelp" class="helpTitle">Ver estado de un proyecto</div>
						<p>
							Los líderes de los proyectos pueden consultar el estado en el que se encuentran los mismos. <br />
							Para ver el estado de un proyecto, selecciónelo en la página de inicio y elija la opción de "Ver estado".
						</p>
				</div>
				<div class="helpTopicTitle">Tareas</div>
				<div class="helpSection">				
					<div id="addIssueHelp" class="helpTitle">Agregar o editar tarea</div>
						<p>
							Los usuarios registrados pueden crear o editar tareas. <br />
							Para crear una tarea en cierto proyecto, selecciónelo en la página de inicio y elija la opción de "Agregar tarea". 
							Luego complete los datos solicitados y presione "Aceptar".
						</p>
				</div>
				<div class="helpSection">
					<div id="editIssueHelp" class="helpTitle">Cambiar estado de una tarea</div>
						<p>
							Sólo podrá modificar una tarea el usuario que la tenga asignada. <br />
							Para hacerlo, seleccióne el proyecto donde esta cargada dicha tarea. Luego haga click en el título de la tarea en cuestión. 
							Finalmente seleccione, de entre la lista de comandos, qué acción desea realizar sobre dicha tarea.
						</p>
				</div>
				<div class="helpTopicTitle">Usuarios</div>
				<div class="helpSection">				
					<div id="registerHelp" class="helpTitle">Registrar usuario</div>
						<p>
							Sólo los administradores pueden registrar a otros usuarios. <br />
							Para registrar un usuario, seleccione "Registrar usuario" del panel lateral, complete los datos solicitados y presione "Aceptar".
						</p>
				</div>
				<div class="helpSection">
					<div id="invalidateUserHelp" class="helpTitle">Invalidar usuario</div>
						<p>
							Sólo los administradores pueden registrar a otros usuarios. <br />
							Un usuario invalidado no podrá iniciar sesión. <br />
							Para invalidar un usuario, seleccione "Invalidar usuario" del panel lateral, complete los datos solicitados y presione "Aceptar".
						</p>
				</div>
			</div>
		</div>
				<%@ include file="sidebar.jsp" %>
	</div>
</div>


<%@ include file="footer.jsp" %>
