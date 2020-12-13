INSERT INTO users
(user_name, user_password, user_admin) VALUES
('vincent', '$2a$12$3btNOlosyVqjblWIfZxOMeUfRsB5k8kYT.lT/OInDFWjD8b2/a.E2', true),
('william', '$2a$12$3btNOlosyVqjblWIfZxOMeUfRsB5k8kYT.lT/OInDFWjD8b2/a.E2', false);

INSERT INTO card_templates
(user_id, card_template_description, card_front_template, card_back_template) VALUES
(1, 'mandarin', 'front', 'back'),
(2, 'cantonese', 'front', 'back');

INSERT INTO cards
(card_template_id, card_data) VALUES
(1, 'some card data'),
(2, 'some card data');