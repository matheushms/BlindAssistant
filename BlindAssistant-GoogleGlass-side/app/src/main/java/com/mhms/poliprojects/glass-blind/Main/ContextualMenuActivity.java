package com.mhms.poliprojects.toblindpeopleimersion.Main;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.drace.contextualvoicecommands.R;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.view.WindowUtils;
import com.mhms.poliprojects.toblindpeopleimersion.Camera.CameraPreview;
import com.mhms.poliprojects.toblindpeopleimersion.Constants.Constants;
import com.mhms.poliprojects.toblindpeopleimersion.Objects.CollectionObject;
import com.mhms.poliprojects.toblindpeopleimersion.Objects.ObjectRecognition;
import com.mhms.poliprojects.toblindpeopleimersion.Utils.Speak;
import com.mhms.poliprojects.toblindpeopleimersion.Utils.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ContextualMenuActivity extends Activity {
    private String TAG ="ContextualMenuActivity";
    private Camera camera;
    private CameraPreview mPreview;
    private ControladorMain control;
    private Bitmap mBitmap;
    private byte[] mdata;
    private MenuData menuData;
    private CustomApplication customApplication;
    private Thread thread1, thread2;
    private GestureDetector mGestureDetector;
    private GestureDetector mGestureDetector1;
    private List<CollectionObject> listObjects;

    private int count = 0;
    private List<Double> listTimes = new ArrayList<>();
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        // Requests a voice menu on this activity. As for any other
        // window feature, be sure to request this before
        // setContentView() is called
        menuData = new MenuData(this,control);
        getWindow().requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        customApplication = (CustomApplication)getApplication();
        customApplication.setControl(control);
        setContentView(R.layout.activity_main);
        mGestureDetector1 = createGestureDetector(this);
        mGestureDetector = mGestureDetector1;
        invalidateOptionsMenu();
       control = new ControladorMain(this);



    }

    public GestureDetector getmGestureDetector1() {
        return mGestureDetector1;
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS ||
                featureId == Window.FEATURE_OPTIONS_PANEL) {
            menuData = new MenuData(this,control);
       menuData.setListObjects(control.getObjects().getListsObjects());
        //   menuData.setListObjects(createFalseCollectionObjects());
            Log.d("fasdf", ":>>>>>>>>>>>>>>>>>><<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>< oncreatenaelmenu\n<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

            menuData.setMenu(menu);
            menuData.preparateMainMenu();


            return true;
        }
        // Pass through to super to setup touch menu.
        return super.onCreatePanelMenu(featureId, menuData.getMenu());
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

      return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onPreparePanel(int featureId, View view, Menu menu) {


        return super.onPreparePanel(featureId, view, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS ||
                featureId == Window.FEATURE_OPTIONS_PANEL) {
          //  control.setPermissionToUpdateMenu(false);
          menuData.realizeAction(item);

        }

        return super.onMenuItemSelected(featureId, item);
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        if(thread1 != null){
            thread1.interrupt();

        }
        if(thread2 !=null){
            thread2.interrupt();
        }
    }

    public ContextualMenuActivity() {

    }

/*GESTURES*/

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        return mGestureDetector.onMotionEvent(event);
    }

    protected boolean onTap() {
        return false;
    }

    protected boolean onTwoTap() {
        return false;
    }

    protected boolean onSwipeRight() {
        return false;
    }

    protected boolean onSwipeLeft() {
        return false;
    }

    protected boolean onSwipeDown() {
        return false;
    }
    private GestureDetector createGestureDetector(final Context context) {
        GestureDetector gestureDetector = new GestureDetector(context);

        gestureDetector.setBaseListener(new GestureDetector.BaseListener() {
            @Override
            public boolean onGesture(Gesture gesture) {
                Speak tts = control.getTts();
                tts.stop();
                if (gesture == Gesture.TAP) {
                    playSoundEffect(Sounds.TAP);


                    Log.d(TAG, ">>>>>>>>>>>>>>> TAP 1<<<<<<<<<<<<<<<<<");



                    return onTap();
                } else if (gesture == Gesture.TWO_TAP) {
                    Log.d(TAG, ">>>>>>>>>>>>>>> TWO TAP 1 <<<<<<<<<<<<<<<<<");
                    return onTwoTap();
                } else if (gesture == Gesture.SWIPE_RIGHT) {
                   playSoundEffect(Sounds.SELECTED);
                    Log.d(TAG, ">>>>>>>>>>>>> SWIPE RIGHT 1 <<<<<");
                    Bitmap bitmap = control.takePicture();
                    CollectionObject objects = objectRecognition(bitmap);
                    if(objects !=null){
                  //  GestureControl gestureControl = new GestureControl(ContextualMenuActivity.this, control, objects,bitmap);
                    listObjects = objects.getListsObjects();

                  //  tts.speak(objectsToString());
                    Log.d(TAG, ">>>>>> OBJECTS >>>> " + objectsToString());
                    //gestureControl.interactionReadObject();
                    }

                    return onSwipeRight();
                } else if (gesture == Gesture.SWIPE_LEFT) {

                    playSoundEffect(Sounds.SELECTED);
                    objectRecognition(control.takePicture());

                    return onSwipeLeft();
                } else if (gesture == Gesture.SWIPE_DOWN) {
                    finish();
                    return onSwipeDown();
                }else if (gesture == Gesture.SWIPE_UP) {
                    Log.d(TAG, ">>>>>>>>>>>>>>> SWIPE UP 1<<<<<<<<<<<<<<<<<");
                    tts.speak("principal menu");
                    return onSwipeDown();
                }
                else if (gesture == Gesture.THREE_TAP) {
                    Log.d(TAG, ">>>>>>>>>>>>>>> THREE TAP 1<<<<<<<<<<<<<<<<<");

                    return onSwipeDown();
                }else if (gesture == Gesture.LONG_PRESS) {
                    Log.d(TAG, ">>>>>>>>>>>>>>> lONG PRESS 1<<<<<<<<<<<<<<<<<");
                    return onSwipeDown();
                }
                return false;
            }
        });
        return gestureDetector;

    }


    private CollectionObject objectRecognition(Bitmap bitmap){
        CollectionObject objects = null;
        Speak tts = control.getTts();
        boolean validator = true;
        try{
            control.setPauseObjectRecognition(true);


            long inTime = System.currentTimeMillis();
            objects = (CollectionObject)control.sendImage(Constants.OPERATION_OBJECT_RECOGNITION,bitmap);

            long finalTime = System.currentTimeMillis();
            double time = ((double)(finalTime - inTime))/(double)1000 ;
            if(time<5) {
                if (objects != null)
                    listTimes.add(time);
                count++;
                Log.d(TAG, "COUNT: " + count + " TIME: " + time + " AVERAGE: " + Util.media(listTimes)
                        + " STANDARD DEVIATION: " + Util.desviopadrao(listTimes));

                TextView textView = (TextView) findViewById(R.id.coming_soon);
                textView.setText("COUNT: " + count + " TIME: " + time + " AVERAGE: " + Util.media(listTimes)
                        + " STANDARD DEVIATION: " + Util.desviopadrao(listTimes));
            }
            Log.d(TAG, "Operation Object Recognition");
            if(objects != null)
             Log.d(TAG,"Title>>>>>>>>>>>> "+objects.getTitle());
        }catch (IOException e){
            playSoundEffect(Sounds.ERROR);
            tts.speak("Error when did take the frame!");
            validator = false;
        }
        if(objects == null && validator){
            playSoundEffect(Sounds.ERROR);
            Log.d(TAG, "Objects == null");
            tts.speak("Error in connection, try again!");
        }
        if(objects != null){
            Log.d(TAG,"Objects != null");
            listObjects = objects.getListsObjects();
            tts.speak(objectsToString());
        }


        control.setPauseObjectRecognition(false);
        return objects;
    }
    public void setmGestureDetector(GestureDetector mGestureDetector) {
        this.mGestureDetector = mGestureDetector;
    }

    private CollectionObject createFalseCollectionObjects(){
        CollectionObject objects = new CollectionObject();

        objects.add(new ObjectRecognition("newspaper",20,30,43,10));
        objects.add(new ObjectRecognition("newspaper",20,30,43,10));
        objects.add(new ObjectRecognition("Laptop",20,30,43,10));
        objects.add(new ObjectRecognition("Laptop",20,30,43,10));
        objects.add(new ObjectRecognition("Laptop",20,30,43,10));
        objects.add(new ObjectRecognition("Laptop",20,30,43,10));
       // objects.add(new ObjectRecognition("book",20,30,43,10));
      //  objects.add(new ObjectRecognition("bed",20,30,43,10));
        objects.add(new ObjectRecognition("Smarthphone", 20, 30, 43, 10));

        return objects;
    }


    public void setThread2(Thread thread2) {
        this.thread2 = thread2;
    }

    public void setThread1(Thread thread1) {
        this.thread1 = thread1;
    }

    private String objectsToString(){
        String result = "";
        String and;
        Iterator<CollectionObject> iter = listObjects.iterator();

        while (iter.hasNext()) {
            CollectionObject listObject = iter.next();

            if(!iter.hasNext())
                and = "";
            else
                and = " and ";



            if(listObject.getQtd() == 1) {

                switch((listObject.getTitle()).charAt(0)){
                    case'a':
                    case'e':
                    case'i':
                    case'o':
                    case'u':
                        result = result + "an " + listObject.getTitle() + and;
                        break;
                    default:
                        result = result +"a " + listObject.getTitle() + and;;
                }
            }

            else
                result = result + listObject.getQtd() + " " + listObject.getTitle()+and;
        }
        Log.d(TAG, result);
        return result;
    }

    private void playSoundEffect(int sound){
        AudioManager audio = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        audio.playSoundEffect(sound);
    }
}