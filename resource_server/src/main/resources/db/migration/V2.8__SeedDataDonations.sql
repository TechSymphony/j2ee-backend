UPDATE donations
SET frequency = CASE
    WHEN id = 1 THEN 'MONTHLY'
    WHEN id = 2 THEN 'YEARLY'
END
WHERE id IN (1, 2);


INSERT INTO donations (donor_id, campaign_id, amount_base, amount_total, donation_date, frequency) VALUES
    ( 1, 1, 5000, 1000000, '2024-01-01', 'MONTHLY'),
    ( 2, 2, 5000, 1000000, '2024-02-12', 'YEARLY'),
    ( 3, 2, 5000, 1000000, '2024-01-11', 'MONTHLY'),
    ( 2, 1, 5000, 1000000, '2024-02-11', 'YEARLY'),
    ( 1, 2, 5000, 1000000, '2024-03-5', 'MONTHLY'),
    ( 2, 1, 5000, 1000000, '2024-01-18', 'YEARLY'),
    ( 3, 2, 5000, 1000000, '2024-05-21', 'MONTHLY'),
    ( 1, 1, 5000, 1000000, '2024-04-02', 'YEARLY'),
    ( 2, 2, 5000, 1000000, '2024-06-12', 'MONTHLY'),
    ( 3, 1, 50000, 2000000, '2024-09-01', 'YEARLY'),
    ( 3, 1, 5000, 100000, '2024-10-01', 'MONTHLY'),
    ( 3, 1, 5000, 1000000, '2024-9-21', 'YEARLY'),
    ( 1, 1, 5000, 1000000, '2024-07-25', 'MONTHLY'),
    ( 1, 2, 5000, 1000000, '2024-07-16', 'YEARLY'),
    ( 2, 2, 5000, 1000000, '2024-05-14', 'MONTHLY'),
    ( 2, 2, 50000, 10000000, '2024-06-01', 'YEARLY'),
    ( 3, 1, 5000, 100000, '2024-02-02', 'MONTHLY'),
    ( 3, 2, 5000, 1000000, '2024-03-11', 'YEARLY');