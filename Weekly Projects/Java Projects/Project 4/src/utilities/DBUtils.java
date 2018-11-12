package utilities;

import com.beaglebuddy.id3.enums.PictureType;
import com.beaglebuddy.id3.pojo.AttachedPicture;
import com.beaglebuddy.mp3.MP3;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author tsamo
 */
public class DBUtils {

    public static Connection getConnection() {
        Connection conn = null;

        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/mp3library");
            conn = ds.getConnection();
        } catch (NamingException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

    public static boolean isNullOrBlank(String s) {
        return (s == null || s.trim().equals(""));
    }


    public static void insertIntoDB(String folderpath, String filename) {
        try {
            File file = new File(folderpath + "\\" + filename);
            MP3 mp3 = new MP3(folderpath + "\\" + filename);
            DBUtils db = new DBUtils();

            if (db.isNullOrBlank(mp3.getLyrics()) || mp3.getPicture(PictureType.FRONT_COVER) == null) {
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
                mp3.setLyrics(lyrics);

                String urlString2 = "http://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key=b145e36ec30ce049e195d3f6d949c403&artist=" + formattedArtist + "&track=" + formattedTitle;
                URL url2 = new URL(urlString2);

                HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
                connection2.setRequestMethod("GET");
                connection2.setRequestProperty("Accept", "application/xml");

                if (connection2.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code: " + connection2.getResponseCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(connection2.getInputStream()));

                String xml = "";
                String line;
                while ((line = br.readLine()) != null) {
                    xml = xml.concat(" " + line);
                }

                xml = xml.substring(1);

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder;
                InputSource is;

                try {
                    builder = factory.newDocumentBuilder();
                    is = new InputSource(new StringReader(xml));
                    Document doc = builder.parse(is);
                    doc.getDocumentElement().normalize();

                    NodeList nList = doc.getElementsByTagName("album");
                    Node nNode = nList.item(0);
                    Element element = (Element) nNode;
                    String coverUrlString = element.getElementsByTagName("image").item(3).getTextContent();

                    URL url3 = new URL(coverUrlString);
                    BufferedImage img = ImageIO.read(url3);
                    File temppicture = File.createTempFile("temp", ".png");
                    temppicture.deleteOnExit();

                    ImageIO.write(img, "png", temppicture);
                    mp3.setPicture(PictureType.FRONT_COVER, temppicture);

                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
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
            } catch (SerialException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}