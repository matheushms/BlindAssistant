package com.example.mathe.smarthphone;


import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class Speak {
    private TextToSpeech tts;
    public TextToSpeech speak(Context context){
        TextToSpeech.OnInitListener listener =
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(final int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            Log.d("TTS", "Text to speech engine started successfully.");
                            Locale locale = new Locale("PT", "BR");
                            tts.setLanguage(locale);
                        } else {
                            Log.d("TTS", "Error starting the text to speech engine.");
                        }
                    }
                };
        tts = new TextToSpeech(context.getApplicationContext(), listener);

        return tts;
    }
}