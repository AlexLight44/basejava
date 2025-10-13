create table resume
(
    uuid      varchar(36) primary key not null,
    full_name TEXT                 not null
);

CREATE table contact
(
    id          SERIAL PRIMARY KEY,
    resume_uuid varchar(36) not null references resume (uuid) on delete cascade,
    type        text     not null,
    value       text     not null
);

create unique index contact_uuid_type_index
    on contact (resume_uuid, type);