package com.urise.servlet;

import com.urise.TestMain.Config;
import com.urise.model.ContactType;
import com.urise.model.Resume;
import com.urise.storage.Storage;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/resume")
public class ResumeServlet extends HttpServlet {

    private Storage storage;
    private Configuration cfg;
    private Template listTemplate;
    private Template viewTemplate;

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

    }
}
