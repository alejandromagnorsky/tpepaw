CREATE sequence sq6;
CREATE sequence sq7;
CREATE sequence sq8;
CREATE sequence sq9;
CREATE sequence sq10;

CREATE TABLE "version"
(
  id integer NOT NULL DEFAULT nextval('sq6'::regclass),
  description text,
  "name" text NOT NULL,
  projectid integer NOT NULL,
  "release" timestamp without time zone NOT NULL,
  state text NOT NULL,
  CONSTRAINT version_pkey PRIMARY KEY (id),
  CONSTRAINT version_projectid_fkey FOREIGN KEY (projectid)
      REFERENCES project (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);


CREATE TABLE vote
(
   userid integer NOT NULL, 
   issueid integer NOT NULL, 
    PRIMARY KEY (userid, issueid), 
    FOREIGN KEY (userid) REFERENCES systemuser (id) ON UPDATE NO ACTION ON DELETE NO ACTION, 
    FOREIGN KEY (issueid) REFERENCES issue (id) ON UPDATE NO ACTION ON DELETE NO ACTION
);


CREATE TABLE affected_versions
(
  issueid integer NOT NULL,
  versionid integer NOT NULL,
  CONSTRAINT affected_versions_pkey PRIMARY KEY (issueid, versionid),
  CONSTRAINT affected_versions_issueid_fkey FOREIGN KEY (issueid)
      REFERENCES issue (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT affected_versions_versionid_fkey FOREIGN KEY (versionid)
      REFERENCES "version" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE fixed_versions
(
  issueid integer NOT NULL,
  versionid integer NOT NULL,
  CONSTRAINT fixed_version_pkey PRIMARY KEY (issueid, versionid),
  CONSTRAINT fixed_version_issueid_fkey FOREIGN KEY (issueid)
      REFERENCES issue (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fixed_version_versionid_fkey FOREIGN KEY (versionid)
      REFERENCES "version" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE collaborator
(
   issueid integer NOT NULL, 
   userid integer NOT NULL, 
    PRIMARY KEY (issueid, userid), 
    FOREIGN KEY (issueid) REFERENCES issue (id) ON UPDATE NO ACTION ON DELETE NO ACTION, 
    FOREIGN KEY (userid) REFERENCES systemuser (id) ON UPDATE NO ACTION ON DELETE NO ACTION
);


ALTER TABLE systemuser ADD COLUMN email text;
UPDATE systemuser SET email = 'example@domain.com';
ALTER TABLE systemuser ALTER COLUMN email SET NOT NULL;

CREATE TABLE filter
(
  id integer NOT NULL DEFAULT nextval('sq7'::regclass),
  "name" text NOT NULL,
  userid integer NOT NULL,
  projectid integer NOT NULL,
  issuecode text,
  issuetitle text,
  issuedescription text,
  issuereporteduserid integer,
  issueassigneduserid integer,
  issuestate text,
  issueresolution text,
  datefrom timestamp without time zone,
  dateto timestamp without time zone,
  CONSTRAINT id_pk PRIMARY KEY (id),
  CONSTRAINT filter_assigneduser_fkey FOREIGN KEY (issueassigneduserid)
      REFERENCES systemuser (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT filter_projectid_fkey FOREIGN KEY (projectid)
      REFERENCES project (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT filter_reporteduser_fkey FOREIGN KEY (issuereporteduserid)
      REFERENCES systemuser (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT filter_userid_fkey FOREIGN KEY (userid)
      REFERENCES systemuser (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT filter_unique UNIQUE (name, userid, projectid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE filter OWNER TO paw;


ALTER TABLE filter ADD COLUMN issuetype text;
ALTER TABLE filter ADD COLUMN affectedversion integer;
ALTER TABLE filter ADD COLUMN fixedversion integer;
ALTER TABLE filter ADD CONSTRAINT affectedversion_fk FOREIGN KEY (affectedversion) REFERENCES version (id);
ALTER TABLE filter ADD CONSTRAINT fixedversion_fk FOREIGN KEY (fixedversion) REFERENCES version (id);


CREATE TABLE file
(
  id integer NOT NULL DEFAULT nextval('sq8'::regclass),
  issueid integer NOT NULL,
  file oid NOT NULL,
  filename text NOT NULL,
  size bigint NOT NULL,
  uploaddate timestamp without time zone NOT NULL,
  uploader integer NOT NULL,
  CONSTRAINT id_pkey PRIMARY KEY (id),
  CONSTRAINT fk_uploader FOREIGN KEY (uploader)
      REFERENCES systemuser (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT issueid_fkey FOREIGN KEY (issueid)
      REFERENCES issue (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE file OWNER TO paw;



CREATE TABLE issue_log
(
  id integer NOT NULL DEFAULT nextval('sq9'::regclass),
  issueid integer NOT NULL,
  userid integer NOT NULL,
  date timestamp without time zone NOT NULL,
  previous text,
  actual text,
  "type" text NOT NULL,
  CONSTRAINT issue_log_pkey PRIMARY KEY (id),
  CONSTRAINT issue_log_issueid_fkey FOREIGN KEY (issueid)
      REFERENCES issue (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT issue_log_userid_fkey FOREIGN KEY (userid)
      REFERENCES systemuser (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE issue ADD COLUMN type text;
UPDATE issue SET type = 'Issue';
ALTER TABLE issue ALTER COLUMN type SET NOT NULL;


CREATE TABLE issuerelation
(
  id integer NOT NULL DEFAULT nextval('sq10'::regclass),
  type text NOT NULL,
  issuea integer NOT NULL,
  issueb integer NOT NULL,
  CONSTRAINT issuerelation_id_pkey PRIMARY KEY (id),
  CONSTRAINT issuerelation_issuea_fkey FOREIGN KEY (issuea)
      REFERENCES issue (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT issuerelation_issueb_fkey FOREIGN KEY (issueb)
      REFERENCES issue (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT issuerelation_unique UNIQUE (type, issuea, issueb)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE issuerelation OWNER TO paw;
