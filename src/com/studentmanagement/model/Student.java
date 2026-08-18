package com.studentmanagement.model;

import java.io.Serializable;

/**
 * Model class representing a Student entity.
 * Demonstrates Encapsulation (private fields, public getters and setters).
 */
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    // Entity Attributes
    private int studentId;
    private String studentName;
    private String email;
    private String phone;
    private String department;
    private int year;

    // 1. Default (No-argument) Constructor
    public Student() {
    }

    // 2. Parameterized Constructor WITHOUT studentId (used when inserting a new student)
    public Student(String studentName, String email, String phone, String department, int year) {
        this.studentName = studentName;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.year = year;
    }

    // 3. Parameterized Constructor WITH studentId (used when retrieving or updating an existing student)
    public Student(int studentId, String studentName, String email, String phone, String department, int year) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.year = year;
    }

    // --- Getters and Setters ---

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Student [" +
                "studentId=" + studentId +
                ", studentName='" + studentName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", department='" + department + '\'' +
                ", year=" + year +
                ']';
    }
}
