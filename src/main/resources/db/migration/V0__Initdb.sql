create database if not exists care_now;

use care_now;

create table customer(
	customer_id bigint auto_increment not null primary key,
    name_of_customer nvarchar(255),
    email varchar(255),
    phone_number varchar(11),
    city nvarchar(255),
    address nvarchar(255),
    img_profile nvarchar(255),
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
    city nvarchar(255),
    workable_area nvarchar(255),
    experience_year int,
    salary varchar(255),
    avarage_rating float,
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
    password varchar(255)
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
    phone_number varchar(255),
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
	customer_id bigint,
    care_taker_id bigint
);

CREATE TABLE calendar (
    calendar_id bigint auto_increment not null primary key,
    day DATE NOT NULL,
    time_to_start TIME NOT NULL,
    time_to_end TIME NOT NULL,
    CHECK (time_to_start < time_to_end),
    care_taker_id bigint
);

create table options(
	options_id bigint auto_increment not null primary key,
    name_option nvarchar(255)
);

create table option_of_care_taker(
	id bigint auto_increment not null primary key,
    option_id bigint,
    care_taker_id bigint
);

create table booking(
	booking_id bigint auto_increment not null primary key,
    booking_address nvarchar(255),
	day date not null,
    time_to_start time not null,
    time_to_end time not null,
    service_progress nvarchar(255),
    customer_id bigint,
    care_taker_id bigint
);

create table payment(
	payment_id bigint auto_increment not null primary key,
    booking_id bigint,
	status boolean,
    price varchar(255),
    transaction_id varchar(255),
    create_at datetime,
    update_at datetime
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

ALTER TABLE option_of_care_taker
ADD CONSTRAINT fk_option_of_care_taker_options FOREIGN KEY (option_id) REFERENCES options(options_id) ON DELETE CASCADE,
ADD CONSTRAINT fk_option_of_care_taker_care_taker FOREIGN KEY (care_taker_id) REFERENCES care_taker(care_taker_id) ON DELETE CASCADE;

ALTER TABLE booking
ADD CONSTRAINT fk_booking_customer FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE CASCADE,
ADD CONSTRAINT fk_booking_care_taker FOREIGN KEY (care_taker_id) REFERENCES care_taker(care_taker_id) ON DELETE CASCADE;

ALTER TABLE payment
ADD CONSTRAINT fk_payment_booking FOREIGN KEY (booking_id) REFERENCES booking(booking_id) ON DELETE CASCADE;

ALTER TABLE image
ADD CONSTRAINT fk_image_care_taker FOREIGN KEY (care_taker_id) REFERENCES care_taker(care_taker_id) ON DELETE CASCADE;
