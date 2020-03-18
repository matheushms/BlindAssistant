package com.mhms.poliprojects.toblindpeopleimersion.Bluetooth;

import android.app.Service;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;


import com.mhms.poliprojects.toblindpeopleimersion.Constants.Constants;
import com.mhms.poliprojects.toblindpeopleimersion.Main.ControladorMain;
import com.mhms.poliprojects.toblindpeopleimersion.Utils.Data;
import com.mhms.poliprojects.toblindpeopleimersion.Utils.Speak;

import java.io.IOException;
import java.util.UUID;

public class BluetoothConnect extends Thread {
    private final BluetoothServerSocket mmServerSocket;
    private String uuid = "8ce255c0-200a-11e0-ac64-0800200c9a66";
    private String NAME = "Text Recognition";
    private UUID MY_UUID = UUID.nameUUIDFromBytes(uuid.getBytes());
    private BluetoothAdapter mBluetoothAdapter;
    private Speak tts;
    private BluetoothSocket socket = null;
    private Context context;
    private Handler handler;
    private ControladorMain control;
    //private Data data;


    public BluetoothConnect(Context context,ControladorMain control) {
        // Use a temporary object that is later assigned to mmServerSocket,
        // because mmServerSocket is final
        this.context = context;
        this.control = control;
        tts = new Speak(context);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
        } catch (IOException e) { }
        mmServerSocket = tmp;

    }
    public void initialize(Handler handler){
        this.handler = handler;
        start();
    }
    public void setSocket(BluetoothSocket socket){
        this.socket = socket;
    }
    public void run() {

        // Keep listening until exception occurs or a socket is returned
        Log.d("bluetooth", "entrou na thread bluetoothconnect");
        while (true) {
            try {if(mmServerSocket != null)
                socket = mmServerSocket.accept();


            } catch (IOException e) {
                break;
            }
            // If a connection was accepted
            if (socket != null) {
                // Do work to manage the connection (in a separate thread)
                //Server server = new Server(socket);
                Log.d("bluetooth", "socket não está mais null oba");

                    if(socket.isConnected()){
                        tts.speak("Device connected");

                    }
                   // Server server = new Server(socket);
                    //server.initialize(handler);
                    //mmServerSocket.close();


                break;
            }
        }
    }


    public BluetoothSocket getSocket() {
        return socket;
    }

    /** Will cancel the listening socket, and cause the thread to finish */
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) { }
    }
}