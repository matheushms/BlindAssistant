package com.example.mathe.smartphone;

public interface Constants {
    //Handler Contants
    int STATE_MESSAGE_TEXT_RECOGNITION = 3;
    int STATE_MESSAGE_OBJECT_DETECT= 4;
    public static final int CHANGE_PARAMETERS = 4;
    //Header codes operations
    byte OPERATION_TEXT_RECOGNITION = 1;
    byte OPERATION_OBJECT_RECOGNITION = 2;
    byte OPERATION_RECOGNIZE_TEXT_IN_OBJECT = 3;
    byte OPERATION_OBJECT_RECOGNITION_CONTINUOUS = 4;
    //Bluetooth Constants
    int REQUEST_ENABLE_BT = 1;
    int STATE_MESSAGE_RECEIVED = 3;
    public static final long TIME_THRESHOLD = 30;
    public static final double THRESHOLD_TO_TALK = 0.5;
    public static final int INPUT_SIZE = 300;
    public static final int IMAGE_MEAN = 117;
    public static final float IMAGE_STD = 1;
    public static final String INPUT_NAME = "input";
    public static final String OUTPUT_NAME = "output";
    byte OPERATION_VERIFY = 5;
    byte ERROR = 0;
    byte OK = 1;

     public final static String MODEL_FILE =
            "file:///android_asset/ssd_mobilenet_v1_android_export.pb";
    ;
    public final static String LABEL_FILE =
            "file:///android_asset/coco_labels_list.txt";

    public static final boolean MAINTAIN_ASPECT = true;

}
