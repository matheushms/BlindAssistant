package com.blindassistant.googleglass.Constants;

public interface Constants {
    //Handler Contants
    int STATE_MESSAGE_TEXT_RECOGNITION = 3;
    int STATE_MESSAGE_OBJECT_DETECT= 4;

    //Header codes operations
    byte OPERATION_TEXT_RECOGNITION = 1;
    byte OPERATION_OBJECT_RECOGNITION = 2;
    byte OPERATION_RECOGNIZE_TEXT_IN_OBJECT = 3;
    byte OPERATION_OBJECT_RECOGNITION_CONTINUOUS = 4;
    byte OPERATION_VERIFY = 5;
    byte ERROR = 0;
    byte OK = 1;


    //Bluetooth Constants
    int REQUEST_ENABLE_BT = 1;

    int INPUT_SIZE = 300;

    // VARIABLES OF MENU OPTIONS
    //GroupIDs:
    int GROUPID_MENU = 1;

    int GROUPID_READ_OBJECTS = 2;

    int GROUPID_OPTIONS = 3;

    int GROUPID_LANGUAGE = 4;

    int GROUPID_SINGLE_OBJECTS = 5;

    int GROUPID_MORE_ONE = 6;

    int GROUPID_POSITION = 7;
    //ItemIDs:
    //groupId menu
    int READ_OBJECTS = 0;
    int OBJECTS = 1;
    int WAIT_CONNECTION = 2;
    int OPTIONS = 3;
    int QUIT =4;

    //groupId options
    int CHANGE_LANGUAGE = 1;

    public static final int THE_FIRST = 0;
    public static final int THE_SECOND = 1;
    public static final int THE_THIRD = 2;
    public static final int THE_FOURTH = 3;
    public static final int THE_FIFTH = 4;


    /*  public final static String MODEL_FILE =
            "file:///android_asset/tensorflow_inception_graph.pb";
    ;
    public final static String LABEL_FILE =
            "file:///android_asset/imagenet_comp_graph_label_strings_pt_br.txt";
*/
    public final static String MODEL_FILE =
            "file:///android_asset/ssd_mobilenet_v1_android_export.pb";
    ;
    public final static String LABEL_FILE =
            "file:///android_asset/coco_labels_list.txt";

    public static final boolean MAINTAIN_ASPECT = true;
}
