create database if not exists care_now;

use care_now;

create table customer(
	customer_id bigint auto_increment not null primary key,
    name_of_customer nvarchar(255),
    email varchar(255),
    phone_number varchar(11),
    district nvarchar(255),
    ward nvarchar(255),
    account_id bigint
);

create table care_taker(
	care_taker_id bigint auto_increment not null primary key,
    name_of_care_taker nvarchar(255),
    email varchar(255),
    phone_number varchar(11),
    introduce_yourself nvarchar(10000),
    dob date,
    gender varchar(255),
    district nvarchar(255),
    ward nvarchar(255),
    workable_area nvarchar(255),
    experience_year int,
    service_price varchar(255),
    avarage_rating float default 0,
    training_status boolean default false,
    account_id bigint
);

create table admin(
	admin_id bigint auto_increment not null primary key,
    name_of_admin nvarchar(255),
    email varchar(255),
    phone_number varchar(11),
    city nvarchar(255),
    address nvarchar(255),
    account_id bigint
);

create table role(
	role_id bigint auto_increment not null primary key,
    role_name varchar(255)
);

create table account(
	account_id bigint auto_increment not null primary key,
    user_name varchar(255),
    password varchar(255),
    active varchar(10) DEFAULT 'ACTIVE'
);

create table account_role(
	account_role_id bigint auto_increment not null primary key,
	role_id bigint,
    account_id bigint
);

create table care_recipient(
	care_recipient_id bigint auto_increment not null primary key,
    name nvarchar (255),
    gender varchar (10),
    year_old varchar(10),
    special_detail nvarchar(1000),
    customer_id bigint
);

create table image(
	image_id bigint auto_increment not null primary key,
    img_profile nvarchar(255),
    img_certificate nvarchar(255),
    img_cccd nvarchar(255),
    care_taker_id bigint
);

create table care_taker_feedback(
	feedback_id bigint auto_increment not null primary key,
    feedback nvarchar(255),
    rating int,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	customer_id bigint,
    care_taker_id bigint
);

CREATE TABLE calendar (
    calendar_id bigint auto_increment not null primary key,
    day DATE NOT NULL,
    time_to_start TIME,
    time_to_end TIME,
    care_taker_id bigint
);

create table options(
	options_id bigint auto_increment not null primary key,
    name_option nvarchar(255)
);

create table option_details_of_care_taker(
	id bigint auto_increment not null primary key,
    option_details_id bigint,
    care_taker_id bigint
);

create table option_details(
    option_details_id bigint auto_increment not null primary key,
    detail_name nvarchar(255),
    options_id bigint
);

create table booking(
	booking_id bigint auto_increment not null primary key,
	place_name nvarchar(255),
    booking_address nvarchar(255),
    description_place nvarchar(255),
    time_to_start time not null,
    time_to_end time not null,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ,
    service_progress nvarchar(255) DEFAULT 'PENDING',
    job_description nvarchar(1000),
    customer_id bigint,
    care_taker_id bigint,
    care_recipient_id bigint
);

create table payment(
	payment_id bigint auto_increment not null primary key,
    booking_id bigint,
	status boolean,
    price FLOAT,
    transaction_id varchar(255),
    create_at datetime,
    update_at datetime
);

CREATE TABLE IF NOT EXISTS chat_room (
    room_id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    customer_id BIGINT,
    care_taker_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    FOREIGN KEY (care_taker_id) REFERENCES care_taker(care_taker_id)
);

CREATE TABLE IF NOT EXISTS chat_message (
    message_id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    room_id BIGINT,
    sender_id BIGINT,
    sender_type VARCHAR(20),
    content TEXT,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (room_id) REFERENCES chat_room(room_id)
);

ALTER TABLE customer
ADD CONSTRAINT fk_customer_account FOREIGN KEY (account_id) REFERENCES account(account_id) ON DELETE CASCADE;

ALTER TABLE care_taker
ADD CONSTRAINT fk_care_taker_account FOREIGN KEY (account_id) REFERENCES account(account_id) ON DELETE CASCADE;

ALTER TABLE admin
ADD CONSTRAINT fk_admin_account FOREIGN KEY (account_id) REFERENCES account(account_id) ON DELETE CASCADE;

ALTER TABLE account_role
ADD CONSTRAINT fk_account_role_role FOREIGN KEY (role_id) REFERENCES role(role_id) ON DELETE CASCADE,
ADD CONSTRAINT fk_account_role_account FOREIGN KEY (account_id) REFERENCES account(account_id) ON DELETE CASCADE;

ALTER TABLE care_recipient
ADD CONSTRAINT fk_care_recipient_customer FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE CASCADE;

ALTER TABLE care_taker_feedback
ADD CONSTRAINT fk_care_taker_feedback_care_taker FOREIGN KEY (care_taker_id) REFERENCES care_taker(care_taker_id) ON DELETE CASCADE,
ADD CONSTRAINT fk_care_taker_feedback_customer FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE CASCADE;

ALTER TABLE calendar
ADD CONSTRAINT fk_calendar_care_taker FOREIGN KEY (care_taker_id) REFERENCES care_taker(care_taker_id) ON DELETE CASCADE;

ALTER TABLE booking
ADD CONSTRAINT fk_booking_customer FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE CASCADE,
ADD CONSTRAINT fk_booking_care_taker FOREIGN KEY (care_taker_id) REFERENCES care_taker(care_taker_id) ON DELETE CASCADE;

ALTER TABLE payment
ADD CONSTRAINT fk_payment_booking FOREIGN KEY (booking_id) REFERENCES booking(booking_id) ON DELETE CASCADE;

ALTER TABLE image
ADD CONSTRAINT fk_image_care_taker FOREIGN KEY (care_taker_id) REFERENCES care_taker(care_taker_id) ON DELETE CASCADE;

ALTER TABLE option_details_of_care_taker
ADD CONSTRAINT fk_option_details_of_care_taker_option_details FOREIGN KEY (option_details_id) REFERENCES option_details(option_details_id) ON DELETE CASCADE,
ADD CONSTRAINT fk_option_details_of_care_taker_care_taker FOREIGN KEY (care_taker_id) REFERENCES care_taker(care_taker_id) ON DELETE CASCADE;

ALTER TABLE option_details
ADD CONSTRAINT fk_option_details_options FOREIGN KEY (options_id) REFERENCES options(options_id) ON DELETE CASCADE;

ALTER TABLE booking
ADD CONSTRAINT fk_booking_care_recipient FOREIGN KEY (care_recipient_id) REFERENCES care_recipient(care_recipient_id) ON DELETE SET NULL;
