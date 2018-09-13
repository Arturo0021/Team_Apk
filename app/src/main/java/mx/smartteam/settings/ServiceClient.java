package mx.smartteam.settings;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.http.HttpConnection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import mx.triplei.Asistencia;
//import mx.smartteam.R;
//import mx.smartteam.R.string;
import mx.smartteam.entities.CategoriaCollection;
import mx.smartteam.entities.EnumFormulario;
import mx.smartteam.entities.Opcion;
import mx.smartteam.entities.Pregunta;
import mx.smartteam.entities.SondeoCollection;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.StrictMode;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceClient {

    private static class Conection {

        private InputStream in = null;
        private int respuesta = -1;
        private URL url = null;
        private HttpURLConnection httpConn;

        public Conection(String strUrl, String strParams) throws MalformedURLException, IOException {

            url = new URL(strUrl + strParams);
            URLConnection conn = url.openConnection();
            if (!(conn instanceof HttpURLConnection)) {
                throw new IOException("No es una conexion HTTP");
            }
            httpConn = (HttpURLConnection) conn;
            // La conexion no permite interaccion con el usuario
            httpConn.setAllowUserInteraction(false);
            // Si permite que la conexion siga las redirecciones
            httpConn.setInstanceFollowRedirects(true);
            // Tipo de request
            httpConn.setRequestMethod("GET");
        }

        public void Open() throws IOException, ParserConfigurationException, SAXException {
            if (httpConn != null) {
                httpConn.connect();
                respuesta = httpConn.getResponseCode();
                if (respuesta == HttpURLConnection.HTTP_OK) {
                    in = httpConn.getInputStream();


                }
            }
        }

        public Document GetDocResult() throws ParserConfigurationException, SAXException, IOException {
            Document doc = null;

            if (in != null) {
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                doc = builder.parse(in, null);
            }
            return doc;
        }

        public String GetStrResult() throws IOException {

            String strResult = null;
            if (in != null) {
                StringBuilder sb = new StringBuilder();

                int chr;
                while ((chr = in.read()) != -1) {
                    sb.append((char) chr);
                }

                strResult = sb.toString();
            }

            return strResult;
        }
    }

    // Login
    public static class Login {

//        private Document doc = null;
//        private InputStream in = null;
//        private int respuesta = -1;
//        private URL url = null;
        Conection conection = null;

        public Login(String... params) throws MalformedURLException, IOException, ParserConfigurationException, SAXException {

            String strParams = String.format("?usuario=%s&password=%s",
                    params[0], params[1]);

            conection = new Conection(Ajustes.Url_Login, strParams);
            conection.Open();



//                url = new URL(Ajustes.Url_Login + strParams);
//
//                URLConnection conn = url.openConnection();
//                if (!(conn instanceof HttpURLConnection)) {
//                    throw new IOException("No es una conexion HTTP");
//                }
//
//                HttpURLConnection httpConn = (HttpURLConnection) conn;
//
//                httpConn.setAllowUserInteraction(false);
//
//                httpConn.setInstanceFollowRedirects(true);
//
//                httpConn.setRequestMethod("GET");
//                httpConn.connect();
//                respuesta = httpConn.getResponseCode();
//                if (respuesta == HttpURLConnection.HTTP_OK) {
//                    in = httpConn.getInputStream();
//
//                    DocumentBuilder builder = DocumentBuilderFactory
//                            .newInstance().newDocumentBuilder();
//                    doc = builder.parse(in, null);
//                }



        }

        public Document GetResult() throws ParserConfigurationException, SAXException, IOException {
            if (conection != null) {
                return conection.GetDocResult();
            }
            return null;
        }
    }

    public static class Folios {

        private static String doc = null;
        private static InputStream in = null;
        private static int respuesta = -1;
        private static URL url = null;

        public static class Valida {

            public Valida(String... params) {

                try {
                    String strParams = String.format(
                            "?idProyecto=%s&idFolio=%s&idUsuario=%s",
                            params[0], params[1], params[2]);

                    url = new URL(Ajustes.Url_ValidaTienda + strParams);

                    URLConnection conn = url.openConnection();
                    if (!(conn instanceof HttpURLConnection)) {
                        throw new IOException("No es una conexion HTTP");
                    }

                    HttpURLConnection httpConn = (HttpURLConnection) conn;

                    // La conexion no permite interaccion con el usuario
                    httpConn.setAllowUserInteraction(false);

                    // Si permite que la conexion siga las redirecciones
                    httpConn.setInstanceFollowRedirects(true);

                    // Tipo de request
                    httpConn.setRequestMethod("GET");
                    httpConn.connect();
                    respuesta = httpConn.getResponseCode();
                    if (respuesta == HttpURLConnection.HTTP_OK) {
                        in = httpConn.getInputStream();

                        StringBuffer sb = new StringBuffer();

                        int chr;
                        while ((chr = in.read()) != -1) {
                            sb.append((char) chr);
                        }

                        doc = sb.toString();

                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            public String GetResult() {
                return doc;
            }
        }

        public static class InviaInformacion {

            public InviaInformacion(String... params) {

                try {
                    StringBuilder strParamns = new StringBuilder();
                    strParamns.append(String.format("?idProyecto=%s&",
                            params[0]));
                    strParamns.append(String.format("determinanteGPS=%s&",
                            params[1]));
                    strParamns
                            .append(String.format("idUsuario=%s&", params[2]));
                    strParamns.append(String.format("latitud=%s&", params[3]));
                    strParamns.append(String.format("longitud=%s&", params[4]));
                    strParamns.append(String.format("idStatus=%s&", params[5]));
                    strParamns.append(String.format("num_fotos=%s", params[6]));

                    url = new URL(Ajustes.Url_EnviaTienda
                            + strParamns.toString());

                    URLConnection conn = url.openConnection();
                    if (!(conn instanceof HttpURLConnection)) {
                        throw new IOException("No es una conexion HTTP");
                    }

                    HttpURLConnection httpConn = (HttpURLConnection) conn;

                    // La conexion no permite interaccion con el usuario
                    httpConn.setAllowUserInteraction(false);

                    // Si permite que la conexion siga las redirecciones
                    httpConn.setInstanceFollowRedirects(true);

                    // Tipo de request
                    httpConn.setRequestMethod("GET");
                    httpConn.connect();
                    respuesta = httpConn.getResponseCode();
                    if (respuesta == HttpURLConnection.HTTP_OK) {
                        in = httpConn.getInputStream();

                        StringBuffer sb = new StringBuffer();

                        int chr;
                        while ((chr = in.read()) != -1) {
                            sb.append((char) chr);
                        }

                        doc = sb.toString();

                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            public String GetResult() {
                return doc;
            }
        }

        public static class CerrarDia {

            public CerrarDia(String... params) {

                try {
                    StringBuilder strParamns = new StringBuilder();
                    strParamns.append(String.format("?idUsuario=%s&", params[0]));
                    strParamns.append(String.format("idProyecto=%s&", params[1]));
                    strParamns.append(String.format("idTienda=%s&", params[2]));
                    //strParamns.append("parametro=cerrar%20dia");
                    strParamns.append(String.format("parametro=%s&", params[3]));

                    url = new URL(Ajustes.Url_cerrar_dia
                            + strParamns.toString());

                    URLConnection conn = url.openConnection();
                    if (!(conn instanceof HttpURLConnection)) {
                        throw new IOException("No es una conexion HTTP");
                    }

                    HttpURLConnection httpConn = (HttpURLConnection) conn;

                    // La conexion no permite interaccion con el usuario
                    httpConn.setAllowUserInteraction(false);

                    // Si permite que la conexion siga las redirecciones
                    httpConn.setInstanceFollowRedirects(true);

                    // Tipo de request
                    httpConn.setRequestMethod("GET");
                    httpConn.connect();
                    respuesta = httpConn.getResponseCode();
                    if (respuesta == HttpURLConnection.HTTP_OK) {
                        in = httpConn.getInputStream();

                        StringBuffer sb = new StringBuffer();

                        int chr;
                        while ((chr = in.read()) != -1) {
                            sb.append((char) chr);
                        }

                        doc = sb.toString();

                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            public String GetResult() {
                return doc;
            }
        }
    }
    /*
     public static Document GetXmlByHttp(final String strUrl) {

     Document doc = null;

     InputStream in = null;
     int respuesta = -1;
     URL url = null;

     try {
     StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
     .permitAll().build();
     StrictMode.setThreadPolicy(policy);

     url = new URL(strUrl);

     URLConnection conn = url.openConnection();
     if (!(conn instanceof HttpURLConnection)) {
     throw new IOException("No es una conexion HTTP");
     }

     HttpURLConnection httpConn = (HttpURLConnection) conn;

     // La conexion no permite interaccion con el usuario
     httpConn.setAllowUserInteraction(false);

     // Si permite que la conexion siga las redirecciones
     httpConn.setInstanceFollowRedirects(true);

     // Tipo de request
     httpConn.setRequestMethod("GET");
     httpConn.connect();
     respuesta = httpConn.getResponseCode();
     if (respuesta == HttpURLConnection.HTTP_OK) {
     in = httpConn.getInputStream();

     DocumentBuilder builder = DocumentBuilderFactory.newInstance()
     .newDocumentBuilder();
     doc = builder.parse(in, null);

     }

     } catch (IOException e) {
     // TODO Auto-generated catch block
     e.printStackTrace();
     } catch (ParserConfigurationException e) {
     // TODO Auto-generated catch block
     e.printStackTrace();
     } catch (SAXException e) {
     // TODO Auto-generated catch block
     e.printStackTrace();
     }

     return doc;
     }
     */

    public static String GetStringHttp(final String strUrl) {

        String doc = null;

        InputStream in = null;
        int respuesta = -1;
        URL url = null;

        try {
            //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
            //        .permitAll().build();
            //StrictMode.setThreadPolicy(policy);

            url = new URL(strUrl);

            URLConnection conn = url.openConnection();
            if (!(conn instanceof HttpURLConnection)) {
                throw new IOException("No es una conexion HTTP");
            }

            HttpURLConnection httpConn = (HttpURLConnection) conn;

            // La conexion no permite interaccion con el usuario
            httpConn.setAllowUserInteraction(false);

            // Si permite que la conexion siga las redirecciones
            httpConn.setInstanceFollowRedirects(true);

            // Tipo de request
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            respuesta = httpConn.getResponseCode();
            if (respuesta == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();

                StringBuffer sb = new StringBuffer();

                int chr;
                while ((chr = in.read()) != -1) {
                    sb.append((char) chr);
                }

                doc = sb.toString();

                // DocumentBuilder builder =
                // DocumentBuilderFactory.newInstance()
                // .newDocumentBuilder();

                // doc = builder.parse(in, null);

            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return doc;
    }

    /*
     public static void SendPhoto(String strUrl, String imgData, int idProyecto,
     int idUsuario, int idTienda) throws MalformedURLException,
     IOException {

     File binaryFile = new File(imgData);

     Bitmap bitmap = null;// myView.getBitmap();

     String attachmentName = "bitmap";
     String attachmentFileName = "bitmap.bmp";
     String crlf = "\r\n";
     String twoHyphens = "--";
     String boundary = "uploadedfile";

     HttpURLConnection httpUrlConnection = null;
     URL url = new URL(strUrl);
     httpUrlConnection = (HttpURLConnection) url.openConnection();
     httpUrlConnection.setUseCaches(false);
     httpUrlConnection.setDoOutput(true);

     httpUrlConnection.setRequestMethod("POST");
     // httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
     // httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
     httpUrlConnection
     .setRequestProperty("Content-Type",
     "multipart/form-data;boundary=---------------------------4664151417711");

     DataOutputStream request = new DataOutputStream(
     httpUrlConnection.getOutputStream());

     request.writeBytes("---------------------------4664151417711");
     request.writeBytes("Content-Disposition: form-data; name=\"" + boundary
     + "\";filename=\"" + attachmentFileName + "\"" + crlf);

     request.writeBytes(crlf);
     request.writeBytes("\r\n---------------------------4664151417711");

     request.writeBytes("Content-Disposition: form-data; name=\"id" + crlf);

     request.writeBytes("\r\n" + attachmentName + "|idCuenta=" + idProyecto
     + "|idUsuario=" + idUsuario + "|idTienda=" + 80316 + "\r\n"); // +
     // (EVOLVEMIDlet.myMidlet.envioAsistencia
     // ?
     // ""
     // :
     // new
     // StringBuffer().append("|idCategoriaFoto=").append(EVOLVEMIDlet.myMidlet.categoriaFoto).append("|comentariosFoto=").append(EVOLVEMIDlet.myMidlet.comentariosFoto).append("|opciones_foto=").append(EVOLVEMIDlet.myMidlet.opcionesFoto).toString())
     // +
     // "\r\n";
     request.writeBytes("\r\n---------------------------4664151417711");

     InputStream input = new FileInputStream(binaryFile);

     input = new FileInputStream(binaryFile);

     byte[] buf = new byte[1024];
     int len;
     while ((len = input.read(buf)) > 0) {
     request.write(buf, 0, len);
     }

     input.close();
     request.flush();
     request.close();

     }

    
     */
    public static class Foto {

        static HttpURLConnection httpConn = null;
        static InputStream is = null;
        static OutputStream os = null;
        static boolean inConection = false;
        static String response = null;
        static String CrLf = "\r\n";
        static String se_cuenta, se_usuario, se_tienda, se_categoria,
                se_comentario, se_opcion;
        static byte[] val$_imgData;
        static ArrayList<String> parametros;

        private static ArrayList<String> NombreImagen(String... params) {
            ArrayList<String> result = new ArrayList<String>();

            String _cuenta_id = "" + params[0];
            while (_cuenta_id.length() < 3) {
                _cuenta_id = "0" + _cuenta_id;
            }
            String _usuario_id = "" + params[1];
            while (_usuario_id.length() < 4) {
                _usuario_id = "0" + _usuario_id;
            }
            String _tienda_id = "" + params[2];
            while (_tienda_id.length() < 6) {
                _tienda_id = "0" + _tienda_id;
            }
            String strResult = _cuenta_id + _usuario_id + _tienda_id + ".jpeg";

            result.add(strResult);
            result.add(_cuenta_id);
            result.add(_usuario_id);
            result.add(_tienda_id);

            return result;
        }

        public static class Entrada {

            public Entrada(Object... params) {
                // TODO Auto-generated constructor stub

                response = "";
                inConection = true;

                val$_imgData = (byte[]) params[0];
                se_cuenta = params[1].toString();
                se_tienda = params[2].toString();
                se_usuario = params[3].toString();

                httpConn = null;
                os = null;
                is = null;

                parametros = NombreImagen(se_cuenta, se_usuario, se_tienda);
                String pic = parametros.get(0); // _cuenta_id + _usuario_id +
                // _tienda_id + ".jpeg";

                String form_field = "";
                form_field = "uploadedfile";
                try {
                    URL url = new URL(Ajustes.Url_Send_entrada);

                    httpConn = (HttpURLConnection) url.openConnection();
                    httpConn.setRequestMethod("POST");

                    String postData = "";

                    String message1 = "";
                    message1 = message1
                            + "-----------------------------4664151417711\r\n";
                    message1 = message1
                            + "Content-Disposition: form-data; name=\""
                            + form_field + "\"; filename=\"" + pic + "\""
                            + "\r\n";
                    message1 = message1 + "Content-Type: image/png\r\n";
                    message1 = message1 + "\r\n";

                    String message2 = "";
                    message2 = message2
                            + "\r\n-----------------------------4664151417711\r\n";

                    httpConn.setRequestProperty("Content-Type",
                            "multipart/form-data; boundary=---------------------------4664151417711");

                    os = httpConn.getOutputStream();

                    os.write(message1.getBytes());

                    int index = 0;
                    int size = 1024;
                    do {
                        if (index + size > val$_imgData.length) {
                            size = val$_imgData.length - index;
                        }
                        os.write(val$_imgData, index, size);
                        index += size;
                    } while (index < val$_imgData.length);

                    os.write(message2.getBytes());

                    String strparams = "";
                    strparams = strparams
                            + "Content-Disposition: form-data; name=\"id\"\r\n";
                    strparams = strparams + "\r\n" + parametros.get(1)
                            + parametros.get(2) + parametros.get(3)
                            + "|idCuenta=" + se_cuenta + "|idUsuario="
                            + se_usuario + "|idTienda=" + se_tienda + "\r\n";// +
                    // (envioAsistencia
                    // ?
                    // ""
                    // :
                    // new
                    // StringBuffer().append("|idCategoriaFoto=").append(EVOLVEMIDlet.myMidlet.categoriaFoto).append("|comentariosFoto=").append(EVOLVEMIDlet.myMidlet.comentariosFoto).append("|opciones_foto=").append(EVOLVEMIDlet.myMidlet.opcionesFoto).toString())
                    // +
                    // "\r\n";

                    strparams = strparams
                            + "\r\n-----------------------------4664151417711--\r\n";
                    System.out.println(strparams);
                    os.write(strparams.getBytes());

                    os.flush();

                    System.out.println("open is");
                    is = httpConn.getInputStream();
                    char buff = '?';

                    byte[] data = new byte[buff];
                    int len;
                    do {
                        System.out.println("READ");
                        len = is.read(data);

                        if (len > 0) {
                            response += new String(data, 0, len);
                        }
                    } while (len > 0);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    httpConn.disconnect();
                }
            }

            public String GetResult() {
                return response;
            }
        }

        public static class Salida {

            public Salida(Object... params) {
                // TODO Auto-generated constructor stub

                response = "";
                inConection = true;

                val$_imgData = (byte[]) params[0];
                se_cuenta = params[1].toString();
                se_tienda = params[2].toString();
                se_usuario = params[3].toString();

                httpConn = null;
                os = null;
                is = null;

                parametros = NombreImagen(se_cuenta, se_usuario, se_tienda);
                String pic = parametros.get(0); // _cuenta_id + _usuario_id +
                // _tienda_id + ".jpeg";

                String form_field = "";
                form_field = "uploadedfile";
                try {
                    URL url = new URL(Ajustes.Url_Send_Salida);

                    httpConn = (HttpURLConnection) url.openConnection();
                    httpConn.setRequestMethod("POST");

                    String postData = "";

                    String message1 = "";
                    message1 = message1
                            + "-----------------------------4664151417711\r\n";
                    message1 = message1
                            + "Content-Disposition: form-data; name=\""
                            + form_field + "\"; filename=\"" + pic + "\""
                            + "\r\n";
                    message1 = message1 + "Content-Type: image/png\r\n";
                    message1 = message1 + "\r\n";

                    String message2 = "";
                    message2 = message2
                            + "\r\n-----------------------------4664151417711\r\n";

                    httpConn.setRequestProperty("Content-Type",
                            "multipart/form-data; boundary=---------------------------4664151417711");

                    os = httpConn.getOutputStream();

                    os.write(message1.getBytes());

                    int index = 0;
                    int size = 1024;
                    do {
                        if (index + size > val$_imgData.length) {
                            size = val$_imgData.length - index;
                        }
                        os.write(val$_imgData, index, size);
                        index += size;
                    } while (index < val$_imgData.length);

                    os.write(message2.getBytes());

                    String strparams = "";
                    strparams = strparams
                            + "Content-Disposition: form-data; name=\"id\"\r\n";
                    strparams = strparams + "\r\n" + parametros.get(1)
                            + parametros.get(2) + parametros.get(3)
                            + "|idCuenta=" + se_cuenta + "|idUsuario="
                            + se_usuario + "|idTienda=" + se_tienda + "\r\n";// +
                    // (envioAsistencia
                    // ?
                    // ""
                    // :
                    // new
                    // StringBuffer().append("|idCategoriaFoto=").append(EVOLVEMIDlet.myMidlet.categoriaFoto).append("|comentariosFoto=").append(EVOLVEMIDlet.myMidlet.comentariosFoto).append("|opciones_foto=").append(EVOLVEMIDlet.myMidlet.opcionesFoto).toString())
                    // +
                    // "\r\n";

                    strparams = strparams
                            + "\r\n-----------------------------4664151417711--\r\n";
                    System.out.println(strparams);
                    os.write(strparams.getBytes());

                    os.flush();

                    System.out.println("open is");
                    is = httpConn.getInputStream();
                    char buff = '?';

                    byte[] data = new byte[buff];
                    int len;
                    do {
                        System.out.println("READ");
                        len = is.read(data);

                        if (len > 0) {
                            response += new String(data, 0, len);
                        }
                    } while (len > 0);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    httpConn.disconnect();
                }
            }

            public String GetResult() {
                return response;
            }
        }

        public static class Sondeo {

            public Sondeo(Object... params) {
                // TODO Auto-generated constructor stub

                response = "";
                inConection = true;

                val$_imgData = (byte[]) params[0];
                se_cuenta = params[1].toString();
                se_tienda = params[2].toString();
                se_usuario = params[3].toString();
                se_categoria = params[4].toString();
                se_comentario = params[5].toString();
                se_opcion = params[6].toString();

                httpConn = null;
                os = null;
                is = null;

                parametros = NombreImagen(se_cuenta, se_usuario, se_tienda);
                String pic = parametros.get(0); // _cuenta_id + _usuario_id +
                // _tienda_id + ".jpeg";

                String form_field = "";
                form_field = "uploadedfile";
                try {
                    URL url = new URL(Ajustes.Url_Upload);

                    httpConn = (HttpURLConnection) url.openConnection();
                    httpConn.setRequestMethod("POST");

                    String postData = "";

                    String message1 = "";
                    message1 = message1
                            + "-----------------------------4664151417711\r\n";
                    message1 = message1
                            + "Content-Disposition: form-data; name=\""
                            + form_field + "\"; filename=\"" + pic + "\""
                            + "\r\n";
                    message1 = message1 + "Content-Type: image/png\r\n";
                    message1 = message1 + "\r\n";

                    String message2 = "";
                    message2 = message2
                            + "\r\n-----------------------------4664151417711\r\n";

                    httpConn.setRequestProperty("Content-Type",
                            "multipart/form-data; boundary=---------------------------4664151417711");

                    os = httpConn.getOutputStream();

                    os.write(message1.getBytes());

                    int index = 0;
                    int size = 1024;
                    do {
                        if (index + size > val$_imgData.length) {
                            size = val$_imgData.length - index;
                        }
                        os.write(val$_imgData, index, size);
                        index += size;
                    } while (index < val$_imgData.length);

                    os.write(message2.getBytes());

                    String strparams = "";
                    strparams = strparams
                            + "Content-Disposition: form-data; name=\"id\"\r\n";
                    strparams = strparams + "\r\n" + parametros.get(1)
                            + parametros.get(2) + parametros.get(3)
                            + "|idCuenta=" + parametros.get(1) + "|idUsuario="
                            + parametros.get(2) + "|idTienda="
                            + parametros.get(3) + "|idCategoriaFoto="
                            + se_categoria + "|comentariosFoto="
                            + se_comentario + "|opciones_foto=" + se_opcion
                            + "\r\n";// +

                    strparams = strparams
                            + "\r\n-----------------------------4664151417711--\r\n";
                    System.out.println(strparams);
                    os.write(strparams.getBytes());

                    os.flush();

                    System.out.println("open is");
                    is = httpConn.getInputStream();
                    char buff = '?';

                    byte[] data = new byte[buff];
                    int len;
                    do {
                        System.out.println("READ");
                        len = is.read(data);

                        if (len > 0) {
                            response += new String(data, 0, len);
                        }
                    } while (len > 0);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    httpConn.disconnect();
                }
            }

            public String GetResult() {
                return response;
            }
        }
    }
    static HttpURLConnection httpConn = null;
    static InputStream is = null;
    static OutputStream os = null;
    static boolean inConection = false;
    static String response = null;
    static String CrLf = "\r\n";

    public static void enviarFoto(final byte[] _imgData, final String strUrl,
            final String se_cuenta, final String se_usuario,
            final String se_tienda) {
        response = "";
        inConection = true;

        // final String val$URL = URL;
        final byte[] val$_imgData = _imgData;

        // new Thread() {

        // public void run() {

        // try {
        // sleep(10000);
        // } catch (InterruptedException e1) {
        // TODO Auto-generated catch block
        // e1.printStackTrace();
        // }
        httpConn = null;
        os = null;
        is = null;

        String pic = "";

        String _cuenta_id = "" + se_cuenta;
        while (_cuenta_id.length() < 3) {
            _cuenta_id = "0" + _cuenta_id;
        }
        String _usuario_id = "" + se_usuario;
        while (_usuario_id.length() < 4) {
            _usuario_id = "0" + _usuario_id;
        }
        String _tienda_id = "" + se_tienda;
        while (_tienda_id.length() < 6) {
            _tienda_id = "0" + _tienda_id;
        }
        pic = _cuenta_id + _usuario_id + _tienda_id + ".jpeg";

        String form_field = "";
        form_field = "uploadedfile";
        try {
            URL url = new URL(strUrl);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("POST");

            String postData = "";

            String message1 = "";
            message1 = message1
                    + "-----------------------------4664151417711\r\n";
            message1 = message1 + "Content-Disposition: form-data; name=\""
                    + form_field + "\"; filename=\"" + pic + "\"" + "\r\n";
            message1 = message1 + "Content-Type: image/png\r\n";
            message1 = message1 + "\r\n";

            String message2 = "";
            message2 = message2
                    + "\r\n-----------------------------4664151417711\r\n";

            httpConn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=---------------------------4664151417711");

            os = httpConn.getOutputStream();

            os.write(message1.getBytes());

            int index = 0;
            int size = 1024;
            do {
                if (index + size > val$_imgData.length) {
                    size = val$_imgData.length - index;
                }
                os.write(val$_imgData, index, size);
                index += size;
            } while (index < val$_imgData.length);

            os.write(message2.getBytes());

            String params = "";
            params = params + "Content-Disposition: form-data; name=\"id\"\r\n";

            _cuenta_id = "" + se_cuenta;
            while (_cuenta_id.length() < 3) {
                _cuenta_id = "0" + _cuenta_id;
            }
            _usuario_id = "" + se_usuario;
            while (_usuario_id.length() < 4) {
                _usuario_id = "0" + _usuario_id;
            }
            _tienda_id = "" + se_tienda;
            while (_tienda_id.length() < 6) {
                _tienda_id = "0" + _tienda_id;
            }
            params = params + "\r\n" + _cuenta_id + _usuario_id + _tienda_id
                    + "|idCuenta=" + se_cuenta + "|idUsuario=" + se_usuario
                    + "|idTienda=" + se_tienda + "\r\n";// + (envioAsistencia ?
            // "" : new
            // StringBuffer().append("|idCategoriaFoto=").append(EVOLVEMIDlet.myMidlet.categoriaFoto).append("|comentariosFoto=").append(EVOLVEMIDlet.myMidlet.comentariosFoto).append("|opciones_foto=").append(EVOLVEMIDlet.myMidlet.opcionesFoto).toString())
            // + "\r\n";

            params = params
                    + "\r\n-----------------------------4664151417711--\r\n";
            System.out.println(params);
            os.write(params.getBytes());

            os.flush();

            System.out.println("open is");
            is = httpConn.getInputStream();
            char buff = '?';

            byte[] data = new byte[buff];
            int len;
            do {
                System.out.println("READ");
                len = is.read(data);

                if (len > 0) {
                    response += new String(data, 0, len);
                }
            } while (len > 0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpConn.disconnect();
        }
    }

    public static void Foto(final byte[] _imgData, final String strUrl,
            final String se_cuenta, final String se_usuario,
            final String se_tienda, final String categoria,
            final String comentario, final String opciones) {

        response = "";
        inConection = true;

        // final String val$URL = URL;
        final byte[] val$_imgData = _imgData;

        // new Thread() {

        // public void run() {

        // try { // sleep(10000); // } catch (InterruptedException e1) { // TODO
        // Auto-generated catch block // e1.printStackTrace(); // } httpConn =
        // null;
        os = null;
        is = null;

        String pic = "";

        String _cuenta_id = "" + se_cuenta;
        while (_cuenta_id.length() < 3) {
            _cuenta_id = "0" + _cuenta_id;
        }
        String _usuario_id = "" + se_usuario;
        while (_usuario_id.length() < 4) {
            _usuario_id = "0" + _usuario_id;
        }
        String _tienda_id = "" + se_tienda;
        while (_tienda_id.length() < 6) {
            _tienda_id = "0" + _tienda_id;
        }
        pic = _cuenta_id + _usuario_id + _tienda_id + ".jpeg";

        String form_field = "";
        form_field = "uploadedfile";
        try {
            URL url = new URL(strUrl);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("POST");

            String postData = "";

            String message1 = "";
            message1 = message1
                    + "-----------------------------4664151417711\r\n";
            message1 = message1 + "Content-Disposition: form-data; name=\""
                    + form_field + "\"; filename=\"" + pic + "\"" + "\r\n";
            message1 = message1 + "Content-Type: image/png\r\n";
            message1 = message1 + "\r\n";

            String message2 = "";
            message2 = message2
                    + "\r\n-----------------------------4664151417711\r\n";

            httpConn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=---------------------------4664151417711");

            os = httpConn.getOutputStream();

            os.write(message1.getBytes());

            int index = 0;
            int size = 1024;
            do {
                if (index + size > val$_imgData.length) {
                    size = val$_imgData.length - index;
                }
                os.write(val$_imgData, index, size);
                index += size;
            } while (index < val$_imgData.length);

            os.write(message2.getBytes());

            String params = "";
            params = params + "Content-Disposition: form-data; name=\"id\"\r\n";

            _cuenta_id = "" + se_cuenta;
            while (_cuenta_id.length() < 3) {
                _cuenta_id = "0" + _cuenta_id;
            }
            _usuario_id = "" + se_usuario;
            while (_usuario_id.length() < 4) {
                _usuario_id = "0" + _usuario_id;
            }
            _tienda_id = "" + se_tienda;
            while (_tienda_id.length() < 6) {
                _tienda_id = "0" + _tienda_id;
            }
            params = params + "\r\n" + _cuenta_id + _usuario_id + _tienda_id
                    + "|idCuenta=" + se_cuenta + "|idUsuario=" + se_usuario
                    + "|idTienda=" + se_tienda + "|idCategoriaFoto="
                    + categoria + "|comentariosFoto=" + comentario
                    + "|opciones_foto=" + opciones + "\r\n";

            // + (envioAsistencia ? // "" : new //
            // StringBuffer().append("|idCategoriaFoto="
            // ).append(EVOLVEMIDlet.myMidlet.categoriaFoto
            // ).append("|comentariosFoto=").
            // append(EVOLVEMIDlet.myMidlet.comentariosFoto
            // ).append("|opciones_foto=").append
            // (EVOLVEMIDlet.myMidlet.opcionesFoto).toString()) // + "\r\n";

            params = params
                    + "\r\n-----------------------------4664151417711--\r\n";
            System.out.println(params);
            os.write(params.getBytes());

            os.flush();

            System.out.println("open is");
            is = httpConn.getInputStream();
            char buff = '?';

            byte[] data = new byte[buff];
            int len;
            do {
                System.out.println("READ");
                len = is.read(data);

                if (len > 0) {
                    response += new String(data, 0, len);
                }
            } while (len > 0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpConn.disconnect();
        }
    }

    public static class Fotografia {

        private String response = null;
        private HttpURLConnection httpConn = null;
        private InputStream is = null;
        private OutputStream os = null;
        private boolean inConection = false;
        private String CrLf = "\r\n";
        private String pic, form_field;
        private String _cuenta_id, _usuario_id, _tienda_id, _categoria_id,
                _comentario, _opcion_id;

        public Fotografia(Object... params) {

            response = "";
            inConection = true;
            httpConn = null;
            os = null;
            is = null;
            pic = "";
            form_field = "uploadedfile";

            byte[] val$_imgData = (byte[]) params[0];

            _cuenta_id = "" + params[1].toString();
            _usuario_id = "" + params[2].toString();
            _tienda_id = "" + params[3].toString();
            _categoria_id = params[4].toString();
            _comentario = params[5].toString();
            _opcion_id = params[6].toString();

            while (_cuenta_id.length() < 3) {
                _cuenta_id = "0" + _cuenta_id;
            }

            while (_usuario_id.length() < 4) {
                _usuario_id = "0" + _usuario_id;
            }

            while (_tienda_id.length() < 6) {
                _tienda_id = "0" + _tienda_id;
            }

            pic = _cuenta_id + _usuario_id + _tienda_id + ".jpeg";

            try {
                URL url = new URL(Ajustes.Url_Upload);
                httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setRequestMethod("POST");

                String postData = "";

                String message1 = "";
                message1 = message1
                        + "-----------------------------4664151417711\r\n";
                message1 = message1 + "Content-Disposition: form-data; name=\""
                        + form_field + "\"; filename=\"" + pic + "\"" + "\r\n";
                message1 = message1 + "Content-Type: image/png\r\n";
                message1 = message1 + "\r\n";

                String message2 = "";
                message2 = message2
                        + "\r\n-----------------------------4664151417711\r\n";

                httpConn.setRequestProperty("Content-Type",
                        "multipart/form-data; boundary=---------------------------4664151417711");

                os = httpConn.getOutputStream();

                os.write(message1.getBytes());

                int index = 0;
                int size = 1024;
                do {
                    if (index + size > val$_imgData.length) {
                        size = val$_imgData.length - index;
                    }
                    os.write(val$_imgData, index, size);
                    index += size;
                } while (index < val$_imgData.length);

                os.write(message2.getBytes());

                String parameters = "";
                parameters = parameters
                        + "Content-Disposition: form-data; name=\"id\"\r\n";

                // _cuenta_id = "" + se_cuenta; while (_cuenta_id.length() < 3)
                // { _cuenta_id = "0" + _cuenta_id; }
                // _usuario_id = "" + se_usuario; while (_usuario_id.length() <
                // 4) { _usuario_id = "0" + _usuario_id; }
                // _tienda_id = "" + se_tienda; while (_tienda_id.length() < 6)
                // { _tienda_id = "0" + _tienda_id; }

                parameters = parameters + "\r\n" + _cuenta_id + _usuario_id
                        + _tienda_id + "|idCuenta=" + _cuenta_id
                        + "|idUsuario=" + _usuario_id + "|idTienda="
                        + _tienda_id + "|idCategoriaFoto=" + _categoria_id
                        + "|comentariosFoto=" + _comentario + "|opciones_foto="
                        + _opcion_id + "\r\n";

                parameters = parameters
                        + "\r\n-----------------------------4664151417711--\r\n";
                System.out.println(parameters);
                os.write(parameters.getBytes());

                os.flush();

                System.out.println("open is");
                is = httpConn.getInputStream();
                char buff = '?';

                byte[] data = new byte[buff];
                int len;
                do {
                    System.out.println("READ");
                    len = is.read(data);

                    if (len > 0) {
                        response += new String(data, 0, len);
                    }
                } while (len > 0);
            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                httpConn.disconnect();
            }
        }

        // TODO Auto-generated method stub // return null; }
        public String GetRespons() {

            return this.response;
        }
    }

    public static class Tienda {

        static String strUrl = Ajustes.Url_send_tiendas;
        static InputStream in = null;
        static int respuesta = -1;
        static URL url = null;
        static String result = null;

        public static class MaterialPromocional {

            public MaterialPromocional(String... params) {
                // TODO Auto-generated method stub

                StringBuilder strParams = new StringBuilder();
                strParams.append("?sku=" + params[0]);
                strParams.append("&idProyecto=" + params[1]);
                strParams.append("&idUsuario=" + params[2]);
                strParams.append("&determinanteGPS=" + params[3]);
                strParams.append("&isMaterialPOP=1");
                strParams.append("&matPopCenefas=" + params[4]);
                strParams.append("&matPopDanglers=" + params[5]);
                strParams.append("&matPopStoppers=" + params[6]);
                strParams.append("&matPopColgantes=" + params[7]);
                strParams.append("&matPopCartulina=" + params[8]);
                strParams.append("&matPopCorbata=" + params[9]);
                strParams.append("&matPopFlash=" + params[10]);
                strParams.append("&matPopTira=" + params[11]);
                strParams.append("&matPopPreciador=" + params[12]);
                strParams.append("&matPopFolleto=" + params[13]);
                strParams.append("&matPopTapete=" + params[14]);
                strParams.append("&matPopFaldon=" + params[15]);
                strParams.append("&matPopOtros=" + params[16]);
                strParams.append("&comentariosExhAdic=" + params[17]);

                try {

                    // StrictMode.ThreadPolicy policy = new
                    // StrictMode.ThreadPolicy.Builder()
                    // .permitAll().build();
                    // StrictMode.setThreadPolicy(policy);

                    url = new URL(strUrl + strParams.toString());

                    URLConnection conn = url.openConnection();
                    if (!(conn instanceof HttpURLConnection)) {
                        throw new IOException("No es una conexion HTTP");
                    }

                    HttpURLConnection httpConn = (HttpURLConnection) conn;

                    // La conexion no permite interaccion con el usuario
                    httpConn.setAllowUserInteraction(false);

                    // Si permite que la conexion siga las redirecciones
                    httpConn.setInstanceFollowRedirects(true);

                    // Tipo de request
                    httpConn.setRequestMethod("GET");
                    httpConn.connect();
                    respuesta = httpConn.getResponseCode();
                    if (respuesta == HttpURLConnection.HTTP_OK) {
                        in = httpConn.getInputStream();

                        StringBuffer sb = new StringBuffer();

                        int chr;
                        while ((chr = in.read()) != -1) {
                            sb.append((char) chr);
                        }
                        result = sb.toString();
                    }

                } catch (IOException e) {

                    e.printStackTrace();
                }
            }

            public String GetResult() {

                return result;
            }
        }

        public static class ExhibicionAdicional {

            public ExhibicionAdicional(String... params) {
                // TODO Auto-generated method stub

                StringBuilder strParams = new StringBuilder();
                strParams.append("?sku=" + params[0]);
                strParams.append("&idProyecto=" + params[1]);
                strParams.append("&idUsuario=" + params[2]);
                strParams.append("&determinanteGPS=" + params[3]);
                strParams.append("&comentariosExhAdic=" + params[4]);
                strParams.append("&isOtro=" + params[5]);
                strParams.append("&isForway=" + params[6]);
                strParams.append("&isAriete=" + params[7]);
                strParams.append("&isCajas=" + params[8]);
                strParams.append("&isTiras=" + params[9]);
                strParams.append("&isAreaFlex=" + params[10]);
                strParams.append("&isBunkers=" + params[11]);
                strParams.append("&isExhibidor=" + params[12]);
                strParams.append("&isIslas=" + params[13]);
                strParams.append("&numeroDeCabeceras=" + params[14]);
                strParams.append("&frentesDeCabeceras=" + params[15]);
                strParams.append("&islasFrentes=" + params[16]);
                strParams.append("&exhibidorFrentes=" + params[17]);
                strParams.append("&bunkersFrentes=" + params[18]);
                strParams.append("&areaFlexFrentes=" + params[19]);
                strParams.append("&tirasFrentes=" + params[20]);
                strParams.append("&cajasFrentes=" + params[21]);
                strParams.append("&arietesFrentes=" + params[22]);
                strParams.append("&forwayFrentes=" + params[23]);
                strParams.append("&otrosFrentes=" + params[24]);

                try {

                    url = new URL(strUrl + strParams.toString());

                    URLConnection conn = url.openConnection();
                    if (!(conn instanceof HttpURLConnection)) {
                        throw new IOException("No es una conexion HTTP");
                    }

                    HttpURLConnection httpConn = (HttpURLConnection) conn;

                    // La conexion no permite interaccion con el usuario
                    httpConn.setAllowUserInteraction(false);

                    // Si permite que la conexion siga las redirecciones
                    httpConn.setInstanceFollowRedirects(true);

                    // Tipo de request
                    httpConn.setRequestMethod("GET");
                    httpConn.connect();
                    respuesta = httpConn.getResponseCode();
                    if (respuesta == HttpURLConnection.HTTP_OK) {
                        in = httpConn.getInputStream();

                        StringBuffer sb = new StringBuffer();

                        int chr;
                        while ((chr = in.read()) != -1) {
                            sb.append((char) chr);
                        }
                        result = sb.toString();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public String GetResult() {

                return result;
            }
        }

        public static class Anaquel {

            public Anaquel(String... params) {
                // TODO Auto-generated method stub

                StringBuilder strParams = new StringBuilder();
                strParams.append(String.format("?sku=%s", params[0]));
                strParams.append(String.format("&idProyecto=%s", params[1]));
                strParams.append(String.format("&idUsuario=%s", params[2]));
                strParams.append(String.format("&determinanteGPS=%s", params[3]));
                strParams.append(String.format("&cantAnaquel=%s", params[4]));
                strParams.append(String.format("&precio=%s", params[5]));
                strParams.append(String.format("&comentarioAnaquel=%s", params[6]));
                strParams.append(String.format("&tramosAnaquel=%s", params[7]));
                strParams.append(String.format("&suelo=%s", params[8]));
                strParams.append(String.format("&manos=%s", params[9]));
                strParams.append(String.format("&ojo=%s", params[10]));
                strParams.append(String.format("&techo=%s", params[11]));

                try {

                    url = new URL(strUrl + strParams.toString());

                    URLConnection conn = url.openConnection();
                    if (!(conn instanceof HttpURLConnection)) {
                        throw new IOException("No es una conexion HTTP");
                    }

                    HttpURLConnection httpConn = (HttpURLConnection) conn;

                    // La conexion no permite interaccion con el usuario
                    httpConn.setAllowUserInteraction(false);

                    // Si permite que la conexion siga las redirecciones
                    httpConn.setInstanceFollowRedirects(true);

                    // Tipo de request
                    httpConn.setRequestMethod("GET");
                    httpConn.connect();
                    respuesta = httpConn.getResponseCode();
                    if (respuesta == HttpURLConnection.HTTP_OK) {
                        in = httpConn.getInputStream();

                        StringBuffer sb = new StringBuffer();

                        int chr;
                        while ((chr = in.read()) != -1) {
                            sb.append((char) chr);
                        }
                        result = sb.toString();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public String GetResult() {

                return result;
            }
        }

        public static class Frentes {

            public Frentes(String... params) {
                // TODO Auto-generated method stub

                StringBuilder strParams = new StringBuilder();
                strParams.append(String.format("?sku=%s", params[0]));
                strParams.append(String.format("&idProyecto=%s", params[1]));
                strParams.append(String.format("&idUsuario=%s", params[2]));
                strParams.append(String.format("&determinanteGPS=%s", params[3]));
                strParams.append(String.format("&cantFrentesEnFrio=%s", params[4]));
                strParams.append(String.format("&precioFrentesEnFrio=%s", params[5]));
                strParams.append(String.format("&comentarioFrentesEnFrio=%s", params[6]));
                //strParams.append(String.format("&tramosAnaquel=%s", params[7]));
                strParams.append(String.format("&sueloFrentesEnFrio=%s", params[7]));
                strParams.append(String.format("&manosFrentesEnFrio=%s", params[8]));
                strParams.append(String.format("&ojoFrentesEnFrio=%s", params[9]));
                strParams.append(String.format("&techoFrentesEnFrio=%s", params[10]));

                try {

                    url = new URL(strUrl + strParams.toString());

                    URLConnection conn = url.openConnection();
                    if (!(conn instanceof HttpURLConnection)) {
                        throw new IOException("No es una conexion HTTP");
                    }

                    HttpURLConnection httpConn = (HttpURLConnection) conn;

                    // La conexion no permite interaccion con el usuario
                    httpConn.setAllowUserInteraction(false);

                    // Si permite que la conexion siga las redirecciones
                    httpConn.setInstanceFollowRedirects(true);

                    // Tipo de request
                    httpConn.setRequestMethod("GET");
                    httpConn.connect();
                    respuesta = httpConn.getResponseCode();
                    if (respuesta == HttpURLConnection.HTTP_OK) {
                        in = httpConn.getInputStream();

                        StringBuffer sb = new StringBuffer();

                        int chr;
                        while ((chr = in.read()) != -1) {
                            sb.append((char) chr);
                        }
                        result = sb.toString();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public String GetResult() {

                return result;
            }
        }

        public static class Bodega {

            public Bodega(String... params) {
                // TODO Auto-generated method stub
                StringBuilder strParams = new StringBuilder();
                strParams.append(String.format("?sku=%s", params[0]));
                strParams.append(String.format("&idProyecto=%s", params[1]));
                strParams.append(String.format("&idUsuario=%s", params[2]));
                strParams.append(String.format("&determinanteGPS=%s", params[3]));
                strParams.append(String.format("&cantBodega=%s", params[4]));
                strParams.append(String.format("&tarimasBodega=%s", params[5]));
                strParams.append(String.format("&comentariosBodega=%s", params[6]));


                try {

                    url = new URL(strUrl + strParams.toString());

                    URLConnection conn = url.openConnection();
                    if (!(conn instanceof HttpURLConnection)) {
                        throw new IOException("No es una conexion HTTP");
                    }

                    HttpURLConnection httpConn = (HttpURLConnection) conn;

                    // La conexion no permite interaccion con el usuario
                    httpConn.setAllowUserInteraction(false);

                    // Si permite que la conexion siga las redirecciones
                    httpConn.setInstanceFollowRedirects(true);

                    // Tipo de request
                    httpConn.setRequestMethod("GET");
                    httpConn.connect();
                    respuesta = httpConn.getResponseCode();
                    if (respuesta == HttpURLConnection.HTTP_OK) {
                        in = httpConn.getInputStream();

                        StringBuffer sb = new StringBuffer();

                        int chr;
                        while ((chr = in.read()) != -1) {
                            sb.append((char) chr);
                        }
                        result = sb.toString();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public String GetResult() {

                return result;
            }
        }
    }

    public static class Sondeo {

        public static class ObtieneCuestionarios {

            Document doc = null;

            public ObtieneCuestionarios(String... params)
                    throws XPathExpressionException {
                // TODO Auto-generated constructor stub
                String parameters = String.format(
                        "?idProyecto=%s&idUsuario=%s&idTienda=%s", params[0],
                        params[1], params[2]);

                InputStream in = null;
                int respuesta = -1;
                URL url = null;

                try {

                    /*
                     * StrictMode.ThreadPolicy policy = new
                     * StrictMode.ThreadPolicy.Builder() .permitAll().build();
                     * StrictMode.setThreadPolicy(policy);
                     */

                    url = new URL(Ajustes.Url_Descarga_Sondeo + parameters);

                    URLConnection conn = url.openConnection();
                    if (!(conn instanceof HttpURLConnection)) {
                        throw new IOException("No es una conexion HTTP");
                    }

                    HttpURLConnection httpConn = (HttpURLConnection) conn;

                    // La conexion no permite interaccion con el usuario
                    httpConn.setAllowUserInteraction(false);

                    // Si permite que la conexion siga las redirecciones
                    httpConn.setInstanceFollowRedirects(true);

                    // Tipo de request
                    httpConn.setRequestMethod("GET");
                    httpConn.connect();
                    respuesta = httpConn.getResponseCode();
                    if (respuesta == HttpURLConnection.HTTP_OK) {
                        in = httpConn.getInputStream();

                        DocumentBuilder builder = DocumentBuilderFactory
                                .newInstance().newDocumentBuilder();
                        doc = builder.parse(in, null);
                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (SAXException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            public SondeoCollection GetResult() throws XPathExpressionException {

                SondeoCollection sondeos = new SondeoCollection();
                mx.smartteam.entities.Sondeo sondeo = null;
                mx.smartteam.entities.Pregunta pregunta;
                if (doc != null) {

                    XPath xpath = XPathFactory.newInstance().newXPath();
                    String expression = "/cuestionarios/encuesta";
                    NodeList nodeList = (NodeList) xpath.evaluate(expression,
                            doc, XPathConstants.NODESET);

                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node encuesta = nodeList.item(i);
                        // datos de la encuesta
                        sondeo = new mx.smartteam.entities.Sondeo();
                        sondeo.Id = Integer.parseInt(((Element) encuesta)
                                .getAttribute("id").toString());
                        sondeo.Nombre = ((Element) encuesta).getAttribute(
                                "nombre").toString();


                        if (!((Element) encuesta).getAttribute("indice").toString().isEmpty()) {

                            sondeo.Orden = Integer.parseInt(((Element) encuesta)
                                    .getAttribute("indice").toString());

                            // preguntas de la encuesta
                            expression = "preguntaEncuesta";
                            NodeList nodeListPreg = (NodeList) xpath.evaluate(
                                    expression, encuesta, XPathConstants.NODESET);



                            for (int j = 0; j < nodeListPreg.getLength(); j++) {
                                Node nodPregunta = nodeListPreg.item(j);
                                pregunta = new Pregunta();
                                pregunta.Id = Integer
                                        .parseInt(((Element) nodPregunta)
                                        .getAttribute("id").toString());
                                pregunta.Tipo = ((Element) nodPregunta)
                                        .getAttribute("tipo").toString();

                                pregunta.Nombre = ((Element) nodPregunta)
                                        .getAttribute("texto").toString();

                                pregunta.Requerido = ((Element) nodPregunta).getAttribute("requerido").toString().equals("0") ? false : true;

                                pregunta.Longitud = Integer.parseInt(((Element) nodPregunta).getAttribute("longitud").toString());


                                // Respuestas opcion
                                expression = "opcionEncuesta";
                                NodeList nodeListOpcionResp = (NodeList) xpath
                                        .evaluate(expression, nodPregunta,
                                        XPathConstants.NODESET);

                                for (int k = 0; k < nodeListOpcionResp.getLength(); k++) {
                                    Node nodOpcionResp = nodeListOpcionResp.item(k);
                                    Opcion opcionResp = new Opcion();
                                    opcionResp.Id = Integer
                                            .parseInt(((Element) nodOpcionResp)
                                            .getAttribute("id").toString());

                                    opcionResp.Nombre = ((Element) nodOpcionResp)
                                            .getAttribute("texto").toString();

                                    pregunta.Opciones.add(opcionResp);
                                }
                                sondeo.Pregunta.add(pregunta);
                            }
                            sondeos.add(sondeo);
                        }
                    }

                }
                return sondeos;
            }
        }

        public static class GuardaCuestionario {

            String doc = null;

            public GuardaCuestionario(String... params) {

                StringBuilder strResult = new StringBuilder();

                strResult.append(String.format("?idProyecto=%s", params[0]));
                strResult.append(String.format("&idUsuario=%s", params[1]));
                strResult.append(String
                        .format("&determinanteGPS=%s", params[2]));
                strResult.append(String.format("&SondeoID=%s", params[3]));
                strResult.append(String.format("&sondeo=%s", params[4]));

                InputStream in = null;
                int respuesta = -1;
                URL url = null;

                try {

                    url = new URL(Ajustes.Url_send_sondeo
                            + strResult.toString());

                    URLConnection conn = url.openConnection();
                    if (!(conn instanceof HttpURLConnection)) {
                        throw new IOException("No es una conexion HTTP");
                    }

                    HttpURLConnection httpConn = (HttpURLConnection) conn;

                    // La conexion no permite interaccion con el usuario
                    httpConn.setAllowUserInteraction(false);

                    // Si permite que la conexion siga las redirecciones
                    httpConn.setInstanceFollowRedirects(true);

                    // Tipo de request
                    httpConn.setRequestMethod("GET");
                    httpConn.connect();
                    respuesta = httpConn.getResponseCode();
                    if (respuesta == HttpURLConnection.HTTP_OK) {
                        in = httpConn.getInputStream();

                        StringBuffer sb = new StringBuffer();

                        int chr;
                        while ((chr = in.read()) != -1) {
                            sb.append((char) chr);
                        }

                        doc = sb.toString();
                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            public String GetResult() {
                return doc;

            }
        }
    }

    public static class Producto {

        public static class ObtieneListado {

            Conection conection = null;

            public ObtieneListado(String... params) throws MalformedURLException, IOException, ParserConfigurationException, SAXException {

                String strparams = String.format("?idUsuario=%s&idProyecto=%s&idVersion=%s&idTienda=%s", params[0], params[1], params[2], params[3]);
                //String strparams = String.format("?idUsuario=%s&idProyecto=%s&idVersion=0", params[0],params[1],params[2]);
                conection = new Conection(Ajustes.Url_Update_productos, strparams);
                conection.Open();
            }

            public String GetResult() throws ParserConfigurationException, SAXException, IOException {
                if (conection != null) {
                    return conection.GetStrResult();
                }
                return null;
            }
        }
    }

    public static class Notificacion {

        Conection conection = null;

        public Notificacion(String... params) throws MalformedURLException, IOException, ParserConfigurationException, SAXException {

            String strParams = String.format("?idUsuario=%s&idProyecto=%s&idTienda=%s", params[0], params[1], params[2]);

            conection = new Conection(Ajustes.Url_mensajes, strParams);
            conection.Open();
        }

        public String GetResult() throws ParserConfigurationException, SAXException, IOException {
            if (conection != null) {
                return conection.GetStrResult();
            }
            return null;
        }
    }
}
