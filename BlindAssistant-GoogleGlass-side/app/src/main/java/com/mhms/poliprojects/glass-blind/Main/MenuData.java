package com.mhms.poliprojects.toblindpeopleimersion.Main;

        import android.graphics.Bitmap;
        import android.speech.tts.TextToSpeech;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.SubMenu;

        import com.google.android.glass.view.WindowUtils;
        import com.mhms.poliprojects.toblindpeopleimersion.Bluetooth.BluetoothController;
        import com.mhms.poliprojects.toblindpeopleimersion.Constants.Constants;
        import com.mhms.poliprojects.toblindpeopleimersion.Constants.ConstantsM;
        import com.mhms.poliprojects.toblindpeopleimersion.Constants.ConstantsPI;
        import com.mhms.poliprojects.toblindpeopleimersion.Objects.CollectionObject;
        import com.mhms.poliprojects.toblindpeopleimersion.Objects.ObjectRecognition;
        import com.mhms.poliprojects.toblindpeopleimersion.Utils.Speak;

        import java.io.IOException;
        import java.io.InputStream;
        import java.util.ArrayList;
        import java.util.Iterator;
        import java.util.List;

public class MenuData {
    private String[] messages;
    private String [] possibles_inputs;
    private String[] options_menu;
    private ContextualMenuActivity menuActivity;
    private String language = "EN";
    private List<CollectionObject> ListObjects;
    private Menu menu;
    private List<ObjectRecognition> singleObjects;
    private Speak tts;
    private int position;
    private CollectionObject objetoDesejado;
    private ControladorMain control;
    private String objectsText;
    private CustomApplication customApplication;
    Bitmap bitmap;
    public MenuData(ContextualMenuActivity menuActivity,ControladorMain control){
        customApplication = (CustomApplication)menuActivity.getApplication();
        this.menuActivity = menuActivity;
        this.control = control;
        tts = new Speak(menuActivity.getApplicationContext());
        initialize(language);


    }



    private void initialize(String language) {
        String buffer = null;

        try {
            InputStream in = menuActivity.getAssets().open("messages_" + language + ".txt");
            int size = in.available();
            byte[] buffer1 = new byte[size];
            in.read(buffer1);
            in.close();
            buffer = new String(buffer1);
            messages = buffer.split("\n");

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("possible", ">>>>>>>>>>Error ao abrir o arquivo messages");
        }
        try {
            InputStream in = menuActivity.getAssets().open("possibles_inputs_" + language + ".txt");
            int size = in.available();
            byte[] buffer1 = new byte[size];
            in.read(buffer1);
            in.close();
            buffer = new String(buffer1);
            possibles_inputs = buffer.split("\n");
            Log.d("possible",">>>>>>>>>>" + possibles_inputs[0]);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("possible", ">>>>>>>>>>Error ao abrir o arquivo possibles Inputs");
        }
        try {
            InputStream in = menuActivity.getAssets().open("options_menu_" + language + ".txt");
            int size = in.available();
            byte[] buffer1 = new byte[size];
            in.read(buffer1);
            in.close();
            buffer = new String(buffer1);
            options_menu = buffer.split("\n");

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("possible", ">>>>>>>>>>Error ao abrir o arquivo options menu");

        }
    }
    public void preparateMainMenu(){
        menu.addSubMenu(Constants.GROUPID_MENU,Constants.READ_OBJECTS,0,options_menu[Constants.READ_OBJECTS]);
        menu.addSubMenu(Constants.GROUPID_MENU,Constants.OBJECTS,0,options_menu[Constants.OBJECTS]);
        menu.addSubMenu(Constants.GROUPID_MENU,Constants.WAIT_CONNECTION,0,options_menu[Constants.WAIT_CONNECTION]);
        //menu.addSubMenu(Constants.GROUPID_MENU,Constants.OPTIONS,0,options_menu[Constants.OPTIONS]);
//        menu.addSubMenu(Constants.GROUPID_MENU,Constants.QUIT,0,options_menu[Constants.QUIT]);

        preparateReadObjectsMenu();
    }

