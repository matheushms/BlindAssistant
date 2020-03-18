package com.mhms.poliprojects.toblindpeopleimersion.Objects;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class CollectionObject {
    private static String TAG = "CollectionObject";
    private List<ObjectRecognition> objects;
    private int qtd = 0;
    private String title;
    Bitmap bitmap = null;
    private  List<ObjectRecognition> objectsH = new ArrayList<>();
    private  List<ObjectRecognition> objectsV = new ArrayList<>();

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public CollectionObject(){
        objects = new ArrayList<ObjectRecognition>();
    }
    public void add(ObjectRecognition object){
        object.setId(qtd);
        qtd++;
        objects.add(object);
    }
    public void clean(){
        objects = new ArrayList<ObjectRecognition>();
        qtd = 0;
    }
    @Override
    public String toString(){
        String result = "";
        for(ObjectRecognition object: objects){
            result = result+object.getTitle()+"\n";
        }
        return result;
    }
    public List<ObjectRecognition> horizontalPosition(){
        List<ObjectRecognition> objectsH = new ArrayList<>();
        objectsH.addAll(objects);
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


        return  objectsH;
    }


    public List<ObjectRecognition> objectsWithSameTitle(ObjectRecognition objectO){
        List<ObjectRecognition> objectsWST = new ArrayList<>();
        for(ObjectRecognition object: objects){
            if(object.getTitle().equals(objectO.getTitle()))
                objectsWST.add(object);
        }
        return objectsWST;
    }
    private double distance(ObjectRecognition o1,ObjectRecognition o2){
        double deltax = Math.pow(o1.getLeft(),2) - Math.pow(o2.getLeft(),2);
        double deltay = Math.pow(o1.getTop(),2) - Math.pow(o2.getTop(),2);

        return Math.sqrt(Math.abs(deltax)+Math.abs(deltay));


    }


    public  boolean isEquals(CollectionObject previousObj){
        boolean isEquals = true;
        boolean aux = true;
        List<CollectionObject> previousObjects = previousObj.getListsObjects();
        List<CollectionObject> actualObjects = getListsObjects();
        if(previousObjects.size() == actualObjects.size()) {
            for (CollectionObject listPreObj : previousObjects) {
                for (CollectionObject listActObj : actualObjects) {

                    if (listActObj.getTitle().equals(listPreObj.getTitle())) {
                        isEquals = true;
                        break;
                    } else {
                        isEquals = false;
                    }

                }
                if(isEquals == false) break;
            }

            if(isEquals == true){
                for (CollectionObject listPreObj : previousObjects) {
                    for (CollectionObject listActObj : actualObjects) {

                        if (listActObj.getQtd() == listPreObj.getQtd()) {
                            isEquals = true;
                            break;
                        } else {
                            isEquals = false;
                        }

                    }
                    if(isEquals == false) break;
                }
            }
        }
        return  isEquals;
    }

    /*
    public ObjectRecognition objectWithMinDistance(ObjectRecognition o1, String name){
        ObjectRecognition objectMoreNext = objects.get(0);
        distance()
        for(ObjectRecognition object: objects){
            if
        }
        return  objectMoreNext;
    }*/

    public List<ObjectRecognition> verticalPosition(){


        objectsV.addAll(objects);
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


        return  objectsH;
    }
    public List<ObjectRecognition> getObjects() {
        return objects;
    }

    public List<CollectionObject> getListsObjects(){
        List<CollectionObject> list = new ArrayList<CollectionObject>();
        while(!objects.isEmpty()){
            String title = objects.get(0).getTitle();
            CollectionObject collectionObject = new CollectionObject();
            List<ObjectRecognition> thingsToRemove = new ArrayList<ObjectRecognition>();
            Iterator<ObjectRecognition> iter = objects.iterator();
            while (iter.hasNext()) {
                ObjectRecognition object = iter.next();
                if(title.equals(object.getTitle())){
                    collectionObject.add(object);
                    thingsToRemove.add(object);
               //     objects.remove(object);
                }
            }
            objects.removeAll(thingsToRemove);

            collectionObject.setTitle(title);
            list.add(collectionObject);




        }


        return  list;
    }

    public ObjectRecognition orderSizePosition(int position){

        // Em ordem crescente do início do mandato
        Collections.sort(objects, new Comparator() {
            public int compare(Object o1, Object o2) {
                ObjectRecognition p1 = (ObjectRecognition) o1;
                ObjectRecognition p2 = (ObjectRecognition) o2;
                float x1 = ((ObjectRecognition) o1).getTamanho();
                float x2 = ((ObjectRecognition) o2).getTamanho();
                return x1 < x2 ? -1 : (x1 > x2 ? +1 : 0);
            }
        });

        return objects.get(position);
    }
    public ObjectRecognition inverseOrderSizePosition(int position){
        // Em ordem crescente do início do mandato
        Collections.sort(objects, new Comparator() {
            public int compare(Object o1, Object o2) {
                ObjectRecognition p1 = (ObjectRecognition) o1;
                ObjectRecognition p2 = (ObjectRecognition) o2;
                float x1 = ((ObjectRecognition) o1).getTamanho();
                float x2 = ((ObjectRecognition) o2).getTamanho();
                return x1 < x2 ? -1 : (x1 > x2 ? +1 : 0);
            }
        });
        Collections.reverse(objects);
        return objects.get(position);
    }
    public ObjectRecognition orderHorizontalPosition(int position){


        // Em ordem crescente do início do mandato
        Collections.sort(objects, new Comparator() {
            public int compare(Object o1, Object o2) {
                ObjectRecognition p1 = (ObjectRecognition) o1;
                ObjectRecognition p2 = (ObjectRecognition) o2;
                float x1 = (p1.getLeft() + p1.getWidth() / 2);
                float x2 = (p2.getLeft() + p2.getWidth() / 2);
                return x1 < x2 ? -1 : (x1 > x2 ? +1 : 0);
            }
        });
        return objects.get(position);
    }
    public ObjectRecognition orderVerticalPosition(int position){


        // Em ordem crescente do início do mandato
        Collections.sort (objects, new Comparator() {
            public int compare(Object o1, Object o2) {
                ObjectRecognition p1 = (ObjectRecognition) o1;
                ObjectRecognition p2 =(ObjectRecognition) o2;
                float x1 = (p1.getTop()+p1.getHeight()/2);
                float x2 = (p2.getTop()+p2.getHeight()/2);
                return x1 < x2 ? -1 :( x1 > x2 ? +1 : 0);
            }
        });

        return objects.get(position);
    }


    public int getQtd() {
        return qtd;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String objectsToString(){
        String text = "";
        String and;
        List<CollectionObject> listObjects= this.getListsObjects();
        Iterator<CollectionObject> iter = listObjects.iterator();

        while (iter.hasNext()) {
            CollectionObject listObject = iter.next();

            if(!iter.hasNext())
                and = "";
            else
                and = " and ";



            if(listObject.getQtd() == 1)
                text = text + " a "+ listObject.getTitle()+and;
            else
                text = text + listObject.getQtd() + " " + listObject.getTitle()+and;
        }
        Log.d("objects",text);
        return text;
    }
    public static CollectionObject separateDataObjects(String data){
        CollectionObject objects= new CollectionObject();
        objects.clean();
        objects = new CollectionObject();
        if(!data.equals("")) {

            String[] objectData = data.split(";");
            for (String object : objectData) {
                if(object.contains("-") && object.contains("#")){
                    String[] parts = object.split("-");

                    String[] coordinates = parts[1].split("#");
                    try {
                        float left = (Float.parseFloat(coordinates[0]));
                        float top = (Float.parseFloat(coordinates[1]));
                        float width = (Float.parseFloat(coordinates[2]));
                        float heigth = (Float.parseFloat(coordinates[3]));
                        Log.d(TAG, "Posições>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + left + " " + top + " " + width + " " + heigth + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                        ObjectRecognition objectR = new ObjectRecognition(parts[0], top, left, width, heigth);
                        objects.add(objectR);

                    }catch (Exception e){

                    }
                }
            }
        }
        return  objects;
    }
}