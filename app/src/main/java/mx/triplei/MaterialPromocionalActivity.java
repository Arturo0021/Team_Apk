package mx.triplei;

import android.app.ActionBar;
import mx.smartteam.settings.Ajustes;
import mx.smartteam.settings.ServiceClient;

import android.os.AsyncTask;
import android.os.Bundle;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.triplei.R;
import mx.smartteam.entities.EnumFormulario;
import mx.smartteam.entities.MaterialPromocional;
import mx.smartteam.entities.Pop;
import mx.smartteam.entities.Producto;
import mx.smartteam.entities.ProductoCollection;
import mx.smartteam.entities.Proyecto;
import mx.smartteam.entities.Usuario;

public class MaterialPromocionalActivity extends Activity {

    //private GlobalSettings settings;
    private Producto currentProducto;
    private Usuario currentUsuario;
    private Proyecto currentProyecto;
    private Pop currentPop;
    private EditText EditCenafas, EditDangles, EditStoppers, EditColgantes,
            EditCartulinas, EditCorbatas, EditFlash, EditTiras,
            EditPreciadores, EditFolletos, EditTapetes, EditFaldones,
            EditOtros, EditComentarios;
       private MaterialPromocional materialPromocionalB=null, materialPromocionalC=null;
       private ProductoCollection productoCollection=null;
    private Context context;
    private EnumFormulario currentFormulario;
    private Integer spCategoria,spMarca;
    private String tipoform;
    Sucursal sucursal =new Sucursal();
    

