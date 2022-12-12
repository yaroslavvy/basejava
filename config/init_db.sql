CREATE TABLE resumes
(
    resume_id CHAR(36) PRIMARY KEY,
    full_name TEXT NOT NULL
);

CREATE TABLE contact_types
(
    contact_type_id   INT PRIMARY KEY,
    contact_type_name CHAR(13) NOT NULL UNIQUE
);

INSERT INTO contact_types (contact_type_id, contact_type_name)
VALUES (0, 'PHONE'),
       (1, 'SKYPE'),
       (2, 'MAIL'),
       (3, 'LINKEDIN'),
       (4, 'GITHUB'),
       (5, 'STACKOVERFLOW'),
       (6, 'HOMEPAGE');

CREATE TABLE contacts
(
    resume_id       CHAR(36) NOT NULL
        CONSTRAINT fk_resumes_contacts REFERENCES resumes (resume_id) ON DELETE CASCADE,
    contact_type_id INT      NOT NULL
        CONSTRAINT fk_contact_types_contacts REFERENCES contact_types (contact_type_id) ON DELETE CASCADE,
    value           TEXT     NOT NULL,
    CONSTRAINT pk_contacts PRIMARY KEY (resume_id, contact_type_id)
);

CREATE TABLE section_types
(
    section_type_id   INT PRIMARY KEY,
    section_type_name CHAR(14) NOT NULL UNIQUE
);

INSERT INTO section_types (section_type_id, section_type_name)
VALUES (0, 'OBJECTIVE'),
       (1, 'PERSONAL'),
       (2, 'ACHIEVEMENTS'),
       (3, 'QUALIFICATIONS'),
       (4, 'EXPERIENCE'),
       (5, 'EDUCATION');

CREATE TABLE list_sections
(
    resume_id       CHAR(36) NOT NULL
        CONSTRAINT fk_resumes_list_sections REFERENCES resumes (resume_id) ON DELETE CASCADE,
    section_type_id INT      NOT NULL
        CONSTRAINT fk_section_types_list_sections REFERENCES section_types (section_type_id) ON DELETE CASCADE,
    line_order INT NOT NULL,
    value           TEXT     NOT NULL,
    CONSTRAINT pk_list_sections PRIMARY KEY (resume_id, section_type_id, line_order),
    CONSTRAINT ch_list_sections_section_type_id CHECK (section_type_id BETWEEN 0 AND 3),
    CONSTRAINT ch_list_sections_line_order CHECK (line_order >= 0)
);

CREATE TABLE companies
(
    company_id SERIAL PRIMARY KEY,
    company_name TEXT UNIQUE NOT NULL,
    url TEXT
);

CREATE TABLE periods
(
    resume_id CHAR(36) NOT NULL
        CONSTRAINT fk_resumes_periods REFERENCES resumes (resume_id) ON DELETE CASCADE,
    company_id SERIAL NOT NULL
        CONSTRAINT fk_companies_periods REFERENCES companies (company_id) ON DELETE CASCADE,
    section_type_id INT NOT NULL
        CONSTRAINT fk_section_types_periods REFERENCES section_types (section_type_id) ON DELETE CASCADE,
    begin_date DATE NOT NULL,
    end_date DATE NOT NULL,
    title TEXT,
    description TEXT,
    CONSTRAINT pk_periods PRIMARY KEY (resume_id, company_id, section_type_id, begin_date, end_date),
    CONSTRAINT ch_section_types_periods CHECK (section_type_id BETWEEN 4 AND 5),
    CONSTRAINT ch_begin_end_date CHECK (begin_date <= end_date)
);

ALTER TABLE resumes
    OWNER TO postgres;
ALTER TABLE contact_types
    OWNER TO postgres;
ALTER TABLE contacts
    OWNER TO postgres;
ALTER TABLE section_types
    OWNER TO postgres;
ALTER TABLE list_sections
    OWNER TO postgres;
ALTER TABLE companies
    OWNER TO postgres;
ALTER TABLE periods
    OWNER TO postgres;

