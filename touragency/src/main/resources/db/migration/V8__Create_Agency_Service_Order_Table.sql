create table agency_service_order
(
    id               bigint not null
        primary key,
    date             date,
    tourist_id        bigint
        constraint fk1aunjupbolymmxniu1fi8xx7l
            references tourist,
    employee_id      bigint
        constraint fkfuyc3horxhkpe8p4suopxhqjd
            references employee,
    agency_service_id bigint
        constraint fkr8dly3b6in7ld6l427t7gxvyv
            references agency_service
);
