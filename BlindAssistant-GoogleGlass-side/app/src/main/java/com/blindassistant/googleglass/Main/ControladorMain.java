package com.blindassistant.googleglass.Main;



import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.drace.contextualvoicecommands.R;

import com.blindassistant.googleglass.Bluetooth.BluetoothController;

import com.blindassistant.googleglass.Camera.CameraInitialize;
import com.blindassistant.googleglass.Camera.CameraPreview;

import com.blindassistant.googleglass.Objects.CollectionObject;

import com.blindassistant.googleglass.Utils.Speak;

import java.io.IOException;


public class ControladorMain {


    private String TAG = "toblindpeople";

    private Speak tts;
    private long timeOfSend = 7000;
    private Handler mHandler;
    private Context context;


    //Bluetooth Variables



    private CollectionObject objects;
    private CameraInitialize camera;
    private BluetoothController bluetoothController;
    private ContextualMenuActivity contextualMenuActivity;
    private Bitmap bitmap;
    private CustomApplication customApplication;
    private TextView textView;
    private boolean pauseObjectRecognition = false;
    private   ControllerOperations controllerOperations;


    public ControladorMain(ContextualMenuActivity activity){


        this.customApplication = (CustomApplication)activity.getApplication();
        this.contextualMenuActivity = activity;
        this.context = activity.getApplicationContext();


        tts = (new Speak(context));
        objects = new CollectionObject();


        //Camera and Preview configuration
        camera = new CameraInitialize();
        CameraPreview mPreview = new CameraPreview(activity, camera.getCamera());

        FrameLayout preview = (FrameLayout) activity.findViewById(R.id.capturedImage);
        preview.addView(mPreview);
        preview.getBackground();
        preview.setVisibility(View.INVISIBLE);

        //Bluetooth configuration
        initializeBluetoothConfiguration();



        textView = (TextView)activity.findViewById(R.id.coming_soon);


       controllerOperations = new ControllerOperations(bluetoothController,context,camera,activity);



    }




    private void initializeBluetoothConfiguration(){
     bluetoothController = new BluetoothController(context,mHandler,this);
        bluetoothController.initializeBT();
        bluetoothController.makeDiscoverable();
       // updateStates = new UpdateStates();
      //  UpdateObjects updateObjects = new UpdateObjects();
       // contextualMenuActivity.setThread1(updateStates);
    //1    contextualMenuActivity.setThread2(updateObjects);
     //   verifyReceive = new VerifyReceive();


    }
    public void setPermissionToUpdateMenu(Boolean state){
        controllerOperations.setPermissionToUpdateMenu(state);
    }

    public Speak getTts() {
        return tts;
    }
    public Object sendImage(byte operation, Bitmap bitmap )throws IOException{

        return controllerOperations.sendImage(operation,bitmap);
    }

    public void setPauseObjectRecognition(boolean pauseObjectRecognition) {
        this.pauseObjectRecognition = pauseObjectRecognition;
    }




    public BluetoothController getBluetoothController() {
        return bluetoothController;
    }

    public CollectionObject getObjects() {
        return objects;
    }

    //Function which work with the received data















    public Bitmap takePicture(){
       return camera.getBitmap();
    }

}





