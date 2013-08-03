--
-- PostgreSQL database dump
--

-- Started on 2011-05-18 01:02:12 ART

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET search_path = public, pg_catalog;


COPY systemuser (name, password, isadmin, valid, fullname) FROM stdin;
Alejandro	a	t	t	Alejandro
Andres	b	t	t	Andres
Mariano	c	t	t	Mariano
user	user	f	t	Usuario
user2	user2	f	t	Usuario2
banned	banned	f	f	Banned
\.


--
-- TOC entry 1834 (class 0 OID 18047)
-- Dependencies: 1513 1835
-- Data for Name: project; Type: TABLE DATA; Schema: public; Owner: paw
--

COPY project (name, description, leaderid, ispublic, code) FROM stdin;
Half-Life	FPS de la empresa Valve.	2	f	a
Dragon Age	RPG con dragones.	1	t	b
BulletStorm	FPS con disparos especiales.	4	f	bs
Dead Space	FPS ambientado en el espacio exterior.	3	t	e
\.


--
-- TOC entry 1833 (class 0 OID 18038)
-- Dependencies: 1511 1835 1834 1835
-- Data for Name: issue; Type: TABLE DATA; Schema: public; Owner: paw
--

COPY issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution) FROM stdin;
Implementar la fisica	Programar el engine que regula las leyes de la fisica en el juego.	3	3	Open	1	2011-04-13 14:48:35.38	50	High	\N
Hacer los personajes		4	3	Completed	2	2011-04-13 14:50:39.584	20	Normal	Resolved
Combate	Implementar la logica del combate	1	3	Open	2	2011-04-13 14:51:39.507	60	Critical	\N
Agregar sidequests	Incorporar varias misiones opcionales.	2	3	Closed	2	2011-04-13 14:52:39.078	20.5	Low	\N
Definir la historia		4	3	Closed	2	2011-04-13 14:53:26.099	10.7	Low	Resolved
Introduccion	Hacer las animaciones de la introduccion.	3	3	Ongoing	2	2011-04-13 14:54:43.258	15.4	Normal	\N
Opciones	Agregar el panel de opciones de configuracion.	2	3	Ongoing	2	2011-04-13 14:55:51.054	8.3999996	Low	\N
Personajes	Crear los personajes principales.	4	4	Ongoing	1	2011-04-13 14:59:00.192	25.6	Low	\N
Mapas	Armar los mapas incluyendo el contenido y la forma de los mismos.	2	4	Open	1	2011-04-13 15:00:35.435	8.8999996	High	\N
Enemigos	Crear los enemigos incluyendo aspecto y comportamiento.	4	1	Ongoing	4	2011-04-13 15:04:40.5	14.6	Normal	\N
Ataques	Decidir e implementar que ataques cuerpo a cuerpo tendra el protagonista.	1	1	Open	4	2011-04-13 15:05:48.397	4.5	Normal	\N
Implementar la fisica		4	4	Open	3	2011-04-13 15:10:07.602	40	Critical	\N
Disparos especiales	Listar los disparos especiales.	5	2	Open	3	2011-04-13 15:56:12.771	2.5	Normal	\N
Movimientos	Programar los movimientos que pueden hacer los personajes.	2	2	Completed	3	2011-04-13 15:57:29.542	9.3999996	Critical	Resolved
Interaccion con el mapa		2	2	Closed	3	2011-04-13 15:58:52.34	-1	High	Duplicated
\.

-- Completed on 2011-05-18 01:02:12 ART

--
-- PostgreSQL database dump complete
--

