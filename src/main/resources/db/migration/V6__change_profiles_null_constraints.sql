alter table profiles
    modify bio text not null;

alter table profiles
    modify phone_number varchar(15) not null;

alter table profiles
    modify date_of_birth date not null;

alter table profiles
    modify loyalty_points int unsigned default '0' not null;

