--
-- MIT License
--
-- Empirilytics Base-Server
-- Copyright (c) 2020-2021 Empirilytics
--
-- Permission is hereby granted, free of charge, to any person obtaining a copy
-- of this software and associated documentation files (the "Software"), to deal
-- in the Software without restriction, including without limitation the rights
-- to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
-- copies of the Software, and to permit persons to whom the Software is
-- furnished to do so, subject to the following conditions:
--
-- The above copyright notice and this permission notice shall be included in all
-- copies or substantial portions of the Software.
--
-- THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
-- IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
-- FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
-- AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
-- LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
-- OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
-- SOFTWARE.
--

create table users
(
    id         INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    name       VARCHAR,
    email      VARCHAR,
    password   BINARY,
    created_at DATETIME,
    updated_at DATETIME
);

create table roles
(
    id         INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    name       VARCHAR,
    created_at DATETIME,
    updated_at DATETIME
);

create table user_roles
(
    user_id    INTEGER NOT NULL,
    role_id    INTEGER NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id)
        REFERENCES users (id),
    FOREIGN KEY (role_id)
        REFERENCES roles (id)
);

create table regions
(
    id          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    name        VARCHAR,
    city        VARCHAR,
    state       VARCHAR,
    cola        FLOAT,
    population  INTEGER,
    crime_index FLOAT,
    housing     FLOAT,
    food        FLOAT,
    created_at  DATETIME,
    updated_at  DATETIME
);

create table industries
(
    id         INTEGER,
    name       VARCHAR,
    created_at  DATETIME,
    updated_at DATETIME
);

create table company_industries
(
    company_id  INTEGER NOT NULL,
    industry_id INTEGER NOT NULL,
    created_at  DATETIME,
    updated_at  DATETIME,
    PRIMARY KEY (company_id, industry_id),
    FOREIGN KEY (company_id)
        REFERENCES companies (id),
    FOREIGN KEY (industry_id)
        REFERENCES industries (id)
);

create table companies
(
    id              INTEGER NOT NULL PRIMARY KEY Autoincrement,
    name            VARCHAR,
    site            VARCHAR,
    careers_site    VARCHAR,
    description     VARCHAR,
    headquarters_id INTEGER,
    region_id       INTEGER NOT NULL,
    created_at      DATETIME,
    updated_at      DATETIME
);

create table contacts
(
    id         INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    name       VARCHAR,
    company_id INTEGER NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (company_id)
        REFERENCES companies (id)
);

create table user_contacts
(
    user_id    INTEGER NOT NULL,
    contact_id INTEGER NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    PRIMARY KEY (user_id, contact_id),
    FOREIGN KEY (user_id)
        REFERENCES users (id),
    FOREIGN KEY (contact_id)
        REFERENCES contacts (id)
);

create table locations
(
    id            INTEGER NOT NULL PRIMARY KEY,
    company_id    INTEGER NOT NULL,
    name          VARCHAR,
    street_line_1 VARCHAR,
    street_line_2 VARCHAR,
    street_line_3 VARCHAR,
    city          VARCHAR,
    state         VARCHAR,
    zip           VARCHAR,
    created_at    DATETIME,
    updated_at    DATETIME,
    FOREIGN KEY (company_id)
        REFERENCES companies (id)
);

create table headquarters (
    id          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    name        VARCHAR,
    location_id INTEGER NOT NULL references locations (id) ,
    created_at  DATETIME,
    updated_at  DATETIME
);

create table job_postings
(
    id         INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    name       VARCHAR,
    remote     BOOLEAN,
    created_at DATETIME,
    updated_at DATETIME
);

create table company_job_postings
(
    company_id     INTEGER,
    job_posting_id INTEGER,
    created_at     DATETIME,
    updated_at     DATETIME,
    PRIMARY KEY (company_id, job_posting_id),
    FOREIGN KEY (company_id)
        REFERENCES companies (id),
    FOREIGN KEY (job_posting_id)
        REFERENCES job_postings (id)
);

create table skills
(
    id         INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    name       VARCHAR,
    created_at DATETIME,
    updated_at DATETIME
);

create table job_posting_skills
(
    job_posting_id INTEGER NOT NULL,
    skill_id       INTEGER NOT NULL,
    created_at     DATETIME,
    updated_at     DATETIME,
    PRIMARY KEY (job_posting_id, skill_id),
    FOREIGN KEY (job_posting_id)
        REFERENCES job_postings (id),
    FOREIGN KEY (skill_id)
        REFERENCES skills (id)
);