    private void preparateReadObjectsMenu(){
        Menu subMenu = menu.findItem(Constants.READ_OBJECTS).getSubMenu();

        subMenu.addSubMenu(Constants.GROUPID_READ_OBJECTS, Constants.THE_FIRST,1,possibles_inputs[ConstantsPI.THE_FIRST]);
        subMenu.addSubMenu(Constants.GROUPID_READ_OBJECTS, Constants.THE_SECOND,1, possibles_inputs[ConstantsPI.THE_SECOND]);
        subMenu.addSubMenu(Constants.GROUPID_READ_OBJECTS, Constants.THE_THIRD, 1, possibles_inputs[ConstantsPI.THE_THIRD]);
        subMenu.addSubMenu(Constants.GROUPID_READ_OBJECTS, Constants.THE_FOURTH,1, possibles_inputs[ConstantsPI.THE_FOURTH]);
        subMenu.addSubMenu(Constants.GROUPID_READ_OBJECTS, Constants.THE_FIFTH, 1, possibles_inputs[ConstantsPI.THE_FIFTH]);

        singleObjects = new ArrayList<ObjectRecognition>();
        Iterator<CollectionObject> iter = ListObjects.iterator();
        List<CollectionObject> thingsToRemove = new ArrayList<CollectionObject>();
        while (iter.hasNext()) {
            CollectionObject listObject = iter.next();
            if(listObject.getQtd() == 1){
                singleObjects.add(listObject.getObjects().get(0));
                subMenu.add(Constants.GROUPID_SINGLE_OBJECTS, 1, 1, listObject.getTitle());
                thingsToRemove.add(listObject);
                // ListObjects.remove(listObject);
            }

        }
        ListObjects.removeAll(thingsToRemove);

        Menu subMenu1 = subMenu.findItem(Constants.THE_FIRST).getSubMenu();
        preparateMoreOneMenu(subMenu1,2);
        subMenu1 = subMenu.getItem(Constants.THE_SECOND).getSubMenu();
        preparateMoreOneMenu(subMenu1,2);
        subMenu1 = subMenu.getItem(Constants.THE_THIRD).getSubMenu();
        preparateMoreOneMenu(subMenu1,3);
        subMenu1 = subMenu.getItem(Constants.THE_FOURTH).getSubMenu();
        preparateMoreOneMenu(subMenu1,4);
        subMenu1 = subMenu.getItem(Constants.THE_FIFTH).getSubMenu();
        preparateMoreOneMenu(subMenu1,5);


    }

    private void preparateMoreOneMenu(Menu subMenu, int qtd){
        int count =Menu.FIRST;

        Iterator<CollectionObject> iter = ListObjects.iterator();

        while (iter.hasNext()) {
            CollectionObject listObject = iter.next();
            if(listObject.getQtd()>=qtd){
                if(subMenu == null)
                    Log.d("problem",">>>>>>>>>>>>>>>>> SubMenu Nulll");


                Log.d("problem",">>>>>>>>>>>>>>>"+listObject.getTitle());
                subMenu.addSubMenu(Constants.GROUPID_MORE_ONE, count,2, listObject.getTitle());
                count++;
            }
        }
        for(int i = Menu.FIRST-1 ;i <count-1; i++){

            SubMenu subMenu1 = subMenu.getItem(i).getSubMenu();

            subMenu1.add(Constants.GROUPID_POSITION, ConstantsPI.IN_HORIZONTAL, 3, possibles_inputs[ConstantsPI.IN_HORIZONTAL]);
            subMenu1.add(Constants.GROUPID_POSITION, ConstantsPI.IN_VERTICAL, 3,possibles_inputs[ConstantsPI.IN_VERTICAL]);
            subMenu1.add(Constants.GROUPID_POSITION, ConstantsPI.LARGEST,3,possibles_inputs[ConstantsPI.LARGEST]);
            subMenu1.add(Constants.GROUPID_POSITION, ConstantsPI.SMALLER,3,possibles_inputs[ConstantsPI.SMALLER]);


        }

    }

    //////////////REALIZE ACTIONS
    public void realizeAction(MenuItem item){
        BluetoothController bluetoothController = control.getBluetoothController();
        if(item.getGroupId() == Constants.GROUPID_MENU){
            actionGroupMenu(item);
        }else if(item.getGroupId() == Constants.GROUPID_READ_OBJECTS){
            if(bluetoothController.socketIsConnected())
                actionGroupReadObjects(item);
            else{
                tts.speak(messages[ConstantsM.NOT_CONNECTED]);
            }
        }else if(item.getGroupId() == Constants.GROUPID_OPTIONS){

        }else if(item.getGroupId() == Constants.GROUPID_POSITION){
            actionGroupPosition(item);
        }else if(item.getGroupId() == Constants.GROUPID_SINGLE_OBJECTS){
            actionGroupSingleObjects(item);
        }else if(item.getGroupId() == Constants.GROUPID_MORE_ONE){

        }
    }

