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

@WebServlet("/modifyCustomerServlet")
public class ModifyCustomerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bankdb";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "prakash@123"; // Replace with your DB password

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accountNumber = request.getParameter("accountNumber");
        String fullname = request.getParameter("fullname");
        String mobile = request.getParameter("mobile");
        String email = request.getParameter("email");
        String address = request.getParameter("address");

        if (accountNumber != null && !accountNumber.isEmpty()) {
            Connection connection = null;
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

                String sql = "UPDATE customers SET full_name = ?, mobile_no= ?, email = ?, address = ? WHERE account_no = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, fullname);
                statement.setString(2, mobile);
                statement.setString(3, email);
                statement.setString(4, address);
                statement.setString(5, accountNumber);

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    response.sendRedirect("modifySuccess.jsp");
                } else {
                    response.sendRedirect("modifyFailure.jsp");
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database access error.");
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Account number is required.");
        }
    }
}
