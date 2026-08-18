package com.studentmanagement.servlet;

import com.studentmanagement.dao.StudentDAO;
import com.studentmanagement.model.Student;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for handling the creation of new student records.
 * URL Pattern: /addStudent
 */
@WebServlet("/addStudent")
public class AddStudentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private StudentDAO studentDAO;

    @Override
    public void init() {
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // If accessed via GET, redirect directly to the add student form page
        response.sendRedirect("add-student.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Ensure request encoding handles special characters
        request.setCharacterEncoding("UTF-8");

        // 1. Read input parameters from HTML form
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String department = request.getParameter("department");
        String yearStr = request.getParameter("year");

        // 2. Server-side basic validation
        if (name == null || name.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            phone == null || phone.trim().isEmpty() ||
            department == null || department.trim().isEmpty() ||
            yearStr == null || yearStr.trim().isEmpty()) {
            
            // Redirect back with validation error parameter
            response.sendRedirect("add-student.html?error=empty_fields");
            return;
        }

        int year = 1;
        try {
            year = Integer.parseInt(yearStr.trim());
        } catch (NumberFormatException e) {
            response.sendRedirect("add-student.html?error=invalid_year");
            return;
        }

        // 3. Create a new Student model instance
        Student newStudent = new Student(name.trim(), email.trim(), phone.trim(), department.trim(), year);

        // 4. Call DAO method to insert into MySQL
        boolean isSuccess = studentDAO.addStudent(newStudent);

        // 5. Send redirect based on insertion outcome
        if (isSuccess) {
            // Redirect to Student List with success flag
            response.sendRedirect("students?message=added");
        } else {
            // Redirect back to form with error flag (e.g. duplicate email)
            response.sendRedirect("add-student.html?error=failed");
        }
    }
}
