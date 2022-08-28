create table ticket_reservation
(
    id          bigint not null
        primary key,
    finish_date date,
    start_date  date,
    total_price bigint,
    tourist_id   bigint
        constraint fk4asfymj7m4gwt4u5c1xa0rge9
            references tourist,
    employee_id bigint
        constraint fk1ruqfrorgbevhk95sgdufktig
            references employee,
    ticket_id     bigint
        constraint fk19p6c3un3mbs7b7bxkcxk8xn2
            references ticket
);


