package com.example.mathe.smarthphone.ObjectRecognition;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.util.Log;


import org.tensorflow.demo.Classifier;


import org.tensorflow.demo.TensorFlowObjectDetectionAPIModel;

import com.example.mathe.smarthphone.Constants;

import java.util.ArrayList;
import java.util.List;

public class RecognizeObjects {


    private Classifier classifier;
    private List<Classifier.Recognition> resultadosAprovados = new ArrayList<Classifier.Recognition>();
    private AssetManager assets;
    private Context context;
    private Bitmap resizedBitmap;
    private List<Classifier.Recognition> results  = null;


    public RecognizeObjects(AssetManager assets, Context context) {
        this.assets = assets;
        this.context = context;
        try {
            classifier = TensorFlowObjectDetectionAPIModel.create(
                    assets, "file:///android_asset/ssd_mobilenet_v1_android_export.pb", "file:///android_asset/coco_labels_list.txt", 300);
        }catch (Exception io){
            Log.d("problem",">>>>>>>>>>>erro ao carregar o modelo");
        }


    }


    public String recognizeObjects(Bitmap bitmap){
        resizedBitmap = Bitmap.createScaledBitmap(
                bitmap, Constants.INPUT_SIZE, Constants.INPUT_SIZE, false);


        processImage();
        return getResultToSend();

    }
    public List<Classifier.Recognition> recognizeObjects2(Bitmap bitmap){
        resizedBitmap = Bitmap.createScaledBitmap(
                bitmap, Constants.INPUT_SIZE, Constants.INPUT_SIZE, false);


        processImage();
        return resultadosAprovados;

    }
    private Bitmap cropImage(Bitmap bitmap, RectF rectf){
        int left,top,width,height;
        float razao = resizedBitmap.getHeight()/Constants.INPUT_SIZE;
        left = (int)rectf.left;
        top = (int)rectf.top;
        width = (int)rectf.width();
        height = (int)rectf.height();

        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, left,top,width,height );

        return croppedBitmap;
    }
    protected String processImage() {
        resultadosAprovados = new ArrayList<Classifier.Recognition>();
        if(classifier == null)
            Log.d("problem",">>>>>>>>>>> classifier null");
        if(resizedBitmap == null)
            Log.d("problem",">>>>>>>>>>> bitmap null");
        results = classifier.recognizeImage(resizedBitmap);
        String text = "";
        Log.d("reconhecidos", "3");
        for (Classifier.Recognition resultado : results) {
            Log.d("reconhecidos", "5");
            RectF rectF = resultado.getLocation();


            if (resultado.getConfidence() > Constants.THRESHOLD_TO_TALK) {
                Log.d("reconhecidos", resultado.getTitle() + "\n confidence:" + resultado.getConfidence()+"\n Location:"+
                        rectF.left+"  "+rectF.top+"  "+rectF.width()+"  "+rectF.height());

                resultadosAprovados.add(resultado);
                text =text + "\n" + resultado.getTitle();
            }
        }
        return text;



    }
    protected void onPreviewSizeChosen() {



    }

    public String getResultToSend() {
        String result = "";
        for (Classifier.Recognition object : resultadosAprovados) {
            RectF local = object.getLocation();
            result = (result + object.getTitle() + "-" + local.left + "#" + local.top + "#" + local.width() + "#" + local.height() + ";");
            // nomeObjeto-Cleft#Ctop#Cwidth#Cheight;nomeObjeto2-C2left#C2top#C2width#C2height;...nomeObjeton-Cnleft#Cntop#Cnwidth#Cnheight;
        }
        return result;
    }
    public String getNames(){
        String result = "";
        for(Classifier.Recognition object : resultadosAprovados){
            result = (result+object.getTitle()+"\n");
        }
        return result;
    }



}
