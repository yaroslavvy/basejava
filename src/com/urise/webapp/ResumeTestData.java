package com.urise.webapp;

import com.urise.webapp.model.*;
import com.urise.webapp.util.DateUtil;

import java.time.Month;
import java.util.*;

public class ResumeTestData {
    public static void main(String[] args) {

        Resume resume = new Resume("Григорий Кислин");

        Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
        contacts.put(ContactType.PHONE, "+7(921) 855-0482");
        contacts.put(ContactType.SKYPE, "skype:grigory.kislin");
        contacts.put(ContactType.MAIL, "gkislin@yandex.ru");
        contacts.put(ContactType.LINKEDIN, "https://www.linkedin.com/in/gkislin");
        contacts.put(ContactType.GITHUB, "https://github.com/gkislin");
        contacts.put(ContactType.STACKOVERFLOW, "https://stackoverflow.com/users/548473/grigory-kislin");
        contacts.put(ContactType.HOMEPAGE, "http://gkislin.ru/");
        resume.setContacts(contacts);

        TextSection objective = new TextSection();
        objective.setText("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям");

        TextSection personal = new TextSection();
        personal.setText("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры.");

        List<String> achievementList = new ArrayList<>();
        achievementList.add("Организация команды и успешная реализация Java проектов для сторонних заказчиков:" +
                " приложения автопарк на стеке Spring Cloud/микросервисы, система мониторинга показателей спортсменов" +
                " на Spring Boot, участие в проекте МЭШ на Play-2, многомодульный Spring Boot + Vaadin проект для комплексных DIY смет");
        achievementList.add("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", " +
                "\"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\"." +
                " Организация онлайн стажировок и ведение проектов. Более 3500 выпускников.");
        achievementList.add("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike." +
                " Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.");
        achievementList.add("Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM." +
                " Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке:" +
                " Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера.");
        achievementList.add("Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, Spring," +
                " Spring-MVC, GWT, ExtGWT (GXT), Commet, HTML5, Highstock для алгоритмического трейдинга.");
        achievementList.add("Создание JavaEE фреймворка для отказоустойчивого взаимодействия слабо-связанных сервисов" +
                " (SOA-base архитектура, JAX-WS, JMS, AS Glassfish). Сбор статистики сервисов и информации о" +
                " состоянии через систему мониторинга Nagios. Реализация онлайн клиента для администрирования" +
                " и мониторинга системы по JMX (Jython/ Django).");
        achievementList.add("Реализация протоколов по приему платежей всех основных платежных системы России" +
                " (Cyberplat, Eport, Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) и Никарагуа.");
        ListSection achievements = new ListSection();
        achievements.setList(achievementList);

        List<String> qualificationList = new ArrayList<>();
        qualificationList.add("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2");
        qualificationList.add("Version control: Subversion, Git, Mercury, ClearCase, Perforce");
        qualificationList.add("DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle," +
                " MySQL, SQLite, MS SQL, HSQLDB");
        qualificationList.add("Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, Groovy");
        qualificationList.add("XML/XSD/XSLT, SQL, C/C++, Unix shell scripts");
        qualificationList.add("Java Frameworks: Java 8 (Time API, Streams), Guava, Java Executor, MyBatis," +
                " Spring (MVC, Security, Data, Clouds, Boot), JPA (Hibernate, EclipseLink), Guice," +
                " GWT(SmartGWT, ExtGWT/GXT), Vaadin, Jasperreports, Apache Commons, Eclipse SWT, JUnit, Selenium (htmlelements).");
        qualificationList.add("Python: Django.");
        qualificationList.add("JavaScript: jQuery, ExtJS, Bootstrap.js, underscore.js");
        qualificationList.add("Scala: SBT, Play2, Specs2, Anorm, Spray, Akka");
        qualificationList.add("Технологии: Servlet, JSP/JSTL, JAX-WS, REST, EJB, RMI, JMS, JavaMail, JAXB, StAX, SAX," +
                " DOM, XSLT, MDB, JMX, JDBC, JPA, JNDI, JAAS, SOAP, AJAX, Commet, HTML5," +
                " ESB, CMIS, BPMN2, LDAP, OAuth1, OAuth2, JWT.");
        qualificationList.add("Инструменты: Maven + plugin development, Gradle, настройка Ngnix");
        qualificationList.add("администрирование Hudson/Jenkins, Ant + custom task, SoapUI, JPublisher, Flyway," +
                " Nagios, iReport, OpenCmis, Bonita, pgBouncer");
        qualificationList.add("Отличное знание и опыт применения концепций ООП, SOA, шаблонов проектрирования," +
                " архитектурных шаблонов, UML, функционального программирования");
        qualificationList.add("Родной русский, английский \"upper intermediate\"");
        ListSection qualifications = new ListSection();
        qualifications.setList(qualificationList);

        Company javaOnlineProject = new Company("Java Online Projects",
                "https://javaops.ru/",
                Arrays.asList(new Period("Автор проекта.",
                        "Создание, организация и проведение Java онлайн проектов и стажировок.",
                        DateUtil.of(2013, Month.OCTOBER),
                        null)));

        Company wrike = new Company("Wrike",
                "https://www.wrike.com/",
                Arrays.asList(new Period("Старший разработчик (backend)",
                        "Проектирование и разработка онлайн платформы управления проектами Wrike" +
                                " (Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis)." +
                                " Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO.",
                        DateUtil.of(2014,Month.OCTOBER),
                        DateUtil.of(2016,Month.JANUARY))));

        Company ritCenter = new Company("RIT Center",
                null,
                Arrays.asList(new Period("Java архитектор",
                        "Организация процесса разработки системы ERP для разных окружений: релизная политика," +
                                " версионирование, ведение CI (Jenkins), миграция базы (кастомизация Flyway)," +
                                " конфигурирование системы (pgBoucer, Nginx), AAA via SSO. Архитектура БД и серверной части системы." +
                                " Разработка интергационных сервисов: CMIS, BPMN2, 1C (WebServices), сервисов общего назначения" +
                                " (почта, экспорт в pdf, doc, html). Интеграция Alfresco JLAN для online редактирование из" +
                                " браузера документов MS Office. Maven + plugin development, Ant, Apache Commons," +
                                " Spring security, Spring MVC, Tomcat,WSO2, xcmis, OpenCmis, Bonita, Python scripting," +
                                " Unix shell remote scripting via ssh tunnels, PL/Python",
                        DateUtil.of(2012,Month.APRIL),
                        DateUtil.of(2014,Month.OCTOBER))));

        Company luxoft = new Company("Luxoft (Deutsche Bank)",
                "https://www.luxoft.ru/",
                Arrays.asList(new Period("Ведущий программист",
                        "Участие в проекте Deutsche Bank CRM (WebLogic, Hibernate, Spring, Spring MVC," +
                                " SmartGWT, GWT, Jasper, Oracle). Реализация клиентской и серверной части CRM." +
                                " Реализация RIA-приложения для администрирования, мониторинга и анализа результатов" +
                                " в области алгоритмического трейдинга. JPA, Spring, Spring-MVC," +
                                " GWT, ExtGWT (GXT), Highstock, Commet, HTML5.",
                        DateUtil.of(2010,Month.DECEMBER),
                        DateUtil.of(2012,Month.APRIL))));

        Company yota = new Company("Yota",
                "https://www.yota.ru/",
                Arrays.asList(new Period("Ведущий специалист",
                        "Дизайн и имплементация Java EE фреймворка для отдела \"Платежные Системы\"" +
                                " (GlassFish v2.1, v3, OC4J, EJB3, JAX-WS RI 2.1, Servlet 2.4, JSP, JMX, JMS, Maven2)." +
                                " Реализация администрирования, статистики и мониторинга фреймворка. Разработка" +
                                " online JMX клиента (Python/ Jython, Django, ExtJS)",
                        DateUtil.of(2008,Month.JUNE),
                        DateUtil.of(2010,Month.DECEMBER))));

        Company enkata = new Company("Enkata",
                "http://www.openspan.com/enkata/",
                Arrays.asList(new Period("Разработчик ПО",
                        "Реализация клиентской (Eclipse RCP) и серверной (JBoss 4.2, Hibernate 3.0, Tomcat, JMS)" +
                                " частей кластерного J2EE приложения (OLAP, Data mining).",
                        DateUtil.of(2007,Month.MARCH),
                        DateUtil.of(2008,Month.JUNE))));

        Company siemens = new Company("Siemens AG",
                "https://new.siemens.com/ru/ru.html",
                Arrays.asList(new Period("Разработчик ПО",
                        "Разработка информационной модели, проектирование интерфейсов," +
                                " реализация и отладка ПО на мобильной IN платформе Siemens @vantage (Java, Unix).",
                        DateUtil.of(2005,Month.JANUARY),
                        DateUtil.of(2007,Month.FEBRUARY))));

        Company alcatel = new Company("Alcatel",
                "www.alcatel.ru",
                Arrays.asList(new Period("Инженер по аппаратному и программному тестированию",
                        "Тестирование, отладка, внедрение ПО цифровой телефонной станции Alcatel 1000 S12 (CHILL, ASM).",
                        DateUtil.of(1997,Month.SEPTEMBER),
                        DateUtil.of(2005,Month.JANUARY))));
        CompanyListSection experience = new CompanyListSection();
        experience.setCompanies(Arrays.asList(javaOnlineProject, wrike, ritCenter, luxoft, yota, enkata, siemens, alcatel));

        Company coursera = new Company("Coursera",
                "https://www.coursera.org/course/progfun",
                Arrays.asList(new Period("\'Functional Programming Principles in Scala\' by Martin Odersky", null,
                        DateUtil.of(2013,Month.MARCH),
                        DateUtil.of(2013,Month.MAY))));

        Company luxoft_edu = new Company("Luxoft",
                "https://ibs-training.ru/kurs/obektno-orientirovannyy_analiz_i_proektirovanie_na_uml.html",
                Arrays.asList(new Period("Курс \'Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.\'", null,
                        DateUtil.of(2011,Month.MARCH),
                        DateUtil.of(2011,Month.APRIL))));

        Company siemens_edu = new Company("Siemens AG",
                "https://www.siemens.com/global/en.html",
                Arrays.asList(new Period("3 месяца обучения мобильным IN сетям (Берлин)", null,
                        DateUtil.of(2005,Month.JANUARY),
                        DateUtil.of(2005,Month.APRIL))));

        Company alcatel_edu = new Company("Alcatel",
                "www.alcatel.ru",
                Arrays.asList(new Period("6 месяцев обучения цифровым телефонным сетям (Москва)", null,
                        DateUtil.of(1997,Month.SEPTEMBER),
                        DateUtil.of(1998,Month.MARCH))));

        Company itmo = new Company("Санкт-Петербургский национальный исследовательский" +
                " университет информационных технологий, механики и оптики",
                "https://itmo.ru/",
                Arrays.asList(new Period("Аспирантура (программист С, С++)", null,
                                DateUtil.of(1993,Month.SEPTEMBER),
                                DateUtil.of(1996,Month.JULY)),
                        new Period("Инженер (программист Fortran, C)", null,
                                DateUtil.of(1987,Month.SEPTEMBER),
                                DateUtil.of(1993,Month.JULY))));

        Company mfti = new Company("Заочная физико-техническая школа при МФТИ",
                "https://school.mipt.ru/",
                Arrays.asList(new Period("Закончил с отличием", null,
                        DateUtil.of(1984,Month.SEPTEMBER),
                        DateUtil.of(1987,Month.JUNE))));

        CompanyListSection education = new CompanyListSection();
        education.setCompanies(Arrays.asList(coursera, luxoft_edu, siemens_edu, alcatel_edu, itmo, mfti));

        Map<SectionType, Section> sections = new EnumMap<>(SectionType.class);
        sections.put(SectionType.OBJECTIVE, objective);
        sections.put(SectionType.PERSONAL, personal);
        sections.put(SectionType.ACHIEVEMENTS, achievements);
        sections.put(SectionType.QUALIFICATIONS, qualifications);
        sections.put(SectionType.EXPERIENCE, experience);
        sections.put(SectionType.EDUCATION, education);

        resume.setSections(sections);

        printResume(resume);
    }

