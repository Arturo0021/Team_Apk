
package mx.smartteam.data;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;


public class ServiceURI {
    //URI servidor donde estan alojados los servicios
    
    private final String localizacion = "http://www.webteam.mx/ServicioTeam35/";// Productivo 
    
    //private final String localizacion = "http://www.webteam.mx/Demo/";//  DEMO 
    //private final String localizacion = "http://192.168.14.88/service/";// Maquina de @Alejandra
    //private final String localizacion = "http://192.168.19.249/webservice/";//  Mi maquina @Arturo
    //private final String localizacion = "http://192.168.19.88/Services/";// Maquina @Jaime
    private final String SERVICE_URI =localizacion + "Tracker.Procesos.svc";
       
    //Nombre de los metodos del servicio 
    private final String METHOD_GETUSUARIO = "/getUsuario";
    private StringEntity strEntity = null;
    private JSONObject jsonResult = null;
    private JSONObject jsonObject = null;
    private JSONStringer jsonString = null;

    public JSONObject HttpGet(String methodName, StringEntity entity) throws IOException, JSONException {
        JSONObject jsonresult = null;

        HttpPost request = new HttpPost(SERVICE_URI + methodName);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        request.setHeader("charset", "UTF-8");
        //request.setHeader("Content-Lenght", "" + "629500000000000");

        request.setEntity(entity);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(request);
        HttpEntity responseEntity = response.getEntity();

        //----------------------------------------------------
        BufferedReader reader1 = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));

        StringBuilder builder = new StringBuilder();
        for (String line = null; (line = reader1.readLine()) != null;) {
            builder.append(line).append("\n");
        }
        Log.i(methodName, builder.toString());
        jsonresult = new JSONObject(builder.toString());
        return jsonresult;
    }

    public String HttpPost(String methodName, StringEntity entity) throws IOException, JSONException {
        HttpPost request = new HttpPost(SERVICE_URI + methodName);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        request.setHeader("charset", "utf-8");

        request.setEntity(entity);


        DefaultHttpClient httpClient = new DefaultHttpClient();

        HttpResponse response = httpClient.execute(request);

        HttpEntity responseEntity = response.getEntity();

        //----------------------------------------------------
        BufferedReader reader1 = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));

        StringBuilder builder = new StringBuilder();
        for (String line = null; (line = reader1.readLine()) != null;) {
            builder.append(line).append("\n");
        }
        
        return builder.toString();

    }

    public JSONArray HttpGetCollection(String methodName, StringEntity entity) throws IOException, JSONException {
        HttpPost request = new HttpPost(SERVICE_URI + methodName);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");

        request.setEntity(entity);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(request);

        HttpEntity responseEntity = response.getEntity();


        char[] buffer = new char[(int) responseEntity.getContentLength()];
        InputStream stream = responseEntity.getContent();
        InputStreamReader reader = new InputStreamReader(stream);
        reader.read(buffer);
        stream.close();

        JSONArray jsonresult = new JSONArray(new String(buffer));
        return jsonresult;

    }

    public void HttpClient(JSONStringer jsonParam) {
        StringBuilder sb = new StringBuilder();
        String http = SERVICE_URI + "/AsistenciaEntradaFotoInsert";
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(http);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.connect();
            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            out.write(URLEncoder.encode( jsonParam.toString(),"UTF-8"));
            out.flush();
            out.close();

            int HttpResult = urlConnection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

                System.out.println("" + sb.toString());

            } else {
                System.out.println(urlConnection.getResponseMessage());
            }
        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        } 
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

}
