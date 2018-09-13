package mx.triplei;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import java.lang.reflect.Field;
import mx.triplei.R;
import mx.smartteam.entities.Categoria;
import mx.smartteam.entities.EnumFormulario;
import mx.smartteam.entities.ExhibicionConfig;
import mx.smartteam.entities.Foto;
import mx.smartteam.entities.Marca;
import mx.smartteam.entities.Opcion;
import mx.smartteam.entities.Pop;
import mx.smartteam.entities.Producto;
import mx.smartteam.entities.Proyecto;
import mx.smartteam.entities.Sondeo;
import mx.smartteam.entities.SondeoModulos;
import mx.smartteam.entities.Usuario;
import mx.smartteam.fragment.CameraFragment;
import mx.smartteam.listener.CameraFragmentListener;

public class CameraActivity extends Activity implements CameraFragmentListener {

    public static final String TAG = "Team/CameraActivity";
    private Usuario currentUsuario;
    private Proyecto currentProyecto;
    private Pop currentPop;
    private Categoria currentCategoria;
    private Marca currentMarca;
    private Producto currentProducto;
    private static final int PICTURE_QUALITY = 90;
    public int opcionfoto;
    public String Tipo,tpoFoto;
    StorageManager mstorage;
    private Integer intopcion = 0;
    private Integer fentrada = 0;
    private SondeoModulos currentSondeo = null;
    private SondeoModulos currentSondeo2;
    private Integer idCategoria,Categoriaid, Marcaid, tiposondeo;
    private Long SKU;
    private EnumFormulario currentFormulario;
    private Foto currentFoto;
    private String tipoform;
    private Opcion currenOpcion = null;
    private int p1, p2;
    private ExhibicionConfig exhibicionConfig ;
            
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);/*Esta linea tiene que ir en la segunda linea*/
        super.onCreate(savedInstanceState);
        getOverflowMenu();
        setContentView(R.layout.activity_camera);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Bundle bundle = getIntent().getExtras();
        /* 1 = Asistencia ,  2 = Foto, 3 = Sondeo , 4 = Modulo  */
        opcionfoto = bundle.get("opcionfoto") == null ? 0 : (Integer) bundle.get("opcionfoto");
        fentrada = bundle.get("fentrada") == null ? 0 : (Integer) bundle.get("fentrada");
        tiposondeo  = bundle.get("tiposondeo") == null ? 0 : (Integer) bundle.get("tiposondeo");
        this.tipoform = (String) bundle.get("tipo") == null ? null : (String) bundle.get("tipo");
        /* Nos traemos los objetos genericos */
        this.currentPop = getIntent().getSerializableExtra("pop") == null ? null : (mx.smartteam.entities.Pop) getIntent().getSerializableExtra("pop");
        this.currentUsuario = getIntent().getSerializableExtra("usuario") == null ? null : (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
        this.currentProyecto = getIntent().getSerializableExtra("proyecto") == null ? null : (mx.smartteam.entities.Proyecto) getIntent().getSerializableExtra("proyecto");
        this.currentProducto = getIntent().getSerializableExtra("producto") == null ? null : (mx.smartteam.entities.Producto) getIntent().getSerializableExtra("producto");
        this.currentCategoria = getIntent().getSerializableExtra("categoria") == null ? null : (mx.smartteam.entities.Categoria) getIntent().getSerializableExtra("categoria");
        this.currentMarca = getIntent().getSerializableExtra("marca") == null ? null :(mx.smartteam.entities.Marca) getIntent().getSerializableExtra("marca");
        this.currentFormulario = bundle.get("item") == null ? null : (EnumFormulario) bundle.get("item");
        this.currentFoto = getIntent().getSerializableExtra("foto") == null ? null : (mx.smartteam.entities.Foto) getIntent().getSerializableExtra("foto");
        //this.currentSondeo = getIntent().getSerializableExtra("sondeo") == null ? null : (mx.smartteam.entities.Sondeo) getIntent().getSerializableExtra("sondeo");
        this.currentSondeo = getIntent().getSerializableExtra("sondeo") == null ? null : (mx.smartteam.entities.SondeoModulos) getIntent().getSerializableExtra("sondeo");
        this.currenOpcion = getIntent().getSerializableExtra("opcion") == null ? null : (mx.smartteam.entities.Opcion) getIntent().getSerializableExtra("opcion");
        this.exhibicionConfig = getIntent().getSerializableExtra("exhibicionconfig") == null ? null :(mx.smartteam.entities.ExhibicionConfig) getIntent().getSerializableExtra("exhibicionconfig");
        
        this.p1 = bundle.get("p1") == null ? 0 : (Integer) bundle.get("p1");
        this.p2 = bundle.get("p2") == null ? 0 : (Integer) bundle.get("p2");
        //
        switch(opcionfoto){
            case 1: /* Asistencia */ TipoMensaje(fentrada); break;
            case 2: /*   FOTOS    */ TipoMensaje(4); break;
            case 3: /*  Sondeos   */  TipoMensaje(5); break;
            case 4: /* Exhibicion Adicional */ TipoMensaje(6); break;
            case 5: /* Material Promocional */ TipoMensaje(7); break;
            case 6: /*      Bodega          */ TipoMensaje(8); break; 
            case 7: /*      Precios         */ TipoMensaje(9); break; 
            case 8: /*      SOS             */ TipoMensaje(10); break; 
            case 9: /*   Foto x producto    */ TipoMensaje(11); break; 
            case 10: /*   Foto Anaquel      */ TipoMensaje(12); break; 
            case 11: /*   Foto Adicional    */ TipoMensaje(13); break; 
            case 12: /*         Anaquel     */ TipoMensaje(14); break;   
            case 13:/* Sod */ TipoMensaje(15); break;
            default:
                TipoMensaje(0);
                finish(); 
            break;
        }
        
        Tipo = bundle.getString("tipo");
        tpoFoto = bundle.getString("tpoFoto");
        intopcion = bundle.get("intopcion") == null ? 0 : (Integer) bundle.get("intopcion"); //  intopcion
        this.currentSondeo2 = getIntent().getSerializableExtra("sondeo") == null ? null : (mx.smartteam.entities.SondeoModulos) getIntent().getSerializableExtra("sondeo");

        File mediaStorageDir = new File("/mnt/sdcard/Pictures/Team/");
        Utilerias.deleteDirectory(mediaStorageDir);
    }
    
    private void TipoMensaje(int tipo){
        String mensaje = "";
        switch(tipo){
            case 0: mensaje = "Tipo de Foto no Identicicada"; finish(); break;
            case 1: mensaje = "FOTO DE ENTRADA"; break;
            case 2: mensaje = "FOTO DE SALIDA"; break;
            case 3: mensaje = "ASISTENCIA"; break;
            case 4: mensaje = "FOTOS"; break;
            case 5: mensaje = "SONDEO " + currentSondeo.Nombre; break;
            case 6: mensaje = "Exh. Adicional";break;
            case 7: mensaje = "Mat. Promo";break;
            case 8: mensaje = "BODEGA";break;
            case 9: mensaje = "PRECIOS";break;
            case 10: mensaje = "SOS";break;
            case 11: mensaje = "Foto XP";break;
            case 12: mensaje = "Foto Anaquel";break;
            case 13: mensaje = "Foto Adicional";break; 
            case 14: mensaje = "ANAQUEL";break; 
            case 15: mensaje = "SOD"; break;
        }
        
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
    
    private void getOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCameraError() {
        Toast.makeText(this, getString(R.string.toast_error_camera_preview),Toast.LENGTH_SHORT);

    }

    /**
     * The user wants to take a picture.
     *
     * @param view
     */
    public void takePicture(View view){
        view.setEnabled(false);
        CameraFragment fragment = (CameraFragment) getFragmentManager().findFragmentById(R.id.camera_fragment);
        fragment.takePicture();
    }
 
    public void onPictureTaken(Bitmap bitmap){
        try {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),getString(R.string.app_name));
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    showSavingPictureErrorToast();
                    return;
                }
            }

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "TEAM_" + timeStamp + ".jpg");
            FileOutputStream stream = new FileOutputStream(mediaFile);
            bitmap.compress(CompressFormat.JPEG, PICTURE_QUALITY, stream);
            MediaScannerConnection.scanFile(this,new String[]{mediaFile.toString()},new String[]{"image/jpeg"},null);
            String ruta;
            ruta = mediaFile.toString();
            
            Intent i = null;
            if(opcionfoto == 1 ){ /* Asistencia  */
                i = new Intent(this, PickAsisActivity.class);
            }else{
                i = new Intent(this, PhotoActivity.class);
            }
            
            switch(opcionfoto){
                case 1: /*  Asistencia  */
                    i.putExtra("fentrada",fentrada);
                break;
                case 2: /* Fotos  */
                    i.putExtra("usuario", currentUsuario);
                    i.putExtra("proyecto", currentProyecto);
                break;
                case 3: /*  Sondeos  */ 
                    
                    switch(tiposondeo){
                        case 8:
                            i.putExtra("sondeo", currentSondeo);
                            i.putExtra("usuario", currentUsuario);
                            i.putExtra("proyecto", currentProyecto);
                            i.putExtra("pop", currentPop);
                            i.putExtra("foto",currentFoto);
                            i.putExtra("tiposondeo", tiposondeo);
                        break;
                        
                        case 6:
                            i.putExtra("sondeo", currentSondeo);
                            i.putExtra("usuario", currentUsuario);
                            i.putExtra("proyecto", currentProyecto);
                            i.putExtra("pop", currentPop);
                            i.putExtra("foto",currentFoto);
                            i.putExtra("tiposondeo", tiposondeo);
                        break;
                            
                        case 5 : 
                            i.putExtra("sondeo", currentSondeo);
                            i.putExtra("usuario", currentUsuario);
                            i.putExtra("proyecto", currentProyecto);
                            i.putExtra("pop", currentPop);
                            i.putExtra("foto",currentFoto);
                            i.putExtra("tiposondeo", tiposondeo);
                            i.putExtra("p1", p1);
                            i.putExtra("p2", p2);
                            
                            
                            
                            break;
                        case 4 : 
                            i.putExtra("sondeo", currentSondeo);
                            i.putExtra("usuario", currentUsuario);
                            i.putExtra("proyecto", currentProyecto);
                            i.putExtra("pop", currentPop);
                            i.putExtra("tiposondeo", tiposondeo);
                            break;
                        case 3 : 
                            i.putExtra("sondeo", currentSondeo);
                            i.putExtra("usuario", currentUsuario);
                            i.putExtra("proyecto", currentProyecto);
                            i.putExtra("pop", currentPop);
                            i.putExtra("tiposondeo", tiposondeo);
                            i.putExtra("opcion", currenOpcion );
                        break;
                            
                        case 2 :  
                            i.putExtra("sondeo", currentSondeo);
                            i.putExtra("usuario", currentUsuario);
                            i.putExtra("proyecto", currentProyecto);
                            i.putExtra("pop", currentPop);
                            i.putExtra("tiposondeo", tiposondeo);
                            if(currentMarca!= null){
                                i.putExtra("marca", currentMarca);
                            }
                            break;

                        case 1 : 
                            i.putExtra("sondeo", currentSondeo);
                            i.putExtra("usuario", currentUsuario);
                            i.putExtra("proyecto", currentProyecto);
                            i.putExtra("pop", currentPop);
                            i.putExtra("producto", currentProducto);
                            i.putExtra("tiposondeo", tiposondeo);
                        break;
                            
                        case 0 : 
                            i.putExtra("sondeo", currentSondeo);
                            i.putExtra("usuario", currentUsuario);
                            i.putExtra("proyecto", currentProyecto);
                            i.putExtra("pop", currentPop);
                            i.putExtra("tiposondeo", tiposondeo);
                        break;
                    }
                break;
            
                case 4: /* Exh Adiconal  */
                    i.putExtra("usuario", currentUsuario);
                    i.putExtra("proyecto", currentProyecto);
                    i.putExtra("producto", currentProducto);
                break;
                    
                case 5: /* Material Promocional */ 
                    i.putExtra("usuario", currentUsuario);
                    i.putExtra("proyecto", currentProyecto);
                    i.putExtra("producto", currentProducto);
                break;
                
                case 6: /*  Bodega*/
                    i.putExtra("usuario", currentUsuario);
                    i.putExtra("proyecto", currentProyecto);
                    i.putExtra("producto", currentProducto);
                break;
                
                case 7: /*  Precios */
                    i.putExtra("usuario", currentUsuario);
                    i.putExtra("proyecto", currentProyecto);
                    i.putExtra("producto", currentProducto);
                break;
                    
                case 8: /*  SOS */
                    i.putExtra("usuario", currentUsuario);
                    i.putExtra("proyecto", currentProyecto);
                    i.putExtra("categoria", currentCategoria);
                    i.putExtra("marca", currentMarca);
                break;
                
                case 9: /*  Foto XP */
                    i.putExtra("usuario", currentUsuario);
                    i.putExtra("proyecto", currentProyecto);
                    i.putExtra("categoria", currentCategoria);
                    i.putExtra("producto", currentProducto);
                    i.putExtra("item", currentFormulario);
                    i.putExtra("tipo", tipoform);
                    i.putExtra("marca", currentMarca);
                break;
 
                case 10: /*  Foto Anaquel */
                    i.putExtra("usuario", currentUsuario);
                    i.putExtra("proyecto", currentProyecto);
                    i.putExtra("categoria", currentCategoria);
                    i.putExtra("item", currentFormulario);
                    i.putExtra("marca", currentMarca);
                break;
                     
                case 11: /*  Foto Adicional */
                    i.putExtra("usuario", currentUsuario);
                    i.putExtra("proyecto", currentProyecto);
                    i.putExtra("categoria", currentCategoria);
                    i.putExtra("foto", currentFoto);
                    i.putExtra("item", currentFormulario);
                break;  
                    
                case 12: /*  Foto Adicional */
                    i.putExtra("usuario", currentUsuario);
                    i.putExtra("proyecto", currentProyecto);
                    i.putExtra("producto", currentProducto);
                break;  
                    
                    
                case 13: /*  Foto SOD   */
                     i.putExtra("usuario", currentUsuario);
                     i.putExtra("proyecto", currentProyecto);
                     i.putExtra("exhibicionconfig", exhibicionConfig);
                break;
                
            }
            
            /*  Completamos la lista de parametros enviados */
            
            i.putExtra("opcionfoto",opcionfoto);
            i.putExtra("pop", currentPop);
            i.putExtra("ruta", ruta);
            i.setData(Uri.fromFile(mediaFile));
            startActivity(i); /* Nos vamos a la otra actividad */ 
            finish();

        } catch (IOException exception) {
            showSavingPictureErrorToast();
            Log.w(TAG, "IOException during saving bitmap", exception);
            return;
        }
    }

    private void showSavingPictureErrorToast() {
        Toast.makeText(this, getText(R.string.toast_error_save_picture), Toast.LENGTH_SHORT).show();
    }
}
