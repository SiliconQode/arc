--
-- The MIT License (MIT)
--
-- Empirilytics Arc DataModel
-- Copyright (c) 2015-2021 Empirilytics
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

create table systems
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR,
    basePath   VARCHAR,
    sysKey     VARCHAR,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT systems_unique UNIQUE (sysKey)
);

create table pattern_repositories
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    repoKey    VARCHAR,
    name       VARCHAR,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT pattern_repositories_unique UNIQUE (repoKey)
);

create table patterns
(
    id                    INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    patternKey            VARCHAR,
    name                  VARCHAR,
    family                VARCHAR,
    pattern_repository_id INTEGER REFERENCES pattern_repositories (id),
    created_at            DATETIME,
    updated_at            DATETIME,
    CONSTRAINT patterns_unique UNIQUE (patternKey)
);

create table roles
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    roleKey    VARCHAR,
    name       VARCHAR,
    type       INTEGER,
    mandatory  BOOLEAN,
    pattern_id INTEGER REFERENCES patterns (id),
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT roles_unique UNIQUE (roleKey)
);

create table roles_role_bindings
(
    id              INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    role_id         INTEGER REFERENCES roles (id),
    role_binding_id INTEGER REFERENCES role_bindings (id),
    created_at      DATETIME,
    updated_at      DATETIME
);

create table relations
(
    id           INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    relKey       VARCHAR,
    project_id   INTEGER,
    reference_id INTEGER,
    to_id        INTEGER,
    from_id      INTEGER,
    type         INTEGER,
    created_at   DATETIME,
    updated_at   DATETIME
);

create table pattern_chains
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    chainKey   VARCHAR,
    system_id  INTEGER REFERENCES systems (id),
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT pattern_chains_unique UNIQUE (chainKey)
);

create table pattern_instances
(
    id               INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    instKey          VARCHAR,
    pattern_size     INTEGER,
    pattern_chain_id INTEGER REFERENCES pattern_chains (id),
    project_id       INTEGER REFERENCES projects (id),
    pattern_id       INTEGER REFERENCES patterns (id),
    created_at       DATETIME,
    updated_at       DATETIME,
    CONSTRAINT pattern_instances_unique UNIQUE (instKey)
);

create table role_bindings
(
    id                  INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    pattern_instance_id INTEGER REFERENCES pattern_instances (id),
    created_at          DATETIME,
    updated_at          DATETIME
);

create table refs
(
    id          INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    refKey      VARCHAR,
    type        INTEGER,
    parent_id   INTEGER,
    parent_type VARCHAR,
    created_at  DATETIME,
    updated_at  DATETIME
);

create table findings
(
    id           INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    findingKey   VARCHAR,
    injected     NUMERIC,
    start        INTEGER,
    end          INTEGER,
    created_at   DATETIME,
    updated_at   DATETIME
);

create table projects_findings
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    project_id INTEGER REFERENCES projects (id),
    finding_id INTEGER REFERENCES findings (id),
    created_at DATETIME,
    updated_at DATETIME
);

create table rules_findings
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    rule_id    INTEGER REFERENCES rules (id),
    finding_id INTEGER REFERENCES findings (id),
    created_at DATETIME,
    updated_at DATETIME
);

create table finding_data
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    finding_id INTEGER,
    created_at DATETIME,
    updated_at DATETIME
);

create table finding_data_points
(
    id              INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    finding_data_id INTEGER,
    handle          VARCHAR,
    value           DOUBLE,
    created_at       DATETIME,
    updated_at       DATETIME
);

create table rules
(
    id                 INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    ruleKey            VARCHAR,
    name               VARCHAR,
    description        VARCHAR,
    priority           INTEGER,
    rule_repository_id INTEGER REFERENCES rule_repositories (id),
    created_at         DATETIME,
    updated_at         DATETIME,
    CONSTRAINT rules_unique UNIQUE (ruleKey)
);

create table rules_tags
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    rule_id    INTEGER REFERENCES rules (id),
    tag_id     INTEGER REFERENCES tags (id),
    created_at DATETIME,
    updated_at DATETIME
);

create table tags
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    tag        VARCHAR,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT tags_unique UNIQUE (tag)
);

create table rule_repositories
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    repoKey    VARCHAR,
    name       VARCHAR,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT rule_repositories_unique UNIQUE (repoKey)
);

create table measures
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    measureKey VARCHAR,
    metricKey  VARCHAR,
    value      DOUBLE,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT measures_unique UNIQUE (measureKey)
);

