create table employee
(
    id         bigint not null
        primary key,
    end_date   date,
    full_name  varchar(255),
    phone      varchar(255),
    start_date date,
    post_id    bigint
        constraint fkcm3b9d5fiw8s6co7lkw8c0lbs
            references post
);


