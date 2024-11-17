-- Xóa dữ liệu cũ
DELETE FROM role_permission;
DELETE FROM permissions;

-- Thêm dữ liệu mẫu vào bảng permissions
INSERT INTO permissions (name, description) VALUES
    ('VIEW_STATISTICS', 'Permission to view statistics'),
    ('MANAGE_ROLES', 'Permission to manage user roles'),
    ('MANAGE_USERS', 'Permission to manage user accounts'),
    ('MANAGE_CATEGORIES', 'Permission to manage categories'),
    ('MANAGE_BENEFICIARIES', 'Permission to manage payment recipients'),
    ('MANAGE_DONATIONS', 'Permission to manage payment recipients'),
    ('MANAGE_CAMPAIGNS', 'Permission to manage charity campaigns');


