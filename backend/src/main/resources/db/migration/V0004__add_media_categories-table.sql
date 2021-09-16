create sequence media_category_seq start with 1000;

create table media_category (
      id   BIGINT primary key,
      name VARCHAR not null
);

create table media_category_relation (
    media_page_id BIGINT,
    category_id BIGINT,
    constraint indicator_id_fk foreign key (media_page_id) references page(id),
    constraint category_id_fk foreign key (category_id) references media_category(id),
    primary key (media_page_id, category_id)
);
