package com.mhms.poliprojects.toblindpeopleimersion.Camera;

        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Rect;
        import android.graphics.YuvImage;
        import android.hardware.Camera;
        import android.util.Log;

        import java.io.ByteArrayOutputStream;

public class CameraInitialize {
    private byte[] mdata;
    private Bitmap mBitmap;
    private Camera camera;

    public CameraInitialize(){
        cameraConfiguration();
    }




    public void dataToBitmap() {

        Camera.Parameters parameters = camera.getParameters();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if(mdata!=null) {
            YuvImage yuvImage = new YuvImage(mdata, parameters.getPreviewFormat(), parameters.getPreviewSize().width, parameters.getPreviewSize().height, null);
            yuvImage.compressToJpeg(new Rect(0, 0, parameters.getPreviewSize().width, parameters.getPreviewSize().height), 100, out);
            byte[] imageBytes = out.toByteArray();
            mBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        }

    }



    private void cameraConfiguration(){
        camera = getCameraInstance();
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size size = parameters.getPreviewSize();
        parameters.setPreviewSize(size.width * 2, size.height * 2);
        parameters.setPreviewFrameRate(5);

        camera.setParameters(parameters);
        camera.setPreviewCallback(previewCallback);
        camera.startPreview();


    }
    private android.hardware.Camera getCameraInstance(){
        android.hardware.Camera c = null;
        releaseCameraAndPreview();
        try {

            c = android.hardware.Camera.open(); // attempt to get a Camera instance

        }
        catch (Exception e){

            e.printStackTrace();
            Log.d("toblindpeople", ">>>>>>>>>>>>> Falha ao ligar a camera <<<<<<<<<<<<<<<<<<<");
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable*/
    }private void releaseCameraAndPreview() {

        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            mdata = data;

        }
    };


    public Bitmap getBitmap() {
        dataToBitmap();
        return mBitmap;
    }

    public Camera getCamera() {
        return camera;
    }
}