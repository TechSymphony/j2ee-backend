ALTER TABLE beneficiaries
DROP COLUMN verification_status;

ALTER TABLE beneficiaries
    ADD COLUMN verification_status SMALLINT NOT NULL DEFAULT 0 ;
