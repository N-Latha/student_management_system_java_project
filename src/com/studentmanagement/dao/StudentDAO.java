package com.studentmanagement.dao;

import com.studentmanagement.model.Student;
import com.studentmanagement.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) class for Student entity.
 * Handles all CRUD (Create, Read, Update, Delete) database interactions using JDBC PreparedStatement.
 */
public class StudentDAO {

    // SQL Query Constants
    private static final String INSERT_STUDENT_SQL = 
        "INSERT INTO students (student_name, email, phone, department, year) VALUES (?, ?, ?, ?, ?)";
    
    private static final String SELECT_ALL_STUDENTS_SQL = 
        "SELECT student_id, student_name, email, phone, department, year FROM students ORDER BY student_id DESC";
    
    private static final String SELECT_STUDENT_BY_ID_SQL = 
        "SELECT student_id, student_name, email, phone, department, year FROM students WHERE student_id = ?";
    
    private static final String SEARCH_STUDENTS_SQL = 
        "SELECT student_id, student_name, email, phone, department, year FROM students WHERE student_id = ? OR student_name LIKE ? ORDER BY student_id DESC";
    
    private static final String UPDATE_STUDENT_SQL = 
        "UPDATE students SET student_name = ?, email = ?, phone = ?, department = ?, year = ? WHERE student_id = ?";
    
    private static final String DELETE_STUDENT_SQL = 
        "DELETE FROM students WHERE student_id = ?";
    
    private static final String COUNT_STUDENTS_SQL = 
        "SELECT COUNT(*) FROM students";

    private static final String RECENT_STUDENTS_SQL = 
        "SELECT student_id, student_name, email, phone, department, year FROM students ORDER BY student_id DESC LIMIT ?";

    /**
     * 1. CREATE: Inserts a new student record into MySQL.
     * @param student The student object to insert
     * @return true if insertion succeeded, false otherwise
     */
    public boolean addStudent(Student student) {
        boolean rowInserted = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(INSERT_STUDENT_SQL);
            
            // Set parameter values using PreparedStatement placeholders (1-indexed)
            preparedStatement.setString(1, student.getStudentName());
            preparedStatement.setString(2, student.getEmail());
            preparedStatement.setString(3, student.getPhone());
            preparedStatement.setString(4, student.getDepartment());
            preparedStatement.setInt(5, student.getYear());

            // executeUpdate() is used for INSERT, UPDATE, DELETE queries
            int rowsAffected = preparedStatement.executeUpdate();
            rowInserted = (rowsAffected > 0);

        } catch (SQLException e) {
            System.err.println("[StudentDAO] Error adding student: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeResources(connection, preparedStatement, null);
        }

        return rowInserted;
    }

    /**
     * 2. READ ALL: Retrieves all students from MySQL.
     * @return List of all students
     */
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<Student>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(SELECT_ALL_STUDENTS_SQL);
            
            // executeQuery() is used for SELECT queries and returns a ResultSet
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("student_id");
                String name = resultSet.getString("student_name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String department = resultSet.getString("department");
                int year = resultSet.getInt("year");

                Student student = new Student(id, name, email, phone, department, year);
                students.add(student);
            }

        } catch (SQLException e) {
            System.err.println("[StudentDAO] Error fetching all students: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeResources(connection, preparedStatement, resultSet);
        }

        return students;
    }

    /**
     * 3. READ BY ID: Retrieves a single student record by primary key (student_id).
     * @param studentId ID of the student
     * @return Student object if found, null otherwise
     */
    public Student getStudentById(int studentId) {
        Student student = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(SELECT_STUDENT_BY_ID_SQL);
            preparedStatement.setInt(1, studentId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("student_name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String department = resultSet.getString("department");
                int year = resultSet.getInt("year");

                student = new Student(studentId, name, email, phone, department, year);
            }

        } catch (SQLException e) {
            System.err.println("[StudentDAO] Error getting student by ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeResources(connection, preparedStatement, resultSet);
        }

        return student;
    }

    /**
     * 4. SEARCH: Searches students by either student_id OR student_name (case-insensitive substring match).
     * @param query Search query text (can be a number or a name)
     * @return List of matching students
     */
    public List<Student> searchStudents(String query) {
        List<Student> students = new ArrayList<Student>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        if (query == null || query.trim().isEmpty()) {
            return getAllStudents();
        }

        query = query.trim();
        int potentialId = -1;
        try {
            potentialId = Integer.parseInt(query);
        } catch (NumberFormatException ignored) {
            // query is not a pure integer, which is fine
        }

        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(SEARCH_STUDENTS_SQL);
            
            // Set student_id search parameter
            preparedStatement.setInt(1, potentialId);
            // Set student_name LIKE search parameter
            preparedStatement.setString(2, "%" + query + "%");

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("student_id");
                String name = resultSet.getString("student_name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String department = resultSet.getString("department");
                int year = resultSet.getInt("year");

                students.add(new Student(id, name, email, phone, department, year));
            }

        } catch (SQLException e) {
            System.err.println("[StudentDAO] Error searching students: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeResources(connection, preparedStatement, resultSet);
        }

        return students;
    }

    /**
     * 5. UPDATE: Updates an existing student record in MySQL.
     * @param student The student object containing updated details
     * @return true if updated successfully, false otherwise
     */
    public boolean updateStudent(Student student) {
        boolean rowUpdated = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(UPDATE_STUDENT_SQL);
            
            preparedStatement.setString(1, student.getStudentName());
            preparedStatement.setString(2, student.getEmail());
            preparedStatement.setString(3, student.getPhone());
            preparedStatement.setString(4, student.getDepartment());
            preparedStatement.setInt(5, student.getYear());
            preparedStatement.setInt(6, student.getStudentId());

            int rowsAffected = preparedStatement.executeUpdate();
            rowUpdated = (rowsAffected > 0);

        } catch (SQLException e) {
            System.err.println("[StudentDAO] Error updating student: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeResources(connection, preparedStatement, null);
        }

        return rowUpdated;
    }

    /**
     * 6. DELETE: Deletes a student record by student_id.
     * @param studentId ID of the student to delete
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteStudent(int studentId) {
        boolean rowDeleted = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(DELETE_STUDENT_SQL);
            preparedStatement.setInt(1, studentId);

            int rowsAffected = preparedStatement.executeUpdate();
            rowDeleted = (rowsAffected > 0);

        } catch (SQLException e) {
            System.err.println("[StudentDAO] Error deleting student: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeResources(connection, preparedStatement, null);
        }

        return rowDeleted;
    }

    /**
     * Helper: Gets total count of students for Dashboard statistics.
     * @return total student count
     */
    public int getTotalStudentCount() {
        int count = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(COUNT_STUDENTS_SQL);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO] Error getting student count: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeResources(connection, preparedStatement, resultSet);
        }

        return count;
    }

    /**
     * Helper: Gets recent students for Dashboard summary.
     * @param limit number of recent records
     * @return List of recent students
     */
    public List<Student> getRecentStudents(int limit) {
        List<Student> students = new ArrayList<Student>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(RECENT_STUDENTS_SQL);
            preparedStatement.setInt(1, limit);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("student_id");
                String name = resultSet.getString("student_name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String department = resultSet.getString("department");
                int year = resultSet.getInt("year");

                students.add(new Student(id, name, email, phone, department, year));
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO] Error getting recent students: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeResources(connection, preparedStatement, resultSet);
        }

        return students;
    }
}
