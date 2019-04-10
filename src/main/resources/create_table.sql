CREATE TABLE users (
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    PRIMARY KEY (username)
);

CREATE TABLE user_roles (
    user_role_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    username VARCHAR(50) NOT NULL,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_role_id),
    FOREIGN KEY (username) REFERENCES users(username)
);

CREATE TABLE lecture (
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    name VARCHAR(50) NOT NULL,
    tag VARCHAR(500) NOT NULL,
    description VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE attachment (
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    filename VARCHAR(255) DEFAULT NULL,
    content_type VARCHAR(255) DEFAULT NULL,
    content BLOB DEFAULT NULL,
    lecture_id INTEGER DEFAULT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (lecture_id) REFERENCES lecture(id) 
);

INSERT INTO users VALUES ('ken', 'pw', 'ken@gmail.com');
INSERT INTO user_roles(username, role) VALUES ('ken', 'ROLE_STUDENT');

INSERT INTO users VALUES ('keith', 'pw', 'keith@gmail.com');
INSERT INTO user_roles(username, role) VALUES ('keith', 'ROLE_TEACHER');

SELECT * FROM users;
SELECT * FROM user_roles;
SELECT * FROM lecture;
SELECT * FROM attachment;