<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .container {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            width: 300px;
            text-align: center;
        }
        h2 {
            color: #333333;
        }
        a {
            display: block;
            color: #007bff;
            text-decoration: none;
            margin: 10px 0;
            font-size: 16px;
            transition: color 0.3s ease;
        }
        a:hover {
            color: #0056b3;
            text-decoration: underline;
        }
        .back-link {
            color: #ff4444;
        }
        .back-link:hover {
            color: #cc0000;
            text-decoration: underline;
        }
    </style>
    <title>Customer Management</title>
</head>
<body>
    <div class="container">
        <h2>Welcome!</h2>
        <a href="register-link.jsp">Register Customer</a><br>
        <a href="viewcustomer.jsp">View Customer</a><br>
        <a href="modifycustomer.jsp">Modify Customer</a><br>
        <a href="deletecustomer.jsp">Delete Customer</a><br>
        <a href="mainlogin.jsp" class="back-link">Logout</a>
    </div>
</body>
</html>
