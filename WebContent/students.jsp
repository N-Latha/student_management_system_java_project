<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.studentmanagement.model.Student" %>
<%@ page import="com.studentmanagement.dao.StudentDAO" %>

<%
    // Ensure studentList is populated even if students.jsp is visited directly
    List<Student> studentList = (List<Student>) request.getAttribute("studentList");
    String searchQuery = (String) request.getAttribute("searchQuery");
    if (searchQuery == null) {
        searchQuery = request.getParameter("query");
    }

    if (studentList == null) {
        StudentDAO dao = new StudentDAO();
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            studentList = dao.searchStudents(searchQuery.trim());
        } else {
            studentList = dao.getAllStudents();
        }
    }

    // Check message parameters from redirects
    String message = request.getParameter("message");
    String error = request.getParameter("error");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>All Students - Student Management System</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

    <!-- 1. Navigation Bar -->
    <nav class="navbar">
        <div class="nav-container">
            <a href="index.jsp" class="nav-brand">
                <div class="brand-icon">S</div>
                <span>StudentMS</span>
            </a>
            <ul class="nav-links">
                <li><a href="index.jsp" class="nav-item">Dashboard</a></li>
                <li><a href="students" class="nav-item active">Students</a></li>
                <li><a href="add-student.html" class="nav-item nav-btn-add">+ Add Student</a></li>
            </ul>
        </div>
    </nav>

    <!-- 2. Main Content -->
    <main class="container">

        <!-- Page Header -->
        <div class="page-header">
            <div>
                <h1 class="page-title">Student Directory</h1>
                <p class="page-subtitle">View, search, edit, and manage all enrolled students.</p>
            </div>
            <div>
                <a href="add-student.html" class="btn btn-primary">+ Add New Student</a>
            </div>
        </div>

        <!-- Success & Error Alert Banners -->
        <% if ("added".equals(message)) { %>
            <div class="alert alert-success">
                <span>&#10003; <strong>Success!</strong> New student record has been added successfully.</span>
                <button class="alert-close">&times;</button>
            </div>
        <% } else if ("updated".equals(message)) { %>
            <div class="alert alert-success">
                <span>&#10003; <strong>Success!</strong> Student information has been updated successfully.</span>
                <button class="alert-close">&times;</button>
            </div>
        <% } else if ("deleted".equals(message)) { %>
            <div class="alert alert-success">
                <span>&#10003; <strong>Success!</strong> Student record has been deleted successfully.</span>
                <button class="alert-close">&times;</button>
            </div>
        <% } else if (error != null) { %>
            <div class="alert alert-danger">
                <span>&#9888; <strong>Error:</strong> Failed to complete the operation. (<%= error %>)</span>
                <button class="alert-close">&times;</button>
            </div>
        <% } %>

        <!-- Card Container -->
        <div class="card">
            <div class="card-body">
                
                <!-- Toolbar: Search Form & Instant Filter -->
                <div class="toolbar">
                    <!-- Server-side Search Form calling SearchStudentServlet -->
                    <form action="searchStudent" method="GET" class="search-form">
                        <div class="search-input-group">
                            <span class="search-icon-inside">&#128269;</span>
                            <input type="text" name="query" class="search-input" 
                                   placeholder="Search by ID or Name..." 
                                   value="<%= (searchQuery != null) ? searchQuery : "" %>">
                        </div>
                        <button type="submit" class="btn btn-primary btn-sm">Search</button>
                        <% if (searchQuery != null && !searchQuery.trim().isEmpty()) { %>
                            <a href="students" class="btn btn-secondary btn-sm" title="Clear search">Reset</a>
                        <% } %>
                    </form>

                    <!-- Client-side Instant Filter helper -->
                    <div style="max-width: 250px; width: 100%;">
                        <input type="text" id="tableSearchInput" class="form-control" style="padding: 0.45rem 0.75rem; font-size: 0.85rem;" placeholder="Quick filter current list...">
                    </div>
                </div>

                <!-- Search status indicator -->
                <% if (searchQuery != null && !searchQuery.trim().isEmpty()) { %>
                    <p style="margin-bottom: 1rem; font-size: 0.9rem; color: var(--text-muted);">
                        Showing search results for: <strong>"<%= searchQuery %>"</strong> 
                        (<%= (studentList != null ? studentList.size() : 0) %> records found)
                    </p>
                <% } %>

                <!-- Students Table View -->
                <% if (studentList == null || studentList.isEmpty()) { %>
                    <div class="empty-state">
                        <div class="empty-icon">&#128269;</div>
                        <h3 class="empty-title">No Students Found</h3>
                        <p class="empty-desc">
                            <%= (searchQuery != null && !searchQuery.trim().isEmpty()) 
                                ? "No student matched your search query. Try searching with a different ID or name." 
                                : "There are currently no students registered in the database." %>
                        </p>
                        <% if (searchQuery != null && !searchQuery.trim().isEmpty()) { %>
                            <a href="students" class="btn btn-secondary">View All Students</a>
                        <% } else { %>
                            <a href="add-student.html" class="btn btn-primary">+ Add First Student</a>
                        <% } %>
                    </div>
                <% } else { %>
                    <div class="table-responsive">
                        <table class="table" id="studentsTable">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Student Name</th>
                                    <th>Email Address</th>
                                    <th>Phone</th>
                                    <th>Department</th>
                                    <th>Year</th>
                                    <th style="text-align: center;">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (Student s : studentList) { %>
                                    <tr>
                                        <td><strong>#<%= s.getStudentId() %></strong></td>
                                        <td><strong><%= s.getStudentName() %></strong></td>
                                        <td><%= s.getEmail() %></td>
                                        <td><%= s.getPhone() %></td>
                                        <td>
                                            <span class="badge badge-blue"><%= s.getDepartment() %></span>
                                        </td>
                                        <td>
                                            <span class="badge badge-gray">Year <%= s.getYear() %></span>
                                        </td>
                                        <td>
                                            <div class="action-buttons" style="justify-content: center;">
                                                <!-- Edit Button -->
                                                <a href="editStudent?id=<%= s.getStudentId() %>" 
                                                   class="btn btn-sm btn-secondary" 
                                                   title="Edit Student">
                                                    &#9998; Edit
                                                </a>
                                                
                                                <!-- Delete Button triggering confirmation modal -->
                                                <button type="button" 
                                                        class="btn btn-sm btn-danger" 
                                                        onclick="confirmDelete('deleteStudent?id=<%= s.getStudentId() %>', '<%= s.getStudentName() %>')"
                                                        title="Delete Student">
                                                    &#128465; Delete
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                <% } %>

            </div>
        </div>

    </main>

    <!-- 3. Delete Confirmation Modal (Pure Vanilla JavaScript) -->
    <div class="modal-overlay" id="deleteModal">
        <div class="modal-box">
            <div class="modal-icon-danger">&#9888;</div>
            <h3 class="modal-title">Delete Student Record?</h3>
            <p class="modal-message">
                Are you sure you want to delete <strong id="deleteStudentName">this student</strong>? 
                This action cannot be undone.
            </p>
            <div class="modal-actions">
                <button type="button" class="btn btn-secondary" id="cancelDeleteBtn">Cancel</button>
                <button type="button" class="btn btn-danger" id="confirmDeleteBtn">&#128465; Yes, Delete</button>
            </div>
        </div>
    </div>

    <!-- 4. Footer -->
    <footer class="footer">
        <p>&copy; Student Management System. Built with Java Servlets, JDBC, MySQL, and Vanilla JS.</p>
    </footer>

    <script src="js/script.js"></script>
</body>
</html>
