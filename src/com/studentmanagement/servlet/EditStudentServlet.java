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
 * Servlet for retrieving a single student's details for editing.
 * URL Pattern: /editStudent
 */
@WebServlet("/editStudent")
public class EditStudentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private StudentDAO studentDAO;

    @Override
    public void init() {
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");

        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect("students?error=missing_id");
            return;
        }

        try {
            int studentId = Integer.parseInt(idParam.trim());
            // 1. Fetch existing student details from MySQL
            Student existingStudent = studentDAO.getStudentById(studentId);

            if (existingStudent != null) {
                // 2. Set student in request scope
                request.setAttribute("student", existingStudent);
                // 3. Forward to edit form JSP
                request.getRequestDispatcher("edit-student.jsp").forward(request, response);
            } else {
                response.sendRedirect("students?error=student_not_found");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("students?error=invalid_id");
        }
    }
}
