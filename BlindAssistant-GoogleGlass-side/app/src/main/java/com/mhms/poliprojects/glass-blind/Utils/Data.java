package com.mhms.poliprojects.toblindpeopleimersion.Utils;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

public class Data {
    public BluetoothSocket getBluetoothSocket() {
        return bluetoothSocket;
    }

    public void setBluetoothSocket(BluetoothSocket bluetoothSocket) {
        this.bluetoothSocket = bluetoothSocket;
    }


    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
    private Handler handler;
    private BluetoothSocket bluetoothSocket;


}