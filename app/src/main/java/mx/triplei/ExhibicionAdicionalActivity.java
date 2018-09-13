package mx.triplei;


import android.app.ActionBar;
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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.triplei.R;
import mx.smartteam.entities.EnumFormulario;
import mx.smartteam.entities.ExhibicionAdicional;
import mx.smartteam.entities.Pop;
import mx.smartteam.entities.Producto;
import mx.smartteam.entities.ProductoCollection;
import mx.smartteam.entities.Proyecto;
import mx.smartteam.entities.Usuario;

public class ExhibicionAdicionalActivity extends Activity {

    private Producto currentProducto;
    private Usuario currentUsuario;
    private Proyecto currentProyecto;
    private Pop currentPop;
    private EditText EditCabecera, EditIsla, EditExhibidor, EditBunker,
            EditArea, EditTira, EditCaja, EditArete, EditForway, EditOtro,
            EditComentario;
    private EditText EditCabeceraFrente, EditIslaFrente, EditExhibidorFrente,
            EditBunkerFrente, EditAreaFrente, EditTiraFrente, EditCajaFrente,
            EditAreteFrente, EditForwayFrente, EditOtroFrente;
    private LinearLayout LayoutCabeceraFrente, LayoutIslaFrente,
            LayoutExhibidoresFrente, LayoutBunkerFrente, LayoutAreaFrente,
            LayoutTiraFrente, LayoutCajaFrente, LayoutAreteFrente,
            LayoutForwayFrente, LayoutOtroFrente;
    private Context context;
    private ExhibicionAdicional exhibicionAdicionalB=null;
    private ExhibicionAdicional exhibicionAdicionalC=null;
    private ProductoCollection productoCollection=null;
    private EnumFormulario currentFormulario;
    private Integer spCategoria,spMarca, bandera =0;
    Sucursal sucursal =new Sucursal();
    private String tipoform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibicion_adicional);

        this.context = this;
        ArrayList<mx.smartteam.entities.Producto> ProductosPorMarca = (ArrayList<mx.smartteam.entities.Producto>) getIntent().getSerializableExtra("productoCollection");
        productoCollection = new ProductoCollection();
        for(int i =0; i< ProductosPorMarca.size(); i++){
           productoCollection.add(ProductosPorMarca.get(i));
        }
        Bundle bundle = getIntent().getExtras();
        this.currentFormulario = (EnumFormulario) bundle.get("item");
        spCategoria = bundle.get("spCategoria") == null ? 0 : (Integer) bundle.get("spCategoria");
        spMarca = bundle.get("spMarca") == null ? 0 : (Integer) bundle.get("spMarca");
        this.tipoform = (String) bundle.get("tipoform") == null ? null : (String) bundle.get("tipoform");
        
        currentUsuario = (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
        currentProyecto = (mx.smartteam.entities.Proyecto) getIntent().getSerializableExtra("proyecto");
        currentPop = (mx.smartteam.entities.Pop) getIntent().getSerializableExtra("pop");
        currentProducto = (mx.smartteam.entities.Producto) getIntent().getSerializableExtra("producto");

        try {
            exhibicionAdicionalB = mx.smartteam.business.ExhibicionAdicional.GetInfoByVisita(context, currentProyecto, currentUsuario, currentPop, currentProducto);
        } catch (Exception ex) {
            ex.getMessage();
        }
        
        if (this.currentUsuario != null && this.currentProyecto != null && this.currentPop != null && this.currentProducto != null){
            this.setTitle(Html.fromHtml("<small><strong>" + currentProducto.Nombre + "</strong></small>"));
            EditCabecera = (EditText) findViewById(R.id.EditCabecera);
            EditIsla = (EditText) findViewById(R.id.EditIsla);
            EditExhibidor = (EditText) findViewById(R.id.EditExhibidor);
            EditBunker = (EditText) findViewById(R.id.EditBunker);
            EditArea = (EditText) findViewById(R.id.EditArea);
            EditTira = (EditText) findViewById(R.id.EditTira);
            EditCaja = (EditText) findViewById(R.id.EditCaja);
            EditArete = (EditText) findViewById(R.id.EditArete);
            EditForway = (EditText) findViewById(R.id.EditForway);
            EditOtro = (EditText) findViewById(R.id.EditOtro);
            EditComentario = (EditText) findViewById(R.id.EditComentario);

            EditCabeceraFrente = (EditText) findViewById(R.id.EditCabeceraFrentes);
            EditIslaFrente = (EditText) findViewById(R.id.EditIslaFrentes);
            EditExhibidorFrente = (EditText) findViewById(R.id.EditExhibidorFrentes);
            EditBunkerFrente = (EditText) findViewById(R.id.EditBunquerFrentes);
            EditAreaFrente = (EditText) findViewById(R.id.EditAreaFrentes);
            EditTiraFrente = (EditText) findViewById(R.id.EditTiraFrentes);
            EditCajaFrente = (EditText) findViewById(R.id.EditCajaFrentes);
            EditAreteFrente = (EditText) findViewById(R.id.EditAreteFrentes);
            EditForwayFrente = (EditText) findViewById(R.id.EditForwayFrentes);
            EditOtroFrente = (EditText) findViewById(R.id.EditOtroFrentes);

            LayoutCabeceraFrente = (LinearLayout) findViewById(R.id.LayoutCabeceraFrente);
            LayoutCabeceraFrente.setFocusable(true);
            LayoutCabeceraFrente.setFocusableInTouchMode(true);

            LayoutIslaFrente = (LinearLayout) findViewById(R.id.LayoutIslaFrente);
            LayoutExhibidoresFrente = (LinearLayout) findViewById(R.id.LayoutExhibidoresFrente);
            LayoutBunkerFrente = (LinearLayout) findViewById(R.id.LayoutBunkerFrente);
            LayoutAreaFrente = (LinearLayout) findViewById(R.id.LayoutAreaFrente);
            LayoutTiraFrente = (LinearLayout) findViewById(R.id.LayoutTiraFrente);
            LayoutCajaFrente = (LinearLayout) findViewById(R.id.LayoutCajaFrente);
            LayoutAreteFrente = (LinearLayout) findViewById(R.id.LayoutAreteFrente);
            LayoutForwayFrente = (LinearLayout) findViewById(R.id.LayoutForwayFrente);
            LayoutOtroFrente = (LinearLayout) findViewById(R.id.LayoutOtroFrente);

            EditCabecera.setOnEditorActionListener(new OnKeyListener());
            EditIsla.setOnEditorActionListener(new OnKeyListener());
            EditExhibidor.setOnEditorActionListener(new OnKeyListener());
            EditBunker.setOnEditorActionListener(new OnKeyListener());
            EditArea.setOnEditorActionListener(new OnKeyListener());
            EditTira.setOnEditorActionListener(new OnKeyListener());
            EditCaja.setOnEditorActionListener(new OnKeyListener());
            EditArete.setOnEditorActionListener(new OnKeyListener());
            EditForway.setOnEditorActionListener(new OnKeyListener());
            EditOtro.setOnEditorActionListener(new OnKeyListener());
            EditComentario.setOnEditorActionListener(new OnKeyListener());
        }
       
           if (exhibicionAdicionalB != null) {
            if (exhibicionAdicionalB.Cabecera != 0) {
                EditCabecera.setText("" + exhibicionAdicionalB.Cabecera);
            } else {
                EditCabecera.setText("");
            }

            if (exhibicionAdicionalB.Cabecera != 0) {
                EditCabecera.setText("" + exhibicionAdicionalB.Cabecera);
            } else {
                EditCabecera.setText("");
            }
            if (exhibicionAdicionalB.Isla != 0) {
                EditIsla.setText("" + exhibicionAdicionalB.Isla);
            } else {
                EditIsla.setText("");
            }

            if (exhibicionAdicionalB.IslaFrente != 0) {
                EditIslaFrente.setText("" + exhibicionAdicionalB.IslaFrente);
            } else {
                EditIsla.setText("");
            }
            if (exhibicionAdicionalB.Exhibidor != 0) {
                EditExhibidor.setText("" + exhibicionAdicionalB.Exhibidor);
            } else {
                EditExhibidor.setText("");
            }

            if (exhibicionAdicionalB.Bunker != 0) {
                EditBunker.setText("" + exhibicionAdicionalB.Bunker);
            } else {
                EditBunker.setText("");
            }
            if (exhibicionAdicionalB.Area != 0) {
                EditArea.setText("" + exhibicionAdicionalB.Area);
            } else {
                EditArea.setText("");
            }
            if (exhibicionAdicionalB.Tira != 0) {
                EditTira.setText("" + exhibicionAdicionalB.Tira);
            } else {
                EditTira.setText("");
            }
            if (exhibicionAdicionalB.Caja != 0) {
                EditCaja.setText("" + exhibicionAdicionalB.Caja);
            } else {
                EditCaja.setText("");
            }

            if (exhibicionAdicionalB.Arete != 0) {
                EditArete.setText("" + exhibicionAdicionalB.Arete);
            } else {
                EditArete.setText("");
            }

            if (exhibicionAdicionalB.Forway != 0) {
                EditForway.setText("" + exhibicionAdicionalB.Forway);
            } else {
                EditForway.setText("");
            }

            if (exhibicionAdicionalB.Otros != 0) {
                EditOtro.setText("" + exhibicionAdicionalB.Otros);
            } else {
                EditOtro.setText("");
            }
            if (exhibicionAdicionalB.Comentario != null) {
                EditComentario.setText("" + exhibicionAdicionalB.Comentario);
            } else {
                EditComentario.setText("");
            }

            if (exhibicionAdicionalB.CabeceraFrente != 0) {
                EditCabeceraFrente.setText("" + exhibicionAdicionalB.CabeceraFrente);
            } else {
                EditCabeceraFrente.setText("");
            }

            if (exhibicionAdicionalB.IslaFrente != 0) {
                EditIslaFrente.setText("" + exhibicionAdicionalB.IslaFrente);
            } else {
                EditIsla.setText("");
            }
            if (exhibicionAdicionalB.ExhibidorFrente != 0) {
                EditExhibidorFrente.setText("" + exhibicionAdicionalB.ExhibidorFrente);
            } else {
                EditExhibidorFrente.setText("");
            }

            if (exhibicionAdicionalB.BunkerFrente != 0) {
                EditBunkerFrente.setText("" + exhibicionAdicionalB.BunkerFrente);
            } else {
                EditBunkerFrente.setText("");
            }

            /* if(exhibicionAdicionalB.AreaFrente!=0||exhibicionAdicionalB.AreaFrente!=null){
             EditAreaFrente.setText(""+exhibicionAdicionalB.AreaFrente);
             }else{
             EditAreaFrente.setText("");
             }*/
            if (exhibicionAdicionalB.TiraFrente != 0) {
                EditTiraFrente.setText("" + exhibicionAdicionalB.TiraFrente);
            } else {
                EditTiraFrente.setText("");
            }
            if (exhibicionAdicionalB.CajaFrente != 0) {
                EditCajaFrente.setText("" + exhibicionAdicionalB.CajaFrente);
            } else {
                EditCajaFrente.setText("");
            }

            if (exhibicionAdicionalB.AreteFrente != 0) {
                EditAreteFrente.setText("" + exhibicionAdicionalB.AreteFrente);
            } else {
                EditAreteFrente.setText("");
            }

            if (exhibicionAdicionalB.ForwayFrente != 0) {
                EditForwayFrente.setText("" + exhibicionAdicionalB.ForwayFrente);
            } else {
                EditForwayFrente.setText("");
            }

            if (exhibicionAdicionalB.OtrosFrente != 0 || exhibicionAdicionalB.OtrosFrente != null) {
                EditOtroFrente.setText("" + exhibicionAdicionalB.OtrosFrente);
            } else {
                EditOtroFrente.setText("");
            }
        } else {
            EditExhibidor.setText("");
            EditBunker.setText("");
            EditArea.setText("");
            EditTira.setText("");
            EditCaja.setText("");
            EditArete.setText("");
            EditForway.setText("");
            EditOtro.setText("");
            EditComentario.setText("");

            EditCabeceraFrente.setText("");
            EditIslaFrente.setText("");
            EditExhibidorFrente.setText("");
            EditBunkerFrente.setText("");
            EditAreaFrente.setText("");
            EditTiraFrente.setText("");
            EditCajaFrente.setText("");
            EditAreteFrente.setText("");
            EditForwayFrente.setText("");
            EditOtroFrente.setText("");

        }
          
   
    }
    
    public void goBack(){
        Intent ii;
        ii = new Intent(ExhibicionAdicionalActivity.this , ProductoActivity.class);
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

    class OnKeyListener implements TextView.OnEditorActionListener {

        private int Visibility(View view) {
            EditText txtEdit = ((EditText) view);
            if (!txtEdit.getText().toString().isEmpty()
                    && Integer.parseInt(txtEdit.getText().toString()) > 0) {
                return View.VISIBLE;
            } else {
                return View.GONE;
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

            switch (v.getId()) {
                case R.id.EditCabecera:
                    LayoutCabeceraFrente.setVisibility(Visibility(v));
                    EditCabecera.setNextFocusDownId(R.id.EditCabeceraFrentes);
                    EditCabeceraFrente.requestFocus(View.FOCUS_DOWN);
                    break;
                case R.id.EditIsla:
                    LayoutIslaFrente.setVisibility(Visibility(v));

                    break;
                case R.id.EditExhibidor:
                    LayoutExhibidoresFrente.setVisibility(Visibility(v));
                    break;

                case R.id.EditBunker:
                    LayoutBunkerFrente.setVisibility(Visibility(v));
                    break;

                case R.id.EditArea:
                    LayoutAreaFrente.setVisibility(Visibility(v));
                    break;

                case R.id.EditTira:
                    LayoutTiraFrente.setVisibility(Visibility(v));
                    break;

                case R.id.EditCaja:
                    LayoutCajaFrente.setVisibility(Visibility(v));
                    break;

                case R.id.EditArete:
                    LayoutAreteFrente.setVisibility(Visibility(v));
                    break;

                case R.id.EditForway:
                    LayoutForwayFrente.setVisibility(Visibility(v));
                    break;

                case R.id.EditOtro:
                    LayoutOtroFrente.setVisibility(Visibility(v));
                    break;

                default:
                    break;
            }
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.exhibicion_adicional, menu);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_siguiente:
                Siguiente();
            return true;
                
            case R.id.action_cancelar:
                    finish();
            return true;
            case R.id.action_save:
                Guardar();
                goBack();
            return true;
            case R.id.action_foto:
                foto();
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    protected void Guardar() {

        /*Boolean isEmpty = true;

        if (!EditCabecera.getText().toString().isEmpty()) {
            isEmpty = false;
        }
        if (!EditIsla.getText().toString().isEmpty()) {
            isEmpty = false;
        }
        if (!EditExhibidor.getText().toString().isEmpty()) {
            isEmpty = false;
        }
        if (!EditBunker.getText().toString().isEmpty()) {
            isEmpty = false;
        }
        if (!EditArea.getText().toString().isEmpty()) {
            isEmpty = false;
        }
        if (!EditTira.getText().toString().isEmpty()) {
            isEmpty = false;
        }
        if (!EditCaja.getText().toString().isEmpty()) {
            isEmpty = false;
        }
        if (!EditArete.getText().toString().isEmpty()) {
            isEmpty = false;
        }
        if (!EditForway.getText().toString().isEmpty()) {
            isEmpty = false;
        }
        if (!EditOtro.getText().toString().isEmpty()) {
            isEmpty = false;
        }
        if (!EditComentario.getText().toString().isEmpty()) {
            isEmpty = false;
        }

        if (!isEmpty) {*/

            ExhibicionAdicional exhibicionAdicional = new ExhibicionAdicional();
            exhibicionAdicional.IdProyecto = currentProyecto.Id;
            exhibicionAdicional.IdUsuario = currentUsuario.Id;
            exhibicionAdicional.DeterminanteGSP = currentPop.DeterminanteGSP;
            exhibicionAdicional.Sku = currentProducto.SKU;
            exhibicionAdicional.IdVisita = currentPop.IdVisita;
            
            exhibicionAdicional.Cabecera = EditCabecera.getText().toString().isEmpty() ? null : Integer.parseInt(EditCabecera.getText().toString());
            if(exhibicionAdicional.Cabecera!= null){bandera++;}
            exhibicionAdicional.CabeceraFrente = EditCabeceraFrente.getText().toString().isEmpty() ? null : Integer.parseInt(EditCabeceraFrente.getText().toString());
            if(exhibicionAdicional.CabeceraFrente!= null){bandera++;}
            
            exhibicionAdicional.Isla = EditIsla.getText().toString().isEmpty() ? null : Integer.parseInt(EditIsla.getText().toString());
            if(exhibicionAdicional.Isla!= null){bandera++;}
            exhibicionAdicional.IslaFrente = EditIslaFrente.getText().toString().isEmpty() ? null : Integer.parseInt(EditIslaFrente.getText().toString());
            if(exhibicionAdicional.IslaFrente!= null){bandera++;}
            
            exhibicionAdicional.Exhibidor = EditExhibidor.getText().toString().isEmpty() ? null : Integer.parseInt(EditExhibidor.getText().toString());
            if(exhibicionAdicional.Exhibidor!= null){bandera++;}
            exhibicionAdicional.ExhibidorFrente = EditExhibidorFrente.getText().toString().isEmpty() ? null : Integer.parseInt(EditExhibidorFrente.getText().toString());
            if(exhibicionAdicional.ExhibidorFrente!= null){bandera++;}
            
            exhibicionAdicional.Bunker = EditBunker.getText().toString().isEmpty() ? null : Integer.parseInt(EditBunker.getText().toString());
            if(exhibicionAdicional.Bunker!= null){bandera++;}
            exhibicionAdicional.BunkerFrente = EditBunkerFrente.getText().toString().isEmpty() ? null : Integer.parseInt(EditBunkerFrente.getText().toString());
            if(exhibicionAdicional.BunkerFrente!= null){bandera++;}
            
            exhibicionAdicional.Area = EditArea.getText().toString().isEmpty() ? null : Integer.parseInt(EditArea.getText().toString());
            if(exhibicionAdicional.Area!= null){bandera++;}
            exhibicionAdicional.AreaFrente = EditAreaFrente.getText().toString().isEmpty() ? null : Integer.parseInt(EditAreaFrente.getText().toString());
            if(exhibicionAdicional.AreaFrente!= null){bandera++;}
            
            exhibicionAdicional.Tira = EditTira.getText().toString().isEmpty() ? null : Integer.parseInt(EditTira.getText().toString());
            if(exhibicionAdicional.Tira!= null){bandera++;}
            exhibicionAdicional.TiraFrente = EditTiraFrente.getText().toString().isEmpty() ? null : Integer.parseInt(EditTiraFrente.getText().toString());
            if(exhibicionAdicional.TiraFrente!= null){bandera++;}
            
            exhibicionAdicional.Caja = EditCaja.getText().toString().isEmpty() ? null : Integer.parseInt(EditCaja.getText().toString());
            if(exhibicionAdicional.Caja!= null){bandera++;}
            exhibicionAdicional.CajaFrente = EditCajaFrente.getText().toString().isEmpty() ? null : Integer.parseInt(EditCajaFrente.getText().toString());
            if(exhibicionAdicional.CajaFrente!= null){bandera++;}
            
            exhibicionAdicional.Arete = EditArete.getText().toString().isEmpty() ? null : Integer.parseInt(EditArete.getText().toString());
            if(exhibicionAdicional.Arete!= null){bandera++;}
            exhibicionAdicional.AreteFrente = EditAreteFrente.getText().toString().isEmpty() ? null : Integer.parseInt(EditAreteFrente.getText().toString());
            if(exhibicionAdicional.AreteFrente!= null){bandera++;}
            
            exhibicionAdicional.Forway = EditForway.getText().toString().isEmpty() ? null : Integer.parseInt(EditForway.getText().toString());
            if(exhibicionAdicional.Forway!= null){bandera++;}
            exhibicionAdicional.ForwayFrente = EditForwayFrente.getText().toString().isEmpty() ? null : Integer.parseInt(EditForwayFrente.getText().toString());
            if(exhibicionAdicional.ForwayFrente!= null){bandera++;}
            
            exhibicionAdicional.Otros = EditOtro.getText().toString().isEmpty() ? null : Integer.parseInt(EditOtro.getText().toString());
            if(exhibicionAdicional.Otros!= null){bandera++;}
            exhibicionAdicional.Comentario = EditComentario.getText().toString().isEmpty() ? null : EditComentario.getText().toString();
            if(exhibicionAdicional.Comentario!= null){bandera++;}
            
            if(exhibicionAdicionalB!= null){
                exhibicionAdicional.IdFoto = exhibicionAdicionalB.IdFoto;
            }
            
            
            new Save().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, exhibicionAdicional);

      /*  } else {
            Toast.makeText(this, "No es posible registrar la informacion",
                    Toast.LENGTH_LONG).show();

        }*/

    }
    private void foto(){
        Intent i;
        mx.smartteam.entities.Foto  objFoto= new  mx.smartteam.entities.Foto();
        objFoto.Tipo = objFoto.Tipo.Exhibicion;
        i = new Intent(this, CameraActivity.class);
        i.putExtra("usuario", currentUsuario);
        i.putExtra("proyecto", currentProyecto);
        i.putExtra("pop", currentPop);
        i.putExtra("opcionfoto",4);/* solo Exh Adicional */
        i.putExtra("producto", currentProducto);                    
        i.putExtra("foto", objFoto);
        startActivity(i);
        super.onStop();
    }
        
    private void OtroProducto() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle( Html.fromHtml("<font color='#FFFFF'>"+R.string.title_dialog+"</font>"));   
        Drawable d=sucursal.setIconAlert(context);
        alertDialogBuilder.setIcon(d);
        alertDialogBuilder.setMessage("Desea capturar otro producto?")
        .setCancelable(false)
        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent("mx.smartteam.producto");
                intent.putExtra("item", EnumFormulario.exhibiciones_adicionales);
                intent.putExtra("usuario", currentUsuario);
                intent.putExtra("proyecto", currentProyecto);
                intent.putExtra("pop", currentPop);
                startActivity(intent);
                finish();
            }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
 /******************************************************************************/  
    private void Siguiente(){
        if  (productoCollection.size() > 0){
            if(productoCollection.size()==1){
                Guardar();
            }else{
                Guardar();
                for (int i=0;i<productoCollection.size();i++){
                    if(currentProducto.SKU.equals(productoCollection.get(i).SKU)){ 
                        productoCollection.remove(i);
                        ThisElements();
                    }
                    if(productoCollection.size()>0){
                        currentProducto=productoCollection.get(0);
                    }else{
                        goBack();
                        finish();
                    }
                }
                try{
                    exhibicionAdicionalB = mx.smartteam.business.ExhibicionAdicional.GetInfoByVisita(context, currentProyecto, currentUsuario, currentPop, currentProducto);
                }catch(Exception ex){
                    ex.getMessage().toString();
                }
            }
        }else{
            goBack();
            finish();
        }
    }
    public void ThisElements(){
        setContentView(R.layout.activity_exhibicion_adicional);
        this.context = this;
        this.currentUsuario = getIntent().getSerializableExtra("usuario") == null ? null : (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
        this.currentProyecto = getIntent().getSerializableExtra("proyecto") == null ? null : (mx.smartteam.entities.Proyecto) getIntent().getSerializableExtra("proyecto");
        this.currentPop = getIntent().getSerializableExtra("pop") == null ? null : (mx.smartteam.entities.Pop) getIntent().getSerializableExtra("pop");
        this.currentProducto = getIntent().getSerializableExtra("producto") == null ? null : (mx.smartteam.entities.Producto) getIntent().getSerializableExtra("producto");
        try{
            exhibicionAdicionalC = mx.smartteam.business.ExhibicionAdicional.GetInfoByVisita2(context, currentProyecto, currentUsuario, currentPop,productoCollection.get(0).Nombre);
        }catch(Exception ex){
            ex.getMessage();
        }

        if (this.currentUsuario != null && this.currentProyecto != null && this.currentPop != null && this.currentProducto != null && productoCollection.size() > 0) {
            this.setTitle(Html.fromHtml("<small><strong>" + this.productoCollection.get(0).Nombre + "</strong></small>"));
        }

        EditCabecera = (EditText) findViewById(R.id.EditCabecera);
        EditIsla = (EditText) findViewById(R.id.EditIsla);
        EditExhibidor = (EditText) findViewById(R.id.EditExhibidor);
        EditBunker = (EditText) findViewById(R.id.EditBunker);
        EditArea = (EditText) findViewById(R.id.EditArea);
        EditTira = (EditText) findViewById(R.id.EditTira);
        EditCaja = (EditText) findViewById(R.id.EditCaja);
        EditArete = (EditText) findViewById(R.id.EditArete);
        EditForway = (EditText) findViewById(R.id.EditForway);
        EditOtro = (EditText) findViewById(R.id.EditOtro);
        EditComentario = (EditText) findViewById(R.id.EditComentario);

        EditCabeceraFrente = (EditText) findViewById(R.id.EditCabeceraFrentes);
        EditIslaFrente = (EditText) findViewById(R.id.EditIslaFrentes);
        EditExhibidorFrente = (EditText) findViewById(R.id.EditExhibidorFrentes);
        EditBunkerFrente = (EditText) findViewById(R.id.EditBunquerFrentes);
        EditAreaFrente = (EditText) findViewById(R.id.EditAreaFrentes);
        EditTiraFrente = (EditText) findViewById(R.id.EditTiraFrentes);
        EditCajaFrente = (EditText) findViewById(R.id.EditCajaFrentes);
        EditAreteFrente = (EditText) findViewById(R.id.EditAreteFrentes);
        EditForwayFrente = (EditText) findViewById(R.id.EditForwayFrentes);
        EditOtroFrente = (EditText) findViewById(R.id.EditOtroFrentes);

        LayoutCabeceraFrente = (LinearLayout) findViewById(R.id.LayoutCabeceraFrente);
        LayoutCabeceraFrente.setFocusable(true);
        LayoutCabeceraFrente.setFocusableInTouchMode(true);

        LayoutIslaFrente = (LinearLayout) findViewById(R.id.LayoutIslaFrente);
        LayoutExhibidoresFrente = (LinearLayout) findViewById(R.id.LayoutExhibidoresFrente);
        LayoutBunkerFrente = (LinearLayout) findViewById(R.id.LayoutBunkerFrente);
        LayoutAreaFrente = (LinearLayout) findViewById(R.id.LayoutAreaFrente);
        LayoutTiraFrente = (LinearLayout) findViewById(R.id.LayoutTiraFrente);
        LayoutCajaFrente = (LinearLayout) findViewById(R.id.LayoutCajaFrente);
        LayoutAreteFrente = (LinearLayout) findViewById(R.id.LayoutAreteFrente);
        LayoutForwayFrente = (LinearLayout) findViewById(R.id.LayoutForwayFrente);
        LayoutOtroFrente = (LinearLayout) findViewById(R.id.LayoutOtroFrente);

        EditCabecera.setOnEditorActionListener(new OnKeyListener());
        EditIsla.setOnEditorActionListener(new OnKeyListener());
        EditExhibidor.setOnEditorActionListener(new OnKeyListener());
        EditBunker.setOnEditorActionListener(new OnKeyListener());
        EditArea.setOnEditorActionListener(new OnKeyListener());
        EditTira.setOnEditorActionListener(new OnKeyListener());
        EditCaja.setOnEditorActionListener(new OnKeyListener());
        EditArete.setOnEditorActionListener(new OnKeyListener());
        EditForway.setOnEditorActionListener(new OnKeyListener());
        EditOtro.setOnEditorActionListener(new OnKeyListener());
        EditComentario.setOnEditorActionListener(new OnKeyListener());
        
        if(exhibicionAdicionalC==null){
            EditExhibidor.setText("");
            EditBunker .setText("");
            EditArea .setText("");
            EditTira .setText("");
            EditCaja .setText("");
            EditArete .setText("");
            EditForway .setText("");
            EditOtro .setText("");
            EditComentario .setText("");
            
            EditCabeceraFrente .setText("");
            EditIslaFrente .setText("");
            EditExhibidorFrente .setText("");
            EditBunkerFrente .setText("");
            EditAreaFrente .setText("");
            EditTiraFrente .setText("");
            EditCajaFrente .setText("");
            EditAreteFrente .setText("");
            EditForwayFrente  .setText("");
           // EditOtroFrente  .setText("");
        }else{
            if(exhibicionAdicionalC.Sku!=0&&exhibicionAdicionalC.Cabecera!=null){
                if(exhibicionAdicionalC.Cabecera!=0){
                    EditCabecera.setText(""+exhibicionAdicionalC.Cabecera);
                }else{
                    EditCabecera.setText("");
                }
                if(exhibicionAdicionalC.Cabecera!=0){
                    EditCabecera.setText(""+exhibicionAdicionalC.Cabecera);
                }else{
                    EditCabecera.setText("");
                }
                if(exhibicionAdicionalC.Isla!=0){
                    EditIsla.setText(""+exhibicionAdicionalC.Isla);
                }else{
                    EditIsla.setText("");
                }
                if(exhibicionAdicionalC.IslaFrente!=0){
                    EditIslaFrente.setText(""+exhibicionAdicionalC.IslaFrente);
                }else{
                    EditIsla.setText("");
                }
                if(exhibicionAdicionalC.Exhibidor!=0){
                    EditExhibidor.setText(""+exhibicionAdicionalC.Exhibidor);
                }else{
                    EditExhibidor.setText("");
                }
                if(exhibicionAdicionalC.Bunker!=0){
                    EditBunker.setText(""+exhibicionAdicionalC.Bunker);
                }else{
                    EditBunker.setText("");
                }
                if(exhibicionAdicionalC.Area!=0){
                    EditArea.setText(""+exhibicionAdicionalC.Area);
                }else{
                    EditArea.setText("");
                }
                if(exhibicionAdicionalC.Tira!=0){
                    EditTira.setText(""+exhibicionAdicionalC.Tira);
                }else{
                    EditTira.setText("");
                }
                if(exhibicionAdicionalC.Caja!=0){
                    EditCaja.setText(""+exhibicionAdicionalC.Caja);
                }else{
                    EditCaja.setText("");
                }    
                if(exhibicionAdicionalC.Arete!=0){
                    EditArete.setText(""+exhibicionAdicionalC.Arete);
                }else{
                    EditArete.setText("");
                }            
                if(exhibicionAdicionalC.Forway!=0){
                    EditForway.setText(""+exhibicionAdicionalC.Forway);
                }else{
                    EditForway.setText("");
                }       
                if(exhibicionAdicionalC.Otros!=0){
                    EditOtro.setText(""+exhibicionAdicionalC.Otros);
                }else{
                    EditOtro.setText("");
                }
                if(exhibicionAdicionalC.Comentario!=null){
                    EditComentario.setText(""+exhibicionAdicionalC.Comentario);
                }else{
                    EditComentario.setText("");
                }
                if(exhibicionAdicionalC.CabeceraFrente!=0){
                    EditCabeceraFrente.setText(""+exhibicionAdicionalC.CabeceraFrente);
                }else{
                    EditCabeceraFrente.setText("");
                }
                if(exhibicionAdicionalC.IslaFrente!=0){
                    EditIslaFrente.setText(""+exhibicionAdicionalC.IslaFrente);
                }else{
                    EditIsla.setText("");
                }
                if(exhibicionAdicionalC.ExhibidorFrente!=0){
                    EditExhibidorFrente.setText(""+exhibicionAdicionalC.ExhibidorFrente);
                }else{
                EditExhibidorFrente.setText("");
                }
                if(exhibicionAdicionalC.BunkerFrente!=0){
                    EditBunkerFrente.setText(""+exhibicionAdicionalC.BunkerFrente);
                }else{
                    EditBunkerFrente.setText("");
                }
                if(exhibicionAdicionalC.TiraFrente!=0){
                    EditTiraFrente.setText(""+exhibicionAdicionalC.TiraFrente);
                }else{
                    EditTiraFrente.setText("");
                }
                if(exhibicionAdicionalC.CajaFrente!=0){
                    EditCajaFrente.setText(""+exhibicionAdicionalC.CajaFrente);
                }else{
                    EditCajaFrente.setText("");
                }
                if(exhibicionAdicionalC.AreteFrente!=0){
                    EditAreteFrente.setText(""+exhibicionAdicionalC.AreteFrente);
                }else{
                    EditAreteFrente.setText("");
                }    
                if(exhibicionAdicionalC.ForwayFrente!=0){
                    EditForwayFrente.setText(""+exhibicionAdicionalC.ForwayFrente);
                }else{
                    EditForwayFrente.setText("");
                }
                if(exhibicionAdicionalC.OtrosFrente!=0||exhibicionAdicionalC.OtrosFrente!=null){
                    EditOtroFrente.setText(""+exhibicionAdicionalC.OtrosFrente);
                }else{
                    EditOtroFrente.setText("");
                } 
            }
        }
            
        if(productoCollection.size()>0){
            for (int i=0;i<productoCollection.size();i++){
                if(currentProducto.SKU.equals(productoCollection.get(i).SKU)){ 
                    Guardar();
                    productoCollection.remove(i);
                }
                if(productoCollection.size()>0){
                    currentProducto=productoCollection.get(0);
                }else{                    
                    ReDirect();
                }
            }
        }else{
            ReDirect();
        }      
     
        exhibicionAdicionalB=exhibicionAdicionalC;
    }
/******************************************************************************/
    public void ReDirect(){
        Intent intent = new Intent("mx.smartteam.producto");
        intent.putExtra("item", EnumFormulario.exhibiciones_adicionales);
        intent.putExtra("usuario", currentUsuario);
        intent.putExtra("proyecto", currentProyecto);
        intent.putExtra("pop", currentPop);
        startActivity(intent);
        finish();
    }
/******************************************************************************/ 

    class Save extends AsyncTask<ExhibicionAdicional, Void, String> {
        private ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(context);
            pd.setTitle("Registrando informacion...");
            pd.setMessage("Espere por favor.");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

        @Override
        protected String doInBackground(ExhibicionAdicional... params) {
            String result = null;
            try {
                int idfoto= PhotoActivity.idfoto;
                if(idfoto > 0){
                    params[0].IdFoto = idfoto;
                }
                if (exhibicionAdicionalB != null) {
                    params[0].Id = exhibicionAdicionalB.Id;
                    mx.smartteam.business.ExhibicionAdicional.Update(context, params[0]);
                } else {
                    if(bandera>0){
                        mx.smartteam.business.ExhibicionAdicional.Insert(context, params[0]);
                    }
                }
                PhotoActivity.idfoto =0;
                bandera = 0;
                result = "OK";
            } catch (Exception ex) {
                result = "-1";
                Logger.getLogger(BodegaActivity.class.getName()).log(Level.SEVERE, null, ex);
            }
            return result;
        }

        protected void onPostExecute(String resultado) {
            pd.dismiss();
            if (resultado == "OK") {
                if(productoCollection.size()== 1){
                    productoCollection.remove(0);
                }
            } else {
                Toast.makeText(context, "Error al registrar la información", Toast.LENGTH_LONG).show();
            }
        }
    }
}
