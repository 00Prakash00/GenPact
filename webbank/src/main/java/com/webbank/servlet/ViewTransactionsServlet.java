package com.webbank.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ViewTransactionsServlet")
public class ViewTransactionsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String accountNo = (String) session.getAttribute("accountNo");

        if (accountNo == null) {
            response.sendRedirect("customer_login.jsp");
            return;
        }

        System.out.println("Account Number in Session: " + accountNo); // Debugging

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankdb", "root", "prakash@123");

            String sql = "SELECT transaction_id, transaction_date, transaction_type, current_balance FROM transactions WHERE account_no = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, accountNo);
            rs = pstmt.executeQuery();

            ArrayList<Transaction> transactions = new ArrayList<>();
            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(rs.getInt("transaction_id"));
                transaction.setTransactionDate(rs.getTimestamp("transaction_date"));
                transaction.setTransactionType(rs.getString("transaction_type"));
                transaction.setCurrentBalance(rs.getDouble("current_balance"));
                transactions.add(transaction);

                System.out.println("Fetched transaction: " + transaction.getTransactionId()); // Debugging
            }

            request.setAttribute("transactions", transactions);
            request.getRequestDispatcher("viewTransactions.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (Exception e) { e.printStackTrace(); }
            if (pstmt != null) try { pstmt.close(); } catch (Exception e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }
}
