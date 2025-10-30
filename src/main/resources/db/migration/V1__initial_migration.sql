-- User table
create table user
(
    user_id    bigint auto_increment
        primary key,
    username   varchar(255)                       not null unique,
    email      varchar(255)                       not null unique,
    password   varchar(255)                       not null,
    lab_id     bigint                             not null,
    role       varchar(255)                       not null,
    created_at datetime default current_timestamp not null,
    updated_at datetime default current_timestamp null on update current_timestamp null,
    constraint user_lab_id_fk
        foreign key (lab_id) references lab (lab_id)
            on delete restrict -- Don't delete labs with users
            on update cascade
);

-- Lab table
create table lab
(
    lab_id        bigint auto_increment
        primary key,
    name          varchar(255)                       not null,
    contact_email varchar(255)                       not null,
    institution   varchar(255)                       not null,
    department    varchar(255)                       not null,
    address       varchar(255)                       not null,
    description   text                               not null,
    created_at    datetime default current_timestamp not null,
    updated_at    datetime default current_timestamp not null on update current_timestamp
);

-- Lab-User junction table
create table lab_user
(
    lab_id  bigint not null,
    user_id bigint not null,
    primary key (lab_id, user_id),  -- Composite primary key prevents duplicates
    constraint fk_lab_user_lab
        foreign key (lab_id) references lab (lab_id)
            on delete cascade, -- If lab is deleted, remove all roster entries
    constraint fk_lab_user_user
        foreign key (user_id) references user (user_id)
            on delete cascade -- If user is deleted, remove from all lab rosters
);

-- Research Protocol table
create table research_protocol
(
    protocol_id     bigint auto_increment
        primary key,
    protocol_number varchar(255)                            not null unique,
    title           text                                    not null,
    description     text                                    not null,
    lab_id          bigint                                  not null,
    status          varchar(255)                            not null,
    approval_date   date                                    null,
    expiration_date date as (date_add(approval_date, interval 1 year)) stored,
    created_at      datetime default current_timestamp      not null,
    updated_at      datetime default current_timestamp      not null on update current_timestamp,
    constraint fk_lab_id
        foreign key (lab_id) references lab (lab_id)
            on delete restrict  -- Don't allow deleting labs with active protocols
);

-- TODO: Lab-Protocol junction table

-- Mouse table
create table mouse
(
    mouse_id      bigint auto_increment
        primary key,
    name          varchar(255)                       not null,
    sex           enum ('M', 'F')                    not null,
    genotype      varchar(255)                       not null,
    strain        varchar(255)                       not null,
    date_of_birth date                               not null,
    availability  boolean  default true              not null,
    notes         text                               null,
    lab_id        bigint                             not null,
    protocol_id   bigint                             not null,
    user_id       bigint                             not null,
    mother_id     bigint                             null,
    father_id     bigint                             null,
    created_at    datetime default current_timestamp not null,
    updated_at    datetime default current_timestamp not null on update current_timestamp,
    constraint fk_father_id
        foreign key (father_id) references mouse (mouse_id)
            on delete set null, -- If father is deleted, set to NULL
    constraint fk_lab_id
        foreign key (lab_id) references lab (lab_id)
            on delete restrict,  -- Don't delete labs with mice
    constraint fk_mother_id
        foreign key (mother_id) references mouse (mouse_id)
            on delete set null, -- If mother is deleted, set to NULL
    constraint fk_protocol_id
        foreign key (protocol_id) references research_protocol (protocol_id)
            on delete restrict, -- Don't delete protocols with active mice
    constraint fk_user_id
        foreign key (user_id) references user (user_id)
            on delete restrict -- Don't delete users who have mice assigned
);

-- TODO: Mouse-Protocol junction table

-- Litter junction table
create table litter
(
    litter_id     bigint auto_increment
        primary key,
    mother_id     bigint not null,
    father_id     bigint not null,
    date_of_birth date   not null,
    protocol_id   int    not null,
    notes         text   null,
    constraint fk_father_id
        foreign key (father_id) references mouse (mouse_id),
    constraint fk_mother_id
        foreign key (mother_id) references mouse (mouse_id),
    constraint fk_protocol_id
        foreign key (protocol_id) references research_protocol (protocol_id)
);

-- Add litter_id to mouse table
alter table mouse add column litter_id bigint null;
alter table mouse add constraint fk_mouse_litter
    foreign key (litter_id) references litter(litter_id);

-- Log Entry table
create table log_entry
(
    log_id     bigint auto_increment
        primary key,
    user_id    bigint                             not null,
    lab_id     bigint                             not null,
    content    text                               not null,
    created_at datetime default current_timestamp not null,
    constraint fk_lab_id
        foreign key (lab_id) references lab (lab_id),
    constraint fk_user_id
        foreign key (user_id) references user (user_id)
);

-- Mouse Log Entry junction table
create table mouse_log_entry
(
    log_id   bigint not null,
    mouse_id bigint not null,

    primary key (log_id, mouse_id),  -- Composite PK prevents duplicates

    constraint fk_mouse_log_entry_log
        foreign key (log_id) references log_entry (log_id)
            on delete cascade ,  -- If log deleted, remove all associations

    constraint fk_mouse_log_entry_mouse
        foreign key (mouse_id) references mouse (mouse_id)
            on delete cascade  -- If mouse deleted, remove from all logs
);

-- Mouse Request table
create table mouse_request
(
    request_id   bigint auto_increment
        primary key,
    requestor_id bigint                             not null,
    mouse_id     bigint                             not null,
    from_lab_id  bigint                             not null,
    to_lab_id    bigint                             not null,
    message      text                               not null,
    status       varchar(255)                       not null,
    created_at   datetime default current_timestamp not null,
    updated_at   datetime default current_timestamp not null on update current_timestamp,
    constraint fk_from_lab_id
        foreign key (from_lab_id) references lab (lab_id)
            on delete cascade,
    constraint fk_mouse_id
        foreign key (mouse_id) references mouse (mouse_id)
            on delete cascade,
    constraint fk_requestor_id
        foreign key (requestor_id) references user (user_id)
            on delete cascade,
    constraint fk_to_lab_id
        foreign key (to_lab_id) references lab (lab_id)
            on delete cascade
);