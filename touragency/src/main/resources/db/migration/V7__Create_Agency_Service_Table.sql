create table agency_service
(
    id           bigint not null
        primary key,
    description  varchar(255),
    price        bigint,
    service_name varchar(255)
);


