package com.blindassistant.googleglass.Main;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.drace.contextualvoicecommands.R;

import com.blindassistant.googleglass.Bluetooth.BluetoothController;
import com.blindassistant.googleglass.Bluetooth.Server;
import com.blindassistant.googleglass.Camera.CameraInitialize;
import com.blindassistant.googleglass.Constants.Constants;
import com.blindassistant.googleglass.Objects.CollectionObject;
import com.blindassistant.googleglass.Utils.Speak;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ControllerOperations {
    private String TAG = "ControllerOperations";
    private BluetoothController bluetoothController;
    private Context context;
    private ControlTime controlTime = new ControlTime();
    private CollectionObject objects= new CollectionObject();
    private CameraInitialize camera;
    private Speak tts;
    private boolean pauseObjectRecognition = false;
    private boolean permissionToUpdateMenu = true;
    private VerifyReceive verifyReceive = new VerifyReceive();
    private long timeOfSend = 4000;
    private TextView textView;

    ControllerOperations(BluetoothController bluetoothController, Context context, CameraInitialize camera, ContextualMenuActivity activity){
        this.bluetoothController = bluetoothController;
        this.context = context;
        this.camera = camera;
        textView = (TextView)activity.findViewById(R.id.coming_soon);

    }






    public Handler createHandler(){
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Log.d("handler", ">>>>>>> operation " + msg.what);
                byte[] readBuff = (byte[])msg.obj;


                if (msg.what == Constants.OPERATION_OBJECT_RECOGNITION_CONTINUOUS){
                    Log.d("handler",">>>>>>> operation "+msg.what);

                    int keyTimer = (int)readBuff[0];

                    readBuff = Server.removeOperation(readBuff);
                    int time = (int)readBuff[0];
                    Log.d("handler",">>>>>>>>>>>>>>>>>>>>>>>>>>>> Time is " +  time);
                    readBuff = Server.removeOperation(readBuff);
                    controlTime.addFinalTime(keyTimer,System.currentTimeMillis(),time*10);
                    timeOfSend = controlTime.averageTime()+2000;
                    Log.d("handler",">>>>>>>>>>>>>>>>>>>>>>>>>>>> Average is " +  timeOfSend);
                    try{

                        String text = new String(readBuff, "UTF-8");
                        Log.d(TAG, "message: " + text);
                        objects = CollectionObject.separateDataObjects(text);
                        text = objects.toString();
                        Log.d(TAG, "Objects: " +
                                "" + text);

                        if(permissionToUpdateMenu) {
                            //contextualMenuActivity.getWindow().invalidatePanelMenu(WindowUtils.FEATURE_VOICE_COMMANDS);
                        }
                        if(!pauseObjectRecognition){
                            sendImage(Constants.OPERATION_OBJECT_RECOGNITION_CONTINUOUS, camera.getBitmap());
                            verifyReceive.initialTime = System.currentTimeMillis();

                        }
                        textView.setText(objects.objectsToString());
                    }catch (IOException e){
                        Log.d(TAG,"Erro ao tratar resultado de reconhecimento de objetos");
                    }
                }else if (msg.what == Constants.OPERATION_OBJECT_RECOGNITION){



                    try{
                        String text = new String(readBuff, "UTF-8");
                        Log.d(TAG, "message: " + text);
                        objects = CollectionObject.separateDataObjects(text);
                        text = objects.toString();
                        Log.d(TAG, "Objects: " +
                                "" + text);
                        tts.speak(text);

                    }catch (IOException e){
                        Log.d(TAG,"Erro ao tratar resultado de reconhecimento de objetos");
                    }
                }
                else if (msg.what == Constants.OPERATION_TEXT_RECOGNITION){


                    try{
                        String text = new String(readBuff, "UTF-8");
                        Log.d(TAG,"message: " + text );

                        tts.speak(text);

                    }catch (IOException e){
                        Log.d(TAG,"Erro ao tratar resultado de reconhecimento de texto");
                    }
                }
                else if (msg.what == Constants.OPERATION_RECOGNIZE_TEXT_IN_OBJECT){


                    try{
                        String text = new String(readBuff, "UTF-8");
                        Log.d(TAG, "message: " + text);
                        objects = CollectionObject.separateDataObjects(text);
                        text = objects.toString();
                        Log.d(TAG, "Objects: " + text);
                        tts.speak(text);
                        Log.d(TAG, ">>>>>>>>>>>>>>>>Received Objects<<<<<<<<<<<<<<<<<");

                        //liveCardMenuActivity.displaySpeechRecognizer();
                    }catch (IOException e){
                        Log.d(TAG,"Erro ao tratar resultado de reconhecimento de objetos");
                    }
                }
            }
        };
        return handler;
    }
    //Function aux to send a image
    synchronized public Object sendImage(byte operation, Bitmap bitmap)throws IOException{

        if(bluetoothController.bluetoothIsEnable()){
            Log.d(TAG, "Bluetooth ligado");
        }
        if (bluetoothController.socketIsEnable()) {
            Log.d(TAG, "socket não está null");
            if (bluetoothController.socketIsConnected()) {
                Log.d(TAG, "socket conectado");
                Object object = createHeaderAndSend(operation,bitmap);
                return  object;
            }else{

            }
        }else{
            Log.d(TAG, "socket null");
        }
        return null;
    }


    //Function to send a image, transforming a frame an
    //array of bytes and adding a header with operation
    private Object createHeaderAndSend(byte operation, Bitmap bitmap)throws IOException {

        if(operation == Constants.OPERATION_TEXT_RECOGNITION){


            Log.d(TAG, "socket está conectado");


            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byte[] imageBytes = stream.toByteArray();
            imageBytes = Server.createHeader(operation, imageBytes);
            sendBytes(imageBytes);
            String text = (String)Server.receiveResults(bluetoothController.getSocket(),context,operation);
            return text;



        }else if(operation == Constants.OPERATION_OBJECT_RECOGNITION){
            Log.d(TAG, "socket está conectado");
            Bitmap resizedBitmap = null;

            resizedBitmap = Bitmap.createScaledBitmap(
                    bitmap, 300, 300, false);


            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG,50,stream);
            byte[] imageBytes = stream.toByteArray();


            imageBytes = Server.createHeader(operation, imageBytes);
            sendBytes(imageBytes);
            Log.d(TAG, "Operation Object Recognition");

            CollectionObject objects = (CollectionObject)Server.receiveResults(bluetoothController.getSocket(),context,operation);

            return objects;

        }else if(operation == Constants.OPERATION_RECOGNIZE_TEXT_IN_OBJECT){
            Log.d(TAG, "socket está conectado");
            Bitmap resizedBitmap = null;

            resizedBitmap = Bitmap.createScaledBitmap(
                    bitmap, 600, 600, false);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG,50,stream);
            byte[] imageBytes = stream.toByteArray();
            imageBytes = Server.createHeader(Constants.OPERATION_RECOGNIZE_TEXT_IN_OBJECT, imageBytes);
            sendBytes(imageBytes);
            CollectionObject objects = (CollectionObject)Server.receiveResults(bluetoothController.getSocket(),context,operation);
            return objects;

        }else if(operation == Constants.OPERATION_OBJECT_RECOGNITION_CONTINUOUS){
            Log.d(TAG, "socket está conectado");
            Bitmap resizedBitmap = null;
            Integer key = controlTime.addInitialTime(System.currentTimeMillis());
            if(bitmap == null)
                Log.d("problem ",">>>>>>>>>>>>>>> bitmap null<<<<<<<<<<");
            //customApplication.setBitmap(bitmap);
            resizedBitmap = Bitmap.createScaledBitmap(
                    bitmap, 300, 300, false);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG,50,stream);
            byte[] imageBytes = stream.toByteArray();
            imageBytes = Server.createHeader(key.byteValue(),imageBytes);
            imageBytes = Server.createHeader(operation, imageBytes);
            long initialTime = System.currentTimeMillis();
            sendBytes(imageBytes);
            CollectionObject objects = (CollectionObject)Server.receiveResults(bluetoothController.getSocket(),context,operation);
            long finalTime = System.currentTimeMillis();
            long time = (finalTime - initialTime)/1000 ;
            return objects;

        }

        return null;

    }

    //Function which send via bluetooth the array of bytes
    private void sendBytes(byte[] imageBytes){
        int subArraySize = 400;

        try {


            bluetoothController.sendBytes((String.valueOf(imageBytes.length)).getBytes());

                for (int i = 0; i < imageBytes.length; i += subArraySize) {
                    byte[] temp;

                    temp = Arrays.copyOfRange(imageBytes, i, Math.min(imageBytes.length, i + subArraySize));
                    Log.d(TAG, "DATA OF MENSAGE >>>>>>>> " + temp.length);
                    bluetoothController.sendBytes(temp);


                }

        }catch (IOException e){
            Log.d("problem",">>>>>>>>>> error >>>>>>>>> "+e.toString());
            bluetoothController.setSocket(null);
        }

    }

    //Essa função tem por objetivo verificar se a resposta do processamento foi recebida
    // Se não, ela envia novamente a imagem para ser processada

    private class VerifyReceive extends  Thread{
        long initialTime;
        public VerifyReceive() {
            initialTime = System.currentTimeMillis();
            start();
        }
        @Override
        public void run() {

            CollectionObject previousObjects = new CollectionObject();

            long limitTime = 4000;

            try{
                while(true){
                    Log.d("updateStates", ">>>>>>>>>>UpdateObjects<<<<<<<<<");
                    if(bluetoothController.socketIsConnected()
                            && bluetoothController.socketIsEnable()
                            && !(objects.isEquals(previousObjects))
                            && (initialTime - System.currentTimeMillis()) > limitTime
                            && !pauseObjectRecognition) {
                        sendImage(Constants.OPERATION_OBJECT_RECOGNITION_CONTINUOUS, camera.getBitmap());
                        initialTime = System.currentTimeMillis();

                    }else{



                    }
                    previousObjects = objects;
                    new Thread().sleep(300);
                }
            }catch (Exception e){

            }

        }

    }
    //Function which update the list of objects in actual scene
    private class UpdateStates extends Thread {

        public UpdateStates() {
            start();
        }
        @Override
        public void run() {
            try{
                while(true){
                    Log.d("updateStates", ">>>>>>>>>>UpdateStates<<<<<<<<<");
                    if(bluetoothController.socketIsConnected() && bluetoothController.socketIsEnable()) {

                    }else{
                        tts.speak("Without Connection!");
                        new Thread().sleep(4000);
                      /*  bluetoothController.makeDiscoverable();
                        int i = 0;
                        for(;i<150 && !bluetoothController.socketIsConnected();i++){
                            try{
                                Thread.sleep(2000);
                            }catch(Exception e){

                            }

                        }*/
                    }

                    new Thread().sleep(3000);
                }
            }catch (Exception e){
//                Log.i("AsyncTask", e.getMessage());
                Log.d("updateStates",">>>>>>>>>>Error<<<<<<<<<");
//                Log.d("updateStates",e.getMessage());
                e.printStackTrace();
            }

        }

    }


    //Function which control de time of update list of objects
    private static class ControlTime{

        private long timeSmaller = 1000;
        private long timeBigger = 0;
        private int limit = 5;
        private int limitKeys = 4;
        private int actualKey = 0;
        private long addTime = 0;
        List<Long> timeDiference = new ArrayList<>();
        List<Long> times = new ArrayList<>();
        List<Integer> keySequence = new ArrayList<>();
        List<String> initialTime = new ArrayList<>();

        //  private void ()

        public ControlTime(){
            timeDiference = new ArrayList<>();
            times = new ArrayList<>();
            keySequence = new ArrayList<>();

        }

        public int addInitialTime(long currentTime){
            actualKey++;
            int aux = actualKey;
            if(initialTime.size() < limitKeys*3){
                initialTime.add(actualKey+"-"+currentTime);
            }else{
                initialTime.remove(0);
                initialTime.add(actualKey+"-"+currentTime);

            }


            if(actualKey > 50)
                actualKey = 0;
            return aux;

        } public void addFinalTime(int key, long currentTime, long timeProcessing){
            int key1;
            long initialT;

            String remove= null;
            for(String time : initialTime){
                String[] timeSlipt = time.split("-");
                key1 = Integer.parseInt(timeSlipt[0]);
                initialT = Long.parseLong(timeSlipt[1]);
                if(key1 == key){
                    long timeaux = (currentTime - initialT) - timeProcessing;
                    if(timeaux > timeBigger){
                        timeBigger = timeaux;
                        addTime= 200;
                    }
                    if(timeaux < timeSmaller)
                        timeSmaller = timeaux;
                    remove = time;

                    addValue(currentTime - initialT);

                    break;
                }
                addTime = 0;

            }
            initialTime.remove(remove);


        }
        public void addValue(long initialTime){

            if(times.size() < limitKeys){

            }else{
                times.remove(0);

            }

            times.add(initialTime);


        }

        private long averageTime(){
            long average = 1000;
            long aux = 0;
            for(Long aux2 : times){
                aux += aux2;

            }
            if(aux == 0);
            else average = aux/times.size();

            return average + addTime;
        }


    }

    //Function which update the list of objects in actual scene
    private class UpdateObjects extends  Thread{
        public UpdateObjects() {
            start();
        }
        @Override
        public void run() {

            CollectionObject previousObjects = new CollectionObject();

            try{
                while(true){
                    Log.d("updateStates", ">>>>>>>>>>UpdateObjects<<<<<<<<<");
                    if(bluetoothController.socketIsConnected() && bluetoothController.socketIsEnable() &&
                            !(objects.isEquals(previousObjects))) {
                        Log.d("updateStates", ">>>>>>>>>>New Scene<<<<<<<<<");
                        tts.speak(objects.objectsToString());

                    }else{



                    }
                    previousObjects = objects;
                    new Thread().sleep(5000);
                }
            }catch (Exception e){

            }

        }

    }
    public void setPermissionToUpdateMenu(boolean permissionToUpdateMenu) {
        this.permissionToUpdateMenu = permissionToUpdateMenu;
    }

}