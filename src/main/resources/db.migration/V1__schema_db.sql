create table if not exists t_user (
    id serial not null,
    username varchar(255),
    password varchar(255),
    role varchar(255) check
        (role in ('ROLE_USER','ROLE_ADMIN', 'ROLE_GUEST')),
    primary key (id)
);