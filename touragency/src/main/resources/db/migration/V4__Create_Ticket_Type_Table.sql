create table ticket_type
(
    id                    bigint           not null
        primary key,
    description           varchar(255),
    price                 double precision not null,
    type_by_comfort integer,
    type_by_price integer,
    type_name             varchar(255)
);


