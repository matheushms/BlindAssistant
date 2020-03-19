package com.blindassistant.googleglass.Main;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.drace.contextualvoicecommands.R;
import com.google.android.glass.view.WindowUtils;
import com.blindassistant.googleglass.Camera.CameraPreview;
import com.blindassistant.googleglass.Objects.CollectionObject;
import com.blindassistant.googleglass.Objects.ObjectRecognition;

import java.util.List;

public class ReadObjectsMenuActivity extends Activity {
    private String TAG ="ContextualMenuActivity";
    private Camera camera;
    private CameraPreview mPreview;
    private ControladorMain control;
    private Bitmap mBitmap;
    private byte[] mdata;
    private MenuData menuData;
    private CustomApplication customApplication;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        // Requests a voice menu on this activity. As for any other
        // window feature, be sure to request this before
        // setContentView() is called

        getWindow().requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS);
      //  menuData = new MenuData(this,control);
        customApplication = (CustomApplication)getApplication();
        customApplication.setControl(control);
        setContentView(R.layout.activity_main);

        invalidateOptionsMenu();
       // control = new ControladorMain(this);

    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS) {
            menu.add("quit");
            //menuData.setListObjects(control.getObjects().getListsObjects());
            Log.d("fasdf",":>>>>>>>>>>>>>>>>>><<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>< fudeu\n<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
           // menuData = new MenuData(this,control);
            //menuData.setListObjects(createFalseCollectionObjects());
            //menuData.setMenu(menu);
           // menuData.preparateMainMenu();


            return true;
        }
        // Pass through to super to setup touch menu.
        return super.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menu.add("rola");
        Log.d("fasdf", ":>>>>>>>>>>>>>>>>>><<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>< fudeu\n<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("fasdf", ":>>>>>>>>>>>>>>>>>><<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>< fudeu\n<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS||
                featureId == Window.FEATURE_OPTIONS_PANEL){

            if(item.getTitle().equals("quit")){

                Log.d("fasdf", ":>>>>>>>>>>>>>>>>>><<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>< hahahahahah\n<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

            }
         //  menuData.realizeAction(item);
        }

        return super.onMenuItemSelected(featureId, item);
    }

/*
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS||
                featureId == Window.FEATURE_OPTIONS_PANEL){

            switch (item.getItemId()) {

                case R.id.read_text_menu_item:

                    break;

                case R.id.read_object_menu_item:


                    //displaySpeechRecognizer();

                    break;
                case R.id.objects_menu_item:

                    control.getObjectsInScene();
                    break;
               case R.id.test:


                    break;
                case R.id.quit:

                    break;

            }
        }

        return super.onMenuItemSelected(featureId, item);
    }*/


    public void displaySpeechRecognizer() {
        Intent intent = new Intent(this,SpeechActivity.class);

        startActivity(intent);

    }


    private List<CollectionObject> createFalseCollectionObjects(){
        CollectionObject objects = new CollectionObject();

        objects.add(new ObjectRecognition("Bacia",20,30,43,10));
        objects.add(new ObjectRecognition("Bacia",20,30,43,10));
        objects.add(new ObjectRecognition("Laptop",20,30,43,10));
        objects.add(new ObjectRecognition("Laptop",20,30,43,10));
        objects.add(new ObjectRecognition("Laptop",20,30,43,10));
        objects.add(new ObjectRecognition("Laptop",20,30,43,10));
        objects.add(new ObjectRecognition("Jornal",20,30,43,10));
        objects.add(new ObjectRecognition("Livro",20,30,43,10));
        objects.add(new ObjectRecognition("Smarthphone",20,30,43,10));

        return objects.getListsObjects();
    }




    @Override
    protected void onPause() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
        super.onPause();
    }

}