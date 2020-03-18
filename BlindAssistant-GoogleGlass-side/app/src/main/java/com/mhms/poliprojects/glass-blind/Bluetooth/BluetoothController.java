package com.mhms.poliprojects.toblindpeopleimersion.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.mhms.poliprojects.toblindpeopleimersion.Constants.Constants;
import com.mhms.poliprojects.toblindpeopleimersion.Main.ControladorMain;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;


public class BluetoothController {
    private String TAG = "BluetoothController";
    private BluetoothAdapter myBt;
    private Context context;
    private Handler handler;
    private BluetoothConnect bluetoothConnect;
    private ControladorMain control;
    private BluetoothSocket socket;
    public BluetoothController(Context context,Handler handler, ControladorMain controladorMain){
     this.context = context;
        this.control = controladorMain;
        this.handler = handler;
    }
    public  boolean initializeBT(){
        myBt = BluetoothAdapter.getDefaultAdapter();
        if (myBt == null) {

        }
        else if (!myBt.isEnabled()) {

            // we need to wait until bt is enabled before set up, so that's done either in the following else, or
            // in the onActivityResult for our code ...
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //     activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {

        }
        return true;
    }
    public void makeNotDiscoverable (){

        // see notes for makeDiscoverable
        Class <?> baClass = BluetoothAdapter.class;
        Method[] methods = baClass.getDeclaredMethods();

        Method mSetScanMode = methods[38];
        try {
            mSetScanMode.invoke(myBt, BluetoothAdapter.SCAN_MODE_CONNECTABLE,300);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
    public void makeDiscoverable(){

        Class <?> baClass = BluetoothAdapter.class;
        Method[] methods = baClass.getDeclaredMethods();

        Method mSetScanMode = methods[38];
        try {
            mSetScanMode.invoke(myBt, BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE,300);
            bluetoothConnect = new BluetoothConnect(context,control);
           bluetoothConnect.initialize(handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  boolean socketIsEnable(){
        if(bluetoothConnect == null)
            return false;
        BluetoothSocket socket = bluetoothConnect.getSocket();
        return !(socket==null);
    }
    public boolean bluetoothIsEnable(){
        return myBt.isEnabled();
    }
    public boolean socketIsConnected(){
        if (bluetoothConnect == null || bluetoothConnect.getSocket() == null)
            return  false;
        return bluetoothConnect.getSocket().isConnected();
    }

    public void setSocket(BluetoothSocket socket){
        bluetoothConnect.setSocket(socket);
        this.socket = socket;
    }

    public BluetoothSocket getSocket(){
        return bluetoothConnect.getSocket();
    }
    public synchronized boolean sendBytes(byte[] bytes)throws IOException{

             OutputStream outputStream = bluetoothConnect.getSocket().getOutputStream();
            outputStream.write(bytes);
            return true;




    }


}