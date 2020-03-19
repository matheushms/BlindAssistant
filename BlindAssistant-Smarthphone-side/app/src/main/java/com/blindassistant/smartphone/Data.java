package com.blindassistant.smartphone;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

public class Data {

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public BluetoothSocket getSocket() {
        return socket;
    }

    public void setSocket(BluetoothSocket socket) {
        this.socket = socket;
    }

    private Handler handler;
    private BluetoothSocket socket;
}
