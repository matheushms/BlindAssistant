package com.mhms.poliprojects.toblindpeopleimersion.Main;

import android.app.Application;
import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;

import com.mhms.poliprojects.toblindpeopleimersion.Objects.CollectionObject;

import java.io.ByteArrayOutputStream;

public class CustomApplication extends Application {
    private BluetoothSocket bluetoothSocket;
    private Bitmap bitmap;
    private Bitmap actualAnalisys;
    private CollectionObject objects = new CollectionObject();
    private byte[] data;
    private Camera camera;
    private ControladorMain control;

    public ControladorMain getControl() {
        return control;
    }




    public void setControl(ControladorMain control) {
        this.control = control;
    }

    public Bitmap getActualAnalisys() {
        return actualAnalisys;
    }

    public void setActualAnalisys() {
        this.actualAnalisys = bitmap;
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

    public BluetoothSocket getBluetoothSocket() {
        return bluetoothSocket;
    }

    public void setBluetoothSocket(BluetoothSocket bluetoothSocket) {
        this.bluetoothSocket = bluetoothSocket;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Bitmap getBitmap() {

        return bitmap;

    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setData(byte[] data) {


        this.data = data;
    }

    public CollectionObject getObjects() {
        return objects;
    }

    public void setObjects(CollectionObject objects) {
        this.objects = objects;
    }
}