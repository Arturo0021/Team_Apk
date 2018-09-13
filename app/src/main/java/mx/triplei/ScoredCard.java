package mx.triplei;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.triplei.R;

public class ScoredCard extends Activity{
    WebView webview;
    Context context; int numdiasmes =0;
    mx.smartteam.entities.Proyecto currentProyecto;
    mx.smartteam.entities.Usuario currentUsuario;
    mx.smartteam.entities.MetaCadenaCollection metaCollection;
    int dxm =0;
        
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoredcard);
        context = this;
        webview = (WebView) findViewById(R.id.webscoredcard);
        
        currentProyecto = (mx.smartteam.entities.Proyecto)getIntent().getSerializableExtra("proyecto");
        currentUsuario = (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
        
        new LlenarTabla().execute(currentProyecto,currentUsuario);
       
    }
      @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scoredcard, menu);
        Drawable d=null;

        return true;
    }

       @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cancelar:
                    finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public static int diasDelMes(int anio, int mes){
        switch(mes){
            case 0:  // Enero
            case 2:  // Marzo
            case 4:  // Mayo
            case 6:  // Julio
            case 7:  // Agosto
            case 9:  // Octubre
            case 11: // Diciembre
            return 31;

            case 3:  // Abril
            case 5:  // Junio
            case 8:  // Septiembre
            case 10: // Noviembre
            return 30;
            case 1:  // Febrero
                if ( ((anio%100 == 0) && (anio%400 == 0)) || ((anio%100 != 0) && (anio%  4 == 0)) )
                    return 29;  // Año Bisiesto
                else
                    return 28;
            default:
            throw new java.lang.IllegalArgumentException( "El mes debe estar entre 0 y 11");
        }
    }

    

    private class LlenarTabla extends AsyncTask<Object, Void, mx.smartteam.entities.ScoredCardCollection>{
            @Override
            protected mx.smartteam.entities.ScoredCardCollection doInBackground(Object... arg0) {
                mx.smartteam.entities.ScoredCardCollection scCollection = null;
                try {
                    String xfecha;
                    xfecha = Utilerias.getFecha();
                    String[] parts = xfecha.split("-");
                    int part1 = Integer.parseInt(parts[0]); // anio
                    int part2 = Integer.parseInt(parts[1]); // mes
                    numdiasmes = diasDelMes(part1, (part2-1));
                    
                    scCollection = mx.smartteam.business.ScoredCard.getAllRegistros(context , currentProyecto, currentUsuario, numdiasmes);
                    
                    metaCollection = mx.smartteam.business.MetaCadena.getAll(context, numdiasmes, currentProyecto);
                    
                } catch (Exception ex) {
                    Logger.getLogger(ScoredCard.class.getName()).log(Level.SEVERE, null, ex);
                }
                return scCollection;
            }

            @Override
            protected void onPostExecute(mx.smartteam.entities.ScoredCardCollection scollection){
                    webview.loadData(pintartabla(scollection), "text/html", null);
            }
            
            
        public String pintartabla (mx.smartteam.entities.ScoredCardCollection sc){
                String tabla = "<table  width='100%' align='center'>" + "  <tr>" + "    <th>#</th>" + "    <th>Fecha</th>" +  "    <th>Cadena</th>" +   " <th>Sucursal</th>"   +           "    <th>Pon</ th>" + "    <th>Cap</th> " + "    <th>Status</th> " +                "  </tr>";
                
                String body = ""; int ii = 1,i=0; int si = 0; int no = 0; 
                String nuevo = "";  String anterior ="";
                int total =0;
               
            String xxx, xxx2 = null;    
            xxx = "<img src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAADb0lEQVRIidWTX2gcRRzHvzOzu5fbXG5zyV1orBX64ov2SUEaT/OiKRQsFYzaFIkiRYu+6EPBh1DQB7GUoIUqVgSpF4wKtiAooiLY+uelpo3aUB9MMJfLJbnLdf/d3s7uzvjgBo40d72mT37hx8z8dufz5febGeD/LnK7gC/mnh9RqJZftRfISqX4d3V9/dLk2NLsDQYj+/axbj358Llz53/YDPl09kiiP33nE65X80rVa+eP5r8Wp3950Bjs2/1xf+qux8IQWK7NYX7xGtYq13+qc3eK6vWPzjwTeMoGJKELdvdDpPDGWP6pidGLFzfy078f2rF7YM93umbc42gpALxYmDk4qyTCvKqJtMvXQCJd+pwXG9J5+tRzpZ9btujDC2OvR8Ke6E6r/1BF/KhpSSvTvfPJjL4zS6HCalTheCvS8pZJQ6xJQhlh6IEiDFSs8n0v5b//bXP1SvOiWrZP5Qa7jkXC3qUw5bCihAhQgxUQoTMDAnWE0iY8dITrOSSSgdBoRKLQm9sKDgC0eXFs9MtKTyp7dm19HbZXJqZfJJa/KB2+SF1eoVHEaRRGUkSE8gak63jUdm1iuZXpreA3VAAAblg5qbPcEcu7ii4kIGUIRASSaVBgIAwIAg6EnBLPixCK2oIg4dutDOjmxPj9n/+VyRhf1VYj6dqeMC2HmLYpTKcq3LorOCeE+xKezy95fvCuy2tDbz5esjuuAAB8NE4qon+/45aJ35CCJwjlqgLONMhIdUyv9ujEgT9+bQVtVsuHNvnNI1cWSpf36D0EmpaUXaoBnWWJhPLBKyPfvtAJvGUFAKDq6gm/nihI6gi/IWmgUnCVBYSKdzqFA1ucwYbKpZVP7tixq2ibAXHr9SXLs86ajeUHjh+cvXorBm11/P3RVw8c3vve7TBYu4+DqXuvJNnA6Wx/7sz8wjzfjkHLFgHA9PRndaO3d4oy8uJ24Dc1AADzem0ym8u9PDw8fNN/t2VQKBTKhtF7gTF2qCmtA0gD6IkjBaALgAYgGc9VoM01bVbA+YlMX18BwFS8JxdDJIAoDhGPPgA3nrc/5A3NzMysDu0dGmWMLpVKpWpsoMQQF4ADIIjhTjzKjisAAKYob6UN4zUA4wBWY3AQh+yU01bj48/+qev6fgCJTvfc0s3gQXA0lxtYxn8t6Ej/AlD0jC6qTBa1AAAAAElFTkSuQmCC' />";
            xxx2 = "<img src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAADcklEQVRIidWSW2gcVRzGv3NmZ2/Z7KXZjZem+GAfioIKPkhjNbRgSmP7Il6xEEGECioi4gXxJW/GYqugxSK2tBsbVGh9EQQhoFX70gaDvURJ0sbd7V66Mzv3nTMz5/jgVELa3W7TJz/4cy5zzu/j+88B/u8itwoovfblKJEjW8zlGmlWaotKs3l654VDc9cYjG7fLvUlko8eP3F8ZjVk4c1DsdSGwSddxXCU+UsnHph+m5/bMZHJ3nXn0eRQfpcPDm2+hNJfi2ioyi+eZU+l2ji868q3zn8GT4zujG7Lbly6W5Of2fHDhyev7l/a8/ntuc33/BjJpe51Gy0YC5USKytzcQ9b4v19aZGUESQkoS6US/Wzi8+O/HHg144tOvPivglusfcz0cSyDPpTNBnXE0P5pxPrB/IiIsFVdLSrqnAvqwQNQ1BKCU9FEaRjMKvNB+/77r0zq9NHVi6quvrJHX25t4TlbZDk6POyDxDVQQCV03QCxGSA0Sa+4XBmWkR4ASdBgjDbPH//deAAQFcuxr6ZuJIazB1RVAWsphGv1CT+clP4fyvUb+g0aDMaME8E4NQWvjAdm1qGSZymNn09+DUJACBQjL200P+SM98E5BiELxCAg0gCPB0DIwKMB/AIJ7bvQij2RcrF/k4GdPXGpi9e/TOXSn9fZ4Yw2w43TI0YmsFNpcVty+au8IkLHw5zT7te+zOmW8Nbzx80ek4AAKTN9rJcfMxoWMTljMskoJLkg8gCQQRmu6U/tvW3fac6QbsmAICNR1+f6c9l52rMFJpjEd0wuKHp3Gxpwlb1rx6emewJ3jEBAPTJ0Ukz4hclJ+DtwKMy5SA08GTg417hHRMAQLlePXbb0PqSymxi2XbZ1vQjfk19aNvJ/eduxqCrDu9+543xRx4/cCsMqdvHxKah3zGQ+nQgnz94cWmJrcWgY4sA4NjX03Ymm52SKN2zFvgNDQBAa6kf5QuFV0ZGRm54dk0GxWKxmslkf5Yk6bkV20kAaQD9YaUAxAFEASTCuQx0eaYr5TE2mVu3rghgKrxTCCECQBAWD0cXgBXOu//kq5qdna0Pbx5+SpJouVKpNEODSAixAJgAvBBuhqPoOQEASJHIB+lM5l0A4wDqIdgLS/TK6arx8RfOJpPJMQCxXu/c1MtgHnu5UBi8jH9b0JP+AVnsnzRjSm/xAAAAAElFTkSuQmCC' />";
                   
            for (mx.smartteam.entities.ScoredCard sc1 : sc) {
                String style = null;
                if((i%2) == 0){
                    style = "bgcolor='#A4A4A4'";
                }
               
                body += "<tr "+ style +">";
                nuevo = sc1.extra;
               
                String img;
                if(sc1.Registros < sc1.ponderacion ){   img = xxx2; no++;
                }else{ img = xxx; si++;}
                
                
                if(sc1.Registros < sc1.ponderacion ){
                  body += "<td>--</td>";
                }
                 else{
                    if(!nuevo.equals(anterior)){
                        body += "<td>"+ ii +"</td>";
                        total = ii;
                        ii++;

                    } else{
                        body += "<td>--</td>";
                    }
                }
                
                body += "<td>"+ sc1.extra+"</td>";
                body += "<td>"+ sc1.nombre+"</td>";
                body += "<td>"+ sc1.sucursal+"</td>";
                body += "<td>"+ sc1.ponderacion+"</td>";
                body += "<td>"+ sc1.Registros+"</td>";
                   
                body += "<td align='center'>" + img + "</td>";
                body += "</tr>";
                anterior = nuevo;
                i++;
            }
            String end = "</table>" + "</body></html>";
            String varla2 = "";
            if(sc.size() > 0){
                if(total>0){
                    mx.smartteam.entities.MetaCadena mc = BuscarRegistro(metaCollection, total);
                   varla2 = "<label>D&iacute;as de captura correctos : " + mc.Mensaje +" </label><br>";
                }
            }
                
            String cabecero = "<html><body>" + varla2
            +"<label><b>Capturas Totales : "+ i +"</b></label><br>" 
            +"<label><b>Capturas correctas "+ xxx +" :  "+ (total) +"</b></label><br> " 
            +"<label><b>Capturas incorrectas "+ xxx2 +" : "+ no +"</b></label> <br>" ;
            return cabecero + tabla + body + end;
        }
    }
    
     public mx.smartteam.entities.MetaCadena BuscarRegistro(mx.smartteam.entities.MetaCadenaCollection metCollection, int to){
         int varla =0;
         for(int i =0;i < metCollection.size(); i++){
             if(metCollection.get(i).DiaCaptura == to){
                varla = i;
             }
         }
        return metCollection.get(varla);
     
     }
    
    
    }
    