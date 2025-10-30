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
    updated_at datetime default current_timestamp on update current_timestamp null
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

-- Add lab-id FK constraint to User table
alter table user add constraint fk_user_lab_id
    foreign key (lab_id) references lab (lab_id)
        on delete restrict -- Don't delete labs with users
        on update cascade;

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
    constraint fk_protocol_lab_id
        foreign key (lab_id) references lab (lab_id)
            on delete restrict  -- Don't allow deleting labs with active protocols
);

-- Lab-Protocol junction table
create table protocol_personnel
(
    protocol_id bigint                             not null,
    user_id     bigint                             not null,
    role        varchar(255)                       not null,
    assigned_at datetime default current_timestamp not null,
    constraint pk_protocol_personnel
        primary key (protocol_id, user_id),
    constraint fk_protocol_personnel_protocol
        foreign key (protocol_id) references research_protocol (protocol_id)
            on delete cascade, -- If protocol deleted, remove all personnel assignments
    constraint fk_protocol_personnel_user
        foreign key (user_id) references user (user_id)
            on delete cascade -- If user deleted, remove from all protocols
);

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
    constraint fk_mouse_father_id
        foreign key (father_id) references mouse (mouse_id)
            on delete set null, -- If father is deleted, set to NULL
    constraint fk_mouse_lab_id
        foreign key (lab_id) references lab (lab_id)
            on delete restrict,  -- Don't delete labs with mice
    constraint fk_mouse_mother_id
        foreign key (mother_id) references mouse (mouse_id)
            on delete set null, -- If mother is deleted, set to NULL
    constraint fk_mouse_protocol_id
        foreign key (protocol_id) references research_protocol (protocol_id)
            on delete restrict, -- Don't delete protocols with active mice
    constraint fk_mouse_user_id
        foreign key (user_id) references user (user_id)
            on delete restrict -- Don't delete users who have mice assigned
);

-- Litter junction table
create table litter
(
    litter_id     bigint auto_increment
        primary key,
    lab_id        bigint not null,
    mother_id     bigint not null,
    father_id     bigint not null,
    date_of_birth date   not null,
    protocol_id   bigint    not null,
    notes         text   null,
    created_at    datetime default current_timestamp not null,
    updated_at    datetime default current_timestamp on update current_timestamp not null,
    constraint fk_litter_father_id
        foreign key (father_id) references mouse (mouse_id)
            on delete restrict,  -- Don't delete breeding mice
    constraint fk_litter_lab_id
        foreign key (lab_id) references lab (lab_id)
            on delete restrict,
    constraint fk_litter_mother_id
        foreign key (mother_id) references mouse (mouse_id)
            on delete restrict,
    constraint fk_litter_protocol_id
        foreign key (protocol_id) references research_protocol (protocol_id)
            on delete restrict
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
    user_id    bigint                             null,
    lab_id     bigint                             not null,
    content    text                               not null,
    created_at datetime default current_timestamp not null,
    constraint fk_log_entry_lab
        foreign key (lab_id) references lab (lab_id)
            on delete restrict, -- Restrict to preserve logs
    constraint fk_log_entry_user
        foreign key (user_id) references user (user_id)
            on delete set null -- Preserve logs even if user deleted
);

-- Mouse Log Entry junction table
create table mouse_log_entry
(
    log_id   bigint not null,
    mouse_id bigint not null,

    constraint pk_mouse_log_entry
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