package com.mhms.poliprojects.toblindpeopleimersion.Main;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.util.Log;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.mhms.poliprojects.toblindpeopleimersion.Constants.Constants;
import com.mhms.poliprojects.toblindpeopleimersion.Objects.CollectionObject;
import com.mhms.poliprojects.toblindpeopleimersion.Objects.ObjectRecognition;
import com.mhms.poliprojects.toblindpeopleimersion.Utils.Speak;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
/*
int	DISALLOWED	User tried a disallowed action.
int	DISMISSED	User dismissed an item.
int	ERROR	An error occurred.
int	SELECTED	An item became selected.
int	SUCCESS	An action completed successfully.
int	TAP	User tapped on item.

Example:
AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
 audio.playSoundEffect(Sounds.TAP);
*/


public class GestureControl {
    private String TAG = "GestureControl";
    private Speak tts;
    private ControladorMain control;
    private ContextualMenuActivity contextualMenuActivity;
    private List<CollectionObject> listObjects;
    private CollectionObject actualObjectList;
    private CustomApplication customApplication;

    private  GestureDetector mGestureDetector2;
    private GestureDetector mGestureDetector3;
    private GestureDetector mGestureDetector4;
    private  Bitmap bitmap;
    private int countAux = 0;
    public GestureControl(ContextualMenuActivity contextualMenuActivity, ControladorMain control,Bitmap bitmap){
        this.contextualMenuActivity = contextualMenuActivity;
        this.control = control;
        this.customApplication = (CustomApplication)contextualMenuActivity.getApplication();
      //  bitmap = cropImage(actualObjectList.getObjects().get(0), customApplication.getActualAnalisys());
        tts = control.getTts();
        listObjects = control.getObjects().getListsObjects();
        actualObjectList = listObjects.get(0);
        mGestureDetector2 = createGestureDetector2(contextualMenuActivity);
        contextualMenuActivity.setmGestureDetector(mGestureDetector2);
        mGestureDetector3 = createGestureDetector3(contextualMenuActivity);
        mGestureDetector4 = createGestureDetector4(contextualMenuActivity);
        this.bitmap = bitmap;
        interactionReadObject();
    }
    public GestureControl(ContextualMenuActivity contextualMenuActivity, ControladorMain control, CollectionObject objects,Bitmap bitmap){

        this.contextualMenuActivity = contextualMenuActivity;
        this.control = control;
        this.customApplication = (CustomApplication)contextualMenuActivity.getApplication();
//        bitmap = cropImage(actualObjectList.getObjects().get(0), customApplication.getActualAnalisys());
        tts = (new Speak(contextualMenuActivity.getApplication()));
        listObjects = objects.getListsObjects();
        actualObjectList = listObjects.get(0);
        mGestureDetector2 = createGestureDetector2(contextualMenuActivity);
        contextualMenuActivity.setmGestureDetector(mGestureDetector2);
        mGestureDetector3 = createGestureDetector3(contextualMenuActivity);
        mGestureDetector4 = createGestureDetector4(contextualMenuActivity);
        this.bitmap = bitmap;
    }

