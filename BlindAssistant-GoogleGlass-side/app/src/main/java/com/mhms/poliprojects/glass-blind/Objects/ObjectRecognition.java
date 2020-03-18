package com.mhms.poliprojects.toblindpeopleimersion.Objects;

public class ObjectRecognition {
    private int id;
    private String title;

    private float top,left,width, height;


    public ObjectRecognition(String title, float top,float left,float width, float height){
        this.id = id;
        this.title = title;
        this.top = top;
        this.left = left;
        this.width = width;
        this.height = height;

    }
    public ObjectRecognition(String title, int id, float top,float left,float width, float height){
        this.id = id;
        this.title = title;
        this.top = top;
        this.left = left;
        this.width = width;
        this.height = height;

    }
    public float getTamanho(){
        return width*height;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }


    public void setId(int id) {
        this.id = id;
    }

    public float getTop() {
        return top;
    }

    public float getHeight() {
        return height;
    }

    public float getLeft() {
        return left;
    }

    public float getWidth() {
        return width;
    }
}