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
 * Servlet for processing updates to existing student records.
 * URL Pattern: /updateStudent
 */
@WebServlet("/updateStudent")
public class UpdateStudentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private StudentDAO studentDAO;

    @Override
    public void init() {
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.sendRedirect("students");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");

        // 1. Read updated values from HTML form
        String idStr = request.getParameter("studentId");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String department = request.getParameter("department");
        String yearStr = request.getParameter("year");

        // 2. Validate inputs
        if (idStr == null || name == null || email == null || phone == null || department == null || yearStr == null) {
            response.sendRedirect("students?error=missing_data");
            return;
        }

        try {
            int studentId = Integer.parseInt(idStr.trim());
            int year = Integer.parseInt(yearStr.trim());

            // 3. Create updated Student model object
            Student student = new Student(studentId, name.trim(), email.trim(), phone.trim(), department.trim(), year);

            // 4. Update in MySQL via DAO
            boolean isUpdated = studentDAO.updateStudent(student);

            // 5. Redirect with appropriate status message
            if (isUpdated) {
                response.sendRedirect("students?message=updated");
            } else {
                response.sendRedirect("editStudent?id=" + studentId + "&error=update_failed");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("students?error=invalid_data");
        }
    }
}