create table metrics_measures
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    metric_id  INTEGER REFERENCES metrics (id),
    measure_id INTEGER REFERENCES measures (id),
    created_at DATETIME,
    updated_at DATETIME
);

create table projects_measures
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    project_id INTEGER REFERENCES projects (id),
    measure_id INTEGER REFERENCES measures (id),
    created_at DATETIME,
    updated_at DATETIME
);

create table namespaces_measures
(
    id           INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    namespace_id INTEGER REFERENCES namespaces (id),
    measure_id   INTEGER REFERENCES measures (id),
    created_at   DATETIME,
    updated_at   DATETIME
);

create table systems_measures
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    system_id  INTEGER REFERENCES systems (id),
    measure_id INTEGER REFERENCES measures (id),
    created_at DATETIME,
    updated_at DATETIME
);

create table pattern_instances_measures
(
    id                  INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    pattern_instance_id INTEGER REFERENCES pattern_instances (id),
    measure_id          INTEGER REFERENCES measures (id),
    created_at          DATETIME,
    updated_at          DATETIME
);

create table files_measures
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    file_id    INTEGER REFERENCES files (id),
    measure_id INTEGER REFERENCES measures (id),
    created_at DATETIME,
    updated_at DATETIME
);

create table modules_measures
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    module_id  INTEGER REFERENCES projects (id),
    measure_id INTEGER REFERENCES measures (id),
    created_at DATETIME,
    updated_at DATETIME
);

create table initializers_measures
(
    id             INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    initializer_id INTEGER REFERENCES initializers (id),
    measure_id     INTEGER REFERENCES measures (id),
    created_at     DATETIME,
    updated_at     DATETIME
);

create table methods_measures
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    method_id  INTEGER REFERENCES methods (id),
    measure_id INTEGER REFERENCES measures (id),
    created_at DATETIME,
    updated_at DATETIME
);

create table classes_measures
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    class_id   INTEGER REFERENCES classes (id),
    measure_id INTEGER REFERENCES measures (id),
    created_at DATETIME,
    updated_at DATETIME
);

create table enums_measures
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    enum_id    INTEGER REFERENCES enums (id),
    measure_id INTEGER REFERENCES measures (id),
    created_at DATETIME,
    updated_at DATETIME
);

create table interfaces_measures
(
    id           INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    interface_id INTEGER REFERENCES interfaces (id),
    measure_id   INTEGER REFERENCES measures (id),
    created_at   DATETIME,
    updated_at   DATETIME
);

create table constructors_measures
(
    id             INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    constructor_id INTEGER REFERENCES constructors (id),
    measure_id     INTEGER REFERENCES measures (id),
    created_at     DATETIME,
    updated_at     DATETIME
);

create table destructors_measures
(
    id            INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    destructor_id INTEGER REFERENCES destructors (id),
    measure_id    INTEGER REFERENCES measures (id),
    created_at    DATETIME,
    updated_at    DATETIME
);

create table metrics
(
    id                   INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    metricKey            VARCHAR,
    name                 VARCHAR,
    description          VARCHAR,
    handle               VARCHAR,
    evaluator            VARCHAR,
    metric_repository_id INTEGER REFERENCES metric_repositories (id),
    created_at           DATETIME,
    updated_at           DATETIME,
    CONSTRAINT metrics_unique UNIQUE (metricKey)
);

create table metric_repositories
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    repoKey    VARCHAR,
    name       VARCHAR,
    toolName   VARCHAR,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT metric_repositories_unique UNIQUE (repoKey)
);

create table projects
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    projKey    VARCHAR,
    name       VARCHAR,
    version    VARCHAR,
    relPath    VARCHAR,
    srcPath    VARCHAR,
    binPath    VARCHAR,
    testPath   VARCHAR,
    system_id  INTEGER REFERENCES systems (id),
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT projects_unique UNIQUE (projKey)
);

create table languages
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT languages_unique UNIQUE (name)
);

create table projects_languages
(
    id          INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    project_id  INTEGER REFERENCES projects (id),
    language_id INTEGER REFERENCES languages (id),
    created_at  DATETIME,
    updated_at  DATETIME
);

create table modules
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    moduleKey  VARCHAR,
    name       VARCHAR,
    relPath    VARCHAR,
    project_id INTEGER REFERENCES projects (id),
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT modules_unique UNIQUE (moduleKey)
);

create table scms
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    scmKey     VARCHAR,
    name       VARCHAR,
    tag        VARCHAR,
    branch     VARCHAR,
    url        VARCHAR,
    project_id INTEGER REFERENCES projects (id),
    type       INTEGER,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT scms_unique UNIQUE (scmKey)
);

