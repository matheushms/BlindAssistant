package com.blindassistant.smartphone.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class BluetoothConnect{
    BluetoothSocket socket;
    InputStream inputStream;
    OutputStream outputStream;
    Context context;
    BluetoothAdapter mBluetoothAdapter;
    String uuid = "8ce255c0-200a-11e0-ac64-0800200c9a66";
    private  BluetoothSocket mmSocket;
    private UUID MY_UUID = UUID.nameUUIDFromBytes(uuid.getBytes());
    public BluetoothConnect(String adress, Context context){
        this.context = context;

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(adress);
        this.socket = socket;
        ConnectThread thread = new ConnectThread(device);



    }

    public BluetoothSocket getSocket() {
        return mmSocket;
    }

    private class ConnectThread extends Thread {

        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) { }
            mmSocket = tmp;

            start();
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {


                // Unable to connect; close the socket and get out
                try {  mmSocket =(BluetoothSocket) mmDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(mmDevice,1);
                    mmSocket.connect();

                } catch (Exception e1) {Log.d(TAG, "PRESTA ATENÇÃO AQUI, BUGGGGGG");connectException.printStackTrace();
                    try{ mmSocket.close();}
                        catch (Exception e2){}
                }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            if(mmSocket.isConnected()){

                    ClientClass manegerConnection = new ClientClass(mmSocket, context);
            }
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }
}
