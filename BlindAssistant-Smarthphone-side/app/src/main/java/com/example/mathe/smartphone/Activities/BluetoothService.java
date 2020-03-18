package com.example.mathe.smartphone.Activities;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.example.mathe.smartphone.Bluetooth.BluetoothController;
import com.example.mathe.smartphone.Bluetooth.ClientClass;
import com.example.mathe.smartphone.Constants;
import com.example.mathe.smartphone.ObjectRecognition.RecognizeObjects;
import com.example.mathe.smartphone.Speak;
import com.example.mathe.smartphone.TextRecognition.TextRecognition;
import com.example.mathe.smartphone.Utils.Util;

import org.tensorflow.demo.Classifier;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BluetoothService extends Service {
    private AssetManager assets;
    private RecognizeObjects recognize;
    TextToSpeech tts;

Bitmap bitmap;
    BluetoothController bluetoothController;


    private int count = 0;
    private List<Double> listTimes = new ArrayList<>();
    private int count2 = 0;
    private List<Double> listTimes2 = new ArrayList<>();
    private List<Double> resolution = new ArrayList<>();

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();




        //   BluetoothConnect bluetoothConnect = new BluetoothConnect(adress, this,data);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       String adress;

if(intent != null) {
    adress = intent.getStringExtra("adress");
    tts = (new Speak()).speak(this);
    Handler mHandler = createHandler();
    bluetoothController = new BluetoothController(this, adress);
    recognize = new RecognizeObjects(getAssets(), getApplicationContext());

}
        return super.onStartCommand(intent, flags, startId);

    }
    private Handler createHandler(){
        @SuppressLint("HandlerLeak") Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                byte[] readBuff = (byte[]) msg.obj;
                byte operation = readBuff[0];
                byte keyTimer = 0;


                Log.d("bluetooth", "o tamanho da imagem é:" + readBuff.length);
                String text;
                readBuff = Arrays.copyOfRange(readBuff, 1, readBuff.length);
                msg.arg1 = readBuff.length;
                assets = getAssets();
                bitmap = BitmapFactory.decodeByteArray(readBuff, 0, msg.arg1);

                //   Util util= new Util();
                //   util.saveImage(getApplicationContext(),bitmap);
                if(operation == Constants.OPERATION_OBJECT_RECOGNITION ){
                    bitmap = Bitmap.createScaledBitmap(
                            bitmap, 300, 300, false);
                    String result = "";
                    long inTime = System.currentTimeMillis();
                        result = recognize.recognizeObjects(bitmap);
                    long finalTime = System.currentTimeMillis();
                    double time = ((double)(finalTime - inTime))/(double)1000 ;
                    listTimes.add(time);
                    count++;

                    List<Classifier.Recognition> resultadosAprovados = recognize.recognizeObjects2(bitmap);

                    //Teste
                    bitmap = Bitmap.createScaledBitmap(
                            bitmap, 1280, 720, false);
                    TextRecognition recognition = new TextRecognition();
                    for (Classifier.Recognition object : resultadosAprovados) {
                        inTime = System.currentTimeMillis();
                        ///

                        recognition.textrecognition(cropImage(object.getLocation(),bitmap),getApplicationContext());
                        ///
                        finalTime= System.currentTimeMillis();
                        time = ((double)(finalTime - inTime))/(double)1000 ;
                        listTimes2.add(time);
                        resolution.add((double)object.getLocation().width()*object.getLocation().height());
                        count2++;
                    }


                    //Teste


                    double resolutionAverage =Math.sqrt(Util.media(resolution));

                        byte[] datam = Util.stringToByte(result);
                        datam = ClientClass.createHeader(operation,datam);
                        try{
                            OutputStream outputStream = bluetoothController.getSocket().getOutputStream();
                            outputStream.write(datam);
                            // tts.speak(, TextToSpeech.QUEUE_ADD, null);
                        }catch (IOException e){
                            e.printStackTrace();
                        }catch (NullPointerException e){
                            tts.speak( "Erro ao enviar", TextToSpeech.QUEUE_ADD, null);
                        }

                        tts.speak( recognize.getNames(), TextToSpeech.QUEUE_ADD, null);
                    }else if(operation == Constants.OPERATION_OBJECT_RECOGNITION_CONTINUOUS ){

                    long initialTime = System.currentTimeMillis();

                        String result = recognize.recognizeObjects(bitmap);
                        long finalTime = System.currentTimeMillis();
                        Integer processingTime = (int)((finalTime - initialTime)/10);
                        Log.d("bluetooth","RESULTADO>>>> "+result+ " And Processing Time is: "+processingTime);
                        byte[] datam = Util.stringToByte(result);


                        datam = ClientClass.createHeader(operation,datam);

                        try{

                            OutputStream outputStream = bluetoothController.getSocket().getOutputStream();
                            outputStream.write(datam);
                            // tts.speak(, TextToSpeech.QUEUE_ADD, null);
                        }catch (IOException e){
                            e.printStackTrace();
                        }

                    }
                    else if(operation == Constants.OPERATION_RECOGNIZE_TEXT_IN_OBJECT){

                    String result = recognize.recognizeObjects(bitmap);
                        byte[] datam = Util.stringToByte(result);
                        datam = ClientClass.createHeader(Constants.OPERATION_RECOGNIZE_TEXT_IN_OBJECT,datam);
                        try{
                            OutputStream outputStream = bluetoothController.getSocket().getOutputStream();
                            outputStream.write(datam);
                            // tts.speak(, TextToSpeech.QUEUE_ADD, null);
                        }catch (IOException e){
                            e.printStackTrace();
                        }

                        tts.speak( recognize.getNames(), TextToSpeech.QUEUE_ADD, null);
                    }
                    else if(operation == Constants.OPERATION_TEXT_RECOGNITION ){

                    long inTime = System.currentTimeMillis();
                        TextRecognition recognition = new TextRecognition();
                    long finalTime = System.currentTimeMillis();
                    double time = ((double)(finalTime - inTime))/(double)1000 ;
                    listTimes2.add(time);
                    count2++;

                        String result = recognition.textrecognition(bitmap,getApplicationContext());
                        if(result.equals("")){
                            result = "não há texto";
                        }

                        byte[] datam = Util.stringToByte(result);
                        datam = ClientClass.createHeader(operation,datam);
                        try{
                            OutputStream outputStream = bluetoothController.getSocket().getOutputStream();
                            outputStream.write(datam);
                            // tts.speak(, TextToSpeech.QUEUE_ADD, null);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        tts.speak(result, TextToSpeech.QUEUE_ADD, null);

                    }
                    else{
                        Log.d("bluetooth", "não há operação");
                    }





            }
        };
        CustomApplication application = (CustomApplication)getApplication();
        application.setHandler(mHandler);
        return mHandler;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public Bitmap cropImage(RectF objectRecognition, Bitmap bitmap){
        int left,top,width,height;
        float razaoHeight = bitmap.getHeight()/(float)Constants.INPUT_SIZE;
        float razaoWidth = bitmap.getWidth()/(float)Constants.INPUT_SIZE;
        // Log.d(TAG,"crop image>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+ razaoHeight+ " " + razaoWidth+ " " + bitmap.getHeight()+ " "+bitmap.getWidth()+"<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

        left = (int)(objectRecognition.left*razaoWidth);
        top = (int)(objectRecognition.top*razaoHeight);
        width = (int)(objectRecognition.width()*razaoWidth);
        height = (int)(objectRecognition.height()*razaoHeight);
        return Bitmap.createBitmap(bitmap, left,top,width,height);

    }


}

