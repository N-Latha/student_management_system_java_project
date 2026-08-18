package com.studentmanagement.servlet;

import com.studentmanagement.dao.StudentDAO;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for handling student deletion requests.
 * URL Pattern: /deleteStudent
 */
@WebServlet("/deleteStudent")
public class DeleteStudentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private StudentDAO studentDAO;

    @Override
    public void init() {
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Read student ID from URL parameter (e.g. /deleteStudent?id=5)
        String idParam = request.getParameter("id");

        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect("students?error=missing_id");
            return;
        }

        try {
            int studentId = Integer.parseInt(idParam.trim());

            // 2. Perform deletion via DAO
            boolean isDeleted = studentDAO.deleteStudent(studentId);

            // 3. Redirect back to students list with result message
            if (isDeleted) {
                response.sendRedirect("students?message=deleted");
            } else {
                response.sendRedirect("students?error=delete_failed");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("students?error=invalid_id");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}
