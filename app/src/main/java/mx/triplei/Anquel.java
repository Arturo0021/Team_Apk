    package mx.triplei;

import android.app.ActionBar;
    import mx.smartteam.entities.Producto;
    import mx.smartteam.exceptions.AnaquelException;
    import android.os.Bundle;
    import android.app.Activity;
    import android.app.AlertDialog;
    import android.app.ProgressDialog;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.graphics.drawable.Drawable;
    import android.os.Handler;
    import android.os.Message;
    import android.text.Html;
    import android.util.Log;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.EditText;
    import android.widget.LinearLayout;
    import android.widget.RadioButton;
    import android.widget.RadioGroup;
    import android.widget.Toast;
    import java.util.ArrayList;
import mx.triplei.R;
    import mx.smartteam.entities.Anaquel;
    import mx.smartteam.entities.EnumFormulario;
    import mx.smartteam.entities.Pop;
    import mx.smartteam.entities.PopVisita;
    import mx.smartteam.entities.Proyecto;
    import mx.smartteam.entities.Usuario;
    import mx.smartteam.entities.ProductoCollection;

    public class Anquel extends Activity {

        ProgressDialog              progDialog;
        private RadioGroup          rad;
        private RadioButton         rbSi,rbNo;
        private LinearLayout        layaut;
        private Producto            currentProducto;
        private Usuario             currentUsuario;
        private Proyecto            currentProyecto;
        private Pop                 currentPop;
        private PopVisita           currentPopVisita;
        private Context             context;
        private Anaquel             anaquelB=null,     anaquelC=null;
        private ProductoCollection  productoCollection=null;
        EditText                    EditExistencia,    EditTotal, 
                                    EditComentarios,   EditTramos,        EditFAlturaTecho, 
                                    EditFAlturaOjos,   EditFAlturaManos,  EditFAlturaSuelo;
        Sucursal sucursal =new Sucursal();
/*******************************************************************************/ 
    @Override
    protected void onCreate(Bundle savedInstanceState){
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anaquel);
        
        this.context = this;
        productoCollection = new ProductoCollection();
        ArrayList<mx.smartteam.entities.Producto> ProductosPorMarca = (ArrayList<mx.smartteam.entities.Producto>) getIntent().getSerializableExtra("productoCollectionx");
        
        for(int i =0; i< ProductosPorMarca.size(); i++){
            productoCollection.add(ProductosPorMarca.get(i));
        }
            
        this.currentUsuario = getIntent().getSerializableExtra("usuario") == null ? null : (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
        this.currentProyecto = getIntent().getSerializableExtra("proyecto") == null ? null : (mx.smartteam.entities.Proyecto) getIntent().getSerializableExtra("proyecto");
        this.currentPop = getIntent().getSerializableExtra("pop") == null ? null : (mx.smartteam.entities.Pop) getIntent().getSerializableExtra("pop");
        this.currentProducto = getIntent().getSerializableExtra("producto") == null ? null : (mx.smartteam.entities.Producto) getIntent().getSerializableExtra("producto");

        try{ 
            //anaquelB = mx.smartteam.business.Anaquel.GetInfoByVisita(context, currentProyecto, currentUsuario, currentPop,currentProducto);
        }catch(Exception ex){
            ex.getMessage();
        }
        
        if (this.currentUsuario != null && this.currentProyecto != null && this.currentPop != null && this.currentProducto != null){
            
            this.setTitle(Html.fromHtml("<small><strong>" + currentProducto.Nombre + "</strong></small>"));
            this.EditExistencia = (EditText) findViewById(R.id.EditExistencia);
            this.EditTotal = (EditText) findViewById(R.id.EditTotal);
            this.EditComentarios = (EditText) findViewById(R.id.EditComentarios);
            this.EditTramos = (EditText) findViewById(R.id.EditTramos);
            this.EditFAlturaTecho = (EditText) findViewById(R.id.EditFAlturaTecho);
            this.EditFAlturaOjos = (EditText) findViewById(R.id.EditFAlturaOjos);
            this.EditFAlturaManos = (EditText) findViewById(R.id.EditFAlturaManos);
            this.EditFAlturaSuelo = (EditText) findViewById(R.id.EditFAlturaSuelo);


            rbSi.setOnClickListener (new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    
                    layaut = (LinearLayout) findViewById(R.id.LeyautFrentes);
                    layaut.setVisibility(View.VISIBLE);
                }
            });

            rbNo.setOnClickListener (new View.OnClickListener(){
                @Override
                public void onClick(View v){// TODO Auto-generated method stub
                        
                    layaut = (LinearLayout) findViewById(R.id.LeyautFrentes);
                    layaut.setVisibility(View.GONE);
                }
            });
        }
        
        if(anaquelB!= null&&anaquelB.Cantidad!=null){
            if(anaquelB.Cantidad!=0||anaquelB.Cantidad!=null){
                EditExistencia.setText(""+anaquelB.Cantidad);
            }else{
                EditExistencia.setText("");
            }

            if(anaquelB.Precio!=0||anaquelB.Precio!=null){
                EditTotal.setText(""+anaquelB.Precio);
            }else{
                EditTotal.setText("");
            }
            
            if(anaquelB.Comentario!=null||!anaquelB.Comentario.equals("null")){
                EditComentarios.setText(""+anaquelB.Comentario);
            }else{
                EditComentarios.setText("");
            }
            
            if(anaquelB.Techo!=0||anaquelB.Techo!=null){
                EditFAlturaTecho.setText(""+anaquelB.Techo);
            }else{
                EditFAlturaTecho.setText("");
            }
            
            if(anaquelB.Ojos!=0||anaquelB.Ojos!=null){
                EditFAlturaOjos.setText(""+anaquelB.Ojos);
            }else{
                EditFAlturaOjos.setText("");
            }
            
            if(anaquelB.Manos!=0||anaquelB.Manos!=null){
                EditFAlturaManos.setText(""+anaquelB.Manos);
            }else{
                EditFAlturaManos.setText("");
            }

            if(anaquelB.Suelo!=0||anaquelB.Suelo!=null){
                EditFAlturaSuelo.setText(""+anaquelB.Suelo);
            }else{
                EditFAlturaSuelo.setText("");
            }
        }else{
            EditExistencia.setText("");
            EditTotal.setText("");
            EditComentarios.setText("");
            EditFAlturaTecho.setText("");
            EditFAlturaOjos.setText("");
            EditFAlturaManos.setText("");
            EditFAlturaSuelo.setText("");
        }
    }
