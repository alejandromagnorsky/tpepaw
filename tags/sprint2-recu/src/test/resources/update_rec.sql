CREATE SEQUENCE sq6;

CREATE TABLE "access"
(
   id integer NOT NULL DEFAULT nextval('sq6'::regclass), 
   date date NOT NULL, 
   issueid integer NOT NULL, 
    PRIMARY KEY (id), 
    FOREIGN KEY (issueid) REFERENCES issue (id) ON UPDATE NO ACTION ON DELETE NO ACTION
);
