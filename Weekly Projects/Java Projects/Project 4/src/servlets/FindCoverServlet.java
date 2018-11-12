package servlets;

import utilities.DBUtils;
import com.beaglebuddy.mp3.MP3;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author tsamo
 */
@javax.servlet.annotation.WebServlet(name = "FindCoverServlet", urlPatterns = {"/FindCoverServlet"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 10 * 10)
public class FindCoverServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();

        final String filePath = (String) session.getAttribute("filePath");

        File file = new File(filePath);
        file.deleteOnExit();
        MP3 mp3 = new MP3(file);
        DBUtils db = new DBUtils();

        String artistUnformatted = mp3.getBand();
        String titleUnformatted = mp3.getTitle();
        String formattedTitle = titleUnformatted.replaceAll(" ", "+");
        String formattedArtist = artistUnformatted.replaceAll(" ", "+");
        String urlString = "http://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key=b145e36ec30ce049e195d3f6d949c403&artist=" + formattedArtist + "&track=" + formattedTitle;
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/xml");

        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code: " + connection.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

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

            URL url2 = new URL(coverUrlString);
            BufferedImage img = ImageIO.read(url2);
            File temppicture = File.createTempFile("temp", ".png");
            temppicture.deleteOnExit();

            ImageIO.write(img, "png", temppicture);
            session.setAttribute("newCover", temppicture);

            if (db.isNullOrBlank(mp3.getLyrics())) {
                ServletContext context = getServletContext();
                RequestDispatcher rd = context.getRequestDispatcher("/FindLyricsServlet");
                rd.forward(request, response);
            } else {
                RequestDispatcher rd = request.getRequestDispatcher("/insertToDB.jsp");
                rd.forward(request, response);
            }


        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
