USE `mouse-colony-app`;

-- Add name column to litter table
ALTER TABLE litter
    ADD COLUMN name VARCHAR(255) NOT NULL;