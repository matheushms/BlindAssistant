package com.blindassistant.googleglass.Bluetooth;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import com.blindassistant.googleglass.Constants.Constants;
import com.blindassistant.googleglass.Objects.CollectionObject;
import com.blindassistant.googleglass.Utils.Speak;
import com.blindassistant.googleglass.Utils.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class Server extends Thread {
    Speak tts ;

    private static final String TAG = "Server";
    public static Object receiveResults(BluetoothSocket socket, Context context, byte operation)throws IOException{
        CollectionObject collectionObject;
        Speak tts = new Speak(context);
        final InputStream inputStream;
        final OutputStream outputStream;
        InputStream tmpInputS = null;
        OutputStream tmpOutputS = null;
        CollectionObject objectsaux = null;
        if(socket != null) {
            try {
                tmpInputS = socket.getInputStream();
                tmpOutputS = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            inputStream = tmpInputS;
            outputStream = tmpOutputS;

            byte[] buffer;
            boolean validator = true;

            int bytes;

            long initialTime = System.currentTimeMillis();
            long limitTime = 2000;
            if(operation == Constants.OPERATION_VERIFY)
                limitTime= 2000;
            byte[] data = null;
            while (validator && (System.currentTimeMillis() - initialTime) < limitTime) {
                try {

                    buffer = new byte[inputStream.available()];
                    byte  rOperation = buffer[0];
                    buffer = removeOperation(buffer);
                    if (inputStream.read(buffer) > 0) {
                            return processingReceive(buffer,operation,context);


                    }

                } catch (Exception e) {

                }

            }
            if(validator && (System.currentTimeMillis() - initialTime) < limitTime)
                throw new IOException();
        }else
            tts.speak("Device not connected");
        return objectsaux;

    }

    private static Object processingReceive(byte[] buffer, byte operation,Context context){
        Object object;
        Speak tts = new Speak(context);

        Log.d("handler", ">>>>>operation " + operation);
        byte[] readBuff = buffer;


        if (operation == Constants.OPERATION_OBJECT_RECOGNITION_CONTINUOUS){

            try{

                String text = new String(readBuff, "UTF-8");
                Log.d(TAG, "message: " + text);
                object = CollectionObject.separateDataObjects(text);
                CollectionObject objects = (CollectionObject)object;
                text = objects.toString();
                Log.d(TAG, "Objects: " +
                        "" + text);

                return object;
                //textView.setText(objects.objectsToString());
            }catch (IOException e){
                Log.d(TAG,"Erro ao tratar resultado de reconhecimento de objetos");
            }
        }else if (operation == Constants.OPERATION_OBJECT_RECOGNITION){



            try{
                operation = buffer[0];
                byte[] data = Server.removeOperation(buffer);
                Log.d("bluetooth", new String(data, "UTF-8") + " operation>>>>>" + operation);


                String text = new String(data, "UTF-8");

                Log.d(TAG, "message: " + text);
                return CollectionObject.separateDataObjects(text);



            }catch (IOException e){
                Log.d(TAG,"Erro ao tratar resultado de reconhecimento de objetos");
            }
        }
        else if (operation == Constants.OPERATION_TEXT_RECOGNITION){


            try{
                String text = new String(readBuff, "UTF-8");
                Log.d(TAG, "message: " + text);
                object = text;
                tts.speak(text);
                return object;
            }catch (IOException e){
                Log.d(TAG,"Erro ao tratar resultado de reconhecimento de texto");
            }
        }
        else if (operation == Constants.OPERATION_RECOGNIZE_TEXT_IN_OBJECT){


            try{
                String text = new String(readBuff, "UTF-8");
                Log.d(TAG, "message: " + text);
                object = CollectionObject.separateDataObjects(text);
                CollectionObject objects = (CollectionObject)object;
                text = objects.toString();
                Log.d(TAG, "Objects: " + text);
                tts.speak(text);
                Log.d(TAG, ">>>>>>>>>>>>>>>>Received Objects<<<<<<<<<<<<<<<<<");
                return object;
                //liveCardMenuActivity.displaySpeechRecognizer();
            }catch (IOException e){
                Log.d(TAG,"Erro ao tratar resultado de reconhecimento de objetos");
            }
        }   else if (operation == Constants.OPERATION_VERIFY){


                return readBuff[0];
                //liveCardMenuActivity.displaySpeechRecognizer();

        }



        return  null;
    }
    public static byte[] createHeader(byte operation, byte [] msg){
        byte[] byt = new byte[1];
        byt[0] = operation;
        return Util.concat(byt, msg);
    }
   public static byte[] removeOperation(byte[] data){
        return Arrays.copyOfRange(data, 1, data.length);
    }


}


