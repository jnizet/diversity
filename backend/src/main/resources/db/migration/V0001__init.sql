create table indicator (
    id      BIGINT primary key,
    biom_id VARCHAR not null
);

create sequence indicator_seq start with 1000;

create table indicator_value (
   id           BIGINT primary key,
   indicator_id BIGINT,
   territory    VARCHAR not null,
   value        DOUBLE PRECISION not null,
   unit         VARCHAR not null,
   constraint indicator_id_fk foreign key (indicator_id) references indicator(id)
);

create sequence indicator_value_seq start with 1000;

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
    title      VARCHAR not null,
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
    title    BOOLEAN not null default false,
    constraint page_id_fk foreign key (page_id) references page(id),
    constraint image_id_fk foreign key (image_id) references image(id),
    constraint page_element_key_un unique (page_id, key)
);

create index page_element_text_idx
    on page_element
        using gin(to_tsvector('french', page_element.text));

create sequence page_element_seq start with 1000;

