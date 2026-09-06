-- Keep inventory IDs aligned with Product Catalog so every frontend item can be ordered.
INSERT INTO inventory_items (product_id, available_quantity, reserved_quantity)
VALUES
    ('223e4567-e89b-12d3-a456-426614174001', 75, 0),
    ('323e4567-e89b-12d3-a456-426614174002', 50, 0)
ON CONFLICT (product_id) DO NOTHING;
