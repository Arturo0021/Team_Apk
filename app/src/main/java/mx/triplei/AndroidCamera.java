package mx.triplei;

import android.app.ActionBar;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.triplei.R;
import static mx.triplei.PhotoActivity.opcionfoto;
import static mx.triplei.SondeoActivity.DATE_DIALOG_ID;
import mx.smartteam.data.RespuestaSondeo;
import mx.smartteam.entities.EnumFormulario;
import mx.smartteam.entities.Foto;
import mx.smartteam.entities.Opcion;
import mx.smartteam.entities.OpcionCollection;
import mx.smartteam.entities.Pop;
import mx.smartteam.entities.PopVisita;
import mx.smartteam.entities.Pregunta;
import mx.smartteam.entities.PreguntaCollection;
import mx.smartteam.entities.Producto;
import mx.smartteam.entities.ProductoCollection;
import mx.smartteam.entities.Proyecto;
import mx.smartteam.entities.SondeoModulos;
import mx.smartteam.entities.Usuario;

public class AndroidCamera extends Activity {
    private Camera mCamera;
    private CameraPreview mPreview;
    private PictureCallback mPicture;
    private LinearLayout cameraPreview;
    private boolean cameraFront = false;
    
    private Context context;
    private EnumFormulario currentFormulario;
    private Usuario currentUsuario;
    private Proyecto currentProyecto;
    private Pop currentPop;
    private Pregunta currentPregunta3 = null;
    private SondeoModulos currentSondeo;
    
