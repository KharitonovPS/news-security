create table if not exists t_user (
    id serial not null,
    username varchar(255),
    password varchar(255),
    primary key (id)
);


create table if not exists user_role (
    user_id integer not null,
    roles varchar(255) check
        (roles in ('ROLE_USER','ROLE_ADMIN'))
);


alter table if exists user_role add constraint FKeqon9sx5vssprq67dxm3s7ump
    foreign key (user_id) references t_user;