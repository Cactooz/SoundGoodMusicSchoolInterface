CREATE TABLE genre (
 genre_id SERIAL NOT NULL,
 genre VARCHAR(100) UNIQUE NOT NULL
);

ALTER TABLE genre ADD CONSTRAINT PK_genre PRIMARY KEY (genre_id);


CREATE TABLE instrument (
 instrument_id SERIAL NOT NULL,
 instrument VARCHAR(100) UNIQUE NOT NULL
);

ALTER TABLE instrument ADD CONSTRAINT PK_instrument PRIMARY KEY (instrument_id);


CREATE TABLE instrument_brand (
 instrument_brand_id SERIAL NOT NULL,
 brand_name VARCHAR(100) UNIQUE NOT NULL
);

ALTER TABLE instrument_brand ADD CONSTRAINT PK_instrument_brand PRIMARY KEY (instrument_brand_id);


CREATE TABLE lesson_type (
 lesson_type_id SERIAL NOT NULL,
 lesson_type VARCHAR(100) UNIQUE NOT NULL
);

ALTER TABLE lesson_type ADD CONSTRAINT PK_lesson_type PRIMARY KEY (lesson_type_id);


CREATE TABLE person (
 person_id SERIAL NOT NULL,
 personal_number VARCHAR(12) UNIQUE NOT NULL,
 first_name VARCHAR(100) NOT NULL,
 last_name VARCHAR(100),
 phone VARCHAR(15),
 email VARCHAR(100) UNIQUE,
 street VARCHAR(100),
 house INT,
 city VARCHAR(100),
 zip VARCHAR(10)
);

ALTER TABLE person ADD CONSTRAINT PK_person PRIMARY KEY (person_id);


CREATE TABLE physical_instrument (
 physical_instrument_id SERIAL NOT NULL,
 id VARCHAR(100) UNIQUE NOT NULL,
 cost INT NOT NULL,
 instrument_id SERIAL NOT NULL,
 instrument_brand_id SERIAL
);

ALTER TABLE physical_instrument ADD CONSTRAINT PK_physical_instrument PRIMARY KEY (physical_instrument_id);


CREATE TABLE skill_level (
 skill_level_id SERIAL NOT NULL,
 skill_level VARCHAR(100) UNIQUE NOT NULL
);

ALTER TABLE skill_level ADD CONSTRAINT PK_skill_level PRIMARY KEY (skill_level_id);


CREATE TABLE student (
 student_id INT NOT NULL,
 enrolled BIT(1) NOT NULL
);

ALTER TABLE student ADD CONSTRAINT PK_student PRIMARY KEY (student_id);


CREATE TABLE student_skill (
 student_id INT NOT NULL,
 skill_level_id INT NOT NULL,
 instrument_id INT NOT NULL
);

ALTER TABLE student_skill ADD CONSTRAINT PK_student_skill PRIMARY KEY (student_id,skill_level_id,instrument_id);


CREATE TABLE contact_person (
 contact_person_id INT NOT NULL,
 student_id INT NOT NULL
);

ALTER TABLE contact_person ADD CONSTRAINT PK_contact_person PRIMARY KEY (contact_person_id,student_id);


CREATE TABLE instructor (
 instructor_id INT NOT NULL,
 ensemble BIT(1) NOT NULL
);

ALTER TABLE instructor ADD CONSTRAINT PK_instructor PRIMARY KEY (instructor_id);


CREATE TABLE price_list (
 price_list_id SERIAL NOT NULL,
 price INT NOT NULL,
 pay INT NOT NULL,
 discount INT NOT NULL,
 skill_level_id INT,
 lesson_type_id INT NOT NULL
);

ALTER TABLE price_list ADD CONSTRAINT PK_price_list PRIMARY KEY (price_list_id);


CREATE TABLE rental (
 rental_id SERIAL NOT NULL,
 start_date DATE NOT NULL,
 end_date DATE NOT NULL,
 student_id INT NOT NULL,
 physical_instrument_id SERIAL
);

ALTER TABLE rental ADD CONSTRAINT PK_rental PRIMARY KEY (rental_id);


CREATE TABLE sibling (
 student_id INT NOT NULL,
 sibling_id INT NOT NULL
);

