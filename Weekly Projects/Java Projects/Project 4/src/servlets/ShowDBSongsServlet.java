package servlets;

import utilities.DBUtils;
import utilities.Song;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

/**
 * @author tsamo
 */
@javax.servlet.annotation.WebServlet(name = "ShowDBSongsServlet", urlPatterns = {"/ShowDBSongsServlet"})
public class ShowDBSongsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        DBUtils db = new DBUtils();
        Connection conncection = db.getConnection();
        String sql = "SELECT COUNT(*) OVER (), m.* FROM mp3files m;";
        PreparedStatement pstm = null;
        ArrayList<Song> songsInDB = new ArrayList<>();
        String desktop = System.getProperty("user.home") + "\\Desktop\\";
        int count = 0;
        File fileTemp;
        FileOutputStream out;
        Song songTemp;

        try {
            pstm = conncection.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            rs.beforeFirst();
            while (rs.next()) {
                count++;
                int id=rs.getInt(2);
                String title = rs.getString(7);
                String album = rs.getString(5);
                String artist = rs.getString(6);
                String genre = rs.getString(10);
                String lyrics = rs.getString(11);
                int year = rs.getInt(12);
                Blob pictureblob = rs.getBlob(9);

                byte[] array = pictureblob.getBytes(1, (int) pictureblob.length());
                String extension = ".png";
                fileTemp = new File(desktop + count + extension);
                fileTemp.deleteOnExit();
                out = new FileOutputStream(fileTemp);
                out.write(array);

                songTemp = new Song();
                songTemp.setTitle(title);
                songTemp.setAlbum(album);
                songTemp.setArtist(artist);
                songTemp.setGenre(genre);
                songTemp.setLyrics(lyrics);
                songTemp.setYear(year);
                songTemp.setCover(fileTemp);
                songTemp.setId(id);
                songsInDB.add(songTemp);
            }
            HttpSession session = request.getSession();
            session.setAttribute("arraylistOfSongsInDB", songsInDB);
            String selection=request.getParameter("selection");
            if(selection.equals("cards")){
                RequestDispatcher rd = request.getRequestDispatcher("/showSongsCardsInDB.jsp");
                rd.forward(request, response);
            }
            else if(selection.equals("table")){
                RequestDispatcher rd = request.getRequestDispatcher("/showSongsTableInDB.jsp");
                rd.forward(request, response);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
