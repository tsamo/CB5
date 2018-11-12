package servlets;

import com.beaglebuddy.mp3.MP3;
import org.json.JSONObject;
import utilities.DBUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * @author tsamo
 */
@javax.servlet.annotation.WebServlet(name = "FindLyricsServlet", urlPatterns = {"/FindLyricsServlet"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 10 * 10)
public class FindLyricsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();

        final String filePath = (String) session.getAttribute("filePath");

        File file = new File(filePath);
        MP3 mp3 = new MP3(file);
        file.deleteOnExit();

        String artistUnformatted = mp3.getBand();
        String titleUnformatted = mp3.getTitle();
        String formattedTitle = titleUnformatted.replaceAll(" ", "+");
        String formattedArtist = artistUnformatted.replaceAll(" ", "+");

        String urlString = "https://api.lyrics.ovh/v1/" + formattedArtist + "/" + formattedTitle;
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code: " + connection.getResponseCode());
        }

        Scanner scan = new Scanner(url.openStream());
        String jsonString = new String();
        while (scan.hasNext())
            jsonString += scan.nextLine();
        scan.close();

        JSONObject json = new JSONObject(jsonString);
        String lyrics = (String) json.get("lyrics");
        session.setAttribute("newLyrics", lyrics);

        RequestDispatcher rd = request.getRequestDispatcher("/insertToDB.jsp");
        rd.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
