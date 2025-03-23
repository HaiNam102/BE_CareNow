CREATE TABLE booking_day (
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    day DATE NOT NULL,
    booking_id BIGINT
);

ALTER TABLE booking_day
ADD CONSTRAINT fk_booking_day_booking FOREIGN KEY (booking_id) REFERENCES booking(booking_id) ON DELETE CASCADE;