/******************************************************************************/ 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
     // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.anquel_sondeo, menu);
        Sucursal sucursal =new Sucursal();
        Drawable d=sucursal.setIconMenu(context);
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(d);
        if(mx.triplei.ProductoActivity.porProducto){
            menu.removeItem(R.id.action_siguiente);
        }else{
            menu.removeItem(R.id.action_guardar);
        }
        
        return true;
    }
 /*******************************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            
            case R.id.action_siguiente:
                 Siguiente();
                 return true;
            
            case R.id.action_guardar:
                 Guardar();
                 return true;
            
            case R.id.action_cancelar:
                 ReDirect();
                 return true;
            
            case R.id.action_foto:
                 foto();
                 return true;
            
            default:
                return super.onOptionsItemSelected(item);
        }
    }
 /******************************************************************************/ 
    @Override
    protected ProgressDialog onCreateDialog(int id) {
            switch (id) {
                case 0:                      // Spinner
                    progDialog = new ProgressDialog(this);
                    progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progDialog.setTitle(R.string.title_dialog);
                    progDialog.setMessage(getResources().getString(R.string.message_espere_dialog));

                    //progThread = new ProgressThread(handler);
                    //progThread.start();
                    return progDialog;
                case 1:                      // Horizontal
                    progDialog = new ProgressDialog(this);
                    progDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    //progDialog.setMax(maxBarValue);
                    progDialog.setMessage("Dollars in checking account:");
                    //progThread = new ProgressThread(handler);
                    //progThread.start();
                    return progDialog;
                default:
                    return null;
            }
        }
     /************************************************************************************************************/ 
        private Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                //confirmacion del registro de informacion
                dismissDialog(0);
                String confirmacion = (String) msg.obj;
                Toast.makeText(context, confirmacion, Toast.LENGTH_LONG).show();

                //Preguntamos si desea capturar otro producto

               // OtroProducto();

            }
        };
      /************************************************************************************************************/ 
        private void Guardar() {
            String msgMinimo = getResources().getString(R.string.cantidad_minima_permitida);
            String msgMaximo = getResources().getString(R.string.cantidad_maxima_permitida);
            try {
                final Anaquel objAnaquel = new Anaquel();

                if (this.EditExistencia.getText().toString().isEmpty()) {

                    throw new AnaquelException.CantidaEmpty();
                }

                if (this.EditTotal.getText().toString().isEmpty()) {
                    throw new AnaquelException.PrecioEmpty();
                }
                objAnaquel.IdProyecto = currentProyecto.Id;
                objAnaquel.IdUsuario = currentUsuario.Id;
                objAnaquel.DeterminanteGSP = currentPop.DeterminanteGSP;
                objAnaquel.Sku = currentProducto.SKU;
                objAnaquel.Cantidad = EditExistencia.getText().toString().isEmpty() ? null : Integer.parseInt(EditExistencia.getText().toString());
                objAnaquel.Precio = EditTotal.getText().toString().isEmpty() ? null : Double.parseDouble(EditTotal.getText().toString());
                objAnaquel.Comentario = EditComentarios.getText().toString();
                objAnaquel.Suelo = EditFAlturaSuelo.getText().toString().isEmpty() ? null : Integer.parseInt(EditFAlturaSuelo.getText().toString());
                objAnaquel.Manos = EditFAlturaManos.getText().toString().isEmpty() ? null : Integer.parseInt(EditFAlturaManos.getText().toString());
                objAnaquel.Ojos = EditFAlturaOjos.getText().toString().isEmpty() ? null : Integer.parseInt(EditFAlturaOjos.getText().toString());
                objAnaquel.Techo = EditFAlturaTecho.getText().toString().isEmpty() ? null : Integer.parseInt(EditFAlturaTecho.getText().toString());
                objAnaquel.IdVisita = currentPop.IdVisita;


                if (Double.parseDouble(this.EditTotal.getText().toString()) > currentProducto.Maximo || Double.parseDouble(this.EditTotal.getText().toString()) < currentProducto.Minimo) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle( Html.fromHtml("<font color='#FFFFF'>"+R.string.title_dialog+"</font>"));
                    //alertDialogBuilder.setTitle(R.string.title_dialog);
                    Drawable d=sucursal.setIconAlert(context);
                    alertDialogBuilder.setIcon(d);
                    alertDialogBuilder.setMessage(Double.parseDouble(this.EditTotal.getText().toString()) > currentProducto.Maximo ? msgMaximo : msgMinimo)
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            showDialog(0);
                            new Thread(new Save(objAnaquel)).start();

                        }
                    })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            EditTotal.setFocusable(true);
                            EditTotal.setSelected(true);
                        }
                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();


                } else {
                    showDialog(0);
                    new Thread(new Save(objAnaquel)).start();


                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                Toast.makeText(getBaseContext(), e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }


        }  

      /************************************************************************************************************/ 
    private void foto(){
 
              Intent i;
            i = new Intent(this, CameraActivity.class);
                   i.putExtra("usuario", currentUsuario);
                    i.putExtra("proyecto", currentProyecto);
                    i.putExtra("pop", currentPop);
                    
                    startActivity(i);
                    // finish();
                    super.onStop();

                    
 }
        private void Siguiente(){
            if  (productoCollection.size() > 0) 
                {
                    for (int i=0;i<productoCollection.size();i++){

                        if(currentProducto.SKU.equals(productoCollection.get(i).SKU)){ 
                             Guardar();
                            productoCollection.remove(i);
                        }
                        if(productoCollection.size()>0){
                           currentProducto=productoCollection.get(0);
                           }else
                            {
                            //ect();
                             //   finish();
                            }
                           }
                }
            else{
             //  finish();
                }
            
           

            ThisElements();
        }

        public void ThisElements(){

                           //Alan
            setContentView(R.layout.activity_anaquel);
            //setContentView(R.layout.activity_anaquel);<--original
            //


            this.context = this;
            this.currentProducto = getIntent().getSerializableExtra("producto") == null ? null : (mx.smartteam.entities.Producto) getIntent().getSerializableExtra("producto");

            try{ 
                if(currentProducto.bandera>0)
                {
                anaquelC = mx.smartteam.business.Anaquel.GetInfoByVisita2(context, currentProyecto, currentUsuario, currentPop,productoCollection.get(0).Nombre);
                }
            }
            catch(Exception ex){
                ex.getMessage();
            }

            if (this.currentUsuario != null && this.currentProyecto != null && this.currentPop != null && this.currentProducto != null && productoCollection.size() > 0) {

                this.setTitle(Html.fromHtml("<small><strong>" + productoCollection.get(0).Nombre + "</strong></small>"));

                this.EditExistencia = (EditText) findViewById(R.id.EditExistencia);

                this.EditTotal = (EditText) findViewById(R.id.EditTotal);

                this.EditComentarios = (EditText) findViewById(R.id.EditComentarios);

                this.EditTramos = (EditText) findViewById(R.id.EditTramos);

                this.EditFAlturaTecho = (EditText) findViewById(R.id.EditFAlturaTecho);

                this.EditFAlturaOjos = (EditText) findViewById(R.id.EditFAlturaOjos);

                this.EditFAlturaManos = (EditText) findViewById(R.id.EditFAlturaManos);

                //this.EditFAlturaSuelo = (EditText) findViewById(R.id.EditFAlturaSuelo);
                //           rad = (RadioGroup) findViewById(R.id.rdgGrupo);
                //rbSi = (RadioButton) findViewById(R.id.radioSi);
                //rbNo = (RadioButton) findViewById(R.id.radioNo);

//                rbSi.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // TODO Auto-generated method stub
//                        layaut = (LinearLayout) findViewById(R.id.LeyautFrentes);
//                        layaut.setVisibility(View.VISIBLE);
//
//                    }
//                });
//
//               rbNo.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // TODO Auto-generated method stub
//                        layaut = (LinearLayout) findViewById(R.id.LeyautFrentes);
//                        layaut.setVisibility(View.GONE);
//                    }
//                });
            }
            else{        
                finish();        
            }

            if(anaquelC==null){

                    EditExistencia.setText("");
                    EditTotal.setText("");
                    EditComentarios.setText("");
                    EditFAlturaTecho.setText("");
                    EditFAlturaOjos.setText("");
                    EditFAlturaManos.setText("");
                    EditFAlturaSuelo.setText("");
                }
                else{
                   if(anaquelC.Sku!=0&&anaquelC.Cantidad!=null){
                     if(anaquelC.Cantidad!=0||anaquelC.Cantidad!=null)
                       {EditExistencia.setText(""+anaquelC.Cantidad);}
                   else{EditExistencia.setText("");}

                     if(anaquelC.Precio!=0||anaquelC.Precio!=null)
                       {EditTotal.setText(""+anaquelC.Precio);}
                   else{EditTotal.setText("");}

                     if(anaquelC.Comentario!=null||!anaquelC.Comentario.equals("null"))
                       {EditComentarios.setText(""+anaquelC.Comentario);}
                   else{EditComentarios.setText("");}

                     if(anaquelC.Techo!=0||anaquelC.Techo!=null)
                       {EditFAlturaTecho.setText(""+anaquelC.Techo);}
                   else{EditFAlturaTecho.setText("");}

                     if(anaquelC.Ojos!=0||anaquelC.Ojos!=null)
                       {EditFAlturaOjos.setText(""+anaquelC.Ojos);}
                   else{EditFAlturaOjos.setText("");}

                     if(anaquelC.Manos!=0||anaquelC.Manos!=null)
                       {EditFAlturaManos.setText(""+anaquelC.Manos);}
                   else{EditFAlturaManos.setText("");}

                     if(anaquelC.Suelo!=0||anaquelC.Suelo!=null)
                       {EditFAlturaSuelo.setText(""+anaquelC.Suelo);}
                   else{EditFAlturaSuelo.setText("");}
                }
            }
            
            
            
           

           if(productoCollection.size()>0){
                //productoCollection.remove(0);         
                for (int i=0;i<productoCollection.size();i++){

                    if(currentProducto.SKU.equals(productoCollection.get(i).SKU)){ 
                        Guardar();
                        productoCollection.remove(i);
                    }
                    if(productoCollection.size()>0){
                       currentProducto=productoCollection.get(0);
                    }
                    else{                    
                        ReDirect();
                       // finish();
                    }
                }
            }
            else{ 
               
                finish();
            }   
              anaquelB=anaquelC;   
        }
    /******************************************************************************/
        public void Body(){

        };
        /*************************************/
        public void ReDirect (){
          Intent intent = new Intent("mx.smartteam.producto");
                    intent.putExtra("item", EnumFormulario.existencias_anaquel);
                   /* intent.putExtra("usuario", currentUsuario);
                    intent.putExtra("proyecto", currentProyecto);
                    intent.putExtra("pop", currentPop);*/
                    startActivity(intent);
                    finish();

        }
    /******************************************************************************/ 
        class Save implements Runnable {

            private final Anaquel anaquel;

            public Save(Anaquel anaquel) {
                this.anaquel = anaquel;
            }

            @Override
            public void run() {
                try {
                    //Thread.sleep(2000);

                    if(currentProducto.bandera==0){
                        
                       // mx.smartteam.business.Anaquel.GetInfoByVisita(context, currentProyecto, currentUsuario,currentPop,currentProducto);
                        /*
                        Context context, mx.smartteam.entities.Proyecto proyecto , mx.smartteam.entities.Usuario usuario,
                mx.smartteam.entities.Pop pop, mx.smartteam.entities.Producto producto
                        */
                    //mx.smartteam.business.Anaquel.Insert(context, this.anaquel);
                    }else
                    { 
                      //anaquel.Sku= anaquelB.Sku;  
                    //mx.smartteam.business.Anaquel.Update(context, this.anaquel);

                    }
                    Message msg = new Message();
                    msg.obj = getResources().getString(R.string.confirmacion_registro);
                    handler.sendMessage(msg);




                } catch (Exception e) {
                    Log.e("Error Save[Anaquel]", e.getMessage());
                }


            }
        }

     /************************************************************************************************************/ 
    }
