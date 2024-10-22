ALTER TABLE campaigns
    DROP COLUMN is_approved ;

ALTER TABLE campaigns
    ADD COLUMN status SMALLINT NOT NULL DEFAULT 0 ;

UPDATE "campaigns"
    SET "status" = CASE
                    WHEN "id" = 1 THEN 1
                    WHEN "id" = 2 THEN 1
    END
WHERE "id" IN (1, 2);






