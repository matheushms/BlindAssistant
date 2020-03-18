package com.example.mathe.smarthphone.Bluetooth;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;


public class BluetoothController {
    private String TAG = "BluetoothController";
    private BluetoothAdapter myBt;
    private Context context;
    private Handler handler;
    String adress;
    BluetoothConnect bluetoothConnect;
    private BluetoothSocket socket;

    public BluetoothController(Context context,  String adress) {
        this.context = context;

       bluetoothConnect = new BluetoothConnect(adress,context );
    }



    public boolean socketIsEnable() {
        if (bluetoothConnect == null)
            return false;
        BluetoothSocket socket = bluetoothConnect.getSocket();
        return !(socket == null);
    }

    public boolean bluetoothIsEnable() {
        return myBt.isEnabled();
    }

    public boolean socketIsConnected() {
        if (bluetoothConnect == null || bluetoothConnect.getSocket() == null)
            return false;
        return bluetoothConnect.getSocket().isConnected();
    }


    public BluetoothSocket getSocket() {
        return bluetoothConnect.getSocket();
    }

    public boolean sendBytes(byte[] bytes) throws IOException {

        OutputStream outputStream = bluetoothConnect.getSocket().getOutputStream();
        outputStream.write(bytes);
        return true;


    }

}
