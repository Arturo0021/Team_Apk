package mx.triplei;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.util.ArrayList;
import mx.triplei.R;
import mx.smartteam.entities.TiendasConfigCollection;

/**
 *
 * @author ivan.guerra
 */
@SuppressLint("SetJavaScriptEnabled")
public class FichaTecnica extends Activity {
    
    LinearLayout linear_1;
    LinearLayout linear_2;
    LinearLayout linear_3;
    LinearLayout linear_4;
    
    ArrayList<mx.smartteam.entities.TiendasConfig> ConfigCollection;
    ArrayList<mx.smartteam.entities.TiendaAsistencia> Asistenciacollection;
    ArrayList<mx.smartteam.entities.TiendaSos> SosCollection;
    ArrayList<mx.smartteam.entities.TiendaFrentes> FrentesCollection;
    ArrayList<mx.smartteam.entities.TiendaExAdicional> ExAdicCollection;
    String Identificador = "";

    ListView list_gral;
    ListView list_gral2;
    ListView list_gral3;
    ListView asistencia_gral;
    
    TextView txt_titulo;
    TextView txt_titulo2;
    TextView txt_titulo3;
    TextView txt_titulo4;
    
    View view_0;
    View view_1;
    View view_2;
    View view_3;
    
    WebView web_graf;
    
    Context context;
    String y;
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	setContentView(R.layout.fichatecnica);
        context = this;
        
        
        asistencia_gral = (ListView)findViewById(R.id.asistencia_gral);
        list_gral = (ListView)findViewById(R.id.list_gral);
        list_gral2 = (ListView)findViewById(R.id.list_gra2);
        list_gral3 = (ListView)findViewById(R.id.list_gra3);
        
        txt_titulo = (TextView)findViewById(R.id.txt_titulo);
        txt_titulo2 = (TextView)findViewById(R.id.txt_titulo2);
        txt_titulo3 = (TextView)findViewById(R.id.txt_titulo3);
        txt_titulo4 = (TextView)findViewById(R.id.txt_titulo4);
        
        view_0 = (View)findViewById(R.id.view_0);
        view_1 = (View)findViewById(R.id.view_1);
        view_2 = (View)findViewById(R.id.view_2);
        view_3 = (View)findViewById(R.id.view_3);
        
        linear_1 = (LinearLayout)findViewById(R.id.linear_1);
        linear_2 = (LinearLayout)findViewById(R.id.linear_2);
        linear_3 = (LinearLayout)findViewById(R.id.linear_3);
        linear_4 = (LinearLayout)findViewById(R.id.linear_4);
        

        try {
        
            Identificador = getIntent().getStringExtra("identificador");
           
           if(Identificador.equalsIgnoreCase("general")) { // -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
               
               String reg = "Registro";
               String reg1 = "Registro";
               String reg2 = "Registro";
               String reg3 = "Registro";
               
               Asistenciacollection = (ArrayList<mx.smartteam.entities.TiendaAsistencia>)getIntent().getSerializableExtra("Asistenciacollection");
               SosCollection = (ArrayList<mx.smartteam.entities.TiendaSos>)getIntent().getSerializableExtra("SosCollection");
               FrentesCollection = (ArrayList<mx.smartteam.entities.TiendaFrentes>)getIntent().getSerializableExtra("FrentesCollection");
               ExAdicCollection = (ArrayList<mx.smartteam.entities.TiendaExAdicional>)getIntent().getSerializableExtra("ExAdicCollection");
          
               Adaptador_Asistencia adapter = new Adaptador_Asistencia(context, R.layout.adapter_asistencia, Asistenciacollection);
               asistencia_gral.setAdapter(adapter);
                if(Asistenciacollection.size() > 1) {
                   reg = "Registros";
                }
               txt_titulo.setText("GENERAL - (" + Asistenciacollection.size() + " " + reg + " )" );
               double totalsos = 0.0F;
                for(mx.smartteam.entities.TiendaSos tienda: SosCollection) {
                   totalsos += Double.parseDouble(tienda.getValor());
                }
               Adaptador_Sos sos = new Adaptador_Sos(context, R.layout.adapter_list_sos, SosCollection, totalsos);
               list_gral.setAdapter(sos);
                if(SosCollection.size() > 1) {
                   reg1 = "Registros";
                }
               txt_titulo2.setText("SOS - (" + SosCollection.size() + " " + reg1 + ")");
               
                linear_2.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {

                        AlertDialog.Builder buildWeb = new AlertDialog.Builder(context);
                                View inflater = getLayoutInflater().inflate(R.layout.inflater_webview, null);
                                WebView web_graf = (WebView)inflater.findViewById(R.id.web_graf);
                             web_graf.getSettings().setJavaScriptEnabled(true);   
                             web_graf.loadData(Char3D("SOS"), "text/html",null);   
                             buildWeb.setView(inflater);
                        AlertDialog dialog = buildWeb.create();
                        dialog.show();

                    }

                });
           
