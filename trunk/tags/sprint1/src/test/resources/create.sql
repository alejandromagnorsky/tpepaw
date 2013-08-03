--
-- PostgreSQL database dump
--

-- Started on 2011-04-13 16:11:39 ART

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET search_path = public, pg_catalog;

--
-- TOC entry 1500 (class 1259 OID 16754)
-- Dependencies: 6
-- Name: sq2; Type: SEQUENCE; Schema: public; Owner: paw
--

CREATE SEQUENCE sq2
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.sq2 OWNER TO paw;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 1501 (class 1259 OID 16756)
-- Dependencies: 1783 6
-- Name: issue; Type: TABLE; Schema: public; Owner: paw; Tablespace: 
--

CREATE TABLE issue (
    id integer DEFAULT nextval('sq2'::regclass) NOT NULL,
    title text NOT NULL,
    description text,
    assigneduser integer,
    reporteduser integer NOT NULL,
    state text NOT NULL,
    projectid integer NOT NULL,
    creationdate timestamp without time zone NOT NULL,
    completiondate timestamp without time zone,
    estimatedtime real,
    priority text,
    resolution text
);


ALTER TABLE public.issue OWNER TO paw;

--
-- TOC entry 1502 (class 1259 OID 16763)
-- Dependencies: 6
-- Name: sq3; Type: SEQUENCE; Schema: public; Owner: paw
--

CREATE SEQUENCE sq3
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.sq3 OWNER TO paw;

--
-- TOC entry 1503 (class 1259 OID 16765)
-- Dependencies: 1784 1785 6
-- Name: project; Type: TABLE; Schema: public; Owner: paw; Tablespace: 
--

CREATE TABLE project (
    id integer DEFAULT nextval('sq3'::regclass) NOT NULL,
    name text NOT NULL,
    description text NOT NULL,
    leaderid integer NOT NULL,
    ispublic boolean DEFAULT false NOT NULL,
    code text NOT NULL
);


ALTER TABLE public.project OWNER TO paw;

--
-- TOC entry 1504 (class 1259 OID 16773)
-- Dependencies: 6
-- Name: sq; Type: SEQUENCE; Schema: public; Owner: paw
--

CREATE SEQUENCE sq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.sq OWNER TO paw;

--
-- TOC entry 1505 (class 1259 OID 16775)
-- Dependencies: 1786 1787 1788 6
-- Name: systemuser; Type: TABLE; Schema: public; Owner: paw; Tablespace: 
--

CREATE TABLE systemuser (
    id integer DEFAULT nextval('sq'::regclass) NOT NULL,
    name text NOT NULL,
    password text NOT NULL,
    isadmin boolean DEFAULT false NOT NULL,
    valid boolean DEFAULT true NOT NULL,
    fullname text
);


ALTER TABLE public.systemuser OWNER TO paw;

--
-- TOC entry 1790 (class 2606 OID 16785)
-- Dependencies: 1501 1501
-- Name: Issue_pkey; Type: CONSTRAINT; Schema: public; Owner: paw; Tablespace: 
--

ALTER TABLE ONLY issue
    ADD CONSTRAINT "Issue_pkey" PRIMARY KEY (id);


--
-- TOC entry 1793 (class 2606 OID 16787)
-- Dependencies: 1503 1503
-- Name: project_code_key; Type: CONSTRAINT; Schema: public; Owner: paw; Tablespace: 
--

ALTER TABLE ONLY project
    ADD CONSTRAINT project_code_key UNIQUE (code);


--
-- TOC entry 1795 (class 2606 OID 16789)
-- Dependencies: 1503 1503
-- Name: project_pkey; Type: CONSTRAINT; Schema: public; Owner: paw; Tablespace: 
--

ALTER TABLE ONLY project
    ADD CONSTRAINT project_pkey PRIMARY KEY (id);


--
-- TOC entry 1797 (class 2606 OID 16791)
-- Dependencies: 1505 1505
-- Name: systemuser_name_key; Type: CONSTRAINT; Schema: public; Owner: paw; Tablespace: 
--

ALTER TABLE ONLY systemuser
    ADD CONSTRAINT systemuser_name_key UNIQUE (name);


--
-- TOC entry 1799 (class 2606 OID 16793)
-- Dependencies: 1505 1505
-- Name: systemuser_pkey; Type: CONSTRAINT; Schema: public; Owner: paw; Tablespace: 
--

ALTER TABLE ONLY systemuser
    ADD CONSTRAINT systemuser_pkey PRIMARY KEY (id);


--
-- TOC entry 1791 (class 1259 OID 16794)
-- Dependencies: 1503
-- Name: fki_; Type: INDEX; Schema: public; Owner: paw; Tablespace: 
--

CREATE INDEX fki_ ON project USING btree (leaderid);


--
-- TOC entry 1800 (class 2606 OID 16795)
-- Dependencies: 1505 1798 1501
-- Name: issue_assigneduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw
--

ALTER TABLE ONLY issue
    ADD CONSTRAINT issue_assigneduser_fkey FOREIGN KEY (assigneduser) REFERENCES systemuser(id);


--
-- TOC entry 1801 (class 2606 OID 16800)
-- Dependencies: 1503 1501 1794
-- Name: issue_projectid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw
--

ALTER TABLE ONLY issue
    ADD CONSTRAINT issue_projectid_fkey FOREIGN KEY (projectid) REFERENCES project(id) ON DELETE CASCADE;


--
-- TOC entry 1802 (class 2606 OID 16805)
-- Dependencies: 1798 1501 1505
-- Name: issue_reporteduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw
--

ALTER TABLE ONLY issue
    ADD CONSTRAINT issue_reporteduser_fkey FOREIGN KEY (reporteduser) REFERENCES systemuser(id);


--
-- TOC entry 1803 (class 2606 OID 16810)
-- Dependencies: 1505 1798 1503
-- Name: project_leaderid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: paw
--

ALTER TABLE ONLY project
    ADD CONSTRAINT project_leaderid_fkey FOREIGN KEY (leaderid) REFERENCES systemuser(id);


--
-- TOC entry 1808 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO paw;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2011-04-13 16:11:39 ART

--
-- PostgreSQL database dump complete
--

