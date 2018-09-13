package mx.triplei;

import java.io.IOException;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;

import android.view.Window;
import java.util.List;

public class Preview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Context context = null;
    private Activity activity;
    private static final String CAMERA_PARAM_ORIENTATION = "orientation";
    private static final String CAMERA_PARAM_LANDSCAPE = "landscape";
    private static final String CAMERA_PARAM_PORTRAIT = "portrait";
    protected Camera.Size mPreviewSize;
    protected Camera.Size mPictureSize;

    public Preview(Activity activity, Camera camera) {
        super(activity.getBaseContext());
        mCamera = camera;
        this.activity = activity;
        this.context = activity.getBaseContext();
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();

        setFocusable(true);

        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        //activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR | ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the
        // preview.
        try {
            // mCamera.setDisplayOrientation(90);

            //configure(mCamera);

            //mCamera.setFaceDetectionListener(faceDetectionListener);

            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            //mCamera.startFaceDetection();

        } catch (IOException e) {
            Log.d("Camera", "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        int ori = this.getContext().getResources().getConfiguration().orientation;

        // stop preview before making changes
        try {
            //mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }
        /*
         //Display Orientatation
         android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
         android.hardware.Camera.getCameraInfo(0, info);
         Display display = ((WindowManager) super.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

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
         int result;
         if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
         result = (info.orientation + degrees) % 360;
         result = (360 - result) % 360;  // compensate the mirror
         } else {  // back-facing
         result = (info.orientation - degrees + 360) % 360;
         }
         mCamera.setDisplayOrientation(result);

         */

        // reformatting changes here

        // start preview with new settings
        try {

            Camera.Parameters cameraParams = mCamera.getParameters();
            boolean portrait = isPortrait();
            configureCameraParameters(cameraParams, portrait);

            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d("Camera", "Error starting camera preview: " + e.getMessage());
        }

    }

    public void setCameraDisplayOrientation(Activity activity,
            int cameraId, android.hardware.Camera camera) {

        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
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
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    private void configure(Camera camera) {
        Camera.Parameters params = camera.getParameters();

        // Configure image format. RGB_565 is the most common format.
        /*List<Integer> formats = params.getSupportedPictureFormats();

         if (formats.contains(PixelFormat.RGB_565)) {
         params.setPictureFormat(PixelFormat.RGB_565);
         } else {
         params.setPictureFormat(PixelFormat.JPEG);
         }*/

        // Choose the biggest picture size supported by the hardware
        List<Size> sizes = params.getSupportedPictureSizes();
        Camera.Size size = sizes.get(sizes.size() - 1);

        params.setPictureSize(size.width, size.height);


        //params.setPictureSize(200,200);


        /*List<String> flashModes = params.getSupportedFlashModes();
         if (flashModes.size() > 0) {
         params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
         }*/

        // Action mode take pictures of fast moving objects
        /*List<String> sceneModes = params.getSupportedSceneModes();
         if (sceneModes.contains(Camera.Parameters.SCENE_MODE_ACTION)) {
         params.setSceneMode(Camera.Parameters.SCENE_MODE_ACTION);
         } else {
         params.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
         }*/

        // if you choose FOCUS_MODE_AUTO remember to call autoFocus() on
        // the Camera object before taking a picture 
        //params.setFocusMode(Camera.Parameters.FOCUS_MODE_FIXED);


        /*
         android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
         android.hardware.Camera.getCameraInfo(0, info);
         Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        
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
         int result;
         if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
         result = (info.orientation + degrees) % 360;
         result = (360 - result) % 360;  // compensate the mirror
         } else {  // back-facing
         result = (info.orientation - degrees + 360) % 360;
         }
         mCamera.setDisplayOrientation(result);
         */




        camera.setParameters(params);
    }
    /*
    FaceDetectionListener faceDetectionListener = new FaceDetectionListener() {
        @Override
        public void onFaceDetection(Face[] faces, Camera camera) {

            if (faces.length == 0) {
                //prompt.setText(" No Face Detected! ");
            } else {
                
                Log.i("TEST", "face coordinates = Rect :" + faces[0].rect.flattenToString());
                Log.i("TEST", "face coordinates = Left eye : " + String.valueOf(faces[0].leftEye));
                Log.i("TEST", "face coordinates = Right eye - " + String.valueOf(faces[0].rightEye));
                Log.i("TEST", "face coordinates = Mouth - " + String.valueOf(faces[0].mouth));
            }
        }
    };*/

    protected void configureCameraParameters(Camera.Parameters cameraParams, boolean portrait) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) { // for 2.1 and before
            if (portrait) {
                cameraParams.set(CAMERA_PARAM_ORIENTATION, CAMERA_PARAM_PORTRAIT);
            } else {
                cameraParams.set(CAMERA_PARAM_ORIENTATION, CAMERA_PARAM_LANDSCAPE);
            }
        } else { // for 2.2 and later
            int angle;
            Display display = activity.getWindowManager().getDefaultDisplay();
            switch (display.getRotation()) {
                case Surface.ROTATION_0: // This is display orientation
                    angle = 90; // This is camera orientation
                    break;
                case Surface.ROTATION_90:
                    angle = 0;
                    break;
                case Surface.ROTATION_180:
                    angle = 270;
                    break;
                case Surface.ROTATION_270:
                    angle = 180;
                    break;
                default:
                    angle = 90;
                    break;
            }
            //Log.v(LOG_TAG, "angle: " + angle);
            mCamera.setDisplayOrientation(angle);
        }

        //cameraParams.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
        //cameraParams.setPictureSize(mPictureSize.width, mPictureSize.height);

        Camera.Size size = null;

        //List<Size> mPreviewSize = cameraParams.getSupportedPreviewSizes();
        //size = mPreviewSize.get(mPreviewSize.size() - 1);
        //cameraParams.setPreviewSize(size.width, size.height);

        //List<Size> mPictureSize = cameraParams.getSupportedPictureSizes();
        //size = mPictureSize.get(mPictureSize.size() - 1);
        //cameraParams.setPictureSize(size.width, size.height);




        mCamera.setParameters(cameraParams);
    }

    public boolean isPortrait() {
        return (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
    }
}
