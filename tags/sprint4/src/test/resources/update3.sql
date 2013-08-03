ALTER TABLE Issue ALTER description DROP NOT NULL;

-- Table: issue_followers

-- DROP TABLE issue_followers;
CREATE TABLE issue_followers
(
  issueid integer NOT NULL,
  userid integer NOT NULL,
  CONSTRAINT issue_followers_pkey PRIMARY KEY (issueid, userid),
  CONSTRAINT issue_followers_issueid_fkey FOREIGN KEY (issueid)
      REFERENCES issue (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT issue_followers_userid_fkey FOREIGN KEY (userid)
      REFERENCES systemuser (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE issue_followers OWNER TO paw;


CREATE TABLE defaultviews
(
  userid integer NOT NULL,
  "view" text NOT NULL,
  CONSTRAINT defaultviews_pkey PRIMARY KEY (userid, view),
  CONSTRAINT defaultviews_userid_fkey FOREIGN KEY (userid)
      REFERENCES systemuser (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE defaultviews OWNER TO paw;
