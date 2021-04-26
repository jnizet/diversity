ALTER TABLE indicator
    ADD COLUMN rank int;
create sequence indicator_rank_seq start with 1;

UPDATE indicator
SET rank = nextval('indicator_rank_seq') where rank is null;

ALTER TABLE indicator ALTER COLUMN rank SET NOT NULL;
