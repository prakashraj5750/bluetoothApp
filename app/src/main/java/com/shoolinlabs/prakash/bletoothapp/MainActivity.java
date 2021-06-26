package com.shoolinlabs.prakash.bletoothapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends Activity  {
    Button b1,b2,b3,b4;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice>pairedDevices;
    ListView lv;
    BluetoothDevice mmDevice;
    BluetoothSocket mmSocket;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    boolean acknowledge=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button) findViewById(R.id.button);
        b2=(Button)findViewById(R.id.button2);
        b3=(Button)findViewById(R.id.button3);
        b4=(Button)findViewById(R.id.button4);

        BA = BluetoothAdapter.getDefaultAdapter();
        lv = (ListView)findViewById(R.id.listView);
    }

    public void on(View v){
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Turned on",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Already on", Toast.LENGTH_LONG).show();
        }
    }

    public void off(View v){
        BA.disable();
        Toast.makeText(getApplicationContext(), "Turned off" ,Toast.LENGTH_LONG).show();
    }


    public  void visible(View v){
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);
    }


    public void list(View v) throws IOException {
        pairedDevices = BA.getBondedDevices();

        final ArrayList list1 = new ArrayList();

        for(BluetoothDevice bt : pairedDevices) list1.add(bt.getName());
        Toast.makeText(getApplicationContext(), "Showing Paired Devices",Toast.LENGTH_SHORT).show();

        final ArrayAdapter adapter = new  ArrayAdapter(this,android.R.layout.simple_list_item_1, list1);

        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for(BluetoothDevice dev: pairedDevices){
                    if(dev.getName().equals(adapter.getItem(i))){
                        mmDevice = dev;
                        Log.i("Bluetooth device", mmDevice.getName());

                    }
                }

            }


        });



        }
        public void connect(View v){
        try{
            if(mmDevice != null){



                connection();
                if(acknowledge == true){
                    Toast.makeText(this, "connected", Toast.LENGTH_LONG).show();
                }


            }
        }catch (Exception e){
            e.printStackTrace();
        }


        }

    private void connection() {
        try{
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            acknowledge = true;
        }catch (Exception e){
            e.printStackTrace();
            acknowledge = false;
        }


    }


    public void OnLed(View v){
        try{
            if(acknowledge==true){
                ledON();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }



    }

    private void ledON() {
        String string = "1";
        try {
            mmOutputStream.write(string.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void OffLed(View v) {
        try{
            if(acknowledge == true){
                ledOff();
            }
        }
       catch (Exception e){
            e.printStackTrace();
       }



    }

    private void ledOff() {
        try{
            char str = '0';
            mmOutputStream.write((byte)str);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}