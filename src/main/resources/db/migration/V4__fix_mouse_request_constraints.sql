USE `mouse-colony-app`;

-- Fix MouseRequest constraints to prevent unwanted cascading deletes
-- Only user deletion should cascade delete requests

-- Drop all existing foreign keys
ALTER TABLE mouse_request
    DROP FOREIGN KEY fk_requestor_id,
    DROP FOREIGN KEY fk_mouse_id,
    DROP FOREIGN KEY fk_from_lab_id,
    DROP FOREIGN KEY fk_to_lab_id;

-- Add foreign keys with correct ON DELETE behavior
ALTER TABLE mouse_request
    -- Keep requestor cascade (requests are tied to users)
    ADD CONSTRAINT fk_requestor_id
        FOREIGN KEY (requestor_id) REFERENCES user (user_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,

    -- Change mouse to RESTRICT (preserve requests as historical data)
    ADD CONSTRAINT fk_mouse_id
        FOREIGN KEY (mouse_id) REFERENCES mouse (mouse_id)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,

    -- Change from_lab to RESTRICT (prevent accidental deletion)
    ADD CONSTRAINT fk_from_lab_id
        FOREIGN KEY (from_lab_id) REFERENCES lab (lab_id)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,

    -- Change to_lab to RESTRICT (prevent accidental deletion)
    ADD CONSTRAINT fk_to_lab_id
        FOREIGN KEY (to_lab_id) REFERENCES lab (lab_id)
            ON DELETE RESTRICT
            ON UPDATE CASCADE;