    private static void printResume(Resume resume) {
        System.out.println(resume.getFullName());

        System.out.println();
        Map<ContactType, String> contacts = resume.getContacts();
        for (ContactType contactType : ContactType.values()) {
            String contactValue = contacts.get(contactType);
            if (contactValue != null) {
                System.out.println(contactType.getTitle() + contactValue);
            }
        }

        Map<SectionType, Section> sections = resume.getSections();
        for (SectionType sectionType : SectionType.values()) {
            Section section = sections.get(sectionType);
            if (section != null) {
                switch (sectionType) {
                    case OBJECTIVE:
                    case PERSONAL:
                        System.out.println();
                        String text = ((TextSection) section).getText();
                        if (text != null) {
                            System.out.println(sectionType.getTitle());
                            System.out.println(text);
                        }
                        break;

                    case ACHIEVEMENTS:
                    case QUALIFICATIONS:
                        System.out.println();
                        List<String> stringList = ((ListSection) section).getList();
                        if (stringList != null) {
                            System.out.println(sectionType.getTitle());
                            for (String line : stringList) {
                                System.out.println("\u00B7 " + line);
                            }
                        }
                        break;

                    case EXPERIENCE:
                    case EDUCATION:
                        List<Company> companyList = ((CompanyListSection) section).getCompanies();
                        if (companyList != null) {
                            System.out.println();
                            System.out.println(sectionType.getTitle());
                            for (Company company : companyList) {
                                if (company != null) {
                                    System.out.println();
                                    String name = company.getName();
                                    if (name != null) {
                                        System.out.println(name);
                                    }
                                    String website = company.getWebsite();
                                    if (website != null) {
                                        System.out.println(website);
                                    }
                                    List<Period> periods = company.getPeriods();
                                    if (periods != null) {
                                        for (Period period : periods) {
                                            System.out.println(period.getBeginDate() + " - "
                                                    + (period.getEndDate() == null ? "Сейчас" : period.getEndDate().toString())
                                                    + "\t\t" + period.getTitle());
                                            String description = period.getDescription();
                                            if (description != null) {
                                                System.out.println(description);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                }
            }
        }
    }
}
