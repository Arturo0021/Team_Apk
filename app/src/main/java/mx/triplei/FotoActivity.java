package mx.triplei;

import android.app.ActionBar;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.text.Html;
import android.util.Base64;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.triplei.R.drawable;
import mx.smartteam.entities.Categoria;
import mx.smartteam.entities.CategoriaCollection;
import mx.smartteam.entities.Foto;
import mx.smartteam.entities.Opcion;
import mx.smartteam.entities.OpcionCollection;
import mx.smartteam.settings.ServiceClient;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import mx.triplei.R;
import mx.smartteam.entities.Pop;
import mx.smartteam.entities.Producto;
import mx.smartteam.entities.Proyecto;
import mx.smartteam.entities.Usuario;

public class FotoActivity extends Activity implements SurfaceHolder.Callback {
    int pickcount=0;
    private Usuario currentUsuario;
    private Proyecto currentProyecto;
    private Pop currentPop;
    private Foto objFoto;
    Context myContext;
    Activity myActivity;
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    boolean previewing = false;
    LayoutInflater controlInflater = null;
    LayoutParams layoutParamsControl = null;
    Button /*buttonTakePicture,*/ buttonComentario, buttonClasificacion, buttonSubClasificacion;
    private byte[] imageBytes;
    // TextView prompt;
    final int RESULT_SAVEIMAGE = 0;
    private Categoria.Adapter_Single adapterClasificacion = null;
    private Opcion.Adapter_Multiple adapterSubClasificacion = null;
    private EditText inputComentario = null;
    private ListView listClasificacion = null;
    private ListView listSubClasificacion = null;
    private Dialog.Clasificacion dgClasificacion;
    private Dialog.SubClasificacion dlgSubClasificacion;
    private Dialog.Comentario dlgComentario;
    Sucursal sucursal =new Sucursal();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myContext = this;
        myActivity = this;
        this.setTitle(Html.fromHtml("<small><strong>Foto</strong></small>"));

        getOverflowMenu();
        setContentView(R.layout.activity_foto);


