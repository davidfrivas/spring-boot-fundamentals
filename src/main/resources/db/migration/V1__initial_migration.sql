-- User table
CREATE TABLE user
(
    user_id    bigint auto_increment
        PRIMARY KEY,
    username   VARCHAR(255)                       NOT NULL UNIQUE,
    name       VARCHAR(255)                       NOT NULL,
    email      VARCHAR(255)                       NOT NULL UNIQUE,
    password   VARCHAR(255)                       NOT NULL,
    lab_id     bigint                             NOT NULL,
    role       VARCHAR(255)                       NOT NULL,
    created_at datetime DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NULL
);

-- Lab table
CREATE TABLE lab
(
    lab_id        bigint auto_increment
        PRIMARY KEY,
    name          VARCHAR(255)                       NOT NULL,
    contact_email VARCHAR(255)                       NOT NULL,
    institution   VARCHAR(255)                       NOT NULL,
    department    VARCHAR(255)                       NOT NULL,
    address       VARCHAR(255)                       NOT NULL,
    description   text                               NOT NULL,
    created_at    datetime DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at    datetime DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP
);

-- Add lab-id FK constraint to User table
ALTER TABLE user
    ADD CONSTRAINT fk_user_lab_id
        FOREIGN KEY (lab_id) REFERENCES lab (lab_id)
            ON DELETE RESTRICT -- Don't delete labs with users
            ON UPDATE CASCADE;

-- Research Protocol table
CREATE TABLE research_protocol
(
    protocol_id     bigint auto_increment
        PRIMARY KEY,
    protocol_number VARCHAR(255)                       NOT NULL UNIQUE,
    title           text                               NOT NULL,
    description     text                               NOT NULL,
    lab_id          bigint                             NOT NULL,
    status          VARCHAR(255)                       NOT NULL,
    approval_date   DATE NULL,
    expiration_date DATE AS (date_add(approval_date, INTERVAL 1 YEAR)) stored,
    created_at      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at      datetime DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_protocol_lab_id
        FOREIGN KEY (lab_id) REFERENCES lab (lab_id)
            ON DELETE RESTRICT -- Don't allow deleting labs with active protocols
);

-- Lab-Protocol junction table
CREATE TABLE protocol_personnel
(
    protocol_id bigint                             NOT NULL,
    user_id     bigint                             NOT NULL,
    role        VARCHAR(255)                       NOT NULL,
    assigned_at datetime DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT pk_protocol_personnel
        PRIMARY KEY (protocol_id, user_id),
    CONSTRAINT fk_protocol_personnel_protocol
        FOREIGN KEY (protocol_id) REFERENCES research_protocol (protocol_id)
            ON DELETE CASCADE, -- If protocol deleted, remove all personnel assignments
    CONSTRAINT fk_protocol_personnel_user
        FOREIGN KEY (user_id) REFERENCES user (user_id)
            ON DELETE CASCADE  -- If user deleted, remove from all protocols
);

-- Mouse table
CREATE TABLE mouse
(
    mouse_id      bigint auto_increment
        PRIMARY KEY,
    name          VARCHAR(255)                       NOT NULL,
    sex           enum ('M', 'F')         NULL,
    genotype      VARCHAR(255)                       NOT NULL,
    strain        VARCHAR(255)                       NOT NULL,
    date_of_birth DATE                               NOT NULL,
    availability  boolean  DEFAULT TRUE              NOT NULL,
    notes         text NULL,
    lab_id        bigint                             NOT NULL,
    protocol_id   bigint                             NOT NULL,
    user_id       bigint                             NOT NULL,
    mother_id     bigint NULL,
    father_id     bigint NULL,
    created_at    datetime DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at    datetime DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_mouse_father_id
        FOREIGN KEY (father_id) REFERENCES mouse (mouse_id)
            ON DELETE SET NULL, -- If father is deleted, set to NULL
    CONSTRAINT fk_mouse_lab_id
        FOREIGN KEY (lab_id) REFERENCES lab (lab_id)
            ON DELETE RESTRICT, -- Don't delete labs with mice
    CONSTRAINT fk_mouse_mother_id
        FOREIGN KEY (mother_id) REFERENCES mouse (mouse_id)
            ON DELETE SET NULL, -- If mother is deleted, set to NULL
    CONSTRAINT fk_mouse_protocol_id
        FOREIGN KEY (protocol_id) REFERENCES research_protocol (protocol_id)
            ON DELETE RESTRICT, -- Don't delete protocols with active mice
    CONSTRAINT fk_mouse_user_id
        FOREIGN KEY (user_id) REFERENCES user (user_id)
            ON DELETE RESTRICT  -- Don't delete users who have mice assigned
);