    @Override
 /************************************************************************************************/
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_promocional);
        context = this;
        
        ArrayList<mx.smartteam.entities.Producto> ProductosPorMarca = (ArrayList<mx.smartteam.entities.Producto>) getIntent().getSerializableExtra("productoCollection");
        productoCollection = new ProductoCollection();
        for(int i =0; i< ProductosPorMarca.size(); i++)
           {
          productoCollection.add(ProductosPorMarca.get(i));
           }

        this.currentUsuario = getIntent().getSerializableExtra("usuario") == null ? null : (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
        this.currentProyecto = getIntent().getSerializableExtra("proyecto") == null ? null : (mx.smartteam.entities.Proyecto) getIntent().getSerializableExtra("proyecto");
        this.currentPop = getIntent().getSerializableExtra("pop") == null ? null : (mx.smartteam.entities.Pop) getIntent().getSerializableExtra("pop");
        this.currentProducto = getIntent().getSerializableExtra("producto") == null ? null : (mx.smartteam.entities.Producto) getIntent().getSerializableExtra("producto");
        Bundle bundle = getIntent().getExtras();
        this.tipoform = (String) bundle.get("tipoform") == null ? null : (String) bundle.get("tipoform");
        this.currentFormulario = (EnumFormulario) bundle.get("item");
        spCategoria = bundle.get("spCategoria") == null ? 0 : (Integer) bundle.get("spCategoria");
        spMarca = bundle.get("spMarca") == null ? 0 : (Integer) bundle.get("spMarca");
        try{ 
            materialPromocionalB = mx.smartteam.business.MaterialPromocional.GetInfoByVisita(context, currentProyecto, currentUsuario, currentPop,currentProducto);
        }catch(Exception ex){
            ex.getMessage();
        }
        if (this.currentUsuario != null && this.currentProyecto != null && this.currentPop != null && this.currentProducto != null) {
            this.setTitle(Html.fromHtml("<small><strong>" + this.currentProducto.Nombre + "</strong></small>"));
        }
        EditCenafas = (EditText) findViewById(R.id.EditCenafas);
        EditDangles = (EditText) findViewById(R.id.EditDangles);
        EditStoppers = (EditText) findViewById(R.id.EditStoppers);
        EditColgantes = (EditText) findViewById(R.id.EditColgantes);
        EditCartulinas = (EditText) findViewById(R.id.EditCartulinas);
        EditCorbatas = (EditText) findViewById(R.id.EditCorbatas);
        EditFlash = (EditText) findViewById(R.id.EditFlash);
        EditTiras = (EditText) findViewById(R.id.EditTiras);
        EditPreciadores = (EditText) findViewById(R.id.EditPreciadores);
        EditFolletos = (EditText) findViewById(R.id.EditFolletos);
        EditTapetes = (EditText) findViewById(R.id.EditTapetes);
        EditFaldones = (EditText) findViewById(R.id.EditFaldones);
        EditOtros = (EditText) findViewById(R.id.EditOtros);
        EditComentarios = (EditText) findViewById(R.id.EditComentarios);
        this.context = this;
        
        if(materialPromocionalB!= null){
             if(materialPromocionalB.Cenafas>0||materialPromocionalB.Cenafas!=null)
               {
                   EditCenafas .setText(materialPromocionalB.Cenafas+"");}
           else{
                 EditCenafas .setText("");}
             
             if(materialPromocionalB.Dangles > 0)
               {EditDangles.setText(materialPromocionalB.Dangles+"");}
           else{ EditDangles.setText("");}
                          
             if(materialPromocionalB.Stoppers > 0)
               {EditStoppers.setText(materialPromocionalB.Stoppers+"");}
           else{ EditStoppers.setText("");}
            
             if(materialPromocionalB.Colgantes>0)
               {EditColgantes .setText(materialPromocionalB.Colgantes+"");}
           else{EditColgantes .setText("");}
             
             if(materialPromocionalB.Cartulinas > 0)
               {EditCartulinas.setText(materialPromocionalB.Cartulinas+"");}
           else{ EditCartulinas.setText("");}
                          
             if(materialPromocionalB.Corbatas > 0)
               {EditCorbatas.setText(materialPromocionalB.Corbatas+"");}
           else{ EditCorbatas.setText("");}
           
             if(materialPromocionalB.Flash>0)
               {EditFlash .setText(materialPromocionalB.Flash+"");}
           else{EditFlash .setText("");}
             
             if(materialPromocionalB.Tiras > 0)
               {EditTiras.setText(materialPromocionalB.Tiras+"");}
           else{ EditTiras.setText("");}
                          
             if(materialPromocionalB.Preciadores > 0)
               {EditPreciadores.setText(materialPromocionalB.Preciadores+"");}
           else{ EditPreciadores.setText("");}
            
             if(materialPromocionalB.Folletos>0)
               {EditFolletos .setText(materialPromocionalB.Folletos+"");}
           else{EditFolletos .setText("");}
             
             if(materialPromocionalB.Tapetes > 0)
               {EditTapetes.setText(materialPromocionalB.Tapetes+"");}
           else{ EditTapetes.setText("");}
                          
             if(materialPromocionalB.Faldones > 0)
               {EditFaldones.setText(materialPromocionalB.Faldones+"");}
           else{ EditFaldones.setText("");}
             
             if(materialPromocionalB.Otros > 0)
               {EditOtros.setText(materialPromocionalB.Otros+"");}
           else{ EditOtros.setText("");}
                          
             if(materialPromocionalB.Comentario != null)
               {
                   EditComentarios.setText(materialPromocionalB.Comentario+"");}
           else{ 
                 EditComentarios.setText("");
             }
      
           }
    }

    @Override
 /************************************************************************************************/
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.material_promocional, menu);
           if(mx.triplei.ProductoActivity.porProducto){
               menu.removeItem(R.id.action_siguiente);
           }else{
               menu.removeItem(R.id.action_guardar);
           }
           Sucursal sucursal =new Sucursal();
        Drawable d=sucursal.setIconMenu(context);
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(d);
           return true;
    }
 /******************************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        switch (item.getItemId()) {
            case R.id.action_siguiente:
                Siguiente();
                return true;
            
            case R.id.action_cancelar:
                finish();
                goBack();
                return true;
            
            case R.id.action_save:
                Guardar();
                return true;
            
            case R.id.action_foto:
                foto();
                return true;
            
            default:
                return super.onOptionsItemSelected(item);
        }

    }
/*******************************************************************************/
     public void goBack(){
        Intent ii;
        ii = new Intent(MaterialPromocionalActivity.this , ProductoActivity.class);
        ii.putExtra("usuario", currentUsuario);
        ii.putExtra("proyecto", currentProyecto);
        ii.putExtra("pop", currentPop);
        ii.putExtra("item", currentFormulario);
        ii.putExtra("spCategoria", spCategoria);
        ii.putExtra("spMarca", spMarca);
        ii.putExtra("tipo", tipoform);
        startActivity(ii);
        finish();
    }
    
    
    private void foto(){
        mx.smartteam.entities.Foto  objFoto= new  mx.smartteam.entities.Foto();
        objFoto.Tipo = objFoto.Tipo.MaterialPromocional;
        Intent i;
        i = new Intent(this, CameraActivity.class);
        i.putExtra("usuario", currentUsuario);
        i.putExtra("proyecto", currentProyecto);
        i.putExtra("pop", currentPop);
        i.putExtra("opcionfoto",5);/* solo Material Promocional */
        i.putExtra("producto", currentProducto);
        i.putExtra("foto", objFoto);
        startActivity(i);
        super.onStop();
    }
