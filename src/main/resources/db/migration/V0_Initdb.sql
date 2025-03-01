create database if not exists care_now;

use care_now;

create table customer(
	customer_id bigint auto_increment not null primary key,
    name_of_customer nvarchar(255),
    email varchar(255),
    phone_number varchar(11),
    city nvarchar(255),
    address nvarchar(255),
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
    special_detail nvarchar(1000)
);

create table image(
	image_id bigint auto_increment not null primary key,
    img_profile nvarchar(255),
    img_certificate nvarchar(255),
    img_cccd nvarchar(255)
);

create table care_taker_feedback(
	feedback_id bigint auto_increment not null primary key,
    feedback nvarchar(255),
    rating int
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