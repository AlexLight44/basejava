CREATE TABLE resume
(
    uuid      VARCHAR(36) PRIMARY KEY NOT NULL,
    full_name TEXT                    NOT NULL
);

CREATE TABLE contact
(
    id          SERIAL PRIMARY KEY,
    resume_uuid VARCHAR(36) NOT NULL REFERENCES resume (uuid) ON DELETE CASCADE,
    type        TEXT        NOT NULL,
    value       TEXT        NOT NULL
);

CREATE UNIQUE INDEX contact_uuid_type_index
    ON contact (resume_uuid, type);

CREATE TABLE section
(
    id          SERIAL PRIMARY KEY,
    resume_uuid VARCHAR(36) NOT NULL REFERENCES resume (uuid) ON DELETE CASCADE,
    type        TEXT        NOT NULL,
    value       TEXT        NOT NULL
);

CREATE UNIQUE INDEX section_uuid_type_index
    ON section (resume_uuid, type);

CREATE TABLE organization
(
    id      SERIAL PRIMARY KEY,
    NAME    TEXT NOT NULL,
    website TEXT
);

CREATE TABLE period
(
    id              SERIAL PRIMARY KEY,
    organization_id INT  NOT NULL REFERENCES organization (id) ON DELETE CASCADE,
    start_date      DATE NOT NULL,
    end_date        DATE,
    title           TEXT NOT NULL,
    description     TEXT
);

CREATE TABLE resume_section
(
    resume_uuid     VARCHAR(36) NOT NULL REFERENCES resume (uuid) ON DELETE CASCADE,
    section_type    TEXT        NOT NULL CHECK ( section_type IN ('EXPERIENCE', 'EDUCATION')),
    organization_id INT         NOT NULL REFERENCES organization (id) ON DELETE CASCADE,
    position_order  INT         NOT NULL,
    PRIMARY KEY (resume_uuid, section_type, organization_id, position_order)
);