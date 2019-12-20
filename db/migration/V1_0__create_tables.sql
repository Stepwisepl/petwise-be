create sequence missing_report_seq start 1 increment 1;
create table missing_report (
   id int8 not null DEFAULT NEXTVAL('missing_report_seq'),
    place_of_missing GEOMETRY not null,
    report_time timestamp not null,
    status int4,
    pet_id int8,
    primary key (id)
);

create sequence pet_seq start 1 increment 1;
create table pet (
   id int8 not null DEFAULT NEXTVAL('pet_seq'),
    image_id varchar(255) not null,
    name varchar(255) not null,
    primary key (id)
);

create sequence spotted_report_seq start 1 increment 1;
create table spotted_report (
   id int8 not null DEFAULT NEXTVAL('spotted_report_seq'),
    place_of_finding GEOMETRY not null,
    time_of_finding timestamp not null,
    missing_report_id int8,
    image_id varchar(255) not null,
    primary key (id)
);

alter table missing_report
   add constraint FKh3dc8w4dlpq4s5vomf40lreiw
   foreign key (pet_id)
   references pet;

alter table spotted_report
   add constraint FKtb4lsm0hkfr6ehbym95u583f
   foreign key (missing_report_id)
   references missing_report;