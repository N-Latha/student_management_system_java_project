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
 * Servlet for searching students by ID or Name.
 * URL Pattern: /searchStudent
 */
@WebServlet("/searchStudent")
public class SearchStudentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private StudentDAO studentDAO;

    @Override
    public void init() {
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Read the search keyword from query parameter
        String query = request.getParameter("query");

        // 2. Perform search via DAO
        List<Student> searchResults;
        if (query != null && !query.trim().isEmpty()) {
            searchResults = studentDAO.searchStudents(query.trim());
        } else {
            searchResults = studentDAO.getAllStudents();
        }

        // 3. Attach search results and original query string to request
        request.setAttribute("studentList", searchResults);
        request.setAttribute("searchQuery", query != null ? query.trim() : "");
        request.setAttribute("isSearch", true);

        // 4. Forward to students.jsp to display results
        request.getRequestDispatcher("students.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}
