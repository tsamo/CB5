package servlets;

import org.apache.commons.io.FileUtils;
import utilities.DBUtils;
import com.beaglebuddy.id3.enums.PictureType;
import com.beaglebuddy.id3.pojo.AttachedPicture;
import com.beaglebuddy.mp3.MP3;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import javax.sql.rowset.serial.SerialException;
import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author tsamo
 */
@javax.servlet.annotation.WebServlet(name = "UploadServlet", urlPatterns = {"/UploadServlet"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 10 * 10)
public class UploadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session=request.getSession();
        final String filePath = (String) session.getAttribute("filePath");
        final String filename = (String) session.getAttribute("filename");

        File file = new File(filePath);
        file.deleteOnExit();
        MP3 mp3 = new MP3(file);
        DBUtils db = new DBUtils();

        if(mp3.getPicture(PictureType.FRONT_COVER) == null&&db.isNullOrBlank(mp3.getLyrics())){
            File temppicture= (File) session.getAttribute("newCover");
            temppicture.deleteOnExit();
            mp3.setPicture(PictureType.FRONT_COVER,temppicture);
            String newLyrics= (String) session.getAttribute("newLyrics");
            mp3.setLyrics(newLyrics);
        }
        else if(mp3.getPicture(PictureType.FRONT_COVER) == null){
            File temppicture= (File) session.getAttribute("newCover");
            temppicture.deleteOnExit();
            mp3.setPicture(PictureType.FRONT_COVER,temppicture);
        }
        else if(db.isNullOrBlank(mp3.getLyrics())){
            String newLyrics= (String) session.getAttribute("newLyrics");
            mp3.setLyrics(newLyrics);
        }

        String album = null, artist = null, title = null, genre = null, lyrics = null;
        int trackNumber = 0, year = 0;
        AttachedPicture picture = null;

        try {
            if (!mp3.getAlbum().isEmpty()) album = mp3.getAlbum();
            if (!mp3.getBand().isEmpty()) artist = mp3.getBand();
            if (!mp3.getTitle().isEmpty()) title = mp3.getTitle();
            if (mp3.getTrack() != 0) trackNumber = mp3.getTrack();
            if (mp3.getPicture(PictureType.FRONT_COVER) != null) picture = mp3.getPicture(PictureType.FRONT_COVER);
            if (!mp3.getMusicType().isEmpty()) genre = mp3.getMusicType();

            if (!mp3.getLyrics().isEmpty()) lyrics = mp3.getLyrics();
            if (mp3.getYear() != 0) year = mp3.getYear();

            Connection conncection = db.getConnection();
            String sql = "INSERT INTO mp3files VALUES (NULL,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement pstm = null;
            Blob pictureBlob = new javax.sql.rowset.serial.SerialBlob(picture.getImage());

            byte[] fileContent = null;
            fileContent = FileUtils.readFileToByteArray(file);
            Blob songBlob = new javax.sql.rowset.serial.SerialBlob(fileContent);

            pstm = conncection.prepareStatement(sql);
            pstm.setString(1, filename);
            pstm.setBlob(2, songBlob);
            pstm.setString(3, album);
            pstm.setString(4, artist);
            pstm.setString(5, title);
            pstm.setInt(6, trackNumber);
            pstm.setBlob(7, pictureBlob);
            pstm.setString(8, genre);
            pstm.setString(9, lyrics);
            pstm.setInt(10, year);

            pstm.executeUpdate();

            pstm.close();
            conncection.close();

            RequestDispatcher rd = request.getRequestDispatcher("/successfulInsertion.jsp");
            rd.forward(request, response);
        } catch (SerialException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
