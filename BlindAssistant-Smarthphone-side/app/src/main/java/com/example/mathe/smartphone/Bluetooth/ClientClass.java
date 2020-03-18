package com.example.mathe.smarthphone.Bluetooth;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import com.example.mathe.smarthphone.Activities.CustomApplication;
import com.example.mathe.smarthphone.Constants;
import com.example.mathe.smarthphone.Data;
import com.example.mathe.smarthphone.Utils.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import android.os.Handler;

public class ClientClass extends Thread {
    private byte[] buffer = null;
    private int lenght = 0;
    private boolean flag = true;
    private int index = 0;
    private int operation;

    private BluetoothSocket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private int STATE_MESSAGE_RECEIVED = 3;
    Context context;


    private Handler handler;
    private String TAGStorage = "storage";
    public ClientClass(BluetoothSocket socket, Context context){

        this.context = context;
        this.socket = socket;
        CustomApplication application = (CustomApplication)context.getApplicationContext();
        this.handler = application.getHandler();
        InputStream tmpInputS = null;
        OutputStream tmpOutputS = null;

        try{
            tmpInputS = socket.getInputStream();
            tmpOutputS = socket.getOutputStream();
        }catch (IOException e){
            e.printStackTrace();
        }
        inputStream = tmpInputS;
        outputStream = tmpOutputS;
        start();
    }
    public void run(){


        while(true){
            receive();

        }
    }


    private synchronized void receive(){
        if(flag) {
            try {
                byte[] temp = new byte[inputStream.available()];
                if (inputStream.read(temp) > 0) {

                    Log.d("bluetooth","recebido>>>>>>>>"+(new String(temp, "UTF-8")));
                    lenght = Integer.parseInt(new String(temp, "UTF-8"));
                    Log.d("bluetooth","LENGHT>>>>>>>>>>>"+lenght);
                    buffer = new byte[lenght];
                    flag = false;
                        /*String receive = new String(temp, "UTF-8");
                        String [] receives = receive.split("-");
                        lenght = Integer.parseInt(receives[0]);
                        Log.d("bluetooth","LENGHT>>>>>>>>>>>"+lenght);
                        screenOrientation = Integer.parseInt(receives[1]);
                        buffer = new byte[lenght];
                        flag = false;*/
                }
                //String text = recognizer.textrecognition(buffer,context,bytes);

            } catch (Exception e) {
                Log.d("bluetooth",">>>>>>>>>>>>>>>>>>ERROR<<<<<<<<<<<<<<<<<<<<<<<<");
                e.printStackTrace();
            }
        }else{
            try{
                byte[] data  = new byte[inputStream.available()];

                int numbers = inputStream.read(data);
                if(data.length>0)
                    Log.d("bluetooth","DATA OF MENSAGE >>>>>>>>>>>> " +buffer.length+" "+ data.length+" "+numbers+"<<<<<<<<<<<<");
                System.arraycopy(data,0,buffer,index,numbers);
                index = index + numbers;
                if(index == lenght){

                    handler.obtainMessage(Constants.STATE_MESSAGE_RECEIVED,lenght,-1,buffer).sendToTarget();

                    flag = true;
                    index = 0;
                }
            }catch (Exception e){
                Log.d("bluetooth",">>>>>>>>>>>>>>>>>>ERROR2<<<<<<<<<<<<<<<<<<<<<<<<");
                e.printStackTrace();
            }
        }
    }
   /* public void run(){

        while(true){
            try {


                byte[] temp = new byte[inputStream.available()];
                Log.d("bluetooth","recebendo dados");
                byte operation = temp[0];
                byte[] data = removeOperation(temp);
                handler.obtainMessage(operation,data.length,-1,data).sendToTarget();
            }catch (Exception e){

            }


        }
    }*/
    private void receiveHeader()throws Exception{
        byte[] temp = new byte[inputStream.available()];
        if (inputStream.read(temp) > 0) {
            String receive = new String(temp, "UTF-8");
            //String [] arg = receive.split("/");
           // lenght = Integer.parseInt(arg[0]);
            lenght = Integer.parseInt(receive);
            //operation = Integer.parseInt(arg[1]);
            buffer = new byte[lenght];
            flag = false;
            Log.d("bluetooth", "operation: "+ operation + "length: " + lenght);
        }
    }
    private void receivePackage()throws Exception{
        byte[] data  = new byte[inputStream.available()];
        int numbers = inputStream.read(data);
        if(data.length > 0)
            Log.d("bluetooth",buffer.length+" "+ data.length+" "+numbers);
        System.arraycopy(data,0,buffer,index,numbers);
        index = index + numbers;
        if(index == lenght){
            handler.obtainMessage(Constants.STATE_MESSAGE_TEXT_RECOGNITION,lenght,-1,buffer).sendToTarget();

            flag = true;
            index = 0;
        }
    }
    public static byte[] createHeader(byte operation, byte [] msg){
        byte[] byt = new byte[1];
        byt[0] = operation;
        return Util.concat(byt,msg);
    }
    private byte[] removeOperation(byte[] data){
        return Arrays.copyOfRange(data,1,data.length);
    }

    private void write(String message){
        byte[] bytes = message.getBytes();
        try{
            outputStream.write(bytes);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}