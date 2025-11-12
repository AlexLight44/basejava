package com.urise.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/resume")
public class ResumeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","text/html; charset=UTF-8");
        String name = request.getParameter("name");
        response.getWriter().write(name == null ? "Hello Resume" : "Hello " + name);
        PrintWriter out = response.getWriter();
        out.write("<!DOCTYPE html>");
        out.write("<html><head><title>Резюме</title></head><body>");
        out.write("<h2>Список резюме</h2>");

        out.write("<table border='1'>");
        out.write("<tr><th>UUID</th><th>Full Name</th></tr>");

        out.write("<tr><td>111</td><td>Иванов</td></tr>");
        out.write("<tr><td>222</td><td>Петрова</td></tr>");
        out.write("<tr><td>333</td><td>Сидоров</td></tr>");

        out.write("</table>");
        out.write("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

    }
}
