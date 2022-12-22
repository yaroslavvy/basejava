package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.ResumeTestData;
import com.urise.webapp.model.*;
import com.urise.webapp.storage.Storage;
import com.urise.webapp.util.DateUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

public class ResumeServlet extends HttpServlet {
    private Storage storage;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        storage = Config.get().getStorage();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        String uuid = req.getParameter("uuid");
        String actionStr = req.getParameter("action");
        if (actionStr == null && uuid == null) {
            List<Resume> resumeList = storage.getAllSorted();
            req.setAttribute("resumeList", resumeList);
            req.setAttribute("listSize", resumeList.size());
            req.getRequestDispatcher("WEB-INF/jsp/list.jsp").forward(req, resp);
            return;
        }
        if (actionStr != null) {
            Actions action;
            try {
                action = Actions.valueOf(actionStr.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                req.getRequestDispatcher("WEB-INF/jsp/404.jsp").forward(req, resp);
                return;
            }
            if (uuid != null) {
                switch (action) {
                    case EDIT:
                        req.setAttribute("resume", storage.get(uuid));
                        req.getRequestDispatcher("WEB-INF/jsp/edit.jsp").forward(req, resp);
                        break;
                    case VIEW:
                        Resume resume = storage.get(uuid);
                        String htmlFormattedResume = ResumeTestData.printResume(resume, true);
                        req.setAttribute("htmlFormattedResume", htmlFormattedResume);
                        req.setAttribute("resume", resume);
                        req.getRequestDispatcher("WEB-INF/jsp/view.jsp").forward(req, resp);
                        break;
                    case DELETE:
                        storage.delete(uuid);
                        resp.sendRedirect(req.getContextPath() + "/resume");
                        break;
                    default:
                        req.getRequestDispatcher("WEB-INF/jsp/404.jsp").forward(req, resp);
                }
                return;
            } else {
                switch (action) {
                    case CREATE:
                        req.getRequestDispatcher("WEB-INF/jsp/edit.jsp").forward(req, resp);
                        break;
                    default:
                        req.getRequestDispatcher("WEB-INF/jsp/404.jsp").forward(req, resp);
                }
                return;
            }
        }
        req.getRequestDispatcher("WEB-INF/jsp/404.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String uuid = req.getParameter("uuid");
        Resume resume;
        if (uuid.isEmpty()) {
            resume = new Resume(req.getParameter("fullName"));
            fillResume(req, resume);
            storage.save(resume);
        } else {
            resume = new Resume(uuid, req.getParameter("fullName"));
            fillResume(req, resume);
            storage.update(resume);
        }
        resp.sendRedirect(req.getContextPath() + "/resume?action=view&uuid=" + resume.getUuid());
    }

    private void fillResume(HttpServletRequest req, Resume resume) {
        for (ContactType contactType : ContactType.values()) {
            String contact = req.getParameter(contactType.name());
            if (contact != null && !contact.isEmpty()) {
                resume.addContact(contactType, contact);
            }
        }
        for (SectionType sectionType : SectionType.values()) {
            switch (sectionType) {
                case OBJECTIVE:
                case PERSONAL:
                    String text = req.getParameter(sectionType.name());
                    if (text != null && !text.isEmpty()) {
                        TextSection textSection = new TextSection();
                        textSection.setText(text);
                        resume.addSection(sectionType, textSection);
                    }
                    break;
                case ACHIEVEMENTS:
                case QUALIFICATIONS:
                    String[] lines = req.getParameterValues(sectionType.name());
                    if (lines != null) {
                        ListSection listSection = new ListSection();
                        for (String line : lines) {
                            if (!line.isEmpty()) {
                                listSection.addLine(line);
                            }
                        }
                        resume.addSection(sectionType, listSection);
                    }
                    break;
                default:
                    break;
            }
        }

        SectionType firstSectionType = SectionType.EXPERIENCE.ordinal() < SectionType.EDUCATION.ordinal() ?
                SectionType.EXPERIENCE : SectionType.EDUCATION;
        SectionType secondSectionType = SectionType.EXPERIENCE.ordinal() >= SectionType.EDUCATION.ordinal() ?
                SectionType.EXPERIENCE : SectionType.EDUCATION;
        String firstSectionCompanyCounter = req.getParameter(firstSectionType.name());
        String secondSectionCompanyCounter = req.getParameter(secondSectionType.name());
        int firstSectionCompanyBeginIndex = -1;
        if (firstSectionCompanyCounter != null && !firstSectionCompanyCounter.isEmpty() && Integer.parseInt(firstSectionCompanyCounter) != 0) {
            firstSectionCompanyBeginIndex = 0;
        }
        int secondSectionCompanyBeginIndex = -1;
        if (secondSectionCompanyCounter != null && !secondSectionCompanyCounter.isEmpty() && Integer.parseInt(secondSectionCompanyCounter) != 0) {
            secondSectionCompanyBeginIndex = firstSectionCompanyBeginIndex == 0 ? Integer.parseInt(firstSectionCompanyCounter) : 0;
        }
        String[] companyNames = req.getParameterValues("companyName");
        String[] companyUrls = req.getParameterValues("companyUrl");
        String[] periodCountersStr = req.getParameterValues("periodCounter");
        String[] periodBeginDates = req.getParameterValues("periodBeginDate");
        String[] periodEndDates = req.getParameterValues("periodEndDate");
        String[] periodTitles = req.getParameterValues("periodTitle");
        String[] periodDescriptions = req.getParameterValues("periodDescription");

        if (companyNames == null || companyUrls == null || periodCountersStr == null
                || periodBeginDates == null || periodEndDates == null
                || periodTitles == null || periodDescriptions == null) {
            throw new IllegalArgumentException("Wrong POST request for EXPERIENCE or/and EDUCATION section(s): " +
                    "Some parameters are absent" + req.getQueryString());
        }
        int[] periodCounters = Arrays.stream(periodCountersStr)
                .flatMapToInt(num -> IntStream.of(Integer.parseInt(num))).toArray();
        int periodSum = Arrays.stream(periodCounters).sum();
        if (companyNames.length > 0 &&
                companyNames.length == companyUrls.length &&
                companyUrls.length == periodCounters.length &&
                periodSum == periodBeginDates.length &&
                periodSum == periodEndDates.length &&
                periodSum == periodTitles.length &&
                periodSum == periodDescriptions.length) {
            CompanyListSection firstSection = new CompanyListSection();
            CompanyListSection secondSection = new CompanyListSection();
            int mainPeriodCounter = 0;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            for (int i = 0; i < companyNames.length; ++i) {
                String companyName = companyNames[i];
                if (companyName != null && !companyName.isEmpty() && periodCounters[i] > 0) {
                    Company company = new Company(companyName, companyUrls[i]);
                    for (int j = 0; j < periodCounters[i]; ++j) {
                        String periodTitle = periodTitles[mainPeriodCounter];
                        LocalDate beginDate = LocalDate.parse("01/" + periodBeginDates[mainPeriodCounter], formatter);
                        String endDateStr = periodEndDates[mainPeriodCounter];
                        LocalDate endDate = endDateStr.equals("Сейчас") || endDateStr.isEmpty() ?
                                DateUtil.NOW : LocalDate.parse("01/" + endDateStr, formatter);
                        if (periodTitle != null && !periodTitle.isEmpty() && beginDate != null && endDate != null) {
                            Company.Period period = new Company.Period(periodTitle,
                                    periodDescriptions[mainPeriodCounter].isEmpty() ? null : periodDescriptions[mainPeriodCounter],
                                    beginDate, endDate);
                            company.addPeriod(period);
                        }
                        ++mainPeriodCounter;
                    }
                    if (i < secondSectionCompanyBeginIndex || secondSectionCompanyBeginIndex == -1) {
                        firstSection.addCompany(company);
                    } else {
                        secondSection.addCompany(company);
                    }
                }
            }
            resume.addSection(firstSectionType, firstSection);
            resume.addSection(secondSectionType, secondSection);
        } else {
            throw new IllegalArgumentException("Wrong POST request for EXPERIENCE or/and EDUCATION section(s): " +
                    "Some parameters have wrong value" + req.getQueryString());
        }
    }
}
