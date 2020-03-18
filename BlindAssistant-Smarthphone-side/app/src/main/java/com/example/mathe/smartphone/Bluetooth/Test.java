package com.example.mathe.smarthphone.Bluetooth;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.util.Log;

import com.example.mathe.smarthphone.Constants;
import com.example.mathe.smarthphone.ObjectRecognition.RecognizeObjects;
import com.example.mathe.smarthphone.TextRecognition.TextRecognition;
import com.example.mathe.smarthphone.Utils.Util;

import org.tensorflow.demo.Classifier;

import java.util.ArrayList;
import java.util.List;

public class Test {


    private int count = 0;
    private List<Double> listTimes = new ArrayList<>();
    private int count2 = 0;
    private List<Double> listTimes2 = new ArrayList<>();
    private List<Double> resolution = new ArrayList<>();
    private Context context;
    private RecognizeObjects recognize;
    TextRecognition recognition = new TextRecognition();
    public Test(Context context){
        this.recognize =  new RecognizeObjects(context.getAssets(), context);
        this.context = context;
    }
    public void test(){
        int n = 30;
        for(int i = 0; i < n; i++){
           processImage( Util.getBitmapFromAssets(context,"img"+i+".jpg") );
        }

    }
    private void processImage(Bitmap bitmap) {
        double time2 = 0;
        bitmap =Bitmap.createScaledBitmap(
        bitmap,300,300,false);
        String result = "";
        long inTime = System.currentTimeMillis();
        result =recognize.recognizeObjects(bitmap);
        long finalTime = System.currentTimeMillis();
        double time = ((double) (finalTime - inTime)) / (double) 1000;
                        listTimes.add(time);
        count++;

        List<Classifier.Recognition> resultadosAprovados = recognize.recognizeObjects2(bitmap);

        //Teste
        bitmap =Bitmap.createScaledBitmap(
        bitmap,1280,720,false);
        for(Classifier.Recognition object :resultadosAprovados){
                inTime = System.currentTimeMillis();
                ///

                recognition.textrecognition(cropImage(object.getLocation(), bitmap), context);
                ///
                finalTime = System.currentTimeMillis();
                time2 = ((double) (finalTime - inTime)) / (double) 1000;
                if(count2 > 30)
                    listTimes2.add(time2);
                resolution.add((double) object.getLocation().width() * object.getLocation().height());
                count2++;
        }


    //Teste


    double resolutionAverage = Math.sqrt(Util.media(resolution));



                    Log.d("bluetooth","OBJECT RECOGNITION -> COUNT: "+count+" TIME: "+time+" AVERAGE: "+Util.media(listTimes)
            +" STANDARD DEVIATION: "+Util.desviopadrao(listTimes));
                    Log.d("bluetooth","TEXT RECOGNITION -> COUNT: "+count2+" TIME: "+time2+" AVERAGE: "+Util.media(listTimes2)
            +" STANDARD DEVIATION: "+Util.desviopadrao(listTimes2)+" AVERAGE RESOLUTION: "+resolutionAverage+"x"+resolutionAverage);
                        Log.d("bluetooth","RESULTADO>>>> "+result);

}


    public Bitmap cropImage(RectF objectRecognition, Bitmap bitmap){
        int left,top,width,height;
        float razaoHeight = bitmap.getHeight()/(float) Constants.INPUT_SIZE;
        float razaoWidth = bitmap.getWidth()/(float)Constants.INPUT_SIZE;
        // Log.d(TAG,"crop image>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+ razaoHeight+ " " + razaoWidth+ " " + bitmap.getHeight()+ " "+bitmap.getWidth()+"<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

        left = (int)(objectRecognition.left*razaoWidth);
        top = (int)(objectRecognition.top*razaoHeight);
        width = (int)(objectRecognition.width()*razaoWidth);
        height = (int)(objectRecognition.height()*razaoHeight);
        return Bitmap.createBitmap(bitmap, left,top,width,height);

    }
}