-- Litter junction table
CREATE TABLE litter
(
    litter_id     bigint auto_increment
        PRIMARY KEY,
    lab_id        bigint                             NOT NULL,
    mother_id     bigint                             NOT NULL,
    father_id     bigint                             NOT NULL,
    date_of_birth DATE                               NOT NULL,
    protocol_id   bigint                             NOT NULL,
    notes         text NULL,
    created_at    datetime DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at    datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_litter_father_id
        FOREIGN KEY (father_id) REFERENCES mouse (mouse_id)
            ON DELETE RESTRICT, -- Don't delete breeding mice
    CONSTRAINT fk_litter_lab_id
        FOREIGN KEY (lab_id) REFERENCES lab (lab_id)
            ON DELETE RESTRICT,
    CONSTRAINT fk_litter_mother_id
        FOREIGN KEY (mother_id) REFERENCES mouse (mouse_id)
            ON DELETE RESTRICT,
    CONSTRAINT fk_litter_protocol_id
        FOREIGN KEY (protocol_id) REFERENCES research_protocol (protocol_id)
            ON DELETE RESTRICT
);

-- Log Entry table
CREATE TABLE log_entry
(
    log_id     bigint auto_increment
        PRIMARY KEY,
    user_id    bigint NULL,
    lab_id     bigint                             NOT NULL,
    content    text                               NOT NULL,
    created_at datetime DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_log_entry_lab
        FOREIGN KEY (lab_id) REFERENCES lab (lab_id)
            ON DELETE RESTRICT, -- Restrict to preserve logs
    CONSTRAINT fk_log_entry_user
        FOREIGN KEY (user_id) REFERENCES user (user_id)
            ON DELETE SET NULL  -- Preserve logs even if user deleted
);

-- Mouse Log Entry junction table
CREATE TABLE mouse_log_entry
(
    log_id   bigint NOT NULL,
    mouse_id bigint NOT NULL,

    CONSTRAINT pk_mouse_log_entry
        PRIMARY KEY (log_id, mouse_id), -- Composite PK prevents duplicates

    CONSTRAINT fk_mouse_log_entry_log
        FOREIGN KEY (log_id) REFERENCES log_entry (log_id)
            ON DELETE CASCADE,          -- If log deleted, remove all associations

    CONSTRAINT fk_mouse_log_entry_mouse
        FOREIGN KEY (mouse_id) REFERENCES mouse (mouse_id)
            ON DELETE CASCADE           -- If mouse deleted, remove from all logs
);

-- Mouse Request table
CREATE TABLE mouse_request
(
    request_id   bigint auto_increment
        PRIMARY KEY,
    requestor_id bigint                             NOT NULL,
    mouse_id     bigint                             NOT NULL,
    from_lab_id  bigint                             NOT NULL,
    to_lab_id    bigint                             NOT NULL,
    message      text                               NOT NULL,
    status       VARCHAR(255)                       NOT NULL,
    created_at   datetime DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at   datetime DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_from_lab_id
        FOREIGN KEY (from_lab_id) REFERENCES lab (lab_id)
            ON DELETE CASCADE,
    CONSTRAINT fk_mouse_id
        FOREIGN KEY (mouse_id) REFERENCES mouse (mouse_id)
            ON DELETE CASCADE,
    CONSTRAINT fk_requestor_id
        FOREIGN KEY (requestor_id) REFERENCES user (user_id)
            ON DELETE CASCADE,
    CONSTRAINT fk_to_lab_id
        FOREIGN KEY (to_lab_id) REFERENCES lab (lab_id)
            ON DELETE CASCADE
);