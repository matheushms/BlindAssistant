package com.example.mathe.smarthphone.Utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Util {



    public static double desviopadrao(List<Double> numeros){
        double media = media(numeros);
        double aux=0;
        for(double numero:numeros){
            aux = aux + (double)Math.pow(numero-media,2);
        }
        if(numeros.size() != 0)
            return (double)Math.sqrt(aux/(numeros.size()));
        return 0;


    }

    public static double media(List<Double> numeros){
        double media = 0;
        for(double numero: numeros){
            media = media + numero;
        }

        return media / numeros.size();
    }

    public static byte[] concat(byte[] first, byte[] second){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );

        try{
            outputStream.write( first );
            outputStream.write( second );
            }catch (Exception e){

        }
        byte c[] = outputStream.toByteArray( );
        return c;
    }

    public static byte[] stringToByte(String s){
        return s.getBytes();
    }
    public static String byteToString(byte[] b){
        return new String(b);
    }
    public void saveImage(Context context, Bitmap ImageToSave) {
        String NameOfFolder = "/results";
        String NameOfFile = "/result";
        Context TheThis = context;
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + NameOfFolder;
        String CurrentDateAndTime = getCurrentDateAndTime();
        File dir = new File(file_path);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dir, NameOfFile + CurrentDateAndTime + ".jpg");
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            ImageToSave.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            MakeSureFileWasCreatedThenMakeAvabile(file, TheThis);
            AbleToSave(TheThis);
        } catch (FileNotFoundException e) {
            UnableToSave(TheThis);
        } catch (IOException e) {
            UnableToSave(TheThis);
        }
    }

    private void MakeSureFileWasCreatedThenMakeAvabile(File file, Context TheThis) {
        MediaScannerConnection.scanFile(TheThis,
                new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {

                    }
                });
    }
    private String getCurrentDateAndTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }
    private void UnableToSave(Context TheThis){
        Toast.makeText(TheThis, "Picture cannot to gallery", Toast.LENGTH_SHORT).show();}
    private void AbleToSave(Context TheThis){
        Toast.makeText(TheThis, "Picture saved be saved", Toast.LENGTH_SHORT).show();}


     public static Bitmap getBitmapFromAssets(Context context, String path){
         Drawable d = null;
         try {
             // get input stream
             InputStream ims = context.getAssets().open(path);
             // load image as Drawable
             d = Drawable.createFromStream(ims, null);
             // set image to ImageView

         }
         catch(IOException ex) {
             ex.printStackTrace();
             Log.d("bluetooth","Drawable igual a nulll <<<<<<<<<<<");
         }
        if(d == null)
            Log.d("bluetooth","Drawable igual a nulll <<<<<<<<<<<");


        return drawableToBitmap(d);
    }
    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

}
