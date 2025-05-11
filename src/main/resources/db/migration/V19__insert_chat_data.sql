-- Insert sample chat rooms
INSERT INTO chat_room (customer_id, care_taker_id) VALUES
(1, 1),
(2, 1),
(1, 2);

-- Insert sample chat messages
INSERT INTO chat_message (room_id, sender_id, sender_type, content) VALUES
(1, 1, 'CUSTOMER', 'Hello, I would like to book your service'),
(1, 1, 'CARE_TAKER', 'Hi, sure! What kind of service do you need?'),
(2, 2, 'CUSTOMER', 'Are you available next week?'),
(2, 1, 'CARE_TAKER', 'Yes, I am available on Monday and Wednesday'),
(3, 1, 'CUSTOMER', 'What is your hourly rate?'),
(3, 2, 'CARE_TAKER', 'My rate is $20 per hour');