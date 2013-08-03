<%@ page pageEncoding="UTF-8" %>
		<div id="sidebar">
			<div id="sidebar-main">
				<c:if test="${logged == false}">
					<span class="bold">Iniciar sesión</span>
					<div id="login-form">
						<form action="login" method=POST>
							<span>Nombre: </span>
							<br />
							<input type="text" size="20" name="name">
							<br />
							<span>Contraseña: </span>
							<br />
							<input type="password" size="20" name="password"><br />
							<c:if test="${loginStatus == false}">
								<span class="bold warning">Usuario o contraseña incorrectos.</span><br />
							</c:if>
							<input id="login-submit" type="submit" value="Ingresar" />
						</form>
					</div>
				</c:if>
				<c:if test="${logged == true}">
					<span class="bold">Bienvenido, <c:out value="${fullname}"></c:out>.</span><br />
					<a class="warning" href="logout">Cerrar sesión</a><br/>
					<c:if test="${canRegister}">
						<br/>
						<a class="commonControl" href="register">Registrar usuario</a>
					</c:if>					
					<c:if test="${canInvalidateUser}">
						<br/>
						<a class="commonControl" href="invalidateUser">Invalidar usuario</a>
					</c:if>
				</c:if>	
			</div>
		</div>