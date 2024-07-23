package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;

@WebServlet("/chartData2")
public class viewReportAdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        String userIdParam = request.getParameter("userid");

        if (userIdParam == null || userIdParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing userid parameter");
            return;
        }

        int userid;
        try {
            userid = Integer.parseInt(userIdParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid userid parameter");
            return;
        }

        try {
            String data = "";
            if ("pie".equals(type)) {
                data = getPieChartData(userid);
            } else if ("weekly".equals(type)) {
                data = getWeeklyChartData(userid);
            } else if ("monthly".equals(type)) {
                data = getMonthlyChartData(userid);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid type parameter");
                return;
            }

            response.setContentType("application/json");
            response.getWriter().write(data);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    private String getPieChartData(int userid) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT taskName, SUM(TIMESTAMPDIFF(MINUTE, startTime, endTime)) / 60 AS totalTime " +
                 "FROM taskRecords WHERE userid = ? GROUP BY taskName")) {
            ps.setInt(1, userid);
            ResultSet rs = ps.executeQuery();

            JSONArray jsonArray = new JSONArray();
            while (rs.next()) {
                JSONArray row = new JSONArray();
                row.put(rs.getString("taskName"));
                row.put(rs.getDouble("totalTime"));
                jsonArray.put(row);
            }

            return jsonArray.toString();
        }
    }

    private String getWeeklyChartData(int userid) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT WEEK(startTime) AS week, SUM(TIMESTAMPDIFF(MINUTE, startTime, endTime)) / 60 AS totalTime " +
                 "FROM taskRecords WHERE userid = ? GROUP BY week")) {
            ps.setInt(1, userid);
            ResultSet rs = ps.executeQuery();

            JSONArray jsonArray = new JSONArray();
            while (rs.next()) {
                JSONArray row = new JSONArray();
                row.put("Week " + rs.getInt("week"));
                row.put(rs.getDouble("totalTime"));
                jsonArray.put(row);
            }

            return jsonArray.toString();
        }
    }

    private String getMonthlyChartData(int userid) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT MONTHNAME(startTime) AS month, SUM(TIMESTAMPDIFF(MINUTE, startTime, endTime)) / 60 AS totalTime " +
                 "FROM taskRecords WHERE userid = ? GROUP BY month")) {
            ps.setInt(1, userid);
            ResultSet rs = ps.executeQuery();

            JSONArray jsonArray = new JSONArray();
            while (rs.next()) {
                JSONArray row = new JSONArray();
                row.put(rs.getString("month"));
                row.put(rs.getDouble("totalTime"));
                jsonArray.put(row);
            }

            return jsonArray.toString();
        }
    }

    private Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/EmployeeTimeTracker";
        String user = "root";
        String password = "prakash@123";
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, password);
    }
}
