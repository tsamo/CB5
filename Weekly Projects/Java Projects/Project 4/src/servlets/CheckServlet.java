package servlets;

import utilities.DBUtils;
import com.beaglebuddy.id3.enums.PictureType;
import com.beaglebuddy.mp3.MP3;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import java.io.*;

/**
 * @author tsamo
 */
@javax.servlet.annotation.WebServlet(name = "CheckServlet", urlPatterns = {"/CheckServlet"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 10 * 10)
public class CheckServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            final Part filepart = request.getPart("fileUploaded");
            final String filename = filepart.getSubmittedFileName();

            String desktop = System.getProperty("user.home") + "\\Desktop\\";

            String filePath = desktop + filename;
            filepart.write(filePath);
            File file = new File(filePath);
            file.deleteOnExit();

            String extension = FilenameUtils.getExtension(filename);
            System.out.println(extension);
            if (!extension.equals("mp3")) {
                response.sendRedirect("/wrongFileType.jsp");
            } else {
                MP3 mp3 = new MP3(file);
                DBUtils db = new DBUtils();
                HttpSession session = request.getSession();
                if (!db.isNullOrBlank(mp3.getLyrics()) && mp3.getPicture(PictureType.FRONT_COVER) != null) {
                    session.setAttribute("filePath", filePath);
                    session.setAttribute("filename", filename);
                    RequestDispatcher rd = request.getRequestDispatcher("/insertToDB.jsp");
                    rd.forward(request, response);
                } else if (!db.isNullOrBlank(mp3.getLyrics())) {
                    session.setAttribute("filePath", filePath);
                    session.setAttribute("filename", filename);
                    RequestDispatcher rd = request.getRequestDispatcher("/missingCover.jsp");
                    rd.forward(request, response);
                } else if (mp3.getPicture(PictureType.FRONT_COVER) != null) {
                    session.setAttribute("filePath", filePath);
                    session.setAttribute("filename", filename);
                    RequestDispatcher rd = request.getRequestDispatcher("/missingLyrics.jsp");
                    rd.forward(request, response);
                }
                else{
                    session.setAttribute("filePath", filePath);
                    session.setAttribute("filename", filename);
                    RequestDispatcher rd = request.getRequestDispatcher("/missignCoverAndLyrics.jsp");
                    rd.forward(request, response);
                }
            }


        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

