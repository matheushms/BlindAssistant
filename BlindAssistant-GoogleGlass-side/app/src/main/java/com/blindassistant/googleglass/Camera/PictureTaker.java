package com.blindassistant.googleglass.Camera;

import android.hardware.Camera;
import android.util.Log;

import com.blindassistant.googleglass.Main.ControladorMain;

public class PictureTaker implements Camera.PictureCallback
{
    private Camera mCam;
   private ControladorMain theApp;

    public PictureTaker(ControladorMain app)
    {
         theApp = app;
    }

    public void takePicture()
    {
        mCam = null;
        releaseCameraAndPreview();
        try {

            mCam = android.hardware.Camera.open(); // attempt to get a Camera instance

        }
        catch (Exception e){
            e.printStackTrace();
            Log.d("toblindpeople", ">>>>>>>>>>>>> Falha ao ligar a camera <<<<<<<<<<<<<<<<<<<");
            // Camera is not available (in use or does not exist)
        }

        if (mCam == null)
        {

            return;
        }

        try
        {

        //    SurfaceView view = MyApp.getPreviewSurface(); // my own fcn
         //   mCam.setPreviewDisplay(view.getHolder());
            mCam.startPreview();
            mCam.takePicture(null, null, this);
        }
        catch (Exception e)
        {
           e.printStackTrace();
        }
    }

    public void onPictureTaken(byte[] data, Camera cam)
    {
    //    theApp.jpegPictureData(data);  // also my own fcn

        Log.d("PictureTaker", "onPictureTaken - jpeg");
//theApp.getBitmap();
        cam.stopPreview();
        cam.release();

        mCam = null;
    }
    private void releaseCameraAndPreview() {

        if (mCam != null) {
           mCam.release();
            mCam = null;
        }
    }
}