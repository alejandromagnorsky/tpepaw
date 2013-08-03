UPDATE issue SET estimatedtime = (estimatedtime*60) WHERE estimatedtime <> -1;
ALTER TABLE issue ALTER estimatedtime TYPE integer;

CREATE SEQUENCE sq4
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE issuemessage (
    id integer DEFAULT nextval('sq4'::regclass) NOT NULL,
    userid integer NOT NULL,
    date timestamp without time zone NOT NULL,
    description text NOT NULL,
    issueid integer NOT NULL
);


ALTER TABLE public.issuemessage OWNER TO paw;


CREATE TABLE comment (
    messageid integer NOT NULL
);


ALTER TABLE public.comment OWNER TO paw;


CREATE TABLE work (
    messageid integer NOT NULL,
    dedicatedtime integer NOT NULL
);

ALTER TABLE public.work OWNER TO paw;


CREATE TABLE projectusers (
    projectid integer NOT NULL,
    userid integer NOT NULL
);

ALTER TABLE public.projectusers OWNER TO paw;

ALTER TABLE ONLY comment
    ADD CONSTRAINT comment_pkey PRIMARY KEY (messageid);

ALTER TABLE ONLY issuemessage
    ADD CONSTRAINT message_pkey PRIMARY KEY (id);

ALTER TABLE ONLY projectusers
    ADD CONSTRAINT projectusers_pkey PRIMARY KEY (projectid, userid);

ALTER TABLE ONLY work
    ADD CONSTRAINT work_pkey PRIMARY KEY (messageid);

ALTER TABLE ONLY comment
    ADD CONSTRAINT comment_messageid_fkey FOREIGN KEY (messageid) REFERENCES issuemessage(id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY issuemessage
    ADD CONSTRAINT message_issueid_fkey FOREIGN KEY (issueid) REFERENCES issue(id);

ALTER TABLE ONLY issuemessage
    ADD CONSTRAINT message_userid_fkey FOREIGN KEY (userid) REFERENCES systemuser(id);

ALTER TABLE ONLY projectusers
    ADD CONSTRAINT projectid FOREIGN KEY (projectid) REFERENCES project(id);

ALTER TABLE ONLY projectusers
    ADD CONSTRAINT userid FOREIGN KEY (userid) REFERENCES systemuser(id);

ALTER TABLE ONLY work
    ADD CONSTRAINT work_messageid_fkey FOREIGN KEY (messageid) REFERENCES issuemessage(id) ON UPDATE CASCADE ON DELETE CASCADE;
    
GRANT ALL ON SCHEMA public TO paw;
GRANT ALL ON SCHEMA public TO PUBLIC;







ALTER TABLE systemuser ALTER COLUMN fullname SET NOT NULL;

ALTER TABLE project 
	ALTER COLUMN description DROP NOT NULL;

ALTER TABLE "work" RENAME messageid  TO id;

ALTER TABLE "comment" RENAME messageid  TO id;

ALTER TABLE issue DROP COLUMN completiondate;

ALTER TABLE issue ALTER COLUMN priority SET NOT NULL;

UPDATE issue SET estimatedtime = null WHERE estimatedtime = -1;


ALTER TABLE "work" ADD COLUMN userid integer;
ALTER TABLE "work" ADD COLUMN date timestamp without time zone;
ALTER TABLE "work" ADD COLUMN description text;
ALTER TABLE "work" ADD COLUMN issueid integer;

UPDATE work
SET userid = (SELECT userid FROM issuemessage WHERE issuemessage.id = work.id),
date = (SELECT date FROM issuemessage WHERE issuemessage.id = work.id),
description = (SELECT description FROM issuemessage WHERE issuemessage.id = work.id),
issueid = (SELECT issueid FROM issuemessage WHERE issuemessage.id = work.id);

ALTER TABLE "work" DROP CONSTRAINT work_messageid_fkey;
ALTER TABLE "work" ADD FOREIGN KEY (userid) REFERENCES systemuser (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "work" ADD FOREIGN KEY (issueid) REFERENCES issue (id) ON UPDATE NO ACTION ON DELETE NO ACTION;


ALTER TABLE "comment" ADD COLUMN userid integer;
ALTER TABLE "comment" ADD COLUMN date timestamp without time zone;
ALTER TABLE "comment" ADD COLUMN description text;
ALTER TABLE "comment" ADD COLUMN issueid integer;

UPDATE comment
SET userid = (SELECT userid FROM issuemessage WHERE issuemessage.id = comment.id),
date = (SELECT date FROM issuemessage WHERE issuemessage.id = comment.id),
description = (SELECT description FROM issuemessage WHERE issuemessage.id = comment.id),
issueid = (SELECT issueid FROM issuemessage WHERE issuemessage.id = comment.id);

ALTER TABLE "comment" DROP CONSTRAINT comment_messageid_fkey;
ALTER TABLE "comment" ADD FOREIGN KEY (userid) REFERENCES systemuser (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "comment" ADD FOREIGN KEY (issueid) REFERENCES issue (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

DROP TABLE issuemessage;

ALTER TABLE "work"
   ALTER COLUMN id SET DEFAULT  nextval('sq4'::regclass);

CREATE SEQUENCE sq5;

SELECT setval('sq5', (SELECT MAX(id) FROM comment));

ALTER TABLE "comment"
   ALTER COLUMN id SET DEFAULT  nextval('sq5'::regclass);
