package mx.triplei;

import android.app.ActionBar;
import mx.smartteam.entities.Opcion;
import mx.smartteam.entities.Pregunta;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.triplei.R;
import mx.smartteam.data.RespuestaSondeo;
import mx.smartteam.entities.OpcionCollection;
import mx.smartteam.entities.Pop;
import mx.smartteam.entities.PreguntaCollection;
import mx.smartteam.entities.Producto;
import mx.smartteam.entities.Proyecto;
import mx.smartteam.entities.Usuario;
import mx.smartteam.entities.Marca;
import mx.smartteam.entities.Categoria;
import mx.smartteam.entities.Contestacion;
import mx.smartteam.entities.EnumFormulario;
import mx.smartteam.entities.Foto;
import mx.smartteam.entities.PopVisita;
import mx.smartteam.entities.ProductoCollection;
import mx.smartteam.entities.SondeoModulos;

public class SondeoActivity extends Activity {

    private LinearLayout LayoutSondeo = null;
    private SondeoModulos currentSondeo;
    private Context context;
    private Usuario currentUsuario;
    private Proyecto currentProyecto;
    private Pop currentPop;
    private Producto currentProducto;
    private Marca currentMarca;
    private Categoria currentCategoria;
    private EnumFormulario currentFormulario;
    static Boolean flag = false;
    private ProductoCollection productoCollection = null;
    private mx.smartteam.entities.Marca marcaB = null;
    private Pregunta currentPregunta = null, currentPregunta2 = null, currentPregunta3 = null,currentPregunta8 = null;
    private Opcion currenOpcion = null, currenOpcion2 = null;
    Camera c = null;
    private Contestacion currentContestacion = null;
    private PopVisita popVisita;
    private TextView txtResult, txtResult2;
	private int year, month, day;
        private int hour, minute;
	static final int DATE_DIALOG_ID = 999;
        static final int TIME_DIALOG_ID = 998;
        public Integer bandera = 0, nclave;
        private Integer spCategoria,spMarca;
        private Integer intopcion = 0;
        

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sondeo);
        this.context = this;
         setCurrentDateOnView();
        Bundle bundle = getIntent().getExtras();
        this.currentUsuario = getIntent().getSerializableExtra("usuario") == null ? null : (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
        this.currentProyecto = getIntent().getSerializableExtra("proyecto") == null ? null : (mx.smartteam.entities.Proyecto) getIntent().getSerializableExtra("proyecto");
        this.currentPop = getIntent().getSerializableExtra("pop") == null ? null : (mx.smartteam.entities.Pop) getIntent().getSerializableExtra("pop");
        this.currentSondeo = getIntent().getSerializableExtra("sondeo") == null ? null : (mx.smartteam.entities.SondeoModulos) getIntent().getSerializableExtra("sondeo");
        this.currentProducto = getIntent().getSerializableExtra("producto") == null ? null : (mx.smartteam.entities.Producto) getIntent().getSerializableExtra("producto");
        this.currentCategoria = getIntent().getSerializableExtra("categoria") == null ? null : (mx.smartteam.entities.Categoria) getIntent().getSerializableExtra("categoria");
        this.currentMarca = getIntent().getSerializableExtra("marca") == null ? null : (mx.smartteam.entities.Marca) getIntent().getSerializableExtra("marca");
        this.currentPregunta = getIntent().getSerializableExtra("pregunta") == null ? null : (mx.smartteam.entities.Pregunta) getIntent().getSerializableExtra("pregunta");
        this.currenOpcion = getIntent().getSerializableExtra("opcion") == null ? null : (mx.smartteam.entities.Opcion) getIntent().getSerializableExtra("opcion");
        this.currentPregunta3 = getIntent().getSerializableExtra("pregunta3") == null ? null : (mx.smartteam.entities.Pregunta) getIntent().getSerializableExtra("pregunta3");
        this.currentPregunta2 = getIntent().getSerializableExtra("pregunta2") == null ? null : (mx.smartteam.entities.Pregunta) getIntent().getSerializableExtra("pregunta2");
        this.currenOpcion2 = getIntent().getSerializableExtra("opcion2") == null ? null : (mx.smartteam.entities.Opcion) getIntent().getSerializableExtra("opcion2");
        this.currentPregunta8 = getIntent().getSerializableExtra("preguntan8") == null ? null :(mx.smartteam.entities.Pregunta) getIntent().getSerializableExtra("preguntan8");
        
        this.currentFormulario = (EnumFormulario) bundle.get("item");
        spCategoria = bundle.get("spCategoria") == null ? 0 : (Integer) bundle.get("spCategoria");
        spMarca = bundle.get("spMarca") == null ? 0 : (Integer) bundle.get("spMarca"); 
        this.popVisita = mx.smartteam.business.PopVisita.GetById(context, currentPop.IdVisita);
        if(currentSondeo != null){
            if(currentPregunta3!= null){
            setTitle(currentPregunta3.Respuesta.toString());       
            }
            else{
                setTitle(currentSondeo.Nombre);
            }
        }
        
        // asignar la opcion de la pregunta 2 a la opcion de la pregunta1
        if(currentPregunta != null && currenOpcion != null){
            currenOpcion.Selected = true;
            currentPregunta.Opciones = null; OpcionCollection noc = new OpcionCollection();
            noc.add(currenOpcion);
            currentPregunta.Opciones = noc;
        }
        else
        {
            currentPregunta = null;
        }
       
        if(currentPregunta2 != null && currenOpcion2 != null)
        {int go=0;
            currenOpcion2.nOpcion = currentPregunta.Opciones.get(0).Id;
            currenOpcion2.Selected = true;
            currentPregunta2.Opciones = null; 
            OpcionCollection noc2 = new OpcionCollection();
            noc2.add(currenOpcion2);
            currentPregunta2.Opciones = noc2;
           
            currentPregunta2.clave=null;
            
            go = mx.smartteam.data.Pregunta.GetPregunta1(context, currentPregunta2, popVisita, currentSondeo);
           
            currentPregunta.ContestacionPreguntaId = go;
        }
        else
        {
            currentPregunta2 = null;
        }
        
        productoCollection = new ProductoCollection();
        
        
        ArrayList<mx.smartteam.entities.Producto>
        ProductosPorMarca =  getIntent().getSerializableExtra("productoCollection") == null ? null : (ArrayList<mx.smartteam.entities.Producto>) getIntent().getSerializableExtra("productoCollection");
        
        if(ProductosPorMarca != null ){
           if(ProductosPorMarca.size() > 0){
                for(int i = 0; i < ProductosPorMarca.size(); i++) {
                productoCollection.add(ProductosPorMarca.get(i));
                }
                if (productoCollection.size() > 0) {
                    this.currentProducto = productoCollection.get(0);
                }
            }
        }
                        
        if (this.currentProducto != null){// VALIDO SOLO POR DONDE EL SONDEO ES TIPO PRODUCTO
            setTitle(currentProducto.Nombre);
           
            this.currentContestacion = mx.smartteam.data.Contestacion.ContestacionGetByVisitaBySondeoBySku(context, popVisita, currentSondeo, this.currentProducto);
        }else{
            if(currentPregunta != null){

                if(currentPregunta2 != null){
                    // this.currentContestacion = mx.smartteam.data.Contestacion.ContestacionGetByVisitaBySondeo(context, popVisita, currentSondeo);
                    Integer  s = currentPregunta2.Opciones.get(0).Constestacion;
                    currentContestacion = new Contestacion(); // DECLARACION D
                    currentContestacion.Id = s;
                    currentContestacion.IdSondeo = currentSondeo.Id;

                }else{

                    this.currentContestacion = mx.smartteam.data.Contestacion.ContestacionGetByVisitaBySondeo(context, popVisita, currentSondeo);

                    if(currentPregunta.Opciones.get(0).Constestacion > 0){
                       currentContestacion.Id = currentPregunta.Opciones.get(0).Constestacion;
                   }else{
                   this.currentContestacion = null;
                   }
                }

            } else   if(currentPregunta3 != null)
                    {
                       String respuesta=currentPregunta3.Respuesta.toString();
                       int []  idcont= new int[2]  ;
                         idcont= mx.smartteam.data.Contestacion.ContestacionId(context,respuesta,popVisita);
                        
                        if(idcont[0] == 0){
                            
                           currentContestacion=null;
                        }else{
                            
                            this.currentContestacion = mx.smartteam.data.Contestacion.ContestacionGetByVisitaBySondeo(context, popVisita, currentSondeo);     
                            currentContestacion.Id=idcont[1];
                        }   
                    }
            
            else{
                //setTitle(currentSondeo.Nombre);
                //TENEMOS QUE VALIDAR PARA TODOS LOS QUE NO SON DE TIPO PRODUCTO
                
                //debemos que indicarle que venimos de marca
                if(currentMarca!= null ){
                    this.currentContestacion = mx.smartteam.data.Contestacion.ContestacionGetByVisitaBySondeoByMarca(context, popVisita, currentSondeo,currentMarca);
                }else if(currentPregunta8 != null){
                    currentContestacion = null;
                } else{
                    this.currentContestacion = mx.smartteam.data.Contestacion.ContestacionGetByVisitaBySondeo(context, popVisita, currentSondeo);
                }
            }
        }
        if (this.currentUsuario != null && this.currentProyecto != null && this.currentPop != null) {
            CreaLayout();
        }
    }
    
    public void setCurrentDateOnView() {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        //setTitle(new StringBuilder().append(month + 1).append("-").append(day).append("-").append(year).append(" "));
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_DIALOG_ID:
                return new DatePickerDialog(this, datePickerListener, year, month, day);
        case TIME_DIALOG_ID:
            return new TimePickerDialog(this, timePickerListener, hour, minute,true);
            
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                year = selectedYear; month = selectedMonth; day = selectedDay;
                txtResult.setText(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day).append(" "));

        }
    };
    
    private TimePickerDialog.OnTimeSetListener timePickerListener =  new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                hour = selectedHour; minute = selectedMinute;
                txtResult2.setText(new StringBuilder().append(padding_str(hour)).append(":").append(padding_str(minute)));
        }
	};
 
    private static String padding_str(int c) {
            if (c >= 10)
               return String.valueOf(c);
            else
               return "0" + String.valueOf(c);
    }
    

    private void OtroProducto() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle( Html.fromHtml("<font color='#FFFFF'>"+R.string.title_dialog+"</font>"));
        //alertDialogBuilder.setTitle(R.string.title_dialog);
        Sucursal sucursal =new Sucursal();
        Drawable d=sucursal.setIconAlert(context);
        alertDialogBuilder.setIcon(d);
        alertDialogBuilder.setMessage("¿Desea capturar otra marca o producto?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent i = new Intent("mx.triplei.bymarca");
                        i.putExtra("item", currentFormulario);
                        i.putExtra("sondeo", currentSondeo);
                        i.putExtra("usuario", currentUsuario);
                        i.putExtra("proyecto", currentProyecto);
                        i.putExtra("pop", currentPop);
                        startActivity(i);

                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                //Intent i = new Intent("mx.smartteam.menu");
                        //startActivity(i);
                        finish();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    
    protected void onLoadData()
    {
        if (this.currentProducto != null){
            setTitle(currentProducto.Nombre);
           
           
            this.currentContestacion = mx.smartteam.data.Contestacion.ContestacionGetByVisitaBySondeoBySku(context, popVisita, currentSondeo, this.currentProducto);
        } else {
            //setTitle(currentSondeo.Nombre);
            this.currentContestacion = mx.smartteam.data.Contestacion.ContestacionGetByVisitaBySondeo(context, popVisita, currentSondeo);
        }
        
        if (this.currentUsuario != null && this.currentProyecto != null && this.currentPop != null) {
            CreaLayout();
        }

    }
    
    private void Siguiente(){
          Guardar();
          productoCollection.remove(currentProducto);
          if (productoCollection.size() > 0) {
            if (productoCollection.size() == 1) {
                currentProducto = productoCollection.get(0);

            } else {
                currentProducto = productoCollection.get(productoCollection.size() - 1);
                onLoadData();

            }
            onLoadData();

        } else {
            //goBack();
            this.finish();
        }
    }
    //@Arturo desde aqui weon
    private void CreaLayout() {
        try {
            this.LayoutSondeo = null;
            this.LayoutSondeo = (LinearLayout) findViewById(R.id.LayoutSondeo);

            mx.smartteam.entities.PreguntaCollection preguntaCollection = null;
            if (currentContestacion != null) {
                     if(currentPregunta3!=null){
                      String respuesta=currentPregunta3.Respuesta.toString();
                       int []  idcont= new int[2]  ;
                         idcont= mx.smartteam.data.Contestacion.ContestacionId(context,respuesta,popVisita);
                        
                        if(idcont[0]==0){
                            
                                 preguntaCollection = mx.smartteam.business.Pregunta.GetPreguntaByContestacion(context, currentContestacion);//data.Pregunta.GetPreguntaBySondeo(context, currentSondeo);
                        }else{
                                 preguntaCollection = mx.smartteam.business.Pregunta.GetPreguntaByContestacion(context, currentContestacion);//data.Pregunta.GetPreguntaBySondeo(context, currentSondeo);
                        } 
               
                }else{
                        preguntaCollection = mx.smartteam.business.Pregunta.GetPreguntaByContestacion(context, currentContestacion);//data.Pregunta.GetPreguntaBySondeo(context, currentSondeo);
                     }
            } else {
                preguntaCollection = mx.smartteam.business.Pregunta.GetPreguntaBySondeo(context, currentSondeo);//data.Pregunta.GetPreguntaBySondeo(context, currentSondeo);
            }

            for (Pregunta pregunta : preguntaCollection) {
                Pregunta.Type tipo = mx.smartteam.entities.Pregunta.Type.valueOf(pregunta.Tipo.toLowerCase());

                switch (tipo) {
                    case abierta:
                        this.LayoutSondeo.addView(PreguntaAbierta(pregunta));
                        break;
                        
                    case decimal:
                        this.LayoutSondeo.addView(PreguntaDecimal(pregunta));
                        break;
                        
                    case numerico:
                        this.LayoutSondeo.addView(PreguntaNumerica(pregunta));
                        break;
                        
                    case multiple:
                        this.LayoutSondeo.addView(PreguntaMultiSelect(pregunta));
                        break;
                        
                    case unicaradio:
                        this.LayoutSondeo.addView(PreguntaSingleSelect(pregunta));
                        break;
                        
                    case sino:
                        this.LayoutSondeo.addView(PreguntaSiNo(pregunta));
                        break;
                        
                    case informativo:
                        this.LayoutSondeo.addView(PreguntaInformativo(pregunta));
                        break;
                        
                    case fecha:
                        bandera++;
                        this.LayoutSondeo.addView(PreguntaFecha(pregunta));
                        break;
                        
                    case hora:
                        this.LayoutSondeo.addView(PreguntaHora(pregunta)); 
                        break;
                        
                    case combo:
                        this.LayoutSondeo.addView(PreguntaComboSelect(pregunta));   // PreguntaComboSelect
                        break;
                        
                }

            }
        } catch (Exception ex) {
            Logger.getLogger(SondeoActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //android.os.Process.killProcess(android.os.Process.myPid());
        Log.d("Evento", "onDestroy");

    }
    
    public View PreguntaFecha(Pregunta pregunta) {
         
        LinearLayout layout = new LinearLayout(getBaseContext());
        LinearLayout layout2 = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setTag(pregunta);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setId(pregunta.Id);
        TextView text = new TextView(this);
        text.setTextColor(Color.BLACK);

        text.setText(pregunta.Nombre);
        
        txtResult = new TextView(this);
        txtResult.setTextSize(20);
        txtResult.setTextColor(Color.BLACK);
        txtResult.setText(pregunta.Respuesta != null ? pregunta.Respuesta.toString() : "");
        //txtResult.setText("hola");
        ImageButton myButton = new ImageButton(this);
        myButton.setLayoutParams(new LinearLayout.LayoutParams(120, 100));
        myButton.setImageResource(R.drawable.calendar);
        myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(DATE_DIALOG_ID);
                }
        });
        
        layout.addView(text);
        layout2.addView(myButton);
        layout2.addView(txtResult);
        layout.addView(layout2);
	return layout;
     }

    public View PreguntaHora(Pregunta pregunta){
         LinearLayout layout = new LinearLayout(getBaseContext());
         LinearLayout layout2 = new LinearLayout(this);
         layout.setOrientation(LinearLayout.HORIZONTAL);
         layout.setTag(pregunta);
         layout.setOrientation(LinearLayout.VERTICAL);
         layout.setId(pregunta.Id);
         TextView text = new TextView(this);
         text.setTextColor(Color.BLACK);
         text.setText(pregunta.Nombre);
         
         txtResult2 = new TextView(this);txtResult2.setTextSize(20);
         txtResult2.setTextColor(Color.BLACK);
         txtResult2.setText(pregunta.Respuesta!= null ? pregunta.Respuesta.toString() : "");
         ImageButton myButton2 = new ImageButton(this);
         myButton2.setImageResource(R.drawable.clock);
         myButton2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT));
         
         myButton2.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View view) {
                 showDialog(TIME_DIALOG_ID);
             }
         });
         layout.addView(text);
         layout2.addView(myButton2);
         layout2.addView(txtResult2);
         layout.addView(layout2);
         
         return layout;
     }
     
    private View PreguntaAbierta(Pregunta pregunta) {
        LinearLayout layout = new LinearLayout(getBaseContext());
        layout.setTag(pregunta);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setId(pregunta.Id);        
        TextView text = new TextView(this);
        text.setTextColor(Color.BLACK);
        text.setText(pregunta.Nombre);
                
        EditText edit = new EditText(this);
        edit.setTextColor(Color.BLACK);
        edit.setText(pregunta.Respuesta != null ? pregunta.Respuesta.toString() : "");
        if (pregunta.Longitud > 0) {
            edit.setTextSize(pregunta.Longitud);
        }

        layout.addView(text);
        layout.addView(edit);
        return layout;
    }

    private View PreguntaInformativo(Pregunta pregunta) {
        LinearLayout layout = new LinearLayout(getBaseContext());
        layout.setTag(pregunta);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setId(pregunta.Id);
        TextView text = new TextView(this);
        text.setTextColor(Color.BLACK);
        text.setText(pregunta.Nombre);
        
        //EditText edit = new EditText(this);
        layout.addView(text);
        //layout.addView(edit);
        return layout;
    }

    private View PreguntaNumerica(Pregunta pregunta) {
        LinearLayout layout = new LinearLayout(getBaseContext());
        layout.setTag(pregunta);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setId(pregunta.Id);
        TextView text = new TextView(this);
        text.setTextColor(Color.BLACK);
        text.setText(pregunta.Nombre);
        String existenciaAnt = "";
        String nombre = "VICK SINEX 15 ML";
        EditText edit = new EditText(this);
        edit.setTextColor(Color.BLACK);
        edit.setText(pregunta.Respuesta != null ? pregunta.Respuesta.toString() : "");
        edit.setInputType(InputType.TYPE_CLASS_NUMBER);

        layout.addView(text);

        layout.addView(edit);
        return layout;
    }

    private View PreguntaDecimal(Pregunta pregunta) {
        LinearLayout layout = new LinearLayout(getBaseContext());
        layout.setTag(pregunta);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setId(pregunta.Id);
        TextView text = new TextView(this);
        text.setTextColor(Color.BLACK);
        text.setText(pregunta.Nombre);

        EditText edit = new EditText(this);
        edit.setTextColor(Color.BLACK);
        edit.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        edit.setText(pregunta.Respuesta != null ? pregunta.Respuesta.toString() : "");
        layout.addView(text);
        layout.addView(edit);
        return layout;
    }

    private View PreguntaMultiSelect(Pregunta pregunta) {

        LinearLayout layout = new LinearLayout(this);
        layout.setTag(pregunta);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setId(pregunta.Id);

        TextView text = new TextView(this);
        text.setText(pregunta.Nombre);
        text.setTextColor(Color.BLACK);

        ListView list = new ListView(this);

        //TODO:Modificaciones LocalHost
        Opcion.AdapterMultiple opcion = new Opcion.AdapterMultiple(this, pregunta.Opciones);

        list.setAdapter(opcion);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        
        layout.addView(text);
        layout.addView(list);
    
        list.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 80 * pregunta.Opciones
                .size(), 2));

        return layout;
    }

    private View PreguntaComboSelect(final Pregunta pregunta) {
        OpcionCollection items = new OpcionCollection(); 
        items = pregunta.Opciones;
        ProductoCollection pp = new ProductoCollection();
        
        
        LinearLayout layout = new LinearLayout(this);
        layout.setTag(pregunta);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setId(pregunta.Id);
        
        TextView text = new TextView(this);
        text.setTextColor(Color.BLACK);
        text.setText(pregunta.Nombre);
        int position=0;
        for(int i = 0; i< items.size(); i++)
        {
            Producto p = new Producto();
            if(currentPregunta3!= null)
            {
                position = 0;
            }
            else
            {
                
                    if(items.get(i).Selected.equals(true)){
                        position = i;
                    }
                }
            
            p.Id = items.get(i).Id;
            p.Nombre = items.get(i).Nombre;
            pp.add(p);
        }
        
        productoCollection = pp;
        final Spinner spinnerx = new Spinner(this);
        spinnerx.setAdapter(new Producto.Adapter(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, pp));
        
        spinnerx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
            Object itex = adapter.getItemAtPosition(i);
            Producto pro = new Producto();
            pro = (mx.smartteam.entities.Producto) itex;
            // pregunta.Opciones.get(i).Selected = true;
            
            pregunta.Opciones = ValidarAllItem(pregunta.Opciones, pro.Id);
            //Toast.makeText(getApplicationContext(), "Id Item Selected" + pro.Id   , Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView arg0) {
            Toast.makeText(getApplicationContext(), "Nothing selected", Toast.LENGTH_SHORT).show();
        }
    }); 
        
        if(position > 0){
            spinnerx.setSelection(position);
        }
        
        layout.addView(text);
        layout.addView(spinnerx);

        return layout;
    }
    
    public OpcionCollection ValidarAllItem(OpcionCollection opcionCollection, int Id){
    //Procesamos info
        if (opcionCollection.size() > 0) {
            for(int i =0 ;i < opcionCollection.size(); i++ )
            {
                if(opcionCollection.get(i).Id.equals(Id) ){
                    opcionCollection.get(i).Selected = true;
                }
                else{
                opcionCollection.get(i).Selected = false;
                }
            
            }    
        }
        
    return opcionCollection;
    }
    
    
    private View PreguntaSiNo(Pregunta pregunta) {

        LinearLayout layout = new LinearLayout(this);
        layout.setTag(pregunta);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setId(pregunta.Id);

        TextView text = new TextView(this);
        text.setTextColor(Color.BLACK);
        text.setText(pregunta.Nombre);

        //---------------------------------------------------------------------------------------------------------------
        //ListView list = new ListView(this);
        mx.smartteam.entities.OpcionCollection opciones = new OpcionCollection();
        Opcion opcSi = new Opcion();
        opcSi.Id = 1;
        opcSi.Nombre = "Si";

        Opcion opcNo = new Opcion();
        opcNo.Id = 0;
        opcNo.Nombre = "No";

        opciones.add(opcSi);
        opciones.add(opcNo);

        pregunta.Opciones = opciones;

        RadioGroup group = new RadioGroup(this);

        RadioButton radS = new RadioButton(this);
        radS.setId(1);
        radS.setTextColor(Color.BLACK);
        radS.setText("Si");

        
        group.addView(radS);

        RadioButton radN = new RadioButton(this);
        radN.setId(0);
        radN.setTextColor(Color.BLACK);
        radN.setText("No");
        if(pregunta.Respuesta != null){
            if(pregunta.Respuesta.equals("Si")){
                radS.setChecked(true);
            }
            if(pregunta.Respuesta.equals("No")){
                radN.setChecked(true);
            }
        }
        group.addView(radN);

        layout.addView(text);
        layout.addView(group, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        return layout;
    }
    /**
     * *****************************************************************************
     */
    int result = 0;

    public void ByMArca() {
        for (int x = 1; x < 3; x++) {
            result = x;

            if (result == 0) {
                exit(0);
            } else {
                // Intent i = new Intent(this, AcercaDe.class );
                //  startActivity(i);
                finish();
            }

        }
    }

    /**
     * *****************************************************************************
     */
    private View PreguntaSingleSelect(Pregunta pregunta) {

        LinearLayout layout = new LinearLayout(this);
        layout.setTag(pregunta);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setId(pregunta.Id);

        TextView text = new TextView(this);
        text.setTextColor(Color.BLACK);

        text.setText(pregunta.Nombre);

        RadioGroup group = new RadioGroup(this);
        int count = 0;
        for (Opcion opcion : pregunta.Opciones) {
            RadioButton rad = new RadioButton(this);
            rad.setId(opcion.Id);
            rad.setText(opcion.Nombre);
            rad.setTextColor(Color.BLACK);

            if (opcion.Selected) {
                rad.setChecked(true);
            }
        
            if(currentContestacion == null){
                if(count == 0){
                    rad.setChecked(true);
                }
            }

            group.addView(rad);
            count++;
        }


        layout.addView(text);
        layout.addView(group, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        return layout;
    }

    private Pregunta ObtieneResultado(LinearLayout view, Pregunta pregunta) {

        Pregunta.Type tipo = mx.smartteam.entities.Pregunta.Type.valueOf(pregunta.Tipo.toLowerCase());

        switch (tipo) {
            case abierta:
                pregunta = ResultadoPreguntaAbierta(view, pregunta);
            break;
            
            case decimal:
                pregunta = ResultadoPreguntaAbierta(view, pregunta);
            break;
            
            case numerico:
                pregunta = ResultadoPreguntaAbierta(view, pregunta);
            break;
            
            case multiple:
                pregunta = ResultadoPreguntaMultiSelect(view, pregunta);
            break;
            
            case unicaradio:
                pregunta = ResultadoPreguntaSigleSelect(view, pregunta);
            break;
            
            case sino:
                pregunta = ResultadoPreguntaSiNo(view, pregunta);
            break;
            
            case fecha:
                pregunta = ResultadoPreguntaFecha(view, pregunta);
            break;
         
            case hora:
                pregunta = ResultadoPreguntaHora(view, pregunta);
            break;
                
            case combo:
                 pregunta = ResultadoPreguntaCombo(view, pregunta);
            break;
            
        }

       
        if(currentPregunta3 != null){
            String respuesta=currentPregunta3.Respuesta.toString();
            int []  idcont= new int[2]  ;
            idcont= mx.smartteam.data.Contestacion.ContestacionId(context,respuesta,popVisita);
                    
            if(idcont[0]==0){}
            else{

                String resp = "resp";
            }
        }
        return pregunta;
    }

    private Pregunta ResultadoPreguntaAbierta(LinearLayout view, Pregunta pregunta) {
        StringBuilder result = new StringBuilder();
        Pregunta _pregunta = pregunta;

        EditText edit = (EditText) view.getChildAt(1);

        if (edit != null && !edit.getText().toString().isEmpty()) {

            _pregunta.Respuesta = edit.getText().toString();

            return _pregunta;

        } else {
            return null;
        }
    }
    
    
    private Pregunta ResultadoPreguntaFecha(LinearLayout view, Pregunta pregunta) {
        Pregunta _pregunta = pregunta;
        
        if(txtResult.getText() != null && txtResult.getText() != ""){
            _pregunta.Respuesta = txtResult.getText().toString();
        }else {
         _pregunta = null;
        }
        
        return _pregunta;
    }
    
    private Pregunta ResultadoPreguntaHora(LinearLayout view, Pregunta pregunta){
        Pregunta _pregunta = pregunta;
        
        String avar = txtResult2.getText().toString();
        if(avar != null && avar != ""){
            _pregunta.Respuesta = avar;
        }else{
            _pregunta = null;
        }
        return _pregunta;
    }
    private Pregunta ResultadoPreguntaMultiSelect(LinearLayout view, Pregunta pregunta) {

        Integer countSlected = 0;

        for (Opcion opcion : pregunta.Opciones) {
            if (opcion.Selected) {
                countSlected += 1;
            }
        }

        if (countSlected == 0) {
            return null;
        } else {
            return pregunta;
        }
    }
    
    private Pregunta ResultadoPreguntaCombo(LinearLayout view, Pregunta pregunta) {
       Spinner spinner_obj = (Spinner) view.getChildAt(1); 
       Object selectItem = spinner_obj.getSelectedItem();
        pregunta.Respuesta = null;
       return pregunta; 
    }

    private Pregunta ResultadoPreguntaSigleSelect(LinearLayout view, Pregunta pregunta) {

        StringBuilder result = new StringBuilder();

        RadioGroup radioGroup = (RadioGroup) view.getChildAt(1);

        int idChk = radioGroup.getCheckedRadioButtonId();

        RadioButton rad = (RadioButton) radioGroup.findViewById(idChk);

        if (rad != null) {
            for (Opcion opc : pregunta.Opciones) {
                if (opc.Id.equals(rad.getId())) {
                    opc.Selected = true;
                } else {
                    opc.Selected = false;
                }
            }
            return pregunta;
        } else {
            return null;
        }


        /*if (rad != null) {

         result.append(String.format("|%s|%s|%s|", pregunta.Id, rad
         .getText().toString(), rad.getId()));
         }*/
    }

    private Pregunta ResultadoPreguntaSiNo(LinearLayout view, Pregunta pregunta) {

        StringBuilder result = new StringBuilder();

        RadioGroup radioGroup = (RadioGroup) view.getChildAt(1);
        int idChk = radioGroup.getCheckedRadioButtonId();
        RadioButton radSelected = (RadioButton) radioGroup.findViewById(idChk);

        if (radSelected != null) {
            pregunta.Respuesta = radSelected.getText().toString();
            return pregunta;
        } else {
            return null;
        }
        //return pregunta;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.anquel_sondeo, menu);
        if (mx.triplei.ProductoActivity.porProducto) {
            menu.removeItem(R.id.action_siguiente);
        } else {
            menu.removeItem(R.id.action_guardar);
        }
        Sucursal sucursal = new Sucursal();
        Drawable d = sucursal.setIconMenu(context);
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(d);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_siguiente:
                Siguiente();
                return true;
            
            case R.id.action_guardar:
                Guardar();
                /*if(currentProducto != null){
                goBack();
                }*/
                break;
            case R.id.action_cancelar:
                finish();
                break;
            case R.id.action_foto:
                foto();
                break;

        }
        return true;
    }

    public void goBack(){
        Intent ii;
        ii = new Intent(SondeoActivity.this , ProductoActivity.class);
        ii.putExtra("usuario", currentUsuario);
        ii.putExtra("proyecto", currentProyecto);
        ii.putExtra("pop", currentPop);
        ii.putExtra("item", currentFormulario);
        ii.putExtra("sondeo", currentSondeo);
        ii.putExtra("spCategoria", spCategoria);
        ii.putExtra("spMarca", spMarca);
        startActivity(ii);
        finish();
    }
    
    private void foto() {
        Foto foto = new Foto();
        Intent i = new Intent(SondeoActivity.this, CameraActivity.class);
        i.putExtra("sondeo", currentSondeo);
        i.putExtra("usuario", currentUsuario);
        i.putExtra("proyecto", currentProyecto);
        i.putExtra("pop", currentPop);
        switch(currentSondeo.IdentificadorVentas){
            
            case 8:
                foto.Comentario = currentPregunta8.Respuesta.toString();
                i.putExtra("foto", foto);
                i.putExtra("opcionfoto",3);/* Sondeos */
                i.putExtra("tiposondeo",8);
            break;
            
            /*case 7:
            // Sondeos Tipo 7 se ha decidido que se realice en otro lado para que no 
            // afecte al funcionamiento de lo que aqui ya se encuentra hecho
                foto.Comentario = currentPregunta3.Respuesta.toString();
                i.putExtra("foto", foto);
                i.putExtra("opcionfoto",3); Sondeos 
                i.putExtra("tiposondeo",7);
            break;*/
            
            case 6:
                foto.Comentario = currentPregunta3.Respuesta.toString();
                i.putExtra("foto", foto);
                i.putExtra("opcionfoto",3);/* Sondeos */
                i.putExtra("tiposondeo",6);
            break;

            case 5:
                i.putExtra("p1",currentPregunta.Opciones.get(0).Id);
                i.putExtra("p2",currentPregunta2.Opciones.get(0).Id);
                i.putExtra("foto", foto);
                i.putExtra("opcionfoto",3);/* Sondeos */
                i.putExtra("tiposondeo",5);
                
            break;
                
                
            case 4:
                i.putExtra("opcionfoto",3);/* Sondeos */
                i.putExtra("tiposondeo",4);
            break;
            
            case 3:
                i.putExtra("opcionfoto",3);/* Sondeos */
                i.putExtra("opcion",currenOpcion);
                i.putExtra("tiposondeo",3);
            break; 

            case 2:
                i.putExtra("opcionfoto",3);/* Sondeos */
                i.putExtra("tiposondeo",2);
                if(currentMarca!= null){
                i.putExtra("marca",currentMarca);    
                }
                
            break; 
                
            case 1:
                i.putExtra("producto", currentProducto);
                i.putExtra("opcionfoto",3);/* Sondeos */
                i.putExtra("tiposondeo",1);
            break;
                
            case 0:
                i.putExtra("opcionfoto",3);/* Sondeos */
                i.putExtra("tiposondeo",0);
            break;
        }
        
        startActivity(i);
        
        super.onStop();

    }

    private void Guardar() {
        
        //if(currentSondeo.getIdentificadorVentas() == 3) {
            // algo
       // } else {
            
        mx.smartteam.entities.PreguntaCollection preguntaCollection = new PreguntaCollection();
        for (int i = 0; i < LayoutSondeo.getChildCount(); i++) {
            LinearLayout view = (LinearLayout) LayoutSondeo.getChildAt(i);
            if (view != null) {
                Pregunta objPregunta = (Pregunta) view.getTag();
                if (objPregunta != null) {
                    if (!objPregunta.Tipo.equals("informativo")) {
                        objPregunta = ObtieneResultado(view, objPregunta);

                        if (objPregunta != null) {
                            preguntaCollection.add(objPregunta);
                        }

                    }
                }
            }
        }
   // }
        if(preguntaCollection.size() > 0)
        {
            if(currentPregunta != null)
            {
                currentPregunta.bandera = 2;
                preguntaCollection.add(currentPregunta);
            }
            
            if(currentPregunta2 != null)
            {
                Integer nclave = 2;
                currentPregunta2.bandera = nclave;
                preguntaCollection.add(currentPregunta2);
            }
             if(currentPregunta3 != null)
            {                     
                      preguntaCollection.add(currentPregunta3);
            }
              if(currentPregunta8 != null)
            {                     
                      preguntaCollection.add(currentPregunta8);
            }
             
             
        new Save().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, preguntaCollection);
        }
        
    }

    class Save extends AsyncTask<mx.smartteam.entities.PreguntaCollection, Void, String> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(context);
            pd.setTitle("Registrando sondeo");
            pd.setMessage("Espere por favor.");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

        @Override
        protected String doInBackground(mx.smartteam.entities.PreguntaCollection... params) {
            long fechac = System.currentTimeMillis() / 1000;
            int newopcion = 0;
            int idopcion =0;
            String result="";
              int idfoto = PhotoActivity.idfoto;
            if(currentPregunta2 != null)
            {
                newopcion = currentPregunta.Opciones.get(0).Id; //Control para las palomitas
                idopcion = currentPregunta2.Opciones.get(0).Id;// Segunda Pregunta enviar al server
                result = RespuestaSondeo.Insertarx(context, currentUsuario, currentPop, currentSondeo, params[0], fechac, currentProducto, currentMarca, currentCategoria, idfoto, currentContestacion == null ? 0 : currentContestacion.Id ,  newopcion, idopcion );                
            }
            else
            {
                  if(currentPregunta3 != null)
                   {
                       String respuesta = currentPregunta3.Respuesta.toString();
                       int []  idcont= new int[2]  ;
                         idcont= mx.smartteam.data.Contestacion.ContestacionId(context,respuesta,popVisita);
                        if(idcont[0]==0){  
                            result = RespuestaSondeo.Insertar(context, currentUsuario, currentPop, currentSondeo, params[0], fechac, currentProducto, currentMarca, currentCategoria, idfoto, idcont[ 1] );           
                        }else{
                            currentPregunta3.ContestacionPreguntaId=idcont[0];
                            result = RespuestaSondeo.Insertar(context, currentUsuario, currentPop, currentSondeo, params[0], fechac, currentProducto, currentMarca, currentCategoria, idfoto, idcont[ 1] );           
                        }
                    }
                    else
                    {
                        if(currentMarca == null){ // Aqui Entran las respuestas para insertar una vez recuperadas
                            result = RespuestaSondeo.Insertar(context, currentUsuario, currentPop, currentSondeo, params[0], fechac, currentProducto, currentMarca, currentCategoria, idfoto, currentContestacion == null ? 0 : currentContestacion.Id );       
                        }else{
                            result = RespuestaSondeo.InsertarMarca(context, currentUsuario, currentPop, currentSondeo, params[0], fechac, currentProducto, currentMarca, currentCategoria, idfoto, currentContestacion == null ? 0 : currentContestacion.Id );
                        }
                    }
                
                
            }
            PhotoActivity.idfoto = 0;
            return result;
        }

        protected void onPostExecute(String resultado) {
            pd.dismiss();
            
            if (resultado.equals("OK")) {
                Toast.makeText(context, "Sondeo registrado", Toast.LENGTH_LONG).show();
                
                if(currentProducto == null){
                    finish();
                }else{
                goBack();
                }
                
                
            } else {
                Toast.makeText(context, "Error al registrar el sondeo", Toast.LENGTH_LONG).show();
            }

        }
    }

}
