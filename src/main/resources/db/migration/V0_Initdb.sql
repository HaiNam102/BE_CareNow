create database if not exists nanny_now;

use nanny_now;

create table customer(
	user_id bigint auto_increment not null primary key,
    name_of_user nvarchar(255),
    email varchar(255),
    phone_number varchar(11),
    city nvarchar(255),
    address nvarchar(255),
    special_request nvarchar(1000),
    account_id bigint
);

create table nanny(
	nanny_id bigint auto_increment not null primary key,
    name_of_nanny nvarchar(255),
    email varchar(255),
    phone_number varchar(11),
    introduce_yourself nvarchar(10000),
    dob date,
    gender varchar(255),
    city nvarchar(255),
    workable_area nvarchar(255),
    experience_year int,
    salary varchar(255),
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

ALTER TABLE customer
ADD CONSTRAINT fk_customer_account FOREIGN KEY (account_id) REFERENCES account(account_id) ON DELETE CASCADE;

ALTER TABLE nanny
ADD CONSTRAINT fk_nanny_account FOREIGN KEY (account_id) REFERENCES account(account_id) ON DELETE CASCADE;

ALTER TABLE admin
ADD CONSTRAINT fk_admin_account FOREIGN KEY (account_id) REFERENCES account(account_id) ON DELETE CASCADE;

ALTER TABLE account_role
ADD CONSTRAINT fk_account_role_role FOREIGN KEY (role_id) REFERENCES role(role_id) ON DELETE CASCADE,
ADD CONSTRAINT fk_account_role_account FOREIGN KEY (account_id) REFERENCES account(account_id) ON DELETE CASCADE;