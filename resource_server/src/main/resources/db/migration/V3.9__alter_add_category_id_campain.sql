-- Check if category_id column exists in campaigns table
DO
$$
BEGIN
    IF
NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'campaigns'
          AND column_name = 'category_id'
    ) THEN
        -- Add category_id column to campaigns table
ALTER TABLE campaigns
    ADD COLUMN category_id INT;

UPDATE campaigns
SET category_id = (SELECT id
                   FROM categories
                   ORDER BY id LIMIT 1
    )
WHERE category_id IS NULL;

-- Finally, alter the column to be NOT NULL
ALTER TABLE campaigns
    ALTER COLUMN category_id SET NOT NULL;
END IF;
END $$;
