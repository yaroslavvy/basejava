package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResumeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        String uuid = req.getParameter("uuid");
        Storage storage = Config.get().getStorage();
        if (uuid == null) {
            req.setAttribute("resumeList", storage.getAllSorted());
            req.getRequestDispatcher("/resume_list.jsp").forward(req, resp);
        } else {
            req.setAttribute("resume", storage.get(uuid));
            req.getRequestDispatcher("/resume.jsp").forward(req, resp);
        }
    }
}
