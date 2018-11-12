package servlets;

import utilities.DBUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author tsamo
 */
@WebServlet("/images/*")
public class GetImageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sql = "SELECT picture_blob FROM mp3files WHERE id = ?";
        String imageName = request.getPathInfo().substring(1);
        try {
            DBUtils db = new DBUtils();
            Connection connection = db.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, imageName);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                byte[] content = rs.getBytes("picture_blob");
                response.setContentType(getServletContext().getMimeType(imageName));
                response.setContentLength(content.length);
                response.getOutputStream().write(content);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