        this.currentUsuario = getIntent().getSerializableExtra("usuario") == null ? null : (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
        this.currentProyecto = getIntent().getSerializableExtra("proyecto") == null ? null : (mx.smartteam.entities.Proyecto) getIntent().getSerializableExtra("proyecto");
        this.currentPop = getIntent().getSerializableExtra("pop") == null ? null : (mx.smartteam.entities.Pop) getIntent().getSerializableExtra("pop");


        if (this.currentUsuario != null && this.currentProyecto != null && this.currentPop != null) {
            try {
                CategoriaCollection categoriaCollection = mx.smartteam.business.Categoria.GetCategoriaFotoByProyecto(myContext, currentProyecto);
                OpcionCollection opcionCollection = mx.smartteam.business.Opcion.GetByProyecto(myContext, currentProyecto);

                dgClasificacion = new Dialog().new Clasificacion(myContext, categoriaCollection);
                dlgSubClasificacion = new Dialog().new SubClasificacion(myContext, opcionCollection);
                dlgComentario = new Dialog().new Comentario(myContext);

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                getWindow().setFormat(PixelFormat.JPEG);
                surfaceView = (SurfaceView) findViewById(R.id.camerapreview);
                surfaceHolder = surfaceView.getHolder();
                surfaceHolder.addCallback(this);
                surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

                LinearLayout layoutBackground = (LinearLayout) findViewById(R.id.layoutbackground);
                layoutBackground.setOnClickListener(new LinearLayout.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub

                        //buttonTakePicture.setEnabled(false);
                        camera.autoFocus(myAutoFocusCallback);
                    }
                });

            } catch (Exception ex) {
                Logger.getLogger(FotoActivity.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.foto, menu);
        Sucursal sucursal =new Sucursal();
        Drawable d=sucursal.setIconMenu(this);
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(d);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_save:
                Guardar();
                break;
            case R.id.action_foto:
                pickcount++;
                if(pickcount==1){
                AudioManager mgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                mgr.setStreamMute(AudioManager.STREAM_SYSTEM, true);
                camera.takePicture(myShutterCallback, myPictureCallback_RAW,
                        myPictureCallback_JPG);
                }else{
                Toast.makeText(myContext, "Ya ha tomado una foto, verique por favor!", Toast.LENGTH_SHORT).show();
                }
                
                break;
            case R.id.action_cancelar:
                finish();
                break;

            case R.id.action_clasificacion:
                dgClasificacion.Show();
                break;

            case R.id.action_subclasificacion:

                dlgSubClasificacion.Show();
                break;

            case R.id.action_comentarios:

                dlgComentario.Show();
                break;

            case R.id.takepicture:
                camera.takePicture(myShutterCallback, myPictureCallback_RAW,
                        myPictureCallback_JPG);
                break;
        }
        return true;
    }

    public class onClick implements View.OnClickListener {

        public void onClick(View arg0) {

            switch (arg0.getId()) {

                case R.id.action_save:

                    Guardar();
                    break;
                case R.id.action_foto:
                    break;
                case R.id.action_cancelar:
                    break;

                case R.id.action_clasificacion:
                    dgClasificacion.Show();
                    break;

                case R.id.action_subclasificacion:

                    dlgSubClasificacion.Show();
                    break;

                case R.id.action_comentarios:

                    dlgComentario.Show();
                    break;

                case R.id.takepicture:
                    camera.takePicture(myShutterCallback, myPictureCallback_RAW,
                            myPictureCallback_JPG);
                    break;
            }


        }
    }

    public class OnItemClickListener_Clasificacion implements AdapterView.OnItemClickListener {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        }
    }

    public class Dialog {

        /**
         *
         */
        public class Clasificacion extends AlertDialog.Builder {

            private CategoriaCollection items;
            private AlertDialog alertDialog;

            public Clasificacion(Context context, CategoriaCollection items) {
                super(context);
                super.setTitle( Html.fromHtml("<font color='#FFFFF'>Clasificación</font>"));   
                //super.setTitle("Clasificación");
                LayoutInflater inflater = LayoutInflater.from(super.getContext());
                this.items = items;


                View viewClasf = inflater.inflate(R.layout.clasificacion_foto, null);
                listClasificacion = (ListView) viewClasf.findViewById(R.id.listClasf);
                listClasificacion.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                adapterClasificacion = new Categoria.Adapter_Single(super.getContext(), items);

                listClasificacion.setAdapter(adapterClasificacion);
                super.setView(viewClasf);

                super.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                super.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                Drawable d=sucursal.setIconAlert(myContext);
                alertDialog.setIcon(d);
                alertDialog = super.create();
            }

            private void Show() {               
                alertDialog.show();
            }
        }

        public class SubClasificacion extends AlertDialog.Builder {

            private OpcionCollection items;
            private AlertDialog alertDialog;

            public SubClasificacion(Context context, OpcionCollection items) {
                super(context);
                //Obtenemos el nombre de la pregunta

                super.setTitle( Html.fromHtml("<font color='#FFFFF'>"+(items.size() > 0 ? items.get(0).Titulo : "Sub Clasificación")+"</font>"));   
                //super.setTitle(items.size() > 0 ? items.get(0).Titulo : "Sub Clasificación");
                this.items = items;
                LayoutInflater inflater = LayoutInflater.from(super.getContext());
                View viewClasf = inflater.inflate(R.layout.clasificacion_foto, null);
                listSubClasificacion = (ListView) viewClasf.findViewById(R.id.listClasf);
                listSubClasificacion.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                //Activity myActi=    (Activity) super.getContext().getSystemService(Context.ACTIVITY_SERVICE);
                adapterSubClasificacion = new Opcion.Adapter_Multiple(super.getContext(), items);
                listSubClasificacion.setAdapter(adapterSubClasificacion);

                super.setView(viewClasf);
                super.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                super.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
//                Drawable d=sucursal.setIconAlert(myContext);
//                alertDialog.setIcon(d);
                alertDialog = super.create();
            }

            private void Show() {                
                alertDialog.show();
            }
        }

        public class Comentario extends AlertDialog.Builder {

            private AlertDialog alertDialog;

            public Comentario(Context context) {
                super(context);
                super.setTitle( Html.fromHtml("<font color='#FFFFF'>Comentario</font>"));   
                //super.setTitle("Comentario");

                inputComentario = new EditText(myContext);

                super.setView(inputComentario);
                super.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = inputComentario.getText().toString().trim();

                    }
                });

                super.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
