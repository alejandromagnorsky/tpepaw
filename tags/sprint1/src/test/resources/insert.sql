--
-- PostgreSQL database dump
--

-- Started on 2011-04-13 16:03:39 ART

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET search_path = public, pg_catalog;

--
-- TOC entry 1809 (class 0 OID 0)
-- Dependencies: 1500
-- Name: sq2; Type: SEQUENCE SET; Schema: public; Owner: paw
--

SELECT pg_catalog.setval('sq2', 15, true);


--
-- TOC entry 1810 (class 0 OID 0)
-- Dependencies: 1502
-- Name: sq3; Type: SEQUENCE SET; Schema: public; Owner: paw
--

SELECT pg_catalog.setval('sq3', 4, true);


--
-- TOC entry 1811 (class 0 OID 0)
-- Dependencies: 1504
-- Name: sq; Type: SEQUENCE SET; Schema: public; Owner: paw
--

SELECT pg_catalog.setval('sq', 6, true);


--
-- TOC entry 1806 (class 0 OID 16651)
-- Dependencies: 1505
-- Data for Name: systemuser; Type: TABLE DATA; Schema: public; Owner: paw
--

COPY systemuser (id, name, password, isadmin, valid, fullname) FROM stdin;
1	Alejandro	a	t	t	Alejandro
2	Andres	b	t	t	Andres
3	Mariano	c	t	t	Mariano
4	user	user	f	t	Usuario
5	user2	user2	f	t	Usuario2
6	banned	banned	f	f	Banned
\.


--
-- TOC entry 1805 (class 0 OID 16641)
-- Dependencies: 1503 1806
-- Data for Name: project; Type: TABLE DATA; Schema: public; Owner: paw
--

COPY project (id, name, description, leaderid, ispublic, code) FROM stdin;
1	Half-Life	FPS de la empresa Valve.	2	f	a
2	Dragon Age	RPG con dragones.	1	t	b
4	Dead Space	FPS ambientado en el espacio exterior.	3	t	e
3	BulletStorm	FPS con disparos especiales.	4	f	bs
\.


--
-- TOC entry 1804 (class 0 OID 16632)
-- Dependencies: 1501 1806 1805 1806
-- Data for Name: issue; Type: TABLE DATA; Schema: public; Owner: paw
--

COPY issue (id, title, description, assigneduser, reporteduser, state, projectid, creationdate, completiondate, estimatedtime, priority, resolution) FROM stdin;
13	Disparos especiales	Listar los disparos especiales.	5	2	Open	3	2011-04-13 15:56:12.771	\N	2.5	Normal	\N
14	Movimientos	Programar los movimientos que pueden hacer los personajes.	2	2	Completed	3	2011-04-13 15:57:29.542	\N	9.3999996	Critical	Resolved
15	Interaccion con el mapa		2	2	Closed	3	2011-04-13 15:58:52.34	\N	-1	High	Duplicated
1	Implementar la fisica	Programar el engine que regula las leyes de la fisica en el juego.	3	3	Open	1	2011-04-13 14:48:35.38	\N	50	High	\N
3	Combate	Implementar la logica del combate	1	3	Open	2	2011-04-13 14:51:39.507	\N	60	Critical	\N
7	Opciones	Agregar el panel de opciones de configuracion.	4	3	Open	2	2011-04-13 14:55:51.054	\N	8.3999996	Low	\N
6	Introduccion	Hacer las animaciones de la introduccion.	3	3	Ongoing	2	2011-04-13 14:54:43.258	\N	15.4	Normal	\N
9	Mapas	Armar los mapas incluyendo el contenido y la forma de los mismos.	2	4	Open	1	2011-04-13 15:00:35.435	\N	8.8999996	High	\N
11	Ataques	Decidir e implementar que ataques cuerpo a cuerpo tendra el protagonista.	1	1	Open	4	2011-04-13 15:05:48.397	\N	4.5	Normal	\N
12	Implementar la fisica		4	4	Open	3	2011-04-13 15:10:07.602	\N	40	Critical	\N
8	Personajes	Crear los personajes principales.	4	4	Ongoing	1	2011-04-13 14:59:00.192	\N	25.6	Low	\N
2	Hacer los personajes		4	3	Completed	2	2011-04-13 14:50:39.584	\N	20	Normal	Resolved
10	Enemigos	Crear los enemigos incluyendo aspecto y comportamiento.	4	1	Ongoing	4	2011-04-13 15:04:40.5	\N	14.6	Normal	\N
5	Definir la historia		4	3	Closed	2	2011-04-13 14:53:26.099	\N	10.7	Low	Resolved
4	Agregar sidequests	Incorporar varias misiones opcionales.	2	3	Closed	2	2011-04-13 14:52:39.078	\N	20.5	Low	\N
\.


-- Completed on 2011-04-13 16:03:39 ART

--
-- PostgreSQL database dump complete
--

