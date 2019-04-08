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

CREATE TABLE courses (
    courseId INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    courseName VARCHAR(50) NOT NULL,
    courseDescription VARCHAR(500) NOT NULL,
    PRIMARY KEY (courseId)
);

INSERT INTO users VALUES ('ken', 'pw', 'ken@gmail.com');
INSERT INTO user_roles(username, role) VALUES ('ken', 'ROLE_STUDENT');

INSERT INTO users VALUES ('keith', 'pw', 'keith@gmail.com');
INSERT INTO user_roles(username, role) VALUES ('keith', 'ROLE_TEACHER');

SELECT * FROM users;
SELECT * FROM user_roles;