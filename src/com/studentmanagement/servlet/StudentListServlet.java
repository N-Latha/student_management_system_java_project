package com.studentmanagement.servlet;

import com.studentmanagement.dao.StudentDAO;
import com.studentmanagement.model.Student;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for retrieving and listing all students.
 * URL Pattern: /students
 */
@WebServlet("/students")
public class StudentListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private StudentDAO studentDAO;

    @Override
    public void init() {
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Retrieve all students from the database via DAO
        List<Student> studentList = studentDAO.getAllStudents();

        // 2. Set retrieved list in request scope for JSP
        request.setAttribute("studentList", studentList);

        // 3. Forward request to students.jsp view
        request.getRequestDispatcher("students.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Forward POST requests to doGet for consistent behavior
        doGet(request, response);
    }
}