//                Drawable d=sucursal.setIconAlert(myContext);
//                alertDialog.setIcon(d);
                alertDialog = super.create();
            }

            private void Show() {                
                alertDialog.show();
            }
        }
    }
    /*
     FaceDetectionListener faceDetectionListener = new FaceDetectionListener() {
     @Override
     public void onFaceDetection(Face[] faces, Camera camera) {

     if (faces.length == 0) {

     } else {

     }

     }
     };*/
    AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean arg0, Camera arg1) {
            // TODO Auto-generated method stub
            //buttonTakePicture.setEnabled(true);
        }
    };
    ShutterCallback myShutterCallback = new ShutterCallback() {
        @Override
        public void onShutter() {
            // TODO Auto-generated method stub
        }
    };
    PictureCallback myPictureCallback_RAW = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] arg0, Camera arg1) {
            // TODO Auto-generated method stub
        }
    };
    PictureCallback myPictureCallback_JPG = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] arg0, Camera arg1) {
            // TODO Auto-generated method stub
			/*
             * Bitmap bitmapPicture = BitmapFactory.decodeByteArray(arg0, 0,
             * arg0.length);
             */

            /* Uri uriTarget = getContentResolver().insert(
             Media.EXTERNAL_CONTENT_URI, new ContentValues());

             OutputStream imageFileOS;
             try {
             imageFileOS = getContentResolver().openOutputStream(uriTarget);
             imageFileOS.write(arg0);
             imageFileOS.flush();
             imageFileOS.close();

             // prompt.setText("Image saved: " + uriTarget.toString());

             } catch (FileNotFoundException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
             } catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
             }

             camera.startPreview();
             // camera.startFaceDetection();*/



            //-----------------------------------------------     
            float ang = arg1.getParameters().getHorizontalViewAngle();
            Display display = myActivity.getWindowManager().getDefaultDisplay();



            int degrees = 0;

            switch (display.getRotation()) {
                case Surface.ROTATION_0:
                    //params.setPictureSize(120, 160);
                    degrees = 0;
                    break;
                case Surface.ROTATION_90:
                    //params.setPictureSize(160, 120);
                    degrees = 90;
                    break;
                case Surface.ROTATION_180:
                    degrees = 180;
                    break;
                case Surface.ROTATION_270:
                    degrees = 270;
                    break;
            }
            int i = myContext.getResources().getConfiguration().orientation;

            LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (i == Configuration.ORIENTATION_PORTRAIT) {
                degrees = 90;
            } else {
                degrees = 0;
            }



            Matrix matrix;
            matrix = new Matrix();
            matrix.postRotate(degrees);


            Bitmap bitmap = BitmapFactory.decodeByteArray(arg0, 0, arg0.length);
            Bitmap bMapScale = Bitmap.createScaledBitmap(bitmap, 420, 380, true);



            ByteArrayOutputStream out = new ByteArrayOutputStream();
            //bMapScale.compress(Bitmap.CompressFormat.JPEG, 50, out);
            //byte[] array = out.toByteArray();








            Bitmap bMapRotate = Bitmap.createBitmap(bMapScale, 0, 0,
                    bMapScale.getWidth(), bMapScale.getHeight(), matrix, true);

            bMapRotate.compress(Bitmap.CompressFormat.JPEG, 96, out);

            //ByteBuffer buffer = ByteBuffer.allocate(bMapRotate.getByteCount());
            //bMapRotate.copyPixelsToBuffer(buffer);






            imageBytes = out.toByteArray();








            //---------------------------------------------------------



            //arg1.stopPreview();
            //arg1.release();


        }
    };

    private void Guardar() {

        if (imageBytes != null) {

            String encodedImage = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
            Foto objFoto = new Foto();
            objFoto.IdVisita = currentPop.IdVisita;
            objFoto.Foto = encodedImage;
            objFoto.Tipo = Foto.Type.Foto;
            objFoto.Comentario = inputComentario.getText().toString();
            


            //Obtenemos la categoria capturada
            Categoria categoria = GetSelectClasificacion();
            if (categoria != null) {
                objFoto.IdCategoria = categoria.Id;
            }

            //Obtenemos las opciones marcada
            OpcionCollection opcionCollection = GetSelectSubClasificacion();

            new Save().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, objFoto, opcionCollection);

        } else {
            Toast.makeText(myContext, "Ud. no ha capturado la imagen, verifique por favor", Toast.LENGTH_LONG).show();
        }
    }

    private mx.smartteam.entities.Categoria GetSelectClasificacion() {
        Categoria catSel = null;

        if (listClasificacion != null) {
            for (Long l : listClasificacion.getCheckItemIds()) {
                catSel = adapterClasificacion.getItem(l.hashCode());
                if (catSel != null) {
                    return catSel;

                }
            }
        }
        return catSel;
    }

    private OpcionCollection GetSelectSubClasificacion() {


        Opcion opcSelects = null;
        OpcionCollection opcionCollection = new OpcionCollection();
        String sep = "";

        if (listSubClasificacion != null) {
            for (Long l : listSubClasificacion.getCheckItemIds()) {
                opcSelects = adapterSubClasificacion.getItem(l.hashCode());
                if (opcSelects != null) {
                    opcionCollection.add(opcSelects);
                }
            }
        }
        return opcionCollection;
    }

    class Save extends AsyncTask<Object, Void, Integer> {

        private Object[] parameters;
        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(myContext);
            pd.setTitle("Enviando imagen");
            pd.setMessage("Espere por favor.");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();

        }

        @Override
        protected Integer doInBackground(Object... params) {
            // TODO Auto-generated method stub
            Integer result=0;
            
             result= mx.smartteam.data.Foto.Insert(myContext, (Foto) params[0]);
            if (result != null) {
                OpcionCollection opciones = (OpcionCollection) params[1];
                for (Opcion opcion : opciones) {
                    try {
                        mx.smartteam.business.Foto.Opcion.Insert(myContext, result, opcion);
                    } catch (Exception ex) {
                        Logger.getLogger(FotoActivity.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }

            return result;

        }

        protected void onPostExecute(Integer resultado) {
            pd.dismiss();
            if (resultado != null && resultado.equals(0)) {
                
                Toast.makeText(myContext, "Error al enviar la foto",
                        Toast.LENGTH_LONG).show();
                
            } else {
                
                Toast.makeText(myContext, "Fotografia enviada correctamente",
                        Toast.LENGTH_LONG).show();
                finish();

            }

        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        // TODO Auto-generated method stub
        if (previewing) {
            // camera.stopFaceDetection();
            // camera.stopPreview();
            previewing = false;
        }

        if (camera != null) {
            try {

                configure(camera);
                // camera.setPreviewDisplay(surfaceHolder);
                // camera.startPreview();

                // prompt.setText(String.valueOf("Max Face: "
                // + camera.getParameters().getMaxNumDetectedFaces()));
                // camera.startFaceDetection();
                previewing = true;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        camera = Camera.open();
        configure(camera);
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException ex) {
            Logger.getLogger(FotoActivity.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        camera.startPreview();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        // camera.stopFaceDetection();
        camera.stopPreview();
        camera.release();
        camera = null;
        previewing = false;
    }

    private void configure(Camera camera) {
        Camera.Parameters params = camera.getParameters();

        // Configure image format. RGB_565 is the most common format.
		/*
         * List<Integer> formats = params.getSupportedPictureFormats();
         * 
         * if (formats.contains(PixelFormat.RGB_565)) {
         * params.setPictureFormat(PixelFormat.RGB_565); } else {
         * params.setPictureFormat(PixelFormat.JPEG); }
         */

        // Choose the biggest picture size supported by the hardware
        List<Camera.Size> sizes = params.getSupportedPictureSizes();
        Camera.Size size = sizes.get(sizes.size() - 1);
        //params.setPictureSize(size.width, size.height);
        //params.setPictureSize(640, 480);
		/*
         * List<String> flashModes = params.getSupportedFlashModes(); if
         * (flashModes.size() > 0) {
         * params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO); }
         */

        // Action mode take pictures of fast moving objects
		/*
         * List<String> sceneModes = params.getSupportedSceneModes(); if
         * (sceneModes.contains(Camera.Parameters.SCENE_MODE_ACTION)) {
         * params.setSceneMode(Camera.Parameters.SCENE_MODE_ACTION); } else {
         * params.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO); }
         */
        // if you choose FOCUS_MODE_AUTO remember to call autoFocus() on
        // the Camera object before taking a picture
        // params.setFocusMode(Camera.Parameters.FOCUS_MODE_FIXED);

        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(0, info);
        Display display = ((WindowManager) getBaseContext().getSystemService(
                Context.WINDOW_SERVICE)).getDefaultDisplay();

        int degrees = 0;

        switch (display.getRotation()) {
            case Surface.ROTATION_0:
                // params.setPictureSize(120, 160);
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                // params.setPictureSize(160, 120);
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);

        camera.setParameters(params);
    }
    public void MuteFoto(){
       AudioManager mgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                mgr.setStreamMute(AudioManager.STREAM_SYSTEM, true);
    
    } 
}