/*******************************************************************************/    
    protected void Guardar(){

        Boolean isEmpty = true;


        if (!EditCenafas.getText().toString().isEmpty()) {
            isEmpty = false;
        }
        if (!EditDangles.getText().toString().isEmpty()) {
            isEmpty = false;
        }
        if (!EditStoppers.getText().toString().isEmpty()) {
            isEmpty = false;
        }
        if (!EditColgantes.getText().toString().isEmpty()) {
            isEmpty = false;
        }
        if (!EditCartulinas.getText().toString().isEmpty()) {
            isEmpty = false;
        }
        if (!EditCorbatas.getText().toString().isEmpty()) {
            isEmpty = false;
        }
        if (!EditFlash.getText().toString().isEmpty()) {
            isEmpty = false;
        }
        if (!EditTiras.getText().toString().isEmpty()) {
            isEmpty = false;
        }
        if (!EditPreciadores.getText().toString().isEmpty()) {
            isEmpty = false;
        }
        if (!EditFolletos.getText().toString().isEmpty()) {
            isEmpty = false;
        }
        if (!EditTapetes.getText().toString().isEmpty()) {
            isEmpty = false;
        }
        if (!EditFaldones.getText().toString().isEmpty()) {
            isEmpty = false;
        }
        if (!EditOtros.getText().toString().isEmpty()) {
            isEmpty = false;
        }
        if (!EditComentarios.getText().toString().isEmpty()) {
            isEmpty = false;
        }

        if (!isEmpty) {

            MaterialPromocional materialPromocional = new MaterialPromocional();
            materialPromocional.IdProyecto = currentProyecto.Id;
            materialPromocional.IdUsuario = currentUsuario.Id;
            materialPromocional.DeterminanteGSP = currentPop.Id;
            materialPromocional.Sku = currentProducto.SKU;
            materialPromocional.IdVisita = currentPop.IdVisita;
            materialPromocional.Cenafas = EditCenafas.getText().toString().isEmpty() ? null : Integer.parseInt(EditCenafas.getText().toString());
            materialPromocional.Dangles = EditDangles.getText().toString().isEmpty() ? null : Integer.parseInt(EditDangles.getText().toString());
            materialPromocional.Stoppers = EditStoppers.getText().toString().isEmpty() ? null : Integer.parseInt(EditStoppers.getText().toString());
            materialPromocional.Colgantes = EditColgantes.getText().toString().isEmpty() ? null : Integer.parseInt(EditColgantes.getText().toString());
            materialPromocional.Cartulinas = EditCartulinas.getText().toString().isEmpty() ? null : Integer.parseInt(EditCartulinas.getText().toString());
            materialPromocional.Corbatas = EditCorbatas.getText().toString().isEmpty() ? null : Integer.parseInt(EditCorbatas.getText().toString());
            materialPromocional.Flash =EditFlash.getText().toString().isEmpty() ? null : Integer.parseInt(EditFlash.getText().toString());
            materialPromocional.Tiras = EditTiras.getText().toString().isEmpty() ? null : Integer.parseInt(EditTiras.getText().toString());
            materialPromocional.Preciadores = EditPreciadores.getText().toString().isEmpty() ? null : Integer.parseInt(EditPreciadores.getText().toString());
            materialPromocional.Folletos = EditFolletos.getText().toString().isEmpty() ? null : Integer.parseInt(EditFolletos.getText().toString());
            materialPromocional.Tapetes = EditTapetes.getText().toString().isEmpty() ? null : Integer.parseInt(EditTapetes.getText().toString());
            materialPromocional.Faldones = EditFaldones.getText().toString().isEmpty() ? null : Integer.parseInt(EditFaldones.getText().toString());
            materialPromocional.Otros = EditOtros.getText().toString().isEmpty() ? null : Integer.parseInt(EditOtros.getText().toString());
            materialPromocional.Comentario = EditComentarios.getText().toString().isEmpty() ? null : EditComentarios.getText().toString();
            if(materialPromocionalB != null){
                materialPromocional.IdFoto = materialPromocionalB.IdFoto;
            }
            
            new Save().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, materialPromocional);

        } else {
            Toast.makeText(this, "No es posible registrar la informacion",
                    Toast.LENGTH_LONG).show();

        }

    }
