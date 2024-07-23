package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;

@WebServlet("/AdminTaskServlet")
public class AdminTaskServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/EmployeeTimeTracker", "root", "prakash@123");

            if ("add".equals(action)) {
                String taskName = request.getParameter("taskName");

                // Prepare the SQL statement to insert a new task
                PreparedStatement ps = con.prepareStatement("INSERT INTO tasksManagement (taskName) VALUES (?)");

                ps.setString(1, taskName);
                ps.executeUpdate();
                HttpSession session = request.getSession();
                session.setAttribute("taskName", taskName);

            } else if ("delete".equals(action)) {
                int taskId = Integer.parseInt(request.getParameter("taskId"));

                // Prepare the SQL statement to delete a task
                PreparedStatement ps = con.prepareStatement("DELETE FROM tasksManagement WHERE id = ?");
                ps.setInt(1, taskId);
                ps.executeUpdate();
                HttpSession session = request.getSession();
                session.setAttribute("taskid", taskId);
            }

            // Close the connection
            con.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

        // Redirect back to the task management page
        response.sendRedirect("taskEdit.jsp");
    }
}
