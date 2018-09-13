package mx.triplei;

import android.app.ActionBar;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import mx.triplei.R;
import mx.smartteam.entities.MarcaCollection;
import mx.smartteam.entities.ProductoCollection;
import mx.smartteam.entities.Pop;
import mx.smartteam.entities.Marca;
import mx.smartteam.entities.Categoria;
import mx.smartteam.entities.EnumFormulario;
import mx.smartteam.entities.PopVisita;
import mx.smartteam.entities.Proyecto;
import mx.smartteam.entities.Usuario;

public class Sos extends Activity implements View.OnClickListener {
    ProgressDialog progressDoalog;
    private LinearLayout layaut;
    private Pop currentPop;
    private PopVisita currentPopVisita;
    private Context context;
    private ProductoCollection productoCollection = null;
    private EditText txtValor;
    private MarcaCollection marcaCollection = null;
    private Marca Marca;
    static boolean bandera = true;
    static Boolean flag = false;
    private Categoria currentCategoria = null;
    private mx.smartteam.entities.Sos currentSos;
    private Marca CurrenMarca;
    private EnumFormulario currentFormulario;
    private Proyecto currentProyecto;
    private Usuario currentUsuario;
    private mx.smartteam.entities.Sos sos = null;
    private Integer spCategoria,spMarca;
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        this.context = this;
        Bundle bundle = getIntent().getExtras();
        setTitle("SOS");    
        spCategoria = bundle.get("spCategoria") == null ? 0 : (Integer) bundle.get("spCategoria");
        spMarca = bundle.get("spMarca") == null ? 0 : (Integer) bundle.get("spMarca");
        this.currentFormulario = (EnumFormulario) bundle.get("item");
        this.currentUsuario = (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
        this.currentProyecto = (mx.smartteam.entities.Proyecto) getIntent().getSerializableExtra("proyecto");
        this.currentPop = (mx.smartteam.entities.Pop) getIntent().getSerializableExtra("pop");
        this.CurrenMarca = (mx.smartteam.entities.Marca) getIntent().getSerializableExtra("marca");
        this.currentCategoria = (mx.smartteam.entities.Categoria) getIntent().getSerializableExtra("categoria");
        txtValor = (EditText) findViewById(R.id.txtvalor);
        try{
            currentSos = mx.smartteam.business.Sos.getByVisita(context, currentPop, CurrenMarca,currentCategoria);
        }catch(Exception ex){}
        if(currentSos != null){
            if(currentSos.Valor != null){
                txtValor.setText("" + currentSos.Valor );
                txtValor.setTextColor(Color.BLACK);
                currentSos.bandera = 1;
            }
        }else{
            txtValor.setText("");
        }
    }
    
    public void goBack(){
        Intent ii;
        ii = new Intent(Sos.this , ByMarca.class);
        ii.putExtra("usuario", currentUsuario);
        ii.putExtra("proyecto", currentProyecto);
        ii.putExtra("pop", currentPop);
        ii.putExtra("item", currentFormulario);
        ii.putExtra("spCategoria", spCategoria);
        ii.putExtra("spMarca", spMarca);
        startActivity(ii);
        finish();
    }
      
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.anquel_sondeo_1, menu);
        Sucursal sucursal =new Sucursal();
        Drawable d=sucursal.setIconMenu(context);
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(d);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try{
            switch (item.getItemId()) {

                case R.id.action_siguiente:
                    mx.smartteam.entities.Foto objFoto= new  mx.smartteam.entities.Foto();
                    objFoto.Tipo = objFoto.Tipo.Sos;
                    Intent i = new Intent(Sos.this, CameraActivity.class);
                    i.putExtra("usuario", currentUsuario);
                    i.putExtra("proyecto", currentProyecto);
                    i.putExtra("pop", currentPop);
                    i.putExtra("categoria", currentCategoria);
                    i.putExtra("marca", CurrenMarca);
                    i.putExtra("foto", objFoto);
                    i.putExtra("opcionfoto",8);/* Sos */
                    startActivity(i);
                return true;

                case R.id.action_guardar:
                    Guardar();
                    //goBack();
                return true;

                case R.id.action_cancelar:
                    finish();
                return true;
                
                default:
            }
        }catch(Exception e){
            e.getMessage().toString();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
   
    public void Guardar() {
        sos = new mx.smartteam.entities.Sos();
        if(txtValor.getText()!= null){
            sos.Valor = Integer.parseInt(txtValor.getText().toString());
            sos.IdVisita = currentPop.IdVisita;
            sos.IdMarca = CurrenMarca.Id;
            sos.IdCategoria = currentCategoria.Id;
            
            if(currentSos != null){
                sos.IdFoto = currentSos.IdFoto;
                sos.Id = currentSos.Id;
            }
            if(CurrenMarca.Max == 0 || CurrenMarca.Max >= sos.Valor){
                Save();
            }else{
                Validacion();
            }
        }else {
            Toast.makeText(context, "Campo obligatorio, verifique por favor ", Toast.LENGTH_LONG).show();
        }
    }
     
    public void Validacion() {
        String mensaje = "Límite Excedido";         
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle( Html.fromHtml("<font color='#FFFFF'>"+mensaje+"</font>"));
	alertDialogBuilder.setMessage("Máximo permitido " + CurrenMarca.Max + ", Verifique por favor")
        .setCancelable(false)
        .setPositiveButton("Continuar",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {               
            }
        });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        super.onStop();
    }
     
    public void Save(){
        progressDoalog = new ProgressDialog(context);
        progressDoalog.setIcon(R.drawable.team);
        progressDoalog.setTitle("Guardando...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setMessage("Espere por favor...");
        progressDoalog.setCancelable(false);
        progressDoalog.show(); 
     
        new Thread(new Runnable() {
            public void run() {
                try{
                    int idfoto= PhotoActivity.idfoto;
                    if(idfoto > 0){
                        sos.IdFoto = idfoto;                
                    }
                    if(currentSos != null){
                        mx.smartteam.business.Sos.Update(context, sos);                    
                    }else{
                        mx.smartteam.business.Sos.Insert(context, sos);  
                    }
                    PhotoActivity.idfoto = 0; 
                    progressDoalog.dismiss();
                    
                    goBack();
                } catch (Throwable ex) {
                    progressDoalog.dismiss();   
                }
            }
        }).start();
    }
    
}