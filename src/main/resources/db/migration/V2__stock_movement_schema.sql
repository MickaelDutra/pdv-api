CREATE TABLE IF NOT EXISTS stock_movement (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    type VARCHAR(20) NOT NULL,
    quantity INTEGER NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_stock_movement_product FOREIGN KEY (product_id) REFERENCES product(id)
);
