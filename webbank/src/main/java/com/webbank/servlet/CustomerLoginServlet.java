package com.webbank.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/CustomerLoginServlet")
public class CustomerLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accountNo = request.getParameter("accountNo");
        String password = request.getParameter("password");

        // Debugging: Print received parameters
        System.out.println("Account No: " + accountNo);
        System.out.println("Password: " + password);

        String DB_URL = "jdbc:mysql://localhost:3306/bankdb";
        String DB_USERNAME = "root";
        String DB_PASSWORD = "prakash@123";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            String sql = "SELECT * FROM customers WHERE account_no = ? AND password = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, accountNo);
            pstmt.setString(2, password);

            rs = pstmt.executeQuery();

            if (rs.next()) {
            	
                HttpSession session = request.getSession();
                session.setAttribute("accountNo", accountNo);
                session.setAttribute("customerName", rs.getString("full_name")); // Assuming you have a full_name column

                // Debugging: Print success message
                System.out.println("Login successful for account: " + accountNo);

                response.sendRedirect("CustomerDashboard.jsp");
            } else {
                // Debugging: Print failure message
                System.out.println("Invalid account number or password.");
                response.getWriter().println("Invalid account number or password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
