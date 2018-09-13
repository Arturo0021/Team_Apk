package mx.triplei;

import android.app.ActionBar;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import mx.triplei.R;

public class CapturaFotoActivity extends Activity {

    Context context;
    private byte[] argBytes;
    Camera c = null;
    private Activity meActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = this;
        meActivity = this;
        setContentView(R.layout.activity_captura_foto);
        ImageButton imgButton = (ImageButton) findViewById(R.id.btnFoto);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                TomarFoto();
            }
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER | ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        final Context ctx = this;
        // new Thread() {
        // public void run() {
        // CapturaFotoActivity.this.runOnUiThread(new Runnable() {
        // public void run() {
        // Do your UI operations like dialog opening or Toast
        // here

    }

    /**
     *
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    protected void onResume() {
        super.onResume();

        Preview mPreview;

        try {
            c = Camera.open(); // attempt to get a Camera
            // instance
            mPreview = new Preview(meActivity, c);

            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(mPreview);
            preview.setHorizontalFadingEdgeEnabled(true);

        } catch (Exception e) {
            Log.d("Camera", "Error starting camera preview: " + e.getMessage());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Sucursal sucursal =new Sucursal();
        Drawable d=sucursal.setIconMenu(context);
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(d);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.itemFoto:
                TomarFoto();
                return true;
            case R.id.itemCancelar:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void TomarFoto() {

        //Camera.Parameters params = c.getParameters();
        //params.setPictureFormat(ImageFormat.JPEG);
        //params.setPreviewFormat(ImageFormat.YV12);
        //params.setPictureSize(160, 120);
        c.takePicture(null, null, myPictureCallback_JPG);

    }
    PictureCallback myPictureCallback_JPG = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] arg0, Camera arg1) {

            //-----------------------------------------------     
            float ang = arg1.getParameters().getHorizontalViewAngle();
            Display display = meActivity.getWindowManager().getDefaultDisplay();

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
            int i = context.getResources().getConfiguration().orientation;

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (i == Configuration.ORIENTATION_PORTRAIT) {
                degrees = 90;
            } else {
                degrees = 0;
            }

            Matrix matrix;
            matrix = new Matrix();
            matrix.postRotate(degrees);

            Bitmap bitmap = BitmapFactory.decodeByteArray(arg0, 0, arg0.length);
            Bitmap bMapScale = Bitmap.createScaledBitmap(bitmap, 480, 600, true);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            //bMapScale.compress(Bitmap.CompressFormat.JPEG, 50, out);
            //byte[] array = out.toByteArray();

            Bitmap bMapRotate = Bitmap.createBitmap(bMapScale, 0, 0,
                    bMapScale.getWidth(), bMapScale.getHeight(), matrix, true);

            bMapRotate.compress(Bitmap.CompressFormat.JPEG, 80, out);

            //ByteBuffer buffer = ByteBuffer.allocate(bMapRotate.getByteCount());
            //bMapRotate.copyPixelsToBuffer(buffer);
            argBytes = out.toByteArray();
            //---------------------------------------------------------

            Bundle bundle = new Bundle();
            bundle.putByteArray("image", argBytes);
            Intent returnResult = new Intent();
            returnResult.putExtras(bundle);

            if (getParent() == null) {
                setResult(RESULT_OK, returnResult);
            } else {
                getParent().setResult(RESULT_OK, getIntent());
            }

            //arg1.stopPreview();
            //arg1.release();
            finish();

        }
    };

    @Override
    protected void onPause() {
        //meActivity.getIntent().putExtra("image", argBytes);
        //meActivity.setResult(RESULT_OK, meActivity.getIntent());
        super.onPause();
    }
}
