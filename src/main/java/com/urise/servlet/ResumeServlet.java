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
import java.util.stream.Collectors;

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

            if ("edit".equals(action)) {
                Resume resume;
                if (uuid != null && !uuid.isEmpty()) {
                    resume = storage.get(uuid);
                } else {
                    resume = new Resume();

                }
                data.put("resume", resume);
                data.put("contactTypes", ContactType.values());
                editTemplate.process(data, out);
            } else if (uuid != null && !uuid.isEmpty()) {

                Resume resume = storage.get(uuid);
                System.out.println("Просмотр резюме: " + resume.getFullName());
                System.out.println("Контакты: " + resume.getContacts().size());
                System.out.println("Секции всего: " + resume.getSections().size());
                if (resume.getSection(SectionType.EXPERIENCE) != null) {
                    OrganizationSection exp = (OrganizationSection) resume.getSection(SectionType.EXPERIENCE);
                    System.out.println("Опыт организаций: " + exp.getOrganizations().size());
                    if (!exp.getOrganizations().isEmpty()) {
                        System.out.println("Первый период: " + exp.getOrganizations().get(0).getPeriods());
                    }
                }
                data.put("resume", resume);
                viewTemplate.process(data, out);
            } else {

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

        if (fullName == null || fullName.trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Full name must not be empty");
            return;
        }
        fullName = fullName.trim();

        final Resume r;
        final boolean isNew = (uuid == null || uuid.trim().isEmpty());

        if (isNew) {
            r = new Resume(UUID.randomUUID().toString(), fullName);
        } else {
            r = storage.get(uuid.trim());
            r.setFullName(fullName);
        }

        r.getContacts().clear();
        for (ContactType type : ContactType.values()) {
            String value = req.getParameter("contact_" + type.name());
            if (value != null && !value.trim().isEmpty()) {
                r.addContact(type, value.trim());
            }
        }

        r.getSections().clear();
        for (String typeName : new String[]{"PERSONAL", "OBJECTIVE", "ACHIEVEMENT", "QUALIFICATIONS"}) {
            String value = req.getParameter("section_" + typeName);
            if (value != null && !value.trim().isEmpty()) {
                SectionType type = SectionType.valueOf(typeName);
                if (type == SectionType.PERSONAL || type == SectionType.OBJECTIVE) {
                    r.addSection(type, new TextSection(value.trim()));
                } else {
                    List<String> items = Arrays.stream(value.trim().split("\n"))
                            .filter(line -> !line.isEmpty())
                            .collect(Collectors.toList());
                    if (!items.isEmpty()) {
                        r.addSection(type, new ListSection(items));
                    }
                }
            }
        }

        for (SectionType type : new SectionType[]{SectionType.EXPERIENCE, SectionType.EDUCATION}) {
            List<Organization> orgs = new ArrayList<>();
            String prefix = type.name().toLowerCase() + "_org";
            for (int orgIndex = 0; orgIndex < 20; orgIndex++) {
                String orgName = req.getParameter(prefix + orgIndex + "_name");
                if (orgName == null || orgName.trim().isEmpty()) continue;

                String orgUrl = req.getParameter(prefix + orgIndex + "_url");
                if (orgUrl != null) orgUrl = orgUrl.trim();

                List<Organization.Period> periods = new ArrayList<>();

                String[] starts = req.getParameterValues(prefix + orgIndex + "_period_start[]");
                String[] ends = req.getParameterValues(prefix + orgIndex + "_period_end[]");
                String[] titles = req.getParameterValues(prefix + orgIndex + "_period_title[]");
                String[] descs = req.getParameterValues(prefix + orgIndex + "_period_desc[]");

                if (starts != null) {
                    for (int p = 0; p < starts.length; p++) {
                        String startStr = starts[p];
                        if (startStr == null || startStr.trim().isEmpty()) continue;

                        LocalDate start = LocalDate.parse(startStr.trim());
                        LocalDate end = (ends != null && p < ends.length && ends[p] != null && !ends[p].trim().isEmpty())
                                ? LocalDate.parse(ends[p].trim())
                                : DateUtil.NOW;

                        String title = (titles != null && p < titles.length) ? titles[p].trim() : "";
                        String desc = (descs != null && p < descs.length) ? descs[p].trim() : null;

                        if (!title.isEmpty()) {
                            periods.add(new Organization.Period(start, end, title, desc));
                        }
                    }
                }

                if (!periods.isEmpty()) {
                    orgs.add(new Organization(orgName.trim(), orgUrl, periods));
                }
            }

            if (!orgs.isEmpty()) {
                r.addSection(type, new OrganizationSection(orgs));
            } else {
                r.getSections().remove(type);
            }
        }

        // Сохранение
        if (isNew) {
            storage.save(r);
        } else {
            storage.update(r);
        }

        resp.sendRedirect("resume");
    }
}