create table namespaces
(
    id            INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nsKey         VARCHAR,
    name          VARCHAR,
    project_id    INTEGER REFERENCES projects (id),
    relPath       VARCHAR,
    parent_ns_id  INTEGER,
    parent_mod_id INTEGER,
    created_at    DATETIME,
    updated_at    DATETIME,
    CONSTRAINT namespaces_unique UNIQUE (nsKey)
);

create table files
(
    id           INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    fileKey      VARCHAR,
    pathIndex    INTEGER NOT NULL,
    name         VARCHAR,
    type         INTEGER,
    relPath      VARCHAR,
    start        INTEGER,
    end          INTEGER,
    parseStage   INTEGER,
    project_id   INTEGER REFERENCES projects (id),
    parent_ns_id INTEGER,
    created_at   DATETIME,
    updated_at   DATETIME,
    CONSTRAINT files_unique UNIQUE (fileKey)
);

create table imports
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR,
    start      INTEGER,
    end        INTEGER,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT imports_unique UNIQUE (name)
);

create table files_imports
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    file_id    INTEGER REFERENCES files (id),
    import_id  INTEGER REFERENCES imports (id),
    created_at DATETIME,
    updated_at DATETIME
);

create table unknown_types
(
    id               INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    start            INTEGER,
    end              INTEGER,
    compKey          VARCHAR,
    name             VARCHAR,
    accessibility    INTEGER,
    qualified_name   VARCHAR,
    project_id       INTEGER REFERENCES projects (id),
    created_at       DATETIME,
    updated_at       DATETIME,
    CONSTRAINT unknown_types_unique UNIQUE (compKey)
);

create table classes
(
    id               INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    start            INTEGER,
    end              INTEGER,
    compKey          VARCHAR,
    name             VARCHAR,
    abstract         INTEGER,
    accessibility    INTEGER,
    qualified_name   VARCHAR,
    namespace_id     INTEGER REFERENCES namespaces (id),
    parent_type_id   INTEGER,
    parent_type_type VARCHAR,
    parent_file_id   INTEGER,
    created_at       DATETIME,
    updated_at       DATETIME,
    CONSTRAINT classes_unique UNIQUE (compKey)
);

create table enums
(
    id               INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    start            INTEGER,
    end              INTEGER,
    compKey          VARCHAR,
    name             VARCHAR,
    accessibility    INTEGER,
    qualified_name   VARCHAR,
    namespace_id     INTEGER REFERENCES namespaces (id),
    parent_type_id   INTEGER,
    parent_type_type VARCHAR,
    parent_file_id   INTEGER,
    created_at       DATETIME,
    updated_at       DATETIME,
    CONSTRAINT enums_unique UNIQUE (compKey)
);

create table interfaces
(
    id               INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    start            INTEGER,
    end              INTEGER,
    compKey          VARCHAR,
    name             VARCHAR,
    accessibility    INTEGER,
    qualified_name   VARCHAR,
    namespace_id     INTEGER REFERENCES namespaces (id),
    parent_type_id   INTEGER,
    parent_type_type VARCHAR,
    parent_file_id   INTEGER,
    created_at       DATETIME,
    updated_at       DATETIME,
    CONSTRAINT interfaces_unique UNIQUE (compKey)
);

create table literals
(
    id            INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    start         INTEGER,
    end           INTEGER,
    compKey       VARCHAR,
    name          VARCHAR,
    accessibility INTEGER,
    parent_id     INTEGER,
    parent_type   VARCHAR,
    created_at    DATETIME,
    updated_at    DATETIME,
    CONSTRAINT literals_unique UNIQUE (compKey)
);

create table initializers
(
    id            INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    start         INTEGER,
    end           INTEGER,
    compKey       VARCHAR,
    name          VARCHAR,
    localVars     INTEGER,
    cfg           VARCHAR,
    accessibility INTEGER,
    parent_id     INTEGER,
    parent_type   VARCHAR,
    number        INTEGER,
    instance      INTEGER(1), -- boolean
    created_at    DATETIME,
    updated_at    DATETIME,
    CONSTRAINT initialilzers_unique UNIQUE (compKey)
);

create table fields
(
    id            INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    start         INTEGER,
    end           INTEGER,
    compKey       VARCHAR,
    name          VARCHAR,
    accessibility INTEGER,
    parent_id     INTEGER,
    parent_type   VARCHAR,
    created_at    DATETIME,
    updated_at    DATETIME,
    CONSTRAINT fields_unique UNIQUE (compKey)
);

