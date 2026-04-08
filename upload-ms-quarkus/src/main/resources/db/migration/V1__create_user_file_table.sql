create table user_file
(
    id                bigserial primary key,
    file_uuid         varchar(255)                not null unique,
    file_name         varchar(255)                not null,
    url               varchar(1000),
    file_data         bytea,
    file_content_type varchar(255)                not null,
    username          varchar(255)                not null,
    uploaded_at       timestamp without time zone not null,
    upload_status     varchar(20)                 not null check ( upload_status in ('CREATED', 'SUCCESS', 'FAILED')),
    expiration_date   timestamp without time zone,
    created_at        timestamp without time zone not null,
    updated_at        timestamp without time zone not null
)