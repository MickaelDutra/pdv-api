CREATE TABLE IF NOT EXISTS payment (
    id BIGSERIAL PRIMARY KEY,
    sale_id BIGINT NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    gross_amount NUMERIC(14,2) NOT NULL,
    discount_amount NUMERIC(14,2) NOT NULL,
    net_amount NUMERIC(14,2) NOT NULL,
    paid_amount NUMERIC(14,2) DEFAULT 0,
    status VARCHAR(20) NOT NULL,
    transaction_id VARCHAR(100),
    card_brand VARCHAR(50),
    card_last_digits VARCHAR(4),
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    processed_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_payment_sale FOREIGN KEY (sale_id) REFERENCES sale(id)
);

CREATE INDEX IF NOT EXISTS idx_payment_sale_id ON payment(sale_id);
CREATE INDEX IF NOT EXISTS idx_payment_status ON payment(status);
CREATE INDEX IF NOT EXISTS idx_payment_transaction_id ON payment(transaction_id);