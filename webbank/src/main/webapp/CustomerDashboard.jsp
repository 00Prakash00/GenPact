<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Customer Dashboard</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f0f0f0;
            color: #333;
        }
        h1 {
            color: #4CAF50;
            text-align: center;
        }
        .dashboard {
            max-width: 600px;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .dashboard p {
            margin-bottom: 10px;
        }
        form {
            margin-bottom: 20px;
        }
        input[type="submit"] {
            width: 100%;
            padding: 10px;
            margin-bottom: 10px;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
            background-color: #4CAF50;
            color: white;
            transition: background-color 0.3s ease;
        }
        input[type="submit"]:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
    <div class="dashboard">
        <h1>Customer Dashboard</h1>
        <p>Welcome, ${sessionScope.customerName}</p>
        <p>Your current balance is: ${sessionScope.balance}</p>
        
        <form action="deposit.jsp">
            <input type="submit" value="Deposit">
        </form>

        <form action="withdraw.jsp">
            <input type="submit" value="Withdraw">
        </form>

        <form action="checkBalance.jsp">
            <input type="submit" value="Check Balance">
        </form>

        <form action="viewTransactions.jsp">
            <input type="submit" value="View Transactions">
        </form>

        <form action="changePassword.jsp">
            <input type="submit" value="Change Password">
        </form>

        <form action="CustomerDashboardServlet" method="post">
            <input type="hidden" name="action" value="logout">
            <input type="submit" value="Logout">
        </form>
    </div>
</body>
</html>
