INSERT INTO users (name, surname, email, password, admin)
VALUES ('Ksenija', 'Pet', 'kp@gmail.com', 'pass', 0);

INSERT INTO users (name, surname, email, password, admin)
VALUES ('Marko', 'Jovanovic', 'marko.j@gmail.com', 'pass123', 1);

INSERT INTO users (name, surname, email, password, admin)
VALUES ('Ana', 'Nikolic', 'ana.n@gmail.com', 'secret', 0);


INSERT INTO subjects (code, name)
VALUES ('CS101', 'Computer Science 101');

INSERT INTO subjects (code, name)
VALUES ('MATH201', 'Discrete Mathematics');

INSERT INTO subjects (code, name)
VALUES ('BUS301', 'Event Management');


INSERT INTO halls (name, capacity, location, type, equipment)
VALUES ('63', 300, 'Nova 2', 'OFFICE', 'Projector,Sound');

INSERT INTO halls (name, capacity, location, type, equipment)
VALUES ('B009', 40, 'Stara 1', 'AMPHITHEATRE', 'Smartboard,TV');

INSERT INTO halls (name, capacity, location, type, equipment)
VALUES ('12', 30, 'Stara 1', 'CLASSROOM', 'TV,Speakerphone');


INSERT INTO events (name, type, description, capacity, subject_id)
VALUES ('Spring Boot Intro', 'WORKSHOP', 'Basics of Spring Boot 4 + REST', 40, 1);

INSERT INTO events (name, type, description, capacity, subject_id)
VALUES ('Discrete Math Seminar', 'CONFERENCE', 'Graph theory and logic', 120, 2);

INSERT INTO events (name, type, description, capacity, subject_id)
VALUES ('Planner Kickoff', 'MEETING', 'Initial planning session', 12, 3);