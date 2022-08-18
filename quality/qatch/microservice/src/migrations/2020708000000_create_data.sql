create table results
(
    id              INTEGER      NOT NULL PRIMARY KEY Auto_Increment,
    submissionId    VARCHAR(128) NOT NULL,
    maintainability DOUBLE,
    reliability     DOUBLE,
    security        DOUBLE,
    efficiency      DOUBLE,
    updated_at      DATETIME,
    created_at      DATETIME
);

create table projects
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