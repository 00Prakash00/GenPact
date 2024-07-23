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
import javax.servlet.http.HttpSession;

@WebServlet("/TransactionServlet")
public class DepositServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String accountNo = (String) session.getAttribute("accountNo");
        double amount = Double.parseDouble(request.getParameter("amount"));

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankdb", "root", "prakash@123");

            // Update balance
            String updateBalanceSql = "UPDATE customers SET balance = balance + ? WHERE account_no = ?";
            pstmt = conn.prepareStatement(updateBalanceSql);
            pstmt.setDouble(1, amount);
            pstmt.setString(2, accountNo);
            pstmt.executeUpdate();
            pstmt.close();

            // Insert transaction
            String insertTransactionSql = "INSERT INTO transactions (account_no, transaction_type, current_balance) VALUES (?, 'deposit', (SELECT balance FROM customers WHERE account_no = ?))";
            pstmt = conn.prepareStatement(insertTransactionSql);
            pstmt.setString(1, accountNo);
            pstmt.setString(2, accountNo);
            pstmt.executeUpdate();

            response.sendRedirect("CustomerDashboard.jsp");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET method is not supported.");
    }
}
