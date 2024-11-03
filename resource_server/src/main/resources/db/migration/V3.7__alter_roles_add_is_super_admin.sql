ALTER TABLE roles ADD COLUMN is_super_admin BOOLEAN DEFAULT false;

UPDATE roles SET is_super_admin = true WHERE name = 'admin';
