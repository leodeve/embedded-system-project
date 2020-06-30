package com.android.on_boardsystemproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.UUID;

public class ManualMode extends AppCompatActivity {

    Button btngauche, btndroite, btnhaut, btnbas, btncentre, btnon, btnoff, btnDis;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //receive the address of the bluetooth device
        Intent intent = getIntent();
        address = intent.getStringExtra(DeviceList.EXTRA_ADDRESS);

        setContentView(R.layout.activity_manual_mode);

        //call the widgets
        btngauche = (Button) findViewById(R.id.btngauche);
        btndroite = (Button) findViewById(R.id.btndroite);
        btnhaut = (Button) findViewById(R.id.btnhaut);
        btnbas = (Button) findViewById(R.id.btnbas);
        btncentre = (Button) findViewById(R.id.btncentre);
        btnon = (Button) findViewById(R.id.btnon);
        btnoff = (Button) findViewById(R.id.btnoff);
        btnDis = (Button) findViewById(R.id.btnDis);
        TextView textView = findViewById(R.id.textView);
        String Newligne = System.getProperty("line.separator");
        textView.setText("App made by:" + Newligne + "Thomas Chamard" + Newligne + "Léo Chauvin" + Newligne + "I1A2");

        new ConnectBT().execute(); //we call the class that allows us to connect


        btngauche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLeft();
            }
        });
        btnhaut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goUp();
            }
        });
        btndroite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRight();
            }
        });
        btnbas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDown();
            }
        });
        btncentre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });
        btnon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {turnOn();
            }
        });
        btnoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {turnOff();
            }
        });
        btnDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnect(); //close connection
            }
        });
    }


    private class ConnectBT extends AsyncTask<Void, Void, Void>  //UI thread (fil d'interface utilisateur)
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(ManualMode.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            } catch (IOException e) {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);
            if (!ConnectSuccess) {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish(); //return to the first layout
            } else {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }

    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }


    private void goUp() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("a".toString().getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void goRight() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("b".toString().getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void goDown() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("c".toString().getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void goLeft() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("d".toString().getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void stop() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("e".toString().getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void turnOn() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("f".toString().getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void turnOff() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("g".toString().getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void disconnect() {
        if (btSocket != null) //if the btSocket is busy
        {
            try {
                btSocket.close(); //close connection
            } catch (IOException e) {
                msg("Error");
            }
        }
        finish(); //return to the first layout
    }
}
