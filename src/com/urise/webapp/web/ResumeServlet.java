package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.ResumeTestData;
import com.urise.webapp.model.*;
import com.urise.webapp.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

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
            }
            else {
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
        }
        else {
            resume = new Resume(uuid, req.getParameter("fullName"));
            fillResume(req, resume);
            storage.update(resume);
        }
        resp.sendRedirect(req.getContextPath() + "/resume?action=view&uuid=" + resume.getUuid());
    }

    private void fillResume (HttpServletRequest req, Resume resume) {
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
                case EXPERIENCE:
                case EDUCATION:
                    //TODO
                    break;
            }
        }
    }
}