ALTER TABLE sibling ADD CONSTRAINT PK_sibling PRIMARY KEY (student_id,sibling_id);


CREATE TABLE teaches_instrument (
 instructor_id INT NOT NULL,
 instrument_id INT NOT NULL
);

ALTER TABLE teaches_instrument ADD CONSTRAINT PK_teaches_instrument PRIMARY KEY (instructor_id,instrument_id);


CREATE TABLE timeslot (
 timeslot_id SERIAL NOT NULL,
 date DATE NOT NULL,
 start_time TIME(4) NOT NULL,
 end_time TIME(4) NOT NULL,
 instructor_id INT NOT NULL
);

ALTER TABLE timeslot ADD CONSTRAINT PK_timeslot PRIMARY KEY (timeslot_id);


CREATE TABLE ensemble (
 ensemble_id SERIAL NOT NULL,
 min_students INT NOT NULL,
 max_students INT NOT NULL,
 genre_id INT NOT NULL,
 timeslot_id INT NOT NULL,
 price_list_id INT NOT NULL
);

ALTER TABLE ensemble ADD CONSTRAINT PK_ensemble PRIMARY KEY (ensemble_id);


CREATE TABLE ensemble_student (
 student_id INT NOT NULL,
 ensemble_id SERIAL NOT NULL
);

ALTER TABLE ensemble_student ADD CONSTRAINT PK_ensemble_student PRIMARY KEY (student_id,ensemble_id);


CREATE TABLE group_lesson (
 group_lesson_id SERIAL NOT NULL,
 min_students INT NOT NULL,
 max_students INT NOT NULL,
 timeslot_id INT NOT NULL,
 instrument_id INT NOT NULL,
 skill_level_id INT NOT NULL,
 price_list_id INT NOT NULL
);

ALTER TABLE group_lesson ADD CONSTRAINT PK_group_lesson PRIMARY KEY (group_lesson_id);


CREATE TABLE group_lesson_student (
 student_id INT NOT NULL,
 group_lesson_id SERIAL NOT NULL
);

ALTER TABLE group_lesson_student ADD CONSTRAINT PK_group_lesson_student PRIMARY KEY (student_id,group_lesson_id);


CREATE TABLE individual_lesson (
 individual_lesson_id SERIAL NOT NULL,
 student_id INT NOT NULL,
 skill_level_id INT NOT NULL,
 instrument_id INT NOT NULL,
 timeslot_id INT NOT NULL,
 price_list_id INT NOT NULL
);

ALTER TABLE individual_lesson ADD CONSTRAINT PK_individual_lesson PRIMARY KEY (individual_lesson_id);


ALTER TABLE physical_instrument ADD CONSTRAINT FK_physical_instrument_0 FOREIGN KEY (instrument_id) REFERENCES instrument (instrument_id);
ALTER TABLE physical_instrument ADD CONSTRAINT FK_physical_instrument_1 FOREIGN KEY (instrument_brand_id) REFERENCES instrument_brand (instrument_brand_id);


ALTER TABLE student ADD CONSTRAINT FK_student_0 FOREIGN KEY (student_id) REFERENCES person (person_id);


ALTER TABLE student_skill ADD CONSTRAINT FK_student_skill_0 FOREIGN KEY (student_id) REFERENCES student (student_id);
ALTER TABLE student_skill ADD CONSTRAINT FK_student_skill_1 FOREIGN KEY (skill_level_id) REFERENCES skill_level (skill_level_id);
ALTER TABLE student_skill ADD CONSTRAINT FK_student_skill_2 FOREIGN KEY (instrument_id) REFERENCES instrument (instrument_id);


ALTER TABLE contact_person ADD CONSTRAINT FK_contact_person_0 FOREIGN KEY (contact_person_id) REFERENCES person (person_id);
ALTER TABLE contact_person ADD CONSTRAINT FK_contact_person_1 FOREIGN KEY (student_id) REFERENCES student (student_id);


ALTER TABLE instructor ADD CONSTRAINT FK_instructor_0 FOREIGN KEY (instructor_id) REFERENCES person (person_id);


