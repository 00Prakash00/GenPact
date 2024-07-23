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

@WebServlet("/CustomerDashboardServlet")
public class CustomerDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String accountNo = (String) request.getSession().getAttribute("accountNo");

        if (action == null || accountNo == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String DB_URL = "jdbc:mysql://localhost:3306/bankdb";
        String DB_USERNAME = "root";
        String DB_PASSWORD = "prakash@123";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            switch (action) {
                case "deposit":
                    double depositAmount = Double.parseDouble(request.getParameter("amount"));
                    updateBalance(accountNo, depositAmount, conn);
                    break;
                case "withdraw":
                    double withdrawAmount = Double.parseDouble(request.getParameter("amount"));
                    updateBalance(accountNo, -withdrawAmount, conn);
                    break;
                case "checkBalance":
                    double balance = getBalance(accountNo, conn);
                    request.getSession().setAttribute("balance", balance);
                    response.getWriter().println("Current Balance: " + balance);
                    return;
                case "viewTransactions":
                    // Logic to fetch and display transactions
                    break;
                case "changePassword":
                    String newPassword = request.getParameter("newPassword");
                    changePassword(accountNo, newPassword, conn);
                    break;
                case "logout":
                    request.getSession().invalidate();
                    response.sendRedirect("login.jsp");
                    return;
                default:
                    response.sendRedirect("CustomerDashboard.jsp");
                    return;
            }

            double newBalance = getBalance(accountNo, conn);
            request.getSession().setAttribute("balance", newBalance);
            response.sendRedirect("CustomerDashboard.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("An error occurred while processing your request.");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void updateBalance(String accountNo, double amount, Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE customers SET balance = balance + ? WHERE account_no = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, amount);
            pstmt.setString(2, accountNo);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) pstmt.close();
        }
    }

    private double getBalance(String accountNo, Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        double balance = 0;

        try {
            String sql = "SELECT balance FROM customers WHERE account_no = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, accountNo);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                balance = rs.getDouble("balance");
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }

        return balance;
    }

    private void changePassword(String accountNo, String newPassword, Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE customers SET password = ? WHERE account_no = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPassword);
            pstmt.setString(2, accountNo);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) pstmt.close();
        }
    }
}
