<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.studentmanagement.model.Student" %>
<%@ page import="com.studentmanagement.dao.StudentDAO" %>

<%
    // Ensure dashboard statistics are loaded even if index.jsp is visited directly
    Integer totalStudents = (Integer) request.getAttribute("totalStudents");
    List<Student> recentStudents = (List<Student>) request.getAttribute("recentStudents");

    if (totalStudents == null || recentStudents == null) {
        StudentDAO dao = new StudentDAO();
        totalStudents = dao.getTotalStudentCount();
        recentStudents = dao.getRecentStudents(5);
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Student Management System</title>
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
                <li><a href="index.jsp" class="nav-item active">Dashboard</a></li>
                <li><a href="students" class="nav-item">Students</a></li>
                <li><a href="add-student.html" class="nav-item nav-btn-add">+ Add Student</a></li>
            </ul>
        </div>
    </nav>

    <!-- 2. Main Dashboard Content -->
    <main class="container">
        
        <!-- Header -->
        <div class="page-header">
            <div>
                <h1 class="page-title">Admin Dashboard</h1>
                <p class="page-subtitle">Welcome to the Student Management System portal.</p>
            </div>
            <div>
                <a href="add-student.html" class="btn btn-primary">+ Register New Student</a>
            </div>
        </div>

        <!-- Metric Cards -->
        <div class="dashboard-grid">
            <div class="stat-card">
                <div class="stat-info">
                    <h3>Total Enrolled Students</h3>
                    <div class="stat-number"><%= totalStudents %></div>
                </div>
                <div class="stat-icon icon-blue">&#127891;</div>
            </div>

            <div class="stat-card">
                <div class="stat-info">
                    <h3>Academic Departments</h3>
                    <div class="stat-number">5</div>
                </div>
                <div class="stat-icon icon-green">&#127979;</div>
            </div>

            <div class="stat-card">
                <div class="stat-info">
                    <h3>Academic Years</h3>
                    <div class="stat-number">4</div>
                </div>
                <div class="stat-icon icon-purple">&#128197;</div>
            </div>
        </div>

        <!-- Quick Actions Banner -->
        <div class="quick-actions">
            <div class="quick-actions-text">
                <h2>Manage Student Records</h2>
                <p>Add new admissions, view complete directory, update academic details, or search records.</p>
            </div>
            <div class="quick-actions-btns">
                <a href="students" class="btn btn-secondary">View All Students</a>
                <a href="add-student.html" class="btn btn-primary">+ Add New Student</a>
            </div>
        </div>

        <!-- Recent Students Card -->
        <div class="card">
            <div class="card-header">
                <h2 class="card-title">Recent Student Enrollments</h2>
                <a href="students" class="btn btn-sm btn-secondary">View Complete List &rarr;</a>
            </div>
            <div class="card-body" style="padding: 0;">
                <% if (recentStudents == null || recentStudents.isEmpty()) { %>
                    <div class="empty-state">
                        <div class="empty-icon">&#128101;</div>
                        <h3 class="empty-title">No Students Found</h3>
                        <p class="empty-desc">Get started by enrolling your first student into the system.</p>
                        <a href="add-student.html" class="btn btn-primary">+ Add Student</a>
                    </div>
                <% } else { %>
                    <div class="table-responsive">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Student Name</th>
                                    <th>Email Address</th>
                                    <th>Phone</th>
                                    <th>Department</th>
                                    <th>Year</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (Student s : recentStudents) { %>
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
                                            <a href="editStudent?id=<%= s.getStudentId() %>" class="btn btn-sm btn-secondary">Edit</a>
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

    <!-- 3. Footer -->
    <footer class="footer">
        <p>&copy; <%= java.time.Year.now().getValue() %> Student Management System. Built with Java Servlets, JDBC, MySQL, and Vanilla JS.</p>
    </footer>

    <script src="js/script.js"></script>
</body>
</html>