    private PopVisita popVisita;
    private ProductoCollection productoCollection = null;
    private LinearLayout LayoutSondeo = null;
    private TextView txtResult, txtResult2;
    public Integer bandera = 0;
    private int year, month, day;
    private int hour, minute;
    static final int DATE_DIALOG_ID = 999;
    static final int TIME_DIALOG_ID = 998;
    private String ruta = null; 
    private File pictureFile = null;
    private byte[] imageBytes;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sondeo2);
        Bundle bundle = getIntent().getExtras();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        context = this;
        initialize();
            /* obtemos los objetos del activity anterior*/
        this.currentFormulario = bundle.get("item") == null ? null : (EnumFormulario)bundle.get("item");
        this.currentUsuario = getIntent().getSerializableExtra("usuario") == null ? null :(mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
        this.currentProyecto = getIntent().getSerializableExtra("proyecto") == null ? null :(mx.smartteam.entities.Proyecto) getIntent().getSerializableExtra("proyecto");
        this.currentPop = getIntent().getSerializableExtra("pop") == null ? null :(mx.smartteam.entities.Pop) getIntent().getSerializableExtra("pop");
        this.currentSondeo = getIntent().getSerializableExtra("sondeo") == null ? null : (mx.smartteam.entities.SondeoModulos) getIntent().getSerializableExtra("sondeo");
        this.currentPregunta3 = getIntent().getSerializableExtra("pregunta3") == null ? null : (mx.smartteam.entities.Pregunta) getIntent().getSerializableExtra("pregunta3");
        
        this.popVisita = mx.smartteam.business.PopVisita.GetById(context, currentPop.IdVisita);
        
        if (this.currentUsuario != null && this.currentProyecto != null && this.currentPop != null) {
            setTitle(currentSondeo.Nombre);
            CreaLayout();
        }
        File mediaStorageDir = new File("/mnt/sdcard/Pictures/Team/");
        Utilerias.deleteDirectory(mediaStorageDir);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.foto_1, menu);
        return true;
    }
    
     public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                
                Guardar();
                
            break;

            case R.id.action_cancelar:
                finish();
            break;
        }
        return true;
    }
     
     
     private void CreaLayout() {
        try {
            this.LayoutSondeo = null;
            this.LayoutSondeo = (LinearLayout) findViewById(R.id.LayoutSondeoAC);

            mx.smartteam.entities.PreguntaCollection preguntaCollection = null;
            
            /* obtenemos las preguntas y las pintamos segun sea el caso*/
                preguntaCollection = mx.smartteam.business.Pregunta.GetPreguntaBySondeo(context, currentSondeo);
            
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
                        this.LayoutSondeo.addView(PreguntaComboSelect(pregunta));
                    break;
                }

            }
        } catch (Exception ex) {
            Logger.getLogger(SondeoActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        
            /*if(currentContestacion == null){
                if(count == 0){
                    rad.setChecked(true);
                }
            }*/

            group.addView(rad);
            count++;
        }


        layout.addView(text);
        layout.addView(group, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
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

        if (countSlected.equals(0)) {
            return null;
        } else {
            return pregunta;
        }
    }
    
    private Pregunta ResultadoPreguntaCombo(LinearLayout view, Pregunta pregunta) {
       //Spinner spinner = (Spinner) view.getChildAt(1); 
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
            
            // Crear método para obtener Resultado
                
            case combo:
                 pregunta = ResultadoPreguntaCombo(view, pregunta);
            break;
            
            
        }

        if(currentPregunta3 != null){
            String respuesta=currentPregunta3.Respuesta.toString();
            int [ ]  idcont= new int[2 ]  ;
            idcont= mx.smartteam.data.Contestacion.ContestacionId(context,respuesta,popVisita);
             
        }
        return pregunta;
    }
   
   
   
     
     
   private void Guardar() {
        mx.smartteam.entities.PreguntaCollection preguntaCollection = null;        
        preguntaCollection = new PreguntaCollection();
        for (int i = 0; i < this.LayoutSondeo.getChildCount(); i++) {
            LinearLayout view = (LinearLayout) this.LayoutSondeo.getChildAt(i);
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
        
        if(preguntaCollection.size() > 0)
        {
            preguntaCollection.add(currentPregunta3);  
            mCamera.takePicture(null, null, mPicture); /* Tomamos una foto */
                        
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
            String result = "OK" ;
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(AndroidCamera.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            File imagefile = new File(ruta);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(imagefile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , out);
            byte[] b = out.toByteArray();
            imageBytes = b;
            
            String encodedImage = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
            Foto objFoto = new Foto();
            objFoto.IdVisita = currentPop.IdVisita;
            objFoto.Foto = encodedImage;
            objFoto.Tipo = Foto.Type.Sondeo;
            objFoto.IdSondeo = currentSondeo.Id;
            objFoto.Comentario = currentPregunta3.Respuesta.toString();
            
            
            if(objFoto.IdCategoria == null){objFoto.IdCategoria = 0;}
            if(objFoto.IdSondeo == null){objFoto.IdSondeo = 0;}
            if(objFoto.nOpcion==null){objFoto.nOpcion = 0;}
            if(objFoto.Marcaid == null){objFoto.Marcaid =0;}
            if(objFoto.SKU == null){objFoto.SKU =0L;}
            
            long fechac = System.currentTimeMillis() / 1000;
            int t = 0; 
            t = mx.smartteam.data.Foto.Insert(context, objFoto);
            if(t == 0){
                result= "OK";
            }
            result = RespuestaSondeo.Insertar(context, currentUsuario, currentPop, currentSondeo, params[0], fechac, null, null, null, 0 , 0 );
            
            return result;
        }

        protected void onPostExecute(String resultado) {
            pd.dismiss();
            
            if (resultado.equals("OK")) {
                Toast.makeText(context, "Sondeo registrado exitosamente.", Toast.LENGTH_LONG).show();
                
                finish();
            } else {
                Toast.makeText(context, "Error al registrar el sondeo", Toast.LENGTH_LONG).show();
            }
        }
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
    
    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
            return cameraId;
    }

    private int findBackFacingCamera() {
        int cameraId = -1;
        //Search for the back facing camera
        //get the number of cameras
        int numberOfCameras = Camera.getNumberOfCameras();
        //for every camera check
        for (int i = 0; i < numberOfCameras; i++) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
                    cameraId = i;
                    cameraFront = false;
                    break;
            }
        }
        return cameraId;
    }

    public void onResume() {
        super.onResume();
        if (!hasCamera(context)) {
                Toast toast = Toast.makeText(context, "Sorry, your phone does not have a camera!", Toast.LENGTH_LONG);
                toast.show();
                finish();
        }
        if (mCamera == null) {
                //if the front facing camera does not exist		
                mCamera = Camera.open(findBackFacingCamera());
                mPicture = getPictureCallback();
                mPreview.refreshCamera(mCamera);
        }
    }

    public void initialize() {
        cameraPreview = (LinearLayout) findViewById(R.id.camera_preview);
        mPreview = new CameraPreview(context, mCamera);
        cameraPreview.addView(mPreview);

    }

    public void chooseCamera() {
        //if the camera preview is the front
        if (cameraFront) {
            int cameraId = findBackFacingCamera();
            if (cameraId >= 0) {
                //open the backFacingCamera
                //set a picture callback
                //refresh the preview
                mCamera = Camera.open(cameraId);				
                mPicture = getPictureCallback();			
                mPreview.refreshCamera(mCamera);
            }
        } else {
            int cameraId = findFrontFacingCamera();
            if (cameraId >= 0) {
                    //open the backFacingCamera
                    //set a picture callback
                    //refresh the preview

                    mCamera = Camera.open(cameraId);
                    mPicture = getPictureCallback();
                    mPreview.refreshCamera(mCamera);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
            //when on Pause, release camera in order to be used from other applications
        releaseCamera();
    }

    private boolean hasCamera(Context context) {
            //check if the device has camera
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    private PictureCallback getPictureCallback() {
            PictureCallback picture = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
        //make a new picture file
            pictureFile = getOutputMediaFile();

            if (pictureFile == null) {
                return;
            }
            try {
                //write the file
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                Toast toast = Toast.makeText(context, "Picture saved: " + pictureFile.getName() + "\n"+ pictureFile.toString(), Toast.LENGTH_LONG);
                //toast.show();
                ruta = null;
                ruta = pictureFile.toString();
                /* DEBEMOS DE GUARDAR LA IMAGEN QUE SE TOMO EN BACKGROUND*/

            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }

            //refresh camera to continue preview
            mPreview.refreshCamera(mCamera);
        }
        };
        return picture;
    }

	//make picture and save to a folder
    private static File getOutputMediaFile() {
        //make a new file directory inside the "sdcard" folder
        File mediaStorageDir = new File("/mnt/sdcard/Pictures/Team/");

        //if this "JCGCamera folder does not exist
        if (!mediaStorageDir.exists()) {
                //if you cannot make this folder return
                if (!mediaStorageDir.mkdirs()) {
                        return null;
                }
        }

        
        //take the current timeStamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        //and make a media file:
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        
        
        return mediaFile;
    }

    private void releaseCamera() {
        // stop and release camera
        if (mCamera != null) {
                mCamera.release();
                mCamera = null;
        }
    }
        
}
