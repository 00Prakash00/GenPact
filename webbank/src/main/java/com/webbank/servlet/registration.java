package com.webbank.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/storeCustomer")
public class registration extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bankdb";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "prakash@123";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.getWriter().println("<html><body>");
        response.getWriter().println("<h3>Please use the registration form to submit your details.</h3>");
        response.getWriter().println("<a href='registration_form.jsp'>Go to Registration Form</a>");
        response.getWriter().println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fullName = request.getParameter("fullName");
        String address = request.getParameter("address");
        String mobileNo = request.getParameter("mobileNo");
        String email = request.getParameter("email");
        String accountType = request.getParameter("accountType");
        String initialBalanceStr = request.getParameter("initialBalance");
        String dob = request.getParameter("dob");
        String idProof = request.getParameter("idProof");

        // Debug: Print received parameters
        System.out.println("Received parameters:");
        System.out.println("Full Name: " + fullName);
        System.out.println("Address: " + address);
        System.out.println("Mobile No: " + mobileNo);
        System.out.println("Email: " + email);
        System.out.println("Account Type: " + accountType);
        System.out.println("Initial Balance: " + initialBalanceStr);
        System.out.println("Date of Birth: " + dob);
        System.out.println("ID Proof: " + idProof);

        double initialBalance = 0.0;
        if (initialBalanceStr != null && !initialBalanceStr.isEmpty()) {
            try {
                initialBalance = Double.parseDouble(initialBalanceStr);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid initial balance value.");
                return;
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Initial balance is required.");
            return;
        }

        String accountNo = UUID.randomUUID().toString().substring(0, 8); // Generate a unique 8-character account number
        String tempPassword = UUID.randomUUID().toString().substring(0, 8); // Generate a temporary 8-character password

        Connection connection = null;
        try {
            // Establish database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            String sql = "INSERT INTO customers (account_no, full_name, address, mobile_no, email, account_type, balance, date_of_birth, id_proof, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, accountNo);
            statement.setString(2, fullName);
            statement.setString(3, address);
            statement.setString(4, mobileNo);
            statement.setString(5, email);
            statement.setString(6, accountType);
            statement.setDouble(7, initialBalance);
            statement.setString(8, dob);
            statement.setString(9, idProof);
            statement.setString(10, tempPassword);
            statement.executeUpdate();

            response.setContentType("text/html");
            response.getWriter().println("<html><body>");
            response.getWriter().println("<h3>Registration successful!</h3>");
            response.getWriter().println("<p>Your account number is: " + accountNo + "</p>");
            response.getWriter().println("<p>Your temporary password is: " + tempPassword + "</p>");
            response.getWriter().println("<a href='customer_login.jsp'>Login Here</a>");
            response.getWriter().println("</body></html>");
        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException("Database access error", e);
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
