package com.urise.servlet;

import com.urise.TestMain.Config;
import com.urise.model.*;
import com.urise.storage.Storage;
import com.urise.util.DateUtil;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.util.*;

@WebServlet("/resume")
public class ResumeServlet extends HttpServlet {

    private Storage storage;
    private Configuration cfg;
    private Template listTemplate;
    private Template viewTemplate;
    private Template editTemplate;

    @Override
    public void init() throws ServletException {
        storage = Config.get().getStorage();

        try {
            cfg = new Configuration(Configuration.VERSION_2_3_33);
            cfg.setDirectoryForTemplateLoading(
                    new File(getServletContext().getRealPath("/WEB-INF/templates"))
            );
            cfg.setDefaultEncoding("UTF-8");
            cfg.setObjectWrapper(new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_33).build());
            listTemplate = cfg.getTemplate("resumes.ftl");
            viewTemplate = cfg.getTemplate("resume.ftl");
            editTemplate = cfg.getTemplate("edit.ftl");
        } catch (IOException e) {
            throw new ServletException("Не могу загрузить шаблон", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String uuid = req.getParameter("uuid");
        String action = req.getParameter("action");
//delete
        if (uuid != null && "delete".equals(action)) {
            storage.delete(uuid);
            resp.sendRedirect("resume");
            return;
        }

        try (Writer out = resp.getWriter()) {
            Map<String, Object> data = new HashMap<>();

            if (uuid == null || uuid.isEmpty()) {

                List<Resume> resumes = storage.getAllSorted();
                List<Map<String, String>> list = resumes.stream()
                        .map(r -> Map.of(
                                "uuid", r.getUuid(),
                                "fullName", r.getFullName(),
                                "email", r.getContact(ContactType.EMAIL) != null
                                        ? r.getContact(ContactType.EMAIL).toString() : "—"
                        ))
                        .toList();

                data.put("resumes", list);
                listTemplate.process(data, out);
            } else if ("edit".equals(action)) {
                Resume resume = (uuid != null && !uuid.isEmpty()) ? storage.get(uuid) : new Resume();
                data.put("resume", resume);
                data.put("contactTypes", ContactType.values());
                editTemplate.process(data, out);
            } else {
                Resume resume = storage.get(uuid);
                data.put("resume", resume);
                viewTemplate.process(data, out);
            }

        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String uuid = req.getParameter("uuid");
        String fullName = req.getParameter("fullName");
        String action = req.getParameter("action");

        Resume r;
        if ("create".equals(action)) {
            r = new Resume(UUID.randomUUID().toString(), fullName);
        } else {
            r = storage.get(uuid);
            r.setFullName(fullName);
        }

        r.getContacts().clear();
        for (ContactType type : ContactType.values()) {
            String value = req.getParameter("contact_" + type.name());
            if (value != null && !value.isEmpty()) {
                r.addContact(type, value);
            }
        }

        r.getSections().clear();
        for (String typeName : new String[]{"PERSONAL", "OBJECTIVE", "ACHIEVEMENT", "QUALIFICATIONS"}) {
            String value = req.getParameter("section_" + typeName);
            if (value != null && !value.isEmpty()) {
                SectionType type = SectionType.valueOf(typeName);
                if (type == SectionType.PERSONAL || type == SectionType.OBJECTIVE) {
                    r.addSection(type, new TextSection(value));
                } else {
                    r.addSection(type, new ListSection(Arrays.asList(value.split("\n"))));
                }
            }
        }

        for (SectionType type : new SectionType[]{SectionType.EXPERIENCE, SectionType.EDUCATION}) {
            String orgValue = req.getParameter("org_" + type.name());
            System.out.println("Parsing org for " + type + ": " + orgValue);
            if (orgValue != null && !orgValue.trim().isEmpty()) {
                List<Organization> orgs = new ArrayList<>();
                String[] orgBlocks = orgValue.split("\n\n");
                for (String block : orgBlocks) {
                    if (block.trim().isEmpty()) continue;
                    String[] lines = block.split("\n");
                    if (lines.length < 1) continue;

                    String[] orgParts = lines[0].split("\\|");
                    String name = orgParts[0].trim();
                    String url = (orgParts.length > 1) ? orgParts[1].trim() : null;

                    List<Organization.Period> periods = new ArrayList<>();
                    for (int i = 1; i < lines.length; i++) {
                        if (lines[i].trim().isEmpty()) continue;
                        String[] periodParts = lines[i].split("\\|");
                        if (periodParts.length < 2) continue;

                        String dateStr = periodParts[0].trim();
                        String[] dates = dateStr.split("-");
                        LocalDate start = LocalDate.parse(dates[0].trim());
                        LocalDate end = (dates.length > 1 && !dates[1].trim().equals("NOW")) ? LocalDate.parse(dates[1].trim()) : DateUtil.NOW;

                        String title = periodParts[1].trim();
                        String desc = (periodParts.length > 2) ? periodParts[2].trim() : null;

                        periods.add(new Organization.Period(start, end, title, desc));
                    }

                    if (!periods.isEmpty()) {
                        orgs.add(new Organization(name, url, periods));
                    }
                }
                if (!orgs.isEmpty()) {
                    r.addSection(type, new OrganizationSection(orgs));
                }
            }
        }

        if ("create".equals(action)) {
            storage.save(r);
        } else {
            storage.update(r);
        }

        resp.sendRedirect("resume");
    }
}
