--Number of lessons by month
CREATE VIEW all_lessons AS
SELECT EXTRACT(YEAR FROM date) AS year,
EXTRACT(MONTH FROM date) AS month,
COUNT(timeslot_id) AS total,
SUM(CASE WHEN timeslot_id IN (SELECT timeslot_id FROM individual_lesson) THEN 1 ELSE 0 END) AS individual,
SUM(CASE WHEN timeslot_id IN (SELECT timeslot_id FROM group_lesson) THEN 1 ELSE 0 END) AS group,
SUM(CASE WHEN timeslot_id IN (SELECT timeslot_id FROM ensemble) THEN 1 ELSE 0 END) AS ensemble
FROM timeslot
GROUP BY year, month
ORDER BY month;

--Get number of siblings
CREATE VIEW total_siblings AS
SELECT COUNT(*) AS students, siblings
FROM (SELECT student_id, SUM(CASE WHEN student_id IN (SELECT student_id FROM student) THEN 1 ELSE 0 END) AS siblings
FROM sibling GROUP BY student_id) AS x
GROUP BY siblings
UNION SELECT COUNT(*), 0
FROM student WHERE student_id
NOT IN (SELECT student_id from sibling)
ORDER BY siblings;

--Get amount of instructors lessons for the current month
CREATE VIEW instructor_lessons AS
SELECT p.personal_number, p.first_name, p.last_name, COUNT(timeslot_id) AS lessons
FROM timeslot AS ts
NATURAL JOIN instructor AS i
INNER JOIN person AS p ON i.instructor_id=p.person_id
WHERE (SELECT EXTRACT(MONTH FROM ts.date) = EXTRACT(MONTH FROM CURRENT_DATE))
GROUP BY p.personal_number, p.first_name, p.last_name
ORDER BY COUNT(timeslot_id) DESC;

--Gets the ensemble lessons this week with amount of spots left
CREATE MATERIALIZED VIEW ensembles_week AS
SELECT ts.date,
CASE WHEN EXTRACT(DOW FROM ts.date) = 0 THEN 'Monday'
	WHEN EXTRACT(DOW FROM ts.date) = 1 THEN 'Tuesday'
	WHEN EXTRACT(DOW FROM ts.date) = 2 THEN 'Wednesday'
	WHEN EXTRACT(DOW FROM ts.date) = 3 THEN 'Thursday'
	WHEN EXTRACT(DOW FROM ts.date) = 4 THEN 'Friday'
	WHEN EXTRACT(DOW FROM ts.date) = 5 THEN 'Saturday'
	WHEN EXTRACT(DOW FROM ts.date) = 6 THEN 'Sunday'
END AS day, g.genre, 
CASE WHEN e.max_students - COUNT(student_id) FILTER (WHERE student_id IN (SELECT student_id FROM ensemble_student)) > 2 THEN '2+ spots left'
	WHEN e.max_students - COUNT(student_id) FILTER (WHERE student_id IN (SELECT student_id FROM ensemble_student)) = 2 THEN '2 spots left'
	WHEN e.max_students - COUNT(student_id) FILTER (WHERE student_id IN (SELECT student_id FROM ensemble_student)) = 1 THEN '1 spot left'
	WHEN e.max_students - COUNT(student_id) FILTER (WHERE student_id IN (SELECT student_id FROM ensemble_student)) = 0 THEN 'Fully booked'
	WHEN e.max_students - COUNT(student_id) FILTER (WHERE student_id IN (SELECT student_id FROM ensemble_student)) < 0 THEN 'Over booked'
END AS spots
FROM timeslot AS ts
NATURAL JOIN ensemble AS e
NATURAL JOIN genre AS g
NATURAL JOIN ensemble_student AS es
WHERE (SELECT EXTRACT(WEEK FROM ts.date) = EXTRACT(WEEK FROM CURRENT_DATE + INTERVAL '1 WEEK'))
GROUP BY ts.date, g.genre, e.max_students ORDER BY EXTRACT(DOW FROM ts.date), genre;

CREATE VIEW rentable_instruments AS
SELECT physical_instrument_id AS id, instrument, brand_name, cost FROM physical_instrument AS pi
NATURAL JOIN instrument
NATURAL JOIN instrument_brand
    WHERE NOT EXISTS (
    SELECT physical_instrument_id FROM rental AS r
    WHERE (SELECT r.end_date > CURRENT_DATE)
    AND r.physical_instrument_id=pi.physical_instrument_id
);
