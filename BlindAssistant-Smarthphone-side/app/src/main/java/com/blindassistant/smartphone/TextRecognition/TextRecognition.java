package com.blindassistant.smartphone.TextRecognition;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class TextRecognition {

    public String textrecognition(Bitmap imageBitmap, Context context) {
        if (imageBitmap != null) {

            TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();





            Frame imageFrame = new Frame.Builder()
                    .setBitmap(imageBitmap)
                    .build();

            SparseArray<TextBlock> items = textRecognizer.detect(imageFrame);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < items.size(); ++i) {
                TextBlock item = items.valueAt(i);
                stringBuilder.append(item.getValue());
                stringBuilder.append("\n");
            }
            return stringBuilder.toString();
        }
        return "";
    }

}
