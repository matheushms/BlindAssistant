package com.blindassistant.googleglass.Main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;

import com.blindassistant.googleglass.Constants.ConstantsPI;
import com.blindassistant.googleglass.Objects.CollectionObject;
import com.blindassistant.googleglass.Objects.ObjectRecognition;

import com.blindassistant.googleglass.Utils.Speak;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class SpeechActivity extends Activity {

    private ControladorMain control;
    private CustomApplication customApplication;
    private int SPEECH_REQUEST = 0 ;
    private int SPEECH_REQUEST_REPEAT = 3;
    private int SPEECH_REQUEST_MORE_ONE = 4;
    private String TAG = "speechActivity";
    private Speak tts;
    private String[] messages = null;
    private List<ObjectRecognition> objectsWST =null;
    List<ObjectRecognition> objects = null;
    private CollectionObject objectsC = null;
    /*     000 - The objects are
             001 - Can you repeat, please*/
    private String[] possibles_inputs = null;

 /*      000 - in horizontal     001 - in vertical
            002 - lagerst           003 - smaller  004 - the first
            005 - the second        006 - the third
            007 - the fourth        008 - the fifth
            009 - the sixth         010 - the seventh
            011 - the eight         012 - the ninth
            013 - repeat            014 - yes
            015 - no
            */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customApplication = (CustomApplication)getApplication();
        control = customApplication.getControl();
        objects = control.getObjects().getObjects();
        objectsC = control.getObjects();
        initialize("EN");
        tts = (new Speak(this));
        displaySpeechRecognizer(SPEECH_REQUEST);

    }
    public void displaySpeechRecognizer(int operation) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        startActivityForResult(intent, operation);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            speechRequest(spokenText);

        }else if (requestCode == SPEECH_REQUEST_REPEAT && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            speechRequestRepeat(spokenText);
        } else if (requestCode == SPEECH_REQUEST_MORE_ONE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            speechRequestMoreOne(spokenText);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void speechRequest(String spokenText){

        Log.d(TAG, "Spoken>>>>>>>>>>>>>>>>>>>>>>> " + spokenText);
        //Se o usuário pedir para repetir o que foi dito
        if(spokenText.compareToIgnoreCase(possibles_inputs[ConstantsPI.REPEAT])==0){
            String text = control.getObjects().toString();
            tts.speak(text);

            displaySpeechRecognizer(SPEECH_REQUEST);



        }else {
            control = customApplication.getControl();

            ObjectRecognition object1 = null;
            //Procura o Objeto Desejado
            for (ObjectRecognition object : objects) {
                if (spokenText.compareToIgnoreCase(object.getTitle())==0)
                    object1 = object;
            }
            if(object1 == null){
                //Can you repeat, please?
                tts.speak(messages[1]);
                displaySpeechRecognizer(SPEECH_REQUEST_REPEAT);
            }else{

            }
        }


    }
    private void speechRequestMoreOne(String spokenText){
        int qtd = extenseToDigit(spokenText,objectsWST.size());
        if(qtd == -1){
           // String message =messages[ConstantsM.CAN_U_REPEAT];
         //   tts.speak(message,TextToSpeech.QUEUE_ADD, null);
            displaySpeechRecognizer(SPEECH_REQUEST_MORE_ONE);
        }else if(qtd == -2){
           // String message =messages[ConstantsM.SIMILAR_OBJECTS]+ " "+ objectsWST.size()+" "+messages[ConstantsM.OBJECTS];
          //  tts.speak(message,TextToSpeech.QUEUE_ADD, null);
          //  message =messages[ConstantsM.CAN_U_REPEAT];
         //   tts.speak(message,TextToSpeech.QUEUE_ADD, null);
            displaySpeechRecognizer(SPEECH_REQUEST_MORE_ONE);
        }else{

        }
    }

    private void readLenghtVerticalHorizontal(String string, int position) {
        ObjectRecognition object;
        if (string.toLowerCase().contains(possibles_inputs[ConstantsPI.IN_HORIZONTAL])) {
            orderHorizontalPosition(objectsWST);

        }else if (string.toLowerCase().contains(possibles_inputs[ConstantsPI.IN_VERTICAL])) {
            orderVerticalPosition(objectsWST);
        }else if (string.toLowerCase().contains(possibles_inputs[ConstantsPI.LARGEST])) {
            tamanhoDiferenca(objectsWST);
        }else if (string.toLowerCase().contains(possibles_inputs[ConstantsPI.SMALLER])) {
            Collections.reverse(objectsWST);
        }else{
           // String message = messages[ConstantsM.CAN_U_REPEAT];
          //  tts.speak(message, TextToSpeech.QUEUE_ADD, null);
            displaySpeechRecognizer(SPEECH_REQUEST_MORE_ONE);
            return;
        }
        object = objectsWST.get(position-1);
     //   Bitmap bitmap = //cropImage(object);
        //control.sendImage(Constants.OPERATION_TEXT_RECOGNITION, customApplication.getBitmap());
        finish();
    }
    private void moreOneObject(ObjectRecognition object, String spokenText){
        objectsWST = objectsC.objectsWithSameTitle(object);
        if(objectsWST.size()>1){//quando há mais de um objeto
           // String message = messages[ConstantsM.THERE_ARE] + " "+objectsWST.size()+" "+messages[ConstantsM.SIMILAR_OBJECTS];
          //  tts.speak(message,TextToSpeech.QUEUE_ADD, null);
            displaySpeechRecognizer(SPEECH_REQUEST_MORE_ONE);
        }else{//quando só há um objeto

        }
    }

    //private Bitmap cropImage(ObjectRecognition object1){
       // Log.d//(TAG, "Objeto>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + object1.getLeft() + " " + object1.getTop() + " " + object1.getWidth() + " " + object1.getHeight() + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        //Bitmap cropImage = control.cropImage(control.getObjects().getBitmap(),object1);
       // return  cropImage;
   // }

    private int extenseToDigit(String string, int max){
        if(string.toLowerCase().contains(possibles_inputs[ConstantsPI.THE_FIRST])){
            if(max >=1)
                return 1;
            return -2;
        }else if(string.toLowerCase().contains(possibles_inputs[ConstantsPI.THE_SECOND])){
            if(max >=2)
                return 2;
            return -2;
        }else if(string.toLowerCase().contains(possibles_inputs[ConstantsPI.THE_THIRD])){
            if(max >=3)
                return 3;
            return -2;
        }else if(string.toLowerCase().contains(possibles_inputs[ConstantsPI.THE_FOURTH])){
            if(max >=4)
                return 4;
            return -2;
        }else if(string.toLowerCase().contains(possibles_inputs[ConstantsPI.THE_FIFTH])){
            if(max >=5)
                return 5;
            return -2;
        }else if(string.toLowerCase().contains(possibles_inputs[ConstantsPI.THE_SIXTH])){
            if(max >=6)
                return 6;
            return -2;
        }else if(string.toLowerCase().contains(possibles_inputs[ConstantsPI.THE_SEVENTH])){
            if(max >=7)
                return 7;
            return -2;
        }else
            return -1;
    }
    private void speechRequestRepeat(String result){
        Log.d(TAG, "Spoken>>>>>>>>>>>>>>>>>>>>>>> " + result);
        // yes
        if(result.compareToIgnoreCase(possibles_inputs[ConstantsPI.YES])==0){
            displaySpeechRecognizer(SPEECH_REQUEST);
        }else {
            finish();
        }
    }
    @Override
    public void finish() {

        super.finish();
    }

    public void tamanhoDiferenca(List<ObjectRecognition> objectsTamanho){

        // Em ordem crescente do início do mandato
        Collections.sort(objectsTamanho, new Comparator() {
            public int compare(Object o1, Object o2) {
                ObjectRecognition p1 = (ObjectRecognition) o1;
                ObjectRecognition p2 = (ObjectRecognition) o2;
                float x1 = ((ObjectRecognition) o1).getTamanho();
                float x2 = ((ObjectRecognition) o2).getTamanho();
                return x1 < x2 ? -1 : (x1 > x2 ? +1 : 0);
            }
        });

    }
    public void orderHorizontalPosition(List<ObjectRecognition> objectsH){


        // Em ordem crescente do início do mandato
        Collections.sort(objectsH, new Comparator() {
            public int compare(Object o1, Object o2) {
                ObjectRecognition p1 = (ObjectRecognition) o1;
                ObjectRecognition p2 = (ObjectRecognition) o2;
                float x1 = (p1.getLeft() + p1.getWidth() / 2);
                float x2 = (p2.getLeft() + p2.getWidth() / 2);
                return x1 < x2 ? -1 : (x1 > x2 ? +1 : 0);
            }
        });

    }
    public void orderVerticalPosition(List<ObjectRecognition> objectsV){


        // Em ordem crescente do início do mandato
        Collections.sort (objectsV, new Comparator() {
            public int compare(Object o1, Object o2) {
                ObjectRecognition p1 = (ObjectRecognition) o1;
                ObjectRecognition p2 =(ObjectRecognition) o2;
                float x1 = (p1.getTop()+p1.getHeight()/2);
                float x2 = (p2.getTop()+p2.getHeight()/2);
                return x1 < x2 ? -1 :( x1 > x2 ? +1 : 0);
            }
        });


    }
    private void initialize(String language){
        String buffer = null;

        try{
            InputStream in = getAssets().open("messages_"+language+".txt");
            int size = in.available();
            byte[] buffer1 = new byte[size];
            in.read(buffer1);
            in.close();
            buffer = new String(buffer1);

            messages = buffer.split("\n");
        }catch (IOException e){
            e.printStackTrace();
        }try{
            InputStream in = getAssets().open("possibles_inputs_"+language+".txt");
            int size = in.available();
            byte[] buffer1 = new byte[size];
            in.read(buffer1);
            in.close();
            buffer = new String(buffer1);
            String[] texts = null;
            possibles_inputs = buffer.split("\n");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