ALTER TABLE price_list ADD CONSTRAINT FK_price_list_0 FOREIGN KEY (skill_level_id) REFERENCES skill_level (skill_level_id);
ALTER TABLE price_list ADD CONSTRAINT FK_price_list_1 FOREIGN KEY (lesson_type_id) REFERENCES lesson_type (lesson_type_id);


ALTER TABLE rental ADD CONSTRAINT FK_rental_0 FOREIGN KEY (student_id) REFERENCES student (student_id);
ALTER TABLE rental ADD CONSTRAINT FK_rental_1 FOREIGN KEY (physical_instrument_id) REFERENCES physical_instrument (physical_instrument_id);


ALTER TABLE sibling ADD CONSTRAINT FK_sibling_0 FOREIGN KEY (student_id) REFERENCES student (student_id);
ALTER TABLE sibling ADD CONSTRAINT FK_sibling_1 FOREIGN KEY (sibling_id) REFERENCES student (student_id);


ALTER TABLE teaches_instrument ADD CONSTRAINT FK_teaches_instrument_0 FOREIGN KEY (instructor_id) REFERENCES instructor (instructor_id);
ALTER TABLE teaches_instrument ADD CONSTRAINT FK_teaches_instrument_1 FOREIGN KEY (instrument_id) REFERENCES instrument (instrument_id);


ALTER TABLE timeslot ADD CONSTRAINT FK_timeslot_0 FOREIGN KEY (instructor_id) REFERENCES instructor (instructor_id);


ALTER TABLE ensemble ADD CONSTRAINT FK_ensemble_0 FOREIGN KEY (genre_id) REFERENCES genre (genre_id);
ALTER TABLE ensemble ADD CONSTRAINT FK_ensemble_1 FOREIGN KEY (timeslot_id) REFERENCES timeslot (timeslot_id);
ALTER TABLE ensemble ADD CONSTRAINT FK_ensemble_2 FOREIGN KEY (price_list_id) REFERENCES price_list (price_list_id);


ALTER TABLE ensemble_student ADD CONSTRAINT FK_ensemble_student_0 FOREIGN KEY (student_id) REFERENCES student (student_id);
ALTER TABLE ensemble_student ADD CONSTRAINT FK_ensemble_student_1 FOREIGN KEY (ensemble_id) REFERENCES ensemble (ensemble_id);


ALTER TABLE group_lesson ADD CONSTRAINT FK_group_lesson_0 FOREIGN KEY (timeslot_id) REFERENCES timeslot (timeslot_id);
ALTER TABLE group_lesson ADD CONSTRAINT FK_group_lesson_1 FOREIGN KEY (instrument_id) REFERENCES instrument (instrument_id);
ALTER TABLE group_lesson ADD CONSTRAINT FK_group_lesson_2 FOREIGN KEY (skill_level_id) REFERENCES skill_level (skill_level_id);
ALTER TABLE group_lesson ADD CONSTRAINT FK_group_lesson_3 FOREIGN KEY (price_list_id) REFERENCES price_list (price_list_id);


ALTER TABLE group_lesson_student ADD CONSTRAINT FK_group_lesson_student_0 FOREIGN KEY (student_id) REFERENCES student (student_id);
ALTER TABLE group_lesson_student ADD CONSTRAINT FK_group_lesson_student_1 FOREIGN KEY (group_lesson_id) REFERENCES group_lesson (group_lesson_id);


ALTER TABLE individual_lesson ADD CONSTRAINT FK_individual_lesson_0 FOREIGN KEY (student_id) REFERENCES student (student_id);
ALTER TABLE individual_lesson ADD CONSTRAINT FK_individual_lesson_1 FOREIGN KEY (skill_level_id) REFERENCES skill_level (skill_level_id);
ALTER TABLE individual_lesson ADD CONSTRAINT FK_individual_lesson_2 FOREIGN KEY (instrument_id) REFERENCES instrument (instrument_id);
ALTER TABLE individual_lesson ADD CONSTRAINT FK_individual_lesson_3 FOREIGN KEY (timeslot_id) REFERENCES timeslot (timeslot_id);
ALTER TABLE individual_lesson ADD CONSTRAINT FK_individual_lesson_4 FOREIGN KEY (price_list_id) REFERENCES price_list (price_list_id);


