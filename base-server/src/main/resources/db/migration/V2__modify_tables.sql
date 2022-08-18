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

alter table job_postings
    add column description VARCHAR;

alter table job_postings
    add column url VARCHAR;

drop table companies;

create table companies
(
    id              INTEGER NOT NULL PRIMARY KEY Autoincrement,
    name            VARCHAR,
    site            VARCHAR,
    careers_site    VARCHAR,
    description     VARCHAR,
    headquarters_id INTEGER,
    created_at      DATETIME,
    updated_at      DATETIME
);

drop table users;

create table users
(
    id         INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    name       VARCHAR,
    email      VARCHAR,
    password   VARCHAR,
    created_at DATETIME,
    updated_at DATETIME
);

alter table contacts
    add position VARCHAR;

create table regions_companies
(
    company_id  INTEGER NOT NULL,
    region_id   INTEGER NOT NULL,
    created_at  DATETIME,
    updated_at  DATETIME,
    PRIMARY KEY (company_id, region_id),
    FOREIGN KEY (company_id)
        REFERENCES companies (id),
    FOREIGN KEY (region_id)
        REFERENCES regions (id)
);
