create table ticket
(
    id           bigint  not null
        primary key,
    is_available boolean not null,
    ticket_number  varchar(255),
    ticket_type_id bigint
        constraint fkd468eq7j1cbue8mk20qfrj5et
            references ticket_type
);