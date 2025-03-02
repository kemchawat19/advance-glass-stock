-- Insert Products
MERGE INTO product (product_name, product_group, product_unit, product_status, create_time_stamp, update_time_stamp)
    KEY (product_name)  -- Avoid duplicates based on product name
    VALUES
        ('มือจับบานเลื่อนสีชา', '00HWC', 'ชุด', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
        ('โช๊คอัพสวิง', '00HWL', 'ตัว', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
        ('สักหลาดบานเลื่อน', '00HWK', 'ม้วน', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
        ('สักหลาดบานสวิง', '00HWK', 'ม้วน', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