    public void interactionReadObject(){
        if(listObjects.size()> 0){

            contextualMenuActivity.setmGestureDetector(mGestureDetector2);
        }else{
            tts.speak("there are no objects");
        }

    }
    private GestureDetector createGestureDetector2(final Context context) {
        GestureDetector gestureDetector = new GestureDetector(context);

        gestureDetector.setBaseListener(new GestureDetector.BaseListener() {
            @Override
            public boolean onGesture(Gesture gesture) {

                if (gesture == Gesture.TAP) {
                    playSoundEffect(Sounds.TAP);
                    Log.d(TAG, ">>>>>>>>>>>>>>> TAP <<<<<<<<<<<<<<<<<");
                    if (actualObjectList.getQtd() == 1) {

                        try {
                            control.sendImage(Constants.OPERATION_TEXT_RECOGNITION, bitmap);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        playSoundEffect(Sounds.SUCCESS);
                    } else {
                        //contextualMenuActivity.setmGestureDetector(mGestureDetector3);


                    }

                    return false;
                } else if (gesture == Gesture.TWO_TAP) {
                    playSoundEffect(Sounds.TAP);
                    Log.d(TAG, ">>>>>>>>>>>>>>> TWO TAP <<<<<<<<<<<<<<<<<");
                    contextualMenuActivity.setmGestureDetector(contextualMenuActivity.getmGestureDetector1());
                    return false;
                }
                else if (gesture == Gesture.THREE_TAP) {
                playSoundEffect(Sounds.DISMISSED);
                Log.d(TAG, ">>>>>>>>>>>>>>> TWO TAP <<<<<<<<<<<<<<<<<");
                contextualMenuActivity.setmGestureDetector(contextualMenuActivity.getmGestureDetector1());
                return false;
             }else if (gesture == Gesture.SWIPE_UP) {
                    Log.d(TAG, ">>>>>>>>>>>>>>> TWO TAP <<<<<<<<<<<<<<<<<");
                    playSoundEffect(Sounds.SELECTED);
                    tts.speak("choosing object");
                   tts.speak(actualObjectList.getTitle());
                  Log.d(TAG, "Actual Object is: " + actualObjectList.getTitle());
                    return false;
                } else if (gesture == Gesture.SWIPE_RIGHT) {
                    playSoundEffect(Sounds.SELECTED);
                    Log.d(TAG, ">>>>>>>>>>>>>>> SWIPE RIGHT <<<<<<<<<<<<<<<<<");
                    if (countAux < listObjects.size() - 1) {
                        countAux++;
                        actualObjectList = listObjects.get(countAux);
                    } else {
                        countAux = 0;
                        actualObjectList = listObjects.get(countAux);
                    }
                  //  tts.speak(actualObjectList.getTitle());
                    Log.d(TAG, "Actual Object is: " + actualObjectList.getTitle());
                    return false;
                } else if (gesture == Gesture.SWIPE_LEFT) {
                    playSoundEffect(Sounds.SELECTED);
                    Log.d(TAG, ">>>>>>>>>>>>>>> SWIPE LEFT <<<<<<<<<<<<<<<<<");
                    if (countAux != 0) {
                        countAux--;
                        actualObjectList = listObjects.get(countAux);
                    } else {
                        countAux = listObjects.size() - 1;
                        actualObjectList = listObjects.get(countAux);
                    }
                   // tts.speak(actualObjectList.getTitle());
                    Log.d(TAG, "Actual Object is: " + actualObjectList.getTitle());
                    return false;
                } else if (gesture == Gesture.LONG_PRESS) {

                    return false;
                }
                return false;
            }
        });
        return gestureDetector;

    }

    private ObjectRecognition actualObject;
    private int position = 0;

    private GestureDetector createGestureDetector3(final Context context) {
        GestureDetector gestureDetector = new GestureDetector(context);

        gestureDetector.setBaseListener(new GestureDetector.BaseListener() {
            @Override
            public boolean onGesture(Gesture gesture) {

                if (gesture == Gesture.TAP) {
                    playSoundEffect(Sounds.TAP);
                    Log.d(TAG, ">>>>>>>>>>>>>>> TAP 3<<<<<<<<<<<<<<<<<");
                    contextualMenuActivity.setmGestureDetector(mGestureDetector4);

                    return false;
                } else if (gesture == Gesture.TWO_TAP) {
                    playSoundEffect(Sounds.TAP);
                    Log.d(TAG, ">>>>>>>>>>>>>>> TWO TAP 3 <<<<<<<<<<<<<<<<<");
                    contextualMenuActivity.setmGestureDetector(mGestureDetector2);
                    return false;
                }else if (gesture == Gesture.THREE_TAP) {
                    playSoundEffect(Sounds.TAP);
                    Log.d(TAG, ">>>>>>>>>>>>>>> TWO TAP 3 <<<<<<<<<<<<<<<<<");
                    contextualMenuActivity.setmGestureDetector(contextualMenuActivity.getmGestureDetector1());
                    return false;
                }
                else if (gesture == Gesture.SWIPE_RIGHT) {
                    playSoundEffect(Sounds.SELECTED);
                    if (position < actualObjectList.getQtd() - 1) {
                        position++;
                    } else {
                        position = 0;
                    }
                    return false;
                } else if (gesture == Gesture.SWIPE_LEFT) {
                    playSoundEffect(Sounds.SELECTED);
                    if (position != 0) {
                        position--;
                    } else {
                        position = actualObjectList.getQtd() - 1;

                    }

                    return false;
                } else if (gesture == Gesture.SWIPE_DOWN) {
                    return false;
                }else if( gesture == Gesture.SWIPE_UP){
                    tts.speak("Choosing position");
                    return false;
                }
                return false;
            }
        });
        return gestureDetector;

    }
    private GestureDetector createGestureDetector4(final Context context) {
        GestureDetector gestureDetector = new GestureDetector(context);

        gestureDetector.setBaseListener(new GestureDetector.BaseListener() {
            @Override
            public boolean onGesture(Gesture gesture) {

                if (gesture == Gesture.TAP) {
                    Log.d(TAG, ">>>>>>>>>>>>>>> tocou no gesture 2<<<<<<<<<<<<<<<<<");
                    Log.d(TAG, ">>>>>>>>>>>>>>> TWO TAP 4 <<<<<<<<<<<<<<<<<");
                    contextualMenuActivity.setmGestureDetector(mGestureDetector3);

                    return false;
                } else if (gesture == Gesture.TWO_TAP) {
                    ObjectRecognition object = actualObjectList.orderSizePosition(position);

                    try {
                        control.sendImage(Constants.OPERATION_TEXT_RECOGNITION, bitmap);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    return false;
                } else if (gesture == Gesture.THREE_TAP) {
                    playSoundEffect(Sounds.TAP);
                    Log.d(TAG, ">>>>>>>>>>>>>>> TWO TAP 3 <<<<<<<<<<<<<<<<<");
                    contextualMenuActivity.setmGestureDetector(contextualMenuActivity.getmGestureDetector1());
                    return false;
                }
                else if (gesture == Gesture.SWIPE_RIGHT) {
                    ObjectRecognition object = actualObjectList.orderHorizontalPosition(position);

                    try {
                        control.sendImage(Constants.OPERATION_TEXT_RECOGNITION, bitmap);
                    }catch (IOException e){
                        e.printStackTrace();
                    }


                    return false;
                } else if (gesture == Gesture.SWIPE_LEFT) {
                    ObjectRecognition object = actualObjectList.orderVerticalPosition(position);

                    try {
                        control.sendImage(Constants.OPERATION_TEXT_RECOGNITION, bitmap);
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                    return false;
                } else if (gesture == Gesture.SWIPE_DOWN) {
                    return false;
                }else if (gesture == Gesture.SWIPE_UP){
                    tts.speak("choosing reference");


                    return false;
                }
                return false;
            }
        });
        return gestureDetector;

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
                            result = " an " + listObject.getTitle() + and;
                            break;
                        default:
                            result = " a " + listObject.getTitle() + and;;
                    }
            }

            else
                result = result + listObject.getQtd() + " " + listObject.getTitle()+and;
        }
        Log.d(TAG, result);
        return result;
    }

    public Bitmap cropImage(ObjectRecognition objectRecognition,Bitmap bitmap){
        int left,top,width,height;
        float razaoHeight = bitmap.getHeight()/(float)Constants.INPUT_SIZE;
        float razaoWidth = bitmap.getWidth()/(float)Constants.INPUT_SIZE;
        // Log.d(TAG,"crop image>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+ razaoHeight+ " " + razaoWidth+ " " + bitmap.getHeight()+ " "+bitmap.getWidth()+"<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

        left = (int)(objectRecognition.getLeft()*razaoWidth);
        top = (int)(objectRecognition.getTop()*razaoHeight);
        width = (int)(objectRecognition.getWidth()*razaoWidth);
        height = (int)(objectRecognition.getHeight()*razaoHeight);
        return Bitmap.createBitmap(bitmap, left,top,width,height);

    }


    private void playSoundEffect(int sound){
        AudioManager audio = (AudioManager) contextualMenuActivity.getSystemService(Context.AUDIO_SERVICE);
        audio.playSoundEffect(sound);
    }
}