    private void actionGroupPosition(MenuItem item){
        ObjectRecognition object = null;

        switch(item.getItemId()){

            case ConstantsPI.IN_HORIZONTAL:
                object = objetoDesejado.orderHorizontalPosition(position);

                break;
            case ConstantsPI.IN_VERTICAL:
                object = objetoDesejado.orderVerticalPosition(position);

                break;
            case ConstantsPI.SMALLER:
                object = objetoDesejado.orderSizePosition(position);

                break;
            case ConstantsPI.LARGEST:
                object = objetoDesejado.inverseOrderSizePosition(position);

                break;
        }

        bitmap = cropImage(object,customApplication.getActualAnalisys());
        try{
        control.sendImage(Constants.OPERATION_TEXT_RECOGNITION, bitmap);
        }catch(IOException e){

        }

        control.setPermissionToUpdateMenu(true);
        menuActivity.getWindow().invalidatePanelMenu(WindowUtils.FEATURE_VOICE_COMMANDS);
    }
    private void actionGroupMenu(MenuItem item){
        switch(item.getItemId()){
            case Constants.READ_OBJECTS:

                tts.speak(objectsText);
                break;
            case Constants.OBJECTS:
                Log.d("problem",">>>>>>>>>>>>Option Objects: "+objectsText+"<<<<<<<<<<<<<<");
                tts.speak(objectsText);
                break;
            case Constants.WAIT_CONNECTION:
               // control.waitConnection();
                break;
            case Constants.OPTIONS:
                break;
            case Constants.QUIT:
                Log.d("Problem", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Speak Text >>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<");
                tts.speak("Funcionando");
                break;

        }
    }
    private void actionGroupReadObjects(MenuItem item){
        switch(item.getItemId()){
            case ConstantsPI.THE_FIRST:
                position = 1;
                break;
            case ConstantsPI.THE_SECOND:
                position = 2;
                break;
            case ConstantsPI.THE_THIRD:
                position = 3;
                break;
            case ConstantsPI.THE_FOURTH:
                position = 4;
                break;
            case ConstantsPI.THE_FIFTH:
                position = 5;
                break;

        }
    }
    private void actionGroupSingleObjects(MenuItem item){
        Iterator<CollectionObject> iter = ListObjects.iterator();
        ObjectRecognition object = null;

        for(int i = 0; i < singleObjects.size(); i++) {
            ObjectRecognition listObject = singleObjects.get(i);
            Log.d("problem",listObject.getTitle()+"<<<<<<<<<<<<<<<<");
            if(item.getTitle().equals(listObject.getTitle())){
                object = listObject;
                Log.d("problem",listObject.getTitle()+"<<<<<<<<<<<<<<<<");
                Log.d("problem",listObject.getTitle()+"<<<<<<<<<<<<<<<<");
              //  Log.d("problem",listObject.getObjects().get(1).getTitle()+"<<<<<<<<<<<<<<<<");
                break;
            }
        }

        if(customApplication.getBitmap()== null)
        Log.d("problem",">>>>>>>>MenuData bitmap is null in actionSingleObjects");
        if(object== null)
            Log.d("problem",">>>>>>>>MenuData object is null in actionSingleObjects");
            bitmap = cropImage(object, customApplication.getActualAnalisys());
        try{
            control.sendImage(Constants.OPERATION_TEXT_RECOGNITION, bitmap);
        }catch(IOException e){

        }

        control.setPermissionToUpdateMenu(true);
        menuActivity.getWindow().invalidatePanelMenu(WindowUtils.FEATURE_VOICE_COMMANDS);
    }

    private void actionGroupMoreOne(MenuItem item){
        Iterator<CollectionObject> iter = ListObjects.iterator();

        while (iter.hasNext()) {
            CollectionObject listObject = iter.next();
            if(item.getTitle().equals(listObject.getTitle())) {
                objetoDesejado = listObject;
                objetoDesejado.setBitmap(listObject.getBitmap());
                break;
            }

        }
    }
    private String objectsToString(){
        String text = "";
        String and;
        Iterator<CollectionObject> iter = ListObjects.iterator();

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


    ///////////////METHODS AUXILIARES

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
    //////////////GETS AND SETS

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public List<CollectionObject> getListObjects() {

        return ListObjects;
    }

    public void setListObjects(List<CollectionObject> listObjects) {
        ListObjects = listObjects;
        customApplication = (CustomApplication)menuActivity.getApplication();
        customApplication.setActualAnalisys();
        objectsText = objectsToString();
    }
}
