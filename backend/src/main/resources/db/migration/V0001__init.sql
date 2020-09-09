create table territory (
    id   BIGINT primary key,
    name VARCHAR not null,
    slug VARCHAR not null unique
);

create sequence territory_seq start with 1000;

create table image (
    id                 BIGINT primary key,
    content_type       VARCHAR not null,
    original_file_name VARCHAR not null
);

create sequence image_seq start with 1000;

create table page (
    id         BIGINT primary key,
    name       VARCHAR not null,
    model_name VARCHAR not null,
    constraint page_name_model_name_un unique (name, model_name)
);

create sequence page_seq start with 1000;

create table page_element (
    id       BIGINT primary key,
    page_id  BIGINT,
    type     VARCHAR not null,
    key      VARCHAR not null,
    text     VARCHAR,
    image_id BIGINT,
    alt      VARCHAR,
    href     VARCHAR,
    constraint page_id_fk foreign key (page_id) references page(id),
    constraint image_id_fk foreign key (image_id) references image(id),
    constraint page_element_key_un unique (page_id, key)
);

create sequence page_element_seq start with 1000;