create table methods
(
    id            INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    start         INTEGER,
    end           INTEGER,
    compKey       VARCHAR,
    name          VARCHAR,
    localVars     INTEGER,
    accessibility INTEGER,
    cfg           VARCHAR,
    parent_id     INTEGER,
    parent_type   VARCHAR,
    created_at    DATETIME,
    updated_at    DATETIME
);

create table constructors
(
    id            INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    start         INTEGER,
    end           INTEGER,
    compKey       VARCHAR,
    name          VARCHAR,
    localVars     INTEGER,
    cfg           VARCHAR,
    accessibility INTEGER,
    parent_id     INTEGER,
    parent_type   VARCHAR,
    created_at    DATETIME,
    updated_at    DATETIME
);

create table destructors
(
    id            INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    start         INTEGER,
    end           INTEGER,
    compKey       VARCHAR,
    name          VARCHAR,
    localVars     INTEGER,
    cfg           VARCHAR,
    accessibility INTEGER,
    parent_id     INTEGER,
    parent_type   VARCHAR,
    created_at    DATETIME,
    updated_at    DATETIME
);

create table parameters
(
    id          INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR,
    varg        INTEGER,
    parent_id   INTEGER,
    parent_type VARCHAR,
    created_at  DATETIME,
    updated_at  DATETIME
);

create table method_exceptions
(
    id          INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    parent_id   INTEGER,
    parent_type VARCHAR,
    created_at  DATETIME,
    updated_at  DATETIME
);

create table methods_method_exceptions
(
    id                  INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    method_id           INTEGER REFERENCES methods (id),
    method_exception_id INTEGER REFERENCES method_exceptions (id),
    created_at          DATETIME,
    updated_at          DATETIME
);

create table constructors_method_exceptions
(
    id                  INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    constructor_id      INTEGER REFERENCES constructors (id),
    method_exception_id INTEGER REFERENCES method_exceptions (id),
    created_at          DATETIME,
    updated_at          DATETIME
);

create table destructors_method_exceptions
(
    id                  INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    destructor_id       INTEGER REFERENCES destructors (id),
    method_exception_id INTEGER REFERENCES method_exceptions (id),
    created_at          DATETIME,
    updated_at          DATETIME
);

create table type_refs
(
    id           INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    dimensions   VARCHAR,
    typeName     VARCHAR,
    typeFullName VARCHAR,
    type         INTEGER,
    typeref_id   INTEGER REFERENCES type_refs (id),
    is_bound     INTEGER(1),
    created_at   DATETIME,
    updated_at   DATETIME
);

create table parameters_typerefs
(
    id           INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    parameter_id INTEGER REFERENCES parameters (id),
    type_ref_id  INTEGER REFERENCES type_refs (id),
    created_at   DATETIME,
    updated_at   DATETIME
);

create table methods_typerefs
(
    id          INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    method_id   INTEGER REFERENCES methods (id),
    type_ref_id INTEGER REFERENCES type_refs (id),
    created_at  DATETIME,
    updated_at  DATETIME
);

create table constructors_typerefs
(
    id             INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    constructor_id INTEGER REFERENCES constructors (id),
    type_ref_id    INTEGER REFERENCES type_refs (id),
    created_at     DATETIME,
    updated_at     DATETIME
);

create table destructors_typerefs
(
    id            INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    destructor_id INTEGER REFERENCES destructors (id),
    type_ref_id   INTEGER REFERENCES type_refs (id),
    created_at    DATETIME,
    updated_at    DATETIME
);

create table fields_typerefs
(
    id          INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    field_id    INTEGER REFERENCES fields (id),
    type_ref_id INTEGER REFERENCES type_refs (id),
    created_at  DATETIME,
    updated_at  DATETIME
);

create table methodexceptions_typerefs
(
    id           INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    exception_id INTEGER REFERENCES method_exceptions (id),
    type_ref_id  INTEGER REFERENCES type_refs (id),
    created_at   DATETIME,
    updated_at   DATETIME
);

create table typerefs_typerefs
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    parent_id  INTEGER REFERENCES parameters (id),
    child_id   INTEGER REFERENCES type_refs (id),
    created_at DATETIME,
    updated_at DATETIME
);

create table modifiers
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT modifiers_unique UNIQUE (name)
);

create table classes_modifiers
(
    id          INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    class_id    INTEGER REFERENCES classes (id),
    modifier_id INTEGER REFERENCES modifiers (id),
    created_at  DATETIME,
    updated_at  DATETIME
);

create table enums_modifiers
(
    id          INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    enum_id     INTEGER REFERENCES enums (id),
    modifier_id INTEGER REFERENCES modifiers (id),
    created_at  DATETIME,
    updated_at  DATETIME
);

