package com.studentmanagement;

import com.studentmanagement.dao.StudentDAO;
import com.studentmanagement.model.Student;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Embedded Standalone Java Web Server (Zero Tomcat Setup Required).
 * Uses Java's built-in HttpServer (com.sun.net.httpserver.HttpServer).
 * Connects directly to MySQL via StudentDAO and serves modern responsive UI.
 */
public class AppServer {

    private static final int PORT = 8085;
    private static final StudentDAO studentDAO = new StudentDAO();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // Map Routes
        server.createContext("/", new DashboardHandler());
        server.createContext("/dashboard", new DashboardHandler());
        server.createContext("/students", new StudentListHandler());
        server.createContext("/add-student.html", new AddStudentPageHandler());
        server.createContext("/addStudent", new AddStudentActionHandler());
        server.createContext("/searchStudent", new SearchStudentHandler());
        server.createContext("/editStudent", new EditStudentHandler());
        server.createContext("/updateStudent", new UpdateStudentHandler());
        server.createContext("/deleteStudent", new DeleteStudentHandler());
        
        // Static assets (CSS & JS)
        server.createContext("/css/", new StaticFileHandler("WebContent/css/", "text/css"));
        server.createContext("/js/", new StaticFileHandler("WebContent/js/", "application/javascript"));

        server.setExecutor(null); // default executor
        server.start();

        System.out.println("================================================================");
        System.out.println("   STUDENT MANAGEMENT SYSTEM - STANDALONE SERVER STARTED");
        System.out.println("================================================================");
        System.out.println("Server running live at: http://localhost:" + PORT + "/");
        System.out.println("No Apache Tomcat setup was required!");
        System.out.println("================================================================");

