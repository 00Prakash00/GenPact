<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.webbank.servlet.Transaction" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>View Transactions</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f0f0;
            margin: 0;
            padding: 20px;
        }

        .container {
            max-width: 800px;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        h1 {
            text-align: center;
            color: #333;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            padding: 12px;
            border: 1px solid #ddd;
            text-align: left;
        }

        th {
            background-color: #4CAF50;
            color: white;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        .back-button {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            text-align: center;
        }

        .back-button:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Transaction History</h1>
        <p>Account Number: <%= session.getAttribute("accountNo") %></p>
        <table>
            <tr>
                <th>Transaction ID</th>
                <th>Date</th>
                <th>Type</th>
                <th>Current Balance</th>
            </tr>
            <%
                ArrayList<Transaction> transactions = (ArrayList<Transaction>) request.getAttribute("transactions");
                if (transactions != null && !transactions.isEmpty()) {
                    for (Transaction transaction : transactions) {
            %>
                        <tr>
                            <td><%= transaction.getTransactionId() %></td>
                            <td><%= transaction.getTransactionDate() %></td>
                            <td><%= transaction.getTransactionType() %></td>
                            <td><%= transaction.getCurrentBalance() %></td>
                        </tr>
            <%
                    }
                } else {
            %>
                    <tr>
                        <td colspan="4">No transactions found.</td>
                    </tr>
            <%
                }
            %>
        </table>
        <a class="back-button" href="CustomerDashboard.jsp">Back to Dashboard</a>
    </div>
</body>
</html>
