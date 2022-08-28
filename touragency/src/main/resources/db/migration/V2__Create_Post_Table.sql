create table post
(
    id            bigint           not null
        primary key,
    description   varchar(255),
    position_name varchar(255),
    salary        double precision not null
);