/************************************************************************************************/ 
    
      private void Siguiente(){
        
        Guardar();
        if  (productoCollection.size() > 0){
            if(productoCollection.size()==1){
                Guardar();
                finish();
            }else{
                for (int i=0;i<productoCollection.size();i++){
                    if(currentProducto.SKU.equals(productoCollection.get(i).SKU)){ 
                        productoCollection.remove(i);
                        ThisElements();
                    }
                    if(productoCollection.size()>0){
                        currentProducto=productoCollection.get(0);
                    }else{
                        finish();
                    }
                }
            }
        }else{
            finish();
        }
      }
/************************************************************************************************/ 
  
    public void ThisElements(){
    setContentView(R.layout.activity_material_promocional);
        context = this;
        
          //settings = ((GlobalSettings) getApplicationContext());

        //setTitle(settings.CurrenProducto.Nombre);
        this.currentUsuario = getIntent().getSerializableExtra("usuario") == null ? null : (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
        this.currentProyecto = getIntent().getSerializableExtra("proyecto") == null ? null : (mx.smartteam.entities.Proyecto) getIntent().getSerializableExtra("proyecto");
        this.currentPop = getIntent().getSerializableExtra("pop") == null ? null : (mx.smartteam.entities.Pop) getIntent().getSerializableExtra("pop");
        this.currentProducto = getIntent().getSerializableExtra("producto") == null ? null : (mx.smartteam.entities.Producto) getIntent().getSerializableExtra("producto");

try{ 
            materialPromocionalC = mx.smartteam.business.MaterialPromocional.GetInfoByVisita2(context, currentProyecto, currentUsuario, currentPop,productoCollection.get(0).Nombre);
            }
        catch(Exception ex){
                            ex.getMessage();
                            }
        if (this.currentUsuario != null && this.currentProyecto != null && this.currentPop != null && this.currentProducto != null && productoCollection.size() > 0) {

            this.setTitle(Html.fromHtml("<small><strong>" + productoCollection.get(0).Nombre + "</strong></small>"));
        }
        EditCenafas = (EditText) findViewById(R.id.EditCenafas);
        EditDangles = (EditText) findViewById(R.id.EditDangles);
        EditStoppers = (EditText) findViewById(R.id.EditStoppers);
        EditColgantes = (EditText) findViewById(R.id.EditColgantes);
        EditCartulinas = (EditText) findViewById(R.id.EditCartulinas);
        EditCorbatas = (EditText) findViewById(R.id.EditCorbatas);
        EditFlash = (EditText) findViewById(R.id.EditFlash);
        EditTiras = (EditText) findViewById(R.id.EditTiras);
        EditPreciadores = (EditText) findViewById(R.id.EditPreciadores);
        EditFolletos = (EditText) findViewById(R.id.EditFolletos);
        EditTapetes = (EditText) findViewById(R.id.EditTapetes);
        EditFaldones = (EditText) findViewById(R.id.EditFaldones);
        EditOtros = (EditText) findViewById(R.id.EditOtros);
        EditComentarios = (EditText) findViewById(R.id.EditComentarios);
        this.context = this;
        
        
        
if(materialPromocionalC!= null&&materialPromocionalC.Cenafas!=null){
             if(materialPromocionalC.Cenafas>0||materialPromocionalC.Cenafas!=null)
               {
                   EditCenafas .setText(materialPromocionalC.Cenafas+"");}
           else{
                 EditCenafas .setText("");}
             
             if(materialPromocionalC.Dangles > 0)
               {EditDangles.setText(materialPromocionalC.Dangles+"");}
           else{ EditDangles.setText("");}
                          
             if(materialPromocionalC.Stoppers > 0)
               {EditStoppers.setText(materialPromocionalC.Stoppers+"");}
           else{ EditStoppers.setText("");}
            
             if(materialPromocionalC.Colgantes>0)
               {EditColgantes .setText(materialPromocionalC.Colgantes+"");}
           else{EditColgantes .setText("");}
             
             if(materialPromocionalC.Cartulinas > 0)
               {EditCartulinas.setText(materialPromocionalC.Cartulinas+"");}
           else{ EditCartulinas.setText("");}
                          
             if(materialPromocionalC.Corbatas > 0)
               {EditCorbatas.setText(materialPromocionalC.Corbatas+"");}
           else{ EditCorbatas.setText("");}
           
             if(materialPromocionalC.Flash>0)
               {EditFlash .setText(materialPromocionalC.Flash+"");}
           else{EditFlash .setText("");}
             
             if(materialPromocionalC.Tiras > 0)
               {EditTiras.setText(materialPromocionalC.Tiras+"");}
           else{ EditTiras.setText("");}
                          
             if(materialPromocionalC.Preciadores > 0)
               {EditPreciadores.setText(materialPromocionalC.Preciadores+"");}
           else{ EditPreciadores.setText("");}
            
             if(materialPromocionalC.Folletos>0)
               {EditFolletos .setText(materialPromocionalC.Folletos+"");}
           else{EditFolletos .setText("");}
             
             if(materialPromocionalC.Tapetes > 0)
               {EditTapetes.setText(materialPromocionalC.Tapetes+"");}
           else{ EditTapetes.setText("");}
                          
             if(materialPromocionalC.Faldones > 0)
               {EditFaldones.setText(materialPromocionalC.Faldones+"");}
           else{ EditFaldones.setText("");}
             
             if(materialPromocionalC.Otros > 0)
               {EditOtros.setText(materialPromocionalC.Otros+"");}
           else{ EditOtros.setText("");}
                          
             if(materialPromocionalC.Comentario != null)
               {
                   EditComentarios.setText(materialPromocionalC.Comentario+"");}
           else{ 
                 EditComentarios.setText("");
             }
      
           }
   
 
 materialPromocionalB=materialPromocionalC;

    }
/******************************************************************************/
    public void ReDirect (){
      Intent intent = new Intent("mx.smartteam.producto");
                intent.putExtra("item", EnumFormulario.material_promocional);
                intent.putExtra("usuario", currentUsuario);
                intent.putExtra("proyecto", currentProyecto);
                intent.putExtra("pop", currentPop);
                startActivity(intent);
                finish();
    
    }
/******************************************************************************/ 
     private void OtroProducto() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle( Html.fromHtml("<font color='#FFFFF'>"+R.string.title_dialog+"</font>"));   
        //alertDialogBuilder.setTitle(R.string.title_dialog);
        Drawable d=sucursal.setIconAlert(context);
        alertDialogBuilder.setIcon(d);
        alertDialogBuilder.setMessage("Desea capturar otro producto?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Intent intent = new Intent("mx.smartteam.producto");
                intent.putExtra("item", EnumFormulario.material_promocional);
                intent.putExtra("usuario", currentUsuario);
                intent.putExtra("proyecto", currentProyecto);
                intent.putExtra("pop", currentPop);
                startActivity(intent);
                finish();
            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

//                Intent i = new Intent("mx.smartteam.menu");
//                startActivity(i);
                finish();
            }
        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
/************************************************************************************************/
    class Save extends AsyncTask<MaterialPromocional, Integer, String> {

        private Object[] parameters;
        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(context);
            pd.setTitle("Enviando informacion...");
            pd.setMessage("Espere por favor.");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();

        }

        @Override
        protected String doInBackground(MaterialPromocional... params) {
            String result = null;

            try {
                // TODO Auto-generated method stub
                int idfoto = PhotoActivity.idfoto;
                if (idfoto > 0) {
                    params[0].IdFoto = idfoto;
                }

                if (materialPromocionalB != null) {
                    params[0].Id = materialPromocionalB.Id;
                    mx.smartteam.business.MaterialPromocional.Update(context, params[0]);
                } else {
                    mx.smartteam.business.MaterialPromocional.Insert(context, params[0]);
                    
                }
                result = "OK";

            } catch (Exception ex) {
                result = "-1";
                Logger.getLogger(MaterialPromocionalActivity.class.getName()).log(Level.SEVERE, null, ex);
            }
            return result;
        }

        protected void onPostExecute(String resultado) {
            pd.dismiss();


            if (resultado.trim().equals("OK")) {
                Toast.makeText(context, "Información enviada correctamente", Toast.LENGTH_LONG).show();
                /*if(productoCollection.size()== 1){
                    //finish();
                }*/
                goBack();
                 //  OtroProducto();
                //finish();
            } else {
                Toast.makeText(context, "Error al enviar la información", Toast.LENGTH_LONG).show();
                // super.execute(this.parameters);
            }

            

        }
    }
/************************************************************************************************/
    
}
