UPDATE donations
SET frequency = CASE
                    WHEN id = 1 THEN 'MONTHLY'
                    WHEN id = 2 THEN 'YEARLY'
    END
WHERE id IN (1, 2);