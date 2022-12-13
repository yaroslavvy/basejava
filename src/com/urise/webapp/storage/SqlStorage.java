package com.urise.webapp.storage;

import com.urise.webapp.LoggerConfig;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.sql.SqlHelper;
import org.slf4j.Logger;

import java.sql.Date;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.urise.webapp.storage.AbstractStorage.RESUME_COMPARATOR_FULL_NAME_THEN_UUID;

public class SqlStorage implements Storage {
    private static final Logger LOGGER = LoggerConfig.getLogger(SqlStorage.class);
    private final SqlHelper sqlHelper;

    public SqlStorage(String url, String user, String password) {
        sqlHelper = new SqlHelper(url, user, password);
    }

    @Override
    public void clear() {
        sqlHelper.executeStatement("DELETE FROM resumes",
                (ps) -> {
                    LOGGER.info("trying to execute sql statement: " + ps);
                    ps.execute();
                    return null;
                });
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.transactionExecute(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO resumes (resume_id, full_name) VALUES (?, ?)")) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, resume.getFullName());
                LOGGER.info("trying to execute sql statement: " + ps);
                ps.execute();
            }
            insertContacts(resume, connection);
            insertTextAndListSections(resume, connection);
            insertCompanyListSections(resume, connection);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.executeStatement("" +
                        "SELECT * FROM resumes r " +
                        "LEFT JOIN contacts c USING (resume_id) " +
                        "WHERE resume_id = ?",
                (ps) -> {
                    ps.setString(1, uuid);
                    LOGGER.info("trying to execute sql statement: " + ps);
                    ResultSet resultSet = ps.executeQuery();
                    if (!resultSet.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume resume = new Resume(resultSet.getString("resume_id"), resultSet.getString("full_name"));
                    do {
                        addContact(resume, resultSet);
                    } while (resultSet.next());

                    return sqlHelper.executeStatement("SELECT * FROM list_sections WHERE resume_id = ?",
                            (ps2) -> {
                                ps2.setString(1, uuid);
                                LOGGER.info("trying to execute sql statement: " + ps2);
                                ResultSet resultSet2 = ps2.executeQuery();
                                if (resultSet2.next()) {
                                    addTextAndListSections(resume, resultSet2);
                                }
                                return sqlHelper.executeStatement("" +
                                                "SELECT * FROM company_list_sections cls " +
                                                "JOIN periods p ON cls.company_id = p.company_id " +
                                                "WHERE cls.resume_id = ?",
                                        (ps3) -> {
                                            ps3.setString(1, uuid);
                                            LOGGER.info("trying to execute sql statement: " + ps3);
                                            ResultSet resultSet3 = ps3.executeQuery();
                                            if (resultSet3.next()) {
                                                addCompanyListSections(resume, resultSet3);
                                            }
                                            return resume;
                                        });
                            });
                });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.executeStatement("DELETE FROM resumes WHERE resume_id = ?",
                (ps) -> {
                    ps.setString(1, uuid);
                    LOGGER.info("trying to execute sql statement: " + ps);
                    sqlHelper.executeUpdateAndLogIfNothingUpdates(ps, uuid);
                    return null;
                });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.executeStatement("SELECT * FROM resumes",
                (ps) -> {
                    LOGGER.info("trying to execute sql statement: " + ps);
                    Map<String, Resume> resumeMap = new HashMap<>();
                    ResultSet resultSet = ps.executeQuery();
                    while (resultSet.next()) {
                        String resume_id = resultSet.getString("resume_id");
                        resumeMap.put(resume_id, new Resume(resume_id, resultSet.getString("full_name")));
                    }
                    return sqlHelper.executeStatement("SELECT * FROM contacts",
                            (ps2) -> {
                                LOGGER.info("trying to execute sql statement: " + ps2);
                                ResultSet resultSet2 = ps2.executeQuery();
                                while (resultSet2.next()) {
                                    String resume_id = resultSet2.getString("resume_id");
                                    addContact(resumeMap.get(resume_id), resultSet2);
                                }
                                return sqlHelper.executeStatement("SELECT * FROM list_sections ORDER BY resume_id",
                                        (ps3) -> {
                                            LOGGER.info("trying to execute sql statement: " + ps3);
                                            ResultSet resultSet3 = ps3.executeQuery();
                                            if (resultSet3.next()) {
                                                while (addTextAndListSections(resumeMap.get(resultSet3.getString("resume_id")), resultSet3)) {
                                                }
                                            }
                                            return sqlHelper.executeStatement("" +
                                                            "SELECT * FROM company_list_sections cls " +
                                                            "JOIN periods p ON cls.company_id = p.company_id " +
                                                            "ORDER BY resume_id",
                                                    (ps4) -> {
                                                        LOGGER.info("trying to execute sql statement: " + ps4);
                                                        ResultSet resultSet4 = ps4.executeQuery();
                                                        if (resultSet4.next()) {
                                                            while (addCompanyListSections(resumeMap.get(resultSet4.getString("resume_id")), resultSet4)) {
                                                            }
                                                        }
                                                        return resumeMap.values().stream().sorted(RESUME_COMPARATOR_FULL_NAME_THEN_UUID).collect(Collectors.toList());
                                                    });
                                        });
                            });
                });
    }

    @Override
    public int size() {
        return sqlHelper.executeStatement("SELECT COUNT(*) FROM resumes",
                (ps) -> {
                    LOGGER.info("trying to execute sql statement: " + ps);
                    ResultSet resultSet = ps.executeQuery();
                    resultSet.next();
                    return (int) resultSet.getLong("count");
                });
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.transactionExecute(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("UPDATE resumes SET full_name = ? WHERE resume_id = ?")) {
                ps.setString(1, resume.getFullName());
                ps.setString(2, resume.getUuid());
                LOGGER.info("trying to execute sql statement: " + ps);
                sqlHelper.executeUpdateAndLogIfNothingUpdates(ps, resume.getUuid());
            }
            deleteAttributes(connection, "DELETE FROM contacts WHERE resume_id = ?", resume);
            insertContacts(resume, connection);
            deleteAttributes(connection, "DELETE FROM list_sections WHERE resume_id = ?", resume);
            insertTextAndListSections(resume, connection);
            deleteAttributes(connection, "DELETE FROM company_list_sections WHERE resume_id = ?", resume);
            insertCompanyListSections(resume, connection);
            return null;
        });
    }

    private void insertContacts(Resume resume, Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO contacts (resume_id, contact_type_id, value) VALUES (?, ?, ?)")) {
            for (Map.Entry<ContactType, String> entry : resume.getContacts().entrySet()) {
                ps.setString(1, resume.getUuid());
                ps.setInt(2, entry.getKey().ordinal());
                ps.setString(3, entry.getValue());
                ps.addBatch();
            }
            LOGGER.info("trying to execute sql statement: " + ps);
            ps.executeBatch();
        }
    }

    private void addContact(Resume resume, ResultSet resultSet) throws SQLException {
        ContactType contactType = ContactType.values()[resultSet.getInt("contact_type_id")];
        String value = resultSet.getString("value");
        if (value != null) {
            resume.addContact(contactType, value);
        }
    }

    private void insertTextAndListSections(Resume resume, Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO list_sections (resume_id, section_type_id, line_order, value) VALUES (?, ?, ?, ?)")) {
            for (Map.Entry<SectionType, Section> entry : resume.getSections().entrySet()) {
                switch (entry.getKey()) {
                    case OBJECTIVE:
                    case PERSONAL:
                        ps.setString(1, resume.getUuid());
                        ps.setInt(2, entry.getKey().ordinal());
                        ps.setInt(3, 0);
                        ps.setString(4, ((TextSection) entry.getValue()).getText());
                        ps.addBatch();
                        break;
                    case ACHIEVEMENTS:
                    case QUALIFICATIONS:
                        ListIterator<String> listIterator = ((ListSection) entry.getValue()).getList().listIterator();
                        while (listIterator.hasNext()) {
                            ps.setString(1, resume.getUuid());
                            ps.setInt(2, entry.getKey().ordinal());
                            ps.setInt(3, listIterator.nextIndex());
                            ps.setString(4, listIterator.next());
                            ps.addBatch();
                        }
                        break;
                    default:
                }
            }
            LOGGER.info("trying to execute sql statement: " + ps);
            ps.executeBatch();
        }
    }

    private void insertCompanyListSections(Resume resume, Connection connection) throws SQLException {
        Map<String, Company> companyMap = new HashMap<>();
        try (PreparedStatement ps = connection.prepareStatement("" +
                "INSERT INTO company_list_sections (company_id, resume_id, section_type_id, company_order, company_name, company_url)" +
                " VALUES (?, ?, ?, ?, ?, ?)")) {
            for (Map.Entry<SectionType, Section> entry : resume.getSections().entrySet()) {
                switch (entry.getKey()) {
                    case EDUCATION:
                    case EXPERIENCE:
                        ListIterator<Company> companyListIterator = ((CompanyListSection) entry.getValue()).getCompanies().listIterator();
                        while (companyListIterator.hasNext()) {
                            String companyId = UUID.randomUUID().toString();
                            ps.setString(1, companyId);
                            ps.setString(2, resume.getUuid());
                            ps.setInt(3, entry.getKey().ordinal());
                            ps.setInt(4, companyListIterator.nextIndex());
                            Company company = companyListIterator.next();
                            companyMap.put(companyId, company);
                            ps.setString(5, company.getName());
                            ps.setString(6, company.getUrl());
                            ps.addBatch();
                        }
                        break;
                    default:
                }
            }
            LOGGER.info("trying to execute sql statement: " + ps);
            ps.executeBatch();
        }
        try (PreparedStatement ps = connection.prepareStatement("" +
                "INSERT INTO periods (company_id, period_order, begin_date, end_date, title, description)" +
                " VALUES (?, ?, ?, ?, ?, ?)")) {
            for (Map.Entry<String, Company> entry : companyMap.entrySet()) {
                ListIterator<Company.Period> periodListIterator = entry.getValue().getPeriods().listIterator();
                while (periodListIterator.hasNext()) {
                    ps.setString(1, entry.getKey());
                    ps.setInt(2, periodListIterator.nextIndex());
                    Company.Period period = periodListIterator.next();
                    ps.setDate(3, Date.valueOf(period.getBeginDate()));
                    ps.setDate(4, Date.valueOf(period.getEndDate()));
                    ps.setString(5, period.getTitle());
                    ps.setString(6, period.getDescription());
                    ps.addBatch();
                }
            }
            LOGGER.info("trying to execute sql statement: " + ps);
            ps.executeBatch();
        }
    }

    private boolean addTextAndListSections(Resume resume, ResultSet resultSet) throws SQLException {
        Map<Integer, String> achievementsMap = new HashMap();
        Map<Integer, String> qualificationsMap = new HashMap();
        boolean next = false;
        do {
            if (!resume.getUuid().equals(resultSet.getString("resume_id"))) {
                next = true;
                break;
            }
            SectionType sectionType = SectionType.values()[resultSet.getInt("section_type_id")];
            switch (sectionType) {
                case OBJECTIVE:
                case PERSONAL:
                    TextSection textSection = new TextSection();
                    textSection.setText(resultSet.getString("value"));
                    resume.addSection(sectionType, textSection);
                    break;
                case ACHIEVEMENTS:
                    addLineToMap(achievementsMap, resultSet);
                    break;
                case QUALIFICATIONS:
                    addLineToMap(qualificationsMap, resultSet);
                    break;
                default:
            }
        } while (resultSet.next());
        addListSection(resume, SectionType.ACHIEVEMENTS, achievementsMap);
        addListSection(resume, SectionType.QUALIFICATIONS, qualificationsMap);
        return next;
    }

    private void addLineToMap(Map<Integer, String> map, ResultSet resultSet) throws SQLException {
        map.put(resultSet.getInt("line_order"), resultSet.getString("value"));
    }

    private void addListSection(Resume resume, SectionType sectionType, Map<Integer, String> map) {
        if (!map.isEmpty()) {
            ListSection listSection = new ListSection();
            map.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEachOrdered(entry -> listSection.addLine(entry.getValue()));
            resume.addSection(sectionType, listSection);
        }
    }

    private boolean addCompanyListSections(Resume resume, ResultSet resultSet) throws SQLException {
        Map<Integer, Company> educationCompanyMap = new HashMap();
        Map<Integer, Company> experienceCompanyMap = new HashMap();
        Map<Integer, HashMap<Integer, Company.Period>> educationPeriodMap = new HashMap();
        Map<Integer, HashMap<Integer, Company.Period>> experiencePeriodMap = new HashMap();
        boolean next = false;

        do {
            if (!resume.getUuid().equals(resultSet.getString("resume_id"))) {
                next = true;
                break;
            }
            SectionType sectionType = SectionType.values()[resultSet.getInt("section_type_id")];
            switch (sectionType) {
                case EDUCATION:
                    addCompaniesAndPeriodsToMaps(educationCompanyMap, educationPeriodMap, resultSet);
                    break;
                case EXPERIENCE:
                    addCompaniesAndPeriodsToMaps(experienceCompanyMap, experiencePeriodMap, resultSet);
                    break;
                default:
            }
        } while (resultSet.next());
        addCompanyListSection(resume, SectionType.EDUCATION, educationCompanyMap, educationPeriodMap);
        addCompanyListSection(resume, SectionType.EXPERIENCE, experienceCompanyMap, experiencePeriodMap);
        return next;
    }

    private void addCompaniesAndPeriodsToMaps(Map<Integer, Company> companyMap,
                                              Map<Integer, HashMap<Integer, Company.Period>> periodMap,
                                              ResultSet resultSet) throws SQLException {
        int companyOrder = resultSet.getInt("company_order");
        companyMap.putIfAbsent(companyOrder,
                new Company(resultSet.getString("company_name"), resultSet.getString("company_url")));
        periodMap.putIfAbsent(companyOrder, new HashMap<>());
        Company.Period period = new Company.Period(resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getDate("begin_date").toLocalDate(),
                resultSet.getDate("end_date").toLocalDate());
        periodMap.get(companyOrder).put(resultSet.getInt("period_order"), period);
    }

    private void addCompanyListSection(Resume resume, SectionType sectionType, Map<Integer, Company> companyMap,
                                       Map<Integer, HashMap<Integer, Company.Period>> periodMap) {
        if (!periodMap.isEmpty()) {
            CompanyListSection companyListSection = new CompanyListSection();
            periodMap.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEachOrdered(periods -> {
                        Company company = companyMap.get(periods.getKey());
                        periods.getValue().entrySet().stream()
                                .sorted(Map.Entry.comparingByKey())
                                .forEachOrdered(entry -> company.addPeriod(entry.getValue()));
                        companyListSection.addCompany(company);
                    });
            resume.addSection(sectionType, companyListSection);
        }
    }

    private void deleteAttributes(Connection connection, String sqlStatement, Resume resume) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(sqlStatement)) {
            ps.setString(1, resume.getUuid());
            LOGGER.info("trying to execute sql statement: " + ps);
            ps.execute();
        }
    }
}
