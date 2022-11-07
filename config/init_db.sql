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

ALTER TABLE resumes
    OWNER TO postgres;
ALTER TABLE contact_types
    OWNER TO postgres;
ALTER TABLE contacts
    OWNER TO postgres;

