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
import mx.triplei.R;
import mx.smartteam.entities.PopVisita;

public class FotoAsistenciaActivity extends Activity implements SurfaceHolder.Callback {
    int pickcount=0;
    Context myContext;
    Activity myActivity;
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    boolean previewing = false;
    LayoutInflater controlInflater = null;
    LayoutParams layoutParamsControl = null;
    Button buttonComentario, buttonClasificacion, buttonSubClasificacion;
    private byte[] imageBytes;
    final int RESULT_SAVEIMAGE = 0;
    private Integer optionSel = 0;
    private mx.smartteam.entities.PopVisita popVisita;
    private mx.smartteam.entities.Pop currentPop;
    Sucursal sucursal =new Sucursal();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_asistencia);
        myContext = this;
        myActivity = this;
        this.setTitle(Html.fromHtml("<small><strong>Foto de asistencia</strong></small>"));

        this.currentPop = getIntent().getSerializableExtra("pop") == null ? null : (mx.smartteam.entities.Pop) getIntent().getSerializableExtra("pop");

        if (currentPop != null) {

            this.popVisita = mx.smartteam.data.PopVisita.GetById(myContext, currentPop.IdVisita);
            if (this.popVisita != null) {

                showDialog(0);
                //this.AsistenciaDialog();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                getWindow().setFormat(PixelFormat.JPEG);
                surfaceView = (SurfaceView) findViewById(R.id.camerapreview);
                surfaceHolder = surfaceView.getHolder();
                surfaceHolder.addCallback(this);
                surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_HARDWARE);

                LinearLayout layoutBackground = (LinearLayout) findViewById(R.id.layoutbackground);
                layoutBackground.setOnClickListener(new LinearLayout.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub

                        //buttonTakePicture.setEnabled(false);
                        camera.autoFocus(myAutoFocusCallback);
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.foto_asistencia, menu);
        Sucursal sucursal =new Sucursal();
        Drawable d=sucursal.setIconMenu(this);
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(d);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
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
                return true;
            case R.id.action_save:
                try {
                    Guardar();
                } catch (Exception e) {
                    Toast.makeText(myContext, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.action_cancelar:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void Guardar() throws Exception {

        if (imageBytes == null) {
            throw new Exception("Ud. no ha capturado una imagen verifique por favor");
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle( Html.fromHtml("<font color='#FFFFF'>SmartTeam</font>"));   
        //alertDialogBuilder.setTitle("SmartTeam");
        Drawable d=sucursal.setIconAlert(myContext);
        alertDialogBuilder.setIcon(d);
        alertDialogBuilder
                .setMessage("Dese utilizar la Imagen?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                String encodedImage = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
                //String encodedImage = encodedImage0.replace("\n", "%20");

                Foto entityFoto = new Foto();
                entityFoto.Foto = encodedImage;
                entityFoto.IdVisita = popVisita.Id;
                if (optionSel == 0) {
                    entityFoto.Tipo = Foto.Type.Entrada;
                } else {
                    entityFoto.Tipo = Foto.Type.Salida;
                }


                new Save().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, entityFoto);

            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //CapturarImagen();
                dialog.cancel();
            }
        });

        //create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    class Save extends AsyncTask<Foto, Void, String> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(myContext);
            pd.setTitle("Registrando foto");
            pd.setMessage("Espere por favor.");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

        @Override
        protected String doInBackground(Foto... params) {

            String result = null;
            Foto foto = (Foto) params[0];
            try {
                mx.smartteam.business.Foto.Insert(myContext, foto);
                result = "OK";
            } catch (Exception ex) {
                result = "-1";
                Logger.getLogger(FotoAsistenciaActivity.class.getName()).log(Level.SEVERE, null, ex);
            }
            return result;
        }

        protected void onPostExecute(String resultado) {
            pd.dismiss();
            if (resultado.trim().equals("OK")) {
                Toast.makeText(myContext, "Foto registrada correctamente",
                        Toast.LENGTH_LONG).show();
                finish();

            } else {
                Toast.makeText(myContext, "Error al enviar la foto",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public class OnItemClickListener_Clasificacion implements AdapterView.OnItemClickListener {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        }
    }
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

            Bitmap bMapRotate = Bitmap.createBitmap(bMapScale, 0, 0, bMapScale.getWidth(), bMapScale.getHeight(), matrix, true);

            bMapRotate.compress(Bitmap.CompressFormat.JPEG, 96, out);

            imageBytes = out.toByteArray();

        }
    };


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

                // configure(camera);
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
            Logger.getLogger(FotoAsistenciaActivity.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        camera.startPreview();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        // //camera.stopFaceDetection();
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
        params.setPictureSize(size.width, size.height);
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
}
