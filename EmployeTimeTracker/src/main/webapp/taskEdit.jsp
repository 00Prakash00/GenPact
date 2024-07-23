<%@ page import="java.sql.Connection, java.sql.DriverManager, java.sql.PreparedStatement, java.sql.ResultSet" %>
<!DOCTYPE html>
<html>
<head>
    <title>Task Management</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        .container {
            width: 80%;
            margin: 0 auto;
        }
        h2 {
            text-align: center;
            margin-top: 50px;
        }
        .task-form {
            margin: 20px 0;
        }
        .task-form input, .task-form button {
            margin: 5px 0;
            padding: 10px;
            width: 100%;
            max-width: 300px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        table, th, td {
            border: 1px solid black;
        }
        th, td {
            padding: 10px;
            text-align: left;
        }
        .actions {
            display: flex;
            justify-content: center;
        }
        .actions form {
            margin: 0 5px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Task Management</h2>
        <form class="task-form" action="AdminTaskServlet" method="post">
            <input type="hidden" name="action" value="add">
            <input type="text" name="taskName" placeholder="Task Name" required>
            <button type="submit">Add Task</button>
        </form>
        <table>
            <tr>
                <th>ID</th>
                <th>Task Name</th>
                <th>Actions</th>
            </tr>
            <%
                Connection con = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/EmployeeTimeTracker", "root", "prakash@123");
                    ps = con.prepareStatement("SELECT * FROM tasksManagement");
                    rs = ps.executeQuery();
                    if (!rs.isBeforeFirst()) { // If no data is returned
                        out.println("<tr><td colspan='3'>No tasks found.</td></tr>");
                    } else {
                        while(rs.next()) {
            %>
            <tr>
                <td><%= rs.getInt("id") %></td>
                <td><%= rs.getString("taskName") %></td>
                <td class="actions">
                    <form action="AdminTaskServlet" method="post">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="taskId" value="<%= rs.getInt("id") %>">
                        <button type="submit">Delete</button>
                    </form>
                </td>
            </tr>
            <%
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                    out.println("<tr><td colspan='3'>Error: " + e.getMessage() + "</td></tr>");
                } finally {
                    try {
                        if(rs != null) rs.close();
                        if(ps != null) ps.close();
                        if(con != null) con.close();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            %>
        </table>
    </div>
</body>
</html>
