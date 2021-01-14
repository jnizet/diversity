ALTER TABLE indicator
ADD COLUMN is_rounded boolean;

UPDATE indicator
SET is_rounded = false where is_rounded is null;

ALTER TABLE indicator ALTER COLUMN is_rounded SET NOT NULL;
