-- Thêm cột location_type vào bảng booking
ALTER TABLE booking
ADD COLUMN location_type VARCHAR(50);

-- Cập nhật dữ liệu hiện tại với giá trị mặc định
UPDATE booking
SET location_type = 'HOSPITAL' 
WHERE booking_id > 0; 