create table interfaces_modifiers
(
    id           INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    interface_id INTEGER REFERENCES interfaces (id),
    modifier_id  INTEGER REFERENCES modifiers (id),
    created_at   DATETIME,
    updated_at   DATETIME
);

create table literals_modifiers
(
    id          INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    literal_id  INTEGER REFERENCES literals (id),
    modifier_id INTEGER REFERENCES modifiers (id),
    created_at  DATETIME,
    updated_at  DATETIME
);

create table initializers_modifiers
(
    id             INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    initializer_id INTEGER REFERENCES initializers (id),
    modifier_id    INTEGER REFERENCES modifiers (id),
    created_at   DATETIME,
    updated_at   DATETIME
);

create table fields_modifiers
(
    id          INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    field_id    INTEGER REFERENCES fields (id),
    modifier_id INTEGER REFERENCES modifiers (id),
    created_at  DATETIME,
    updated_at  DATETIME
);

create table methods_modifiers
(
    id          INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    method_id   INTEGER REFERENCES methods (id),
    modifier_id INTEGER REFERENCES modifiers (id),
    created_at  DATETIME,
    updated_at  DATETIME
);

create table constructors_modifiers
(
    id             INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    constructor_id INTEGER REFERENCES constructors (id),
    modifier_id    INTEGER REFERENCES modifiers (id),
    created_at     DATETIME,
    updated_at     DATETIME
);

create table destructors_modifiers
(
    id            INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    destructor_id INTEGER REFERENCES destructors (id),
    modifier_id   INTEGER REFERENCES modifiers (id),
    created_at    DATETIME,
    updated_at    DATETIME
);

create table parameters_modifiers
(
    id           INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    parameter_id INTEGER REFERENCES parameters (id),
    modifier_id  INTEGER REFERENCES modifiers (id),
    created_at   DATETIME,
    updated_at   DATETIME
);

create table template_params
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR,
    created_at DATETIME,
    updated_at DATETIME
);

create table template_params_typerefs
(
    id                INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    template_param_id INTEGER REFERENCES template_params (id),
    typeref_id        INTEGER REFERENCES type_refs (id),
    created_at        DATETIME,
    updated_at        DATETIME
);

create table methods_template_params
(
    id                INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    method_id         INTEGER REFERENCES methods (id),
    template_param_id INTEGER REFERENCES template_params (id),
    created_at        DATETIME,
    updated_at        DATETIME
);

create table constructors_template_params
(
    id                INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    constructor_id    INTEGER REFERENCES constructors (id),
    template_param_id INTEGER REFERENCES template_params (id),
    created_at        DATETIME,
    updated_at        DATETIME
);

create table destructors_template_params
(
    id                INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    destructor_id     INTEGER REFERENCES destructors (id),
    template_param_id INTEGER REFERENCES template_params (id),
    created_at        DATETIME,
    updated_at        DATETIME
);

create table fields_template_params
(
    id                INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    field_id          INTEGER REFERENCES fields (id),
    template_param_id INTEGER REFERENCES template_params (id),
    created_at        DATETIME,
    updated_at        DATETIME
);

create table interfaces_template_params
(
    id                INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    interface_id      INTEGER REFERENCES interfaces (id),
    template_param_id INTEGER REFERENCES template_params (id),
    created_at        DATETIME,
    updated_at        DATETIME
);

create table classes_template_params
(
    id                INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    class_id          INTEGER REFERENCES classes (id),
    template_param_id INTEGER REFERENCES template_params (id),
    created_at        DATETIME,
    updated_at        DATETIME
);

create table enums_template_params
(
    id                INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    enum_id           INTEGER REFERENCES enums (id),
    template_param_id INTEGER REFERENCES template_params (id),
    created_at        DATETIME,
    updated_at        DATETIME
);

insert into modifiers (name)
values ('STATIC'),
       ('FINAL'),
       ('ABSTRACT'),
       ('NATIVE'),
       ('STRICTFP'),
       ('SYNCHRONIZED'),
       ('TRANSIENT'),
       ('VOLATILE'),
       ('DEFAULT'),
       ('ASYNC'),
       ('CONST'),
       ('EXTERN'),
       ('READONLY'),
       ('SEALED'),
       ('UNSAFE'),
       ('VIRTUAL'),
       ('OUT'),
       ('REF'),
       ('PARAMS'),
       ('OVERRIDE'),
       ('NEW'),
       ('PARTIAL'),
       ('EXPLICIT'),
       ('IMPLICIT'),
       ('YIELD'),
       ('THIS');
