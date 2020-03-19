package com.blindassistant.googleglass.Utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class Speak {
    private TextToSpeech tts;
    public Speak(Context context){
        TextToSpeech.OnInitListener listener =
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(final int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            Log.d("TTS", "Text to speech engine started successfully.");
                            //Locale locale = new Locale("PT", "BR");
                            tts.setLanguage(Locale.ENGLISH);
                        } else {
                            Log.d("TTS", "Error starting the text to speech engine.");
                        }
                    }
                };
        tts = new TextToSpeech(context.getApplicationContext(), listener);

        //return tts;
    }
    public void speak(String text){
        tts.speak(text, TextToSpeech.QUEUE_ADD, null);
    }
    public void speak(String text,int mode){
        tts.speak(text, mode, null);
    }
    public void stop(){
        tts.stop();

    }
}