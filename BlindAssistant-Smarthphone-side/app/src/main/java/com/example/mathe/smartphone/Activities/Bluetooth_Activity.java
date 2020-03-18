package com.example.mathe.smarthphone.Activities;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.mathe.smarthphone.Bluetooth.Device;
import com.example.mathe.smarthphone.Bluetooth.Test;
import com.example.mathe.smarthphone.Constants;
import com.example.mathe.smarthphone.Data;
import com.example.mathe.smarthphone.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Bluetooth_Activity extends AppCompatActivity {
    private ListView pairedDevicesView;
    private ListView detectedDevicesView;
    private ArrayAdapter<String> detectedArrayAdapter;
    private ArrayAdapter<String> pairedArrayAdapter;
    private List<String> detectedStrings = new ArrayList<String>();
    private List<String> pairedStrings = new ArrayList<String>();
    private ArrayAdapter presentPairedDevices;
    private BluetoothAdapter mBluetoothAdapter;
    private AssetManager assets;
    private Switch aSwitch;
    private Context mcontext;
    private static final String TAG = "Bluetooth_Activity";
    private Data data = new Data();
    private String TAGStorage = "storage";
    private void ativarBluetooth(){
        Log.d(TAG, "enableDisableBT enabling BT.");
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, Constants.REQUEST_ENABLE_BT);
            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }

    }




    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);

                switch(state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    /**
     * Broadcast Receiver for changes made to bluetooth states such as:
     * 1) Discoverability mode on/off or expire.
     */
    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {

                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode) {
                    //Device is in Discoverable Mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Enabled.");
                        break;
                    //Device not in discoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections.");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "mBroadcastReceiver2: Connecting....");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "mBroadcastReceiver2: Connected.");
                        break;
                }

            }
        }
    };


    /**
     * Broadcast Receiver for listing devices that are not yet paired
     * -Executed by btnDiscover() method.
     */
    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {


        @Override

        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
                detectedStrings.add(device.getName()+"\n"+device.getAddress());
                ArrayAdapter<String> detectedArrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,detectedStrings);
                detectedDevicesView.setAdapter(detectedArrayAdapter);



            }
        }
    };


    private void atualizarListaPareados(){
        List<Device> pDevices = new ArrayList<Device>();
        ArrayAdapter mArrayAdapter = null;
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(true){
            for(BluetoothDevice device : pairedDevices) {
                pairedStrings.add(device.getName() + "\n" + device.getAddress());
            }
        }
        ArrayAdapter<String> pairedArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,pairedStrings);
        pairedDevicesView.setAdapter(pairedArrayAdapter);


    }

    private void itemListener(final ListView listView){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long id) {

                String item = (String) listView.getItemAtPosition(position);

                String adress = item.substring(item.indexOf("\n")+1);


                Intent intent = new Intent(getApplicationContext(), BluetoothService.class);
                intent.putExtra("adress",adress);
                startService(intent);
                //BluetoothService bluetoothService = new BluetoothService(data, adress);
                Toast.makeText(getApplicationContext(),"Dispositivo selecionado : "+ item.substring(0,item.indexOf("\n")+1),Toast.LENGTH_SHORT).show();
               }
        });

    }
    private void discover(){
        Log.d(TAG, "btnDiscover: Looking for unpaired devices.");

        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "btnDiscover: Canceling discovery.");

            //check BT permissions in manifest
            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
        if(!mBluetoothAdapter.isDiscovering()){

            //check BT permissions in manifest
            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_activity);


        Test test = new Test(this);
        test.test();

        detectedDevicesView = (ListView)findViewById(R.id.detectedDevices);
        Button btAtualizar = (Button)findViewById(R.id.btAtualizar);
        aSwitch = (Switch)findViewById(R.id.switchOnOff);
        pairedDevicesView = (ListView)findViewById((R.id.pairedDevices));
        isStoragePermissionGranted();
        assets = getAssets();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        itemListener(pairedDevicesView);
        itemListener(detectedDevicesView);
        btAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mBluetoothAdapter.isEnabled()){

                    discover();
                    atualizarListaPareados();
                }else{
                    pairedStrings = new ArrayList<String>();
                    detectedStrings = new ArrayList<String>();

                    atualizarListaPareados();

                }
         }
        });

        aSwitch.setChecked(mBluetoothAdapter.isEnabled());

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!aSwitch.isChecked()) {

                    Log.d(TAG, "enableDisableBT: disabling BT.");
                    mBluetoothAdapter.disable();

                    IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                    registerReceiver(mBroadcastReceiver1, BTIntent);

                }else{

                    ativarBluetooth();
                    discover();
                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: called.");
        super.onDestroy();
//        unregisterReceiver(mBroadcastReceiver1);
  //      unregisterReceiver(mBroadcastReceiver2);
        //mBluetoothAdapter.cancelDiscovery();
    }

    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }




}