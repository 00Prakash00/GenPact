package com.webbank.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DeleteCustomerServlet")
public class DeleteCustomerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection parameters (adjust as per your database setup)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bankdb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "prakash@123";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accountNumber = request.getParameter("accountNo");

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Step 1: Connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Step 2: Prepare SQL statement
            String sql = "DELETE FROM customers WHERE account_no = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, accountNumber);

            // Step 3: Execute update
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Deletion successful
                response.sendRedirect("adminworkspace.jsp?message=deleted");
            } else {
                // Account not found
                response.sendRedirect("deletecustomer.jsp?error=notfound");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new ServletException("Database access error", e);
        } finally {
            // Step 4: Close connections
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
