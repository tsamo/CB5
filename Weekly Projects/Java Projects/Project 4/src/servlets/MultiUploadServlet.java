package servlets;

import org.apache.commons.io.FilenameUtils;
import utilities.DBUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author tsamo
 */
@javax.servlet.annotation.WebServlet(name = "MultiUploadServlet", urlPatterns = {"/MultiUploadServlet"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 10 * 10)
public class MultiUploadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String folderpath = request.getParameter("folderPath");
        String filename;
        File folder = new File(folderpath);
        File[] listOfFiles = folder.listFiles();
        ArrayList<File> mp3Files = new ArrayList<>();
        String extension;
        DBUtils db=new DBUtils();
        for (int i = 0; i < listOfFiles.length; i++) {
            extension = FilenameUtils.getExtension(listOfFiles[i].getName());
            if (listOfFiles[i].isFile() && extension.equals("mp3")) {
                mp3Files.add(listOfFiles[i]);
            }
        }
        for (File f:mp3Files) {
            filename=f.getName();
            db.insertIntoDB(folderpath,filename);
        }

        RequestDispatcher rd = request.getRequestDispatcher("/successfulInsertion.jsp");
        rd.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