               Adapter_Frentes_Anaquel anaquel = new Adapter_Frentes_Anaquel(context, R.layout.adapter_frentes_anaquel, FrentesCollection);
               list_gral2.setAdapter(anaquel);
                if(FrentesCollection.size() > 1) {
                   reg2 = "Registros";
                }
               txt_titulo3.setText("FRENTES ANAQUEL - (" + FrentesCollection.size() + " " + reg2 + ")");
               
                    linear_3.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            
                            AlertDialog.Builder buildWeb = new AlertDialog.Builder(context);
                                    View inflater = getLayoutInflater().inflate(R.layout.inflater_webview, null);
                                    WebView web_graf = (WebView)inflater.findViewById(R.id.web_graf);
                                 web_graf.getSettings().setJavaScriptEnabled(true);   
                                 web_graf.loadData(Char3D("FRENTES"), "text/html",null);   
                                 buildWeb.setView(inflater);
                            AlertDialog dialog = buildWeb.create();
                            dialog.show();
                            
                        }
                        
                    });
               
               
               Adaptador_ExAdicional exadic = new Adaptador_ExAdicional(context, R.layout.adapter_exadicional, ExAdicCollection);
               list_gral3.setAdapter(exadic);
                if(ExAdicCollection.size() > 1) {
                   reg3 = "Registros";
                }
               txt_titulo4.setText("EXHIBICIÓN ADICIONAL - (" + ExAdicCollection.size() + " " + reg3 + ")");
           
           } else if(Identificador.equalsIgnoreCase("asistencia")) { // -*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*
               
               String numRegistro = "Registro";
               Asistenciacollection = (ArrayList<mx.smartteam.entities.TiendaAsistencia>)getIntent().getSerializableExtra("Asistenciacollection");
               Adaptador_Asistencia adapter = new Adaptador_Asistencia(context, R.layout.adapter_asistencia, Asistenciacollection);
               asistencia_gral.setAdapter(adapter);
               
               if(Asistenciacollection.size() > 1) {
                   numRegistro = "Registros";
               }
               
               txt_titulo.setText("ASISTENCIA - (" + Asistenciacollection.size() + " " + numRegistro + " )" );
               
               txt_titulo2.setVisibility(View.GONE);
               txt_titulo3.setVisibility(View.GONE);
               txt_titulo4.setVisibility(View.GONE);
               
               view_1.setVisibility(View.GONE);
               view_2.setVisibility(View.GONE);
               view_3.setVisibility(View.GONE);
               
               
               
           } else if(Identificador.equalsIgnoreCase("sos")) { // *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
               
               double totalsos = 0.0F;
               String numRegistro = "Registro";
               Asistenciacollection = (ArrayList<mx.smartteam.entities.TiendaAsistencia>)getIntent().getSerializableExtra("Asistenciacollection");
               SosCollection = (ArrayList<mx.smartteam.entities.TiendaSos>)getIntent().getSerializableExtra("SosCollection");
               Adaptador_Asistencia asistencia = new Adaptador_Asistencia(context, R.layout.adapter_asistencia, Asistenciacollection);
               asistencia_gral.setAdapter(asistencia);
               txt_titulo.setText("GENERAL");
               
                if(SosCollection.size() > 1) {
                   numRegistro = "Registros";
                }
               
               for(mx.smartteam.entities.TiendaSos tienda: SosCollection) {
                   totalsos += Double.parseDouble(tienda.getValor());
               }
               Adaptador_Sos adapter = new Adaptador_Sos(context, R.layout.adapter_list_sos, SosCollection, totalsos);
               list_gral.setAdapter(adapter);
               txt_titulo2.setText("SOS - (" + SosCollection.size() + " " + numRegistro + ")");
               
               txt_titulo2.setVisibility(View.VISIBLE);
               txt_titulo3.setVisibility(View.GONE);
               txt_titulo4.setVisibility(View.GONE);
               
               view_1.setVisibility(View.VISIBLE);
               view_2.setVisibility(View.GONE);
               view_3.setVisibility(View.GONE);
               
                    linear_2.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            
                            AlertDialog.Builder buildWeb = new AlertDialog.Builder(context);
                                    View inflater = getLayoutInflater().inflate(R.layout.inflater_webview, null);
                                    WebView web_graf = (WebView)inflater.findViewById(R.id.web_graf);
                                 web_graf.getSettings().setJavaScriptEnabled(true);   
                                 web_graf.loadData(Char3D("SOS"), "text/html",null);   
                                 buildWeb.setView(inflater);
                            AlertDialog dialog = buildWeb.create();
                            dialog.show();
                            
                        }
                        
                    });
               
           } else if(Identificador.equalsIgnoreCase("frentes")) { // *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
               
               String numRegistro = "Registro";
               Asistenciacollection = (ArrayList<mx.smartteam.entities.TiendaAsistencia>)getIntent().getSerializableExtra("Asistenciacollection");
               FrentesCollection = (ArrayList<mx.smartteam.entities.TiendaFrentes>)getIntent().getSerializableExtra("FrentesCollection");
               
               Adaptador_Asistencia asistencia = new Adaptador_Asistencia(context, R.layout.adapter_asistencia, Asistenciacollection);
               asistencia_gral.setAdapter(asistencia);
               txt_titulo.setText("GENERAL");
               
                if(FrentesCollection.size() > 1) {
                   numRegistro = "Registros";
                }
               
               Adapter_Frentes_Anaquel adapter = new Adapter_Frentes_Anaquel(context, R.layout.adapter_frentes_anaquel, FrentesCollection);
               list_gral.setAdapter(adapter);
               txt_titulo2.setText("FRENTES ANAQUEL - (" + FrentesCollection.size() + " " + numRegistro + ")");
               
               txt_titulo2.setVisibility(View.VISIBLE);
               txt_titulo3.setVisibility(View.GONE);
               txt_titulo4.setVisibility(View.GONE);
               
               view_1.setVisibility(View.VISIBLE);
               view_2.setVisibility(View.GONE);
               view_3.setVisibility(View.GONE);
               
                    linear_2.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            
                            AlertDialog.Builder buildWeb = new AlertDialog.Builder(context);
                                    View inflater = getLayoutInflater().inflate(R.layout.inflater_webview, null);
                                    WebView web_graf = (WebView)inflater.findViewById(R.id.web_graf);
                                 web_graf.getSettings().setJavaScriptEnabled(true);   
                                 web_graf.loadData(Char3D("FRENTES"), "text/html",null);   
                                 buildWeb.setView(inflater);
                            AlertDialog dialog = buildWeb.create();
                            dialog.show();
                            
                        }
                        
                    });
           
           } else if(Identificador.equalsIgnoreCase("exadic")) { // *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
              
               String numRegistro = "Registro";
               Asistenciacollection = (ArrayList<mx.smartteam.entities.TiendaAsistencia>)getIntent().getSerializableExtra("Asistenciacollection");
               ExAdicCollection = (ArrayList<mx.smartteam.entities.TiendaExAdicional>)getIntent().getSerializableExtra("ExAdicCollection");
               Adaptador_Asistencia asistencia = new Adaptador_Asistencia(context, R.layout.adapter_asistencia, Asistenciacollection);
               asistencia_gral.setAdapter(asistencia);
               txt_titulo.setText("GENERAL");
               
                if(ExAdicCollection.size() > 1) {
                   numRegistro = "Registros";
                }
               
               Adaptador_ExAdicional adapter = new Adaptador_ExAdicional(context, R.layout.adapter_exadicional, ExAdicCollection);
               list_gral.setAdapter(adapter);
               txt_titulo2.setText("EXHIBICIÓN ADICIONAL - (" + ExAdicCollection.size() + " " + numRegistro + ")");
               
               txt_titulo2.setVisibility(View.VISIBLE);
               txt_titulo3.setVisibility(View.GONE);
               txt_titulo4.setVisibility(View.GONE);
               
               view_1.setVisibility(View.VISIBLE);
               view_2.setVisibility(View.GONE);
               view_3.setVisibility(View.GONE);
               
                    /*linear_2.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            Toast.makeText(context, Identificador, Toast.LENGTH_LONG).show();
                        }
                        
                    });*/
           
           }
             
        } catch (Exception e) {
            e.getMessage();
        }
        
    }
    
        public String Char3D(String modulo){
            
            y = "<html> "
                + " <head> "
                    + " <script type='text/javascript' src='https://www.google.com/jsapi'></script> "
                    + " <script type='text/javascript'> "
                    + " google.load('visualization', '1', {packages:['corechart']}); "
                    + " google.setOnLoadCallback(drawChart); "
                        + " function drawChart() { "
                            + " var data = google.visualization.arrayToDataTable([ "
                            + " ['Items','Value'], ";

                                if(modulo.equalsIgnoreCase("SOS")) {
                                    for(int i = 0; i< SosCollection.size(); i++) {

                                        if(i != SosCollection.size() -1) {
                                            y += "['" + SosCollection.get(i).getMarca() + "', " + SosCollection.get(i).getValor() + "], ";  
                                        } else {
                                            y += "['" + SosCollection.get(i).getMarca() + "', " + SosCollection.get(i).getValor() + "]";
                                        }
                                        
                                    }
                                } else if(modulo.equalsIgnoreCase("FRENTES")) {
                                    
                                        for(int i = 0; i< FrentesCollection.size(); i++) {
                                            if(i != FrentesCollection.size() -1) {
                                                y += "['CANTIDAD EN ANAQUEL', " + FrentesCollection.get(i).getCantAnaquel() + "], ";
                                                y += "['MANOS', " + FrentesCollection.get(i).getManos() + "],";
                                                y += "['OJO', "   + FrentesCollection.get(i).getOjo() + "],";
                                                y += "['SUELO', " + FrentesCollection.get(i).getSuelo() + "],";
                                                y += "['TECHO', " + FrentesCollection.get(i).getTecho() + "],";
                                            } else {
                                                y += "['CANTIDAD EN ANAQUEL', " + FrentesCollection.get(i).getCantAnaquel() + "], ";
                                                y += "['MANOS', " + FrentesCollection.get(i).getManos() + "],";
                                                y += "['OJO', "   + FrentesCollection.get(i).getOjo() + "],";
                                                y += "['SUELO', " + FrentesCollection.get(i).getSuelo() + "],";
                                                y += "['TECHO', " + FrentesCollection.get(i).getTecho() + "]";
                                            }                               
                                        }
                                    
                                }

                    y += " ]); "
                        + " var options = { "
                            + " title: 'GRAFICA " + modulo + "', "
                            + " is3D: true, "
                        + " }; "
                         + " var chart = new google.visualization.PieChart(document.getElementById('piechart_3d')); "
                         + " chart.draw(data, options); "
                        + " } "
                    + " </script> "
                + " </head> "
                + " <body> "
                    + " <div id='piechart_3d' style='width: 100%; height: 100%;'></div> "
               + " </body> "
             + " </html> ";
                    
            return y;
        }

}
