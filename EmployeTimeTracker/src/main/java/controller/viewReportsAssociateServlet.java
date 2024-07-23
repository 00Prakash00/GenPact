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
import javax.servlet.http.HttpSession;

import org.json.JSONArray;


@WebServlet("/chartData")
public class viewReportsAssociateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userid") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String type = request.getParameter("type");
        int userid = (int) session.getAttribute("userid");

        try {
            String data = "";
            if ("pie".equals(type)) {
                data = getPieChartData(userid);
            } else if ("weekly".equals(type)) {
                data = getWeeklyChartData(userid);
            } else if ("monthly".equals(type)) {
                data = getMonthlyChartData(userid);
            }

            response.setContentType("application/json");
            response.getWriter().write(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getPieChartData(int userid) throws Exception {
        Connection conn = getConnection();
        String query = "SELECT taskName, SUM(TIMESTAMPDIFF(MINUTE, startTime, endTime)) / 60 AS totalTime FROM taskrecords WHERE userid = ? GROUP BY taskName";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, userid);
        ResultSet rs = ps.executeQuery();

        JSONArray jsonArray = new JSONArray();
        while (rs.next()) {
            JSONArray row = new JSONArray();
            row.put(rs.getString("taskName"));
            row.put(rs.getDouble("totalTime"));
            jsonArray.put(row);
        }
        rs.close();
        ps.close();
        conn.close();

        return jsonArray.toString();
    }

    private String getWeeklyChartData(int userid) throws Exception {
        Connection conn = getConnection();
        String query = "SELECT WEEK(startTime) AS week, SUM(TIMESTAMPDIFF(MINUTE, startTime, endTime)) / 60 AS totalTime FROM taskrecords WHERE userid = ? GROUP BY week";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, userid);
        ResultSet rs = ps.executeQuery();

        JSONArray jsonArray = new JSONArray();
        while (rs.next()) {
            JSONArray row = new JSONArray();
            row.put("Week " + rs.getInt("week"));
            row.put(rs.getDouble("totalTime"));
            jsonArray.put(row);
        }
        rs.close();
        ps.close();
        conn.close();

        return jsonArray.toString();
    }

    private String getMonthlyChartData(int userid) throws Exception {
        Connection conn = getConnection();
        String query = "SELECT MONTHNAME(startTime) AS month, SUM(TIMESTAMPDIFF(MINUTE, startTime, endTime)) / 60 AS totalTime FROM taskrecords WHERE userid = ? GROUP BY month";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, userid);
        ResultSet rs = ps.executeQuery();
        JSONArray jsonArray = new JSONArray();
        while (rs.next()) {
            JSONArray row = new JSONArray();
            row.put(rs.getString("month"));
            row.put(rs.getDouble("totalTime"));
            jsonArray.put(row);
        }
        rs.close();
        ps.close();
        conn.close();

        return jsonArray.toString();
    }

    private Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/employeetimetracker";
        String user = "root";
        String password = "prakash@123";
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, password);
    }
}
