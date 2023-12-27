CREATE TABLE APPLICATIONS(
    ID INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    SERVICE_ID INT NOT NULL,
    CONTACTS VARCHAR(20) NOT NULL,
    EDUCATION VARCHAR(20) NOT NULL,
    CERTIFICATES VARCHAR(20) NOT NULL,
    WORKING_EXPERIENCE VARCHAR(20) NOT NULL,
    SPECIALIST_ID INT NOT NULL,
    STATUS VARCHAR(20) NOT NULL,
    COMMENT VARCHAR(50),
    CONSTRAINT FK_APPLICATIONS_SPECIALISTS FOREIGN KEY (SPECIALIST_ID) REFERENCES SPECIALISTS(SPECIALIST_ID),
    CONSTRAINT FK_APPLICATIONS_SERVICES FOREIGN KEY (SERVICE_ID) REFERENCES SERVICES(SERVICE_ID)

);