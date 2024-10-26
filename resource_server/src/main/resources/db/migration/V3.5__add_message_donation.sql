ALTER TABLE "donations"
    ADD COLUMN "message" VARCHAR(255),
    ADD COLUMN "transaction_id" VARCHAR(255),
    ADD COLUMN "status" SMALLINT NOT NULL DEFAULT 0 ;