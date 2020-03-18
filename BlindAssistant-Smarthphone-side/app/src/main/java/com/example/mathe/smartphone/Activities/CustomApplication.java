package com.example.mathe.smartphone.Activities;

import android.app.Application;

import android.hardware.Camera;
import android.os.Handler;





public class CustomApplication extends Application {

    private byte[] data;
    private Camera camera;
    private Handler handler;


    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }


    public void setData(byte[] data) {


        this.data = data;
    }


}