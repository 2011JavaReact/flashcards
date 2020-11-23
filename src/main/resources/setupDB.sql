CREATE TABLE users (
  user_id SERIAL PRIMARY KEY,
  user_name VARCHAR(32) NOT NULL UNIQUE,
  user_password VARCHAR(256) NOT NULL,
  user_admin BOOLEAN NOT NULL
);

CREATE TABLE card_templates (
  card_template_id SERIAL PRIMARY KEY,
  user_id INT NOT NULL,
  card_template_description VARCHAR(256) NOT NULL,
  card_front_template TEXT NOT NULL,
  card_back_template TEXT NOT NULL,

  FOREIGN KEY (user_id) REFERENCES users(user_id)
    ON DELETE CASCADE
    ON UPDATE RESTRICT
);

CREATE TABLE cards (
  card_id SERIAL PRIMARY KEY,
  card_template_id INT NOT NULL,
  card_data TEXT NOT NULL,

  FOREIGN KEY (card_template_id) REFERENCES card_templates(card_template_id)
    ON DELETE CASCADE
    ON UPDATE RESTRICT
);