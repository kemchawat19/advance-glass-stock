-- Create Product Table
CREATE TABLE IF NOT EXISTS product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(255) NOT NULL,
    product_group VARCHAR(100),
    product_unit VARCHAR(50),
    product_status VARCHAR(10),
    create_time_stamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time_stamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create Stock Table
CREATE TABLE IF NOT EXISTS stock (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL UNIQUE,
    quantity INT NOT NULL DEFAULT 0,
    create_time_stamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time_stamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);

-- Create Entry Table
CREATE TABLE IF NOT EXISTS entry (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    entry_number VARCHAR(20) NOT NULL,
    entry_type VARCHAR(20) NOT NULL,
    entry_date TIMESTAMP NOT NULL,
    job_number VARCHAR(20),
    reference_number VARCHAR(20),
    process_status VARCHAR(10),
    confirmed_date TIMESTAMP,
    supplier_id INT,
    supplier_name VARCHAR(255),
    supplier_invoice VARCHAR(20),
    employee_id INT,
    employee_name VARCHAR(100),
    description VARCHAR(100),
    create_time_stamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time_stamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE (entry_type, entry_number)
);

CREATE SEQUENCE IF NOT EXISTS receipt_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS request_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS return_seq START WITH 1 INCREMENT BY 1;

-- Create Entry Detail Table
CREATE TABLE IF NOT EXISTS entry_detail (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    entry_id BIGINT NOT NULL,
    stock_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit VARCHAR(20),
    unit_price DECIMAL(10,2),
    amount DECIMAL(10,2),
    create_time_stamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time_stamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (entry_id) REFERENCES entry(id) ON DELETE CASCADE,
    FOREIGN KEY (stock_id) REFERENCES stock(id) ON DELETE CASCADE
);
