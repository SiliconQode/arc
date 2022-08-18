CREATE DATABASE qatch;

CREATE TABLE results
(
    id              INTEGER      NOT NULL PRIMARY KEY Auto_Increment,
    submissionId    VARCHAR(128) NOT NULL,
    maintainability DOUBLE,
    reliability     DOUBLE,
    security        DOUBLE,
    efficiency      DOUBLE,
    tqi             DOUBLE,
    updated_at      DATETIME,
    created_at      DATETIME
);

CREATE TABLE projects
(
    id           INTEGER       NOT NULL PRIMARY KEY Auto_Increment,
    submissionId VARCHAR(128)  NOT NULL,
    name         VARCHAR(128)  NOT NULL,
    path         VARCHAR(2048) NOT NULL,
    resultsPath  VARCHAR(2048) NOT NULL,
    language     VARCHAR(50)   NOT NULL,
    updated_at   DATETIME,
    created_at   DATETIME
);