        // Auto-open in browser if desktop is supported
        try {
            if (java.awt.Desktop.isDesktopSupported() && java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.BROWSE)) {
                java.awt.Desktop.getDesktop().browse(new java.net.URI("http://localhost:" + PORT + "/"));
            }
        } catch (Exception ignored) {}
    }

    // =========================================================================
    // 1. DASHBOARD HANDLER
    // =========================================================================
    static class DashboardHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            int totalStudents = studentDAO.getTotalStudentCount();
            List<Student> recentStudents = studentDAO.getRecentStudents(5);

            StringBuilder html = new StringBuilder();
            html.append(getHeader("Dashboard - Student Management System", "dashboard"));
            html.append("<main class='container'>");
            html.append("<div class='page-header'>");
            html.append("<div><h1 class='page-title'>Admin Dashboard</h1><p class='page-subtitle'>Welcome to the Student Management System portal.</p></div>");
            html.append("<div><a href='/add-student.html' class='btn btn-primary'>+ Register New Student</a></div>");
            html.append("</div>");

            // Stat cards
            html.append("<div class='dashboard-grid'>");
            html.append("<div class='stat-card'><div class='stat-info'><h3>Total Enrolled Students</h3><div class='stat-number'>").append(totalStudents).append("</div></div><div class='stat-icon icon-blue'>&#127891;</div></div>");
            html.append("<div class='stat-card'><div class='stat-info'><h3>Academic Departments</h3><div class='stat-number'>5</div></div><div class='stat-icon icon-green'>&#127979;</div></div>");
            html.append("<div class='stat-card'><div class='stat-info'><h3>Academic Years</h3><div class='stat-number'>4</div></div><div class='stat-icon icon-purple'>&#128197;</div></div>");
            html.append("</div>");

            // Quick actions
            html.append("<div class='quick-actions'>");
            html.append("<div class='quick-actions-text'><h2>Manage Student Records</h2><p>Add new admissions, view complete directory, update academic details, or search records.</p></div>");
            html.append("<div class='quick-actions-btns'><a href='/students' class='btn btn-secondary'>View All Students</a><a href='/add-student.html' class='btn btn-primary'>+ Add New Student</a></div>");
            html.append("</div>");

            // Recent students
            html.append("<div class='card'><div class='card-header'><h2 class='card-title'>Recent Student Enrollments</h2><a href='/students' class='btn btn-sm btn-secondary'>View Complete List &rarr;</a></div>");
            html.append("<div class='card-body' style='padding:0;'>");
            if (recentStudents == null || recentStudents.isEmpty()) {
                html.append("<div class='empty-state'><div class='empty-icon'>&#128101;</div><h3 class='empty-title'>No Students Found</h3><p class='empty-desc'>Get started by enrolling your first student.</p><a href='/add-student.html' class='btn btn-primary'>+ Add Student</a></div>");
            } else {
                html.append("<div class='table-responsive'><table class='table'><thead><tr><th>ID</th><th>Student Name</th><th>Email Address</th><th>Phone</th><th>Department</th><th>Year</th><th>Action</th></tr></thead><tbody>");
                for (Student s : recentStudents) {
                    html.append("<tr>");
                    html.append("<td><strong>#").append(s.getStudentId()).append("</strong></td>");
                    html.append("<td><strong>").append(escapeHtml(s.getStudentName())).append("</strong></td>");
                    html.append("<td>").append(escapeHtml(s.getEmail())).append("</td>");
                    html.append("<td>").append(escapeHtml(s.getPhone())).append("</td>");
                    html.append("<td><span class='badge badge-blue'>").append(escapeHtml(s.getDepartment())).append("</span></td>");
                    html.append("<td><span class='badge badge-gray'>Year ").append(s.getYear()).append("</span></td>");
                    html.append("<td><a href='/editStudent?id=").append(s.getStudentId()).append("' class='btn btn-sm btn-secondary'>Edit</a></td>");
                    html.append("</tr>");
                }
                html.append("</tbody></table></div>");
            }
            html.append("</div></div></main>");
            html.append(getFooter());

            sendResponse(exchange, 200, html.toString(), "text/html; charset=UTF-8");
        }
    }

    // =========================================================================
    // 2. STUDENT LIST & DIRECTORY HANDLER
    // =========================================================================
    static class StudentListHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());
            String message = queryParams.get("message");
            String error = queryParams.get("error");
            String searchQuery = queryParams.get("query");

            List<Student> students;
            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                students = studentDAO.searchStudents(searchQuery.trim());
            } else {
                students = studentDAO.getAllStudents();
            }

            StringBuilder html = new StringBuilder();
            html.append(getHeader("Student Directory - Student Management System", "students"));
            html.append("<main class='container'>");
            html.append("<div class='page-header'>");
            html.append("<div><h1 class='page-title'>Student Directory</h1><p class='page-subtitle'>View, search, edit, and manage all enrolled students.</p></div>");
            html.append("<div><a href='/add-student.html' class='btn btn-primary'>+ Add New Student</a></div>");
            html.append("</div>");

            // Alerts
            if ("added".equals(message)) {
                html.append("<div class='alert alert-success'><span>&#10003; <strong>Success!</strong> New student record has been added successfully.</span><button class='alert-close'>&times;</button></div>");
            } else if ("updated".equals(message)) {
                html.append("<div class='alert alert-success'><span>&#10003; <strong>Success!</strong> Student information has been updated successfully.</span><button class='alert-close'>&times;</button></div>");
            } else if ("deleted".equals(message)) {
                html.append("<div class='alert alert-success'><span>&#10003; <strong>Success!</strong> Student record has been deleted successfully.</span><button class='alert-close'>&times;</button></div>");
            } else if (error != null) {
                html.append("<div class='alert alert-danger'><span>&#9888; <strong>Error:</strong> Action could not be completed.</span><button class='alert-close'>&times;</button></div>");
            }

            html.append("<div class='card'><div class='card-body'>");
            // Toolbar
            html.append("<div class='toolbar'>");
            html.append("<form action='/searchStudent' method='GET' class='search-form'>");
            html.append("<div class='search-input-group'><span class='search-icon-inside'>&#128269;</span>");
            html.append("<input type='text' name='query' class='search-input' placeholder='Search by ID or Name...' value='").append(searchQuery != null ? escapeHtml(searchQuery) : "").append("'></div>");
            html.append("<button type='submit' class='btn btn-primary btn-sm'>Search</button>");
            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                html.append("<a href='/students' class='btn btn-secondary btn-sm'>Reset</a>");
            }
            html.append("</form>");
            html.append("<div style='max-width:250px;width:100%;'><input type='text' id='tableSearchInput' class='form-control' style='padding:0.45rem 0.75rem;font-size:0.85rem;' placeholder='Quick filter current list...'></div>");
            html.append("</div>");

            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                html.append("<p style='margin-bottom:1rem;font-size:0.9rem;color:var(--text-muted);'>Showing search results for: <strong>\"").append(escapeHtml(searchQuery)).append("\"</strong> (").append(students.size()).append(" records found)</p>");
            }

            if (students.isEmpty()) {
                html.append("<div class='empty-state'><div class='empty-icon'>&#128269;</div><h3 class='empty-title'>No Students Found</h3><p class='empty-desc'>No student records match your query.</p><a href='/add-student.html' class='btn btn-primary'>+ Add Student</a></div>");
            } else {
                html.append("<div class='table-responsive'><table class='table' id='studentsTable'><thead><tr><th>ID</th><th>Student Name</th><th>Email Address</th><th>Phone</th><th>Department</th><th>Year</th><th style='text-align:center;'>Actions</th></tr></thead><tbody>");
                for (Student s : students) {
                    html.append("<tr>");
                    html.append("<td><strong>#").append(s.getStudentId()).append("</strong></td>");
                    html.append("<td><strong>").append(escapeHtml(s.getStudentName())).append("</strong></td>");
                    html.append("<td>").append(escapeHtml(s.getEmail())).append("</td>");
                    html.append("<td>").append(escapeHtml(s.getPhone())).append("</td>");
                    html.append("<td><span class='badge badge-blue'>").append(escapeHtml(s.getDepartment())).append("</span></td>");
                    html.append("<td><span class='badge badge-gray'>Year ").append(s.getYear()).append("</span></td>");
                    html.append("<td style='text-align:center;'><div class='action-buttons' style='justify-content:center;'>");
                    html.append("<a href='/editStudent?id=").append(s.getStudentId()).append("' class='btn btn-sm btn-secondary'>&#9998; Edit</a>");
                    html.append("<button type='button' class='btn btn-sm btn-danger' onclick=\"confirmDelete('/deleteStudent?id=").append(s.getStudentId()).append("', '").append(escapeHtml(s.getStudentName())).append("')\">&#128465; Delete</button>");
                    html.append("</div></td>");
                    html.append("</tr>");
                }
                html.append("</tbody></table></div>");
            }

            html.append("</div></div>");
            html.append(getDeleteModal());
            html.append("</main>");
            html.append(getFooter());

            sendResponse(exchange, 200, html.toString(), "text/html; charset=UTF-8");
        }
    }

    // =========================================================================
    // 3. ADD STUDENT PAGE HANDLER
    // =========================================================================
    static class AddStudentPageHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            File htmlFile = new File("WebContent/add-student.html");
            if (htmlFile.exists()) {
                byte[] bytes = java.nio.file.Files.readAllBytes(htmlFile.toPath());
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(200, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            } else {
                sendResponse(exchange, 404, "Page not found", "text/plain");
            }
        }
    }

    // =========================================================================
    // 4. ADD STUDENT ACTION HANDLER (POST)
    // =========================================================================
    static class AddStudentActionHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Map<String, String> params = parseFormData(body);

                String name = params.get("name");
                String email = params.get("email");
                String phone = params.get("phone");
                String department = params.get("department");
                String yearStr = params.get("year");

                int year = 1;
                try { year = Integer.parseInt(yearStr); } catch (Exception ignored) {}

                Student newStudent = new Student(name, email, phone, department, year);
                boolean success = studentDAO.addStudent(newStudent);

                if (success) {
                    redirect(exchange, "/students?message=added");
                } else {
                    redirect(exchange, "/add-student.html?error=failed");
                }
            } else {
                redirect(exchange, "/add-student.html");
            }
        }
    }

    // =========================================================================
    // 5. SEARCH STUDENT HANDLER
    // =========================================================================
    static class SearchStudentHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, String> params = parseQueryParams(exchange.getRequestURI().getQuery());
            String query = params.get("query");
            redirect(exchange, "/students?query=" + (query != null ? java.net.URLEncoder.encode(query, StandardCharsets.UTF_8) : ""));
        }
    }

    // =========================================================================
    // 6. EDIT STUDENT FORM HANDLER
    // =========================================================================
    static class EditStudentHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, String> params = parseQueryParams(exchange.getRequestURI().getQuery());
            String idStr = params.get("id");
            if (idStr == null) {
                redirect(exchange, "/students");
                return;
            }

            int id = Integer.parseInt(idStr);
            Student student = studentDAO.getStudentById(id);
            if (student == null) {
                redirect(exchange, "/students?error=notfound");
                return;
            }

            String currentDept = student.getDepartment() != null ? student.getDepartment() : "";
            int currentYear = student.getYear();

            StringBuilder html = new StringBuilder();
            html.append(getHeader("Edit Student #" + student.getStudentId(), "students"));
            html.append("<main class='container'>");
            html.append("<div class='page-header'>");
            html.append("<div><h1 class='page-title'>Edit Student Information</h1><p class='page-subtitle'>Update record for Student ID #").append(student.getStudentId()).append(" (").append(escapeHtml(student.getStudentName())).append(")</p></div>");
            html.append("<div><a href='/students' class='btn btn-secondary'>&larr; Back to Student List</a></div>");
            html.append("</div>");

            html.append("<div class='form-container'><div class='card'>");
            html.append("<div class='card-header'><h2 class='card-title'>Modify Student Details</h2></div>");
            html.append("<div class='card-body'>");
            html.append("<form id='studentForm' action='/updateStudent' method='POST' novalidate>");
            html.append("<input type='hidden' name='studentId' value='").append(student.getStudentId()).append("'>");

            html.append("<div class='form-group'><label class='form-label'>Student ID (Primary Key)</label><input type='text' class='form-control' value='#").append(student.getStudentId()).append("' readonly disabled></div>");

            html.append("<div class='form-group'><label for='name' class='form-label'>Full Name <span class='required'>*</span></label><input type='text' id='name' name='name' class='form-control' value='").append(escapeHtml(student.getStudentName())).append("' required><div id='nameError' class='error-feedback'></div></div>");

            html.append("<div class='form-group'><label for='email' class='form-label'>Email Address <span class='required'>*</span></label><input type='email' id='email' name='email' class='form-control' value='").append(escapeHtml(student.getEmail())).append("' required><div id='emailError' class='error-feedback'></div></div>");

            html.append("<div class='form-group'><label for='phone' class='form-label'>Phone Number <span class='required'>*</span></label><input type='tel' id='phone' name='phone' class='form-control' value='").append(escapeHtml(student.getPhone())).append("' maxlength='10' required><div id='phoneError' class='error-feedback'></div></div>");

            html.append("<div class='form-row'>");
            html.append("<div class='form-group'><label for='department' class='form-label'>Department <span class='required'>*</span></label><select id='department' name='department' class='form-control' required>");
            String[] depts = {"Computer Science", "Information Technology", "Electronics & Comm", "Mechanical Engineering", "Civil Engineering", "Electrical Engineering", "Biotechnology"};
            for (String d : depts) {
                html.append("<option value='").append(d).append("'").append(d.equals(currentDept) ? " selected" : "").append(">").append(d).append("</option>");
            }
            html.append("</select><div id='departmentError' class='error-feedback'></div></div>");

            html.append("<div class='form-group'><label for='year' class='form-label'>Academic Year <span class='required'>*</span></label><select id='year' name='year' class='form-control' required>");
            for (int y = 1; y <= 4; y++) {
                html.append("<option value='").append(y).append("'").append(y == currentYear ? " selected" : "").append(">Year ").append(y).append("</option>");
            }
            html.append("</select><div id='yearError' class='error-feedback'></div></div>");
            html.append("</div>");

            html.append("<div class='form-actions'><a href='/students' class='btn btn-secondary'>Cancel</a><button type='submit' class='btn btn-primary'>&#10003; Update Changes</button></div>");
            html.append("</form></div></div></div></main>");
            html.append(getFooter());

            sendResponse(exchange, 200, html.toString(), "text/html; charset=UTF-8");
        }
    }

    // =========================================================================
    // 7. UPDATE STUDENT ACTION HANDLER (POST)
    // =========================================================================
    static class UpdateStudentActionHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Handled below
        }
    }

    static class UpdateStudentHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Map<String, String> params = parseFormData(body);

                int studentId = Integer.parseInt(params.get("studentId"));
                String name = params.get("name");
                String email = params.get("email");
                String phone = params.get("phone");
                String department = params.get("department");
                int year = Integer.parseInt(params.get("year"));

                Student student = new Student(studentId, name, email, phone, department, year);
                boolean success = studentDAO.updateStudent(student);

                if (success) {
                    redirect(exchange, "/students?message=updated");
                } else {
                    redirect(exchange, "/editStudent?id=" + studentId + "&error=failed");
                }
            } else {
                redirect(exchange, "/students");
            }
        }
    }

    // =========================================================================
    // 8. DELETE STUDENT ACTION HANDLER
    // =========================================================================
    static class DeleteStudentHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, String> params = parseQueryParams(exchange.getRequestURI().getQuery());
            String idStr = params.get("id");
            if (idStr != null) {
                try {
                    int studentId = Integer.parseInt(idStr);
                    studentDAO.deleteStudent(studentId);
                    redirect(exchange, "/students?message=deleted");
                    return;
                } catch (Exception ignored) {}
            }
            redirect(exchange, "/students?error=deletefailed");
        }
    }

    // =========================================================================
    // 9. STATIC FILE HANDLER (CSS & JS)
    // =========================================================================
    static class StaticFileHandler implements HttpHandler {
        private final String baseDir;
        private final String contentType;

        public StaticFileHandler(String baseDir, String contentType) {
            this.baseDir = baseDir;
            this.contentType = contentType;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String filename = path.substring(path.lastIndexOf('/') + 1);
            File file = new File(baseDir + filename);

            if (file.exists() && !file.isDirectory()) {
                byte[] bytes = java.nio.file.Files.readAllBytes(file.toPath());
                exchange.getResponseHeaders().set("Content-Type", contentType);
                exchange.sendResponseHeaders(200, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            } else {
                sendResponse(exchange, 404, "File not found", "text/plain");
            }
        }
    }

    // =========================================================================
    // HELPER METHODS
    // =========================================================================
    private static String getHeader(String title, String activeNav) {
        return "<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>" + title + "</title><link rel='stylesheet' href='/css/style.css'></head><body>" +
                "<nav class='navbar'><div class='nav-container'>" +
                "<a href='/' class='nav-brand'><div class='brand-icon'>S</div><span>StudentMS</span></a>" +
                "<ul class='nav-links'>" +
                "<li><a href='/' class='nav-item" + ("dashboard".equals(activeNav) ? " active" : "") + "'>Dashboard</a></li>" +
                "<li><a href='/students' class='nav-item" + ("students".equals(activeNav) ? " active" : "") + "'>Students</a></li>" +
                "<li><a href='/add-student.html' class='nav-item nav-btn-add'>+ Add Student</a></li>" +
                "</ul></div></nav>";
    }

    private static String getFooter() {
        return "<footer class='footer'><p>&copy; " + java.time.Year.now().getValue() + " Student Management System. Built with Java, JDBC, MySQL, HTML, CSS, and Vanilla JS.</p></footer><script src='/js/script.js'></script></body></html>";
    }

    private static String getDeleteModal() {
        return "<div class='modal-overlay' id='deleteModal'><div class='modal-box'><div class='modal-icon-danger'>&#9888;</div><h3 class='modal-title'>Delete Student Record?</h3><p class='modal-message'>Are you sure you want to delete <strong id='deleteStudentName'>this student</strong>? This action cannot be undone.</p><div class='modal-actions'><button type='button' class='btn btn-secondary' id='cancelDeleteBtn'>Cancel</button><button type='button' class='btn btn-danger' id='confirmDeleteBtn'>&#128465; Yes, Delete</button></div></div></div>";
    }

    private static void sendResponse(HttpExchange exchange, int statusCode, String response, String contentType) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static void redirect(HttpExchange exchange, String location) throws IOException {
        exchange.getResponseHeaders().set("Location", location);
        exchange.sendResponseHeaders(302, -1);
    }

    private static Map<String, String> parseQueryParams(String query) {
        Map<String, String> result = new HashMap<>();
        if (query == null || query.isEmpty()) return result;
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                result.put(URLDecoder.decode(kv[0], StandardCharsets.UTF_8), URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
            }
        }
        return result;
    }

    private static Map<String, String> parseFormData(String body) {
        return parseQueryParams(body);
    }

    private static String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;");
    }
}
