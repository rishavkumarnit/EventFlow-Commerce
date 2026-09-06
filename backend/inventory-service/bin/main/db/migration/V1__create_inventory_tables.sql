CREATE TABLE inventory_items (
    product_id UUID PRIMARY KEY,
    available_quantity INTEGER NOT NULL CHECK (available_quantity >= 0),
    reserved_quantity INTEGER NOT NULL DEFAULT 0 CHECK (reserved_quantity >= 0)
);

CREATE TABLE processed_events (
    event_id UUID PRIMARY KEY,
    processed_at TIMESTAMPTZ NOT NULL
);

INSERT INTO inventory_items (product_id, available_quantity, reserved_quantity)
VALUES ('123e4567-e89b-12d3-a456-426614174000', 100, 0);
