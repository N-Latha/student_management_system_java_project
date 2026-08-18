<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.studentmanagement.model.Student" %>
<%@ page import="com.studentmanagement.dao.StudentDAO" %>

<%
    // Obtain student object from request scope
    Student student = (Student) request.getAttribute("student");

    // Fallback: If accessed directly via URL with ?id=X
    if (student == null) {
        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idParam.trim());
                StudentDAO dao = new StudentDAO();
                student = dao.getStudentById(id);
            } catch (NumberFormatException ignored) {}
        }
    }

    if (student == null) {
        response.sendRedirect("students?error=student_not_found");
        return;
    }

    String currentDept = student.getDepartment() != null ? student.getDepartment() : "";
    int currentYear = student.getYear();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Student #<%= student.getStudentId() %> - Student Management System</title>
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
                <li><a href="students" class="nav-item">Students</a></li>
                <li><a href="add-student.html" class="nav-item nav-btn-add">+ Add Student</a></li>
            </ul>
        </div>
    </nav>

    <!-- 2. Main Form Content -->
    <main class="container">

        <div class="page-header">
            <div>
                <h1 class="page-title">Edit Student Information</h1>
                <p class="page-subtitle">Update record for Student ID #<%= student.getStudentId() %> (<%= student.getStudentName() %>)</p>
            </div>
            <div>
                <a href="students" class="btn btn-secondary">&larr; Back to Student List</a>
            </div>
        </div>

        <div class="form-container">
            <div class="card">
                <div class="card-header">
                    <h2 class="card-title">Modify Student Details</h2>
                </div>
                <div class="card-body">
                    <!-- Form submitting updated data to UpdateStudentServlet -->
                    <form id="studentForm" action="updateStudent" method="POST" novalidate>
                        
                        <!-- Hidden Student ID for database UPDATE WHERE condition -->
                        <input type="hidden" name="studentId" value="<%= student.getStudentId() %>">

                        <!-- Readonly Student ID Display -->
                        <div class="form-group">
                            <label class="form-label">Student ID (Primary Key)</label>
                            <input type="text" class="form-control" value="#<%= student.getStudentId() %>" readonly disabled>
                            <small class="form-text">Student ID is auto-generated and cannot be altered.</small>
                        </div>

                        <!-- Full Name Field -->
                        <div class="form-group">
                            <label for="name" class="form-label">Full Name <span class="required">*</span></label>
                            <input type="text" id="name" name="name" class="form-control" 
                                   value="<%= student.getStudentName() %>" required>
                            <div id="nameError" class="error-feedback"></div>
                        </div>

                        <!-- Email Address Field -->
                        <div class="form-group">
                            <label for="email" class="form-label">Email Address <span class="required">*</span></label>
                            <input type="email" id="email" name="email" class="form-control" 
                                   value="<%= student.getEmail() %>" required>
                            <div id="emailError" class="error-feedback"></div>
                        </div>

                        <!-- Phone Number Field -->
                        <div class="form-group">
                            <label for="phone" class="form-label">Phone Number <span class="required">*</span></label>
                            <input type="tel" id="phone" name="phone" class="form-control" 
                                   value="<%= student.getPhone() %>" maxlength="10" required>
                            <div id="phoneError" class="error-feedback"></div>
                        </div>

                        <!-- Department & Year Row -->
                        <div class="form-row">
                            <div class="form-group">
                                <label for="department" class="form-label">Department <span class="required">*</span></label>
                                <select id="department" name="department" class="form-control" required>
                                    <option value="">-- Select Department --</option>
                                    <option value="Computer Science" <%= "Computer Science".equals(currentDept) ? "selected" : "" %>>Computer Science</option>
                                    <option value="Information Technology" <%= "Information Technology".equals(currentDept) ? "selected" : "" %>>Information Technology</option>
                                    <option value="Electronics & Comm" <%= "Electronics & Comm".equals(currentDept) ? "selected" : "" %>>Electronics & Comm</option>
                                    <option value="Mechanical Engineering" <%= "Mechanical Engineering".equals(currentDept) ? "selected" : "" %>>Mechanical Engineering</option>
                                    <option value="Civil Engineering" <%= "Civil Engineering".equals(currentDept) ? "selected" : "" %>>Civil Engineering</option>
                                    <option value="Electrical Engineering" <%= "Electrical Engineering".equals(currentDept) ? "selected" : "" %>>Electrical Engineering</option>
                                    <option value="Biotechnology" <%= "Biotechnology".equals(currentDept) ? "selected" : "" %>>Biotechnology</option>
                                </select>
                                <div id="departmentError" class="error-feedback"></div>
                            </div>

                            <div class="form-group">
                                <label for="year" class="form-label">Academic Year <span class="required">*</span></label>
                                <select id="year" name="year" class="form-control" required>
                                    <option value="">-- Select Year --</option>
                                    <option value="1" <%= (currentYear == 1) ? "selected" : "" %>>1st Year (Freshman)</option>
                                    <option value="2" <%= (currentYear == 2) ? "selected" : "" %>>2nd Year (Sophomore)</option>
                                    <option value="3" <%= (currentYear == 3) ? "selected" : "" %>>3rd Year (Junior)</option>
                                    <option value="4" <%= (currentYear == 4) ? "selected" : "" %>>4th Year (Senior)</option>
                                </select>
                                <div id="yearError" class="error-feedback"></div>
                            </div>
                        </div>

                        <!-- Form Actions -->
                        <div class="form-actions">
                            <a href="students" class="btn btn-secondary">Cancel</a>
                            <button type="submit" class="btn btn-primary">&#10003; Update Changes</button>
                        </div>

                    </form>
                </div>
            </div>
        </div>

    </main>

    <!-- 3. Footer -->
    <footer class="footer">
        <p>&copy; Student Management System. Built with Java Servlets, JDBC, MySQL, and Vanilla JS.</p>
    </footer>

    <script src="js/script.js"></script>
</body>
</html>
