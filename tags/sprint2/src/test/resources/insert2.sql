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
member	member	f	t	Miembro
\.


COPY project (name, description, leaderid, ispublic, code) FROM stdin;
Mass Effect	RPG espacial	2	f	me
\.

COPY issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution) FROM stdin;
Crear historia		2	2	Open	5	2011-05-09 21:06:52.201	\N	Low	\N
Combate a distancia	Armas capaces de apuntar.	1	2	Ongoing	5	2011-05-09 21:07:42.799	5120	High	\N
Crear personajes		3	2	Open	5	2011-05-09 21:08:03.563	2640	Normal	\N
Implementar dialogos		1	2	Completed	5	2011-05-09 21:08:29.568	3600	High	Resolved
\.


--
-- TOC entry 1836 (class 0 OID 18113)
-- Dependencies: 1517 1835 1833
-- Data for Name: comment; Type: TABLE DATA; Schema: public; Owner: paw
--

COPY comment (userid, date, description, issueid) FROM stdin;
1	2011-05-09 21:11:05.906	Ya estan los esquemas. Falta agregarles detalles.	9
7	2011-05-09 21:13:42.893	Muy buenos.	18
2	2011-05-09 21:16:16.213	Podria tener partes de Star Wars.	16
1	2011-05-09 21:16:56.489	Si, por ejemplo podemos poner algunas frases de la saga.	16
2	2011-05-14 03:34:34.817	Hola.	7
2	2011-05-18 00:55:49.919	Ataques con espada.	11
\.


--
-- TOC entry 1838 (class 0 OID 18119)
-- Dependencies: 1519 1834 1835
-- Data for Name: projectusers; Type: TABLE DATA; Schema: public; Owner: paw
--

COPY projectusers (projectid, userid) FROM stdin;
5	1
5	7
5	2
5	3
1	2
1	4
\.


--
-- TOC entry 1837 (class 0 OID 18116)
-- Dependencies: 1518 1835 1833
-- Data for Name: work; Type: TABLE DATA; Schema: public; Owner: paw
--

COPY work (dedicatedtime, userid, date, description, issueid) FROM stdin;
480	2	2011-05-09 21:09:08.284	Ya estan las pistolas laser.	17
960	1	2011-05-09 21:09:46.216	Agregadas las granadas.	17
960	1	2011-05-09 21:10:26.732	Realizados los esquemas principales.\r\n	9
360	2	2011-05-09 21:12:39.579	Ya estan las ropas.	18
300	7	2011-05-09 21:13:31.099	Personalidades agregadas.	18
120	7	2011-05-09 21:14:20.455	Se puede seleccionar una opcion de la lista.	19
480	7	2011-05-09 21:14:40.741	Agregados los iconos.	19
3000	2	2011-05-09 21:15:41.492	Ya estan todos los archivos con los dialogos.	19
480	2	2011-05-14 03:35:03.065	Menu de opciones graficas agregado.	7
120	2	2011-05-18 00:56:40.505	Tambien con armas de fuego.	11
1920	2	2011-05-18 00:56:47.199	Agregados ataques con espada.	11
\.

