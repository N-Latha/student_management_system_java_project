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
 * Servlet for handling Dashboard home page requests.
 * URL Patterns: "", "/", "/dashboard"
 */
@WebServlet(urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private StudentDAO studentDAO;

    @Override
    public void init() {
        // Instantiate DAO once when servlet is initialized
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Fetch dashboard metrics from database via DAO
        int totalStudents = studentDAO.getTotalStudentCount();
        List<Student> recentStudents = studentDAO.getRecentStudents(5);

        // 2. Attach data to request scope
        request.setAttribute("totalStudents", totalStudents);
        request.setAttribute("recentStudents", recentStudents);

        // 3. Forward request to index.jsp for rendering
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
