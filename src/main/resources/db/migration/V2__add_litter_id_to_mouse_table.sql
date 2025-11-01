USE `mouse-colony-app`;

-- Add litter_id column to mouse table
ALTER TABLE mouse
    ADD COLUMN litter_id BIGINT NULL,
    ADD CONSTRAINT fk_mouse_litter_id
        FOREIGN KEY (litter_id) REFERENCES litter (litter_id)
            ON DELETE SET NULL;  -- If litter record deleted, mice remain but litter_id is NULL