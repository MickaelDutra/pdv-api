CREATE TABLE IF NOT EXISTS sale (
    id BIGSERIAL PRIMARY KEY,
    customer_name VARCHAR(255),
    payment_method VARCHAR(100) NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    total_amount NUMERIC(14,2) NOT NULL,
    discount NUMERIC(14,2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sale_item (
    id BIGSERIAL PRIMARY KEY,
    sale_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price NUMERIC(14,2) NOT NULL,
    total NUMERIC(14,2) NOT NULL,
    discount NUMERIC(14,2) NOT NULL,
    CONSTRAINT fk_sale_item_sale FOREIGN KEY (sale_id) REFERENCES sale(id),
    CONSTRAINT fk_sale_item_product FOREIGN KEY (product_id) REFERENCES product(id)
);
